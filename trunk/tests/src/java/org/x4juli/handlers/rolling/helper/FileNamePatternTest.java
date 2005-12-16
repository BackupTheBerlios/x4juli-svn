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
package org.x4juli.handlers.rolling.helper;

import java.util.Calendar;

import org.x4juli.global.components.AbstractJuliTestCase;
import org.x4juli.handlers.rolling.AbstractRollingPolicy;
import org.x4juli.handlers.rolling.RolloverDescription;

/**
 * Testcase for x4juli.
 * @since 0.5
 */
public class FileNamePatternTest extends AbstractJuliTestCase {
    // -------------------------------------------------------------- Variables

    // ----------------------------------------------------------- Constructors
    /**
     * 
     */
    public FileNamePatternTest() {
        super();
    }

    /**
     * @param name
     */
    public FileNamePatternTest(String name) {
        super(name);
    }

    // --------------------------------------------------------- Public Methods

    public void testFormatInteger1() {
        assertIntegerPattern("t", 3, "t");
    }

    public void testFormatInteger2() {
        assertIntegerPattern("foo", 3, "foo");
    }

    public void testFormatInteger3() {
        assertIntegerPattern("foo%", 3, "foo%");
    }

    public void testFormatInteger4() {
        assertIntegerPattern("%ifoo", 3, "3foo");
    }

    public void testFormatInteger5() {
        assertIntegerPattern("foo%ixixo", 3, "foo3xixo");
    }

    public void testFormatInteger6() {
        assertIntegerPattern("foo%i.log", 3, "foo3.log");
    }

    public void testFormatInteger7() {
        assertIntegerPattern("foo.%i.log", 3, "foo.3.log");
    }

    public void testFormatInteger8() {
        assertIntegerPattern("%ifoo%", 3, "3foo%");
    }

    public void testFormatInteger9() {
        assertIntegerPattern("%ifoo%%", 3, "3foo%");
    }

    public void testFormatInteger10() {
        assertIntegerPattern("%%foo", 3, "%foo");
    }

    public void testFormatInteger11() {
        assertIntegerPattern("foo%ibar%i", 3, "foo3bar3");
    }

    public void testFormatDate1() {
        assertDatePattern("foo%d{yyyy.MM.dd}", 2003, 4, 20, 17, 55, "foo2003.05.20");
    }

    public void testFormatDate2() {
        assertDatePattern("foo%d{yyyy.MM.dd HH:mm}", 2003, 4, 20, 17, 55, "foo2003.05.20 17:55");
    }

    public void testFormatDate3() {
        assertDatePattern("%d{yyyy.MM.dd HH:mm} foo", 2003, 4, 20, 17, 55, "2003.05.20 17:55 foo");
    }

    /**
     * A %d is treated as %d{yyyy-MM-dd} if followed by a malformed format
     * specifier.
     * 
     */
    public void testFormatDate4() {
        assertDatePattern("foo%dyyyy.MM.dd}", 2003, 4, 20, 17, 55, "foo2003-05-20yyyy.MM.dd}");
    }

    /**
     * A %d is treated as %d{yyyy-MM-dd} if followed by a malformed format
     * specifier.
     * 
     */
    public void testFormatDate5() {
        assertDatePattern("foo%d{yyyy.MM.dd", 2003, 4, 20, 17, 55, "foo2003-05-20{yyyy.MM.dd");
    }

    // ------------------------------------------------------ Protected Methods

    // -------------------------------------------------------- Private Methods

    private void assertDatePattern(final String pattern, final int year, final int month,
            final int day, final int hour, final int min, final String expected) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, hour, min);
        FileNameTestRollingPolicy policy = new FileNameTestRollingPolicy(pattern);
        String actual = policy.format(cal.getTime());
        assertEquals(expected, actual);
    }

    private void assertIntegerPattern(final String pattern, final int value, final String expected) {
        FileNameTestRollingPolicy policy = new FileNameTestRollingPolicy(pattern);
        String actual = policy.format(new Integer(value));
        assertEquals(expected, actual);
    }

    private static class FileNameTestRollingPolicy extends AbstractRollingPolicy {
        public FileNameTestRollingPolicy(final String pattern) {
            setFileNamePattern(pattern);
            parseFileNamePattern();
        }

        public void activateOptions() {
            //NOP
        }

        public RolloverDescription initialize(final String activeName, final boolean append) {
            return null;
        }

        public RolloverDescription rollover(final String activeName) {
            return null;
        }

//        public Logger getLogger() {
//            return null;
//        }

        public String format(Object obj) {
            StringBuffer buf = new StringBuffer();
            formatFileName(obj, buf);
            return buf.toString();
        }
    }

}

// EOF FileNamePatternTest.java
