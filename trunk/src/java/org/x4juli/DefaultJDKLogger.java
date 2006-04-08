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
package org.x4juli;

import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.x4juli.global.components.AbstractExtendedLogger;
import org.x4juli.global.helper.LoggerUtil;
import org.x4juli.global.spi.ExtendedLogRecord;
import org.x4juli.global.spi.ExtendedLogRecordImpl;

/**
 * Logger representing the <code>java.util.logging.Logger</code> without
 * any native implementation of other Log(ger) interfaces. Used for
 * the DefaultJDKLoggerFactory.
 * @author Boris Unckel
 * @since 0.7
 */
class DefaultJDKLogger extends AbstractExtendedLogger {

    /**
     * @param name
     * @param resourceBundleName
     */
    public DefaultJDKLogger(String name, String resourceBundleName) {
        super(name, resourceBundleName);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.7
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
     * @since 0.7
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
     * @since 0.7
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
     * @since 0.7
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
     * @since 0.7
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
     * @since 0.7
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
     * @since 0.7
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
     * @since 0.7
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
     * @since 0.7
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
     * @since 0.7
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
     * @since 0.7
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
     * @since 0.7
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
     * @since 0.7
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
     * @since 0.7
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
     * @since 0.7
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
     * @since 0.7
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
     * @since 0.7
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
     * @since 0.7
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
     * @since 0.7
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
     * @since 0.7
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
     * @since 0.7
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
     * @since 0.7
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
     * @since 0.7
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
     * @since 0.7
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
     * @since 0.7
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
     * @since 0.7
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
     * @since 0.7
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

}

// EOF DefaultJDKLogger.java
