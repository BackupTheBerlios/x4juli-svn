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
package org.x4juli.filter;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.x4juli.filter.DenyAllFilter;
import org.x4juli.formatter.PatternFormatter;
import org.x4juli.global.components.AbstractJuliTestCase;
import org.x4juli.global.helper.LoggerUtil;
import org.x4juli.global.spi.ExtendedFormatter;
import org.x4juli.handlers.TestHandler;

/**
 * Testcase for x4juli.
 * @since 0.5
 */
public class DenyAllFilterTest extends AbstractJuliTestCase {

    protected Logger testLogger = null;

    protected TestHandler testHandler = null;
    
    private String lineSeparator = (String) java.security.AccessController
    .doPrivileged(new sun.security.action.GetPropertyAction(
            "line.separator"));


    /**
     * 
     */
    public DenyAllFilterTest() {
        super();
    }

    /**
     * @param name
     */
    public DenyAllFilterTest(String name) {
        super(name);
    }
    
    public void setUp() {
        this.testHandler = new TestHandler();
    }

    public void tearDown() {
        this.testLogger = null;
        super.tearDown();
    }

    public void testDecide1(){
        testLogger = Logger.getLogger("parent");
        testLogger.setUseParentHandlers(false);
        LoggerUtil.removeAllHandlers(testLogger);
        PatternFormatter formatter = new PatternFormatter("%m%n");
        testHandler.setFormatter((ExtendedFormatter)formatter);
        testHandler.addFilter(new DenyAllFilter());
        testLogger.addHandler((java.util.logging.Handler)testHandler);
        testLogger.setLevel(Level.ALL);
        
        testLogger.finest("-finest-");
        testLogger.severe("-severe-");
        testHandler.setExpected("");
        assertTrue("Handler must be empty",testHandler.compare());
    }

    public void testDecide1b(){
        testLogger = Logger.getLogger("parent");
        testLogger.setUseParentHandlers(false);
        LoggerUtil.removeAllHandlers(testLogger);
        PatternFormatter formatter = new PatternFormatter("%m%n");
        testHandler.setFormatter((ExtendedFormatter)formatter);
        testHandler.setFilter(new DenyAllFilter());
        testLogger.addHandler((java.util.logging.Handler)testHandler);
        testLogger.setLevel(Level.ALL);
        
        testLogger.finest("-finest-");
        testLogger.severe("-severe-");
        testHandler.setExpected("");
        assertTrue("Handler must be empty",testHandler.compare());
    }
    
    public void testDecide2(){
        testLogger = Logger.getLogger("parent");
        testLogger.setUseParentHandlers(false);
        LoggerUtil.removeAllHandlers(testLogger);
        PatternFormatter formatter = new PatternFormatter("%m%n");
        testHandler.setFormatter((ExtendedFormatter)formatter);
        testLogger.addHandler((java.util.logging.Handler)testHandler);
        testLogger.setFilter(new DenyAllFilter());
        testLogger.setLevel(Level.ALL);
        
        testLogger.finest("-finest-");
        testLogger.severe("-severe-");
        testHandler.setExpected("");
        assertTrue("Handler must be empty",testHandler.compare());
    }

}

// EOF DenyAllFilterTest.java
