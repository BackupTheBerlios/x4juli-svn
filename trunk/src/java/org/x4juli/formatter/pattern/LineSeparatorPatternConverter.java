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

/**
 * Formats a line separator.
 * <p>
 * Logging API as a whole was originally done for <a
 * href="http://logging.apache.org/log4j/">Apache log4j</a>. <b>Juli</b> is a
 * port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging
 * APIs</a>. All credits for initial idea, design, implementation,
 * documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This
 * file was originally published by <i>Ceki G&uuml;lc&uuml;</i>. Please use
 * exclusively the <i>appropriate</i> mailing lists for questions, remarks and
 * contribution.
 * </p>
 *
 * @author Boris Unckel
 * @since 0.5
 */
public class LineSeparatorPatternConverter extends LogRecordPatternConverter {
    /**
     * Singleton.
     */
    private static final LineSeparatorPatternConverter INSTANCE = new LineSeparatorPatternConverter();

    /**
     * Private constructor.
     */
    private LineSeparatorPatternConverter() {
        super("Line Sep", "lineSep");
    }

    /**
     * Obtains an instance of pattern converter.
     *
     * @param options options, may be null.
     * @return instance of pattern converter.
     * @since 0.5
     */
    public static LineSeparatorPatternConverter newInstance(final String[] options) {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.5
     */
    public void format(final ExtendedLogRecord record, final StringBuffer toAppendTo) {
        toAppendTo.append(SystemUtils.LINE_SEPARATOR);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.5
     */
    public void format(final Object obj, final StringBuffer toAppendTo) {
        toAppendTo.append(SystemUtils.LINE_SEPARATOR);
    }
}

// EOF LineSeparatorPatternConverter.java
