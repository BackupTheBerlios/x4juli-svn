/*
 * Copyright 2006 x4juli.org.
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
package org.x4juli.slf4j;

import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.slf4j.Marker;
import org.x4juli.global.helper.LoggerUtil;
import org.x4juli.global.spi.ExtendedLogRecord;
import org.x4juli.global.spi.ExtendedLogRecordImpl;
import org.x4juli.logger.AbstractExtendedLogger;

/**
 * Missing documentation.
 * @todo Missing documentation.
 * @author Boris Unckel
 * @since 0.7
 */
class Slf4jLogger extends AbstractExtendedLogger {

    // SLF4J Level have to be the same as in JCL because of
    // identical method names.

    static final Level SLF4J_MAPPING_DEBUG = Level.FINE;

    static final Level SLF4J_MAPPING_INFO = Level.INFO;

    static final Level SLF4J_MAPPING_WARN = Level.WARNING;

    static final Level SLF4J_MAPPING_ERROR = Level.SEVERE;

    /**
     * @param name
     * @param resourceBundleName
     */
    public Slf4jLogger(String name, String resourceBundleName) {
        super(name, resourceBundleName);
    }

    // --------------------------------------------------------- Public Methods

    // ------------------------------------------------Methods for both
    // Interfaces

    /**
     * Refers to <code>java.util.logging.Level.FINE</code>.
     *
     * @see org.apache.commons.logging.Log#isDebugEnabled()
     * @see org.slf4j.Logger#isDebugEnabled()
     * @since 0.5
     */
    public boolean isDebugEnabled() {
        return isLoggable(SLF4J_MAPPING_DEBUG);
    }

    /**
     * Refers to <code>java.util.logging.Level.INFO</code>.
     *
     * @see org.apache.commons.logging.Log#isInfoEnabled()
     * @see org.slf4j.Logger#isInfoEnabled()
     * @since 0.5
     */
    public boolean isInfoEnabled() {
        return isLoggable(SLF4J_MAPPING_INFO);
    }

    /**
     * Refers to <code>java.util.logging.Level.SEVERE</code>.
     *
     * @see org.apache.commons.logging.Log#isErrorEnabled()
     * @see org.slf4j.Logger#isErrorEnabled()
     * @since 0.5
     */
    public boolean isErrorEnabled() {
        return isLoggable(SLF4J_MAPPING_ERROR);
    }

    // --------------------------------------Methods only for org.slf4j.Logger

    /**
     * Refers to <code>java.util.logging.Level.FINE</code>.
     *
     * @see org.slf4j.Logger#debug(String)
     * @since 0.6
     */
    public void debug(final String msg) {
        if (!isLoggable(SLF4J_MAPPING_DEBUG)) {
            return;
        }
        log(SLF4J_MAPPING_DEBUG, msg);
    }

    /**
     * Refers to <code>java.util.logging.Level.FINE</code>.
     *
     * @see org.slf4j.Logger#debug(String, Throwable)
     * @since 0.6
     */
    public void debug(final String msg, final Throwable t) {
        if (!isLoggable(SLF4J_MAPPING_DEBUG)) {
            return;
        }
        log(SLF4J_MAPPING_DEBUG, msg, t);
    }

    /**
     * Refers to <code>java.util.logging.Level.FINE</code>.
     *
     * @see org.slf4j.Logger#debug(String, Object)
     * @since 0.6
     */
    public void debug(final String format, final Object arg) {
        if (!isLoggable(SLF4J_MAPPING_DEBUG) || format == null) {
            return;
        }
        robustLogSlf4j(SLF4J_MAPPING_DEBUG, format, new Object[]{arg});
    }

    /**
     * Refers to <code>java.util.logging.Level.FINE</code>.
     *
     * @see org.slf4j.Logger#debug(String, Object, Object)
     * @since 0.6
     */
    public void debug(final String format, final Object arg1, final Object arg2) {
        if (!isLoggable(SLF4J_MAPPING_DEBUG) || format == null) {
            return;
        }
        robustLogSlf4j(SLF4J_MAPPING_DEBUG, format, new Object[]{arg1, arg2});
    }

    /**
     * Refers to <code>java.util.logging.Level.FINE</code>.
     * @see org.slf4j.Logger#debug(String, Object[])
     * @since 0.7
     */
    public void debug(final String format, final Object[] argArray) {
        if (!isLoggable(SLF4J_MAPPING_DEBUG) || format == null) {
            return;
        }
        robustLogSlf4j(SLF4J_MAPPING_DEBUG, format, argArray);    
    }

