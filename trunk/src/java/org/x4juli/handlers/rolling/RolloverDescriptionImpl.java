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

import org.x4juli.handlers.rolling.helper.Action;

/**
 * Description of actions needed to complete rollover.
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
public class RolloverDescriptionImpl implements RolloverDescription {
    // -------------------------------------------------------------- Variables

    /**
     * Active log file name after rollover.
     */
    private final String activeFileName;

    /**
     * Should active file be opened for appending.
     */
    private final boolean append;

    /**
     * Action to be completed after close of current active log file before
     * returning control to caller.
     */
    private final Action synchronous;

    /**
     * Action to be completed after close of current active log file and before
     * next rollover attempt, may be executed asynchronously.
     */
    private final Action asynchronous;

    // ----------------------------------------------------------- Constructors

    /**
     * Create new instance.
     *
     * @param activeFileName active log file name after rollover, may not be
     *            null.
     * @param append true if active log file after rollover should be opened for
     *            appending.
     * @param synchronous action to be completed after close of current active
     *            log file, may be null.
     * @param asynchronous action to be completed after close of current active
     *            log file and before next rollover attempt.
     */
    public RolloverDescriptionImpl(final String activeFileName, final boolean append,
            final Action synchronous, final Action asynchronous) {
        if (activeFileName == null) {
            throw new NullPointerException("activeFileName");
        }

        this.append = append;
        this.activeFileName = activeFileName;
        this.synchronous = synchronous;
        this.asynchronous = asynchronous;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * {@inheritDoc}
     *
     * @since
     */
    public String getActiveFileName() {
        return this.activeFileName;
    }

    /**
     * {@inheritDoc}
     *
     * @since
     */
    public boolean getAppend() {
        return this.append;
    }

    /**
     * {@inheritDoc}
     *
     * @since
     */
    public Action getSynchronous() {
        return this.synchronous;
    }

    /**
     * {@inheritDoc}
     *
     * @since
     */
    public Action getAsynchronous() {
        return this.asynchronous;
    }

}

// EOF RolloverDescriptionImpl.java
