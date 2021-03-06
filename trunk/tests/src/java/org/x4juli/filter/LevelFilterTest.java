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

import org.x4juli.filter.LevelMatchFilter;
import org.x4juli.formatter.PatternFormatter;
import org.x4juli.global.components.AbstractJuliTestCase;
import org.x4juli.global.helper.LoggerUtil;
import org.x4juli.global.spi.ExtendedFormatter;
import org.x4juli.handlers.TestHandler;

/**
 * Testcase for x4juli.
 * @since 0.5
 */
public class LevelFilterTest extends AbstractJuliTestCase {

    protected Logger testLogger = null;

    protected TestHandler testHandler = null;
    
    private String lineSeparator = (String) java.security.AccessController
    .doPrivileged(new sun.security.action.GetPropertyAction(
            "line.separator"));


    /**
     * 
     */
    public LevelFilterTest() {
        super();
    }

    /**
     * @param name
     */
    public LevelFilterTest(String name) {
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
        testHandler.setLevel(Level.ALL);
        LevelMatchFilter lmFilter = new LevelMatchFilter();
        lmFilter.setLevelToMatch(Level.INFO.toString());
        lmFilter.setAcceptOnMatch(true);
        lmFilter.activateOptions();
        testHandler.addFilter(lmFilter);
        testLogger.addHandler((java.util.logging.Handler)testHandler);
        testLogger.setLevel(Level.ALL);
        
        testLogger.finest("-finest-");
        testLogger.info("-info-");
        testLogger.severe("-severe-");
        testHandler.setExpected("-info-"+lineSeparator);
        assertTrue("Must contain info",testHandler.compare());
    }

    public void testDecide1b(){
        testLogger = Logger.getLogger("parent");
        testLogger.setUseParentHandlers(false);
        LoggerUtil.removeAllHandlers(testLogger);
        PatternFormatter formatter = new PatternFormatter("%m%n");
        testHandler.setFormatter((ExtendedFormatter)formatter);
        testHandler.setLevel(Level.ALL);
        LevelMatchFilter lmFilter = new LevelMatchFilter();
        lmFilter.setLevelToMatch(Level.INFO.toString());
        lmFilter.setAcceptOnMatch(true);
        lmFilter.activateOptions();
        testHandler.setFilter(lmFilter);
        testLogger.addHandler((java.util.logging.Handler)testHandler);
        testLogger.setLevel(Level.ALL);
        
        testLogger.finest("-finest-");
        testLogger.info("-info-");
        testLogger.severe("-severe-");
        testHandler.setExpected("-info-"+lineSeparator);
        assertTrue("Must contain info",testHandler.compare());
    }
    
    public void testDecide2(){
        testLogger = Logger.getLogger("parent");
        testLogger.setUseParentHandlers(false);
        LoggerUtil.removeAllHandlers(testLogger);
        PatternFormatter formatter = new PatternFormatter("%m%n");
        testHandler.setFormatter((ExtendedFormatter)formatter);
        testLogger.addHandler((java.util.logging.Handler)testHandler);
        LevelMatchFilter lmFilter = new LevelMatchFilter();
        lmFilter.setLevelToMatch(Level.INFO.toString());
        lmFilter.setAcceptOnMatch(true);
        lmFilter.activateOptions();
        testLogger.setFilter(lmFilter);
        testLogger.setLevel(Level.ALL);
        
        testLogger.finest("-finest-");
        testLogger.info("-info-");
        testLogger.severe("-severe-");
        testHandler.setExpected("-info-"+lineSeparator);
        assertTrue("Must contain info",testHandler.compare());
    }

}

// EOF DenyAllFilterTest.java
