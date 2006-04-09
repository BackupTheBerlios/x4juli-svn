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
package org.x4juli.global.helper;

import java.util.logging.ConsoleHandler;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.x4juli.formatter.SimpleFormatter;
import org.x4juli.global.spi.AbstractComponent;
import org.x4juli.global.spi.ExtendedLogRecord;
import org.x4juli.global.spi.ExtendedLogRecordWrapper;
import org.x4juli.global.spi.ExtendedLogger;
import org.x4juli.global.spi.OptionHandler;

/**
 * @author Boris Unckel
 *
 */
public final class LoggerUtil extends AbstractComponent {

    // -------------------------------------------------------------- Variables
    private static final LoggerUtil INSTANCE = new LoggerUtil();

    // ------------------------------------------------------------ Constructor

    /**
     * No instanciation wanted
     */
    private LoggerUtil() {
        //NOP
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Wraps an LogRecord into an ExtendedLogRecord.
     * @param record to wrap.
     * @return an ExtendedLogRecord with the same content, maybe null if record is null.
     */
    public static ExtendedLogRecord wrapLogRecord(final LogRecord record){
        if(record == null){
            return null;
        }
        if(record instanceof ExtendedLogRecord){
            return (ExtendedLogRecord) record;
        }
        ExtendedLogRecord ret = new ExtendedLogRecordWrapper(record);
        return ret;
    }
    
    /**
     * Removes all handlers of an existing logger.
     * @param logger to remove the handlers from.
     * @since 0.5
     */
    public static void removeAllHandlers(final Logger logger) {
        Handler[] allHandler = logger.getHandlers();
//        getInternalLogger().log(Level.FINEST, "logger handler count[{0}]", new Integer(allHandler.length));
        for (int i = 0; i < allHandler.length; i++) {
            Handler handler = allHandler[i];
            logger.removeHandler(handler);
        }
//        if (getInternalLogger().isLoggable(Level.FINEST)) {
//            allHandler = logger.getHandlers();
//            getInternalLogger().log(Level.FINEST, "logger handler after remove count[{0}]",
//                    new Integer(allHandler.length));
//        }
    }

    /**
     * Removes all handlers of an existing logger.
     * @param logger to remove the handlers from.
     * @since 0.5
     */
    public static void removeAllHandlers(final ExtendedLogger logger) {
        Handler[] allHandler = logger.getHandlers();
//        getInternalLogger().log(Level.FINEST, "logger handler count[{0}]", new Integer(allHandler.length));
        for (int i = 0; i < allHandler.length; i++) {
            Handler handler = allHandler[i];
            logger.removeHandler(handler);
        }
//        if (getInternalLogger().isLoggable(Level.FINEST)) {
//            allHandler = logger.getHandlers();
//            getInternalLogger().log(Level.FINEST, "logger handler after remove count[{0}]",
//                    new Integer(allHandler.length));
//        }
    }

    /**
     * Retrieves a new logger and configures it with the following options.
     * @see #createLogger(String, Formatter, Handler, Filter, Level, boolean)
     * @see #configureLogger(Logger, Formatter, Handler, Filter, Level, boolean)
     * @since 0.5
     */
    public static Logger createLogger(final Class clazz, final Formatter formatter,
            final Handler handler) {
        Logger logger = Logger.getLogger(clazz.getName());
        return configureLogger(logger, formatter, handler, null, Level.ALL, false);
    }

    /**
     * Retrieves a new logger and configures it with the following options.
     * @see #createLogger(String, Formatter, Handler, Filter, Level, boolean)
     * @see #configureLogger(Logger, Formatter, Handler, Filter, Level, boolean)
     * @since 0.5
     */
    public static Logger createLogger(final String logName) {
        Logger logger = Logger.getLogger(logName);
        return configureLogger(logger);
    }

    /**
     * Retrieves a new logger and configures it with the following options.
     * @see #createLogger(String, Formatter, Handler, Filter, Level, boolean)
     * @see #configureLogger(Logger, Formatter, Handler, Filter, Level, boolean)
     * @since 0.5
     */
    public static Logger createLogger(final String logName, final Formatter formatter) {
        Logger logger = Logger.getLogger(logName);
        return configureLogger(logger, formatter);
    }

    /**
     * Retrieves a new logger and configures it with the following options.
     * @see #createLogger(String, Formatter, Handler, Filter, Level, boolean)
     * @see #configureLogger(Logger, Formatter, Handler, Filter, Level, boolean)
     * @since 0.5
     */
    public static Logger createLogger(final String logName, final Formatter formatter,
            final Handler handler) {
        Logger logger = Logger.getLogger(logName);
        return configureLogger(logger, formatter, handler);
    }

    /**
     * Retrieves a new logger and configures it with the following options.
     * @see #createLogger(String, Formatter, Handler, Filter, Level, boolean)
     * @see #configureLogger(Logger, Formatter, Handler, Filter, Level, boolean)
     * @since 0.5
     */
    public static Logger createLogger(final String logName, final Formatter formatter,
            final Handler handler, final Filter filter) {
        Logger logger = Logger.getLogger(logName);
        return configureLogger(logger, formatter, handler, filter);
    }

    /**
     * Retrieves a new logger and configures it with the following options.
     * @see #createLogger(String, Formatter, Handler, Filter, Level, boolean)
     * @see #configureLogger(Logger, Formatter, Handler, Filter, Level, boolean)
     * @since 0.5
     */
    public static Logger createLogger(final String logName, final Formatter formatter,
            final Handler handler, final Filter filter, final Level level) {
        Logger logger = Logger.getLogger(logName);
        return configureLogger(logger, formatter, handler, filter, level);
    }

    /**
     * Retrieves a new logger and configures it with the following options.
     * @param logName of the retrieved logger.
     * @param formatter to use with the handler.
     * @param handler to use with the logger.
     * @param filter to use with the handler.
     * @param level to use with logger and handler.
     * @param addHandlers to remove existing handlers or not.
     * @return the configured logger.
     * @see #configureLogger(Logger, Formatter, Handler, Filter, Level, boolean)
     * @since 0.5
     */
    public static Logger createLogger(final String logName, final Formatter formatter,
            final Handler handler, final Filter filter, final Level level, final boolean addHandlers) {
        Logger logger = Logger.getLogger(logName);
        return configureLogger(logger, formatter, handler, filter, level, addHandlers);
    }

    /**
     * Configures the logger. Details:<br/>
     * @see #configureLogger(Logger, Formatter, Handler, Filter, Level, boolean)
     * @since 0.5
     */
    public static Logger configureLogger(final Logger logger) {
        return configureLogger(logger, new SimpleFormatter());
    }

    /**
     * Configures the logger. Details:<br/>
     * @see #configureLogger(Logger, Formatter, Handler, Filter, Level, boolean)
     * @since 0.5
     */
    public static Logger configureLogger(final Logger logger, final Formatter formatter) {
        return configureLogger(logger, formatter, new ConsoleHandler());
    }

    /**
     * Configures the logger. Details:<br/>
     * @see #configureLogger(Logger, Formatter, Handler, Filter, Level, boolean)
     * @since 0.5
     */
    public static Logger configureLogger(final Logger logger, final Formatter formatter,
            final Handler handler) {
        return configureLogger(logger, formatter, handler, null);
    }

    /**
     * Configures the logger. Details:<br/>
     * @see #configureLogger(Logger, Formatter, Handler, Filter, Level, boolean)
     * @since 0.5
     */
    public static Logger configureLogger(final Logger logger, final Formatter formatter,
            final Handler handler, final Filter filter) {
        return configureLogger(logger, formatter, handler, filter, Level.ALL);
    }

    /**
     * Configures the logger. Details:<br/>
     * @see #configureLogger(Logger, Formatter, Handler, Filter, Level, boolean)
     * @since 0.5
     */
    public static Logger configureLogger(final Logger logger, final Formatter formatter,
            final Handler handler, final Filter filter, final Level level) {
        return configureLogger(logger, formatter, handler, filter, level, false);
    }

    /**
     * Configures a new logger with the following options.
     * @param logger to configure.
     * @param formatter to use with the appender,
     *        activate options is called, SimpleFormatter is default.
     * @param handler to use with the logger, ConsoleHandler is default.
     * @param filter to use with the handler, null is default.
     * @param level to use with logger and handler, Level.ALL is default.
     * @param addHandlers to remove all other handlers or not.
     * @return the configured logger.
     * @since 0.5
     */
    public static Logger configureLogger(final Logger logger, final Formatter formatter,
            final Handler handler, final Filter filter, final Level level, final boolean addHandlers) {
        if (!addHandlers) {
            removeAllHandlers(logger);
        }
        handler.setLevel(level);
        handler.setFormatter(formatter);
        handler.setFilter(filter);
        if (handler instanceof OptionHandler) {
            ((OptionHandler) handler).activateOptions();
        }
        logger.setLevel(level);
        logger.addHandler(handler);
        return logger;
    }

    // ------------------------------------------------------ Protected Methods

    // -------------------------------------------------------- Private Methods

    /**
     * Retrieves the internal logger.
     * @return internal Logger for this class
     * @since 0.5
     */
    private static ExtendedLogger getInternalLogger() {
        return LoggerUtil.INSTANCE.getLogger();
    }

}
