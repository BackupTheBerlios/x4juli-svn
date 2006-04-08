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
package org.x4juli.config.joran;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.x4juli.config.AbstractConfigurator;
import org.x4juli.config.joran.action.*;
import org.x4juli.config.joran.spi.*;
import org.x4juli.global.spi.ErrorItem;
import org.x4juli.global.spi.LoggerRepository;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A JoranConfigurator instance should not be used more than once to configure a LoggerRepository.
 * <p>
 * Logging API as a whole was originally done for <a href="http://logging.apache.org/log4j/">Apache
 * log4j</a>. <b>x4uli</b> is a port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging APIs</a>. All
 * credits for initial idea, design, implementation, documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This file was originally
 * published by <i>Curt Arnold, Ceki G&uuml;lc&uuml;</i>. Please use exclusively the <i>appropriate</i>
 * mailing lists for questions, remarks and contribution.
 * </p>
 * 
 * @author Boris Unckel
 * @since 0.7
 */
public class JoranConfigurator extends AbstractConfigurator {

    private Interpreter joranInterpreter;

    private LoggerRepository repository;

    private boolean listAppnderAttached = false;

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public List getErrorList() {
        return getExecutionContext().getErrorList();
    }

    public ExecutionContext getExecutionContext() {
        return joranInterpreter.getExecutionContext();
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void doConfigure(final URL url, final LoggerRepository repository) {
        ParseAction action = new ParseAction() {
            public void parse(final SAXParser parser, final DefaultHandler handler) throws SAXException, IOException {
                parser.parse(url.toString(), handler);
            }
        };
        doConfigure(action, repository);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void doConfigure(final InputStream stream, final LoggerRepository repository) {
        ParseAction action = new ParseAction() {
            public void parse(final SAXParser parser, final DefaultHandler handler) throws SAXException, IOException {
                parser.parse(stream, handler);
            }
        };
        doConfigure(action, repository);
    }

    protected void doConfigure(final ParseAction action, final LoggerRepository repository) {
        // This line is needed here because there is logging from inside this method.
        this.repository = repository;
        selfInitialize(this.repository);

        ExecutionContext ec = joranInterpreter.getExecutionContext();
        List errorList = ec.getErrorList();

        SAXParser saxParser = null;
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setValidating(false);
            spf.setNamespaceAware(true);
            saxParser = spf.newSAXParser();
        } catch (Exception pce) {
            final String errMsg = "Parser configuration error occured";
            getLogger(repository).log(Level.SEVERE, errMsg, pce);
            ec.addError(new ErrorItem(errMsg, pce));
            return;
        }

        JoranDocument document = new JoranDocument(errorList, repository);

        try {
            action.parse(saxParser, document);
        } catch (IOException ie) {
            final String errMsg = "I/O error occured while parsing xml file";
            getLogger(repository).log(Level.SEVERE, errMsg, ie);
            ec.addError(new ErrorItem(errMsg, ie));
        } catch (Exception ex) {
            final String errMsg = "Problem parsing XML document. See previously reported errors. Abandoning all further processing.";
            getLogger(repository).log(Level.SEVERE, errMsg, ex);
            errorList.add(new ErrorItem(errMsg));
            return;
        }

        ec.pushObject(repository);

        try {
            attachListAppender(repository);

            document.replay(joranInterpreter);

            getLogger(repository).finer("Finished parsing.");
        } catch (SAXException e) {
            // all exceptions should have been recorded already.
        } finally {
            detachListAppender(repository);
        }

    }

    protected void selfInitialize(final LoggerRepository repository) {
        RuleStore rs = new SimpleRuleStore(repository);
        rs.addRule(new Pattern("configuration"), new ConfigurationAction());
        rs.addRule(new Pattern("configuration/substitutionProperty"),
                new SubstitutionPropertyAction());
        rs.addRule(new Pattern("configuration/repositoryProperty"), new RepositoryPropertyAction());
        rs.addRule(new Pattern("configuration/conversionRule"), new ConversionRuleAction());
        rs.addRule(new Pattern("configuration/plugin"), new PluginAction());
        rs.addRule(new Pattern("configuration/logger"), new LoggerAction());
        rs.addRule(new Pattern("configuration/logger/level"), new LevelAction());
        rs.addRule(new Pattern("configuration/root"), new RootLoggerAction());
        rs.addRule(new Pattern("configuration/root/level"), new LevelAction());
        rs.addRule(new Pattern("configuration/logger/handler-ref"), new HandlerRefAction());
        rs.addRule(new Pattern("configuration/root/handler-ref"), new HandlerRefAction());
        rs.addRule(new Pattern("configuration/handler"), new HandlerAction());
        rs.addRule(new Pattern("configuration/handler/handler-ref"), new HandlerRefAction());
        rs.addRule(new Pattern("configuration/handler/formatter"), new FormatterAction());
        rs.addRule(new Pattern("configuration/handler/level"), new LevelAction());
        // rs.addRule(
        // new Pattern("configuration/jndiSubstitutionProperty"),
        // new JndiSubstitutionPropertyAction());
        rs.addRule(new Pattern("configuration/newRule"), new NewRuleAction());
        rs.addRule(new Pattern("*/param"), new ParamAction());

        joranInterpreter = new Interpreter(rs);
        joranInterpreter.setLoggerRepository(repository);

        // The following line adds the capability to parse nested components
        joranInterpreter.addImplicitAction(new NestComponentIA());
        ExecutionContext ec = joranInterpreter.getExecutionContext();

        Map omap = ec.getObjectMap();
        omap.put(ActionConst.HANDLER_BAG, new HashMap());
        omap.put(ActionConst.FILTER_CHAIN_BAG, new HashMap());
    }

    protected interface ParseAction {
        void parse(final SAXParser parser, final DefaultHandler handler) throws SAXException,
                IOException;
    }

}

// EOF JoranConfigurator.java
