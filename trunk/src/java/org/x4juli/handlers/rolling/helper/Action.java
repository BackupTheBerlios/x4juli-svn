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
package org.x4juli.handlers.rolling.helper;

import java.io.IOException;

/**
 * The Action interface should be implemented by any class that performs file
 * system actions for RollingFileHandler after the close of the active log file.
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
public interface Action extends Runnable {

    /**
     * Perform an action.
     *
     * @return true if action was successful. A return value of false will cause
     *         the rollover to be aborted if possible.
     * @throws IOException
     *             if IO error, a thrown exception will cause the rollover to be
     *             aborted if possible.
     * @since 0.5
     */
    boolean execute() throws IOException;

    /**
     * Cancels the action if not already initialized or waits till completion.
     *
     * @since 0.5
     */
    void close();

    /**
     * Determines if action has been completed.
     *
     * @return true if action is complete.
     * @since 0.5
     */
    boolean isComplete();

}

// EOF Action.java
