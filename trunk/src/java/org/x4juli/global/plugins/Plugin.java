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
package org.x4juli.global.plugins;

import java.beans.PropertyChangeListener;

import org.x4juli.global.spi.LoggerRepository;
import org.x4juli.global.spi.OptionHandler;

/**
 * Defines the required interface for all Plugin objects.
 * 
 * <p>
 * A plugin implements some specific functionality to extend x4juli. Each plugin is
 * associated with a specific LoggerRepository, which it then uses/acts upon. The functionality of
 * the plugin is up to the developer.
 * </p>
 * <p>
 * Examples of plugins are Receiver and Watchdog. Receiver plugins allow for remote <code>LogRecord</code>s
 * to be received and processed by a repository as if the event was sent locally. Watchdog plugins
 * allow for a repository to be reconfigured when some "watched" configuration data changes.
 * </p>
 * <p>
 * Logging API as a whole was originally done for <a href="http://logging.apache.org/log4j/">Apache
 * log4j</a>. <b>x4uli</b> is a port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging APIs</a>. All
 * credits for initial idea, design, implementation, documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This file was originally
 * published by <i>Mark Womack, Nicko Cadell, Paul Smith</i>. Please use exclusively the
 * <i>appropriate</i> mailing lists for questions, remarks and contribution.
 * </p>
 * 
 * @author Boris Unckel
 * @since 0.7
 */
public interface Plugin extends OptionHandler {
    /**
     * Gets the name of the plugin.
     * 
     * @return String the name of the plugin.
     * @since 0.7
     */
    public String getName();

    /**
     * Sets the name of the plugin.
     * 
     * @param name the name of the plugin.
     * @since 0.7
     */
    public void setName(String name);

    /**
     * Gets the logger repository for this plugin.
     * 
     * @return LoggerRepository the logger repository this plugin is attached to.
     * @since 0.7
     */
    public LoggerRepository getLoggerRepository();

    /**
     * Sets the logger repository used by this plugin. This
     * repository will be used by the plugin functionality.
     * 
     * @param repository the logger repository to attach this plugin to.
     * @since 0.7
     */
    public void setLoggerRepository(LoggerRepository repository);

    /**
     * Adds a PropertyChangeListener to this instance which is
     * notified only by changes of the property with name propertyName
     * @param propertyName the name of the property in standard JavaBean syntax (e.g. for setName(), property="name")
     * @param l
     * @since 0.7
     */
    public void addPropertyChangeListener(
      String propertyName, PropertyChangeListener l);

    /**
     * Adds a PropertyChangeListener that will be notified of all property
     * changes.
     * 
     * @param l The listener to add.
     * @since 0.7
     */
    public void addPropertyChangeListener(PropertyChangeListener l);

    /**
     * Removes a specific PropertyChangeListener from this instances
     * registry that has been mapped to be notified of all property
     * changes.
     * 
     * @param l The listener to remove.
     * @since 0.7
     */
    public void removePropertyChangeListener(PropertyChangeListener l);

    /**
     * Removes a specific PropertyChangeListener from this instance's
     * registry which has been previously registered to be notified
     * of only a specific property change.
     * @param propertyName
     * @param l
     * @since 0.7
     */
    public void removePropertyChangeListener(
      String propertyName, PropertyChangeListener l);

    /**
     * True if the plugin is active and running.
     * 
     * @return boolean true if the plugin is currently active.
     * @since 0.7
     */
    public boolean isActive();

    /**
     * Returns true if the testPlugin is considered to be "equivalent" to the
     * this plugin.  The equivalency test is at the discretion of the plugin
     * implementation.  The PluginRegistry will use this method when starting
     * new plugins to see if a given plugin is considered equivalent to an
     * already running plugin with the same name.  If they are considered to
     * be equivalent, the currently running plugin will be left in place, and
     * the new plugin will not be started.
     * 
     * It is possible to override the equals() method, however this has
     * more meaning than is required for this simple test and would also
     * require the overriding of the hashCode() method as well.  All of this
     * is more work than is needed, so this simple method is used instead.
     * 
     * @param testPlugin The plugin to test equivalency against.
     * @return Returns true if testPlugin is considered to be equivelent.
     * @since 0.7
     */
    public boolean isEquivalent(Plugin testPlugin);
    
    /**
     * Call when the plugin should be stopped.
     * @since 0.7
     */
    public void shutdown();
}

// EOF Plugin.java
