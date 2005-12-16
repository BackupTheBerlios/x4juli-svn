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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * The testsuite for most tests for x4juli.
 * @since 0.5
 */
public class X4JuliSuite extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(X4JuliSuite.class);
	}

	public static Test suite(){
		TestSuite suite = new TestSuite();
        suite.setName("AllTests of Juli");
        suite.addTestSuite(MinimumTest.class);
        suite.addTest(org.x4juli.formatter.AllTests.suite());        
        suite.addTest(org.x4juli.handlers.AllTests.suite());
        suite.addTest(org.x4juli.global.AllTests.suite());
		return suite;
	}
	
}
