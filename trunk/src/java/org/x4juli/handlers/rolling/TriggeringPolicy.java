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

import java.util.logging.Handler;

import org.x4juli.global.spi.ExtendedLogRecord;
import org.x4juli.global.spi.OptionHandler;

/**
 * A <code>TriggeringPolicy</code> controls the conditions under which
 * rollover occurs. Such conditions include time of day, file size, an external
 * event, the log request or a combination thereof.
 * <p>
 * Logging API as a whole was originally done for <a
 * href="http://logging.apache.org/log4j/">Apache log4j</a>. <b>Juli</b> is a
 * port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging
 * APIs</a>. All credits for initial idea, design, implementation,
 * documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This
 * file was originally published by <i>Ceki G&uuml;lc&uuml;, Curt Arnold</i>.
 * Please use exclusively the <i>appropriate</i> mailing lists for questions,
 * remarks and contribution.
 * </p>
 *
 * @author Boris Unckel
 * @since 0.5
 */
public interface TriggeringPolicy extends OptionHandler {

    /**
     * Determines if a rollover may be appropriate at this time. If true is
     * returned, RolloverPolicy.rollover will be called but it can determine
     * that a rollover is not warranted.
     *
     * @param handler A reference to the handler.
     * @param record A reference to the current record.
     * @param filename The filename for the currently active log file.
     * @param fileLength Length of the file in bytes.
     * @return true if a rollover should occur.
     * @since 0.5
     */
    public boolean isTriggeringEvent(final Handler handler, final ExtendedLogRecord record,
            final String filename, final long fileLength);
}

// EOF TriggeringPolicy.java
