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

import java.util.logging.Level;

import org.x4juli.global.spi.ExtendedLogRecord;

/**
 * Return the record's level in a StringBuffer.
 *
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
public class LevelPatternConverter extends LogRecordPatternConverter {

    // -------------------------------------------------------------- Variables

    private String option = null;

    // ----------------------------------------------------------- Constructors

    /**
     * Private constructor.
     */
    private LevelPatternConverter(final String[] options) {
        super("Level", "level");
        if ((options != null) && (options.length > 0)) {
            this.option = options[0];
        } else {
            this.option = null;
        }
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Obtains an instance of pattern converter.
     *
     * @param options options, may be null.
     * @return instance of pattern converter.
     * @since 0.5
     */
    public static LevelPatternConverter newInstance(final String[] options) {
        return new LevelPatternConverter(options);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.5
     */
    public void format(ExtendedLogRecord record, StringBuffer toAppendTo) {
        String toAppend = null;
        if (this.option == null || this.option.equals("default")) {
            toAppend = record.getLevel().toString();
        } else if (this.option.equals("i18n")) {
            toAppend = record.getLevel().getLocalizedName();
        } else {
            toAppend = record.getLevel().toString();
        }
        toAppendTo.append(toAppend);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.5
     */
    public String getStyleClass(Object e) {
        if (e instanceof ExtendedLogRecord) {
            Level lrLevel = ((ExtendedLogRecord) e).getLevel();

            if (Level.FINEST.equals(lrLevel)) {
                return "level finest";
            } else if (Level.FINER.equals(lrLevel)) {
                return "level finer";
            } else if (Level.FINE.equals(lrLevel)) {
                return "level fine";
            } else if (Level.INFO.equals(lrLevel)) {
                return "level info";
            } else if (Level.WARNING.equals(lrLevel)) {
                return "level warning";
            } else if (Level.SEVERE.equals(lrLevel)) {
                return "level severe";
            } else {
                return "level " + lrLevel.toString();
            }

        }
        return "level";
    }
}

// EOF LevelPatternConverter.java
