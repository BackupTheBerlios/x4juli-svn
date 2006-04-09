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


/**
 * This interface specifies the operations for Juli loggers.
 * Currently empty, at the moment is no need to extend the logger.
 * @author Boris Unckel
 * @since 0.6
 */
public interface ExtendedLogger extends Logger, LevelAttachable {

    /**
     * Logs a ExtendedLogRecord.
     * @param record to log.
     * @see java.util.logging.Logger#log(java.util.logging.LogRecord)
     * @since 0.6
     */
    void log(ExtendedLogRecord record);
    
    /**
     * Sets the LoggerRepository
     * @param repository where the logger belongs to.
     * @param security ensures just spi objects can call this method.
     * @since 0.6
     */
    void setLoggerRepository(LoggerRepository repository, SpiSecurity security);

    /**
     * Return the the {@link LoggerRepository} where this <code>ExtendedLogger</code>
     * is attached.
     * 
     * @return the attached repository
     * @since 0.7
     */
    public LoggerRepository getLoggerRepository();

}

// EOF ExtendedLogger.java
