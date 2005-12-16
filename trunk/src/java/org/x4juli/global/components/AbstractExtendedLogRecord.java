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
package org.x4juli.global.components;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

import org.x4juli.global.LoggerClassInformation;
import org.x4juli.global.spi.ExtendedLogRecord;
import org.x4juli.global.spi.ThrowableInformation;
import org.x4juli.global.spi.location.LocationInfo;

/**
 * An abstract class defining the missing methods for full featured logging.
 * Specifies lazy load of an extend <code>java.util.logging.LogRecord</code>.
 * Information are created just when they are needed and just once for each
 * object.
 * @author Boris Unckel
 * @since 0.5
 */
public abstract class AbstractExtendedLogRecord extends LogRecord implements ExtendedLogRecord {

    // -------------------------------------------------------------- Variables
    private static final long START_TIME = System.currentTimeMillis();

    /**
     * Represents the last formatted message.
     */
    protected String cachedFormattedMessage = null;

    /**
     * This variable contains information about this event's throwable.
     */
    private ThrowableInformation throwableInfo = null;

    /**
     * Location information for the caller.
     */
    private LocationInfo locationInfo = null;

    // ----------------------------------------------------------- Constructors

    /**
     * Constructs a new ExtendedLogRecord.
     * @param level to log.
     * @param msg to format and log.
     */
    public AbstractExtendedLogRecord(final Level level, final String msg) {
        super(level, msg);
    }

    // --------------------------------------------------------- Public Methods

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public ThrowableInformation getThrowableInformation() {
        if (getThrown() == null) {
            return null;
        }
        if (this.throwableInfo == null) {
                this.throwableInfo = new ThrowableInformation(getThrown());
        }
        return this.throwableInfo;
    }

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public LocationInfo getLocationInformation() {
        if (this.locationInfo == null) {
            LoggerClassInformation lci = (LoggerClassInformation) LogManager.getLogManager();
            String fqcnOfLogger = lci.getFQCNofLogger();
            this.locationInfo = new LocationInfo(new Throwable(), fqcnOfLogger);
        }
        return this.locationInfo;
    }

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public long getStartTime() {
        return AbstractExtendedLogRecord.START_TIME;
    }

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public String toString() {
        StringBuffer buf = new StringBuffer("ExtendedLogRecord:");
        buf.append("Level[");
        buf.append(getLevel());
        buf.append("] LoggerName[");
        buf.append(getLoggerName());
        buf.append("] Millis[");
        buf.append(getMillis());
        buf.append("] SequenceNumber[");
        buf.append(getSequenceNumber());
        buf.append("] ThreadID[");
        buf.append(getThreadID());
        buf.append("] Message[");
        buf.append(getMessage());
        buf.append("] Thrown[");
        buf.append(getThrown());
        buf.append("]");
        return buf.toString();
    }

    /**
     * {@inheritDoc}
     * @since 0.6
     */
    public void setFormattedMessage(final String formattedMessage) {
        this.cachedFormattedMessage = formattedMessage;
    }

    /**
     * {@inheritDoc}
     * @since 0.6
     */
    public String getFormattedMessage() {
        if (this.cachedFormattedMessage != null) {
            return this.cachedFormattedMessage;
        }
        return getMessage();
    }

}

// EOF AbstractExtendedLogRecord.java
