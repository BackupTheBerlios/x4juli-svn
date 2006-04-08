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
package org.x4juli;

import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.x4juli.global.components.AbstractExtendedLogger;
import org.x4juli.global.helper.LoggerUtil;
import org.x4juli.global.spi.ExtendedLogRecord;
import org.x4juli.global.spi.ExtendedLogRecordImpl;

/**
 * Missing documentation.
 * @todo Missing documentation.
 * @author Boris Unckel
 * @since 0.7
 */
class JCLLogger extends AbstractExtendedLogger {

    // -------------------------------------------------------------- Variables

    static final Level JCL_MAPPING_TRACE = Level.FINEST;

    static final Level JCL_MAPPING_DEBUG = Level.FINE;

    static final Level JCL_MAPPING_INFO = Level.INFO;

    static final Level JCL_MAPPING_WARN = Level.WARNING;

    static final Level JCL_MAPPING_ERROR = Level.SEVERE;

    static final Level JCL_MAPPING_FATAL = Level.SEVERE;

    // ------------------------------------------------------------ Constructor

    /**
     * @param name
     * @param resourceBundleName
     */
    public JCLLogger(String name, String resourceBundleName) {
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
        return isLoggable(JCL_MAPPING_DEBUG);
    }

    /**
     * Refers to <code>java.util.logging.Level.INFO</code>.
     *
     * @see org.apache.commons.logging.Log#isInfoEnabled()
     * @see org.slf4j.Logger#isInfoEnabled()
     * @since 0.5
     */
    public boolean isInfoEnabled() {
        return isLoggable(JCL_MAPPING_INFO);
    }

    /**
     * Refers to <code>java.util.logging.Level.SEVERE</code>.
     *
     * @see org.apache.commons.logging.Log#isErrorEnabled()
     * @see org.slf4j.Logger#isErrorEnabled()
     * @since 0.5
     */
    public boolean isErrorEnabled() {
        return isLoggable(JCL_MAPPING_ERROR);
    }

    // -----------------Methods only for org.apache.commons.logging.Log

    /**
     * Refers to <code>java.util.logging.Level.FINEST</code>.
     *
     * @see org.apache.commons.logging.Log#isTraceEnabled()
     * @since 0.5
     */
    public boolean isTraceEnabled() {
        return isLoggable(JCL_MAPPING_TRACE);
    }

    /**
     * Refers to <code>java.util.logging.Level.WARNING</code>.
     *
     * @see org.apache.commons.logging.Log#isWarnEnabled()
     * @since 0.5
     */
    public boolean isWarnEnabled() {
        return isLoggable(JCL_MAPPING_WARN);
    }

    /**
     * Refers to <code>java.util.logging.Level.SEVERE</code>.
     *
     * @see org.apache.commons.logging.Log#isFatalEnabled()
     * @since 0.5
     */
    public boolean isFatalEnabled() {
        return isLoggable(JCL_MAPPING_FATAL);
    }

    /**
     * Refers to <code>java.util.logging.Level.FINEST</code>.
     *
     * @see org.apache.commons.logging.Log#trace(java.lang.Object)
     * @since 0.5
     */
    public void trace(final Object message) {
        if (!isLoggable(JCL_MAPPING_TRACE)) {
            return;
        }
        robustLog(JCL_MAPPING_TRACE, message, null);
    }

    /**
     * Refers to <code>java.util.logging.Level.FINEST</code>.
     *
     * @see org.apache.commons.logging.Log#trace(java.lang.Object,
     *      java.lang.Throwable)
     * @since 0.5
     */
    public void trace(final Object message, final Throwable t) {
        if (!isLoggable(JCL_MAPPING_TRACE)) {
            return;
        }
        robustLog(JCL_MAPPING_TRACE, message, t);
    }

    /**
     * Refers to <code>java.util.logging.Level.FINE</code>.
     *
     * @see org.apache.commons.logging.Log#debug(java.lang.Object)
     * @since 0.5
     */
    public void debug(final Object message) {
        if (!isLoggable(JCL_MAPPING_DEBUG)) {
            return;
        }
        robustLog(JCL_MAPPING_DEBUG, message, null);

    }

