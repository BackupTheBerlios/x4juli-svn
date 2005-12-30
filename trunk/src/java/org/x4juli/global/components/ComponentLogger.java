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
package org.x4juli.global.components;

import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.x4juli.formatter.helper.FormatterUtil;
import org.x4juli.formatter.pattern.ThrowableInformationPatternConverter;
import org.x4juli.global.Constants;
import org.x4juli.global.SystemUtils;
import org.x4juli.global.helper.LoggerUtil;
import org.x4juli.global.spi.ExtendedLogRecord;
import org.x4juli.global.spi.ExtendedLogRecordImpl;
import org.x4juli.global.spi.ExtendedLogger;

/**
 * A logger for internal use in x4juli to print various messages to
 * System.out and System.err.
 *
 * @author Boris Unckel
 * @since 0.6
 */
final class ComponentLogger extends Logger implements ExtendedLogger {

    // -------------------------------------------------------------- Variables
    private static final ThrowableInformationPatternConverter TIPC =
        ThrowableInformationPatternConverter.newInstance(null);


    // ----------------------------------------------------------- Constructors
    /**
     * Constructor for internal logger, defaults to Level.WARNING.
     * @param name of the logger.
     * @param resourceBundleName to i18n.
     */
    ComponentLogger(final String name, final String resourceBundleName) {
        super(name, resourceBundleName);
        setLevel(Level.WARNING);
    }

