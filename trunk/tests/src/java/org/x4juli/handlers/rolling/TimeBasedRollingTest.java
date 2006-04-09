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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.x4juli.formatter.PatternFormatter;
import org.x4juli.global.components.AbstractJuliTestCase;
import org.x4juli.global.helper.LoggerUtil;
import org.x4juli.global.spi.ExtendedFormatter;
import org.x4juli.global.util.Compare;
import org.x4juli.handlers.RollingFileHandler;
import org.x4juli.handlers.rolling.TimeBasedRollingPolicy;
import org.x4juli.logger.NOPLogger;

/**
 * Testcase for x4juli.
 * @since 0.5
 */
public class TimeBasedRollingTest extends AbstractJuliTestCase {

    private Logger root = null;

    private Logger logger = null;

    /**
     * 
     */
    public TimeBasedRollingTest() {
        super();
    }

    /**
     * @param name
     */
    public TimeBasedRollingTest(String name) {
        super(name);
    }

    /**
     * {@inheritDoc}
     * 
     * @since
     */
    protected void tearDown() {
        this.root = null;
        this.logger = null;
        super.tearDown();
    }

    /**
     * {@inheritDoc}
     * 
     * @since
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.root = Logger.getLogger("ParentLogger");
        LoggerUtil.removeAllHandlers(this.root);
        this.root.setUseParentHandlers(false);
        this.root.setLevel(Level.ALL);
        PatternFormatter pf = new PatternFormatter("%d{ABSOLUTE} [%t] %level %c{2}#%M:%L - %m%n");
        ConsoleHandler ch = new ConsoleHandler();
        ch.setFormatter(pf);
        ch.setLevel(Level.ALL);
        this.root.addHandler(ch);
        this.logger = Logger.getLogger("ParentLogger.TimeBasedRollingTest");
        this.logger.setUseParentHandlers(true);
        this.logger.setLevel(Level.ALL);
        LoggerUtil.removeAllHandlers(this.logger);
    }

    /**
     * Test rolling without compression, activeFileName left blank, no
     * stop/start
     */
    public void test1() throws Exception {
        PatternFormatter layout = new PatternFormatter("%c{1} - %m%n");
        RollingFileHandler rfa = new RollingFileHandler("TimeBasedRollingTest.test1");
        rfa.setFormatter((ExtendedFormatter)layout);

        String datePattern = "yyyy-MM-dd_HH_mm_ss";

        TimeBasedRollingPolicy tbrp = new TimeBasedRollingPolicy("output/test1-%d{" + datePattern
                + "}");
        tbrp.activateOptions();
        rfa.setRollingPolicy(tbrp);
        rfa.activateOptions();
        this.logger.addHandler(rfa);

        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
        String[] filenames = new String[4];

        Calendar cal = Calendar.getInstance();

        for (int i = 0; i < 4; i++) {
            filenames[i] = "output/test1-" + sdf.format(cal.getTime());
            cal.add(Calendar.SECOND, 1);
        }

        System.out.println("Waiting until next second and 100 millis.");
        delayUntilNextSecond(100);
        System.out.println("Done waiting.");

        for (int i = 0; i < 5; i++) {
            this.logger.finer("Hello---" + i);
            Thread.sleep(500);
        }

        for (int i = 0; i < 4; i++) {
            // System.out.println(i + " expected filename [" + filenames[i] +
            // "].");
        }

        for (int i = 0; i < 4; i++) {
            assertTrue(Compare.compare(filenames[i], "witness/rolling/tbr-test1." + i));
        }
    }

    void delayUntilNextSecond(int millis) {
        long now = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(now));

        cal.set(Calendar.MILLISECOND, millis);
        cal.add(Calendar.SECOND, 1);

        long next = cal.getTime().getTime();

