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
package org.x4juli.global.spi;

import java.util.ResourceBundle;
import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * This Interface specifies the operations of <code>java.util.logging.Logger</code>
 * and allows Juli to use this without extending the original
 * <code>java.util.logging.Logger</code> in future versions.
 * For proper functionality Juli uses the extended version of this. It just has package
 * visibilty to avoid outside world using it.
 * @see org.x4juli.global.spi.ExtendedLogger
 * @author Boris Unckel
 * @since 0.6
 */
interface Logger {

    /**
     * @see java.util.logging.Logger#getResourceBundle()
     * @since 0.6
     */
    ResourceBundle getResourceBundle();

    /**
     * @see java.util.logging.Logger#getResourceBundleName()
     * @since 0.6
     */
    String getResourceBundleName();

    /**
     * @see java.util.logging.Logger#setFilter(java.util.logging.Filter)
     * @since 0.6
     */
    void setFilter(Filter newFilter) throws SecurityException;

    /**
     * @see java.util.logging.Logger#getFilter()
     * @since 0.6
     */
    Filter getFilter();

    /**
     * @see java.util.logging.Logger#log(java.util.logging.LogRecord)
     * @since 0.6
     */
    void log(LogRecord record);

    /**
     * @see java.util.logging.Logger#log(java.util.logging.Level, java.lang.String)
     * @since 0.6
     */
    void log(Level level, String msg);

    /**
     * @see java.util.logging.Logger#log(java.util.logging.Level, java.lang.String, java.lang.Object)
     * @since 0.6
     */
    void log(Level level, String msg, Object param1);

    /**
     * @see java.util.logging.Logger#log(java.util.logging.Level, java.lang.String, java.lang.Object[])
     * @since 0.6
     */
    void log(Level level, String msg, Object params[]);

    /**
     * @see java.util.logging.Logger#log(java.util.logging.Level, java.lang.String, java.lang.Throwable)
     * @since 0.6
     */
    void log(Level level, String msg, Throwable thrown);

    /**
     * @see java.util.logging.Logger#logp(java.util.logging.Level, java.lang.String, java.lang.String, java.lang.String)
     * @since 0.6
     */
    void logp(Level level, String sourceClass, String sourceMethod, String msg);

    /**
     * @see java.util.logging.Logger#logp(java.util.logging.Level, java.lang.String, java.lang.String, java.lang.String, java.lang.Object)
     * @since 0.6
     */
    void logp(Level level, String sourceClass, String sourceMethod, String msg, Object param1);

    /**
     * @see java.util.logging.Logger#logp(java.util.logging.Level, java.lang.String, java.lang.String, java.lang.String, java.lang.Object[])
     * @since 0.6
     */
    void logp(Level level, String sourceClass, String sourceMethod, String msg,
            Object params[]);

    /**
     * @see java.util.logging.Logger#logp(java.util.logging.Level, java.lang.String, java.lang.String, java.lang.String, java.lang.Throwable)
     * @since 0.6
     */
    void logp(Level level, String sourceClass, String sourceMethod, String msg,
            Throwable thrown);

    /**
     * @see java.util.logging.Logger#logrb(java.util.logging.Level, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     * @since 0.6
     */
    void logrb(Level level, String sourceClass, String sourceMethod, String bundleName,
            String msg);

    /**
     * @see java.util.logging.Logger#logrb(java.util.logging.Level, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Object)
     * @since 0.6
     */
    void logrb(Level level, String sourceClass, String sourceMethod, String bundleName,
            String msg, Object param1);

    /**
     * @see java.util.logging.Logger#logrb(java.util.logging.Level, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Object[])
     * @since 0.6
     */
    void logrb(Level level, String sourceClass, String sourceMethod, String bundleName,
            String msg, Object params[]);

    /**
     * @see java.util.logging.Logger#logrb(java.util.logging.Level, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Throwable)
     * @since 0.6
     */
    void logrb(Level level, String sourceClass, String sourceMethod, String bundleName,
            String msg, Throwable thrown);

