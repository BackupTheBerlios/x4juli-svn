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
 * Formats a string literal.
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
public class LiteralPatternConverter extends LogRecordPatternConverter {

    // -------------------------------------------------------------- Variables

    /**
     * String literal.
     */
    private final String literal;

    // ----------------------------------------------------------- Constructors

    /**
     * Create a new instance.
     *
     * @param literal string literal.
     */
    public LiteralPatternConverter(final String literal) {
        super("Literal", "literal");
        this.literal = literal;
    }

    // ----------------------------------------------------------- Constructors

    /**
     * {@inheritDoc}
     *
     * @since 0.5
     */
    public void format(final ExtendedLogRecord record, final StringBuffer toAppendTo) {
        toAppendTo.append(this.literal);
    }

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public void format(final Object obj, final StringBuffer toAppendTo) {
      toAppendTo.append(this.literal);
    }

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public String toString() {
        StringBuffer buf = new StringBuffer("LiteralPatternConverter:");
        buf.append("Literal[");
        buf.append(this.literal);
        buf.append("] ");
        buf.append(super.toString());
        return buf.toString();
    }

}

// EOF LiteralPatternConverter.java
