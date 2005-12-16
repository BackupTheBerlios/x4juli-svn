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

import java.util.logging.ErrorManager;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

interface Handler {

    /**
     * @see java.util.logging.Handler#publish(java.util.logging.LogRecord)
     * @since 0.5
     */
    void publish(LogRecord record);

    /**
     * @see java.util.logging.Handler#flush()
     * @since 0.5
     */
    void flush();

    /**
     * @see java.util.logging.Handler#close()
     * @since 0.5
     */
    void close() throws SecurityException;

    /**
     * @see java.util.logging.Handler#setFormatter(java.util.logging.Formatter)
     * @since 0.5
     */
    void setFormatter(Formatter newFormatter) throws SecurityException;

    /**
     * @see java.util.logging.Handler#getFormatter()
     * @since 0.5
     */
    Formatter getFormatter();

    /**
     * @see java.util.logging.Handler#setEncoding(java.lang.String)
     * @since 0.5
     */
    void setEncoding(String encoding) throws SecurityException,
            java.io.UnsupportedEncodingException;

    /**
     * @see java.util.logging.Handler#getEncoding()
     * @since 0.5
     */
    String getEncoding();

    /**
     * @see java.util.logging.Handler#setFilter(java.util.logging.Filter)
     * @since 0.5
     */
    void setFilter(Filter newFilter) throws SecurityException;

    /**
     * @see java.util.logging.Handler#getFilter()
     * @since 0.5
     */
    Filter getFilter();

    /**
     * @see java.util.logging.Handler#setErrorManager(java.util.logging.ErrorManager)
     * @since 0.5
     */
    void setErrorManager(ErrorManager em);

    /**
     * @see java.util.logging.Handler#getErrorManager()
     * @since 0.5
     */
    ErrorManager getErrorManager();

    /**
     * @see java.util.logging.Handler#setLevel(java.util.logging.Level)
     * @since 0.5
     */
    void setLevel(Level newLevel) throws SecurityException;

    /**
     * @see java.util.logging.Handler#getLevel()
     * @since 0.5
     */
    Level getLevel();

    /**
     * @see java.util.logging.Handler#isLoggable(java.util.logging.LogRecord)
     * @since 0.5
     */
    boolean isLoggable(LogRecord record);

}

// EOF HandlerIf.java
