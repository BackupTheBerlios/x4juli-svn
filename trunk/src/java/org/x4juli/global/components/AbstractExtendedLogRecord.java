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

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

import org.x4juli.global.LoggerClassInformation;
import org.x4juli.global.context.MDC;
import org.x4juli.global.context.NDC;
import org.x4juli.global.spi.ExtendedLogRecord;
import org.x4juli.global.spi.ThrowableInformation;
import org.x4juli.global.spi.location.LocationInfo;

/**
 * An abstract class defining the missing methods for full featured logging. Specifies lazy load of
 * an extend <code>java.util.logging.LogRecord</code>. Information are created just when they are
 * needed and just once for each object.
 * 
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

    /**
     * The full qualified class name of the logger which submitted the logrecord.
     */
    private String fqcnOfLogger = ((LoggerClassInformation) LogManager.getLogManager())
            .getFQCNofLogger();

    /**
     * The nested diagnostic context (NDC) of logging event.
     */
    private String ndc;

    /**
     * Have we tried to do an NDC lookup? If we did, there is no need to do it again. Note that its
     * value is always false when serialized. Thus, a receiving SocketNode will never use it's own
     * (incorrect) NDC. See also writeObject method.
     */
    private boolean ndcLookupRequired = true;

    private final String threadName;

    /**
     * <p>
     * The properties map is specific for this LogRecord.
     * </p>
     * 
     * <p>
     * When serialized, it contains a copy of MDC properties as well.
     * </p>
     * 
     * <p>
     * It survives serialization.
     * </p>
     */
    private Map properties;

    // ----------------------------------------------------------- Constructors

    /**
     * Constructs a new ExtendedLogRecord.
     * 
     * @param level to log.
     * @param msg to format and log.
     */
    public AbstractExtendedLogRecord(final Level level, final String msg) {
        super(level, msg);
        String tempThreadname = null;
        try {
            tempThreadname = Thread.currentThread().getName();
        } catch (Exception e) {
            tempThreadname = String.valueOf(getThreadID());
        }
        this.threadName = tempThreadname;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * {@inheritDoc}
     * 
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
     * 
     * @since 0.5
     */
    public LocationInfo getLocationInformation() {
        if (this.locationInfo == null) {
            this.locationInfo = new LocationInfo(new Throwable(), this.fqcnOfLogger);
        }
        return this.locationInfo;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.5
     */
    public long getStartTime() {
        return AbstractExtendedLogRecord.START_TIME;
    }

    /**
     * {@inheritDoc}
     * 
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
        buf.append("] ThreadName[");
        buf.append(getThreadName());
        buf.append("] Message[");
        buf.append(getMessage());
        buf.append("] Thrown[");
        buf.append(getThrown());
        buf.append("]");
        return buf.toString();
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.6
     */
    public void setFormattedMessage(final String formattedMessage) {
        this.cachedFormattedMessage = formattedMessage;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.6
     */
    public String getFormattedMessage() {
        return this.cachedFormattedMessage;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public String getFQCNofLogger() {
        return this.fqcnOfLogger;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void setFQCNofLogger(final String fqcn) {
        this.fqcnOfLogger = fqcn;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public synchronized String getNDC() {
        if (ndcLookupRequired) {
            ndcLookupRequired = false;
            ndc = NDC.get();
        }
        return ndc;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public synchronized void setNDC(final String ndcString) {
        if (this.ndc != null) {
            throw new IllegalStateException("The NDC has been already set.");
        }
        ndcLookupRequired = false;
        ndc = ndcString;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public String getThreadName() {
        return this.threadName;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public String getProperty(String key) {
        String value = null;

        if (properties != null) {
            value = (String) properties.get(key);

            if (value != null) {
                return value;
            }
        }

        // if the key was not found in this even't properties, try the MDC
        value = MDC.get(key);

        return value;

    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public Map getProperties() {
        return this.properties;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void setProperties(final Hashtable props) {
        this.properties = props;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void setProperty(String key, String value) {
        if (properties == null) {
            // create a copy of MDC and repository properties
            initializeProperties();
        }

        if (value != null) {
            properties.put(key, value);
        } else {
            properties.remove(key);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void initializeProperties() {
        if (properties == null) {
            properties = new TreeMap();
            Map mdcMap = MDC.getContext();
            if (mdcMap != null) {
                properties.putAll(mdcMap);
            }
        }
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public Set getPropertyKeySet() {
        initializeProperties();
        return Collections.unmodifiableSet(properties.keySet());
    }

}

// EOF AbstractExtendedLogRecord.java