    /**
     * Refers to <code>java.util.logging.Level.FINE</code>.
     *
     * @see org.apache.commons.logging.Log#debug(java.lang.Object,
     *      java.lang.Throwable)
     * @since 0.5
     */
    public void debug(final Object message, final Throwable t) {
        if (!isLoggable(JCL_MAPPING_DEBUG)) {
            return;
        }
        robustLog(JCL_MAPPING_DEBUG, message, t);
    }

    /**
     * Refers to <code>java.util.logging.Level.INFO</code>.
     *
     * @see org.apache.commons.logging.Log#info(java.lang.Object)
     * @since 0.5
     */
    public void info(final Object message) {
        if (!isLoggable(JCL_MAPPING_INFO)) {
            return;
        }
        robustLog(JCL_MAPPING_INFO, message, null);

    }

    /**
     * Refers to <code>java.util.logging.Level.INFO</code>.
     *
     * @see org.apache.commons.logging.Log#info(java.lang.Object,
     *      java.lang.Throwable)
     * @since 0.5
     */
    public void info(final Object message, final Throwable t) {
        if (!isLoggable(JCL_MAPPING_INFO)) {
            return;
        }
        robustLog(JCL_MAPPING_INFO, message, t);
    }

    /**
     * Refers to <code>java.util.logging.Level.WARNING</code>.
     *
     * @see org.apache.commons.logging.Log#warn(java.lang.Object)
     * @since 0.5
     */
    public void warn(final Object message) {
        if (!isLoggable(JCL_MAPPING_WARN)) {
            return;
        }
        robustLog(JCL_MAPPING_WARN, message, null);
    }

    /**
     * Refers to <code>java.util.logging.Level.WARNING</code>.
     *
     * @see org.apache.commons.logging.Log#warn(java.lang.Object,
     *      java.lang.Throwable)
     * @since 0.5
     */
    public void warn(final Object message, final Throwable t) {
        if (!isLoggable(JCL_MAPPING_WARN)) {
            return;
        }
        robustLog(JCL_MAPPING_WARN, message, t);
    }

    /**
     * Refers to <code>java.util.logging.Level.SEVERE</code>.
     *
     * @see org.apache.commons.logging.Log#error(java.lang.Object)
     * @since 0.5
     */
    public void error(final Object message) {
        if (!isLoggable(JCL_MAPPING_ERROR)) {
            return;
        }
        robustLog(JCL_MAPPING_ERROR, message, null);
    }

    /**
     * Refers to <code>java.util.logging.Level.SEVERE</code>.
     *
     * @see org.apache.commons.logging.Log#error(java.lang.Object,
     *      java.lang.Throwable)
     * @since 0.5
     */
    public void error(final Object message, final Throwable t) {
        if (!isLoggable(JCL_MAPPING_ERROR)) {
            return;
        }
        robustLog(JCL_MAPPING_ERROR, message, t);
    }

    /**
     * Refers to <code>java.util.logging.Level.SEVERE</code>.
     *
     * @see org.apache.commons.logging.Log#fatal(java.lang.Object)
     * @since 0.5
     */
    public void fatal(final Object message) {
        if (!isLoggable(JCL_MAPPING_FATAL)) {
            return;
        }
        robustLog(JCL_MAPPING_FATAL, message, null);
    }

    /**
     * Refers to <code>java.util.logging.Level.SEVERE</code>.
     *
     * @see org.apache.commons.logging.Log#fatal(java.lang.Object,
     *      java.lang.Throwable)
     * @since 0.5
     */
    public void fatal(final Object message, final Throwable t) {
        if (!isLoggable(JCL_MAPPING_FATAL)) {
            return;
        }
        robustLog(JCL_MAPPING_FATAL, message, t);
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


}

// EOF JCLLogger.java
