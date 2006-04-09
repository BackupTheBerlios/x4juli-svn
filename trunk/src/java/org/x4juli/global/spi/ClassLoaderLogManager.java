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

import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.x4juli.config.joran.JoranConfigurator;
import org.x4juli.global.Constants;
import org.x4juli.global.LoggerClassInformation;
import org.x4juli.global.LoggerRepositoryHolder;
import org.x4juli.global.context.ContextFactory;
import org.x4juli.logger.NOPLogger;

/**
 * Per classloader LogManager implementation. Based on Version 1.11 <a
 * href="http://cvs.apache.org/viewcvs.cgi/jakarta-tomcat-connectors/juli/src/java/org/apache/juli/">
 * Apache Tomcat JULI</a>
 *
 * @author Remy Maucherat
 * @author Boris Unckel
 *
 * @since 0.5
 *
 */
public abstract class ClassLoaderLogManager extends LogManager implements LoggerRepositoryHolder,
        LoggerClassInformation {

    // -------------------------------------------------------------- Variables

    /**
     * Map containing the classloader information, keyed per classloader. A weak
     * hashmap is used to ensure no classloader reference is leaked from
     * application redeployment.
     */
    protected final Map classLoaderLoggers = new WeakHashMap();

    // ------------------------------------------------------------ Constructor

    /**
     * Default Constructor used by bootstrap of <code>java.util.logging.LogManager</code>.
     */
    public ClassLoaderLogManager() {
        super();
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Add the specified logger to the classloader local configuration.
     *
     * @param logger The logger to be added.
     * @return whether the logger had to be added (true) or not (false).
     * @exception  SecurityException  if a security manager exists and if
     *             the caller does not have LoggingPermission("control").
     * @since 0.7
     */
    public synchronized boolean addLogger(final java.util.logging.Logger logger) {
        checkAccess();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        LoggerRepository repository = getClassLoaderInfo(classLoader).repository;
        boolean ret = repository.addLogger(logger);
        return ret;
    }

    /**
     * Get the logger associated with the specified name inside the classloader
     * local configuration. If this returns null, and the call originated for
     * Logger.getLogger, a new logger with the specified name will be
     * instantiated and added using addLogger.
     *
     * @param name The name of the logger to retrieve.
     * @return the logger corresponding to classloader and name.
     */
    public synchronized java.util.logging.Logger getLogger(final String name) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        LoggerRepository repository = getClassLoaderInfo(classLoader).repository;
        return (java.util.logging.Logger) repository.getLogger(name);
    }

    /**
     * Get an enumeration of the logger names currently defined in the
     * classloader local configuration.
     *
     * @return all logger names corresponding to the classloder.
     * @since 0.7
     */
    public synchronized Enumeration getLoggerNames() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ClassLoaderLogInfo info = getClassLoaderInfo(classLoader);
        return info.repository.getCurrentLoggerNames();
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void reset() throws SecurityException {
        checkAccess();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ClassLoaderLogInfo info = getClassLoaderInfo(classLoader);
        info.repository.resetConfiguration();
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void addPropertyChangeListener(PropertyChangeListener l) throws SecurityException {
        checkAccess();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ClassLoaderLogInfo info = getClassLoaderInfo(classLoader);
        info.addPropertyChangeListener(l);
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void removePropertyChangeListener(PropertyChangeListener l) throws SecurityException {
        checkAccess();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ClassLoaderLogInfo info = getClassLoaderInfo(classLoader);
        info.removePropertyChangeListener(l);
    }

    /**
     * Get the value of the specified property in the classloader local
     * configuration.
     *
     * @param name The property name
     * @return the property value or if not existing null.
     */
    public String getProperty(final String name) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ClassLoaderLogInfo info = getClassLoaderInfo(classLoader);
        String result = info.repository.getProperty(name);
        // Simple property replacement (mostly for folder names)
        if (result != null) {
            result = replace(result);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.5
     */
    public void readConfiguration() throws IOException, SecurityException {

        checkAccess();

        readConfiguration(Thread.currentThread().getContextClassLoader());
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.5
     */
    public void readConfiguration(final InputStream is) throws IOException, SecurityException {

        checkAccess();
        reset();

        readConfiguration(is, Thread.currentThread().getContextClassLoader());
    }

    /**
     * {@inheritDoc}
     * @since 0.5
     * @exception  SecurityException  if a security manager exists and if
     *             the caller does not have LoggingPermission("control").
     */
    public LoggerRepository getLoggerRepository() {
        checkAccess();
        ClassLoaderLogInfo cllInfo = getClassLoaderInfo(
                Thread.currentThread().getContextClassLoader());
        return cllInfo.repository;
    }

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public String getFQCNofLogger() {
        return "java.util.logging.Logger";
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Retrieve the configuration associated with the specified classloader. If
     * it does not exist, it will be created.
     *
     * @param classloader The classloader for which we will retrieve or build
     *            the configuration
     * @return the LogInfo for the given classLoader.
     */
    protected ClassLoaderLogInfo getClassLoaderInfo(final ClassLoader classloader) {
        ClassLoader clToFind = classloader;
        if (clToFind == null) {
            clToFind = ClassLoader.getSystemClassLoader();
        }
        ClassLoaderLogInfo info = (ClassLoaderLogInfo) this.classLoaderLoggers.get(clToFind);
        if (info == null) {
            final ClassLoader classLoaderParam = clToFind;
            AccessController.doPrivileged(new PrivilegedAction() {
                public Object run() {
                    try {
                        readConfiguration(classLoaderParam);
                    } catch (IOException e) {
                        // Ignored Exception, to recognize it,
                        // use IDE Debugger Breakpoint in NOPLogger
                        NOPLogger.NOP_LOGGER.log(Level.FINEST, "Ignored exception", e);
                    }
                    return null;
                }
            });
            info = (ClassLoaderLogInfo) this.classLoaderLoggers.get(clToFind);
        }
        return info;
    }

    /**
     * Read configuration for the specified classloader.
     *
     * @param classLoader to load the properties from.
     * @throws IOException if reading the contained or derived
     *             logging.properties file fails.
     */
    protected void readConfiguration(final ClassLoader classLoader) throws IOException {
        readClassLoaderId(classLoader);
        InputStream is = null;
        // Special case for URL classloaders which are used in containers:
        // only look in the local repositories to avoid redefining loggers 20
        // times
        if ((classLoader instanceof URLClassLoader)
                && (((URLClassLoader) classLoader).findResource("x4juli.xml") != null)) {
            is = classLoader.getResourceAsStream("x4juli.xml");
        }
        if ((is == null) && (classLoader == ClassLoader.getSystemClassLoader())) {
            String configFileStr = System.getProperty("java.util.logging.config.file");
            if (configFileStr != null) {
                try {
                    is = new FileInputStream(replace(configFileStr));
                } catch (IOException e) {
                    // Ignored Exception, to recognize it, use IDE Debugger Breakpoint in NOPLogger
                    NOPLogger.NOP_LOGGER.log(Level.FINEST, "Ignored exception", e);
                }
            }
            // Try the default x4juli configuration
            if (is == null) {
                    is = ClassLoader.getSystemClassLoader().getResourceAsStream("x4juli-default.xml");
            }
        }

        LoggerFactory loggerFactory = getLoggerFactory();
        ExtendedLogger localRootLogger = loggerFactory.makeNewLoggerInstance("", null); 
        if (is == null) {
            // Retrieve the root logger of the parent classloader instead
            ClassLoader current = classLoader.getParent();
            ClassLoaderLogInfo info = null;
            while (current != null && info == null) {
                info = getClassLoaderInfo(current);
                current = current.getParent();
            }
            if (info != null) {
                localRootLogger.setParent((Logger)info.repository.getRootLogger());
            }
        }
        Hierarchy hierarchy = new Hierarchy(localRootLogger);
        hierarchy.setLoggerFactory(loggerFactory);
        ClassLoaderLogInfo info = new ClassLoaderLogInfo(hierarchy);
        this.classLoaderLoggers.put(classLoader, info);

        if (is != null) {
            readConfiguration(is, classLoader);
        }
    }

    /**
     * Load specified configuration.
     *
     * @param is InputStream to the properties file
     * @param classLoader for which the configuration will be loaded
     * @throws IOException If something wrong happens during load.
     */
    protected void readConfiguration(final InputStream is,
                                     final ClassLoader classLoader) throws IOException {

        ClassLoaderLogInfo info = (ClassLoaderLogInfo) this.classLoaderLoggers.get(classLoader);
        readClassLoaderId(classLoader);
        JoranConfigurator joranConfigurator = new JoranConfigurator();

        try {
            joranConfigurator.doConfigure(is, info.repository);
        } finally {
            try {
                is.close();
            } catch (Throwable t) {
                // Ignored Exception, to recognize it, use IDE Debugger Breakpoint in NOPLogger
                NOPLogger.NOP_LOGGER.log(Level.FINEST, "Ignored exception", t);
            }
        }

    }

    /**
     * Reads the identifier of the ClassLoader.
     * If no identifier properties are specified <code>Constants.CLASSLOADER_SYSTEM_ID</code>
     * is used.
     * For <code>ClassLoader.getSystemClassLoader()</code> the <code>Constants.CLASSLOADER_SYSTEM_ID</code>
     * is used. 
     * @param classLoader to read identifier from.
     * @since 0.7
     * @see ContextFactory#getThreadContextClassLoaderMapper()
     */
    protected void readClassLoaderId(final ClassLoader classLoader) {
        if(classLoader == null) {
            return;
        }
        final TCCLMapper mapper = ContextFactory.getThreadContextClassLoaderMapper();
        final int clHashCode = classLoader.hashCode();
        if(classLoader == ClassLoader.getSystemClassLoader()) {
            mapper.addClassLoaderId(clHashCode, Constants.CLASSLOADER_SYSTEM_ID);
            return;
        }
        InputStream isForId = classLoader.getResourceAsStream(Constants.CLASSLOADER_ID_FILE);
        if (isForId != null) {
            Properties idProps = new Properties();
            try {
                idProps.load(isForId);
            } catch (Exception e) {
                // Ignored Exception, to recognize it, use IDE Debugger Breakpoint in NOPLogger
                NOPLogger.NOP_LOGGER.log(Level.FINEST, "Ignored exception", e);
            }
            final String identifier = idProps.getProperty(Constants.CLASSLOADER_ID_PROPERTY, Constants.CLASSLOADER_DEFAULT_ID);
            mapper.addClassLoaderId(clHashCode, identifier);
        }
    }
    
    /**
     * Retrieves the actual LoggerFactory.
     * @return the loggerFactory to use for the LoggerRepository.
     */
    protected abstract LoggerFactory getLoggerFactory();
    
    /**
     * System property replacement in the given string.
     *
     * @param str The original string
     * @return the modified string
     */
    protected String replace(final String str) {
        String result = str.trim();
        if (result.startsWith("${")) {
            int pos = result.indexOf('}');
            if (pos != -1) {
                String propName = result.substring(2, pos);
                String replacement = System.getProperty(propName);
                if (replacement != null) {
                    result = replacement + result.substring(pos + 1);
                }
            }
        }
        return result;
    }

    // -------------------------------------------- ClassLoaderInfo Inner Class

    /**
     * Repository for loggers, nodes, handlers, properties for each classloader.
     *
     * @since 0.5
     */
    protected static final class ClassLoaderLogInfo {

        public final LoggerRepository repository;
        private final Hashtable listenerWrapper = new Hashtable();

        /**
         * Constructor.
         *
         * @param rootNode of this classloader.
         */
        ClassLoaderLogInfo(final LoggerRepository loggerRepository) {
            this.repository = loggerRepository;
        }

        void addPropertyChangeListener(PropertyChangeListener l) {
            if(l == null) {
                return;
            }
            PropertyChangeListenerWrapper wrapper = new PropertyChangeListenerWrapper(l);
            repository.addLoggerRepositoryEventListener(wrapper);
            this.listenerWrapper.put(l, wrapper);
        }

        void removePropertyChangeListener(PropertyChangeListener l) {
            if(l == null) {
                return;
            }
            PropertyChangeListenerWrapper wrapper = (PropertyChangeListenerWrapper)
                                              this.listenerWrapper.get(l);
            if(wrapper == null) {
                return;
            }
            repository.removeLoggerRepositoryEventListener(wrapper);
            this.listenerWrapper.remove(l);
        }

    }

}

// EOF ClassLoaderLogManager.java
