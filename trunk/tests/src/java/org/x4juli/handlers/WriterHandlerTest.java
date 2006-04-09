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

import java.io.CharArrayWriter;

import org.x4juli.formatter.DummyFormatter;
import org.x4juli.global.spi.ExtendedFormatter;
import org.x4juli.handlers.WriterHandler;

/**
 * Testcase for x4juli.
 * @since 0.5
 */
public class WriterHandlerTest extends AbstractHandlerTest {
    // -------------------------------------------------------------- Variables
    
    // ----------------------------------------------------------- Constructors
    /**
     * 
     */
    public WriterHandlerTest() {
        super();
    }

    /**
     * @param name
     */
    public WriterHandlerTest(String name) {
        super(name);
    }
    
    // --------------------------------------------------------- Public Methods

    protected AbstractHandler getHandler() {
        return new WriterHandler("handler name");
    }

    protected AbstractHandler getConfiguredHandler() {
        WriterHandler wa = new WriterHandler("another name");

        // set a bogus writer
        wa.setWriter(new CharArrayWriter());
        // set a bogus layout
        wa.setFormatter((ExtendedFormatter)new DummyFormatter());
        wa.activateOptions();
        return wa;
    }

    public void testPartiallyConfiguredHandler() {
        WriterHandler wa1 = new WriterHandler("foo");

        // set a bogus writer
        wa1.setWriter(new CharArrayWriter());
        wa1.activateOptions();
        assertFalse(wa1.isActive());

        WriterHandler wa2 = new WriterHandler("foo 2");

        // set a bogus writer
        wa2.setFormatter((ExtendedFormatter)new DummyFormatter());
        wa2.activateOptions();
        assertFalse(wa2.isActive());
      }
}

// EOF WriterHandlerTest.java
