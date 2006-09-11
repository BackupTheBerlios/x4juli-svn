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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.x4juli.config.AbstractConfigurator;
import org.x4juli.config.joran.JoranConfigurator;
import org.x4juli.formatter.PatternFormatter;
import org.x4juli.global.Constants;
import org.x4juli.global.LoggerRepositoryHolder;
import org.x4juli.global.context.ContextFactory;
import org.x4juli.global.helper.IOUtil;
import org.x4juli.global.spi.location.LocationInfo;
import org.x4juli.handlers.ConsoleHandler;
import org.x4juli.logger.DefaultJDKLoggerFactory;
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
public abstract class ClassLoaderLogManager extends LogManager implements LoggerRepositoryHolder {

    // -------------------------------------------------------------- Variables

    /**
     * Map containing the classloader information, keyed per classloader. A weak hashmap is used to
     * ensure no classloader reference is leaked from application redeployment.
     */
    protected final Map classLoaderLoggers = new WeakHashMap();

    protected boolean internalLogPossible = false;

    protected final String FQCNofLogManager = this.getClass().getName();

    private ExtendedLogger myLogger = null;

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
     * @exception SecurityException if a security manager exists and if the caller does not have
     *                LoggingPermission("control").
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
     * Get the logger associated with the specified name inside the classloader local configuration.
     * If this returns null, and the call originated for Logger.getLogger, a new logger with the
     * specified name will be instantiated and added using addLogger.
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
     * Get an enumeration of the logger names currently defined in the classloader local
     * configuration.
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
     * 
     * @since 0.7
     */
    public void reset() throws SecurityException {
        // There are two cases:
        // 1) The java.util.LogManager has a shutdown hook (see private class Cleaner).
        // In this case, all Repositories have to be closed, even the one of the
        // SystemClassLoader
        // 2) The method was called from outside world, i.E. a J2EE Container which
        // has an hotdeploy feature and throws away the ClassLoader.
        // In this case, just the ContextClassLoader repository is beeing reseted.
        //
        // There is a difference between reset and shutdown!
        Exception ex = new Exception("Reset was called");
        LocationInfo locationInfo = new LocationInfo(ex, ClassLoaderLogManager.class.getName());
        String classname = locationInfo.getClassName();
        if (classname.equals("java.util.logging.LogManager$Cleaner")) {
            // No need to checkAccess() because this "if" can only be called from
            // the JVM with the shutdown hook
            getMyLogger().log(Level.INFO, "Shutdown hook cachted. Shutting down all repositories.");
            ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
            ClassLoaderLogInfo systemClassLoaderLogInfo = getClassLoaderInfo(systemClassLoader);
            Iterator classLoaderLogInfoIter = classLoaderLoggers.values().iterator();
            while (classLoaderLogInfoIter.hasNext()) {
                ClassLoaderLogInfo info = (ClassLoaderLogInfo) classLoaderLogInfoIter.next();
                if (info != systemClassLoaderLogInfo) {
                    try {
                        info.repository.shutdown();
                    } catch (Throwable t) {
                        t.printStackTrace();
                        // Nothing should prevent us to continue.
                    }
                }
            }
            try {
                systemClassLoaderLogInfo.repository.shutdown();
            } catch (Throwable t) {
                t.printStackTrace();
                // Nothing should prevent us to continue.
            }

        } else {
            // It is necessary to checkAccess, because this "else" can be called
            // from outside world.
            checkAccess();
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader == null) {
                classLoader = ClassLoader.getSystemClassLoader();
            }
            if (existsLogInfo(classLoader)) {
                ClassLoaderLogInfo info = getClassLoaderInfo(classLoader);
                info.repository.resetConfiguration();
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
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
     * 
     * @since 0.7
     */
    public void removePropertyChangeListener(PropertyChangeListener l) throws SecurityException {
        checkAccess();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ClassLoaderLogInfo info = getClassLoaderInfo(classLoader);
        info.removePropertyChangeListener(l);
    }

    /**
     * Get the value of the specified property in the classloader local configuration.
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
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        readConfiguration(classLoader);
        readClassLoaderId(classLoader);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.5
     */
    public void readConfiguration(final InputStream is) throws IOException, SecurityException {
        checkAccess();
        reset();
        // Configure with the given InputStream (which has to contain the XML format for
        // JoranConfigurator)
        // Use the TCCL
        // do a reset on the logger repository before the configuration starts again.
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        }
        Configurator defaultConfigurator = getDefaultConfigurator(classLoader);
        LoggerRepository repository = getClassLoaderInfo(classLoader).repository;
        runConfigurator(defaultConfigurator, is, repository, null);
        readClassLoaderId(classLoader);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.5
     * @exception SecurityException if a security manager exists and if the caller does not have
     *                LoggingPermission("control").
     */
    public LoggerRepository getLoggerRepository() {
        checkAccess();
        ClassLoaderLogInfo cllInfo = getClassLoaderInfo(Thread.currentThread()
                .getContextClassLoader());
        return cllInfo.repository;
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Retrieve the configuration associated with the specified classloader. If it does not exist,
     * it will be created.
     * 
     * @param classloader The classloader for which we will retrieve or build the configuration
     * @return the LogInfo for the given classLoader.
     */
    protected ClassLoaderLogInfo getClassLoaderInfo(final ClassLoader classloader) {
        ClassLoader clToFind = classloader;
        if (clToFind == null) {
            clToFind = ClassLoader.getSystemClassLoader();
        }
        ClassLoaderLogInfo info = (ClassLoaderLogInfo) this.classLoaderLoggers.get(clToFind);
        if (info == null) {
            getMyLogger().log(Level.INFO,
                    "Need to configure ClassLoaderLogInfo for[" + clToFind.hashCode() + "]");
            final ClassLoader classLoaderParam = clToFind;
            AccessController.doPrivileged(new PrivilegedAction() {
                public Object run() {
                    readConfiguration(classLoaderParam);
                    return null;
                }
            });
            info = (ClassLoaderLogInfo) this.classLoaderLoggers.get(clToFind);
        }
        return info;
    }

    protected void readConfiguration(final ClassLoader classLoader) {
        ClassLoader toConfigure = null;
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        // Decide which classLoader has to be configured
        if (classLoader == null) {
            toConfigure = systemClassLoader;
        } else {
            toConfigure = classLoader;
        }

        boolean enableSystemLog = false;        
        if (toConfigure == systemClassLoader) {
            enableSystemLog = true;
            this.myLogger = null;
        }

        LoggerFactory loggerFactory = getLoggerFactory(classLoader);
        ExtendedLogger localRootLogger = loggerFactory.makeNewLoggerInstance("", null);
        // Why localRootLogger.setUseParentHandlers(false) ?
        // Case a: The configuration is not inherited
        // The localRootLogger will have its own config file - the user is able to override this
        // Case b: The configuration is inherited
        // The localRootLogger will have the same handlers/filters as its parent and we do not
        // want to log everything twice
        localRootLogger.setUseParentHandlers(false);
        setParentRootLogger(toConfigure, localRootLogger);

        Hierarchy hierarchy = new Hierarchy(localRootLogger);
        hierarchy.setLoggerFactory(loggerFactory);
        hierarchy.setName("ClassLoader:" + toConfigure.hashCode());
        hierarchy.setInherited(false);

        LoggerRepository parentLoggerRepository = null;
        StreamConfiguratorPair scPair = determineStreamAndConfigurator(toConfigure);
        if (scPair.inputStream == null && toConfigure != systemClassLoader) {
            ClassLoader nonInherited = findFirstNonInheritedInHierarchy(toConfigure);
            scPair = determineStreamAndConfigurator(nonInherited);
            parentLoggerRepository = getClassLoaderInfo(nonInherited).repository;
            hierarchy.setInherited(true);
        }

        boolean configuredProperly = runConfigurator(scPair.configurator, scPair.inputStream,
                hierarchy, parentLoggerRepository);

        LoggerRepository repository = hierarchy;
        if (!configuredProperly && toConfigure == systemClassLoader) {
            System.err
                    .println("The system was not configured properly. Fallback configuration activated");
            repository = failSafeSystemConfig();
        }

        ClassLoaderLogInfo info = new ClassLoaderLogInfo(repository);
        this.classLoaderLoggers.put(classLoader, info);
        if (enableSystemLog) {
            this.internalLogPossible = true;
        }
    }

    protected final LoggerRepository failSafeSystemConfig() {
        LoggerFactory loggerFactory = new DefaultJDKLoggerFactory();
        ExtendedLogger localRootLogger = loggerFactory.makeNewLoggerInstance("", null);
        localRootLogger.setUseParentHandlers(false);
        localRootLogger.setLevel(Level.INFO);
        Hierarchy hierarchy = new Hierarchy(localRootLogger);
        hierarchy.setLoggerFactory(loggerFactory);
        hierarchy.setName("SYSTEM-HIERARCHY");

        PatternFormatter patternFormatter = new PatternFormatter(
                PatternFormatter.TTCC_CONVERSION_PATTERN);
        patternFormatter.setLoggerRepository(hierarchy);
        patternFormatter.activateOptions();

        ConsoleHandler consoleHandler = new ConsoleHandler("SYSTEM-CONSOLE");
        consoleHandler.setLoggerRepository(hierarchy);
        consoleHandler.setTarget("System.out");
        consoleHandler.setFollow(false);
        consoleHandler.setImmediateFlush(true);
        consoleHandler.setFormatter((ExtendedFormatter) patternFormatter);
        consoleHandler.activateOptions();

        return hierarchy;
    }

    /**
     * Determines and sets the parent logger of the root of an LoggerRepository.
     * 
     * @param classLoader where the childRoot belongs to, not allowed to be null.
     * @param childRoot to determine the parent root logger of, not allowed to be null.
     */
    protected void setParentRootLogger(final ClassLoader classLoader, final ExtendedLogger childRoot) {
        if (classLoader == null) {
            throw new LogIllegalStateException("Cannot determine Parent with classLoader null");
        }
        if (childRoot == null) {
            throw new LogIllegalStateException("Cannot call setParent on null");
        }
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        if (classLoader == systemClassLoader) {
            // Nothing todo. The SystemClassLoader contains the top root logger of all
            // LoggerRepositories
            return;
        }
        ClassLoader parent = classLoader.getParent();
        LoggerRepository parentRepository = null;
        if (parent == null) {
            parentRepository = getClassLoaderInfo(systemClassLoader).repository;
        } else {
            parentRepository = getClassLoaderInfo(parent).repository;
        }
        childRoot.setParent((Logger) parentRepository.getRootLogger());
    }

    /**
     * @param classLoader
     * @return
     */
    protected StreamConfiguratorPair determineStreamAndConfigurator(final ClassLoader classLoader) {
        InputStream inputstream = null;
        String configFile = null;
        if (classLoader == ClassLoader.getSystemClassLoader()) {
            String cfproperty = System.getProperty(Constants.CONFIG_FILE_PROPERTY);
            if (cfproperty != null) {
                cfproperty = replace(cfproperty);
                try {
                    inputstream = new FileInputStream(cfproperty);
                    configFile = cfproperty;
                } catch (FileNotFoundException e) {
                    System.err.println("Error: File[" + cfproperty + "] of["
                            + Constants.CONFIG_FILE_PROPERTY + "] not found.");
                    e.printStackTrace();
                }
            }
            if (inputstream == null) {
                inputstream = classLoader.getResourceAsStream(Constants.CONFIG_FILE_XML);
                configFile = Constants.CONFIG_FILE_XML;
            }
            if (inputstream == null) {
                inputstream = classLoader.getResourceAsStream(Constants.CONFIG_FILE_PROPERTIES);
                configFile = Constants.CONFIG_FILE_PROPERTIES;
            }
            if (inputstream == null) {
                inputstream = classLoader.getResourceAsStream(Constants.CONFIG_FILE_DEFAULT_XML);
                configFile = Constants.CONFIG_FILE_DEFAULT_XML;
            }
        } else if (classLoader instanceof URLClassLoader) {
            printClassLoaderHierarchy(classLoader);
            URLClassLoader cl = (URLClassLoader) classLoader;
            if (cl.findResource(Constants.CONFIG_FILE_XML) != null) {
                inputstream = cl.getResourceAsStream(Constants.CONFIG_FILE_XML);
                configFile = Constants.CONFIG_FILE_XML;
            } else if (cl.findResource(Constants.CONFIG_FILE_PROPERTIES) != null) {
                inputstream = cl.getResourceAsStream(Constants.CONFIG_FILE_PROPERTIES);
                configFile = Constants.CONFIG_FILE_PROPERTIES;
            }
        }
        if ((inputstream != null) && (configFile != null)) {
            Configurator configurator = determineConfigurator(configFile);
            getMyLogger().log(
                    Level.FINE,
                    "Returning StreamConfiguratorPair for [" + classLoader.hashCode() + "] with["
                            + configFile + "]");
            return new StreamConfiguratorPair(inputstream, configurator);
        }
        getMyLogger().log(Level.FINER,
                "Returning StreamConfiguratorPair null null for [" + classLoader.hashCode() + "]");
        return new StreamConfiguratorPair(null, null);
    }

    /**
     * @param childClassLoader
     * @return
     */
    protected ClassLoader findFirstNonInheritedInHierarchy(final ClassLoader childClassLoader) {
        if (childClassLoader == null) {
            throw new LogIllegalStateException("Null cannot be looked up.");
        }
        if (childClassLoader == ClassLoader.getSystemClassLoader()) {
            throw new LogIllegalStateException("SystemClassLoader cannot inherit from a parent");
        }
        ClassLoader current = childClassLoader;
        ClassLoaderLogInfo loginfo = null;
        boolean found = false;
        while (current != null && found == false) {
            if (existsLogInfo(current)) {
                loginfo = getClassLoaderInfo(current);
                // We are looking for the first ClassLoader which is not inherited
                // So found is !isInherited
                found = !loginfo.repository.isInherited();
                if (!found) {
                    getMyLogger().log(Level.FINER,
                            "Repository of [" + current.hashCode() + "] is inherited");
                    current = current.getParent();
                } else {
                    getMyLogger().log(
                            Level.FINER,
                            "Found is[" + current.hashCode() + "] for["
                                    + childClassLoader.hashCode() + "]");
                }
            } else {
                current = current.getParent();
            }
        }
        if (!found || current == null) {
            getMyLogger().log(Level.FINER, "Not found, returning SystemClassLoader");
            current = ClassLoader.getSystemClassLoader();
        }
        return current;
    }

    /**
     * @param configurator
     * @param stream
     * @param repository
     * @param parentLoggerRepository
     * @return true on configuration without exception, on any exception false
     */
    protected synchronized boolean runConfigurator(final Configurator configurator,
            final InputStream stream, final LoggerRepository repository,
            final LoggerRepository parentLoggerRepository) {
        getMyLogger().log(Level.FINER, "Running Configurator on[{0}] with parent[{1}]",
                new Object[] { repository, parentLoggerRepository });
        try {
            configurator.doConfigure(stream, repository, parentLoggerRepository);
        } catch (Exception e) {
            String errmess = "Exception during configuration of Repository[" + repository.getName() + "]";
            if (internalLogPossible)
                getMyLogger().log(Level.SEVERE, errmess, e);
            else {
                System.err.println(errmess);
                e.printStackTrace();
            }
            return false;
        } finally {
            IOUtil.closeInputStream(stream);
            if(configurator instanceof AbstractConfigurator) {
                ((AbstractConfigurator)configurator).dumpErrors();
            }
        }
        getMyLogger().log(Level.FINER, "Finished Configurator on[{0}] with parent[{1}]",
                new Object[] { repository, parentLoggerRepository });
        return true;
    }

    /**
     * Reads the identifier of the ClassLoader. If no identifier properties are specified
     * <code>Constants.CLASSLOADER_SYSTEM_ID</code> is used. For
     * <code>ClassLoader.getSystemClassLoader()</code> the
     * <code>Constants.CLASSLOADER_SYSTEM_ID</code> is used.
     * 
     * @param classLoader to read identifier from.
     * @since 0.7
     * @see ContextFactory#getThreadContextClassLoaderMapper()
     */
    protected void readClassLoaderId(final ClassLoader classLoader) {
        if (classLoader == null) {
            return;
        }
        final TCCLMapper mapper = ContextFactory.getThreadContextClassLoaderMapper();
        final int clHashCode = classLoader.hashCode();
        if (classLoader == ClassLoader.getSystemClassLoader()) {
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
            final String identifier = idProps.getProperty(Constants.CLASSLOADER_ID_PROPERTY,
                    Constants.CLASSLOADER_DEFAULT_ID);
            mapper.addClassLoaderId(clHashCode, identifier);
        }
    }

    /**
     * Check whether there is already a ClassInfo for a given Classloader or not.
     * 
     * @param classLoader to check.
     * @return true if there is an object in the map (not null).
     */
    protected boolean existsLogInfo(final ClassLoader classLoader) {
        if (this.classLoaderLoggers.get(classLoader) != null) {
            return true;
        }
        return false;
    }

    protected Configurator determineConfigurator(final String filename) {
        // TODO Logic for different Configurators
        return new JoranConfigurator();
    }

    protected Configurator getDefaultConfigurator(final ClassLoader classLoader) {
        String key = System.getProperty(Constants.CONFIGURATOR_PROPERTY);
        if (key != null) {
            String className = key.trim();
            ClassLoader clToLoad = classLoader;
            if (classLoader == null) {
                clToLoad = ClassLoader.getSystemClassLoader();
            }
            try {
                Class configuratorClass = clToLoad.loadClass(className);
                return (Configurator) configuratorClass.newInstance();
            } catch (ClassNotFoundException e) {
                // TODO Better warnings
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new JoranConfigurator();
    }

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

    protected final ExtendedLogger getMyLogger() {
        if (!internalLogPossible) {
            return NOPLogger.NOP_LOGGER;
        } else {
            ClassLoaderLogInfo info = getClassLoaderInfo(ClassLoader.getSystemClassLoader());
            this.myLogger = info.repository.getLogger(FQCNofLogManager);
        }
        return this.myLogger;
    }

    private static boolean runonce = false;

    private static void printClassLoaderHierarchy(final ClassLoader classLoader) {
        if (classLoader == null || runonce) {
            return;
        }
        ClassLoader current = classLoader;
        StringBuffer buf = new StringBuffer();
        while (current != null) {
            if (current != ClassLoader.getSystemClassLoader()) {
                buf.append("CL[");
                buf.append(current.hashCode());
                buf.append("],");
            } else {
                buf.append("SYSCL[");
                buf.append(current.hashCode());
                buf.append("],");
            }
            current = current.getParent();
        }
        runonce = true;
        System.out.println(buf.toString());
    }

    /**
     * Retrieves the actual LoggerFactory.
     * 
     * @param classLoader to lookup for the factory.
     * @return the loggerFactory to use for the LoggerRepository.
     * @since 0.7
     */
    protected abstract LoggerFactory getLoggerFactory(ClassLoader classLoader);

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
            if (l == null) {
                return;
            }
            PropertyChangeListenerWrapper wrapper = new PropertyChangeListenerWrapper(l);
            repository.addLoggerRepositoryEventListener(wrapper);
            this.listenerWrapper.put(l, wrapper);
        }

        void removePropertyChangeListener(PropertyChangeListener l) {
            if (l == null) {
                return;
            }
            PropertyChangeListenerWrapper wrapper = (PropertyChangeListenerWrapper) this.listenerWrapper
                    .get(l);
            if (wrapper == null) {
                return;
            }
            repository.removeLoggerRepositoryEventListener(wrapper);
            this.listenerWrapper.remove(l);
        }

    }

    /**
     * A StreamConfiguratorPair puts together a InputStream with the config file and a corresponding
     * Configurator to use.
     * 
     * @author Boris Unckel
     * @since 0.7
     */
    protected static final class StreamConfiguratorPair {
        /**
         * InputStream to read from.
         */
        public final InputStream inputStream;

        /**
         * Configurator to use with inputStream.
         */
        public final Configurator configurator;

        /**
         * Constructor for corresponding pair.
         * 
         * @param is to read from.
         * @param config to use with InputStream.
         */
        public StreamConfiguratorPair(InputStream is, Configurator config) {
            this.inputStream = is;
            this.configurator = config;
        }
    }

}

// EOF ClassLoaderLogManager.java