    /**
     * @see java.util.logging.Logger#entering(java.lang.String, java.lang.String)
     * @since 0.6
     */
    void entering(String sourceClass, String sourceMethod);

    /**
     * @see java.util.logging.Logger#entering(java.lang.String, java.lang.String, java.lang.Object)
     * @since 0.6
     */
    void entering(String sourceClass, String sourceMethod, Object param1);

    /**
     * @see java.util.logging.Logger#entering(java.lang.String, java.lang.String, java.lang.Object[])
     * @since 0.6
     */
    void entering(String sourceClass, String sourceMethod, Object params[]);

    /**
     * @see java.util.logging.Logger#exiting(java.lang.String, java.lang.String)
     * @since 0.6
     */
    void exiting(String sourceClass, String sourceMethod);

    /**
     * @see java.util.logging.Logger#exiting(java.lang.String, java.lang.String, java.lang.Object)
     * @since 0.6
     */
    void exiting(String sourceClass, String sourceMethod, Object result);

    /**
     * @see java.util.logging.Logger#throwing(java.lang.String, java.lang.String, java.lang.Throwable)
     * @since 0.6
     */
    void throwing(String sourceClass, String sourceMethod, Throwable thrown);

    /**
     * @see java.util.logging.Logger#severe(java.lang.String)
     * @since 0.6
     */
    void severe(String msg);

    /**
     * @see java.util.logging.Logger#warning(java.lang.String)
     * @since 0.6
     */
    void warning(String msg);

    /**
     * @see java.util.logging.Logger#info(java.lang.String)
     * @since 0.6
     */
    void info(String msg);

    /**
     * @see java.util.logging.Logger#config(java.lang.String)
     * @since 0.6
     */
    void config(String msg);

    /**
     * @see java.util.logging.Logger#fine(java.lang.String)
     * @since 0.6
     */
    void fine(String msg);

    /**
     * @see java.util.logging.Logger#finer(java.lang.String)
     * @since 0.6
     */
    void finer(String msg);

    /**
     * @see java.util.logging.Logger#finest(java.lang.String)
     * @since 0.6
     */
    void finest(String msg);

    /**
     * @see java.util.logging.Logger#setLevel(java.util.logging.Level)
     * @since 0.6
     */
    void setLevel(Level newLevel) throws SecurityException;

    /**
     * @see java.util.logging.Logger#getLevel()
     * @since 0.6
     */
    Level getLevel();

    /**
     * @see java.util.logging.Logger#isLoggable(java.util.logging.Level)
     * @since 0.6
     */
    boolean isLoggable(Level level);

    /**
     * @see java.util.logging.Logger#getName()
     * @since 0.6
     */
    String getName();

    /**
     * @see java.util.logging.Logger#addHandler(java.util.logging.Handler)
     * @since 0.6
     */
    void addHandler(Handler handler) throws SecurityException;

    /**
     * @see java.util.logging.Logger#removeHandler(java.util.logging.Handler)
     * @since 0.6
     */
    void removeHandler(Handler handler) throws SecurityException;

    /**
     * @see java.util.logging.Logger#getHandlers()
     * @since 0.6
     */
    Handler[] getHandlers();

    /**
     * @see java.util.logging.Logger#setUseParentHandlers(boolean)
     * @since 0.6
     */
    void setUseParentHandlers(boolean useParentHandlers);

    /**
     * @see java.util.logging.Logger#getUseParentHandlers()
     * @since 0.6
     */
    boolean getUseParentHandlers();

    /**
     * @see java.util.logging.Logger#getParent()
     * @since 0.6
     */
    java.util.logging.Logger getParent();

    /**
     * @see java.util.logging.Logger#setParent(java.util.logging.Logger)
     * @since 0.6
     */
    void setParent(java.util.logging.Logger parent);

}
// EOF Logger.java
