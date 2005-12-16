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
package org.x4juli.handlers;

import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * Testcase for x4juli.
 * @since 0.5
 */
public class AllTests extends TestCase {

    /**
     * 
     */
    public AllTests() {
        super();
    }

    /**
     * @param name
     */
    public AllTests(String name) {
        super(name);
    }
    
    public static TestSuite suite(){
        TestSuite suite = new TestSuite();
        suite.setName("AllTests of juli.handlers");
        suite.addTestSuite(WriterHandlerTest.class);
        suite.addTestSuite(FileHandlerTest.class);
        suite.addTestSuite(EncodingTest.class);
        suite.addTest(org.x4juli.handlers.rolling.AllTests.suite());
        return suite;
    }

}

// EOF AllTests.java
