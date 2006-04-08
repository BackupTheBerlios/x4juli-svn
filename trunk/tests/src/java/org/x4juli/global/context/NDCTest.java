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
package org.x4juli.global.context;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.x4juli.formatter.PatternFormatter;
import org.x4juli.global.components.AbstractJuliTestCase;
import org.x4juli.global.helper.LoggerUtil;
import org.x4juli.global.spi.ExtendedFormatter;
import org.x4juli.global.spi.NDC;
import org.x4juli.global.util.Compare;
import org.x4juli.handlers.FileHandler;

/**
 * Test for nested diagnostic context.
 * 
 * @since 0.7
 */
public class NDCTest extends AbstractJuliTestCase {
    Logger renameLogger;

    PatternFormatter formatter;

    /**
     * @param name
     */
    public NDCTest(String name) {
        super(name);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.renameLogger = Logger.getLogger("NDCTest");
        this.renameLogger.setLevel(Level.ALL);
        this.formatter = new PatternFormatter("%-5p %x - %m%n");
        this.formatter.activateOptions();
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    protected void tearDown() {
        super.tearDown();
    }

    public void testNDC1() throws Exception {
        FileHandler fh = new FileHandler("NDCTestHandler");
        fh.setFile("output/ndctest.txt");
        fh.setAppend(false);
        fh.setLevel(Level.ALL);
        fh.setFormatter((ExtendedFormatter)this.formatter);
        fh.activateOptions();
        LoggerUtil.removeAllHandlers(this.renameLogger);
        this.renameLogger.setUseParentHandlers(false);
        this.renameLogger.addHandler(fh);
        NDC myNDC = ContextFactory.getNestedDiagnosticContext();

        commonLog();
        myNDC.push("n1");
        commonLog();
        myNDC.push("n2");
        myNDC.push("n3");
        commonLog();
        myNDC.pop();
        commonLog();
        myNDC.clear();
        commonLog();

        fh.flush();
        assertTrue(Compare.compare("output/ndctest.txt", "witness/ndc/NDC.1"));
    }

    protected void commonLog() {
        renameLogger.fine("m1");
        renameLogger.info("m2");
        renameLogger.warning("m3");
        renameLogger.severe("m4");
    }

}

// EOF NDCTest.java
