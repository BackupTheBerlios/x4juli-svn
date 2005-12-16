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

import java.io.IOException;

import org.x4juli.handlers.rolling.helper.AbstractAction;

/**
 * Dummy Action which always signals succesfull execution.
 * Used just for default behaviour in RollingFileHandler.
 * @author Boris Unckel
 * @since 0.5
 */
final class NOPAction extends AbstractAction {

    public static final NOPAction NOP_ACTION = new NOPAction();
    
    /**
     * 
     */
    private NOPAction() {
        super();
    }

    /**
     * {@inheritDoc}
     * @since
     */
    public boolean execute() throws IOException {
        return true;
    }

}

// EOF NOPAction.java
