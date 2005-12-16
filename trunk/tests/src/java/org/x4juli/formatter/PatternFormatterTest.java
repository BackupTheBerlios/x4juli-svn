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

package org.x4juli.formatter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.x4juli.formatter.PatternFormatter;
import org.x4juli.global.components.AbstractJuliTestCase;
import org.x4juli.global.helper.LoggerUtil;
import org.x4juli.handlers.TestHandler;


/**
 * Testcase for x4juli.
 * @since 0.5
 */
public class PatternFormatterTest extends AbstractJuliTestCase {

	static String patternLayout1 = "%m";

	static String patternLayout2 = "%m%n";

	static String patternLayout3 = "%c";

	static String patternLayout3a = "%c{2}";

	static String patternLayout4 = "%C";

	static String patternLayout4a = "%C{1}";

	static String patternLayout5 = "%F";

	static String patternLayout6 = "%l";

	static String patternLayout7 = "%r";

	static String patternLayout8 = "%M";

	static String patternLayout9 = "%p";

	static String patternLayout9a = "%p{default}";

	static String patternLayout9b = "%p{i18n}";

	static String patternLayout10 = "%t";

	static String patternLayout11 = "%throwable";

	static String patternLayout11a = "%throwable{short}";

	protected Logger testLogger = null;

	protected TestHandler testHandler = null;

	protected String actualPattern = null;

	private String lineSeparator = (String) java.security.AccessController
			.doPrivileged(new sun.security.action.GetPropertyAction(
					"line.separator"));

	public PatternFormatterTest(final String name) {
		super(name);
	}

	public void setUp() {
		this.testHandler = new TestHandler();
	}

	public void tearDown() {
		this.testLogger = null;
		this.actualPattern = null;
		super.tearDown();
	}

	public Logger getTestLogger() {
		if (testLogger == null) {
			PatternFormatter formatter = new PatternFormatter(actualPattern);
			try {
				testLogger = LoggerUtil.createLogger("testLogger", formatter,
						testHandler);
			} catch (Exception e) {
				String errtxt = "Error creating testLogger";
				getLogger().log(Level.SEVERE, errtxt, e);
				throw new RuntimeException(errtxt, e);
			}
		}
		return testLogger;
	}

	public void test1() throws Exception {
		this.actualPattern = patternLayout1;
		String testMessage = "Foo Test Message";
		testHandler.setExpected(testMessage);
		getTestLogger().info(testMessage);
		assertTrue(testHandler.compare());
	}

	public void test2() throws Exception {
		this.actualPattern = patternLayout2;
		String testMessage = "Foo Test Message";
		testHandler.setExpected(testMessage + lineSeparator);
		getTestLogger().info(testMessage);
		assertTrue(testHandler.compare());
	}

	public void test3() throws Exception {
		// Test for full category name of the Logger
		this.actualPattern = patternLayout3;
		String loggerName = "a.b.c.D";
		testHandler.setExpected(loggerName);
		Logger tstLog = LoggerUtil.createLogger(loggerName,
				new PatternFormatter(actualPattern), testHandler);
		tstLog.info("uninteresting String");
		assertTrue(testHandler.compare());

		// Test for part category name of the Logger
		tstLog = null;
		tearDown();
		setUp();
		this.actualPattern = patternLayout3a;
		testHandler.setExpected("c.D");
		tstLog = LoggerUtil.createLogger(loggerName, new PatternFormatter(
				actualPattern), testHandler);
		tstLog.info("uninteresting String");
		assertTrue(testHandler.compare());

	}

	public void test4() throws Exception {
		// Test for full category name of the Logger
		this.actualPattern = patternLayout4;
		String callerClass = PatternFormatterTest.class.getName();
		testHandler.setExpected(callerClass);
		getTestLogger().info("uninteresting String");
		assertTrue(testHandler.compare());

		// Test for part category name of the Logger
		tearDown();
		setUp();
		this.actualPattern = patternLayout4a;
		testHandler.setExpected("PatternFormatterTest");
		getTestLogger().info("uninteresting String");
		assertTrue(testHandler.compare());

	}

	public void test5() throws Exception {
		this.actualPattern = patternLayout5;
		testHandler.setExpected("PatternFormatterTest.java");
		getTestLogger().info("uninteresting String");
		assertTrue(testHandler.compare());
	}

	public void test6() throws Exception {
		this.actualPattern = patternLayout6;
		testHandler
				.setExpected("org.x4juli.formatter.PatternFormatterTest.test6(PatternFormatterTest.java:178)");
		getTestLogger().info("uninteresting String");
		assertTrue(testHandler.compare());
	}

	public void test7() throws Exception {
		this.actualPattern = patternLayout7;
		getTestLogger().info("uninteresting String");
		String result = testHandler.getActual();
		try {
			Long.parseLong(result);
		} catch (NumberFormatException e) {
			fail("Time contains no long, but[" + result + "]");
		}
	}

	public void test8() throws Exception {
		this.actualPattern = patternLayout8;
		getTestLogger().info("uninteresting String");
		testHandler.setExpected("test8");
		assertTrue(testHandler.compare());
	}

	public void test9() throws Exception {
		this.actualPattern = patternLayout9;
		getTestLogger().severe("uninteresting String");
		testHandler.setExpected(Level.SEVERE.getName());
		assertTrue(testHandler.compare());

		tearDown();
		setUp();
		this.actualPattern = patternLayout9a;
		getTestLogger().severe("uninteresting String");
		testHandler.setExpected(Level.SEVERE.getName());
		assertTrue(testHandler.compare());

		tearDown();
		setUp();
		this.actualPattern = patternLayout9b;
		getTestLogger().severe("uninteresting String");
		testHandler.setExpected(Level.SEVERE.getLocalizedName());
		assertTrue(testHandler.compare());
	}

	public void test10() throws Exception {
		this.actualPattern = patternLayout10;
		getTestLogger().info("uninteresting String");
		String result = testHandler.getActual();
		try {
			Integer.parseInt(result);
		} catch (NumberFormatException e) {
			fail("ThreadId contains no int, but[" + result + "]");
		}
		
	}

	public void test11() throws Exception {
		this.actualPattern = patternLayout11;
		Exception testEx = new Exception();
		ByteArrayOutputStream 	out = new ByteArrayOutputStream();
		PrintStream pst = new PrintStream(out);
        testEx.printStackTrace(pst);
		getTestLogger().log(Level.FINE,"uninteresting String",testEx);
		testHandler.setExpected(out.toString());
		assertTrue(testHandler.compare());
	}	
	
	public static Test suite() {
		TestSuite suite = new TestSuite();
        suite.setName(PatternFormatterTest.class.getName());
		suite.addTest(new PatternFormatterTest("test1"));
		suite.addTest(new PatternFormatterTest("test2"));
		suite.addTest(new PatternFormatterTest("test3"));
		suite.addTest(new PatternFormatterTest("test4"));
		suite.addTest(new PatternFormatterTest("test5"));
		suite.addTest(new PatternFormatterTest("test6"));
		suite.addTest(new PatternFormatterTest("test7"));
		suite.addTest(new PatternFormatterTest("test8"));
		suite.addTest(new PatternFormatterTest("test9"));
		suite.addTest(new PatternFormatterTest("test10"));
		suite.addTest(new PatternFormatterTest("test11"));

		return suite;
	}
}
