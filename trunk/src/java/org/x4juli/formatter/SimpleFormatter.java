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

import java.io.StringWriter;

import org.x4juli.global.SystemUtils;
import org.x4juli.global.components.AbstractFormatter;
import org.x4juli.global.spi.ExtendedLogRecord;

/**
 * <p>
 * Logging API as a whole was originally done for <a
 * href="http://logging.apache.org/log4j/">Apache log4j</a>. <b>Juli</b> is a
 * port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging
 * APIs</a>. All credits for initial idea, design, implementation,
 * documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This
 * file was originally published by <i>Ceki G&uuml;lc&uuml; </i>. Please use
 * exclusively the <i>appropriate</i> mailing lists for questions, remarks and
 * contribution.
 * </p>
 *
 * @author Boris Unckel
 * @since 0.5
 */
public class SimpleFormatter extends AbstractFormatter {

    // -------------------------------------------------------------- Variables

    // ----------------------------------------------------------- Constructors

    /**
     * Default Constructor
     */
    public SimpleFormatter() {
        //NOP
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Writes the log statement in a format consisting of the <code>level</code>,
     * followed by " - " and then the <code>message</code>. For example,
     *
     * <pre>
     * INFO - &quot;A message&quot;
     * </pre>
     *
     * <p>
     * The <code>LogRecord.getName()</code> parameter is ignored.
     * <p>
     *
     * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
     * @since 0.5
     */
    public String doFormat(final ExtendedLogRecord record) {
        StringWriter output = new StringWriter();
        output.write(record.getLevel().getName());
        output.write(" - ");
        output.write(formatMessage(record));
        output.write(SystemUtils.LINE_SEPARATOR);
        return output.toString();
    }

    /**
     * @see org.x4juli.global.spi.OptionHandler#activateOptions()
     * @since 0.5
     */
    public void activateOptions() {
        //NOP
    }

}
