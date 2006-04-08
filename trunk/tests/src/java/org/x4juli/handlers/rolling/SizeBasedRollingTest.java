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
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.x4juli.formatter.PatternFormatter;
import org.x4juli.global.components.AbstractJuliTestCase;
import org.x4juli.global.helper.LoggerUtil;
import org.x4juli.global.spi.ExtendedFormatter;
import org.x4juli.global.util.Compare;
import org.x4juli.handlers.RollingFileHandler;
import org.x4juli.handlers.rolling.FixedWindowRollingPolicy;
import org.x4juli.handlers.rolling.SizeBasedTriggeringPolicy;

/**
 * Testcase for x4juli.
 * @since 0.5
 */
public class SizeBasedRollingTest extends AbstractJuliTestCase {

    Logger root = null;

    Logger logger = null;

    /**
     * 
     */
    public SizeBasedRollingTest() {
        super();
    }

    /**
     * @param name
     */
    public SizeBasedRollingTest(String name) {
        super(name);
    }

    /**
     * {@inheritDoc}
     * 
     * @since
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.root = Logger.getLogger("SizeBasedRollingTest");
        LoggerUtil.removeAllHandlers(this.root);
        this.root.setUseParentHandlers(false);
        this.root.setLevel(Level.ALL);
        this.logger = Logger.getLogger("SizeBasedRollingTest.child");
        this.logger.setUseParentHandlers(true);
        this.logger.setLevel(Level.ALL);
    }

    /**
     * 
     * @since
     */
    protected void tearDown() {
        super.tearDown();
        this.root = null;
        this.logger = null;
    }

    /**
     * Tests that the lack of an explicit active file will use the low index as
     * the active file.
     * 
     */
    public void test1() throws Exception {
        PatternFormatter formatter = new PatternFormatter("%m\n");
        RollingFileHandler rfa = new RollingFileHandler("SizeBasedRollingTest.test1");
        rfa.setAppend(false);
        rfa.setBufferedIO(false);
        rfa.setImmediateFlush(true);
        rfa.setFormatter((ExtendedFormatter)formatter);

        FixedWindowRollingPolicy swrp = new FixedWindowRollingPolicy("output/sizeBased-test1.%i");
        SizeBasedTriggeringPolicy sbtp = new SizeBasedTriggeringPolicy(100);

        swrp.setMinIndex(0);
        swrp.activateOptions();

        rfa.setRollingPolicy(swrp);
        rfa.setTriggeringPolicy(sbtp);
        rfa.activateOptions();
        this.root.addHandler(rfa);

        // Write exactly 10 bytes with each log
        for (int i = 0; i < 25; i++) {
            if (i < 10) {
                this.logger.finer("Hello---" + i);
            } else if (i < 100) {
                this.logger.finer("Hello--" + i);
            }
        }

        assertTrue("output/sizeBased-test1.0 has to exist",
                new File("output/sizeBased-test1.0").exists());
        assertTrue("output/sizeBased-test1.1 has to exist",
                new File("output/sizeBased-test1.1").exists());
        assertTrue("output/sizeBased-test1.2 has to exist",
                new File("output/sizeBased-test1.2").exists());

        assertTrue("output/sizeBased-test1.0 has to be equal to witness/rolling/sbr-test2.log",
                Compare.compare("output/sizeBased-test1.0", "witness/rolling/sbr-test2.log"));
        assertTrue("output/sizeBased-test1.1 has to be equal to witness/rolling/sbr-test2.0.log",
                Compare.compare("output/sizeBased-test1.1", "witness/rolling/sbr-test2.0"));
        assertTrue("output/sizeBased-test1.2 has to be equal to witness/rolling/sbr-test2.2.log",
                Compare.compare("output/sizeBased-test1.2", "witness/rolling/sbr-test2.1"));
    }