    // --------------------------------------------------------- Public Methods
    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void setFilter(final Filter newFilter) throws SecurityException {
        // NOP
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public Filter getFilter() {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void log(final LogRecord record) {
        if (record == null) {
            return;
        }
        doLogAddRb(LoggerUtil.wrapLogRecord(record));
    }

    /**
     * {@inheritDoc}
     * @since 0.6
     */
    public void log(final ExtendedLogRecord record) {
        if (record == null) {
            return;
        }
        doLogAddRb(record);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void log(final Level level, final String msg) {
        ExtendedLogRecord record = new ExtendedLogRecordImpl(level, msg);
        doLogAddRb(record);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void log(final Level level, final String msg, final Object param1) {
        ExtendedLogRecord record = new ExtendedLogRecordImpl(level, msg);
        record.setParameters(new Object[]{param1});
        doLogAddRb(record);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void log(final Level level, final String msg, final Object[] params) {
        ExtendedLogRecord record = new ExtendedLogRecordImpl(level, msg);
        record.setParameters(params);
        doLogAddRb(record);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void log(final Level level, final String msg, final Throwable thrown) {
        ExtendedLogRecord record = new ExtendedLogRecordImpl(level, msg);
        record.setThrown(thrown);
        doLogAddRb(record);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void logp(final Level level, final String sourceClass,
                     final String sourceMethod, final String msg) {
        ExtendedLogRecord record = new ExtendedLogRecordImpl(level, msg);
        record.setSourceClassName(sourceClass);
        record.setSourceMethodName(sourceMethod);
        doLogAddRb(record);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void logp(final Level level, final String sourceClass, final String sourceMethod,
                     final String msg, final Object param1) {
        ExtendedLogRecord record = new ExtendedLogRecordImpl(level, msg);
        record.setSourceClassName(sourceClass);
        record.setSourceMethodName(sourceMethod);
        record.setParameters(new Object[]{param1});
        doLogAddRb(record);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void logp(final Level level, final String sourceClass, final String sourceMethod,
                     final String msg, final Object[] params) {
        ExtendedLogRecord record = new ExtendedLogRecordImpl(level, msg);
        record.setSourceClassName(sourceClass);
        record.setSourceMethodName(sourceMethod);
        doLogAddRb(record);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void logp(final Level level, final String sourceClass,
                     final String sourceMethod, final String msg, final Throwable thrown) {
        ExtendedLogRecord record = new ExtendedLogRecordImpl(level, msg);
        record.setSourceClassName(sourceClass);
        record.setSourceMethodName(sourceMethod);
        record.setThrown(thrown);
        doLogAddRb(record);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void logrb(final Level level, final String sourceClass, final String sourceMethod,
                      final String bundleName, final String msg) {
        ExtendedLogRecord record = new ExtendedLogRecordImpl(level, msg);
        record.setSourceClassName(sourceClass);
        record.setSourceMethodName(sourceMethod);
        record.setResourceBundleName(bundleName);
        doLog(record);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void logrb(final Level level, final String sourceClass, final String sourceMethod,
                      final String bundleName, final String msg, final Object param1) {
        ExtendedLogRecord record = new ExtendedLogRecordImpl(level, msg);
        record.setSourceClassName(sourceClass);
        record.setSourceMethodName(sourceMethod);
        record.setResourceBundleName(bundleName);
        record.setParameters(new Object[]{param1});
        doLog(record);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void logrb(final Level level, final String sourceClass, final String sourceMethod,
                      final String bundleName, final String msg, final Object[] params) {
        ExtendedLogRecord record = new ExtendedLogRecordImpl(level, msg);
        record.setSourceClassName(sourceClass);
        record.setSourceMethodName(sourceMethod);
        record.setResourceBundleName(bundleName);
        record.setParameters(params);
        doLog(record);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void logrb(final Level level, final String sourceClass, final String sourceMethod,
                      final String bundleName, final String msg, final Throwable thrown) {
        ExtendedLogRecord record = new ExtendedLogRecordImpl(level, msg);
        record.setSourceClassName(sourceClass);
        record.setSourceMethodName(sourceMethod);
        record.setResourceBundleName(bundleName);
        record.setThrown(thrown);
        doLog(record);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void entering(final String sourceClass, final String sourceMethod) {
        ExtendedLogRecord record = new ExtendedLogRecordImpl(Level.FINER, "ENTRY");
        record.setSourceClassName(sourceClass);
        record.setSourceMethodName(sourceMethod);
        doLog(record);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void entering(final String sourceClass, final String sourceMethod,
                         final Object param1) {
        ExtendedLogRecord record = new ExtendedLogRecordImpl(Level.FINER, "ENTRY {0}");
        record.setSourceClassName(sourceClass);
        record.setSourceMethodName(sourceMethod);
        record.setParameters(new Object[]{param1});
        doLog(record);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void entering(final String sourceClass, final String sourceMethod,
                         final Object[] params) {
        ExtendedLogRecord record = new ExtendedLogRecordImpl(Level.FINER, "ENTRY {0}");
        record.setSourceClassName(sourceClass);
        record.setSourceMethodName(sourceMethod);
        record.setParameters(params);
        doLog(record);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void exiting(final String sourceClass, final String sourceMethod) {
        ExtendedLogRecord record = new ExtendedLogRecordImpl(Level.FINER, "RETURN");
        record.setSourceClassName(sourceClass);
        record.setSourceMethodName(sourceMethod);
        doLog(record);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void exiting(final String sourceClass, final String sourceMethod,
                        final Object result) {
        ExtendedLogRecord record = new ExtendedLogRecordImpl(Level.FINER, "ENTRY {0}");
        record.setSourceClassName(sourceClass);
        record.setSourceMethodName(sourceMethod);
        record.setParameters(new Object[]{result});
        doLog(record);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void throwing(final String sourceClass, final String sourceMethod,
                         final Throwable thrown) {
        ExtendedLogRecord record = new ExtendedLogRecordImpl(Level.FINER, "THROW");
        record.setSourceClassName(sourceClass);
        record.setSourceMethodName(sourceMethod);
        record.setThrown(thrown);
        doLog(record);

    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void severe(final String msg) {
        ExtendedLogRecord record = new ExtendedLogRecordImpl(Level.SEVERE, msg);
        doLogAddRb(record);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void warning(final String msg) {
        ExtendedLogRecord record = new ExtendedLogRecordImpl(Level.WARNING, msg);
        doLogAddRb(record);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void info(final String msg) {
        ExtendedLogRecord record = new ExtendedLogRecordImpl(Level.INFO, msg);
        doLogAddRb(record);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void config(final String msg) {
        ExtendedLogRecord record = new ExtendedLogRecordImpl(Level.CONFIG, msg);
        doLogAddRb(record);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void fine(final String msg) {
        ExtendedLogRecord record = new ExtendedLogRecordImpl(Level.FINE, msg);
        doLogAddRb(record);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void finer(final String msg) {
        ExtendedLogRecord record = new ExtendedLogRecordImpl(Level.FINER, msg);
        doLogAddRb(record);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void finest(final String msg) {
        ExtendedLogRecord record = new ExtendedLogRecordImpl(Level.FINEST, msg);
        doLogAddRb(record);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void addHandler(final Handler handler) throws SecurityException {
        // NOP
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void removeHandler(final Handler handler) throws SecurityException {
        // NOP
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public Handler[] getHandlers() {
        return new Handler[]{};
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void setUseParentHandlers(final boolean useParentHandlers) {
        // NOP
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public boolean getUseParentHandlers() {
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public Logger getParent() {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void setParent(final Logger parent) {
        // NOP
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Method adds the resource bundle of the logger to the record.
     * @param logRecord to add resource bundle and log.
     */
    protected void doLogAddRb(final ExtendedLogRecord logRecord) {
        if (logRecord == null
            || logRecord.getLevel() == null
            || !isLoggable(logRecord.getLevel())) {
            return;
        }
        logRecord.setLoggerName(getName());
        logRecord.setResourceBundleName(getResourceBundleName());
        logRecord.setResourceBundle(getResourceBundle());
        doLog(logRecord);
    }

    /**
     * Prints to Systemout or Systemerr. System.err is used when
     * the log level is WARNING or higher.
     * @param logRecord to print.
     */
    protected synchronized void doLog(final ExtendedLogRecord logRecord) {
        if (logRecord == null
           || logRecord.getLevel() == null
           || !isLoggable(logRecord.getLevel())) {
            return;
        }
        StringBuffer buf = new StringBuffer("JULI: ");
        buf.append(logRecord.getLevel());
        buf.append(" ");
        buf.append(FormatterUtil.formatMessage(logRecord));
        if (logRecord.getThrowableInformation() != null) {
            buf.append(SystemUtils.LINE_SEPARATOR);
            TIPC.format(logRecord, buf);
        }
        String format = buf.toString();
        if (getLevel().intValue() >= Constants.LEVEL_WARNING) {
            System.err.println(format);
        } else {
            System.out.println(format);
        }
    }

}

// EOF ComponentLogger.java
