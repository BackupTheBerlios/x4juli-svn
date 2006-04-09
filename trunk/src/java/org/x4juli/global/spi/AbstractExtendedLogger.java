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

import java.util.Arrays;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Logger;


/**
 * Basic implementation for implementing directly a ExtendedLogger.
 * This is mainly for wrappers with an interface, i.E. Jakarta Commons
 * Logging or Simple Logging Facade for Java.
 * As sample refer to {@link org.x4juli.X4JuliLogger}.
 * 
 * @author Boris Unckel
 * @since 0.6
 */
public abstract class AbstractExtendedLogger extends Logger implements ExtendedLogger, HandlerAttachable {

    private LoggerRepository repository = null;
    
    /**
     * Constructs a logger with a specific resourcebundle.
     * @param name of the logger.
     * @param resourceBundleName for i18n messages.
     */
    protected AbstractExtendedLogger(final String name, final String resourceBundleName) {
        super(name, resourceBundleName);
    }

    /**
     * Adds the LoggerName and if available the resource bundle to the LogRecord.
     * @param logRecord to complete with common info.
     */
    protected void completeLogRecord(final ExtendedLogRecord logRecord) {
        logRecord.setLoggerName(getName());
        String rbName = getResourceBundleName();
        if (rbName != null && logRecord.getResourceBundleName() == null) {
            logRecord.setResourceBundleName(rbName);
            logRecord.setResourceBundle(getResourceBundle());
        }
        logRecord.getNDC();
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public final LoggerRepository getLoggerRepository() {
        return this.repository;
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public final void setLoggerRepository(final LoggerRepository repository, final SpiSecurity security) {
        if(security == null) {
            throw new LogSecurityException(
             "The setLoggerRepository is not allowed to be called" 
             + "from outside org.x4juli.global.spi");
        }
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void addHandler(final ExtendedHandler newHandler) throws SecurityException {
        this.addHandler((Handler) newHandler);
        
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public List getAllHandlers() {
        return Arrays.asList(getHandlers());
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public ExtendedHandler getHandler(final String name) {
        ExtendedHandler ret = null;
        Handler[] handlers = getHandlers();
        for (int i = 0; i < handlers.length; i++) {
            ExtendedHandler handler = (ExtendedHandler) handlers[i];
            if(handler.getName().equals(name)) {
                ret = handler;
                break;
            }
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public boolean isAttached(final ExtendedHandler handler) {
        boolean ret = false;
        Handler[] handlers = getHandlers();
        for (int i = 0; i < handlers.length; i++) {
            ExtendedHandler localhandler = (ExtendedHandler) handlers[i];
            if(localhandler == handler) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void removeAllHandlers() throws SecurityException {
        Handler[] handlers = getHandlers();
        for (int i = 0; i < handlers.length; i++) {
            Handler localhandler = handlers[i];
            removeHandler(localhandler);
            }
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void removeHandler(final ExtendedHandler handler) throws SecurityException {
        removeHandler((Handler)handler);
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void removeHandler(String name) throws SecurityException {
        Handler[] handlers = getHandlers();
        for (int i = 0; i < handlers.length; i++) {
            ExtendedHandler handler = (ExtendedHandler) handlers[i];
            if(handler.getName().equals(name)) {
                removeHandler(handler);
                break;
            }
        }
    }

}

// EOF AbstractExtendedLogger.java
