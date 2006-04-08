/*
 * Copyright 1999,2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.x4juli.config.joran.spi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.Vector;
import java.util.logging.Level;

import org.x4juli.config.joran.action.Action;
import org.x4juli.config.joran.action.ImplicitAction;
import org.x4juli.global.resources.MessageProperties;
import org.x4juli.global.spi.Component;
import org.x4juli.global.spi.ErrorItem;
import org.x4juli.global.spi.ExtendedLogger;
import org.x4juli.global.spi.LoggerRepository;
import org.xml.sax.Attributes;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * <id>Interpreter</id> is Joran's main driving class. It extends SAX
 * {@link org.xml.sax.helpers.DefaultHandler DefaultHandler} which invokes various
 * {@link Action actions} according to predefined patterns.
 * 
 * <p>
 * Patterns are kept in a {@link RuleStore} which is programmed to store and then later produce the
 * applicable actions for a given pattern.
 * 
 * <p>
 * The pattern corresponding to a top level &lt;a&gt; element is the string <id>"a"</id>.
 * 
 * <p>
 * The pattern corresponding to an element &lt;b&gt; embeded within a top level &lt;a&gt; element is
 * the string <id>"a/b"</id>.
 * 
 * <p>
 * The pattern corresponding to an &lt;b&gt; and any level of nesting is "&#42;/b. Thus, the &#42;
 * character placed at the beginning of a pattern serves as a wildcard for the level of nesting.
 * 
 * Conceptually, this is very similar to the API of commons-digester. Joran offers several small
 * advantages. First and foremost, it offers support for implicit actions which result in a
 * significant leap in flexibility. Second, in our opinion better error reporting capability. Third,
 * it is self-reliant. Last but not least, joran is quite tiny and is expected to remain so.
 * <p>
 * Logging API as a whole was originally done for <a href="http://logging.apache.org/log4j/">Apache
 * log4j</a>. <b>x4uli</b> is a port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging APIs</a>. All
 * credits for initial idea, design, implementation, documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This file was originally
 * published by <i>Ceki G&uuml;lcu&uuml;</i>. Please use exclusively the <i>appropriate</i>
 * mailing lists for questions, remarks and contribution.
 * </p>
 * 
 * @author Boris Unckel
 * @since 0.7
 */
public class Interpreter extends DefaultHandler implements Component {
    
    private static List EMPTY_LIST = new Vector(0);
    private RuleStore ruleStore;
    private ExecutionContext ec;
    private ArrayList implicitActions;
    Pattern pattern;
    Locator locator;

    // The entity resolver is only needed in order to be compatible with
    // XML files written for DOMConfigurator containing the following DOCTYPE
    // <!DOCTYPE log4j:configuration SYSTEM "log4j.dtd">
    private EntityResolver entityResolver;
    
    private LoggerRepository repository;
    
    /**
     * The <id>actionListStack</id> contains a list of actions that are
     * executing for the given XML element.
     *
     * A list of actions is pushed by the {link #startElement} and popped by
     * {@link #endElement}.
     *
     */
    Stack actionListStack;

    /**
     * If the skip nested is set, then we skip all its nested elements until
     * it is set back to null at when the element's end is reached.
     */
    Pattern skip = null;
    
    public Interpreter(RuleStore rs) {
      ruleStore = rs;
      ec = new ExecutionContext(this);
      implicitActions = new ArrayList(3);
      pattern = new Pattern();
      actionListStack = new Stack();
    }

    public ExecutionContext getExecutionContext() {
      return ec;
    }

    public void startDocument() {
    }

    public void startElement(
            final String namespaceURI, final String localName, final String qName, final Attributes atts) {

      String tagName = getTagName(localName, qName);
      
      //LogLog.debug("in startElement <" + tagName + ">");

      pattern.push(tagName);
      
      List applicableActionList = getApplicableActionList(pattern, atts);

      if (applicableActionList != null) {
        actionListStack.add(applicableActionList);
        callBeginAction(applicableActionList, tagName, atts);
      } else {
        actionListStack.add(EMPTY_LIST);

        String errMsg =
          "no applicable action for <" + tagName + ">, current pattern is ["
          + pattern+"]";
        getLogger().warning(errMsg);
        ec.addError(new ErrorItem(errMsg));
      }
    }

