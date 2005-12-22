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
package org.x4juli.global.components;

import java.util.logging.Logger;

import org.x4juli.global.spi.ExtendedLogRecord;
import org.x4juli.global.spi.ExtendedLogger;

/**
 * Basic implementation for implementing directly a log wrapper with an
 * interface.
 * 
 * @author Boris Unckel
 * @since 0.6
 */
public abstract class AbstractExtendedLogger extends Logger implements ExtendedLogger {

    /**
     * Constructs a logger with a specific resourcebundle.
     * @param name of the logger.
     * @param resourceBundleName for i18n messages.
     */
    protected AbstractExtendedLogger(final String name, final String resourceBundleName) {
        super(name, resourceBundleName);
    }

    /**
     * Adds the LoggerName and if available the resource bundle to the LogRecord.
     * @param logRecord to complete with common info.
     */
    protected void completeLogRecord(ExtendedLogRecord logRecord) {
        logRecord.setLoggerName(getName());
        String rbName = getResourceBundleName();
        if (rbName != null && logRecord.getResourceBundleName() == null) {
            logRecord.setResourceBundleName(rbName);
            logRecord.setResourceBundle(getResourceBundle());
        }
    }

}

// EOF AbstractExtendedLogger.java
