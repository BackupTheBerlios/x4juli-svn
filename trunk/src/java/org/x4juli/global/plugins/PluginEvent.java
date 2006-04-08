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
package org.x4juli.global.plugins;

import java.util.EventObject;

/**
 * All Plugin events are encapsulated in this class, which
 * simply contains the source Plugin, but may in future include more
 * information.
 * <p>
 * Logging API as a whole was originally done for <a href="http://logging.apache.org/log4j/">Apache
 * log4j</a>. <b>x4uli</b> is a port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging APIs</a>. All
 * credits for initial idea, design, implementation, documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This file was originally
 * published by <i>Paul Smith</i>. Please use exclusively the <i>appropriate</i> mailing lists for
 * questions, remarks and contribution.
 * </p>
 * 
 * @author Boris Unckel
 * @since 0.7
 */
public class PluginEvent extends EventObject {

    /**
     * @param source The source plugin of the event.
     * @since 0.7
     */
    PluginEvent(Plugin source) {
      super(source);
    }

    /**
     * Returns the source Plugin of this event, which is simple
     * the getSource() method casted to Plugin for convenience.
     * @return Plugin source of this event
     * @since 0.7
     */
    public Plugin getPlugin() {
      return (Plugin) getSource();
    }

}

// EOF PluginEvent.java