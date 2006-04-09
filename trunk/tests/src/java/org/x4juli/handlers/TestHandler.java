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

package org.x4juli.handlers;

import java.io.StringWriter;
import java.util.logging.LogRecord;

import org.x4juli.global.SystemUtils;
import org.x4juli.global.spi.ExtendedLogRecord;
import org.x4juli.global.spi.ThrowableInformation;

/**
 * Testcase for x4juli.
 * @since 0.5
 */
public class TestHandler extends AbstractHandler {

    private StringWriter actual = null;

    private String expected = null;

    public TestHandler() {
        super("TestHandler");
        this.actual = new StringWriter();
        this.closed = false;
        activateOptions();
    }

    public void setExpected(final String expected) {
        this.expected = expected;
    }

    /**
     * @see java.util.logging.Handler#close()
     */
    public void close() throws SecurityException {
        this.closed = true;
        actual = null;
    }

    /**
     * @see java.util.logging.Handler#flush()
     */
    public void flush() {
        // NOOP

    }


    public synchronized boolean compare() {
        String result = actual.toString();
        boolean ret = expected.equals(result);
        if (!ret) {
            System.out.println("result[" + result + "] expected[" + expected + "]");
        }
        return ret;
    }

    public String getActual() {
        return actual.toString();
    }

    protected void appendLogRecord(ExtendedLogRecord record) {
        if (!isActive()) {
            return;
        }
            String toAppend = null;
            // Case: There is an ExtendedFormatter
            if (extFormatter != null) {
                toAppend = extFormatter.format(record);

                this.actual.write(toAppend);

                if (extFormatter.ignoresThrowable()) {
                    ThrowableInformation ti = record.getThrowableInformation();
                    if (ti != null) {
                        String[] s = ti.getThrowableStrRep();
                        if (s != null) {
                            int len = s.length;

                            for (int i = 0; i < len; i++) {
                                this.actual.write(s[i]);
                                this.actual.write(SystemUtils.LINE_SEPARATOR);
                            }
                        }
                    }
                }
            } else {
                // Case: There is not an ExtendedFormatter but
                // java.util.logging.Formatter
                toAppend = getFormatter().format((LogRecord) record);
                this.actual.write(toAppend);
            }
    }

    /**
     * {@inheritDoc}
     * @since 0.6
     */
    public String getFullQualifiedClassName() {
        return "org.x4juli.handlers.TestHandler";
    }

}
