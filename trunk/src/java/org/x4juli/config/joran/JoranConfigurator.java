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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.Level;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.x4juli.config.AbstractConfigurator;
import org.x4juli.config.joran.action.*;
import org.x4juli.config.joran.spi.*;
import org.x4juli.global.spi.Component;
import org.x4juli.global.spi.ErrorItem;
import org.x4juli.global.spi.ExtendedFilter;
import org.x4juli.global.spi.ExtendedHandler;
import org.x4juli.global.spi.ExtendedLogger;
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
    public void doConfigure(final URL url, final LoggerRepository repository,
            final LoggerRepository parentLoggerRepository) {
        ParseAction action = new ParseAction() {
            public void parse(final SAXParser parser, final DefaultHandler handler)
                    throws SAXException, IOException {
                parser.parse(url.toString(), handler);
            }
        };
        doConfigure(action, repository, parentLoggerRepository);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void doConfigure(final InputStream stream, final LoggerRepository repository,
            final LoggerRepository parentLoggerRepository) {
        ParseAction action = new ParseAction() {
            public void parse(final SAXParser parser, final DefaultHandler handler)
                    throws SAXException, IOException {
                parser.parse(stream, handler);
            }
        };
        doConfigure(action, repository, parentLoggerRepository);
    }

    protected void doConfigure(final ParseAction action, final LoggerRepository repository,
            final LoggerRepository parentLoggerRepository) {
        // This line is needed here because there is logging from inside this method.
        this.repository = repository;
        selfInitialize(this.repository);

        ExecutionContext ec = getExecutionContext();
        List errorList = ec.getErrorList();

        if (parentLoggerRepository != null) {
            inheritConfig(parentLoggerRepository);
        }
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

    /**
     * @param repositoryToInit
     */
    protected void selfInitialize(final LoggerRepository repositoryToInit) {
        final boolean isInherited = repositoryToInit.isInherited();
        RuleStore rs = new SimpleRuleStore(repositoryToInit);
        Component theAction = new ConfigurationAction(isInherited);
        theAction.setLoggerRepository(repositoryToInit);
        rs.addRule(new Pattern("configuration"), (Action) theAction);

        theAction = new ParamAction(false);
        theAction.setLoggerRepository(repositoryToInit);
        rs.addRule(new Pattern("configuration/param"), (Action) theAction);

        theAction = new SubstitutionPropertyAction(isInherited);
        theAction.setLoggerRepository(repositoryToInit);
        rs.addRule(new Pattern("configuration/substitutionProperty"), (Action) theAction);

        theAction = new ParamAction(false);
        theAction.setLoggerRepository(repositoryToInit);
        rs.addRule(new Pattern("configuration/substitutionProperty/param"), (Action) theAction);

        theAction = new RepositoryPropertyAction(false);
        theAction.setLoggerRepository(repositoryToInit);
        rs.addRule(new Pattern("configuration/repositoryProperty"), (Action) theAction);

        theAction = new ParamAction(false);
        theAction.setLoggerRepository(repositoryToInit);
        rs.addRule(new Pattern("configuration/repositoryProperty/param"), (Action) theAction);

        theAction = new ConversionRuleAction(false);
        theAction.setLoggerRepository(repositoryToInit);
        rs.addRule(new Pattern("configuration/conversionRule"), (Action) theAction);

        theAction = new ParamAction(false);
        theAction.setLoggerRepository(repositoryToInit);
        rs.addRule(new Pattern("configuration/conversionRule/param"), (Action) theAction);

        theAction = new PluginAction(false);
        theAction.setLoggerRepository(repositoryToInit);
        rs.addRule(new Pattern("configuration/plugin"), (Action) theAction);

        theAction = new ParamAction(false);
        theAction.setLoggerRepository(repositoryToInit);
        rs.addRule(new Pattern("configuration/plugin/param"), (Action) theAction);

        theAction = new RootLoggerAction(false);
        theAction.setLoggerRepository(repositoryToInit);
        rs.addRule(new Pattern("configuration/root"), (Action) theAction);

        theAction = new ParamAction(false);
        theAction.setLoggerRepository(repositoryToInit);
        rs.addRule(new Pattern("configuration/root/param"), (Action) theAction);

        theAction = new LevelAction(false);
        theAction.setLoggerRepository(repositoryToInit);
        rs.addRule(new Pattern("configuration/root/level"), (Action) theAction);

        theAction = new HandlerRefAction(false);
        theAction.setLoggerRepository(repositoryToInit);
        rs.addRule(new Pattern("configuration/root/handler-ref"), (Action) theAction);

        theAction = new LoggerAction(false);
        theAction.setLoggerRepository(repositoryToInit);
        rs.addRule(new Pattern("configuration/logger"), (Action) theAction);

        theAction = new ParamAction(false);
        theAction.setLoggerRepository(repositoryToInit);
        rs.addRule(new Pattern("configuration/logger/param"), (Action) theAction);

        theAction = new LevelAction(false);
        theAction.setLoggerRepository(repositoryToInit);
        rs.addRule(new Pattern("configuration/logger/level"), (Action) theAction);

        theAction = new HandlerRefAction(false);
        theAction.setLoggerRepository(repositoryToInit);
        rs.addRule(new Pattern("configuration/logger/handler-ref"), (Action) theAction);

        theAction = new HandlerAction(isInherited);
        theAction.setLoggerRepository(repositoryToInit);
        rs.addRule(new Pattern("configuration/handler"), (Action) theAction);

        theAction = new ParamAction(isInherited);
        theAction.setLoggerRepository(repositoryToInit);
        rs.addRule(new Pattern("configuration/handler/param"), (Action) theAction);

        theAction = new HandlerRefAction(isInherited);
        theAction.setLoggerRepository(repositoryToInit);
        rs.addRule(new Pattern("configuration/handler/handler-ref"), (Action) theAction);

        theAction = new FormatterAction(isInherited);
        theAction.setLoggerRepository(repositoryToInit);
        rs.addRule(new Pattern("configuration/handler/formatter"), (Action) theAction);

        theAction = new ParamAction(isInherited);
        theAction.setLoggerRepository(repositoryToInit);
        rs.addRule(new Pattern("configuration/handler/formatter/param"), (Action) theAction);

        theAction = new LevelAction(isInherited);
        theAction.setLoggerRepository(repositoryToInit);
        rs.addRule(new Pattern("configuration/handler/level"), (Action) theAction);
        // rs.addRule(
        // new Pattern("configuration/jndiSubstitutionProperty"),
        // new JndiSubstitutionPropertyAction());
        // TODO Solution for instantiating new Rules.
        // rs.addRule(new Pattern("configuration/newRule"), new NewRuleAction(isInherited));

        joranInterpreter = new Interpreter(rs);
        joranInterpreter.setLoggerRepository(repositoryToInit);

        // The following line adds the capability to parse nested components
        theAction = new NestComponentIA(false);
        theAction.setLoggerRepository(repositoryToInit);
        joranInterpreter.addImplicitAction((ImplicitAction) theAction);
        ExecutionContext ec = getExecutionContext();

        Map omap = ec.getObjectMap();
        omap.put(ActionConst.HANDLER_BAG, new HashMap());
        omap.put(ActionConst.FILTER_CHAIN_BAG, new HashMap());
    }

    /**
     * Inherits the config from the parent loggerrepository to the current configuration objects.
     * Currently handlers and filters are "reused", the loggers are created by the configuration
     * file. This is a expensive operation, it is much better to configure the LoggerRepository
     * directly.
     * 
     * @param source to inherit from.
     * @param ec to obtain the destination objects.
     */
    private void inheritConfig(final LoggerRepository source) {
        // ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        // String hashCode = "null";
        // if (classLoader != null) {
        // hashCode = String.valueOf(classLoader.hashCode());
        // }
        // System.out.println("Inheriting config for ClassLoader[" + hashCode + "]");
        ExecutionContext ec = getExecutionContext();

        // Create temporary maps for the copy
        HashMap sourceHandlerMap = new HashMap();
        HashMap sourceFilterMap = new HashMap();

        // Copy root logger handler and filter
        // Copy the handlers of the logger (if one exits)
        ExtendedLogger logger = source.getRootLogger();

        // Copy the filter of the logger (if one exists)
        ExtendedFilter filter = (ExtendedFilter) logger.getFilter();
        if ((filter != null) && (filter.getName() != null)
                && (!sourceFilterMap.containsKey(filter.getName()))) {
            String filtername = filter.getName();
            sourceFilterMap.put(filtername, filter);
        }
        // Copy the handlers of the logger (if one exits)
        Handler[] handlers = logger.getHandlers();
        if (handlers != null && handlers.length != 0) {
            for (int i = 0; i < handlers.length; i++) {
                ExtendedHandler handler = (ExtendedHandler) handlers[i];
                String handlername = handler.getName();
                if (!sourceHandlerMap.containsKey(handlername)) {
                    // System.out.println("Copy [" + handlername + "] to sourceHandlerMap");
                    sourceHandlerMap.put(handlername, handler);
                }
                filter = (ExtendedFilter) handler.getFilter();
                if ((filter != null) && (filter.getName() != null)
                        && (!sourceFilterMap.containsKey(filter.getName()))) {
                    String filtername = filter.getName();
                    sourceFilterMap.put(filtername, filter);
                }
            }
        }

        // Iterate over all loggers to obtain their filters and handlers
        Iterator loggerIter = source.getCurrentLoggers();
        while (loggerIter.hasNext()) {
            logger = (ExtendedLogger) loggerIter.next();

            // Copy the filter of the logger (if one exists)
            filter = (ExtendedFilter) logger.getFilter();
            if ((filter != null) && (filter.getName() != null)
                    && (!sourceFilterMap.containsKey(filter.getName()))) {
                String filtername = filter.getName();
                sourceFilterMap.put(filtername, filter);
            }
            // Copy the handlers of the logger (if one exits)
            handlers = logger.getHandlers();
            if (handlers == null || handlers.length == 0) {
                continue;
            }
            for (int i = 0; i < handlers.length; i++) {
                ExtendedHandler handler = (ExtendedHandler) handlers[i];
                String handlername = handler.getName();
                if (!sourceHandlerMap.containsKey(handlername)) {
                    // System.out.println("Copy [" + handlername + "] to sourceHandlerMap");
                    sourceHandlerMap.put(handlername, handler);
                }
                filter = (ExtendedFilter) handler.getFilter();
                if ((filter != null) && (filter.getName() != null)
                        && (!sourceFilterMap.containsKey(filter.getName()))) {
                    String filtername = filter.getName();
                    sourceFilterMap.put(filtername, filter);
                }
            }
        }

        // Fill destination bag with their inherited handlers
        if (!sourceHandlerMap.isEmpty()) {
            // System.out.println("!sourceHandlerMap.isEmpty() Size[" + sourceHandlerMap.size() +
            // "]");
            HashMap destMap = (HashMap) ec.getObjectMap().get(ActionConst.HANDLER_BAG);
            destMap.clear();
            destMap.putAll(sourceHandlerMap);
        }

        // Fill destination bag with their inherited filters
        if (!sourceFilterMap.isEmpty()) {
            HashMap destMap = (HashMap) ec.getObjectMap().get(ActionConst.FILTER_CHAIN_BAG);
            destMap.clear();
            destMap.putAll(sourceFilterMap);
        }
    }

    protected interface ParseAction {
        void parse(final SAXParser parser, final DefaultHandler handler) throws SAXException,
                IOException;
    }

}

// EOF JoranConfigurator.java
