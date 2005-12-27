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

package org.x4juli.global;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.x4juli.global.components.AbstractJuliTestCase;
import org.x4juli.global.context.NDCTest;
import org.x4juli.global.spi.ThrowableInformationTest;

/**
 * Testcase for x4juli.
 * @since 0.5
 */
public class AllTests extends AbstractJuliTestCase {

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
    
    public static Test suite(){
        TestSuite suite = new TestSuite("org.x4juli.global");
        suite.addTestSuite(ThrowableInformationTest.class);
        suite.addTestSuite(NDCTest.class);
        return suite;
    }

}

// EOF AllTests.java
