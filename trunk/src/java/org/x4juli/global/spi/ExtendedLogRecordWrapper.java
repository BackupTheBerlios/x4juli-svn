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
package org.x4juli.global.spi;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.x4juli.global.components.AbstractExtendedLogRecord;
import org.x4juli.global.spi.location.LocationInfo;

/**
 * This Wrapper is used for existing instances of an LogRecord to provide
 * all information needed. Unfortunately it is not immutable because one cannot
 * clone an LogRecord.
 * @author Boris Unckel
 * @since 0.5
 */
public final class ExtendedLogRecordWrapper extends AbstractExtendedLogRecord {

    // -------------------------------------------------------------- Variables

    /**
     * 
     */
    private static final long serialVersionUID = 4477335741068732142L;

    private static final long startTime = System.currentTimeMillis();

    /**
     * This variable contains information about this event's throwable
     */
    private ThrowableInformation throwableInfo = null;

    /**
     * Location information for the caller.
     */
    private LocationInfo locationInfo = null;

    private LogRecord record = null;

    // ------------------------------------------------------------ Constructor

    /**
     * This implementation is backuped by an attached original LogRecord.
     * 
     * @param record which contains the basic information.
     */
    public ExtendedLogRecordWrapper(final LogRecord record) {
        super(record.getLevel(), record.getMessage());
        this.record = record;
    }

    /**
     * @see java.util.logging.LogRecord#getLevel()
     * @since 0.5
     */
    public Level getLevel() {
        return this.record.getLevel();
    }

    /**
     * @see java.util.logging.LogRecord#getLoggerName()
     * @since 0.5
     */
    public String getLoggerName() {
        return this.record.getLoggerName();
    }

    /**
     * @see java.util.logging.LogRecord#getMessage()
     * @since 0.5
     */
    public String getMessage() {
        return this.record.getMessage();
    }

    /**
     * @see java.util.logging.LogRecord#getMillis()
     * @since 0.5
     */
    public long getMillis() {
        return this.record.getMillis();
    }

    /**
     * @see java.util.logging.LogRecord#getParameters()
     * @since 0.5
     */
    public Object[] getParameters() {
        return this.record.getParameters();
    }

    /**
     * @see java.util.logging.LogRecord#getResourceBundle()
     * @since 0.5
     */
    public ResourceBundle getResourceBundle() {
        return this.record.getResourceBundle();
    }

    /**
     * @see java.util.logging.LogRecord#getResourceBundleName()
     * @since 0.5
     */
    public String getResourceBundleName() {
        return this.record.getResourceBundleName();
    }

    /**
     * @see java.util.logging.LogRecord#getSequenceNumber()
     * @since 0.5
     */
    public long getSequenceNumber() {
        return this.record.getSequenceNumber();
    }

    /**
     * @see java.util.logging.LogRecord#getSourceClassName()
     * @since 0.5
     */
    public String getSourceClassName() {
        return this.record.getSourceClassName();
    }

    /**
     * @see java.util.logging.LogRecord#getSourceMethodName()
     * @since 0.5
     */
    public String getSourceMethodName() {
        return this.record.getSourceMethodName();
    }

    /**
     * @see java.util.logging.LogRecord#getThreadID()
     * @since 0.5
     */
    public int getThreadID() {
        return this.record.getThreadID();
    }

    /**
     * @see java.util.logging.LogRecord#getThrown()
     * @since 0.5
     */
    public Throwable getThrown() {
        return this.record.getThrown();
    }


}
