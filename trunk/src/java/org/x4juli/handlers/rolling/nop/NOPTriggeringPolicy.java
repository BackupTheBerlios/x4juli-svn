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

package org.x4juli.handlers.rolling.nop;

import java.util.logging.Handler;

import org.x4juli.global.spi.AbstractComponent;
import org.x4juli.global.spi.ExtendedLogRecord;
import org.x4juli.handlers.rolling.TriggeringPolicy;

/**
 *
 * @author Boris Unckel
 * @since
 */
public final class NOPTriggeringPolicy extends AbstractComponent implements TriggeringPolicy {

    public static final NOPTriggeringPolicy NOP_TRIGGERING_POLICY = new NOPTriggeringPolicy();
    
    /**
     * 
     */
    private NOPTriggeringPolicy() {
        super();
    }

    public boolean isTriggeringEvent(Handler handler, ExtendedLogRecord record, String filename, long fileLength) {
        return false;
    }

    public void activateOptions() {
        // NOP
    }

}

// EOF NOPTriggeringPolicy.java
