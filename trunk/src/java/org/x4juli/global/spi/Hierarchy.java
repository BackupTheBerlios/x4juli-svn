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
import java.util.Map;

import org.x4juli.global.plugins.PluginRegistry;
import org.x4juli.global.scheduler.Scheduler;
import org.x4juli.logger.DefaultJDKLoggerFactory;
import org.x4juli.logger.NOPLogger;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;

/**
 * Missing documentation.
 * <p>
 * Logging API as a whole was originally done for <a href="http://logging.apache.org/log4j/">Apache
 * log4j</a>. <b>x4uli</b> is a port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging APIs</a>. All
 * credits for initial idea, design, implementation, documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This file was originally
 * published by <i>Ceki G&uuml;lc&uuml;, Mark Womack</i>. Please use exclusively the <i>appropriate</i>
 * mailing lists for questions, remarks and contribution.
 * </p>
 * 
 * @author Boris Unckel
 * @since 0.7
 */
public class Hierarchy implements LoggerRepository {

    private LoggerFactory defaultFactory;

    private LoggerFactory loggerFactory = null;

    private ArrayList repositoryEventListeners;

    private ArrayList loggerEventListeners;

    private final SpiSecurity security;

    String name;

    Hashtable ht;

    ExtendedLogger root;

    PluginRegistry pluginRegistry;

    Map properties;

    private Scheduler scheduler;

    // The repository can also be used as an object store for various objects used
    // by log4j components
    private Map objectMap;

    // the internal logger used by this instance of Hierarchy for its own reporting
    private ExtendedLogger myLogger;

    private List errorList = new Vector();

    boolean emittedNoHandlerWarning = false;

    boolean emittedNoResourceBundleWarning = false;

    boolean pristine = true;

    boolean inheritedHierachy;

    /**
     * Create a new logger hierarchy.
     * 
     * @param root The root of the new hierarchy.
     * 
     */
    public Hierarchy(ExtendedLogger root) {
        ht = new Hashtable();
        repositoryEventListeners = new ArrayList(1);
        loggerEventListeners = new ArrayList(1);
        this.root = root;
        this.objectMap = new HashMap();
        this.security = new SpiSecurity();
        this.inheritedHierachy = false;
        this.root.setLoggerRepository((LoggerRepository) this, this.security);
        // rendererMap = new RendererMap();
        // rendererMap.setLoggerRepository(this);
        properties = new Hashtable();
        defaultFactory = new DefaultJDKLoggerFactory();
    }