    /**
     * Refers to <code>java.util.logging.Level.INFO</code>.
     *
     * @see org.slf4j.Logger#debug(String, Throwable)
     * @since 0.6
     */
    public void info(final String msg, final Throwable t) {
        if (!isLoggable(SLF4J_MAPPING_INFO)) {
            return;
        }
        log(SLF4J_MAPPING_INFO, msg, t);
    }

    /**
     * Refers to <code>java.util.logging.Level.INFO</code>.
     *
     * @see org.slf4j.Logger#info(String, Object)
     * @since 0.6
     */
    public void info(final String format, final Object arg) {
        if (!isLoggable(SLF4J_MAPPING_INFO) || format == null) {
            return;
        }
        robustLogSlf4j(SLF4J_MAPPING_INFO, format, new Object[]{arg});
    }

    /**
     * Refers to <code>java.util.logging.Level.INFO</code>.
     *
     * @see org.slf4j.Logger#info(String, Object, Object)
     * @since 0.6
     */
    public void info(final String format, final Object arg1, final Object arg2) {
        if (!isLoggable(SLF4J_MAPPING_INFO) || format == null) {
            return;
        }
        robustLogSlf4j(SLF4J_MAPPING_INFO, format, new Object[]{arg1, arg2});
    }

    /**
     * Refers to <code>java.util.logging.Level.INFO</code>.
     * 
     * @see org.slf4j.Logger#info(String, Object[])
     * @since 0.7
     */
    public void info(final String format, final Object[] argArray) {
        if (!isLoggable(SLF4J_MAPPING_INFO) || format == null) {
            return;
        }
        robustLogSlf4j(SLF4J_MAPPING_INFO, format, argArray);
    }

    /**
     * Refers to <code>java.util.logging.Level.WARNING</code>.
     *
     * @see org.slf4j.Logger#warn(String)
     * @since 0.6
     */
    public void warn(final String msg) {
        if (!isLoggable(SLF4J_MAPPING_WARN)) {
            return;
        }
        log(SLF4J_MAPPING_WARN, msg);
    }

    /**
     * Refers to <code>java.util.logging.Level.WARNING</code>.
     *
     * @see org.slf4j.Logger#warn(String, Throwable)
     * @since 0.6
     */
    public void warn(final String msg, final Throwable t) {
        if (!isLoggable(SLF4J_MAPPING_WARN)) {
            return;
        }
        log(SLF4J_MAPPING_WARN, msg, t);
    }

    /**
     * Refers to <code>java.util.logging.Level.WARNING</code>.
     *
     * @see org.slf4j.Logger#warn(String, Object)
     * @since 0.6
     */
    public void warn(final String format, final Object arg) {
        if (!isLoggable(SLF4J_MAPPING_WARN) || format == null) {
            return;
        }
        robustLogSlf4j(SLF4J_MAPPING_WARN, format, new Object[]{arg});
    }

    /**
     * Refers to <code>java.util.logging.Level.WARNING</code>.
     *
     * @see org.slf4j.Logger#warn(String, Object, Object)
     * @since 0.6
     */
    public void warn(final String format, final Object arg1, final Object arg2) {
        if (!isLoggable(SLF4J_MAPPING_WARN) || format == null) {
            return;
        }
        robustLogSlf4j(SLF4J_MAPPING_WARN, format, new Object[]{arg1, arg2});
    }

    /**
     * Refers to <code>java.util.logging.Level.WARNING</code>.
     * @see org.slf4j.Logger#warn(String, Object[])
     * @since
     */
    public void warn(final String format, final Object[] argArray) {
        if (!isLoggable(SLF4J_MAPPING_WARN) || format == null) {
            return;
        }
        robustLogSlf4j(SLF4J_MAPPING_WARN, format, argArray);
    }

    /**
     * Refers to <code>java.util.logging.Level.SEVERE</code>.
     *
     * @see org.slf4j.Logger#error(String)
     * @since 0.6
     */
    public void error(final String msg) {
        if (!isLoggable(SLF4J_MAPPING_ERROR)) {
            return;
        }
        log(SLF4J_MAPPING_ERROR, msg);
    }

