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

import org.x4juli.global.spi.OptionHandler;

/**
 * * A <code>RollingPolicy</code> specifies the actions taken on a logging
 * file rollover.
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
public interface RollingPolicy extends OptionHandler {

    /**
     * Initialize the policy and return any initial actions for rolling file appender..
     *
     * @param file current value of RollingFileHandler.getFile().
     * @param append current value of RollingFileHandler.getAppend().
     * @return Description of the initialization, may be null to indicate
     * no initialization needed.
     * @throws SecurityException if denied access to log files.
     * @since 0.5
     */
    public RolloverDescription initialize(
      final String file, final boolean append) throws SecurityException;

    /**
     * Prepare for a rollover.  This method is called prior to
     * closing the active log file, performs any necessary
     * preliminary actions and describes actions needed
     * after close of current log file.
     *
     * @param activeFile file name for current active log file.
     * @return Description of pending rollover, may be null to indicate no rollover
     * at this time.
     * @throws SecurityException if denied access to log files.
     * @since 0.5
     */
    public RolloverDescription rollover(final String activeFile) throws SecurityException;
}

// EOF RollingPolicy.java
