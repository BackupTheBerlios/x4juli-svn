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

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

import java.util.Date;
import java.util.TimeZone;

/**
 * CachedDateFormat optimizes the performance of a wrapped DateFormat. The
 * implementation is not thread-safe. If the millisecond pattern is not
 * recognized, the class will only use the cache if the same value is requested.
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
 *
 */
public class CachedDateFormat extends DateFormat {

    // -------------------------------------------------------------- Variables
    /**
     * Constant used to represent that there was no change observed when
     * changing the millisecond count.
     * <br/>
     * Value is {@value}.
     */
    public static final int NO_MILLISECONDS = -2;

    /**
     * Constant used to represent that there was an observed change, but was an
     * expected change.
     * <br/>
     * Value is {@value}.
     */
    public static final int UNRECOGNIZED_MILLISECONDS = -1;

    private static final long serialVersionUID = -7619565728180812324L;

    private static final int TEN = 10;

    private static final int ONE_HUNDRED = 100;

    private static final long ONE_THOUSAND_L = 1000L;

    private static final int ONE_THOUSAND = 1000;

    /**
     * Supported digit set. If the wrapped DateFormat uses a different unit set,
     * the millisecond pattern will not be recognized and duplicate requests
     * will use the cache.
     */
    private static final String DIGITS = "0123456789";

    /**
     * First magic number used to detect the millisecond position.
     */
    private static final int MAGIC1 = 654;

    /**
     * Expected representation of first magic number.
     */
    private static final String MAGICSTRING1 = "654";

    /**
     * Second magic number used to detect the millisecond position.
     */
    private static final int MAGIC2 = 987;

    /**
     * Expected representation of second magic number.
     */
    private static final String MAGICSTRING2 = "987";

    /**
     * Expected representation of 0 milliseconds.
     */
    private static final String ZERO_STRING = "000";

    /**
     * Wrapped formatter.
     */
    private final DateFormat formatter;

    /**
     * Index of initial digit of millisecond pattern or
     * UNRECOGNIZED_MILLISECONDS or NO_MILLISECONDS.
     */
    private int millisecondStart;

    /**
     * Integral second preceding the previous convered Date.
     */
    private long slotBegin;

    /**
     * Cache of previous conversion.
     */
    private StringBuffer cache = new StringBuffer(50);

    /**
     * Maximum validity period for the cache. Typically 1, use cache for
     * duplicate requests only, or 1000, use cache for requests within the same
     * integral second.
     */
    private final int expiration;

    /**
     * Date requested in previous conversion.
     */
    private long previousTime;

    /**
     * Scratch date object used to minimize date object creation.
     */
    private final Date tmpDate = new Date(0);

    // ----------------------------------------------------------- Constructors

