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

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.x4juli.X4JuliLogger;
import org.x4juli.global.LoggerClassInformation;
import org.x4juli.global.SystemUtils;
import org.x4juli.global.spi.ThrowableInformation;

/**
 * Testcase for x4juli.
 * @since 0.5
 */
public class X4JuliLoggerTest extends AbstractDifferentLoggerTest {

	public X4JuliLoggerTest(String name, Level testLevel) {
        super(name, testLevel);
	}

	/**
	 * The method returns the logger for the concrete tests.
	 * The implementation with the cast is just acceptable for
	 * a testsuite. This should not be done in productive systems.
	 * @return the logger.
	 */
	protected Log getTestLogger(){
		return (Log) testLog;
	}
	
	public void testGetLog() {
		final String neededLogmanager = "org.x4juli.X4JuliLogManager";
		final String logmanagerProp = (String) java.security.AccessController
				.doPrivileged(new sun.security.action.GetPropertyAction(
						"java.util.logging.manager"));
		assertNotNull(logmanagerProp);
		assertEquals("LogManager not properly configured expected["
				+ neededLogmanager + "] actual[" + logmanagerProp + "]",
				neededLogmanager, logmanagerProp);

		Logger myLog = Logger.getLogger("demo123");
		// Test on correct interface
        assertNotNull(myLog);
		assertTrue(myLog instanceof org.apache.commons.logging.Log);
        assertTrue(myLog instanceof org.slf4j.Logger);

        final String jclFactoryProp = (String) java.security.AccessController
        .doPrivileged(new sun.security.action.GetPropertyAction(
        "org.apache.commons.logging.LogFactory"));
        if(jclFactoryProp != null && jclFactoryProp.equals("org.x4juli.JCLFactory")){
            Log myThirdLog = LogFactory.getLog("demo123");
            assertSame(myLog, myThirdLog);
        }
	}

	public void testIsTraceEnabled() {
        assertNotNull(getTestLogger());
		assertTrue(getTestLogger().isTraceEnabled());
		assertTrue(getTestLogger().isDebugEnabled());
		assertTrue(getTestLogger().isInfoEnabled());
		assertTrue(getTestLogger().isWarnEnabled());
		assertTrue(getTestLogger().isErrorEnabled());
		assertTrue(getTestLogger().isFatalEnabled());

	}

	public void testIsDebugEnabled() {
        assertNotNull(getTestLogger());
        assertFalse(getTestLogger().isTraceEnabled());
		assertTrue(getTestLogger().isDebugEnabled());
		assertTrue(getTestLogger().isInfoEnabled());
		assertTrue(getTestLogger().isWarnEnabled());
		assertTrue(getTestLogger().isErrorEnabled());
		assertTrue(getTestLogger().isFatalEnabled());
	}

	public void testIsInfoEnabled() {
        assertNotNull(getTestLogger());
		assertFalse(getTestLogger().isTraceEnabled());
		assertFalse(getTestLogger().isDebugEnabled());
		assertTrue(getTestLogger().isInfoEnabled());
		assertTrue(getTestLogger().isWarnEnabled());
		assertTrue(getTestLogger().isErrorEnabled());
		assertTrue(getTestLogger().isFatalEnabled());
	}

	public void testIsWarnEnabled() {
        assertNotNull(getTestLogger());
        assertFalse(getTestLogger().isTraceEnabled());
		assertFalse(getTestLogger().isDebugEnabled());
		assertFalse(getTestLogger().isInfoEnabled());
		assertTrue(getTestLogger().isWarnEnabled());
		assertTrue(getTestLogger().isErrorEnabled());
		assertTrue(getTestLogger().isFatalEnabled());
	}

	public void testIsErrorEnabled() {
        assertNotNull(getTestLogger());
        assertFalse(getTestLogger().isTraceEnabled());
		assertFalse(getTestLogger().isDebugEnabled());
		assertFalse(getTestLogger().isInfoEnabled());
		assertFalse(getTestLogger().isWarnEnabled());
		assertTrue(getTestLogger().isErrorEnabled());
		assertTrue(getTestLogger().isFatalEnabled());
	}

	public void testIsFatalEnabled() {
        assertNotNull(getTestLogger());
        assertFalse(getTestLogger().isTraceEnabled());
		assertFalse(getTestLogger().isDebugEnabled());
		assertFalse(getTestLogger().isInfoEnabled());
		assertFalse(getTestLogger().isWarnEnabled());
		assertTrue(getTestLogger().isErrorEnabled());
		assertTrue(getTestLogger().isFatalEnabled());
	}

	/*
	 * Class under test for void trace(Object)
	 */
	public void testTraceObject() {
        assertNotNull(getTestLogger());
        final String msg = "testmessage";
		getTestLogger().trace(msg);
		assertEquals(msg, testHandler.getActual());
	}

	public String prepareTestObjectThrowable(String msg, Exception ex) {
		ThrowableInformation ti = new ThrowableInformation(ex);
		String[] stringRep = ti.getThrowableStrRep();
		StringBuffer buf = new StringBuffer(msg);
		for (int i = 0; i < stringRep.length; i++) {
			String string = stringRep[i];
			buf.append(string).append(SystemUtils.LINE_SEPARATOR);
		}
		return buf.toString();
	}

	/*
	 * Class under test for void trace(Object, Throwable)
	 */
	public void testTraceObjectThrowable() {
        assertNotNull(getTestLogger());
        testHandler.setExpected(prepareTestObjectThrowable(msg, ex));
		getTestLogger().trace(msg, ex);
		assertTrue(testHandler.compare());
	}

