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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Stack;
import java.util.Vector;

import org.x4juli.global.helper.OptionConverter;
import org.x4juli.global.spi.ErrorItem;
import org.xml.sax.Locator;

/**
 * The ExecutionContext contains the contextual state of a Joran parsing 
 * session. {@link org.apache.log4j.joran.action.Action Actions} depend on this 
 * context to exchange and store information.
 * <p>
 * Logging API as a whole was originally done for <a href="http://logging.apache.org/log4j/">Apache
 * log4j</a>. <b>x4uli</b> is a port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging APIs</a>. All
 * credits for initial idea, design, implementation, documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This file was originally
 * published by <i>Ceki G&uuml;lc&uuml;</i>. Please use exclusively the <i>appropriate</i> mailing
 * lists for questions, remarks and contribution.
 * </p>
 * 
 * @author Boris Unckel
 * @since 0.7
 */
public class ExecutionContext {
    
    private Stack objectStack;
    private Map objectMap;
    private List errorList;
    private Properties substitutionProperties;
    private Interpreter joranInterpreter;

    public ExecutionContext(final Interpreter joranInterpreter) {
      this.joranInterpreter = joranInterpreter;
      objectStack = new Stack();
      objectMap = new HashMap(5);
      errorList = new Vector();
      substitutionProperties = new Properties();
    }

//    /**
//     * Clear the internal structures for reuse of the execution context
//     * 
//     */
//    public void clear() {
//      objectStack.clear();
//      objectMap.clear();
//      errorList.clear();
//      substitutionProperties.clear();
//    }
    
    public void addError(final ErrorItem errorItem) {
      Locator locator = joranInterpreter.getLocator();

      if (locator != null) {
        errorItem.setLineNumber(locator.getLineNumber());
        errorItem.setColNumber(locator.getColumnNumber());
      }

      errorList.add(errorItem);
    }

    public List getErrorList() {
      return errorList;
    }

    public Locator getLocator() {
      return joranInterpreter.getLocator();
    }

    public Interpreter getJoranInterpreter() {
      return joranInterpreter;
    }

    public Stack getObjectStack() {
      return objectStack;
    }

    public Object peekObject() {
      return objectStack.peek();
    }

    public void pushObject(final Object o) {
      objectStack.push(o);
    }

    public Object popObject() {
      return objectStack.pop();
    }

    public Object getObject(final int i) {
      return objectStack.get(i);
    }

    public Map getObjectMap() {
      return objectMap;
    }

    /**
     * Add a property to the properties of this execution context.
     * If the property exists already, it is overwritten.
     */
    public void addProperty(final String key, final String value) {
      if(key == null || value == null) {
        return;
      }
      String trimvalue = value.trim();
      substitutionProperties.put(key, trimvalue);
    }

   public void addProperties(final Properties props) {
      if(props == null) {
        return;
      }
      Iterator i = props.keySet().iterator();
      while(i.hasNext()) {
        String key = (String) i.next();
        addProperty(key, props.getProperty(key));
      }
    }
   
    public String getSubstitutionProperty(final String key) {
      return substitutionProperties.getProperty(key);
    }

    public String subst(final String value) {
      if(value == null) {
        return null;
      }
      return OptionConverter.substVars(value, substitutionProperties);
    }

}

// EOF ExecutionContext.java
