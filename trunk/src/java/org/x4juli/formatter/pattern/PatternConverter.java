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

import org.x4juli.global.resources.MessageProperties;
import org.x4juli.global.spi.AbstractComponent;

/**
 * <p>
 * PatternConverter is an abstract class that provides the formatting
 * functionality that derived classes need.
 * </p>
 *
 * <p>
 * Conversion specifiers in a conversion patterns are parsed to individual
 * PatternConverters. Each of which is responsible for converting an object in a
 * converter specific manner.
 * </p>
 * <p>
 * Logging API as a whole was originally done for <a
 * href="http://logging.apache.org/log4j/">Apache log4j</a>. <b>Juli</b> is a
 * port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging
 * APIs</a>. All credits for initial idea, design, implementation,
 * documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This
 * file was originally published by <i>James P. Cakalic, Ceki G&uuml;lc&uuml;,
 * Chris Nokes, Curt Arnold</i>. Please use exclusively the <i>appropriate</i>
 * mailing lists for questions, remarks and contribution.
 * </p>
 *
 * @author Boris Unckel
 * @since 0.5
 */
public abstract class PatternConverter extends AbstractComponent {

    // -------------------------------------------------------------- Variables

    /**
     * Converter name.
     */
    private final String name;

    /**
     * Converter style name.
     */
    private final String style;

    // ----------------------------------------------------------- Constructors

    /**
     * Create a new pattern converter.
     *
     * @param name name for pattern converter.
     * @param style CSS style for formatted output.
     * @since 0.5
     */
    protected PatternConverter(final String name, final String style) {
        this.name = name;
        this.style = style;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Formats an object into a string buffer.
     *
     * @param obj event to format, may not be null.
     * @param toAppendTo string buffer to which the formatted event will be
     *            appended. May not be null.
     * @since 0.5
     */
    public abstract void format(final Object obj, final StringBuffer toAppendTo);

    /**
     * This method returns the name of the conversion pattern.
     *
     * The name can be useful to certain Layouts such as HTMLLayout.
     *
     * @return the name of the conversion pattern
     * @since 0.5
     */
    public final String getName() {
        return this.name;
    }

    /**
     * This method returns the CSS style class that should be applied to the
     * LoggingEvent passed as parameter, which can be null.
     *
     * This information is currently used only by HTMLLayout.
     *
     * @param e null values are accepted
     * @return the name of the conversion pattern
     * @since 0.5
     */
    public String getStyleClass(Object e) {
        return this.style;
    }

    /**
     * {@inheritDoc}
     * @since 0.6
     */
    public MessageProperties getMessageProperties() {
        return MessageProperties.PROPERTIES_FORMATTER_PATTERN;
    }

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public String toString() {
        StringBuffer buf = new StringBuffer("PatternConverter:");
        buf.append("Name[");
        buf.append(this.name);
        buf.append("] Style[");
        buf.append(this.style);
        buf.append("]");
        return buf.toString();
    }


}

// EOF PatternConverter.java
