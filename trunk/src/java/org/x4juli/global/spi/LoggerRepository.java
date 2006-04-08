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
package org.x4juli.global.spi;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.x4juli.global.plugins.PluginRegistry;
import org.x4juli.global.scheduler.Scheduler;

/**
 * Missing documentation.
 * <p>
 * Logging API as a whole was originally done for <a href="http://logging.apache.org/log4j/">Apache
 * log4j</a>. <b>x4uli</b> is a port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging APIs</a>. All
 * credits for initial idea, design, implementation, documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This file was originally
 * published by <i>Ceki G&uuml;lc&uuml;, Mark Womack</i>. Please use exclusively the <i>appropriate</i>
 * mailing lists for questions, remarks and contribution.
 * </p>
 * 
 * @author Boris Unckel
 * @since 0.7
 */
public interface LoggerRepository {

    /**
     * Add a {@link LoggerRepositoryEventListener} to the repository. The listener will be called
     * when repository events occur.
     * 
     * @since 0.7
     */
    public void addLoggerRepositoryEventListener(LoggerRepositoryEventListener listener);

    /**
     * Remove a {@link LoggerRepositoryEventListener} from the repository.
     * 
     * @since 0.7
     */
    public void removeLoggerRepositoryEventListener(LoggerRepositoryEventListener listener);

    /**
     * Add a {@link LoggerEventListener} to the repository. The listener will be called when
     * repository events occur.
     * 
     * @since 0.7
     */
    public void addLoggerEventListener(LoggerEventListener listener);

    /**
     * Remove a {@link LoggerEventListener} from the repository.
     * 
     * @since 0.7
     */
    public void removeLoggerEventListener(LoggerEventListener listener);

    /**
     * Get the name of this logger repository.
     * 
     * @since 0.7
     */
    public String getName();

    /**
     * A logger repository is a named entity.
     * 
     * @since 0.7
     */
    public void setName(String repoName);

    /**
     * @param logger
     * @since 0.7
     */
    public void emitNoHandlerWarning(ExtendedLogger logger);

    /**
     * @param name
     * @return
     * @since 0.7
     */
    public ExtendedLogger getLogger(String name);

    /**
     * Method to satisfy the <code>java.util.logging.LogManager</code>
     * with getLoggerNames().
     * @return enumeration containing all names as String.
     * 
     * @since 0.7
     */
    public Enumeration getCurrentLoggerNames();

    /**
     * The call of this method should be avoided, but it is
     * needed for the <code>java.util.logging.LogManager.addLogger(Logger)</code>
     * method.
     * @param logger to add.
     * @return true if the logger did not exist before and was added, false if it already
     * exists.
     * @since 0.7
     */
    public boolean addLogger(java.util.logging.Logger logger);

    public LoggerFactory getLoggerFactory();
    
    /**
     * @return
     */
    public ExtendedLogger getRootLogger();

    /**
     * Is the current configuration of the reposiroty in its original (pristine) state?
     * 
     * @since 0.7
     */
    public boolean isPristine();

    /**
     * Set the pristine flag.
     * 
     * @see #isPristine
     * @since 0.7
     */
    public void setPristine(boolean state);

    public ExtendedLogger exists(String name);

    public void shutdown();

    public Iterator getCurrentLoggers();

    public abstract void resetConfiguration();

    public void setLoggerFactory(LoggerFactory loggerFactory);
    
    /**
     * Requests that a handler added event be sent to any registered {@link LoggerEventListener}.
     * 
     * @param logger The logger to which the handler was added.
     * @param handler The handler added to the logger.
     * @since 0.7
     */
    public abstract void fireAddHandlerEvent(ExtendedLogger logger, ExtendedHandler handler);

    /**
     * Requests that a handler removed event be sent to any registered {@link LoggerEventListener}.
     * 
     * @param logger The logger from which the appender was removed.
     * @param handler The appender removed from the logger.
     * @since 0.7
     */
    public abstract void fireRemoveHandlerEvent(ExtendedLogger logger, ExtendedHandler handler);

    /**
     * Requests that a level changed event be sent to any registered {@link LoggerEventListener}.
     * 
     * @param logger The logger which changed levels.
     * @since 0.7
     */
    public abstract void fireLevelChangedEvent(ExtendedLogger logger);

    /**
     * Requests that a configuration changed event be sent to any registered
     * {@link LoggerRepositoryEventListener}.
     * 
     * @since 0.7
     */
    public abstract void fireConfigurationChangedEvent();

    /**
     * Return the PluginRegisty for this LoggerRepository.
     * 
     * @since 0.7
     */
    public PluginRegistry getPluginRegistry();

    /**
     * Return the {@link Scheduler} for this LoggerRepository.
     * 
     * @since 0.7
     */
    public Scheduler getScheduler();

    /**
     * Get the properties specific for this repository.
     * 
     * @since 0.7
     */
    public Map getProperties();

    /**
     * Get the property of this repository.
     * 
     * @since 0.7
     */
    public String getProperty(String key);

    /**
     * Set a property of this repository.
     * 
     * @since 0.7
     */
    public void setProperty(String key, String value);

    /**
     * Errors which cannot be logged, go to the error list
     * 
     * @return List
     * @since 0.7
     */
    public List getErrorList();

    /**
     * Errors which cannot be logged, go to the error list
     * 
     * @param errorItem an ErrorItem to add to the error list
     * @since 0.7
     */
    public void addErrorItem(ErrorItem errorItem);

    /**
     * A LoggerRepository can also act as a store for various objects used by log4j components.
     * 
     * @return The object stored under 'key'.
     * @since 0.7
     */
    public Object getObject(String key);

    /**
     * Store an object under 'key'.
     * 
     * @param key
     * @param value
     */
    public void putObject(String key, Object value);

}

// EOF LoggerRepository.java
