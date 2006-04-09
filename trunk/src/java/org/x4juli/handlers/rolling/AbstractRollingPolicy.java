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
package org.x4juli.handlers.rolling;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.x4juli.formatter.pattern.DatePatternConverter;
import org.x4juli.formatter.pattern.FormattingInfo;
import org.x4juli.formatter.pattern.IntegerPatternConverter;
import org.x4juli.formatter.pattern.PatternConverter;
import org.x4juli.formatter.pattern.PatternParser;
import org.x4juli.global.resources.MessageProperties;
import org.x4juli.global.spi.AbstractComponent;
import org.x4juli.global.spi.OptionHandler;
import org.x4juli.handlers.MessageText;

/**
 * Implements methods common to most, it not all, rolling policies. Currently
 * such methods are limited to a compression mode getter/setter.
 * <p>
 * Logging API as a whole was originally done for <a
 * href="http://logging.apache.org/log4j/">Apache log4j</a>. <b>Juli</b> is a
 * port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging
 * APIs</a>. All credits for initial idea, design, implementation,
 * documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This
 * file was originally published by <i>Ceki G&uuml;lc&uuml;, Curt Arnold</i>. Please use
 * exclusively the <i>appropriate</i> mailing lists for questions, remarks and
 * contribution.
 * </p>
 *
 * @author Boris Unckel
 * @since 0.5
 */
public abstract class AbstractRollingPolicy extends AbstractComponent implements RollingPolicy,
        OptionHandler {

    // -------------------------------------------------------------- Variables

    /**
     * File name pattern converters.
     */
    private PatternConverter[] patternConverters;

    /**
     * File name field specifiers.
     */
    private FormattingInfo[] patternFields;

    /**
     * File name pattern.
     */
    private String fileNamePatternStr;

    /**
     * Active file name may be null. Duplicates FileAppender.file and should be
     * removed.
     */
    protected String activeFileName;

    // ----------------------------------------------------------- Constructors

    /**
     * Constructor for use with file based configuration.
     */
    protected AbstractRollingPolicy(){
        configure();
        activateOptions();
    }

    /**
     * Constructor for use in programmatically configuration.
     * @param fileNamePattern pattern to determine the filename.
     */
    protected AbstractRollingPolicy(final String fileNamePattern){
        setFileNamePattern(fileNamePattern);
    }

    // --------------------------------------------------------- Public Methods

    /**
     * {@inheritDoc}
     *
     * @since 0.5
     */
    public void activateOptions() {
        // find out period from the filename pattern
        if (this.fileNamePatternStr != null) {
            parseFileNamePattern();
        } else {
            getLogger().log(Level.WARNING,MessageText.The_ActiveFile_name_option_must_be_set_before_using_this_rolling_policy);
            throw new IllegalStateException(MessageText.The_ActiveFile_name_option_must_be_set_before_using_this_rolling_policy);
        }
    }

    /**
     * Configure all properties of the object. Subclasses should call
     * super.configure() to ensure proper configuration.
     *
     * @since 0.5
     */
    public void configure() {
        //Currently NOP
    }

    /**
     * Set file name pattern.
     *
     * @param fnp file name pattern.
     * @since 0.5
     */
    public void setFileNamePattern(String fnp) {
        this.fileNamePatternStr = fnp;
    }

    /**
     * Get file name pattern.
     *
     * @return file name pattern.
     * @since 0.5
     */
    public String getFileNamePattern() {
        return this.fileNamePatternStr;
    }

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public MessageProperties getMessageProperties() {
        return MessageProperties.PROPERTIES_HANDLER;
    }

    /**
     * {@inheritDoc}
     * @since
     */
    public String toString() {
        StringBuffer buf = new StringBuffer("AbstractRollingPolicy:");
        buf.append("FileNamePatternString[");
        buf.append(getFileNamePattern());
        buf.append("] ActiveFile[");
        buf.append(this.activeFileName);
        buf.append("]");
        return buf.toString();
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Parse file name pattern.
     * @since 0.5
     */
    protected final void parseFileNamePattern() {
        List converters = new ArrayList();
        List fields = new ArrayList();

        PatternParser.parse(this.fileNamePatternStr, converters, fields, null, PatternParser
                .getFileNamePatternRules());
        this.patternConverters = new PatternConverter[converters.size()];
        this.patternConverters = (PatternConverter[]) converters.toArray(this.patternConverters);
        this.patternFields = new FormattingInfo[converters.size()];
        this.patternFields = (FormattingInfo[]) fields.toArray(this.patternFields);
    }

    /**
     * Format file name.
     *
     * @param obj object to be evaluted in formatting, may not be null.
     * @param buf string buffer to which formatted file name is appended, may
     *            not be null.
     * @since 0.5
     */
    protected final void formatFileName(final Object obj, final StringBuffer buf) {
        for (int i = 0; i < this.patternConverters.length; i++) {
            int fieldStart = buf.length();
            this.patternConverters[i].format(obj, buf);

            if (this.patternFields[i] != null) {
                this.patternFields[i].format(fieldStart, buf);
            }
        }
    }

    /**
     * Returns the first occuring <code>DatePatternConverter</code> in the
     * pattern Converter array.
     *
     * @return the first DatePatternConverter, may return null.
     * @since 0.5
     */
    protected final PatternConverter getDatePatternConverter() {
        for (int i = 0; i < this.patternConverters.length; i++) {
            if (this.patternConverters[i] instanceof DatePatternConverter) {
                return this.patternConverters[i];
            }
        }
        return null;

    }

    /**
     * Returns the first occuring <code>IntegerPatternConverter</code> in the
     * pattern Converter array.
     *
     * @return the first IntegerPatternConverter, may return null.
     * @since 0.5
     */
    protected final PatternConverter getIntegerPatternConverter() {
        for (int i = 0; i < this.patternConverters.length; i++) {
            if (this.patternConverters[i] instanceof IntegerPatternConverter) {
                return this.patternConverters[i];
            }
        }
        return null;
    }

}

// EOF AbstractRollingPolicy.java