    /**
     * Refers to <code>java.util.logging.Level.SEVERE</code>.
     *
     * @see org.slf4j.Logger#error(String, Throwable)
     * @since 0.6
     */
    public void error(final String msg, final Throwable t) {
        if (!isLoggable(SLF4J_MAPPING_ERROR)) {
            return;
        }
        log(SLF4J_MAPPING_ERROR, msg, t);
    }

    /**
     * Refers to <code>java.util.logging.Level.SEVERE</code>.
     *
     * @see org.slf4j.Logger#error(String, Object)
     * @since 0.6
     */
    public void error(final String format, final Object arg) {
        if (!isLoggable(SLF4J_MAPPING_ERROR) || format == null) {
            return;
        }
        robustLogSlf4j(SLF4J_MAPPING_ERROR, format, new Object[]{arg});
    }

    /**
     * Refers to <code>java.util.logging.Level.SEVERE</code>.
     *
     * @see org.slf4j.Logger#error(String, Object, Object)
     * @since 0.6
     */
    public void error(final String format, final Object arg1, final Object arg2) {
        if (!isLoggable(SLF4J_MAPPING_ERROR) || format == null) {
            return;
        }
        robustLogSlf4j(SLF4J_MAPPING_ERROR, format, new Object[]{arg1, arg2});
    }

