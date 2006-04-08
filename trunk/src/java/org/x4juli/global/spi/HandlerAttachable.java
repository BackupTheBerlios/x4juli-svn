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
package org.x4juli.global.spi;

import java.util.List;

/**
 * Interface for attaching appenders to objects.
 * <p>
 * Logging API as a whole was originally done for <a href="http://logging.apache.org/log4j/">Apache
 * log4j</a>. <b>x4uli</b> is a port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging APIs</a>. All
 * credits for initial idea, design, implementation, documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This file was originally
 * published by <i>Ceki G&uuml;lc&uuml;</i>. Please use exclusively the <i>appropriate</i> mailing
 * lists for questions, remarks and contribution.
 * </p>
 * 
 * @author Boris Unckel
 * @since 0.7
 */
public interface HandlerAttachable {

    /**
     * Add an handler.
     * @param newHandler to add.
     * @throws SecurityException if touching is forbidden.
     * @since 0.7
     */
    public void addHandler(ExtendedHandler newHandler) throws SecurityException;

    /**
     * Get all previously added appenders as an List.
     * @return list containing ExtendedHandler objects.
     * @since 0.7
     */
    public List getAllHandlers();

    /**
     * Get an handler by name.
     * @param name of the handler.
     * @return the handler or null if not existing.
     * @since 0.7
     */
    public ExtendedHandler getHandler(String name);

    /**
     * Returns <code>true</code> if the specified appender is in list of attached attached,
     * <code>false</code> otherwise.
     * @param handler to find.
     * @return true if found, else false.
     * 
     * @since 0.7
     */
    public boolean isAttached(ExtendedHandler handler);

    /**
     * Remove all previously added appenders.
     * @throws SecurityException if touching is forbidden.
     * @since 0.7
     */
    void removeAllHandlers() throws SecurityException;

    /**
     * Remove the appender passed as parameter from the list of appenders.
     * @param handler to remove.
     * @throws SecurityException if touching is forbidden.
     * @since 0.7
     */
    void removeHandler(ExtendedHandler handler) throws SecurityException;

    /**
     * Remove the appender with the name passed as parameter from the list of appenders.
     * @param name of the handler
     * @throws SecurityException if touching is forbidden.
     * @since 0.7
     */
    void removeHandler(String name) throws SecurityException;

}

// EOF HandlerAttachable.java
