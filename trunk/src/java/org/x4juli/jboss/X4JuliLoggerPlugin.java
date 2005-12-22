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
package org.x4juli.jboss;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.jboss.logging.LoggerPlugin;
import org.x4juli.global.spi.ExtendedLogRecord;
import org.x4juli.global.spi.ExtendedLogRecordImpl;

/**
 * A plugin for the <a href="http://www.jboss.org">JBoss Application Server</a> for better X4Juli
 * support. A native implementation (as done for JCL or SLF4J is not possible).
 * 
 * @author Boris Unckel
 * @since 0.7
 */
public class X4JuliLoggerPlugin implements LoggerPlugin {

    static {
        try {
            LogManager manager = LogManager.getLogManager();
            manager.getClass();
        } catch (Throwable t) {
            System.err.println("X4JuliLoggerPlugin: Error getting LogManager.");
            System.err.println("X4JuliLoggerPlugin: You have got system parameters or classpath or logging config problems.");
            t.printStackTrace();
            throw new RuntimeException("x4juli setup is wrong. Check system parameters, classpath and logging.properties",t);
        }
    }

    private static final Level JBOSS_MAPPING_TRACE = Level.FINEST;

    private static final Level JBOSS_MAPPING_DEBUG = Level.FINE;

    private static final Level JBOSS_MAPPING_INFO = Level.INFO;

    private static final Level JBOSS_MAPPING_WARN = Level.WARNING;

    private static final Level JBOSS_MAPPING_ERROR = Level.SEVERE;

    private static final Level JBOSS_MAPPING_FATAL = Level.SEVERE;

    private static final String JBOSS_FQCN = org.jboss.logging.Logger.class.getName();

    private Logger logger;

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void init(final String name) {
        this.logger = Logger.getLogger(name);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public boolean isTraceEnabled() {
        return this.logger.isLoggable(JBOSS_MAPPING_TRACE);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void trace(Object message) {
        if (!this.logger.isLoggable(JBOSS_MAPPING_TRACE)) {
            return;
        }
        ExtendedLogRecord logrecord = new ExtendedLogRecordImpl(JBOSS_MAPPING_TRACE, String
                .valueOf(message));
        logrecord.setFQCNofLogger(JBOSS_FQCN);
        this.logger.log((LogRecord) logrecord);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void trace(Object message, Throwable t) {
        if (!this.logger.isLoggable(JBOSS_MAPPING_TRACE)) {
            return;
        }
        ExtendedLogRecord logrecord = new ExtendedLogRecordImpl(JBOSS_MAPPING_TRACE, String
                .valueOf(message));
        logrecord.setFQCNofLogger(JBOSS_FQCN);
        logrecord.setThrown(t);
        this.logger.log((LogRecord) logrecord);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public boolean isDebugEnabled() {
        return this.logger.isLoggable(JBOSS_MAPPING_DEBUG);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void debug(Object message) {
        if (!this.logger.isLoggable(JBOSS_MAPPING_DEBUG)) {
            return;
        }
        ExtendedLogRecord logrecord = new ExtendedLogRecordImpl(JBOSS_MAPPING_DEBUG, String
                .valueOf(message));
        logrecord.setFQCNofLogger(JBOSS_FQCN);
        this.logger.log((LogRecord) logrecord);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void debug(Object message, Throwable t) {
        if (!this.logger.isLoggable(JBOSS_MAPPING_DEBUG)) {
            return;
        }
        ExtendedLogRecord logrecord = new ExtendedLogRecordImpl(JBOSS_MAPPING_DEBUG, String
                .valueOf(message));
        logrecord.setFQCNofLogger(JBOSS_FQCN);
        logrecord.setThrown(t);
        this.logger.log((LogRecord) logrecord);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public boolean isInfoEnabled() {
        return this.logger.isLoggable(JBOSS_MAPPING_INFO);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void info(Object message) {
        if (!this.logger.isLoggable(JBOSS_MAPPING_INFO)) {
            return;
        }
        ExtendedLogRecord logrecord = new ExtendedLogRecordImpl(JBOSS_MAPPING_INFO, String
                .valueOf(message));
        logrecord.setFQCNofLogger(JBOSS_FQCN);
        this.logger.log((LogRecord) logrecord);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void info(Object message, Throwable t) {
        if (!this.logger.isLoggable(JBOSS_MAPPING_INFO)) {
            return;
        }
        ExtendedLogRecord logrecord = new ExtendedLogRecordImpl(JBOSS_MAPPING_INFO, String
                .valueOf(message));
        logrecord.setFQCNofLogger(JBOSS_FQCN);
        logrecord.setThrown(t);
        this.logger.log((LogRecord) logrecord);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void warn(Object message) {
        if (!this.logger.isLoggable(JBOSS_MAPPING_WARN)) {
            return;
        }
        ExtendedLogRecord logrecord = new ExtendedLogRecordImpl(JBOSS_MAPPING_WARN, String
                .valueOf(message));
        logrecord.setFQCNofLogger(JBOSS_FQCN);
        this.logger.log((LogRecord) logrecord);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void warn(Object message, Throwable t) {
        if (!this.logger.isLoggable(JBOSS_MAPPING_WARN)) {
            return;
        }
        ExtendedLogRecord logrecord = new ExtendedLogRecordImpl(JBOSS_MAPPING_WARN, String
                .valueOf(message));
        logrecord.setFQCNofLogger(JBOSS_FQCN);
        logrecord.setThrown(t);
        this.logger.log((LogRecord) logrecord);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void error(Object message) {
        if (!this.logger.isLoggable(JBOSS_MAPPING_ERROR)) {
            return;
        }
        ExtendedLogRecord logrecord = new ExtendedLogRecordImpl(JBOSS_MAPPING_ERROR, String
                .valueOf(message));
        logrecord.setFQCNofLogger(JBOSS_FQCN);
        this.logger.log((LogRecord) logrecord);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void error(Object message, Throwable t) {
        if (!this.logger.isLoggable(JBOSS_MAPPING_ERROR)) {
            return;
        }
        ExtendedLogRecord logrecord = new ExtendedLogRecordImpl(JBOSS_MAPPING_ERROR, String
                .valueOf(message));
        logrecord.setFQCNofLogger(JBOSS_FQCN);
        logrecord.setThrown(t);
        this.logger.log((LogRecord) logrecord);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void fatal(Object message) {
        if (!this.logger.isLoggable(JBOSS_MAPPING_FATAL)) {
            return;
        }
        ExtendedLogRecord logrecord = new ExtendedLogRecordImpl(JBOSS_MAPPING_FATAL, String
                .valueOf(message));
        logrecord.setFQCNofLogger(JBOSS_FQCN);
        this.logger.log((LogRecord) logrecord);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void fatal(Object message, Throwable t) {
        if (!this.logger.isLoggable(JBOSS_MAPPING_FATAL)) {
            return;
        }
        ExtendedLogRecord logrecord = new ExtendedLogRecordImpl(JBOSS_MAPPING_FATAL, String
                .valueOf(message));
        logrecord.setFQCNofLogger(JBOSS_FQCN);
        logrecord.setThrown(t);
        this.logger.log((LogRecord) logrecord);
    }

}

// EOF X4JuliLoggerPlugin.java