    /**
     * Refers to <code>java.util.logging.Level.SEVERE</code>.
     * @see org.slf4j.Logger#error(String, Object[])
     * @since 0.7
     */
    public void error(final String format, final Object[] argArray) {
        if (!isLoggable(SLF4J_MAPPING_ERROR) || format == null) {
            return;
        }
        robustLogSlf4j(SLF4J_MAPPING_ERROR, format, argArray);
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        if (!isLoggable(SLF4J_MAPPING_DEBUG) || format == null) {
            return;
        }
        robustLogSlf4j(SLF4J_MAPPING_DEBUG, format, new Object[]{arg1, arg2});
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void debug(Marker marker, String format, Object arg) {
        if (!isLoggable(SLF4J_MAPPING_DEBUG) || format == null) {
            return;
        }
        robustLogSlf4j(SLF4J_MAPPING_DEBUG, format, new Object[]{arg});
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void debug(Marker marker, String format, Object[] argArray) {
        if (!isLoggable(SLF4J_MAPPING_DEBUG) || format == null) {
            return;
        }
        robustLogSlf4j(SLF4J_MAPPING_DEBUG, format, argArray);
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void debug(Marker marker, String msg, Throwable t) {
        if (!isLoggable(SLF4J_MAPPING_DEBUG)) {
            return;
        }
        log(SLF4J_MAPPING_DEBUG, msg, t);
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void debug(Marker marker, String msg) {
        if (!isLoggable(SLF4J_MAPPING_DEBUG)) {
            return;
        }
        log(SLF4J_MAPPING_DEBUG, msg);
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        if (!isLoggable(SLF4J_MAPPING_ERROR) || format == null) {
            return;
        }
        robustLogSlf4j(SLF4J_MAPPING_ERROR, format, new Object[]{arg1, arg2});
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void error(Marker marker, String format, Object arg) {
        if (!isLoggable(SLF4J_MAPPING_ERROR) || format == null) {
            return;
        }
        robustLogSlf4j(SLF4J_MAPPING_ERROR, format, new Object[]{arg});
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void error(Marker marker, String format, Object[] argArray) {
        if (!isLoggable(SLF4J_MAPPING_ERROR) || format == null) {
            return;
        }
        robustLogSlf4j(SLF4J_MAPPING_ERROR, format, argArray);
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void error(Marker marker, String msg, Throwable t) {
        if (!isLoggable(SLF4J_MAPPING_ERROR)) {
            return;
        }
        log(SLF4J_MAPPING_ERROR, msg, t);
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void error(Marker marker, String msg) {
        if (!isLoggable(SLF4J_MAPPING_ERROR)) {
            return;
        }
        log(SLF4J_MAPPING_ERROR, msg);
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        if (!isLoggable(SLF4J_MAPPING_INFO) || format == null) {
            return;
        }
        robustLogSlf4j(SLF4J_MAPPING_INFO, format, new Object[]{arg1, arg2});
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void info(Marker marker, String format, Object arg) {
        if (!isLoggable(SLF4J_MAPPING_INFO) || format == null) {
            return;
        }
        robustLogSlf4j(SLF4J_MAPPING_INFO, format, new Object[]{arg});
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void info(Marker marker, String format, Object[] argArray) {
        if (!isLoggable(SLF4J_MAPPING_INFO) || format == null) {
            return;
        }
        robustLogSlf4j(SLF4J_MAPPING_INFO, format, argArray);
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void info(Marker marker, String msg, Throwable t) {
        if (!isLoggable(SLF4J_MAPPING_INFO)) {
            return;
        }
        log(SLF4J_MAPPING_INFO, msg, t);
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void info(Marker marker, String msg) {
        if (!isLoggable(SLF4J_MAPPING_INFO)) {
            return;
        }
        log(SLF4J_MAPPING_INFO, msg);
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        if (!isLoggable(SLF4J_MAPPING_WARN) || format == null) {
            return;
        }
        robustLogSlf4j(SLF4J_MAPPING_WARN, format, new Object[]{arg1, arg2});
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void warn(Marker marker, String format, Object arg) {
        if (!isLoggable(SLF4J_MAPPING_WARN) || format == null) {
            return;
        }
        robustLogSlf4j(SLF4J_MAPPING_WARN, format, new Object[]{arg});
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void warn(Marker marker, String format, Object[] argArray) {
        if (!isLoggable(SLF4J_MAPPING_WARN) || format == null) {
            return;
        }
        robustLogSlf4j(SLF4J_MAPPING_WARN, format, argArray);
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void warn(Marker marker, String msg, Throwable t) {
        if (!isLoggable(SLF4J_MAPPING_WARN)) {
            return;
        }
        log(SLF4J_MAPPING_WARN, msg, t);
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void warn(Marker marker, String msg) {
        if (!isLoggable(SLF4J_MAPPING_WARN)) {
            return;
        }
        log(SLF4J_MAPPING_WARN, msg);
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public boolean isDebugEnabled(Marker marker) {
        return isLoggable(SLF4J_MAPPING_DEBUG);
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public boolean isErrorEnabled(Marker marker) {
        return isLoggable(SLF4J_MAPPING_ERROR);
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public boolean isInfoEnabled(Marker marker) {
        return isLoggable(SLF4J_MAPPING_INFO);
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public boolean isWarnEnabled(Marker marker) {
        return isLoggable(SLF4J_MAPPING_WARN);
    }

    // Following methods are overwritten to get correct location information
    // and for performance reasons (avoiding use of ExtendedLogRecordWrapper).

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void log(final LogRecord record) {
        if (record == null || record.getLevel() == null || !isLoggable(record.getLevel())) {
            return;
        }
        ExtendedLogRecord lr = LoggerUtil.wrapLogRecord(record);
        if(lr.getFQCNofLogger() == null) {
            lr.setFQCNofLogger(FQCNofLogger);
        }
        lr.getNDC();
        super.log((LogRecord)lr);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void log(final ExtendedLogRecord record) {
        if (record == null || record.getLevel() == null || !isLoggable(record.getLevel())) {
            return;
        }
        if(record.getFQCNofLogger() == null) {
            record.setFQCNofLogger(FQCNofLogger);
        }
        record.getNDC();
        super.log((LogRecord) record);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void log(final Level level, final String msg, final Object param1) {
        if (level == null || !isLoggable(level)) {
            return;
        }
        LogRecord logRecord = new ExtendedLogRecordImpl(level, msg);
        logRecord.setParameters(new Object[] {param1 });
        completeLogRecord((ExtendedLogRecord) logRecord);
        super.log(logRecord);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void log(final Level level, final String msg, final Object[] params) {
        if (level == null || !isLoggable(level)) {
            return;
        }
        LogRecord logRecord = new ExtendedLogRecordImpl(level, msg);
        logRecord.setParameters(params);
        completeLogRecord((ExtendedLogRecord) logRecord);
        super.log(logRecord);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void log(final Level level, final String msg, final Throwable thrown) {
        if (level == null || !isLoggable(level)) {
            return;
        }
        LogRecord logRecord = new ExtendedLogRecordImpl(level, msg);
        logRecord.setThrown(thrown);
        completeLogRecord((ExtendedLogRecord) logRecord);
        super.log(logRecord);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void log(final Level level, final String msg) {
        if (level == null || !isLoggable(level)) {
            return;
        }
        LogRecord logRecord = new ExtendedLogRecordImpl(level, msg);
        completeLogRecord((ExtendedLogRecord) logRecord);
        super.log(logRecord);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void logp(final Level level, final String sourceClass, final String sourceMethod,
            final String msg, final Object param1) {
        if (level == null || !isLoggable(level)) {
            return;
        }
        LogRecord logRecord = new ExtendedLogRecordImpl(level, msg);
        logRecord.setSourceClassName(sourceClass);
        logRecord.setSourceMethodName(sourceMethod);
        logRecord.setParameters(new Object[] {param1 });
        completeLogRecord((ExtendedLogRecord) logRecord);
        super.log(logRecord);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void logp(final Level level, final String sourceClass, final String sourceMethod,
            final String msg, final Object[] params) {
        if (level == null || !isLoggable(level)) {
            return;
        }
        LogRecord logRecord = new ExtendedLogRecordImpl(level, msg);
        logRecord.setSourceClassName(sourceClass);
        logRecord.setSourceMethodName(sourceMethod);
        logRecord.setParameters(params);
        completeLogRecord((ExtendedLogRecord) logRecord);
        super.log(logRecord);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void logp(final Level level, final String sourceClass, final String sourceMethod,
            final String msg, final Throwable thrown) {
        if (level == null || !isLoggable(level)) {
            return;
        }
        LogRecord logRecord = new ExtendedLogRecordImpl(level, msg);
        logRecord.setSourceClassName(sourceClass);
        logRecord.setSourceMethodName(sourceMethod);
        logRecord.setThrown(thrown);
        completeLogRecord((ExtendedLogRecord) logRecord);
        super.log(logRecord);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void logp(final Level level, final String sourceClass, final String sourceMethod,
            final String msg) {
        if (level == null || !isLoggable(level)) {
            return;
        }
        LogRecord logRecord = new ExtendedLogRecordImpl(level, msg);
        logRecord.setSourceClassName(sourceClass);
        logRecord.setSourceMethodName(sourceMethod);
        completeLogRecord((ExtendedLogRecord) logRecord);
        super.log(logRecord);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void logrb(final Level level, final String sourceClass, final String sourceMethod,
            final String bundleName, final String msg, final Object param1) {
        if (level == null || !isLoggable(level)) {
            return;
        }
        LogRecord logRecord = new ExtendedLogRecordImpl(level, msg);
        logRecord.setSourceClassName(sourceClass);
        logRecord.setSourceMethodName(sourceMethod);
        logRecord.setResourceBundleName(bundleName);
        completeLogRecord((ExtendedLogRecord) logRecord);
        super.log(logRecord);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void logrb(final Level level, final String sourceClass, final String sourceMethod,
            final String bundleName, final String msg, final Object[] params) {
        if (level == null || !isLoggable(level)) {
            return;
        }
        LogRecord logRecord = new ExtendedLogRecordImpl(level, msg);
        logRecord.setSourceClassName(sourceClass);
        logRecord.setSourceMethodName(sourceMethod);
        logRecord.setResourceBundleName(bundleName);
        logRecord.setParameters(params);
        completeLogRecord((ExtendedLogRecord) logRecord);
        super.log(logRecord);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void logrb(final Level level, final String sourceClass, final String sourceMethod,
            final String bundleName, final String msg, final Throwable thrown) {
        if (level == null || !isLoggable(level)) {
            return;
        }
        LogRecord logRecord = new ExtendedLogRecordImpl(level, msg);
        logRecord.setSourceClassName(sourceClass);
        logRecord.setSourceMethodName(sourceMethod);
        logRecord.setResourceBundleName(bundleName);
        logRecord.setThrown(thrown);
        completeLogRecord((ExtendedLogRecord) logRecord);
        super.log(logRecord);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void logrb(final Level level, final String sourceClass, final String sourceMethod,
            final String bundleName, final String msg) {
        if (level == null || !isLoggable(level)) {
            return;
        }
        LogRecord logRecord = new ExtendedLogRecordImpl(level, msg);
        logRecord.setSourceClassName(sourceClass);
        logRecord.setSourceMethodName(sourceMethod);
        logRecord.setResourceBundleName(bundleName);
        completeLogRecord((ExtendedLogRecord) logRecord);
        super.log(logRecord);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void finest(final String msg) {
        if (!isLoggable(Level.FINEST)) {
            return;
        }
        log(Level.FINEST, msg);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void finer(final String msg) {
        if (!isLoggable(Level.FINER)) {
            return;
        }
        log(Level.FINER, msg);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void fine(final String msg) {
        if (!isLoggable(Level.FINE)) {
            return;
        }
        log(Level.FINE, msg);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void info(final String msg) {
        if (!isLoggable(Level.INFO)) {
            return;
        }
        log(Level.INFO, msg);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void config(final String msg) {
        if (!isLoggable(Level.CONFIG)) {
            return;
        }
        log(Level.CONFIG, msg);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void warning(final String msg) {
        if (!isLoggable(Level.WARNING)) {
            return;
        }
        log(Level.WARNING, msg);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void severe(final String msg) {
        if (!isLoggable(Level.SEVERE)) {
            return;
        }
        log(Level.SEVERE, msg);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void entering(final String sourceClass, final String sourceMethod) {
        if (!isLoggable(Level.FINER)) {
            return;
        }
        entering(sourceClass, sourceMethod, new Object[] {});
    }

    /**
     * {@inheritDoc}
     *
     * @since
     */
    public void entering(final String sourceClass, final String sourceMethod, final Object param1) {
        if (!isLoggable(Level.FINER)) {
            return;
        }
        entering(sourceClass, sourceMethod, new Object[] {param1 });
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void entering(final String sourceClass, final String sourceMethod,
                         final Object[] params) {
        if (!isLoggable(Level.FINER)) {
            return;
        }
        final String entry = "ENTRY";
        if (params == null || params.length == 0) {
            logp(Level.FINER, sourceClass, sourceMethod, entry);
            return;
        }
        // Original Version uses String concating, use of StringBuffer speeds
        // up.
        StringBuffer msg = new StringBuffer(entry);
        for (int i = 0; i < params.length; i++) {
            msg.append(" {");
            msg.append(i);
            msg.append("}");
        }
        logp(Level.FINER, sourceClass, sourceMethod, msg.toString(), params);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void exiting(final String sourceClass, final String sourceMethod) {
        if (!isLoggable(Level.FINER)) {
            return;
        }
        logp(Level.FINER, sourceClass, sourceMethod, "RETURN");
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void exiting(final String sourceClass, final String sourceMethod, final Object result) {
        if (!isLoggable(Level.FINER)) {
            return;
        }
        logp(Level.FINER, sourceClass, sourceMethod, "RETURN {0}", new Object[] {result });
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.6
     */
    public void throwing(final String sourceClass, final String sourceMethod,
                         final Throwable thrown) {
        if (!isLoggable(Level.FINER)) {
            return;
        }
        LogRecord logRecord = new ExtendedLogRecordImpl(Level.FINER, "THROW");
        logRecord.setSourceClassName(sourceClass);
        logRecord.setSourceMethodName(sourceMethod);
        logRecord.setThrown(thrown);
        completeLogRecord((ExtendedLogRecord) logRecord);
        super.log(logRecord);
    }

    // ------------------------------------------------------ Protected Methods
    
    /**
     * Method especially for public log methods which have an object (instead of
     * an String) as message.
     * @param level is not allowed to be null.
     * @param message object to create String to log.
     * @param t optional throwable to log, may be null.
     * @since 0.7
     */
    protected void robustLog(final Level level, final Object message, final Throwable t){
        String mes = null;
        try {
            mes = String.valueOf(message);
        } catch (Exception e) {
            mes = "Error in creating message["+e+"]";
            //Temporary solution!
            System.err.println("X4Juli: " + mes);
            e.printStackTrace();
        }
        LogRecord logRecord = new ExtendedLogRecordImpl(level, mes);
        if (t != null) {
            logRecord.setThrown(t);
        }
        completeLogRecord((ExtendedLogRecord)logRecord);
        super.log(logRecord);
    }

    /**
     * Method especially for public slf4j log methods which have an object or object[]
     * as parameter for the message formatting.
     * @param level is not allowed to be null.
     * @param message to format with args to complete logging.
     * @param args for message format.
     * @since 0.7
     */
    protected void robustLogSlf4j(final Level level, final String message, final Object[] args){
        String mes = null;
        try {
            mes = org.slf4j.impl.MessageFormatter.arrayFormat(message, args);
        } catch (Exception e) {
            mes = message + " Error in formatting message["+e+"]";
            //Temporary solution!
            System.err.println("X4Juli: " + mes);
            e.printStackTrace();
        }
        LogRecord logRecord = new ExtendedLogRecordImpl(level, mes);
        completeLogRecord((ExtendedLogRecord)logRecord);
        super.log(logRecord);
    }

}

// EOF Slf4jLogger.java
