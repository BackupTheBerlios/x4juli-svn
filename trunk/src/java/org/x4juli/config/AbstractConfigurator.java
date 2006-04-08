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
package org.x4juli.config;

import java.util.List;
import java.util.logging.Handler;

import org.x4juli.formatter.PatternFormatter;
import org.x4juli.global.Constants;
import org.x4juli.global.spi.Configurator;
import org.x4juli.global.spi.ErrorItem;
import org.x4juli.global.spi.ExtendedFormatter;
import org.x4juli.global.spi.ExtendedHandler;
import org.x4juli.global.spi.ExtendedLogger;
import org.x4juli.global.spi.LoggerRepository;
import org.x4juli.handlers.ConsoleHandler;
import org.x4juli.handlers.ListHandler;

/**
 * Code useful to most configurators.
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
public abstract class AbstractConfigurator implements Configurator {
    /**
     * Defining this value makes x4juli print x4juli-internal debug statements.
     * <p> 
     * The value of this string is <b>log4j.debug</b>.
     * 
     * <p>Note that the search for all option names is case sensitive.  
     * */ 
     public static final String DEBUG_KEY = "org.x4juli.debug";

     protected ExtendedLogger getLogger(LoggerRepository repository) {
         return repository.getLogger(this.getClass().getName());
       }
       
       protected void addError(ErrorItem errorItem) {
         getErrorList().add(errorItem);
       }
       
       abstract public List getErrorList();
       
       /**
        * Attach a list appender which will be used to collect the logging events
        * generated by log4j components, including the JoranConfigurator. These
        * events will later be output when {@link #detachListAppender} method
        * is called.
        * 
        * @param repository
        */
       protected void attachListAppender(LoggerRepository repository) {
         ExtendedLogger ll = repository.getLogger(Constants.X4JULI_PACKAGE_NAME);
         ListHandler handler = new ListHandler();
         handler.setLoggerRepository(repository);
         handler.setName(Constants.TEMP_LIST_HANDLER_NAME);
         handler.activateOptions();
         ll.addHandler((Handler)handler);
         ll.setUseParentHandlers(false);
       }
       
       /**
        * Output the previously collected events using the current log4j 
        * configuration. When that is completed, cluse and detach the
        * ListHandler previously created by {@link #attachListAppender}.
        * 
        * @param repository
        */
       protected void detachListAppender(LoggerRepository repository) {

         ExtendedLogger ll = repository.getLogger(Constants.X4JULI_PACKAGE_NAME);
         
         // FIXME: What happens if the users wanted to set the additivity flag
         // for "org.apahce.log4j" to false in the config file? We are now 
         // potentially overriding her wishes but I don't see any other way.
         ll.setUseParentHandlers(true);
         
         ExtendedHandler[] myHandler = (ExtendedHandler[]) ll.getHandlers();
         ListHandler listAppender = null;
         for (int i = 0; i < myHandler.length; i++) {
            ExtendedHandler handler = myHandler[i];
            if(handler.getName().equals(Constants.TEMP_LIST_HANDLER_NAME)){
                listAppender = (ListHandler) handler;
            }
        }
         
         if(listAppender == null) {
           String errMsg = "Could not find appender "+Constants.TEMP_LIST_HANDLER_NAME;
           getLogger(repository).severe(errMsg);
           addError(new ErrorItem(errMsg));
           return;
         }

         //TODO Publizieren der LogEvents
//         List eventList = listAppender.getList();
//         int size = eventList.size();
//         for(int i = 0; i < size; i++) {
//           LoggingEvent event = (LoggingEvent) eventList.get(i);
//           Logger xLogger = event.getLogger();
//           if (event.getLevel().isGreaterOrEqual(xLogger.getEffectiveLevel())) {
//             xLogger.callAppenders(event);
//           }
//         }
//         listAppender.clearList();
//         listAppender.close();
//         ll.removeAppender(listAppender);
       }
       
       static public void attachTemporaryConsoleAppender(final LoggerRepository repository) {
         ExtendedLogger ll = repository.getLogger(Constants.X4JULI_PACKAGE_NAME);
         
         ConsoleHandler handler = new ConsoleHandler();
         handler.setLoggerRepository(repository);
         PatternFormatter myFormatter = new PatternFormatter("X4JULI-INTERNAL: %d %level [%t] %c#%M:%L)- %m%n");
         handler.setFormatter((ExtendedFormatter)myFormatter);
         handler.setName(Constants.TEMP_CONSOLE_HANDLER_NAME);
         handler.setImmediateFlush(true);
         handler.activateOptions();
         ll.addHandler(handler);
       }

       static public void detachTemporaryConsoleAppender(final LoggerRepository repository, final List errorList) {

         ExtendedLogger ll = repository.getLogger(Constants.X4JULI_PACKAGE_NAME);
         ExtendedHandler[] myHandler = (ExtendedHandler[]) ll.getHandlers();
         ConsoleHandler consoleHandler = null;
         for (int i = 0; i < myHandler.length; i++) {
            ExtendedHandler handler = myHandler[i];
            if(handler.getName().equals(Constants.TEMP_CONSOLE_HANDLER_NAME)){
                consoleHandler = (ConsoleHandler) handler;
            }
        }
         if (consoleHandler == null) {
           String errMsg =
             "Could not find appender " + Constants.TEMP_CONSOLE_HANDLER_NAME;
           errorList.add(new ErrorItem(errMsg));
           return;
         }
         consoleHandler.close();
         ll.removeHandler(consoleHandler);
       }
       
       /**
        * Dump any errors on System.err.
        */
       public void dumpErrors() {
         List errorList = getErrorList();
         for(int i = 0; i < errorList.size(); i++) {
           ErrorItem ei = (ErrorItem) errorList.get(i);
           ei.dump(System.err);
         }
       }
}

// EOF AbstractConfigurator.java