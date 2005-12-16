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
package org.x4juli.formatter.pattern;

import org.x4juli.global.SystemUtils;
import org.x4juli.global.spi.ExtendedLogRecord;
import org.x4juli.global.spi.ThrowableInformation;

/**
 * Outputs the ThrowableInformation portion of the LoggingiEvent as a full stacktrace
 * unless this converter's option is 'short', where it just outputs the first line of the trace.
 * <p>
 * Logging API as a whole was originally done for <a
 * href="http://logging.apache.org/log4j/">Apache log4j</a>. <b>Juli</b> is a
 * port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging
 * APIs</a>. All credits for initial idea, design, implementation,
 * documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This
 * file was originally published by <i>Paul Smith</i>. Please use exclusively
 * the <i>appropriate</i> mailing lists for questions, remarks and
 * contribution.
 * </p>
 *
 * @author Boris Unckel
 * @since 0.5
 */
public class ThrowableInformationPatternConverter extends LogRecordPatternConverter {

    // -------------------------------------------------------------- Variables

    /**
     * If "short", only first line of throwable report will be formatted.
     */
    private final String option;

    // ----------------------------------------------------------- Constructors

    /**
     * Private constructor.
     *
     * @param options options, may be null.
     */
    private ThrowableInformationPatternConverter(final String[] options) {
        super("Throwable", "throwable");

        if ((options != null) && (options.length > 0)) {
            this.option = options[0];
        } else {
            this.option = null;
        }
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Gets an instance of the class.
     *
     * @param options pattern options, may be null. If first element is "short",
     *            only the first line of the throwable will be formatted.
     * @return instance of class.
     * @since 0.5
     */
    public static ThrowableInformationPatternConverter newInstance(final String[] options) {
        return new ThrowableInformationPatternConverter(options);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.5
     */
    public void format(ExtendedLogRecord record, StringBuffer toAppendTo) {
        ThrowableInformation information = record.getThrowableInformation();

        if (information != null) {
            String[] stringRep = information.getThrowableStrRep();

            int length = 0;

            if (this.option == null) {
                length = stringRep.length;
            } else if (this.option.equals("full")) {
                length = stringRep.length;
            } else if (this.option.equals("short")) {
                length = 1;
            } else {
                length = stringRep.length;
            }

            for (int i = 0; i < length; i++) {
                String string = stringRep[i];
                toAppendTo.append(string).append(SystemUtils.LINE_SEPARATOR);
            }
        }

    }

    /**
     * This converter obviously handles throwables.
     *
     * @return true.
     */
    public boolean handlesThrowable() {
        return true;
    }

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public String toString() {
        StringBuffer buf = new StringBuffer("ThrowableInformationPatternConverter:");
        buf.append("Option[");
        buf.append(this.option);
        buf.append("] ");
        buf.append(super.toString());
        return buf.toString();
    }

}

// EOF ThrowableInformationPatternConverter.java
