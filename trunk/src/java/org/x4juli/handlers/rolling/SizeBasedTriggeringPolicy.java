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

import org.x4juli.global.components.AbstractComponent;
import org.x4juli.global.spi.ExtendedLogRecord;
import org.x4juli.global.spi.OptionHandler;

/**
 * SizeBasedTriggeringPolicy looks at size of the file being currently written
 * to.
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
public class SizeBasedTriggeringPolicy extends AbstractComponent implements TriggeringPolicy, OptionHandler {
    // -------------------------------------------------------------- Variables

    /**
     * Rollover threshold size in bytes.
     */
    // let 10 MB the default max size
    private static final long DEFAULT_MAX_FILE_SIZE = 10 * 1024 * 1024;
    private long maxFileSize = DEFAULT_MAX_FILE_SIZE;

    // ----------------------------------------------------------- Constructors

    /**
     * Constructor for use with file based configuration.
     */
    public SizeBasedTriggeringPolicy() {
        configure();
        activateOptions();
    }


    /**
     * Constructor for use in programmatically configuration.
     * @param maxFileSize rollover threshold size in bytes.
     */
    public SizeBasedTriggeringPolicy(final long maxFileSize) {
        setMaxFileSize(maxFileSize);
    }

    // --------------------------------------------------------- Public Methods

    /**
     * {@inheritDoc}
     *
     * @since 0.5
     */
    public boolean isTriggeringEvent(Handler handler, ExtendedLogRecord record, String filename,
            long fileLength) {
        return (fileLength >= this.maxFileSize);
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.5
     */
    public void activateOptions() {
        // NOP
    }

    /**
     * Configure all properties of the object. Subclasses should call
     * super.configure() to ensure proper configuration.
     *
     * @since 0.5
     */
    public void configure() {
        String className = this.getClass().getName();

        //FileNamePattern
        String key = className + ".maxFileSize";
        setMaxFileSize(getProperty(key,DEFAULT_MAX_FILE_SIZE));
    }

    /**
     * Gets rollover threshold size in bytes.
     *
     * @return rollover threshold size in bytes.
     * @since 0.5
     */
    public long getMaxFileSize() {
        return this.maxFileSize;
    }

    /**
     * Sets rollover threshold size in bytes.
     *
     * @param l new value for rollover threshold size.
     * @since 0.5
     */
    public void setMaxFileSize(long l) {
        this.maxFileSize = l;
    }

}

// EOF SizeBasedTriggeringPolicy.java
