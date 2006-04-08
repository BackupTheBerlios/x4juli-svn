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
package org.x4juli;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.x4juli.global.components.AbstractExtendedLogger;
import org.x4juli.global.spi.ExtendedLogRecord;

/**
 * A no operation (NOP) implementation of {@link Logger}.
 *
 * @author Boris Unckel
 * @since 0.5
 */
public final class NOPLogger extends AbstractExtendedLogger {

    // -------------------------------------------------------------- Variables

    /**
     * There is just one unique instance needed
     */
    public static final NOPLogger NOP_LOGGER = new NOPLogger(null, null);

    // ----------------------------------------------------------- Constructors

    /**
     * Constructur Private due to single instance need.
     *
     * @param name is ignored
     * @param resourceBundleName is ignored
     */
    private NOPLogger(final String name, final String resourceBundleName) {
        super("NOPLogger", null);
        super.setLevel(Level.OFF);
    }

    // --------------------------------------------------------- Public Methods

    /**
     * No Operation.
     *
     * @since 0.5
     */
    public void entering(final String sourceClass, final String sourceMethod, final Object param1) {
        // NOP
    }

    /**
     * No Operation.
     *
     * @since 0.5
     */
    public void entering(final String sourceClass, final String sourceMethod, final Object[] params) {
        // NOP
    }

    /**
     * No Operation.
     *
     * @since 0.5
     */
    public void entering(final String sourceClass, final String sourceMethod) {
        // NOP
    }

    /**
     * No Operation.
     *
     * @since 0.5
     */
    public void exiting(final String sourceClass, final String sourceMethod, final Object result) {
        // NOP
    }

    /**
     * No Operation.
     *
     * @since 0.5
     */
    public void exiting(final String sourceClass, final String sourceMethod) {
        // NOP
    }

    /**
     * No Operation.
     *
     * @since 0.5
     */
    public void fine(final String msg) {
        // NOP
    }

    /**
     * No Operation.
     *
     * @since 0.5
     */
    public void finer(final String msg) {
        // NOP
    }

    /**
     * No Operation.
     *
     * @since 0.5
     */
    public void finest(final String msg) {
        // NOP
    }

    /**
     * No Operation.
     *
     * @since 0.5
     */
    public void info(final String msg) {
        // NOP
    }

    /**
     * Always returns false.
     * @see java.util.logging.Logger#isLoggable(java.util.logging.Level)
     * @return always false
     */
    public boolean isLoggable(final Level level) {
        return false;
    }

    /**
     * No Operation.
     *
     * @since 0.5
     */
    public void log(final Level level, final String msg, final Object param1) {
        // NOP
    }

    /**
     * No Operation.
     *
     * @since 0.5
     */
    public void log(final Level level, final String msg, final Object[] params) {
        // NOP
    }

    /**
     * No Operation.
     *
     * @since 0.5
     */
    public void log(final Level level, final String msg, final Throwable thrown) {
        // NOP
    }

    /**
     * No Operation.
     *
     * @since 0.5
     */
    public void log(final Level level, final String msg) {
        // NOP
    }

    /**
     * No Operation.
     *
     * @since 0.5
     */
    public void log(final LogRecord record) {
        // NOP
    }

    /**
     * No Operation.
     * @since 0.6
     */
    public void log(final ExtendedLogRecord record) {
        // NOP
    }

    /**
     * No Operation.
     *
     * @since 0.5
     */
    public void logp(final Level level, final String sourceClass, final String sourceMethod, final String msg, final Object param1) {
        // NOP
    }

    /**
     * No Operation.
     *
     * @since 0.5
     */
    public void logp(final Level level, final String sourceClass, final String sourceMethod, final String msg,
            final Object[] params) {
        // NOP
    }

    /**
     * No Operation.
     *
     * @since 0.5
     */
    public void logp(final Level level, final String sourceClass, final String sourceMethod, final String msg,
            final Throwable thrown) {
        // NOP
    }

    /**
     * No Operation.
     *
     * @since 0.5
     */
    public void logp(final Level level, final String sourceClass, final String sourceMethod, final String msg) {
        // NOP
    }

    /**
     * No Operation.
     *
     * @since 0.5
     */
    public void logrb(final Level level, final String sourceClass, final String sourceMethod, final String bundleName,
            final String msg, final Object param1) {
        // NOP
    }

    /**
     * No Operation.
     *
     * @since 0.5
     */
    public void logrb(final Level level, final String sourceClass, final String sourceMethod, final String bundleName,
            final String msg, final Object[] params) {
        // NOP
    }

    /**
     * No Operation.
     *
     * @since 0.5
     */
    public void logrb(final Level level, final String sourceClass, final String sourceMethod, final String bundleName,
            final String msg, final Throwable thrown) {
        // NOP
    }

    /**
     * No Operation.
     *
     * @since 0.5
     */
    public void logrb(final Level level, final String sourceClass, final String sourceMethod, final String bundleName,
            final String msg) {
        // NOP
    }

    /**
     * No Operation.
     *
     * @since 0.5
     */
    public void severe(final String msg) {
        // NOP
    }

    /**
     * No Operation.
     *
     * @since 0.5
     */
    public void throwing(final String sourceClass, final String sourceMethod, final Throwable thrown) {
        // NOP
    }

    /**
     * No Operation.
     *
     * @since 0.5
     */
    public void warning(final String msg) {
        // NOP
    }

    /**
     * Always returns <code>Level.OFF</code>.
     *
     * @see java.util.logging.Logger#getLevel()
     * @return {@link Level#OFF}
     * @since 0.5
     */
    public Level getLevel() {
        return Level.OFF;
    }

    /**
     * Always sets {@link Level#OFF}.
     *
     * @see java.util.logging.Logger#setLevel(java.util.logging.Level)
     * @param newLevel is ignored
     * @since 0.5
     */
    public void setLevel(final Level newLevel) throws SecurityException {
        super.setLevel(Level.OFF);
    }

}
