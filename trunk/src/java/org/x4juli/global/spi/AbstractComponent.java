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

import java.security.Permission;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LoggingPermission;

import org.x4juli.global.Constants;
import org.x4juli.global.resources.MessageProperties;
import org.x4juli.logger.NOPLogger;

/**
 * The basic implementation for all x4juli components.
 * <p>
 * Logging API as a whole was originally done for <a href="http://logging.apache.org/log4j/">Apache
 * log4j</a>. <b>Juli</b> is a port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging APIs</a>. All
 * credits for initial idea, design, implementation, documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This file was originally
 * published by <i>Ceki G&uuml;lc&uuml;</i>. Please use exclusively the <i>appropriate</i> mailing
 * lists for questions, remarks and contribution.
 * </p>
 * 
 * @author Boris Unckel
 * @since 0.5
 */
public abstract class AbstractComponent implements Component {

    // -------------------------------------------------------------- Variables

    static final Level INTERNAL_LOG_LEVEL;

    /**
     * The LoggingPermission for configuration changes.
     */
    static final Permission loggingPermission = new LoggingPermission("control", null);

    /**
     * Contains objects for this component.
     */
    protected LoggerRepository repository;

    private int errorCount = 0;

    private ExtendedLogger logger;

    // ----------------------------------------------------------- Constructors
    static {
        Level newLevel = Level.WARNING;
        try {
            String loglevel = System.getProperty("org.x4juli.internal.level");
            if (loglevel != null) {
                newLevel = Level.parse(loglevel);
            }
        } catch (Throwable t) {
            newLevel = Level.WARNING;
        }
        INTERNAL_LOG_LEVEL = newLevel;
    }

    /**
     * Default, NOP Constructor.
     */
    public AbstractComponent() {
        super();
    }

    // --------------------------------------------------------- Public Methods

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void setLoggerRepository(final LoggerRepository repository) {
        if (this.repository == null) {
            this.repository = repository;
        } else if (this.repository != repository) {
            throw new LogIllegalStateException("Repository has been already set");
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.5
     */
    public MessageProperties getMessageProperties() {
        return MessageProperties.PROPERTIES_GLOBAL_SPI;
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * If there is an SecurityManager, the LogginPermission is checked.
     * @throws SecurityException
     */
    protected void checkAccess() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager == null) {
            return;
        }
        securityManager.checkPermission(loggingPermission);
    }

    /**
     * Return the {@link LoggerRepository} this component is attached to.
     * 
     * @return Owning LoggerRepository
     * @since 0.7
     */
    protected LoggerRepository getLoggerRepository() {
        return this.repository;
    }

    /**
     * Called by derived classes when they deem that the component has recovered from an erroneous
     * state.
     * 
     * @since 0.5
     */
    protected void resetErrorCount() {
        this.errorCount = 0;
    }

    /**
     * Return an instance specific logger to be used by the component itself. This logger is not
     * intended to be accessed by the end-user, hence the protected keyword.
     * 
     * <p>
     * This logger always sends output to an <code>ConsoleHandler</code>, which outputs to
     * System.err
     * </p>
     * 
     * @return A Logger instance.
     * @since 0.5
     */
    protected ExtendedLogger getLogger() {
        if (this.logger == null) {
            MessageProperties messageProperties = getMessageProperties();
            String resource = null;
            if (messageProperties != null) {
                resource = messageProperties.getValueAsString();
            }
            this.logger = this.repository.getLogger(this.getClass().getName(), resource);
        }
        return this.logger;
    }

    /**
     * Frequently called methods in juli components can invoke this method in order to avoid
     * flooding the output when logging lasting error conditions.
     * 
     * @return a regular logger, or a NOPLogger if called too frequently.
     * @since 0.5
     */
    protected ExtendedLogger getNonFloodingLogger() {
        if (this.errorCount++ >= Constants.ERROR_COUNT_LIMIT) {
            return NOPLogger.NOP_LOGGER;
        } else {
            return getLogger();
        }
    }

    /**
     * Get an property value out of the <code>LogManager</code> by name.
     * 
     * @param name of the parameter to be obtained.
     * @param defaultValue to return if no value has been found.
     * @return the value of the given property, if null defaultValue.
     */
    protected String getProperty(final String name, final String defaultValue) {
        // String value = this.manager.getProperty(name);
        String value = this.repository.getProperty(name);
        if (value == null) {
            value = defaultValue;
        } else {
            value = value.trim();
        }
        return value;
    }

    /**
     * Get an property value out of the <code>LogManager</code> by name.
     * 
     * @param name of the parameter to be obtained.
     * @param defaultValue to return if no value has been found.
     * @return the value of the given property, if null defaultValue.
     */
    protected int getProperty(final String name, final int defaultValue) {
        String value = this.repository.getProperty(name);
        int ret;
        if (value == null) {
            ret = defaultValue;
        } else {
            try {
                ret = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                getLogger().log(Level.WARNING, MessageText.Property_has_not_an_int_value,
                        new Object[] { name, value, new Integer(defaultValue) });
                ret = defaultValue;
            }
        }
        return ret;
    }

    /**
     * Get an property value out of the <code>LogManager</code> by name.
     * 
     * @param name of the parameter to be obtained.
     * @param defaultValue to return if no value has been found.
     * @return the value of the given property, if null defaultValue.
     */
    protected long getProperty(final String name, final long defaultValue) {
        String value = this.repository.getProperty(name);
        long ret;
        if (value == null) {
            ret = defaultValue;
        } else {
            try {
                ret = Long.parseLong(value);
            } catch (NumberFormatException e) {
                getLogger().log(Level.WARNING, MessageText.Property_has_not_an_int_value,
                        new Object[] { name, value, new Long(defaultValue) });
                ret = defaultValue;
            }
        }
        return ret;
    }

    /**
     * Get an property value out of the <code>LogManager</code> by name.
     * 
     * @param name of the parameter to be obtained.
     * @param defaultValue to return if no value has been found.
     * @return the value of the given property, if null defaultValue.
     */
    protected boolean getProperty(final String name, final boolean defaultValue) {
        String value = this.repository.getProperty(name);
        boolean ret;
        if (value == null) {
            ret = defaultValue;
        } else {
            ret = Boolean.valueOf(value).booleanValue();
        }
        return ret;
    }

    // -------------------------------------------------------- Private Methods

}
