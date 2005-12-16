/*
 * Copyright 2005, x4juli.org.
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
package org.x4juli.global;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Global values for x4juli.
 * @author Boris Unckel
 * @since 0.5
 */
public final class Constants {

    /**
     * Length of the line separator, provided by the system.
     * <br/>
     */
    public static final int LINE_SEP_LEN = SystemUtils.LINE_SEPARATOR.length();

    /**
     * Int value of the log level.
     */
    public static final int LEVEL_FINEST = Level.FINEST.intValue();

    /**
     * Int value of the log level.
     */
    public static final int LEVEL_FINER = Level.FINER.intValue();

    /**
     * Int value of the log level.
     */
    public static final int LEVEL_FINE = Level.FINE.intValue();

    /**
     * Int value of the log level.
     */
    public static final int LEVEL_INFO = Level.INFO.intValue();

    /**
     * Int value of the log level.
     */
    public static final int LEVEL_WARNING = Level.WARNING.intValue();

    /**
     * Int value of the log level.
     */
    public static final int LEVEL_SEVERE = Level.SEVERE.intValue();

    /**
     * Int value of the log level.
     */
    public static final int LEVEL_OFF = Level.OFF.intValue();

    /**
     * Int value of the log level.
     */
    public static final int LEVEL_ALL = Level.ALL.intValue();

    /**
     * Full qualified name of the default java.util.logging.Logger class.
     */
    public static final String FQCN_JUL_LOGGER = Logger.class.getName();

    /**
     * The default x4juli package name.
     */
    public static final String X4JULI_PACKAGE_NAME = "org.x4juli";

    /**
     * Key for an absolute date format.
     */
    public static final String ABSOLUTE_FORMAT = "ABSOLUTE";

    /**
     * The absolute date pattern.
     * <br/>
     * Value is {@value}.
     */
    public static final String ABSOLUTE_TIME_PATTERN = "HH:mm:ss,SSS";

    /**
     * Key for an date and time format.
     */
    public static final String DATE_AND_TIME_FORMAT = "DATE";

    /**
     * The date and time format pattern.
     * <br/>
     * Value is {@value}.
     */
    public static final String DATE_AND_TIME_PATTERN = "dd MMM yyyy HH:mm:ss,SSS";

    /**
     * Key for ISO 8601 format.
     * <br/>
     * Value is {@value}.
     */
    public static final String ISO8601_FORMAT = "ISO8601";

    /**
     * The ISO 8601 date and time format pattern.
     */
    public static final String ISO8601_PATTERN = "yyyy-MM-dd HH:mm:ss,SSS";

    /**
     * Maximal errors to log.
     */
    public static final int ERROR_COUNT_LIMIT = 3;

    /**
     * When location information is not available the constant <code>NOT_AVAILABLE_CHAR</code>
     * is returned. Current value of this string constant is <b>{@value}</b>.
     */
    public static final String NOT_AVAILABLE_CHAR = "?";

    /**
     * Default path and prefix for any default value for an log file.
     * Should not be used directly without appending a unique key.
     */
    public static final String DEFAULT_LOG_FILE = SystemUtils.USER_HOME
                                                  + SystemUtils.FILE_SEPARATOR + "x4juli";

    /**
     * A random object for generating seeds.
     * <p>
     * Usage:
     * <br/>
     * Random myLocalRandom = new Random(Constants.RANDOM_FOR_SEED.nextLong());
     * </p>
     * Do not use it directly for generating random values.
     */
    public static final Random RANDOM_FOR_SEED = new Random();

    /**
     * No instanciation wanted.
     */
    private Constants() {
        //NOP
    }

}
