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
package org.x4juli.handlers;

import org.x4juli.global.components.AbstractJuliTestCase;


/**
 * Testcase for x4juli.
 * @since 0.5
 */
public abstract class AbstractHandlerTest extends AbstractJuliTestCase {
    // -------------------------------------------------------------- Variables

    // ----------------------------------------------------------- Constructors
    /**
     * 
     */
    public AbstractHandlerTest() {
        super();
    }

    /**
     * @param name
     */
    public AbstractHandlerTest(String name) {
        super(name);
    }

    // --------------------------------------------------------- Public Methods
    public void testNewAppender() {
        // new handlers whould be inactive
        AbstractHandler handler = getHandler();
        assertFalse(handler.isActive());
        assertFalse(handler.isClosed());

        handler.close();
        assertTrue(handler.isClosed());
    }

    public void testConfiguredHandler() {
        AbstractHandler handler = getConfiguredHandler();
        handler.activateOptions();
        assertTrue(handler.isActive());
        assertFalse(handler.isClosed());

        handler.close();
        assertTrue(handler.isClosed());
    }

    // ------------------------------------------------------ Protected Methods
    abstract protected AbstractHandler getHandler();

    abstract protected AbstractHandler getConfiguredHandler();

}

// EOF AbstractHandlerTest.java
