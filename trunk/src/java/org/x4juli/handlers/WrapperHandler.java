/*
 * Copyright 2006 x4juli.org.
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
package org.x4juli.handlers;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

import org.x4juli.global.spi.ExtendedLogRecord;

/**
 * Missing documentation.
 * @todo Missing documentation.
 * @author Boris Unckel
 * @since 0.7
 */
public class WrapperHandler extends AbstractHandler {

    final Handler wrappedHandler;
    
    /**
     * 
     */
    public WrapperHandler(Handler toWrap, String handlerName) {
        super(handlerName);
        this.wrappedHandler = toWrap;
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    protected void appendLogRecord(final ExtendedLogRecord record) {
        wrappedHandler.publish((LogRecord)record);
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void close() throws SecurityException {
        this.closed = true;
        this.wrappedHandler.close();
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void flush() {
        this.wrappedHandler.flush();
    }

}

// EOF WrapperHandler.java