        try {
            Thread.sleep(next - now);
        } catch (Exception e) {
            // Ignored Exception, to recognize it, use IDE Debugger Breakpoint in NOPLogger
            NOPLogger.NOP_LOGGER.log(Level.FINEST,"Ignored exception",e);
        }
    }

    void delayUntilNextMinute(int seconds) {
        long now = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(now));

        cal.set(Calendar.SECOND, seconds);
        cal.add(Calendar.MINUTE, 1);

        long next = cal.getTime().getTime();

        try {
            Thread.sleep(next - now);
        } catch (Exception e) {
            // Ignored Exception, to recognize it, use IDE Debugger Breakpoint in NOPLogger
            NOPLogger.NOP_LOGGER.log(Level.FINEST,"Ignored exception",e);
        }
    }

    /**
     * No compression, with stop/restart, activeFileName left blank
     */
    public void test2() throws Exception {
        String datePattern = "yyyy-MM-dd_HH_mm_ss";

        PatternFormatter layout1 = new PatternFormatter("%c{1} - %m%n");
        RollingFileHandler rfa1 = new RollingFileHandler("TimeBasedRollingTest.test2.1");
        rfa1.setFormatter((ExtendedFormatter)layout1);

        TimeBasedRollingPolicy tbrp1 = new TimeBasedRollingPolicy("output/test2-%d{" + datePattern
                + "}");
        tbrp1.activateOptions();
        rfa1.setRollingPolicy(tbrp1);
        rfa1.activateOptions();
        this.logger.addHandler(rfa1);

        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
        String[] filenames = new String[4];

        Calendar cal = Calendar.getInstance();

        for (int i = 0; i < 4; i++) {
            filenames[i] = "output/test2-" + sdf.format(cal.getTime());
            cal.add(Calendar.SECOND, 1);
        }

        System.out.println("Waiting until next second and 100 millis.");
        delayUntilNextSecond(100);
        System.out.println("Done waiting.");

        for (int i = 0; i <= 2; i++) {
            this.logger.finer("Hello---" + i);
            Thread.sleep(500);
        }

        this.logger.removeHandler(rfa1);
        rfa1.close();

        PatternFormatter layout2 = new PatternFormatter("%c{1} - %m%n");
        RollingFileHandler rfa2 = new RollingFileHandler("TimeBasedRollingTest.test2.2");
        rfa2.setFormatter((ExtendedFormatter)layout2);

        TimeBasedRollingPolicy tbrp2 = new TimeBasedRollingPolicy("output/test2-%d{" + datePattern
                + "}");
        tbrp2.activateOptions();
        rfa2.setRollingPolicy(tbrp2);
        rfa2.activateOptions();
        this.logger.addHandler(rfa2);

        for (int i = 3; i <= 4; i++) {
            this.logger.finer("Hello---" + i);
            Thread.sleep(500);
        }

        rfa2.close();

        for (int i = 0; i < 4; i++) {
            assertTrue(Compare.compare(filenames[i], "witness/rolling/tbr-test2." + i));
        }
    }

    /**
     * With compression, activeFileName left blank, no stop/restart
     */
    public void test3() throws Exception {
        PatternFormatter layout = new PatternFormatter("%c{1} - %m%n");
        RollingFileHandler rfa = new RollingFileHandler("TimeBasedRollingTest.test3");
        rfa.setFormatter((ExtendedFormatter)layout);

        String datePattern = "yyyy-MM-dd_HH_mm_ss";

        TimeBasedRollingPolicy tbrp = new TimeBasedRollingPolicy("output/test3-%d{" + datePattern
                + "}.gz");
        tbrp.activateOptions();
        rfa.setRollingPolicy(tbrp);
        rfa.activateOptions();
        this.logger.addHandler(rfa);

        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
        String[] filenames = new String[4];

        Calendar cal = Calendar.getInstance();

        for (int i = 0; i < 3; i++) {
            filenames[i] = "output/test3-" + sdf.format(cal.getTime()) + ".gz";
            cal.add(Calendar.SECOND, 1);
        }

        filenames[3] = "output/test3-" + sdf.format(cal.getTime());

        System.out.println("Waiting until next second and 100 millis.");
        delayUntilNextSecond(100);
        System.out.println("Done waiting.");

        for (int i = 0; i < 5; i++) {
            this.logger.finer("Hello---" + i);
            Thread.sleep(500);
        }

        for (int i = 0; i < 4; i++) {
            // System.out.println(i + " expected filename [" + filenames[i] +
            // "].");
        }

        rfa.close();

        for (int i = 0; i < 3; i++) {
            assertTrue(Compare.gzCompare(filenames[i], "witness/rolling/tbr-test3." + i + ".gz"));
        }

        assertTrue(Compare.compare(filenames[3], "witness/rolling/tbr-test3.3"));
    }

    /**
     * Without compression, activeFileName set, with stop/restart
     */
    public void test4() throws Exception {
        String datePattern = "yyyy-MM-dd_HH_mm_ss";

        PatternFormatter layout1 = new PatternFormatter("%c{1} - %m%n");
        RollingFileHandler rfa1 = new RollingFileHandler("TimeBasedRollingTest.test4.1");
        rfa1.setFormatter((ExtendedFormatter)layout1);

        TimeBasedRollingPolicy tbrp1 = new TimeBasedRollingPolicy("output/test4.log");
        tbrp1.setFileNamePattern("output/test4-%d{" + datePattern + "}");
        tbrp1.activateOptions();
        rfa1.setRollingPolicy(tbrp1);
        rfa1.activateOptions();
        this.logger.addHandler(rfa1);

        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
        String[] filenames = new String[4];

        Calendar cal = Calendar.getInstance();

        for (int i = 0; i < 3; i++) {
            filenames[i] = "output/test4-" + sdf.format(cal.getTime());
            cal.add(Calendar.SECOND, 1);
        }
        filenames[3] = "output/test4.log";

        System.out.println("Waiting until next second and 100 millis.");
        delayUntilNextSecond(100);
        System.out.println("Done waiting.");
        
        //File.1: Hello 0,1; File.2: Hello 2 
        for (int i = 0; i <= 2; i++) {
            this.logger.finer("Hello---" + i);
            Thread.sleep(500);
        }

        this.logger.removeHandler(rfa1);
        rfa1.close();

        PatternFormatter layout2 = new PatternFormatter("%c{1} - %m%n");
        RollingFileHandler rfa2 = new RollingFileHandler("TimeBasedRollingTest.test4.2");
        rfa2.setFormatter((ExtendedFormatter)layout2);

        TimeBasedRollingPolicy tbrp2 = new TimeBasedRollingPolicy("output/test4-%d{" + datePattern + "}");
        rfa2.setFile("output/test4.log");
        tbrp2.activateOptions();
        rfa2.setRollingPolicy(tbrp2);
        rfa2.activateOptions();
        this.logger.addHandler(rfa2);

        //File.2: Hello 3; File.3: Hello 4; File.0 empty
        for (int i = 3; i <= 4; i++) {
            this.logger.finer("Hello---" + i);
            Thread.sleep(500);
        }

        rfa2.close();

        for (int i = 0; i < 4; i++) {
            System.out.println("No["+i+"] filename["+filenames[i]+"] vs [witness/rolling/tbr-test4." + i+"]");
            assertTrue(Compare.compare(filenames[i], "witness/rolling/tbr-test4." + i));
        }
    }

    /**
     * No compression, activeFileName set, without stop/restart
     */
    public void test5() throws Exception {
        PatternFormatter layout = new PatternFormatter("%c{1} - %m%n");
        RollingFileHandler rfa = new RollingFileHandler("TimeBasedRollingTest.test5");
        rfa.setFormatter((ExtendedFormatter)layout);

        String datePattern = "yyyy-MM-dd_HH_mm_ss";

        TimeBasedRollingPolicy tbrp = new TimeBasedRollingPolicy("output/test5-%d{" + datePattern + "}");
        rfa.setFile("output/test5.log");
        tbrp.activateOptions();
        rfa.setRollingPolicy(tbrp);
        rfa.activateOptions();
        this.logger.addHandler(rfa);

        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
        String[] filenames = new String[4];

        Calendar cal = Calendar.getInstance();

        for (int i = 0; i < 3; i++) {
            filenames[i] = "output/test5-" + sdf.format(cal.getTime());
            cal.add(Calendar.SECOND, 1);
        }

        filenames[3] = "output/test5.log";

        System.out.println("Waiting until next second and 100 millis.");
        delayUntilNextSecond(100);
        System.out.println("Done waiting.");

        for (int i = 0; i < 5; i++) {
            this.logger.finer("Hello---" + i);
            Thread.sleep(500);
        }

        for (int i = 0; i < 4; i++) {
            assertTrue(Compare.compare(filenames[i], "witness/rolling/tbr-test5." + i));
        }
    }

    /**
     * With compression, activeFileName set, no stop/restart,
     */
    public void test6() throws Exception {
        PatternFormatter layout = new PatternFormatter("%c{1} - %m%n");
        RollingFileHandler rfa = new RollingFileHandler("TimeBasedRollingTest.test6");
        rfa.setFormatter((ExtendedFormatter)layout);

        String datePattern = "yyyy-MM-dd_HH_mm_ss";

        TimeBasedRollingPolicy tbrp = new TimeBasedRollingPolicy("output/test6-%d{" + datePattern + "}.gz");
        rfa.setFile("output/test6.log");
        tbrp.activateOptions();
        rfa.setRollingPolicy(tbrp);
        rfa.activateOptions();
        this.logger.addHandler(rfa);

        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
        String[] filenames = new String[4];

        Calendar cal = Calendar.getInstance();

        for (int i = 0; i < 3; i++) {
            filenames[i] = "output/test6-" + sdf.format(cal.getTime()) + ".gz";
            cal.add(Calendar.SECOND, 1);
        }

        filenames[3] = "output/test6.log";

        System.out.println("Waiting until next second and 100 millis.");
        delayUntilNextSecond(100);
        System.out.println("Done waiting.");

        for (int i = 0; i < 5; i++) {
            this.logger.finer("Hello---" + i);
            Thread.sleep(500);
        }

        for (int i = 0; i < 4; i++) {
            // System.out.println(i + " expected filename [" + filenames[i] +
            // "].");
        }

        rfa.close();

        for (int i = 0; i < 3; i++) {
            assertTrue(Compare.gzCompare(filenames[i], "witness/rolling/tbr-test6." + i + ".gz"));
        }

        assertTrue(Compare.compare(filenames[3], "witness/rolling/tbr-test6.3"));
    }

}

// EOF TimeBasedRollingTest.java
