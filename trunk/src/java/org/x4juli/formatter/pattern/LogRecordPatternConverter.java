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

import org.x4juli.global.spi.ExtendedLogRecord;

/**
 * LogRecordPatternConverter is a base class for pattern converters that can
 * format information from instances of LoggingEvent.
 * <p>
 * Logging API as a whole was originally done for <a
 * href="http://logging.apache.org/log4j/">Apache log4j</a>. <b>Juli</b> is a
 * port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging
 * APIs</a>. All credits for initial idea, design, implementation,
 * documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This
 * file was originally published by <i>Curt Arnold</i>. Please use exclusively
 * the <i>appropriate</i> mailing lists for questions, remarks and
 * contribution.
 * </p>
 *
 * @author Boris Unckel
 * @since 0.5
 */
public abstract class LogRecordPatternConverter extends PatternConverter {

    // -------------------------------------------------------------- Variables

    // ----------------------------------------------------------- Constructors

    /**
     * Constructs an instance of LogRecordPatternConverter.
     *
     * @param name name of converter.
     * @param style CSS style for output.
     * @since 0.5
     */
    protected LogRecordPatternConverter(final String name, final String style) {
        super(name, style);
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Formats an event into a string buffer.
     *
     * @param record to format, may not be null.
     * @param toAppendTo string buffer to which the formatted event will be
     *            appended. May not be null.
     * @since 0.5
     */
    public abstract void format(final ExtendedLogRecord record, final StringBuffer toAppendTo);

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public void format(final Object obj, final StringBuffer output) {
        if (obj instanceof ExtendedLogRecord) {
            format((ExtendedLogRecord) obj, output);
        }
    }

    /**
     * Normally pattern converters are not meant to handle Exceptions although
     * few pattern converters might.
     *
     * By examining the return values for this method, the containing layout
     * will determine whether it handles throwables or not.
     *
     * @return true if this PatternConverter handles throwables
     * @since 0.5
     */
    public boolean handlesThrowable() {
        return false;
    }
}

// EOF LogRecordPatternConverter.java