	/*
	 * Class under test for void debug(Object)
	 */
	public void testDebugObject() {
        assertNotNull(getTestLogger());
        final String msg = "testmessage";
		getTestLogger().debug(msg);
		assertEquals(msg, testHandler.getActual());
	}

	/*
	 * Class under test for void debug(Object, Throwable)
	 */
	public void testDebugObjectThrowable() {
        assertNotNull(getTestLogger());
        testHandler.setExpected(prepareTestObjectThrowable(msg, ex));
		getTestLogger().debug(msg, ex);
		assertTrue(testHandler.compare());

	}

	/*
	 * Class under test for void info(Object)
	 */
	public void testInfoObject() {
        assertNotNull(getTestLogger());
        final String msg = "testmessage";
		getTestLogger().info(msg);
		assertEquals(msg, testHandler.getActual());
	}

	/*
	 * Class under test for void info(Object, Throwable)
	 */
	public void testInfoObjectThrowable() {
        assertNotNull(getTestLogger());
        testHandler.setExpected(prepareTestObjectThrowable(msg, ex));
		getTestLogger().info(msg, ex);
		assertTrue(testHandler.compare());

	}

	/*
	 * Class under test for void warn(Object)
	 */
	public void testWarnObject() {
        assertNotNull(getTestLogger());
        final String msg = "testmessage";
		getTestLogger().warn(msg);
		assertEquals(msg, testHandler.getActual());
	}

	/*
	 * Class under test for void warn(Object, Throwable)
	 */
	public void testWarnObjectThrowable() {
        assertNotNull(getTestLogger());
        testHandler.setExpected(prepareTestObjectThrowable(msg, ex));
		getTestLogger().warn(msg, ex);
		assertTrue(testHandler.compare());

	}

	/*
	 * Class under test for void error(Object)
	 */
	public void testErrorObject() {
        assertNotNull(getTestLogger());
        final String msg = "testmessage";
		getTestLogger().error(msg);
		assertEquals(msg, testHandler.getActual());

	}

	/*
	 * Class under test for void error(Object, Throwable)
	 */
	public void testErrorObjectThrowable() {
        assertNotNull(getTestLogger());
        testHandler.setExpected(prepareTestObjectThrowable(msg, ex));
		getTestLogger().error(msg, ex);
		assertTrue(testHandler.compare());
	}

	/*
	 * Class under test for void fatal(Object)
	 */
	public void testFatalObject() {
        assertNotNull(getTestLogger());
        final String msg = "testmessage";
		getTestLogger().fatal(msg);
		assertEquals(msg, testHandler.getActual());
	}

	/*
	 * Class under test for void fatal(Object, Throwable)
	 */
	public void testFatalObjectThrowable() {
        assertNotNull(getTestLogger());
        testHandler.setExpected(prepareTestObjectThrowable(msg, ex));
		getTestLogger().fatal(msg, ex);
		assertTrue(testHandler.compare());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.setName(X4JuliLoggerTest.class.getName());
		suite.addTest(new X4JuliLoggerTest("testGetLog", Level.FINEST));

		// Test for isXXXEnabled
		suite.addTest(new X4JuliLoggerTest("testIsTraceEnabled",
				X4JuliLogger.JCL_MAPPING_TRACE));
		suite.addTest(new X4JuliLoggerTest("testIsDebugEnabled",
				X4JuliLogger.JCL_MAPPING_DEBUG));
		suite.addTest(new X4JuliLoggerTest("testIsInfoEnabled",
				X4JuliLogger.JCL_MAPPING_INFO));
		suite.addTest(new X4JuliLoggerTest("testIsWarnEnabled",
				X4JuliLogger.JCL_MAPPING_WARN));
		suite.addTest(new X4JuliLoggerTest("testIsErrorEnabled",
				X4JuliLogger.JCL_MAPPING_ERROR));
		suite.addTest(new X4JuliLoggerTest("testIsFatalEnabled",
				X4JuliLogger.JCL_MAPPING_FATAL));

		// Test for XXXObject
		suite.addTest(new X4JuliLoggerTest("testTraceObject",
				X4JuliLogger.JCL_MAPPING_TRACE));
		suite.addTest(new X4JuliLoggerTest("testDebugObject",
				X4JuliLogger.JCL_MAPPING_DEBUG));
		suite.addTest(new X4JuliLoggerTest("testInfoObject",
				X4JuliLogger.JCL_MAPPING_INFO));
		suite.addTest(new X4JuliLoggerTest("testWarnObject",
				X4JuliLogger.JCL_MAPPING_WARN));
		suite.addTest(new X4JuliLoggerTest("testErrorObject",
				X4JuliLogger.JCL_MAPPING_ERROR));
		suite.addTest(new X4JuliLoggerTest("testFatalObject",
				X4JuliLogger.JCL_MAPPING_FATAL));

		// test for XXXObjectThrowable
		suite.addTest(new X4JuliLoggerTest("testTraceObjectThrowable",
				X4JuliLogger.JCL_MAPPING_TRACE));
		suite.addTest(new X4JuliLoggerTest("testDebugObjectThrowable",
				X4JuliLogger.JCL_MAPPING_DEBUG));
		suite.addTest(new X4JuliLoggerTest("testInfoObjectThrowable",
				X4JuliLogger.JCL_MAPPING_INFO));
		suite.addTest(new X4JuliLoggerTest("testWarnObjectThrowable",
				X4JuliLogger.JCL_MAPPING_WARN));
		suite.addTest(new X4JuliLoggerTest("testErrorObjectThrowable",
				X4JuliLogger.JCL_MAPPING_ERROR));
		suite.addTest(new X4JuliLoggerTest("testFatalObjectThrowable",
				X4JuliLogger.JCL_MAPPING_FATAL));

		return suite;
	}

}
