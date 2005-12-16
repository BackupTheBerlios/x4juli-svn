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

import org.x4juli.global.Constants;
import org.x4juli.global.spi.ExtendedLogRecord;

/**
 * Formats the class name of the site of the logging request.
 * The information is obtained by the originally provided one.
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
 * @see java.util.logging.LogRecord#getSourceClassName()
 * @since 0.5
 */
public class ClassNamePatternConverter extends NamePatternConverter {

    // -------------------------------------------------------------- Variables

    // ----------------------------------------------------------- Constructors

    /**
     * Private constructor.
     *
     * @param options may be null.
     * @since 0.5
     */
    private ClassNamePatternConverter(final String[] options) {
        super("Class Name", "class name", options);
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Gets an instance of ClassNamePatternConverter.
     *
     * @param options options, may be null.
     * @return instance of pattern converter.
     * @since 0.5
     */
    public static ClassNamePatternConverter newInstance(final String[] options) {
        return new ClassNamePatternConverter(options);
    }

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public void format(final ExtendedLogRecord record, final StringBuffer toAppendTo) {
        final int initialLength = toAppendTo.length();
        final String sourceClass = record.getSourceClassName();

        if (sourceClass != null) {
            toAppendTo.append(sourceClass);
        } else if (record.getLocationInformation().getClassName() != null){
            toAppendTo.append(record.getLocationInformation().getClassName());
        } else {
            toAppendTo.append(Constants.NOT_AVAILABLE_CHAR);
        }

        abbreviate(initialLength, toAppendTo);
    }

}

// EOF ClassNamePatternConverter.java