    public void dump() {
        synchronized (ht) {
            Enumeration thekeys = ht.keys();
            StringBuffer buf = new StringBuffer("Hierarchy[");
            buf.append(name);
            buf.append("] Root[");
            buf.append(getRootLogger());
            buf.append("]: ");
            while (thekeys.hasMoreElements()) {
                Object key = thekeys.nextElement();
                Object value = ht.get(key);
                buf.append("[" + key + "=" + value + "]");
            }
            buf.append("End dump");
            System.out.println(buf.toString());
            System.out.flush();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void addErrorItem(final ErrorItem errorItem) {
        getErrorList().add(errorItem);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void addLoggerEventListener(final LoggerEventListener listener) {
        synchronized (loggerEventListeners) {
            if (loggerEventListeners.contains(listener)) {
                getMyLogger().warning(
                        "Ignoring attempt to add a previously registerd LoggerEventListener.");
            } else {
                loggerEventListeners.add(listener);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void addLoggerRepositoryEventListener(LoggerRepositoryEventListener listener) {
        synchronized (repositoryEventListeners) {
            if (repositoryEventListeners.contains(listener)) {
                getMyLogger()
                        .warning(
                                "Ignoring attempt to add a previously registerd LoggerRepositoryEventListener.");
            } else {
                repositoryEventListeners.add(listener);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void setLoggerFactory(final LoggerFactory logFactory) {
        if (this.loggerFactory != null) {
            throw new LogIllegalStateException(
                    "LoggerFactory is not allowed to be set twice. Current["
                            + loggerFactory.getClass().getName() + "]");
        }
        this.loggerFactory = logFactory;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void emitNoHandlerWarning(ExtendedLogger logger) {
        if (!this.emittedNoHandlerWarning) {
            this.emittedNoHandlerWarning = true;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public ExtendedLogger exists(final String loggername) {
        Object o = ht.get(new LoggerKey(loggername));

        if (o instanceof ExtendedLogger) {
            return (ExtendedLogger) o;
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public LoggerFactory getLoggerFactory() {
        if (loggerFactory != null) {
            return loggerFactory;
        }
        return defaultFactory;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void fireAddHandlerEvent(final ExtendedLogger logger, final ExtendedHandler handler) {
        ArrayList list = copyListenerList(loggerEventListeners);
        int size = list.size();

        for (int i = 0; i < size; i++) {
            ((LoggerEventListener) list.get(i)).appenderAddedEvent(logger, handler);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void fireConfigurationChangedEvent() {
        ArrayList list = copyListenerList(repositoryEventListeners);
        int size = list.size();

        for (int i = 0; i < size; i++) {
            ((LoggerRepositoryEventListener) list.get(i)).configurationChangedEvent(this);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void fireLevelChangedEvent(final ExtendedLogger logger) {
        ArrayList list = copyListenerList(loggerEventListeners);
        int size = list.size();

        for (int i = 0; i < size; i++) {
            ((LoggerEventListener) list.get(i)).levelChangedEvent(logger);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void fireRemoveHandlerEvent(final ExtendedLogger logger, final ExtendedHandler handler) {
        ArrayList list = copyListenerList(loggerEventListeners);
        int size = list.size();

        for (int i = 0; i < size; i++) {
            ((LoggerEventListener) list.get(i)).appenderRemovedEvent(logger, handler);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public Iterator getCurrentLoggers() {
        // The accumlation in v is necessary because not all elements in
        // ht are Logger objects as there might be some ProvisionNodes
        // as well.
        Vector v = new Vector(ht.size());

        Enumeration elems = ht.elements();

        while (elems.hasMoreElements()) {
            Object o = elems.nextElement();

            if (o instanceof Logger) {
                v.addElement(o);
            }
        }
        return v.iterator();
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public Enumeration getCurrentLoggerNames() {
        // The accumlation in v is necessary because not all elements in
        // ht are Logger objects as there might be some ProvisionNodes
        // as well.
        Vector v = new Vector(ht.size());

        Enumeration elems = ht.elements();

        while (elems.hasMoreElements()) {
            Object o = elems.nextElement();

            if (o instanceof Logger) {
                ExtendedLogger logger = (ExtendedLogger) o;
                v.addElement(logger.getName());
            }
        }
        return v.elements();
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public List getErrorList() {
        return this.errorList;
    }

    public ExtendedLogger getLogger(final String loggername) {
        return getLogger(loggername, null);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public ExtendedLogger getLogger(final String loggername, final String resourceBundleName) {
        // System.out.println("getInstance("+name+") called.");
        LoggerKey key = new LoggerKey(loggername);

        // Synchronize to prevent write conflicts. Read conflicts (in
        // getChainedLevel method) are possible only if variable
        // assignments are non-atomic.
        ExtendedLogger logger;

        synchronized (ht) {
            Object o = ht.get(key);

            if (o == null) {
                // Self logging of other methods of Hierarchy is wanted, but
                // in getLogger(...) impossible
                // getMyLogger().finer(
                // "Creating new logger [" + name + "] in repository [" + getName() + "].");
                logger = getLoggerFactory().makeNewLoggerInstance(loggername, resourceBundleName);
                logger.setLoggerRepository(this, security);
                ht.put(key, logger);
                updateParents(logger);
                return logger;
            } else if (o instanceof ExtendedLogger) {
                // getMyLogger().finer(
                // "Returning existing logger [" + loggername + "] in repository ["
                // + getName() + "].");
                return (ExtendedLogger) o;
            } else if (o instanceof LogNode) {
                // System.out.println("("+name+") ht.get(this) returned ProvisionNode");
                logger = getLoggerFactory().makeNewLoggerInstance(loggername, null);
                logger.setLoggerRepository(this, security);
                ht.put(key, logger);
                updateChildren((LogNode) o, logger);
                updateParents(logger);

                return logger;
            } else {
                // It should be impossible to arrive here
                return null; // but let's keep the compiler happy.
            }
        }
    }

    /**
     * The call of this method should be avoided, but it is needed for the
     * <code>java.util.logging.LogManager.addLogger(Logger)</code> method.
     * 
     * @param logger to add.
     * @return true if the logger did not exist before and was added, false if it already exists.
     */
    public boolean addLogger(final java.util.logging.Logger logger) {
        String loggername = logger.getName();
        if (loggername == null) {
            throw new LogIllegalParamter("Name of the logger should not be null");
        }
        ExtendedLogger localLogger = null;
        if (logger instanceof ExtendedLogger) {
            localLogger = (ExtendedLogger) logger;
        } else {
            return false;
        }
        LoggerKey key = new LoggerKey(loggername);
        synchronized (ht) {
            Object o = ht.get(key);
            if (o == null) {
                localLogger.setLoggerRepository(this, security);
                ht.put(key, localLogger);
                updateParents(localLogger);
                return true;
            } else if (o instanceof ExtendedLogger) {
                // Logger exists, will not be replaced.
                return false;
            } else if (o instanceof LogNode) {
                localLogger.setLoggerRepository(this, security);
                ht.put(key, localLogger);
                updateChildren((LogNode) o, localLogger);
                updateParents(localLogger);
                return true;
            } else {
                // It should be impossible to arrive here
                return false;
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public String getName() {
        return this.name;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public Object getObject(final String key) {
        return objectMap.get(key);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public PluginRegistry getPluginRegistry() {
        if (pluginRegistry == null) {
            pluginRegistry = new PluginRegistry(this);
        }
        return pluginRegistry;
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
    public String getProperty(final String key) {
        return (String) properties.get(key);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public ExtendedLogger getRootLogger() {
        return this.root;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public Scheduler getScheduler() {
        if (scheduler == null) {
            scheduler = new Scheduler();
            scheduler.setDaemon(true);
            scheduler.start();
        }
        return scheduler;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public boolean isPristine() {
        return this.pristine;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void putObject(final String key, final Object value) {
        objectMap.put(key, value);

    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void removeLoggerEventListener(final LoggerEventListener listener) {
        synchronized (loggerEventListeners) {
            if (!loggerEventListeners.contains(listener)) {
                getMyLogger().warning(
                        "Ignoring attempt to remove a non-registered LoggerEventListener.");
            } else {
                loggerEventListeners.remove(listener);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void removeLoggerRepositoryEventListener(final LoggerRepositoryEventListener listener) {
        synchronized (repositoryEventListeners) {
            if (!repositoryEventListeners.contains(listener)) {
                getMyLogger()
                        .warning(
                                "Ignoring attempt to remove a non-registered LoggerRepositoryEventListener.");
            } else {
                repositoryEventListeners.remove(listener);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void setName(final String repoName) {
        if (name == null) {
            name = repoName;
        } else if (!name.equals(repoName)) {
            throw new LogIllegalStateException("Repository [" + name + "] cannot be renamed as ["
                    + repoName + "].");
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void setPristine(final boolean state) {
        this.pristine = state;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void setProperty(final String key, final String value) {
        properties.put(key, value);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void shutdown() {
        getMyLogger().log(Level.INFO, "Shutting down repository["+getName()+"]");
        //System.out.println("Shutting down repository["+getName()+"]");
        shutdown(false);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void resetConfiguration() {
        getMyLogger().log(Level.FINE, "Resetting configuration of repository["+getName()+"]");
        getRootLogger().setLevel(Level.ALL);

        shutdown(true); // nested locks are OK

        Iterator cats = getCurrentLoggers();

        while (cats.hasNext()) {
            Logger c = (Logger) cats.next();
            c.setLevel(null);
            c.setUseParentHandlers(true);
        }

        setInherited(false);

        // inform the listeners that the configuration has been reset
        ArrayList list = copyListenerList(repositoryEventListeners);
        int size = list.size();

        for (int i = 0; i < size; i++) {
            ((LoggerRepositoryEventListener) list.get(i)).configurationResetEvent(this);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public boolean isInherited() {
        return this.inheritedHierachy;
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public String toString() {
        if(getName() != null) {
            return getName();
        }
        return super.toString();
    }

    /**
     * Sets the inherited attributed. This method is not visible in the interface LoggerRepository
     * 
     * @param inherited from another repository.
     */
    public void setInherited(final boolean inherited) {
        this.inheritedHierachy = inherited;
    }

    private ExtendedLogger getMyLogger() {
        if (myLogger == null) {
            myLogger = getLogger(this.getClass().getName());
        }
        return myLogger;
    }

    /**
     * Returns a copy of the given listener vector.
     */
    private ArrayList copyListenerList(final ArrayList list) {
        ArrayList listCopy = null;

        synchronized (list) {
            int size = list.size();
            listCopy = new ArrayList(size);

            for (int x = 0; x < size; x++) {
                listCopy.add(list.get(x));
            }
        }

        return listCopy;
    }

    /**
     * The shutdown behaviour depends on the isInherited() flag. If the repository is inherited, the
     * shutdown will just flush() all current handlers. If the repository is not inherited, it will
     * close all current handlers.
     * 
     * @param doingReset if true fire shutdown events.
     */
    private void shutdown(boolean doingReset) {

        // stop this repo's scheduler if it has one
        if (scheduler != null) {
            scheduler.shutdown();
            scheduler = null;
        }

        // let listeners know about shutdown if this is
        // not being done as part of a reset.
        if (!doingReset) {
            ArrayList list = copyListenerList(repositoryEventListeners);
            int size = list.size();

            for (int i = 0; i < size; i++) {
                ((LoggerRepositoryEventListener) list.get(i)).shutdownEvent(this);
            }
        }

        synchronized (ht) {
            HandlerAttachable myroot = getRootLogger();
            Iterator iterLogger = this.getCurrentLoggers();
            List rootHandlerList = myroot.getAllHandlers();
            // First all non root loggers are removed. This allows
            // the maximum of output in an LogManager shutdown hook phase,
            // based on a configured root logger of the System ClassLoader
            while (iterLogger.hasNext()) {
                HandlerAttachable currentLogger = (HandlerAttachable) iterLogger.next();
                List currentHandlerList = currentLogger.getAllHandlers();
                if (currentHandlerList == null || currentHandlerList.size() == 0) {
                    continue;
                }
                Iterator iterHandler = currentHandlerList.iterator();
                while (iterHandler.hasNext()) {
                    ExtendedHandler currentHandler = (ExtendedHandler) iterHandler.next();
                    if (!rootHandlerList.contains(currentHandler)) {
                        if (this.isInherited()) {
                            currentHandler.flush();
                        } else {
                            System.out.println("Closing Handler["+currentHandler.getName()+"]");
                            currentHandler.close();
                        }
                        currentLogger.removeHandler(currentHandler);
                    }
                }
            }

            Iterator iterRootHandler = rootHandlerList.iterator();
            while (iterRootHandler.hasNext()) {
                ExtendedHandler currentHandler = (ExtendedHandler) iterRootHandler.next();
                if (this.isInherited()) {
                    currentHandler.flush();
                } else {
                    System.out.println("Closing Handler["+currentHandler.getName()+"]");
                    currentHandler.close();
                }
                myroot.removeHandler(currentHandler);
            }
        }
    }

    private final void updateChildren(final LogNode pn, final ExtendedLogger logger) {
        // System.out.println("updateChildren called for " + logger.name);
        final int last = pn.size();

        for (int i = 0; i < last; i++) {
            ExtendedLogger l = (ExtendedLogger) pn.elementAt(i);

            // System.out.println("Updating child " +p.name);
            // Unless this child already points to a correct (lower) parent,
            // make cat.parent point to l.parent and l.parent to cat.
            if (!l.getParent().getName().startsWith(logger.getName())) {
                logger.setParent(l.getParent());
                l.setParent((java.util.logging.Logger) logger);
            }
        }
    }

    private final void updateParents(final ExtendedLogger logger) {
        String loggername = logger.getName();
        int length = loggername.length();
        boolean parentFound = false;

        // System.out.println("UpdateParents called for " + name);
        // if name = "w.x.y.z", loop thourgh "w.x.y", "w.x" and "w", but not "w.x.y.z"
        for (int i = loggername.lastIndexOf('.', length - 1); i >= 0; i = loggername.lastIndexOf(
                '.', i - 1)) {
            String substr = loggername.substring(0, i);

            // System.out.println("Updating parent : " + substr);
            LoggerKey key = new LoggerKey(substr); // simple constructor
            Object o = ht.get(key);

            // Create a provision node for a future parent.
            if (o == null) {
                // System.out.println("No parent "+substr+" found. Creating ProvisionNode.");
                LogNode pn = new LogNode(logger);
                ht.put(key, pn);
            } else if (o instanceof Logger) {
                parentFound = true;
                logger.setParent((java.util.logging.Logger) o);

                // System.out.println("Linking " + cat.name + " -> " + ((Category) o).name);
                break; // no need to update the ancestors of the closest ancestor
            } else if (o instanceof LogNode) {
                ((LogNode) o).addElement(logger);
            } else {
                Exception e = new LogIllegalStateException("unexpected object type " + o.getClass()
                        + " in ht.");
                e.printStackTrace();
            }
        }

        // If we could not find any existing parents, then link with root.
        if (!parentFound) {
            logger.setParent((java.util.logging.Logger) root);
        }
    }
}

// EOF Hierarchy.java
