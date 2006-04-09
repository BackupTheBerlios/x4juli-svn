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

package org.x4juli.performance;

import java.util.logging.LogRecord;

import org.x4juli.global.spi.ExtendedLogRecord;
import org.x4juli.handlers.AbstractHandler;


/**
 * Testcase for x4juli.
 * @since 0.5
 */
public class NOPHandler extends AbstractHandler {

    public static String s;
    public String t;
    /**
     * 
     */
    public NOPHandler() {
        //NOP
    }

    /**
     * @param handlerName
     */
    public NOPHandler(String handlerName) {
        super(handlerName);
    }

    /**
     * {@inheritDoc}
     * @since
     */
    protected void appendLogRecord(ExtendedLogRecord record) {
        if(getFormatter() != null){
            this.t = getFormatter().format((LogRecord)record);
            s = this.t;
        }
    }

    /**
     * {@inheritDoc}
     * @since
     */
    public void close() throws SecurityException {
        System.out.println("NOPHandler::close called");
    }

    /**
     * {@inheritDoc}
     * @since
     */
    public void flush() {
        //NOP
    }

    /**
     * {@inheritDoc}
     * @since
     */
    public String getFullQualifiedClassName() {
        // TODO Auto-generated method stub
        return "org.x4juli.performance.NOPHandler";
    }

}

// EOF NOPHandler.java
