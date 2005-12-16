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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;

import org.x4juli.global.Constants;
import org.x4juli.global.spi.ExtendedLogRecord;
import org.x4juli.global.spi.ExtendedLogRecordImpl;

/**
 * Convert and format the event's date in a StringBuffer.
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
public class DatePatternConverter extends LogRecordPatternConverter {

    // -------------------------------------------------------------- Variables

    /**
     * Date format.
     */
    private final CachedDateFormat df;

    // ----------------------------------------------------------- Constructors

    /**
     * Private constructor.
     *
     * @param options may be null.
     * @since 0.5
     */
    private DatePatternConverter(final String[] options) {
        super("Date", "date");

        String patternOption;

        if ((options == null) || (options.length == 0)) {
            // the branch could be optimized, but here we are making explicit
            // that null values for patternOption are allowed.
            patternOption = null;
        } else {
            patternOption = options[0];
        }

        String pattern;

        if ((patternOption == null) || patternOption.equalsIgnoreCase(Constants.ISO8601_FORMAT)) {
            pattern = Constants.ISO8601_PATTERN;
        } else if (patternOption.equalsIgnoreCase(Constants.ABSOLUTE_FORMAT)) {
            pattern = Constants.ABSOLUTE_TIME_PATTERN;
        } else if (patternOption.equalsIgnoreCase(Constants.DATE_AND_TIME_FORMAT)) {
            pattern = Constants.DATE_AND_TIME_PATTERN;
        } else {
            pattern = patternOption;
        }

        int maximumCacheValidity = 1000;
        SimpleDateFormat simpleFormat = null;

        try {
            simpleFormat = new SimpleDateFormat(pattern);
            maximumCacheValidity = CachedDateFormat.getMaximumCacheValidity(pattern);
        } catch (IllegalArgumentException e) {
            ExtendedLogRecord rec = new ExtendedLogRecordImpl(Level.WARNING,MessageText.Could_not_instantiate_SimpleDateFormat_with_pattern);
            rec.setParameters(new Object[]{patternOption});
            rec.setThrown(e);
            getLogger().log(rec);
            // default to the ISO8601 format
            simpleFormat = new SimpleDateFormat(Constants.ISO8601_PATTERN);
        }

        // if the option list contains a TZ option, then set it.
        if ((options != null) && (options.length > 1)) {
            TimeZone tz = TimeZone.getTimeZone((String) options[1]);
            simpleFormat.setTimeZone(tz);
        }

        this.df = new CachedDateFormat(simpleFormat, maximumCacheValidity);
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Obtains an instance of pattern converter.
     *
     * @param options may be null.
     * @return instance of pattern converter.
     * @since 0.5
     */
    public static DatePatternConverter newInstance(final String[] options) {
        return new DatePatternConverter(options);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.5
     */
    public void format(final ExtendedLogRecord record, final StringBuffer output) {
        this.df.format(record.getMillis(), output);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.5
     */
    public void format(final Object obj, final StringBuffer output) {
        if (obj instanceof Date) {
            format((Date) obj, output);
        }

        super.format(obj, output);
    }

    /**
     * Append formatted date to string buffer.
     *
     * @param date date
     * @param toAppendTo buffer to which formatted date is appended.
     * @since 0.5
     */
    public void format(final Date date, final StringBuffer toAppendTo) {
        this.df.format(date.getTime(), toAppendTo);
    }
}

// EOF DatePatternConverter.java
