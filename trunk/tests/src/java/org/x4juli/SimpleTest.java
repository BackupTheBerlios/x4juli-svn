/*
 * Copyright 2005, x4juli.org.
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

package org.x4juli;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import junit.framework.TestCase;

/**
 * Simple Output for tests with configuration files.
 * @since 0.5
 */
public class SimpleTest extends TestCase {

    /**
     * 
     */
    public SimpleTest() {
        super();
    }

    /**
     * @param name
     */
    public SimpleTest(String name) {
        super(name);
    }

    public static void main(String[] args) {
        SimpleTest simpleTest = new SimpleTest();
        simpleTest.testSimple1();
    }

    public void testSimple1() {
        System.out.println("Start testSimple1");
        Logger first = Logger.getLogger("sample");
        first.finest("Logging to logger[sample] using finest-level.");
        first.info("Logging to logger[sample] using info-level.");
        first.severe("Logging to logger[sample] using severe-level.");
        Logger logger = Logger.getLogger("sample.child");
        logger.finest("Logging to logger[sample.child] using finest-level.");
        logger.info("Logging to logger[sample.child] using info-level.");
        Logger another = Logger.getLogger("sample.child.child");
        another.finest("Logging to logger[sample.child.child] using finest-level.");
        another.info("Logging to logger[sample.child.child] using info-level.");
        another.log(Level.SEVERE,
                "Logging to logger[sample.child.child] using severe-level with an exception.",
                new Exception());
        Logger.global.finest("Logging to global Logger using finest-Level.");
        Logger.global.severe("Logging to global Logger using severe-Level.");
        Logger.getAnonymousLogger().finest("Logging to anonymous Logger using finest-Level.");
        Logger.getAnonymousLogger().severe("Logging to anonymous Logger using severe-Level.");
        if (LogManager.getLogManager() instanceof DiagnosticLogManager) {
            ((DiagnosticLogManager) LogManager.getLogManager()).printLogNodeTree();
        }
        System.out.println("End testSimple1");
    }

}

// EOF SimpleTest.java