    /**
     * Test basic rolling functionality with explicit setting of
     * FileHandler.file.
     */
    public void test2() throws Exception {
        PatternFormatter layout = new PatternFormatter("%m\n");
        RollingFileHandler rfa = new RollingFileHandler("SizeBasedRollingTest.test2");
        rfa.setAppend(false);
        rfa.setBufferedIO(false);
        rfa.setImmediateFlush(true);
        rfa.setFormatter((ExtendedFormatter)layout);
        rfa.setFile("output/sizeBased-test2.log");

        FixedWindowRollingPolicy swrp = new FixedWindowRollingPolicy("output/sizeBased-test2.%i");
        SizeBasedTriggeringPolicy sbtp = new SizeBasedTriggeringPolicy(100);

        swrp.setMinIndex(0);
        swrp.activateOptions();

        rfa.setRollingPolicy(swrp);
        rfa.setTriggeringPolicy(sbtp);
        rfa.activateOptions();
        this.root.addHandler(rfa);

        // Write exactly 10 bytes with each log
        for (int i = 0; i < 25; i++) {
            if (i < 10) {
                this.logger.finer("Hello---" + i);
            } else if (i < 100) {
                this.logger.finer("Hello--" + i);
            }
        }

        assertTrue("output/sizeBased-test2.log has to exist",
                new File("output/sizeBased-test2.log").exists());
        assertTrue("output/sizeBased-test2.0.log has to exist",
                new File("output/sizeBased-test2.0").exists());
        assertTrue("output/sizeBased-test2.1.log has to exist",
                new File("output/sizeBased-test2.1").exists());

        assertTrue("output/sizeBased-test2 has to be equal to witness/rolling/sbr-test2.log",
                Compare.compare("output/sizeBased-test2.log", "witness/rolling/sbr-test2.log"));
        assertTrue("output/sizeBased-test2.0 has to be equal to witness/rolling/sbr-test2.0.log",
                Compare.compare("output/sizeBased-test2.0", "witness/rolling/sbr-test2.0"));
        assertTrue("output/sizeBased-test2.1 has to be equal to witness/rolling/sbr-test2.1.log",
                Compare.compare("output/sizeBased-test2.1", "witness/rolling/sbr-test2.1"));
    }

    /**
     * Same as testBasic but also with GZ compression.
     */
    public void test3() throws Exception {
        PatternFormatter layout = new PatternFormatter("%m\n");
        RollingFileHandler rfa = new RollingFileHandler("SizeBasedRollingTest.test3");
        rfa.setAppend(false);
        rfa.setBufferedIO(false);
        rfa.setImmediateFlush(true);
        rfa.setFormatter((ExtendedFormatter)layout);

        FixedWindowRollingPolicy fwrp = new FixedWindowRollingPolicy("output/sbr-test3.%i.gz");
        SizeBasedTriggeringPolicy sbtp = new SizeBasedTriggeringPolicy(100);

        fwrp.setMinIndex(0);
        rfa.setFile("output/sbr-test3.log");
        fwrp.activateOptions();
        rfa.setRollingPolicy(fwrp);
        rfa.setTriggeringPolicy(sbtp);
        rfa.activateOptions();
        this.root.addHandler(rfa);

        // Write exactly 10 bytes with each log
        for (int i = 0; i < 25; i++) {
            Thread.sleep(100);
            if (i < 10) {
                this.logger.finer("Hello---" + i);
            } else if (i < 100) {
                this.logger.finer("Hello--" + i);
            }
        }

        assertTrue(new File("output/sbr-test3.log").exists());
        assertTrue(new File("output/sbr-test3.0.gz").exists());
        assertTrue(new File("output/sbr-test3.1.gz").exists());

        assertTrue(Compare.compare("output/sbr-test3.log", "witness/rolling/sbr-test3.log"));
        assertTrue(Compare.gzCompare("output/sbr-test3.0.gz", "witness/rolling/sbr-test3.0.gz"));
        assertTrue(Compare.gzCompare("output/sbr-test3.1.gz", "witness/rolling/sbr-test3.1.gz"));
    }

