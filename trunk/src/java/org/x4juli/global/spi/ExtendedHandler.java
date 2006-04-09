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

import java.util.logging.LogRecord;

/**
 * Specifies operations for any x4juli handler.
 * @since 0.5
 * @author Boris Unckel
 */
public interface ExtendedHandler extends Handler, LevelAttachable {

    /**
     * Determine performant the FullQualifiedClassName. Subclasses should
     * overwrite this method. Dynamic determination is not recommended.
     *
     * @return the FQCN
     * @since 0.5
     */
    String getFullQualifiedClassName();

    /**
     * @see org.x4juli.handlers.AbstractHandler#publish(LogRecord)
     * @param record to check and write in the log.
     * @since 0.5
     */
    void publish(final ExtendedLogRecord record);

    /**
     * Checks whether a record should be logged or not. Criteria: not null,
     * level, filter chain passed.
     *
     * @param record the record to check.
     * @return true if record fullfills criteria.
     * @since 0.5
     */
    boolean isLoggable(ExtendedLogRecord record);

    /**
     * Add a filter to end of the filter list.
     *
     * @param newFilter the filter to add
     * @since 0.5
     */
    void addFilter(final ExtendedFilter newFilter);

    /**
     * Clear the filters chain.
     *
     * @since 0.5
     */
    void clearFilters();

    /**
     * Returns true if this handler instance is closed.
     * @return true if this handler instance is closed.
     *
     * @since 0.5
     */
    boolean isClosed();

    /**
     * Returns true if this handler is working order.
     * @return true if this handler is working order.
     *
     * @since 0.5
     */
    boolean isActive();

    /**
     * Set the name of this Handler.
     * @param name of this handler.
     *
     * @since 0.5
     */
    void setName(String name);

    /**
     * Retrieve name of the Handler.
     *
     * @return the name of the Handler.
     * @since 0.5
     */
    String getName();
    
    /**
     * Sets the Formatter of the Handler.
     * @param formatter of this handler.
     * @since 0.7
     */
    void setFormatter(ExtendedFormatter formatter);
}

// EOF ExtendedHandler.java