    /**
     * Creates a new CachedDateFormat object.
     *
     * @param dateFormat Date format, may not be null.
     * @param expiration maximum cached range in milliseconds. If the dateFormat
     *            is known to be incompatible with the caching algorithm, use a
     *            value of 0 to totally disable caching or 1 to only use cache
     *            for duplicate requests.
     * @since 0.5
     */
    public CachedDateFormat(final DateFormat dateFormat, final int expiration) {
        if (dateFormat == null) {
            throw new IllegalArgumentException("dateFormat cannot be null");
        }

        if (expiration < 0) {
            throw new IllegalArgumentException("expiration must be non-negative");
        }

        this.formatter = dateFormat;
        this.expiration = expiration;
        this.millisecondStart = 0;

        //
        // set the previousTime so the cache will be invalid
        // for the next request.
        this.previousTime = Long.MIN_VALUE;
        this.slotBegin = Long.MIN_VALUE;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Finds start of millisecond field in formatted time.
     *
     * @param time long time, must be integral number of seconds
     * @param formatted String corresponding formatted string
     * @param formatter DateFormat date format
     * @return int position in string of first digit of milliseconds, -1
     *         indicates no millisecond field, -2 indicates unrecognized field
     *         (likely RelativeTimeDateFormat)
     * @since 0.5
     */
    public static int findMillisecondStart(final long time, final String formatted,
            final DateFormat formatter) {
        long slotBegin = (time / CachedDateFormat.ONE_THOUSAND) * CachedDateFormat.ONE_THOUSAND;

        if (slotBegin > time) {
            slotBegin -= CachedDateFormat.ONE_THOUSAND;
        }

        int millis = (int) (time - slotBegin);

        int magic = MAGIC1;
        String magicString = MAGICSTRING1;

        if (millis == MAGIC1) {
            magic = MAGIC2;
            magicString = MAGICSTRING2;
        }

        String plusMagic = formatter.format(new Date(slotBegin + magic));

        /**
         * If the string lengths differ then we can't use the cache except for
         * duplicate requests.
         */
        if (plusMagic.length() != formatted.length()) {
            return UNRECOGNIZED_MILLISECONDS;
        } else {
            // find first difference between values
            for (int i = 0; i < formatted.length(); i++) {
                if (formatted.charAt(i) != plusMagic.charAt(i)) {
                    //
                    // determine the expected digits for the base time
                    StringBuffer formattedMillis = new StringBuffer("ABC");
                    millisecondFormat(millis, formattedMillis, 0);

                    String plusZero = formatter.format(new Date(slotBegin));

                    // If the next 3 characters match the magic
                    // string and the expected string
                    if ((plusZero.length() == formatted.length())
                            && magicString.regionMatches(0, plusMagic, i, magicString.length())
                            && formattedMillis.toString().regionMatches(0, formatted, i,
                                    magicString.length())
                            && ZERO_STRING.regionMatches(0, plusZero, i, ZERO_STRING.length())) {
                        return i;
                    } else {
                        return UNRECOGNIZED_MILLISECONDS;
                    }
                }
            }
        }

        return NO_MILLISECONDS;
    }

    /**
     * Formats a Date into a date/time string.
     *
     * @param date the date to format.
     * @param sbuf the string buffer to write to.
     * @param fieldPosition remains untouched.
     * @return the formatted time string.
     * @since 0.5
     */
    public StringBuffer format(final Date date, final StringBuffer sbuf,
            final FieldPosition fieldPosition) {
        format(date.getTime(), sbuf);

        return sbuf;
    }

    /**
     * Formats a millisecond count into a date/time string.
     *
     * @param now Number of milliseconds after midnight 1 Jan 1970 GMT.
     * @param buf the string buffer to write to.
     * @return the formatted time string.
     * @since 0.5
     */
    public StringBuffer format(final long now, final StringBuffer buf) {
        //
        // If the current requested time is identical to the previously
        // requested time, then append the cache contents.
        //
        if (now == this.previousTime) {
            buf.append(this.cache);

            return buf;
        }

        //
        // If millisecond pattern was not unrecognized
        // (that is if it was found or milliseconds did not appear)
        //
        if (this.millisecondStart != UNRECOGNIZED_MILLISECONDS) {
            // Check if the cache is still valid.
            // If the requested time is within the same integral second
            // as the last request and a shorter expiration was not requested.
            if ((now < (this.slotBegin + this.expiration)) && (now >= this.slotBegin)
                    && (now < (this.slotBegin + CachedDateFormat.ONE_THOUSAND_L))) {
                //
                // if there was a millisecond field then update it
                //
                if (this.millisecondStart >= 0) {
                    millisecondFormat((int) (now - this.slotBegin),
                            this.cache, this.millisecondStart);
                }

                //
                // update the previously requested time
                // (the slot begin should be unchanged)
                this.previousTime = now;
                buf.append(this.cache);

                return buf;
            }
        }

        //
        // could not use previous value.
        // Call underlying formatter to format date.
        this.cache.setLength(0);
        this.tmpDate.setTime(now);
        this.cache.append(this.formatter.format(this.tmpDate));
        buf.append(this.cache);
        this.previousTime = now;
        this.slotBegin = (this.previousTime / CachedDateFormat.ONE_THOUSAND)
                           * CachedDateFormat.ONE_THOUSAND;

        if (this.slotBegin > this.previousTime) {
            this.slotBegin -= CachedDateFormat.ONE_THOUSAND;
        }

        //
        // if the milliseconds field was previous found
        // then reevaluate in case it moved.
        //
        if (this.millisecondStart >= 0) {
            this.millisecondStart = findMillisecondStart(now, this.cache.toString(),
                    this.formatter);
        }

        return buf;
    }

    /**
     * Formats a count of milliseconds (0-999) into a numeric representation.
     *
     * @param millis Millisecond coun between 0 and 999.
     * @param buf String buffer, may not be null.
     * @param offset Starting position in buffer, the length of the buffer must
     *            be at least offset + 3.
     * @since 0.5
     */
    private static void millisecondFormat(final int millis, final StringBuffer buf,
                                           final int offset) {
        buf.setCharAt(offset, DIGITS.charAt(millis / CachedDateFormat.ONE_HUNDRED));
        buf.setCharAt(offset + 1, DIGITS.charAt((millis / CachedDateFormat.TEN)
                                                 % CachedDateFormat.TEN));
        buf.setCharAt(offset + 2, DIGITS.charAt(millis % CachedDateFormat.TEN));
    }

    /**
     * Set timezone.
     *
     * Setting the timezone using getCalendar().setTimeZone() will likely cause
     * caching to misbehave.
     *
     * @param timeZone TimeZone new timezone
     * @since 0.5
     */
    public void setTimeZone(final TimeZone timeZone) {
        this.formatter.setTimeZone(timeZone);
        this.previousTime = Long.MIN_VALUE;
        this.slotBegin = Long.MIN_VALUE;
    }

    /**
     * This method is delegated to the formatter which most likely returns null.
     *
     * @param s string representation of date.
     * @param pos field position, unused.
     * @return parsed date, likely null.
     * @since 0.5
     */
    public Date parse(final String s, final ParsePosition pos) {
        return this.formatter.parse(s, pos);
    }

    /**
     * Gets number formatter.
     *
     * @return NumberFormat number formatter
     * @since 0.5
     */
    public NumberFormat getNumberFormat() {
        return this.formatter.getNumberFormat();
    }

    /**
     * Gets maximum cache validity for the specified SimpleDateTime conversion
     * pattern.
     *
     * @param pattern conversion pattern, may not be null.
     * @return Duration in milliseconds from an integral second that the cache
     *         will return consistent results.
     * @since 0.5
     */
    public static int getMaximumCacheValidity(final String pattern) {
        //
        // If there are more "S" in the pattern than just one "SSS" then
        // (for example, "HH:mm:ss,SSS SSS"), then set the expiration to
        // one millisecond which should only perform duplicate request caching.
        //
        int firstS = pattern.indexOf('S');

        if ((firstS >= 0) && (firstS != pattern.lastIndexOf("SSS"))) {
            return 1;
        }

        return CachedDateFormat.ONE_THOUSAND;
    }

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public String toString() {
        StringBuffer buf = new StringBuffer("CachedDateFormat:");
        buf.append("MillisecondStart[");
        buf.append(this.millisecondStart);
        buf.append("] SlotBegin[");
        buf.append(this.slotBegin);
        buf.append("] Expiration[");
        buf.append(this.expiration);
        buf.append("] PreviousTime[");
        buf.append(this.previousTime);
        buf.append("] Cache[");
        buf.append(this.cache);
        buf.append("] TmpDate[");
        buf.append(this.tmpDate);
        buf.append("]");
        return buf.toString();
    }


}

// EOF CachedDateFormat.java