    public void endElement(String namespaceURI, String localName, String qName) {
      List applicableActionList = (List) actionListStack.pop();

      if(skip != null) {
        //System.err.println("In End, pattern is "+pattern+", skip pattern "+skip);
        if(skip.equals(pattern)) {
          getLogger().log(Level.INFO, "Normall processing will continue with the next element. Current pattern is <{0}>", pattern);
          skip = null;
        } else {
          getLogger().log(Level.FINER, "Skipping invoking end() method for <{0}>.", pattern);
        }
      } else if (applicableActionList != EMPTY_LIST) {
        callEndAction(applicableActionList, getTagName(localName, qName));
      }

      // given that we always push, we must also pop the pattern
      pattern.pop();
    }

    public Locator getLocator() {
      return locator;
    }

    public void setDocumentLocator(final Locator l) {
      locator = l;
    }

    String getTagName(final String localName, final String qName) {
      String tagName = localName;

      if ((tagName == null) || (tagName.length() < 1)) {
        tagName = qName;
      }

      return tagName;
    }

    public void addImplicitAction(final ImplicitAction ia) {
      implicitActions.add(ia);
    }

    /**
     * Check if any implicit actions are applicable. As soon as an applicable
     * action is found, it is returned. Thus, the returned list will have at most
     * one element.
     */
    List lookupImplicitAction(final Pattern pattern, final Attributes attributes, final ExecutionContext ec) {
      int len = implicitActions.size();

      for (int i = 0; i < len; i++) {
        ImplicitAction ia = (ImplicitAction) implicitActions.get(i);

        if (ia.isApplicable(pattern, attributes, ec)) {
          List actionList = new ArrayList(1);
          actionList.add(ia);

          return actionList;
        }
      }

      return null;
    }

    /**
     * Return the list of applicable patterns for this
    */
    List getApplicableActionList(final Pattern pattern, final Attributes attributes) {
      List applicableActionList = ruleStore.matchActions(pattern);

      //logger.debug("set of applicable patterns: " + applicableActionList);
      if (applicableActionList == null) {
        applicableActionList = lookupImplicitAction(pattern, attributes, ec);
      }

      return applicableActionList;
    }

    void callBeginAction(
      List applicableActionList, String tagName, Attributes atts) {
      if (applicableActionList == null) {
        return;
      }

      if(skip != null) {
        getLogger().log(Level.FINER, "Skipping invoking begin() method for <{0}>.", pattern);
        return;
      }
      
      Iterator i = applicableActionList.iterator();

      while (i.hasNext()) {
        Action action = (Action) i.next();

        // now let us invoke the action. We catch and report any eventual 
        // exceptions
        try {
          action.begin(ec, tagName, atts);
        } catch( ActionException ae) {
          switch(ae.getSkipCode()) {
          case ActionException.SKIP_CHILDREN:
            skip = (Pattern) pattern.clone();
            break;
          case ActionException.SKIP_SIBLINGS:
            skip = (Pattern) pattern.clone();
            // pretend the exception came from one level up. This will cause
            // all children and following siblings elements to be skipped
            skip.pop();
            break;
          }
          getLogger().log(Level.INFO, "Skip pattern set to <{0}>", skip);
        } catch (Exception e) {
          //TODO print Stacktrace entfernen.
          e.printStackTrace();
          skip = (Pattern) pattern.clone();
          getLogger().log(Level.INFO, "Skip pattern set to <{0}>", skip);
          ec.addError(new ErrorItem("Exception in Action for tag <"+tagName+">", e));
        }
      }
    }

    void callEndAction(final List applicableActionList, final String tagName) {
      if (applicableActionList == null) {
        return;
      }

      //logger.debug("About to call end actions on node: <" + localName + ">");
      Iterator i = applicableActionList.iterator();

      while (i.hasNext()) {
        Action action = (Action) i.next();
        // now let us invoke the end method of the action. We catch and report 
        // any eventual exceptions
        try {
          action.end(ec, tagName);
        } catch( ActionException ae) {
          switch(ae.getSkipCode()) {
          case ActionException.SKIP_CHILDREN:
            // after end() is called there can't be any children
            break;
          case ActionException.SKIP_SIBLINGS:
            skip = (Pattern) pattern.clone();
            skip.pop();
            break;
          }
          getLogger().log(Level.INFO, "Skip pattern set to <{0}>", skip);
        } catch(Exception e) {
          ec.addError(new ErrorItem("Exception in Action for tag <"+tagName+">", e));
          skip = (Pattern) pattern.clone();
          skip.pop(); // induce the siblings to be skipped
          getLogger().log(Level.INFO,"Skip pattern set to <{0}>.", skip);
        }
      }
    }

