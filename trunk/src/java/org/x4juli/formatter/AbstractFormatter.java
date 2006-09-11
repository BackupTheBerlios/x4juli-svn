/*
 * Copyright 2005, x4juli.org.
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
package org.x4juli.formatter;

import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

import org.x4juli.formatter.helper.FormatterUtil;
import org.x4juli.global.Constants;
import org.x4juli.global.helper.LoggerUtil;
import org.x4juli.global.resources.MessageProperties;
import org.x4juli.global.spi.Component;
import org.x4juli.global.spi.ExtendedFormatter;
import org.x4juli.global.spi.ExtendedLogRecord;
import org.x4juli.global.spi.ExtendedLogger;
import org.x4juli.global.spi.LogIllegalStateException;
import org.x4juli.global.spi.LoggerRepository;
import org.x4juli.global.spi.OptionHandler;
import org.x4juli.logger.NOPLogger;

/**
 * AbstractFormatter is for ease of development. It must extend
 * 
 * <code>java.util.logging.Formatter</code> and be a Component with the attributes of a
 * BaseComponent. The code of BaseComponent is copied here therefor.
 * 
 * @author Boris Unckel
 * @since 0.5
 */
public abstract class AbstractFormatter extends Formatter implements Component, OptionHandler,
        ExtendedFormatter {

    // -------------------------------------------------------------- Variables

    /**
     * Store for this component.
     */
    protected LoggerRepository repository;

    /**
     * Formatter ignores throwables in output or not.
     */
    protected boolean ignoresThrowable = true;

    private ExtendedLogger logger;

    private int errorCount = 0;

    // ----------------------------------------------------------- Constructors

    // --------------------------------------------------------- Public Methods

    /**
     * Default constructor, does not configure or activateOptions.
     * @since 0.7
     */
    public AbstractFormatter() {
        super();
    }

    /**
     * Specifiy Properties for the component. Default Implementation returns null. {@inheritDoc}
     * 
     * @since 0.5
     */
    public MessageProperties getMessageProperties() {
        return MessageProperties.PROPERTIES_FORMATTER;
    }

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
     * Formatter ignores throwables in output?
     * 
     * @return whether throwables are ignored or not.
     * @since 0.5
     */
    public boolean ignoresThrowable() {
        return this.ignoresThrowable;
    }

    /**
     * Formatter ignores throwables in output?
     * 
     * @param ignoresThrowable whether throwables are ignored or not.
     * @since 0.5
     */
    public void setIgnoresThrowable(final boolean ignoresThrowable) {
        this.ignoresThrowable = ignoresThrowable;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.5
     */
    public final String format(final LogRecord record) {
        return format(LoggerUtil.wrapLogRecord(record));
    }

    /**
     * Formats the given LogRecord.
     * 
     * @return the formatted information.
     * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
     * @since 0.5
     */
    public String format(final ExtendedLogRecord record) {
        String ret = doFormat(record);
        return ret;
    }

    /**
     * Method to implement for the concrete formatting of the LogRecord.
     * 
     * @param record to format.
     * @return the formatted information specified in the concrete class.
     * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
     * @since 0.5
     */
    public abstract String doFormat(ExtendedLogRecord record);

    /**
     * High availability format. Neither incorrect messages (missing java.text.MessageFormat
     * compatibility) nor exceptions during format will fail.
     * 
     * @param record containing raw message and parameters.
     * @return the formatted message.
     * @see FormatterUtil#formatMessage(ExtendedLogRecord)
     * @since 0.5
     */
    public String formatMessage(final ExtendedLogRecord record) {
        return FormatterUtil.formatMessage(record);
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Called by derived classes when they deem that the component has recovered from an erroneous
     * state.
     */
    protected void resetErrorCount() {
        this.errorCount = 0;
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
     * Return an instance specific logger to be used by the component itself. This logger is not
     * intended to be accessed by the end-user, hence the protected keyword.
     * 
     * <p>
     * This logger always sends output to an
     * 
     * <code>ConsoleHandler</code>, which outputs to System.err
     * </p>
     * 
     * @return A Logger instance for the concrete formatter.
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
     * Frequently called methods in log4j components can invoke this method in order to avoid
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
     * @param name of the parameter to be obtained
     * @param defaultValue
     * @return the value of the given property, if null defaultValue
     */
    protected String getProperty(final String name, final String defaultValue) {
        String value = this.repository.getName();
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
     * @param name of the parameter to be obtained
     * @param defaultValue
     * @return the value of the given property, if null defaultValue
     */
    protected int getProperty(final String name, final int defaultValue) {
        String value = this.repository.getName();
        int ret;
        if (value == null) {
            ret = defaultValue;
        } else {
            try {
                ret = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                getLogger().log(Level.WARNING,
                        MessageText.Unexcpected_IO_Exception_during_formating,
                        new Object[] { name, value, new Integer(defaultValue) });
                ret = defaultValue;
            }
        }
        return ret;
    }

    /**
     * Get an property value out of the <code>LogManager</code> by name.
     * 
     * @param name of the parameter to be obtained
     * @param defaultValue
     * @return the value of the given property, if null defaultValue
     */
    protected boolean getProperty(final String name, final boolean defaultValue) {
        String value = this.repository.getName();
        boolean ret;
        if (value == null) {
            ret = defaultValue;
        } else {
            ret = Boolean.valueOf(value).booleanValue();
        }
        return ret;
    }

}