    /**
     * Test basic rolling functionality with bogus path in file name pattern.
     */
    public void test4() throws Exception {
        PatternFormatter layout = new PatternFormatter("%m\n");
        RollingFileHandler rfa = new RollingFileHandler("SizeBasedRollingTest.test4");
        rfa.setAppend(false);
        rfa.setBufferedIO(false);
        rfa.setImmediateFlush(true);
        rfa.setFormatter((ExtendedFormatter)layout);
        rfa.setFile("output/sizeBased-test4.log");

        FixedWindowRollingPolicy swrp = new FixedWindowRollingPolicy("output/test4/sizeBased-test4.%i");
        SizeBasedTriggeringPolicy sbtp = new SizeBasedTriggeringPolicy(100);

        swrp.setMinIndex(0);

        //
        // test4 directory should not exists. Should cause all rollover attempts
        // to fail.
        //
        swrp.activateOptions();

        rfa.setRollingPolicy(swrp);
        rfa.setTriggeringPolicy(sbtp);
        rfa.activateOptions();
        this.root.addHandler(rfa);

        // Write exactly 10 bytes with each log
        for (int i = 0; i < 25; i++) {
            if (i < 10) {
                this.logger.finer("Hello---" + i);
            } else if (i < 100) {
                this.logger.finer("Hello--" + i);
            }
        }

        assertTrue(new File("output/sizeBased-test4.log").exists());

        assertTrue(Compare.compare("output/sizeBased-test4.log", "witness/rolling/sbr-test4.log"));
    }

    /**
     * Checking handling of rename failures due to other access to the indexed
     * files.
     */
    public void test5() throws Exception {
        PatternFormatter layout = new PatternFormatter("%m\n");
        RollingFileHandler rfa = new RollingFileHandler("SizeBasedRollingTest.test4");
        rfa.setAppend(false);
        rfa.setBufferedIO(false);
        rfa.setImmediateFlush(true);
        rfa.setFormatter((ExtendedFormatter)layout);
        rfa.setFile("output/sizeBased-test5.log");

        FixedWindowRollingPolicy swrp = new FixedWindowRollingPolicy("output/sizeBased-test5.%i");
        SizeBasedTriggeringPolicy sbtp = new SizeBasedTriggeringPolicy(100);

        swrp.setMinIndex(0);
        swrp.activateOptions();

        rfa.setRollingPolicy(swrp);
        rfa.setTriggeringPolicy(sbtp);
        rfa.activateOptions();
        this.root.addHandler(rfa);

        //
        // put stray file about locked file
        FileOutputStream os1 = new FileOutputStream("output/sizeBased-test5.1");
        os1.close();

        FileOutputStream os0 = new FileOutputStream("output/sizeBased-test5.0");

        // Write exactly 10 bytes with each log
        for (int i = 0; i < 25; i++) {
            if (i < 10) {
                this.logger.finer("Hello---" + i);
            } else if (i < 100) {
                this.logger.finer("Hello--" + i);
            }
        }

        os0.close();

        if (new File("output/sizeBased-test5.3").exists()) {
            //
            // looks like platform where open files can be renamed
            //
            assertTrue(new File("output/sizeBased-test5.log").exists());
            assertTrue(new File("output/sizeBased-test5.0").exists());
            assertTrue(new File("output/sizeBased-test5.1").exists());
            assertTrue(new File("output/sizeBased-test5.2").exists());
            assertTrue(new File("output/sizeBased-test5.3").exists());

            assertTrue(Compare.compare("output/sizeBased-test5.log",
                    "witness/rolling/sbr-test2.log"));
            assertTrue(Compare.compare("output/sizeBased-test5.0", "witness/rolling/sbr-test2.0"));
            assertTrue(Compare.compare("output/sizeBased-test5.1", "witness/rolling/sbr-test2.1"));

        } else {
            //
            // rollover attempts should all fail
            // so initial log file should have all log content
            // open file should be unaffected
            // stray file should have only been moved one slot.
            assertTrue(new File("output/sizeBased-test5.log").exists());
            assertTrue(new File("output/sizeBased-test5.0").exists());
            assertTrue(new File("output/sizeBased-test5.2").exists());

            assertTrue(Compare.compare("output/sizeBased-test5.log",
                    "witness/rolling/sbr-test4.log"));
        }
    }
    
}

// EOF SizeBasedRollingTest.java