    public RuleStore getRuleStore() {
      return ruleStore;
    }

    public void setRuleStore(final RuleStore ruleStore) {
      this.ruleStore = ruleStore;
    }

//    /**
//     * Call the finish methods for all actions. Unfortunately, the endDocument
//     * method is not called in case of errors in the XML document, which
//     * makes endDocument() pretty damn useless.
//     */
//    public void endDocument() {
//      Set arrayListSet = ruleStore.getActionSet();
//      Iterator iterator = arrayListSet.iterator();
//      while(iterator.hasNext()) {
//        ArrayList al = (ArrayList) iterator.next();
//        for(int i = 0; i < al.size(); i++) {
//           Action a = (Action) al.get(i);
//           a.endDocument(ec);
//        }
//      }
//    }

    public void error(final SAXParseException spe) throws SAXException {
      ec.addError(new ErrorItem("Parsing error", spe));
      getLogger().log(Level.SEVERE,
        "Parsing problem on line " + spe.getLineNumber() + " and column "
        + spe.getColumnNumber(), spe);
    }

    public void fatalError(final SAXParseException spe) throws SAXException {
      ec.addError(new ErrorItem("Parsing fatal error", spe));
      getLogger().log(Level.SEVERE,
        "Parsing problem on line " + spe.getLineNumber() + " and column "
        + spe.getColumnNumber(), spe);
    }

    public void warning(final SAXParseException spe) throws SAXException {
      ec.addError(new ErrorItem("Parsing warning", spe));
      getLogger().log(Level.WARNING,
        "Parsing problem on line " + spe.getLineNumber() + " and column "
        + spe.getColumnNumber(), spe);
    }

    public void endPrefixMapping(final java.lang.String prefix) {
    }

    public void ignorableWhitespace(final char[] ch, final int start, final int length) {
    }

    public void processingInstruction(
            final java.lang.String target, final java.lang.String data) {
    }

    public void skippedEntity(final java.lang.String name) {
    }

    public void startPrefixMapping(
            final java.lang.String prefix, final java.lang.String uri) {
    }

    public EntityResolver getEntityResolver() {
      return entityResolver;
    }
    
    public void setEntityResolver(final EntityResolver entityResolver) {
      this.entityResolver = entityResolver;
    }

    
    /**
     * If a specific entityResolver is set for this Interpreter instance, then 
     * we use it to resolve entities. Otherwise, we use the default implementation
     * offered by the super class.
     * 
     * <p>Due to inexplicable voodoo, the original resolveEntity method in 
     * org.xml.sax.helpers.DefaultHandler declares throwing an IOException, 
     * whereas the org.xml.sax.helpers.DefaultHandler class included in
     * JDK 1.4 masks this exception.
     * 
     * <p>In order to compile under JDK 1.4, we are forced to mask the IOException
     * as well. Since its signatures varies, we cannot call our super class' 
     * resolveEntity method. We are forced to implement the default behavior 
     * ourselves, which in this case, is just returning null.
     * 
     */
    public InputSource resolveEntity(final String publicId, final String systemId) throws SAXException {
      if(entityResolver == null) {
        // the default implementation is to return null
        return null;
      } else {
        try {
          return entityResolver.resolveEntity(publicId, systemId);
        } catch(IOException ioe) {
          // fall back to the default "implementation"
          return null;
        }
      }
    }

    public void setLoggerRepository(final LoggerRepository repository) {
      this.repository = repository;
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public MessageProperties getMessageProperties() {
        return null;
    }

    protected ExtendedLogger getLogger() {
      if(repository != null) {
        return repository.getLogger(this.getClass().getName());
      } else {
         //TODO Internen Logger bereitstellen
         //return SimpleULogger.getLogger(this.getClass().getName());
         return null;
      }
    } 

}

// EOF Interpreter.java
