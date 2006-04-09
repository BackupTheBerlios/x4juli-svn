/*
 * Copyright 1999,2005 The Apache Software Foundation.
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
package org.x4juli.logger;

import org.x4juli.global.spi.ExtendedLogRecord;


/**
 * Missing documentation.
 * @todo Missing documentation.
 * @author Boris Unckel
 * @since 0.7
 */
public class ExtendedLoggerWrapper extends AbstractExtendedLogger {

    private final java.util.logging.Logger wrappedLogger;
    
    /**
     * Constructor wrapping the original Logger.
     * @param toWrap the logger to wrap.
     */
    public ExtendedLoggerWrapper(java.util.logging.Logger toWrap) {
        super(toWrap.getName(), toWrap.getResourceBundleName());
        this.wrappedLogger = toWrap;
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void log(ExtendedLogRecord record) {
        this.wrappedLogger.log((java.util.logging.LogRecord) record);
    }

}

// EOF ExtendedLoggerWrapper.java
