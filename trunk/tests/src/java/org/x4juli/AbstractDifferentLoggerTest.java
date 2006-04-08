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
import java.util.logging.Logger;

import org.x4juli.NOPLogger;
import org.x4juli.formatter.PatternFormatter;
import org.x4juli.global.components.AbstractJuliTestCase;
import org.x4juli.global.helper.LoggerUtil;
import org.x4juli.global.spi.ExtendedFormatter;
import org.x4juli.handlers.TestHandler;

/**
 * Testcase for x4juli.
 * @since 0.5
 */
public abstract class AbstractDifferentLoggerTest extends AbstractJuliTestCase {

	Level testLevel = null;

	Logger testLog = null;

	TestHandler testHandler = null;

	final String msg = "testmessage";

	final Exception ex = new Exception("testEx");

	public AbstractDifferentLoggerTest(final String name, final Level testLevel) {
		super(name);
		this.testLevel = testLevel;
	}

	protected void setUp() throws Exception {
        super.setUp();
		testHandler = new TestHandler();
		testHandler.setLevel(testLevel);
		testHandler.setFormatter((ExtendedFormatter)new PatternFormatter("%m%throwable"));
		Logger testLogger = Logger.getLogger(getName());
		LoggerUtil.removeAllHandlers(testLogger);
		testLogger.addHandler(testHandler);
		testLogger.setLevel(testLevel);
        testLogger.setUseParentHandlers(false);
		testLogger.setParent(NOPLogger.NOP_LOGGER);
        testLog = testLogger;
	}

	protected void tearDown() {
		super.tearDown();
		if (testHandler != null) {
			testHandler.close();
			testHandler = null;
		}
		testLog = null;
        testLevel = null;
	}
	

	
	
}
