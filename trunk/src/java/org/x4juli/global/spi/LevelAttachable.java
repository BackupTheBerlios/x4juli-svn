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
package org.x4juli.global.spi;

import java.util.logging.Level;

/**
 * Getter and Setter for the level parameter.
 * @author Boris Unckel
 * @since 0.7
 */
public interface LevelAttachable {

    /**
     * Sets the level of the underyling instance.
     * @param level to set.
     * @since 0.7
     */
    public void setLevel(Level level);
    
    /**
     * Obtains the current level object.
     * @return the current level, may be null.
     * @since 0.7
     */
    public Level getLevel();

    /**
     * Obtains the name of the object.
     * @return the name.
     * @since 0.7
     */
    public String getName();
    
}

// EOF LevelAttachable.java
