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
 * Return the relative time in milliseconds since loading of the LoggingEvent
 * class.
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
public class RelativeTimePatternConverter extends LogRecordPatternConverter {

    // -------------------------------------------------------------- Variables

    /**
     * Cached formatted timestamp.
     */
    private CachedTimestamp lastTimestamp = new CachedTimestamp(0, "");

    // ----------------------------------------------------------- Constructors

    /**
     * Private constructor.
     */
    public RelativeTimePatternConverter() {
      super("Time", "time");
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Obtains an instance of RelativeTimePatternConverter.
     * @param options options, currently ignored, may be null.
     * @return instance of RelativeTimePatternConverter.
     */
    public static RelativeTimePatternConverter newInstance(
      final String[] options) {
      return new RelativeTimePatternConverter();
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.5
     */
    public void format(ExtendedLogRecord record, StringBuffer toAppendTo) {
        long timestamp = record.getMillis();

        if (!this.lastTimestamp.format(timestamp, toAppendTo)) {
          final String formatted =
            Long.toString(timestamp - record.getStartTime());
          toAppendTo.append(formatted);
          this.lastTimestamp = new CachedTimestamp(timestamp, formatted);
        }
    }

    /**
     * Cached timestamp and formatted value.
     * @since 0.5
     */
    private static final class CachedTimestamp {
        /**
         * Cached timestamp.
         */
        private final long timestamp;

        /**
         * Cached formatted timestamp.
         */
        private final String formatted;

        /**
         * Creates a new instance.
         *
         * @param timestamp timestamp.
         * @param formatted formatted timestamp.
         */
        public CachedTimestamp(long timestamp, final String formatted) {
            this.timestamp = timestamp;
            this.formatted = formatted;
        }

        /**
         * Appends the cached formatted timestamp to the buffer if timestamps
         * match.
         *
         * @param newTimestamp requested timestamp.
         * @param toAppendTo buffer to append formatted timestamp.
         * @return true if requested timestamp matched cached timestamp.
         * @since 0.5
         */
        public boolean format(long newTimestamp, final StringBuffer toAppendTo) {
            if (newTimestamp == this.timestamp) {
                toAppendTo.append(this.formatted);

                return true;
            }

            return false;
        }

        /**
         * {@inheritDoc}
         * @since 0.5
         */
        public String toString() {
            StringBuffer buf = new StringBuffer("CachedTimestamp:");
            buf.append("Timestamp[");
            buf.append(this.timestamp);
            buf.append("] Formatted");
            buf.append(this.formatted);
            buf.append("]");
            return buf.toString();
        }

    }


}

// EOF RelativeTimePatternConverter.java
