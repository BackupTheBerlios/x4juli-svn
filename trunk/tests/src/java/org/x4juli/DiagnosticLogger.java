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
package org.x4juli;

import java.util.logging.Handler;
import java.util.logging.Logger;


/**
 * Offers more or direct information in testing.
 * @since 0.5
 */
public class DiagnosticLogger extends Logger {

    public DiagnosticLogger(String name) {
        super(name, null);
    }

    /**
     * @param name
     * @param resourceBundleName
     */
    public DiagnosticLogger(String name, String resourceBundleName) {
        super(name, resourceBundleName);
    }

    /**
     * {@inheritDoc}
     * 
     * @since
     */
    public String toString() {
        StringBuffer buf = new StringBuffer(super.toString());
        buf.append(":");
        buf.append("Name[");
        buf.append(getName());
        buf.append("] Level[");
        buf.append(getLevel());
        buf.append("] UseParentHandlers[");
        buf.append(getUseParentHandlers());
        buf.append("] Handlers[");
        Handler[] temp = this.getHandlers();
        for (int i = 0; i < temp.length; i++) {
            Handler handler = temp[i];
            buf.append(String.valueOf(handler));
            buf.append(";");
        }
        buf.append("]");
        if (getParent() != null) {
            buf.append(" ParentName[");
            Logger tempParent = this;
            for (int i = 0; i < 100; i++) {
                tempParent = tempParent.getParent();
                if (tempParent.getName() != null) {
                    buf.append(tempParent.getName());
                    break;
                }
            }
            buf.append("]");
        }
        return buf.toString();
    }

}

// EOF DiagnosticLogger.java
