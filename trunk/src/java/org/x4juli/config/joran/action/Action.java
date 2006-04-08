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
package org.x4juli.config.joran.action;

import org.x4juli.config.joran.spi.ActionException;
import org.x4juli.config.joran.spi.ExecutionContext;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

/**
 * Most of the work for configuring log4j is done by Actions.
 * 
 * Methods of an Action are invoked while an XML file is parsed through.
 * 
 * @author Boris Unckel
 * @since 0.7
 */
public interface Action {
    static final String NAME_ATTRIBUTE = "name";

    static final String VALUE_ATTRIBUTE = "value";

    static final String FILE_ATTRIBUTE = "file";

    static final String CLASS_ATTRIBUTE = "class";

    static final String PATTERN_ATTRIBUTE = "pattern";

    static final String ACTION_CLASS_ATTRIBUTE = "actionClass";

    /**
     * Called when the parser first encounters an element.
     *
     * The return value indicates whether child elements should be processed. If
     * the returned value is 'false', then child elements are ignored.
     */
    void begin(ExecutionContext ec, String name, Attributes attributes) throws ActionException;

    void end(ExecutionContext ec, String name) throws ActionException;
}

// EOF Action.java
