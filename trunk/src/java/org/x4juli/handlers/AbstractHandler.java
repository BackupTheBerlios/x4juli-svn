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
package org.x4juli.handlers;

import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

import org.x4juli.NOPLogger;
import org.x4juli.filter.WrapperFilter;
import org.x4juli.formatter.SimpleFormatter;
import org.x4juli.global.Constants;
import org.x4juli.global.helper.LoggerUtil;
import org.x4juli.global.resources.MessageProperties;
import org.x4juli.global.spi.Component;
import org.x4juli.global.spi.ExtendedFilter;
import org.x4juli.global.spi.ExtendedFormatter;
import org.x4juli.global.spi.ExtendedHandler;
import org.x4juli.global.spi.ExtendedLogRecord;
import org.x4juli.global.spi.ExtendedLogger;
import org.x4juli.global.spi.LogIllegalStateException;
import org.x4juli.global.spi.LoggerRepository;
import org.x4juli.global.spi.OptionHandler;

/**
 * Abstract superclass of the other handlers in the package. This class provides
 * the code for common functionality, such as support for threshold filtering
 * and support for general filters. This class takes care of usage of
 * <code>org.x4juli.global.spi.ExtendedFilter</code>,
 * <code>org.x4juli.global.spi.ExtendedFormatter</code>,
 * <code>org.x4juli.global.spi.ExtendedLogRecord</code>.
 *
 * <table border="1" cellspacing="0" cellpadding="2">
 * <tr>
 * <th valign="top" scope="col">Attribute</th>
 * <th valign="top" scope="col">Description</th>
 * <th valign="top" scope="col">Required</th>
 * </tr>
 * <tr>
 * <td valign="top">.name</td>
 * <td valign="top">Name of the handler.</td>
 * <td valign="top">YES. No default, exception instead.</td>
 * </tr>
 * <tr>
 * <td valign="top">.level</td>
 * <td valign="top">Level of the handler. Allowed valued are from
 * <code>java.util.logging.Level.*</code>.</td>
 * <td valign="top">No. Default <code>ALL</code></td>
 * </tr>
 * <tr>
 * <td valign="top">.formatter</td>
 * <td valign="top">Formatter of the handler. Value is the full qualified
 * classname of the formatter.</td>
 * <td valign="top">No, but recommened. Default
 * <code>org.x4juli.formatter.SimpleFormatter</code></td>
 * </tr>
 * <tr>
 * <td valign="top">.filter</td>
 * <td valign="top">Filter of the handler. Value is the full qualified
 * Classname of the filter. WARNING STILL UNDER DEVELOPMENT!</td>
 * <td valign="top">No. Default <code>null</code>.</td>
 * </tr>
 * </table>
 * <p>
 * Logging API as a whole was originally done for <a
 * href="http://logging.apache.org/log4j/">Apache log4j</a>. <b>Juli</b> is a
 * port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging
 * APIs</a>. All credits for initial idea, design, implementation,
 * documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This
 * file was originally published by <i>Ceki G&uuml;lc&uuml;</i>. Please use
 * exclusively the <i>appropriate</i> mailing lists for questions, remarks and
 * contribution.
 * </p>
 *
 * @author Boris Unckel
 * @since 0.5
 */
