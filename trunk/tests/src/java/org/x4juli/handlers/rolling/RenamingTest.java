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
package org.x4juli.handlers.rolling;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Logger;

import org.x4juli.formatter.PatternFormatter;
import org.x4juli.global.components.AbstractJuliTestCase;
import org.x4juli.global.helper.LoggerUtil;
import org.x4juli.global.spi.ExtendedFormatter;
import org.x4juli.global.util.Compare;
import org.x4juli.handlers.RollingFileHandler;
import org.x4juli.handlers.rolling.TimeBasedRollingPolicy;

/**
 * Testcase for x4juli.
 * @since 0.5
 */
public class RenamingTest extends AbstractJuliTestCase {
    Logger renameLogger;

    PatternFormatter formatter;

    /**
     * 
     */
    public RenamingTest() {
        super();
    }

    /**
     * @param name
     */
    public RenamingTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
        this.renameLogger = Logger.getLogger("RenamingTest");
        this.formatter = new PatternFormatter("%c{1} - %m%n");
        this.formatter.activateOptions();
        LoggerUtil.configureLogger(this.renameLogger, this.formatter);

    }

    public void tearDown() {
        super.tearDown();
    }

    public void testRename() throws Exception {
        Thread.sleep(1000);
        System.out.println("Entering testRename");
        RollingFileHandler rfa = new RollingFileHandler("foo");
        rfa.setFormatter((ExtendedFormatter)this.formatter);
        rfa.setAppend(false);

        // rollover by the second
        String datePattern = "yyyy-MM-dd_HH_mm_ss";
        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);

        TimeBasedRollingPolicy tbrp = new TimeBasedRollingPolicy("output/testren-%d{" + datePattern
                + "}");
        rfa.setFile("output/testren.log");
        rfa.setImmediateFlush(true);
        tbrp.activateOptions();
        rfa.setRollingPolicy(tbrp);
        rfa.activateOptions();

        Calendar cal = Calendar.getInstance();
        LoggerUtil.removeAllHandlers(this.renameLogger);
        this.renameLogger.addHandler(rfa);
        this.renameLogger.finer("Hello   " + 0);
        Thread.sleep(5000);
        this.renameLogger.finer("Hello   " + 1);

        String rolledFile = "output/testren-" + sdf.format(cal.getTime());

        //
        // if the rolled file exists
        // either the test wasn't run from the Ant script
        // which opens test.log in another process or
        // the test is running on a platform that allows open files to be
        // renamed
        getLogger().info("Starte check testRename");
        if (new File(rolledFile).exists()) {
            getLogger().info("rolledFile exists, two checks");
            boolean checkOne = Compare.compare(rolledFile, "witness/rolling/renaming.0");
            assertTrue("rolledFile vs renaming.0",checkOne);
            boolean checkTwo = Compare.compare("output/testren.log", "witness/rolling/renaming.1");
            assertTrue("testren.log vs renaming.1", checkTwo);
        } else {
            //
            // otherwise the rollover should have been blocked
            //
            getLogger().info("rolledFile does not exist, one check");
            boolean ret = Compare.compare("output/testren.log", "witness/rolling/renaming.2");
            assertTrue(ret);
        }
    }
}

// EOF RenamingTest.java
