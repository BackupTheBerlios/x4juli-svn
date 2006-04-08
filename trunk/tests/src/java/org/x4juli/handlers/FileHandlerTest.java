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

import java.io.File;

import org.x4juli.formatter.DummyFormatter;
import org.x4juli.global.components.AbstractHandler;
import org.x4juli.global.spi.ExtendedFormatter;
import org.x4juli.handlers.FileHandler;

/**
 * Testcase for x4juli.
 * @since 0.5
 */
public class FileHandlerTest extends AbstractHandlerTest {
    // -------------------------------------------------------------- Variables

    // ----------------------------------------------------------- Constructors
    /**
     * 
     */
    public FileHandlerTest() {
        super();
    }

    /**
     * @param name
     */
    public FileHandlerTest(String name) {
        super(name);
    }

    // --------------------------------------------------------- Public Methods
    
    
    // ------------------------------------------------------ Protected Methods

    /**
     * {@inheritDoc}
     * 
     * @since
     */
    protected AbstractHandler getHandler() {
        return new FileHandler("handler name");
    }

    /**
     * {@inheritDoc}
     * 
     * @since
     */
    protected AbstractHandler getConfiguredHandler() {
        FileHandler wa = new FileHandler("foo");
        wa.setFile("output/temp");
        wa.setFormatter((ExtendedFormatter)new DummyFormatter());
        wa.activateOptions();
        return wa;
    }
    
    public void testPartiallyConfiguredHandler() {
        FileHandler wa1 = new FileHandler("foo 2");
        wa1.setFile("output/temp");
        wa1.activateOptions();
        assertFalse(wa1.isActive());

        FileHandler wa2 = new FileHandler("foo 2a");
        wa2.setFormatter((ExtendedFormatter)new DummyFormatter());
        wa2.activateOptions();
        assertFalse(wa2.isActive());
      }

      /**
       * Tests that any necessary directories are attempted to
       * be created if they don't exist.  See bug 9150.
       *
       */
      public void testDirectoryCreation() {
          File newFile = new File("output/newdir/temp.log");
          newFile.delete();
          File newDir = new File("output/newdir");
          newDir.delete();

          FileHandler wa = new FileHandler("foo 3");
          wa.setFile("output/newdir/temp.log");
          wa.setFormatter((ExtendedFormatter)new DummyFormatter());
          wa.activateOptions();
          assertTrue(new File("output/newdir/temp.log").exists());
      }
}

// EOF FileHandlerTest.java
