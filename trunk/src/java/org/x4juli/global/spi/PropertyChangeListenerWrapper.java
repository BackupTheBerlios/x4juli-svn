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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.x4juli.ClassLoaderLogManager;

/**
 * Wrapps an PropertyChangeListener into an LoggerRepositoryEventListener.
 * @todo Missing documentation.
 * @author Boris Unckel
 * @since 0.7
 */
public class PropertyChangeListenerWrapper implements LoggerRepositoryEventListener {

    private final PropertyChangeListener pcListener;
    
    /**
     * 
     */
    public PropertyChangeListenerWrapper(final PropertyChangeListener propertyChangeListener) {
        super();
        this.pcListener = propertyChangeListener;
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void configurationChangedEvent(LoggerRepository repository) {
        this.pcListener.propertyChange(new PropertyChangeEvent(ClassLoaderLogManager.class,null,null,null));
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void configurationResetEvent(LoggerRepository repository) {
        // NOP
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void shutdownEvent(LoggerRepository repository) {
        // NOP
    }


}

// EOF PropertyChangeListenerWrapper.java
