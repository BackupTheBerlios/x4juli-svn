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
import java.util.logging.Level;

/**
 * This Interface specifies the operations of <code>java.util.logging.LogRecord</code>
 * and allow Juli to use this without extending the original <code>java.util.logging.LogRecord</code>.
 * For proper functionality Juli uses the extended version of this.
 * It just has package visibilty to avoid outside world using it.
 * @author Boris Unckel
 * @since 0.5
 */
interface LogRecord {

    /**
     * @see java.util.logging.LogRecord#getLoggerName()
     * @since 0.5
     */
    String getLoggerName();

    /**
     * @see java.util.logging.LogRecord#setLoggerName(java.lang.String)
     * @since 0.5
     */
    void setLoggerName(String name);

    /**
     * @see java.util.logging.LogRecord#getResourceBundle()
     * @since 0.5
     */
    ResourceBundle getResourceBundle();

    /**
     * @see java.util.logging.LogRecord#setResourceBundle(java.util.ResourceBundle)
     * @since 0.5
     */
    void setResourceBundle(ResourceBundle bundle);

    /**
     * @see java.util.logging.LogRecord#getResourceBundleName()
     * @since 0.5
     */
    String getResourceBundleName();

    /**
     * @see java.util.logging.LogRecord#setResourceBundleName(java.lang.String)
     * @since 0.5
     */
    void setResourceBundleName(String name);

    /**
     * @see java.util.logging.LogRecord#getLevel()
     * @since 0.5
     */
    Level getLevel();

    /**
     * @see java.util.logging.LogRecord#setLevel(java.util.logging.Level)
     * @since 0.5
     */
    void setLevel(Level level);

    /**
     * @see java.util.logging.LogRecord#getSequenceNumber()
     * @since 0.5
     */
    long getSequenceNumber();

    /**
     * @see java.util.logging.LogRecord#setSequenceNumber(long)
     * @since 0.5
     */
    void setSequenceNumber(long seq);

    /**
     * @see java.util.logging.LogRecord#getSourceClassName()
     * @since 0.5
     */
    String getSourceClassName();

    /**
     * @see java.util.logging.LogRecord#setSourceClassName(java.lang.String)
     * @since 0.5
     */
    void setSourceClassName(String sourceClassName);

    /**
     * @see java.util.logging.LogRecord#getSourceMethodName()
     * @since 0.5
     */
    String getSourceMethodName();

    /**
     * @see java.util.logging.LogRecord#setSourceMethodName(java.lang.String)
     * @since 0.5
     */
    void setSourceMethodName(String sourceMethodName);

    /**
     * @see java.util.logging.LogRecord#getMessage()
     * @since 0.5
     */
    String getMessage();

    /**
     * @see java.util.logging.LogRecord#setMessage(java.lang.String)
     * @since 0.5
     */
    void setMessage(String message);

    /**
     * @see java.util.logging.LogRecord#getParameters()
     * @since 0.5
     */
    Object[] getParameters();

    /**
     * @see java.util.logging.LogRecord#setParameters(java.lang.Object[])
     * @since 0.5
     */
    void setParameters(Object parameters[]);

    /**
     * @see java.util.logging.LogRecord#getThreadID()
     * @since 0.5
     */
    int getThreadID();

    /**
     * @see java.util.logging.LogRecord#setThreadID(int)
     * @since 0.5
     */
    void setThreadID(int threadID);

    /**
     * @see java.util.logging.LogRecord#getMillis()
     * @since 0.5
     */
    long getMillis();

    /**
     * @see java.util.logging.LogRecord#setMillis(long)
     * @since 0.5
     */
    void setMillis(long millis);

    /**
     * @see java.util.logging.LogRecord#getThrown()
     * @since 0.5
     */
    Throwable getThrown();

    /**
     * @see java.util.logging.LogRecord#setThrown(java.lang.Throwable)
     * @since 0.5
     */
    void setThrown(Throwable thrown);

}
