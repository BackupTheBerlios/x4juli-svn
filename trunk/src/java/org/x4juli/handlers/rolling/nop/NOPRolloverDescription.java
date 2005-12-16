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

import org.x4juli.handlers.rolling.RolloverDescriptionImpl;

/**
 * Implementation for no action during rollover.
 * @author Boris Unckel
 * @since 0.5
 */
final class NOPRolloverDescription extends RolloverDescriptionImpl {

    /**
     * Constructor with defaulting to append, NOPAction and null
     * for asyncrounos action.
     * @param activeFileName
     */
    public NOPRolloverDescription(final String activeFileName) {
        super(activeFileName, false, NOPAction.NOP_ACTION, null);
    }

}

// EOF NOPRolloverDescription.java
