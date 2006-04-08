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

/**
 * Interface used to listen for {@link Logger} related events such as add/remove appender or
 * changing levels. Clients register an instance of the interface and the instance is called back
 * when the various events occur.
 * 
 * {@link LoggerRepository} provides methods for adding and removing LoggerEventListener instances.
 * 
 * When implementing the methods of this interface, it is useful to remember that the Logger can
 * access the repository using its getRepository() method.
 * 
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
public interface LoggerEventListener {
    /**
     * Called when an appender is added to the logger.
     * 
     * @param logger The logger to which the appender was added.
     * @param appender The appender added to the logger.
     */
    public void appenderAddedEvent(ExtendedLogger logger, ExtendedHandler handler);

    /**
     * Called when an appender is removed from the logger.
     * 
     * @param logger The logger from which the appender was removed.
     * @param appender The appender removed from the logger.
     */
    public void appenderRemovedEvent(ExtendedLogger logger, ExtendedHandler handler);

    /**
     * Called when level changed on the logger.
     * 
     * @param logger The logger that changed levels.
     */
    public void levelChangedEvent(ExtendedLogger logger);

}

// EOF LoggerEventListener.java