public abstract class AbstractHandler extends Handler
                      implements Component, OptionHandler, ExtendedHandler {

    // -------------------------------------------------------------- Variables

    /**
     * The component object store.
     */
    protected LoggerRepository repository;

    /**
     * The component logmanager.
     */
    protected final LogManager manager = LogManager.getLogManager();

    /**
     * The first @link{ExtendedFilter} in the filter chain. Set to <code>null</code>
     * initially.
     */
    protected ExtendedFilter headFilter = null;

    /**
     * The last @link{ExtendedFilter} in the filter chain. Set to <code>null</code>
     * initially.
     */
    protected ExtendedFilter tailFilter = null;

    /**
     * Is this appender closed?
     */
    protected boolean closed = false;

    /**
     * Is the appender ready for action.
     */
    protected boolean active = false;

    /**
     * Handlers are named.
     */
    protected String name;

    /**
     * The extendedFormatter remains null if an
     * <code>java.util.Logging.Formatter</code> is set. Then the formatter of
     * the super class is used instead.
     */
    protected ExtendedFormatter extFormatter = null;

    /**
     * The component logger.
     */
    private ExtendedLogger logger;

    /**
     * The component error counter.
     */
    private int errorCount = 0;

    /**
     * The guard prevents an appender from repeatedly calling its own doAppend
     * method.
     */
    private boolean guard = false;

    // ----------------------------------------------------------- Constructors

    /**
     * Default Constructor instantiation used for configuration by file. This
     * automatically activatesOptions(). Avoid in programmatically use.
     *
     * @since 0.5
     */
    protected AbstractHandler() {
        if (!this.active) {
//TODO BEREINIGEN!!!
            //            configure();
//            activateOptions();
        }
    }

    /**
     * Utility Constructor. All properties must be set programmatically. Finally
     * you need to call actiavtesOptions().
     *
     * @param handlerName of the current instance.
     * @since 0.5
     */
    protected AbstractHandler(final String handlerName) {
        this.name = handlerName;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * {@inheritDoc}
     * <br/> Subclasses have not set active to true, but call super as
     * last statement!
     *
     * @since 0.5
     */
    public void activateOptions() {
        this.active = true;
        this.closed = false;
    }

    /**
     * Configure all properties of the object. Subclasses should call
     * super.configure() to ensure proper configuration.
     *
     * @since 0.5
     */
    public void configure() {
        String className = this.getClass().getName();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        //Name of the handler
        String key = this.getClass().getName() + ".name";
        String handlerName = getProperty(key, null);
        if (handlerName == null) {
            throw new IllegalArgumentException("Any handler must have a name, key[" + key
                    + "] missing");
        }
        setName(handlerName);

        // Level
        key = className + ".level";
        setLevel(Level.parse(getProperty(key, "" + Level.ALL)));
        // Formatter
        key = className + ".formatter";
        String formatterName = getProperty(key, null);
        if (formatterName != null) {
            try {
                setFormatter((Formatter) cl.loadClass(formatterName).newInstance());
            } catch (Exception e) {
                // Ignore Exception, to recognize it, use IDE Debugger Breakpoint in NOPLogger
                NOPLogger.NOP_LOGGER.log(Level.FINEST, "Ignored exception", e);
            }
        } else {
            setFormatter((ExtendedFormatter)new SimpleFormatter());
        }

        // Filter
        key = className + ".filter";
        String filterName = getProperty(key, null);
        if (filterName != null) {
            try {
                setFilter((Filter) cl.loadClass(filterName).newInstance());
            } catch (Exception e) {
                // Ignore Exception, to recognize it, use IDE Debugger Breakpoint in NOPLogger
                NOPLogger.NOP_LOGGER.log(Level.FINEST, "Ignored exception", e);
            }
        }

    }

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public String getFullQualifiedClassName() {
        return "org.x4juli.handlers.AbstractHandler";
    }

    /**
     * {@inheritDoc}
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
     * @since 0.5
     */
    public MessageProperties getMessageProperties() {
        return MessageProperties.PROPERTIES_HANDLER;
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.5
     */
    public final void publish(final LogRecord record) {
        publish(LoggerUtil.wrapLogRecord(record));
    }

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public synchronized void publish(final ExtendedLogRecord record) {
        // WARNING: The guard check MUST be the first statement in the
        // publish() method.

        // prevent re-entry.
        if (this.guard) {
            return;
        }
        try {
            this.guard = true;
            if (record == null) {
                return;
            }
            if (this.closed) {
                getNonFloodingLogger().log(Level.SEVERE,
                        MessageText.Not_allowed_to_write_to_a_closed_handler, this.name);
                return;
            }

            if (!this.active) {
                getNonFloodingLogger().log(Level.SEVERE,
                        MessageText.Not_allowed_to_write_to_an_inactive_handler, this.name);
                return;
            }
            if (!isLoggable(record)) {
                getLogger().log(Level.FINER, "Record is not loggable");
                return;
            }

            this.appendLogRecord(record);

        } finally {
            this.guard = false;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.5
     */
    public final boolean isLoggable(final LogRecord record) {
        if (record == null) {
            return false;
        }
        boolean result = false;
        result = isLoggable(LoggerUtil.wrapLogRecord(record));
        return result;
    }

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public boolean isLoggable(final ExtendedLogRecord record) {
        if (record == null || record.getLevel() == null) {
            return false;
        }
        int levelValue = getLevel().intValue();
        if (record.getLevel().intValue() < levelValue || levelValue == Constants.LEVEL_OFF) {
            return false;
        }
        ExtendedFilter f = this.headFilter;
        int lastDecision = ExtendedFilter.X4JULI_DENY;

        while (f != null) {
            lastDecision = f.decide(record);
            switch (lastDecision) {
            case ExtendedFilter.X4JULI_DENY:
                return false;

            case ExtendedFilter.X4JULI_ACCEPT:
                return true;

            default:
            case ExtendedFilter.X4JULI_NEUTRAL:
                f = f.getNext();
            }
        }
        if (lastDecision == ExtendedFilter.X4JULI_NEUTRAL) {
            return false;
        }
        return true;

    }

    /**
     * Returns the head filter of the filter chain.
     *
     * @return headFilter
     * @since 0.5
     */
    public Filter getFilter() {
        return this.headFilter;
    }

    /**
     * Different to the super Class, the filter is wrapped and added to the
     * chain.
     *
     * @param newFilter the filter to add
     * @throws SecurityException if changing the filter is not allowed.
     * @since 0.5
     */
    public final void setFilter(final Filter newFilter) throws SecurityException {
        if (newFilter instanceof ExtendedFilter) {
            addFilter((ExtendedFilter) newFilter);
        } else if (newFilter != null) {
            WrapperFilter wrap = new WrapperFilter(newFilter);
            addFilter(wrap);
        }
    }

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public void addFilter(final ExtendedFilter newFilter) {
        if (this.headFilter == null) {
            this.headFilter = newFilter;
            this.tailFilter = newFilter;
        } else {
            this.tailFilter.setNext(newFilter);
            this.tailFilter = newFilter;
        }
    }

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public void clearFilters() {
        this.headFilter = null;
        this.tailFilter = null;
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.5
     */
    public final void setFormatter(final Formatter newFormatter)
            throws SecurityException {
        if (newFormatter instanceof ExtendedFormatter) {
            this.extFormatter = (ExtendedFormatter) newFormatter;
        } else {
            super.setFormatter(newFormatter);
            this.extFormatter = null;
        }
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void setFormatter(final ExtendedFormatter newFormatter) {
        this.extFormatter = newFormatter;
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.5
     */
    public final Formatter getFormatter() {
        if (this.extFormatter != null) {
            return (Formatter) this.extFormatter;
        } else {
            return super.getFormatter();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.5
     */
    public boolean isClosed() {
        return this.closed;
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.5
     */
    public boolean isActive() {
        // an appender can be active only if it is not closed
        return (this.active && !this.closed);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.5
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.5
     */
    public String getName() {
        return this.name;
    }

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public String toString() {
        StringBuffer buf = new StringBuffer(this.getClass().getName());
        buf.append(": Name[");
        buf.append(getName());
        buf.append("] Active[");
        buf.append(this.active);
        buf.append("] Closed[");
        buf.append(this.closed);
        buf.append("]");
        return buf.toString();
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Finalize this appender by calling the derived class' <code>close</code>
     * method.
     * {@inheritDoc}
     * @since 0.5
     */
    protected void finalize() throws Throwable {
        super.finalize();
        // An appender might be closed then garbage collected. There is no
        // point in closing twice.
        if (this.closed) {
            return;
        }

        getLogger().log(Level.FINER, MessageText.Finalizing_handler_named, this.name);
        close();
    }

    /**
     * Return the {@link ObjectStore} this component is attached to.
     *
     * @return Owning ObjectStore
     * @since 0.5
     */
    protected LoggerRepository getLoggerRepository() {
        return this.repository;
    }

    /**
     * Called by derived classes when they deem that the component has recovered
     * from an erroneous state.
     *
     * @since 0.5
     */
    protected void resetErrorCount() {
        this.errorCount = 0;
    }

    /**
     * Return an instance specific logger to be used by the component itself.
     * This logger is not intended to be accessed by the end-user, hence the
     * protected keyword.
     *
     * <p>
     * This logger always sends output to an <code>ConsoleHandler</code>,
     * which outputs to System.err
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
            //TODO Message Properterties
            String temp = this.getClass().getName();
            this. logger = this.repository.getLogger(temp);
        }
        return this.logger;
    }

    /**
     * Frequently called methods in juli components can invoke this method in
     * order to avoid flooding the output when logging lasting error conditions.
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
     * @param propname of the parameter to be obtained
     * @param defaultValue if propname is not found.
     * @return the value of the given property, if null defaultValue
     */
    protected String getProperty(final String propname, final String defaultValue) {
        String value = this.manager.getProperty(propname);
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
     * @param propname of the parameter to be obtained
     * @param defaultValue to return if no value has been found.
     * @return the value of the given property, if null defaultValue
     */
    protected int getProperty(final String propname, final int defaultValue) {
        String value = this.manager.getProperty(propname);
        int ret;
        if (value == null) {
            ret = defaultValue;
        } else {
            try {
                ret = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                getLogger().log(Level.WARNING,
                        org.x4juli.handlers.MessageText.Property_has_not_an_int_value,
                        new Object[] {propname, value, new Integer(defaultValue) });
                ret = defaultValue;
            }
        }
        return ret;
    }

    /**
     * Get an property value out of the <code>LogManager</code> by name.
     *
     * @param propname of the parameter to be obtained
     * @param defaultValue to return if no value has been found.
     * @return the value of the given property, if null defaultValue
     */
    protected boolean getProperty(final String propname, final boolean defaultValue) {
        String value = this.manager.getProperty(propname);
        boolean ret;
        if (value == null) {
            ret = defaultValue;
        } else {
            ret = Boolean.valueOf(value).booleanValue();
        }
        return ret;
    }

    /**
     * Subclasses of <code>AbstractHandler</code> should implement this method
     * to perform actual logging. See also {@link #publish(ExtendedLogRecord)}
     * method.
     *
     * @param record to write to the log
     */
    protected abstract void appendLogRecord(ExtendedLogRecord record);

}

// EOF AbstractHandler.java
