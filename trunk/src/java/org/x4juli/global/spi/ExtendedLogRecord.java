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

import org.x4juli.global.spi.location.LocationInfo;

/**
 * Interface to specify additional information for a
 * <code>java.util.logging.LogRecord</code>. This is needed i.e. for the
 * PatternFormatter.
 * 
 * @author Boris Unckel
 * @since 0.5
 */
public interface ExtendedLogRecord extends LogRecord, java.io.Serializable {

    /**
     * Provide extended information about the attached throwable. Returns null,
     * if there was no throwable in the LogRecord attached.
     * 
     * @return internal representation of Juli for the Throwable
     * @since 0.5
     */
    ThrowableInformation getThrowableInformation();

    /**
     * Provide extended information about the caller's location. These
     * information should respect the Logger class or any subclasses. (Make use
     * of the correct, corresponding FQCN).
     * 
     * @return internal representation of Juli for the Caller Location
     * @since 0.5
     */
    LocationInfo getLocationInformation();

    /**
     * Provide the time since start of logging.
     * 
     * @return the start time of Juli, determined by the first use of the
     *         implementor
     * @since 0.5
     */
    long getStartTime();
    
    /**
     * Setup the cache for the formatted message.
     * @param formattedMessage current state of the formatted message.
     */
    void setFormattedMessage(String formattedMessage);
    
    /**
     * Returns the cached formatted message.
     * @return the cached formatted message, if cache is empty, returns the message.
     */
    String getFormattedMessage();
    
    /**
     * Returns the actual FQCN of the (wrapper) class which submitted the logrecord. 
     * @return the full qualified class name of the logger class.
     */
    String getFQCNofLogger();
    
    /**
     * Sets the full qualified class name of the logger. This method is needed
     * for wrapper classes which are not able to directly inherit 
     * from {@link org.x4juli.global.components.AbstractExtendedLogger}.
     * @param fqcn of the logger submitting the logrecord to the system. 
     */
    void setFQCNofLogger(String fqcn);

}
