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

/**
 * Base class for other pattern converters which can return only parts of their name.
 * <p>
 * Logging API as a whole was originally done for <a
 * href="http://logging.apache.org/log4j/">Apache log4j</a>. <b>Juli</b> is a
 * port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging
 * APIs</a>. All credits for initial idea, design, implementation,
 * documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This
 * file was originally published by <i>Ceki G&uuml;lc&uuml;, Curt Arnold</i>.
 * Please use exclusively the <i>appropriate</i> mailing lists for questions,
 * remarks and contribution.
 * </p>
 *
 * @author Boris Unckel
 * @since 0.5
 */
public abstract class NamePatternConverter extends LogRecordPatternConverter {

    // -------------------------------------------------------------- Variables

    /**
     * Abbreviator.
     */
    private final NameAbbreviator abbreviator;

    // ----------------------------------------------------------- Constructors

    /**
     * Constructor.
     *
     * @param name name of converter.
     * @param style style name for associated output.
     * @param options options, may be null, first element will be interpreted as
     *            an abbreviation pattern.
     * @since 0.5
     */
    protected NamePatternConverter(final String name, final String style, final String[] options) {
        super(name, style);

        if ((options != null) && (options.length > 0)) {
            this.abbreviator = NameAbbreviator.getAbbreviator(options[0]);
        } else {
            this.abbreviator = NameAbbreviator.getDefaultAbbreviator();
        }
    }

    // --------------------------------------------------------- Public Methods


    // ------------------------------------------------------ Protected Methods

    /**
     * Abbreviate name in string buffer.
     *
     * @param nameStart starting position of name to abbreviate.
     * @param buf string buffer containing name.
     * @since 0.5
     */
    protected final void abbreviate(final int nameStart, final StringBuffer buf) {
        this.abbreviator.abbreviate(nameStart, buf);
    }
}

// EOF NamePatternConverter.java
