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

package org.x4juli;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.WeakHashMap;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.x4juli.global.LoggerClassInformation;
import org.x4juli.global.LoggerRepositoryHolder;
import org.x4juli.global.spi.ObjectStore;
import org.x4juli.global.spi.ObjectStoreImpl;

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
public class ClassLoaderLogManager extends LogManager implements LoggerRepositoryHolder,
        LoggerClassInformation {

    // -------------------------------------------------------------- Variables

    /**
     * Map containing the classloader information, keyed per classloader. A weak
     * hashmap is used to ensure no classloader reference is leaked from
     * application redeployment.
     */
    protected final Map classLoaderLoggers = new WeakHashMap();

    /**
     * This prefix is used to allow using prefixes for the properties names of
     * handlers and their subcomponents.
     */
    protected ThreadLocal prefix = new ThreadLocal();

    /**
     * The ObjectStore is added to have an single, global ObjectStoreImpl. Later
     * it might be a full implementation with throwing events on changes...
     */
    protected ObjectStore loggerRepository = new ObjectStoreImpl();

    // ------------------------------------------------------------ Constructor

    /**
     * Default Constructor.
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
     */
    public synchronized boolean addLogger(final Logger logger) {

        final String loggerName = logger.getName();

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ClassLoaderLogInfo info = getClassLoaderInfo(classLoader);
        if (info.loggers.containsKey(loggerName)) {
            return false;
        }
        info.loggers.put(loggerName, logger);

        // Apply initial level for new logger
        final String levelString = getProperty(loggerName + ".level");
        if (levelString != null) {
            try {
                AccessController.doPrivileged(new PrivilegedAction() {
                    public Object run() {
                        logger.setLevel(Level.parse(levelString.trim()));
                        return null;
                    }
                });
            } catch (IllegalArgumentException e) {
                // Leave level set to null
                // Ignore Exception, to recognize it, use IDE Debugger Breakpoint in NOPLogger
                NOPLogger.NOP_LOGGER.log(Level.FINEST, "Ignored exception", e);
            }
        }

        // If any parent loggers have levels definied, make sure they are
        // instantiated
        int dotIndex = loggerName.lastIndexOf('.');
        while (dotIndex >= 0) {
            final String parentName = loggerName.substring(0, dotIndex);
            if (getProperty(parentName + ".level") != null) {
                Logger.getLogger(parentName);
                break;
            }
            dotIndex = loggerName.lastIndexOf('.', dotIndex - 1);
        }

        // Find associated node
        LogNode node = info.rootNode.findNode(loggerName);
        node.logger = logger;

        // Set parent logger
        Logger parentLogger = node.findParentLogger();
        if (parentLogger != null) {
            doSetParentLogger(logger, parentLogger);
        }

        // Tell children we are their new parent
        node.setParentLogger(logger);

        // Add associated handlers, if any are defined using the .handlers
        // property.
        // In this case, handlers of the parent logger(s) will not be used
        String handlers = getProperty(loggerName + ".handlers");
        if (handlers != null) {
            logger.setUseParentHandlers(false);
            StringTokenizer tok = new StringTokenizer(handlers, ",");
            while (tok.hasMoreTokens()) {
                String handlerName = (tok.nextToken().trim());
                Handler handler = null;
                ClassLoader current = classLoader;
                while (current != null) {
                    info = (ClassLoaderLogInfo) this.classLoaderLoggers.get(current);
                    if (info != null) {
                        handler = (Handler) info.handlers.get(handlerName);
                        if (handler != null) {
                            break;
                        }
                    }
                    current = current.getParent();
                }
                if (handler != null) {
                    logger.addHandler(handler);
                }
            }
        }

        // Parse useParentHandlers to set if the logger should delegate to its
        // parent.
        // Unlike java.util.logging, the default is to not delegate if a list of
        // handlers
        // has been specified for the logger.
        String useParentHandlersString = getProperty(loggerName + ".useParentHandlers");
        if (Boolean.valueOf(useParentHandlersString).booleanValue()) {
            logger.setUseParentHandlers(true);
        }

        return true;
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
    public synchronized Logger getLogger(final String name) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return (Logger) getClassLoaderInfo(classLoader).loggers.get(name);
    }

    /**
     * Get an enumeration of the logger names currently defined in the
     * classloader local configuration.
     *
     * @return all logger names corresponding to the classloder.
     */
    public synchronized Enumeration getLoggerNames() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return Collections.enumeration(getClassLoaderInfo(classLoader).loggers.keySet());
    }

    /**
     * Get the value of the specified property in the classloader local
     * configuration.
     *
     * @param name The property name
     * @return the property value or if not existing null.
     */
    public String getProperty(final String name) {
        String keyname = name;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String propPrefix = (String) this.prefix.get();
        if (propPrefix != null) {
            keyname = propPrefix + keyname;
        }
        ClassLoaderLogInfo info = getClassLoaderInfo(classLoader);
        String result = info.props.getProperty(keyname);
        // If the property was not found, and the current classloader had no
        // configuration (property list is empty), look for the parent
        // classloader
        // properties.
        if ((result == null) && (info.props.isEmpty())) {
            ClassLoader current = classLoader.getParent();
            while (current != null) {
                info = (ClassLoaderLogInfo) this.classLoaderLoggers.get(current);
                if (info != null) {
                    result = info.props.getProperty(keyname);
                    if ((result != null) || (!info.props.isEmpty())) {
                        break;
                    }
                }
                current = current.getParent();
            }
            if (result == null) {
                result = super.getProperty(keyname);
            }
        }
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
     */
    public ObjectStore getLoggerRepository() {
        return this.loggerRepository;
    }

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public String getFQCNofLogger() {
        return "java.util.logging.Logger";
    }

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public Class getLoggerClass() {
        return java.util.logging.Logger.class;
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

        InputStream is = null;
        // Special case for URL classloaders which are used in containers:
        // only look in the local repositories to avoid redefining loggers 20
        // times
        if ((classLoader instanceof URLClassLoader)
                && (((URLClassLoader) classLoader).findResource("logging.properties") != null)) {
            is = classLoader.getResourceAsStream("logging.properties");
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
            // Try the default JVM configuration
            if (is == null) {
                File defaultFile = new File(new File(System.getProperty("java.home"), "lib"),
                        "logging.properties");
                try {
                    is = new FileInputStream(defaultFile);
                } catch (IOException e) {
                    // Critical problem, do something ...
                    // Ignored Exception, to recognize it, use IDE Debugger Breakpoint in NOPLogger
                    NOPLogger.NOP_LOGGER.log(Level.FINEST, "Ignored exception", e);
                }
            }
        }

        Logger localRootLogger = createRootLogger();
        if (is == null) {
            // Retrieve the root logger of the parent classloader instead
            ClassLoader current = classLoader.getParent();
            ClassLoaderLogInfo info = null;
            while (current != null && info == null) {
                info = getClassLoaderInfo(current);
                current = current.getParent();
            }
            if (info != null) {
                localRootLogger.setParent(info.rootNode.logger);
            }
        }
        ClassLoaderLogInfo info = new ClassLoaderLogInfo(new LogNode(null, localRootLogger));
        info.loggers.put("", localRootLogger);
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

        try {
            info.props.load(is);
        } catch (IOException e) {
            // Report error
            System.err.println("Configuration error");
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (Throwable t) {
                // Ignored Exception, to recognize it, use IDE Debugger Breakpoint in NOPLogger
                NOPLogger.NOP_LOGGER.log(Level.FINEST, "Ignored exception", t);
            }
        }

        // Create handlers for the root logger of this classloader
        String rootHandlers = info.props.getProperty(".handlers");
        String handlers = info.props.getProperty("handlers");
        Logger localRootLogger = info.rootNode.logger;
        if (handlers != null) {
            StringTokenizer tok = new StringTokenizer(handlers, ",");
            while (tok.hasMoreTokens()) {
                final String handlerName = (tok.nextToken().trim());
                String handlerClassName = handlerName;
                String propPrefix = "";
                if (handlerClassName.length() <= 0) {
                    continue;
                }
                // Parse and remove a prefix (prefix start with a digit, such as
                // "10WebappFooHanlder.")
                if (Character.isDigit(handlerClassName.charAt(0))) {
                    int pos = handlerClassName.indexOf('.');
                    if (pos >= 0) {
                        propPrefix = handlerClassName.substring(0, pos + 1);
                        handlerClassName = handlerClassName.substring(pos + 1);
                    }
                }
                try {
                    this.prefix.set(propPrefix);
                    Handler handler = (Handler) classLoader.loadClass(handlerClassName)
                            .newInstance();
                    // The specification strongly implies all configuration
                    // should be done
                    // during the creation of the handler object.
                    // This includes setting level, filter, formatter and
                    // encoding.
                    this.prefix.set(null);
                    info.handlers.put(handlerName, handler);
                    if (rootHandlers == null) {
                        localRootLogger.addHandler(handler);
                    }
                } catch (Exception e) {
                    // Report error
                    System.err.println("Handler error");
                    e.printStackTrace();
                }
            }

            // Add handlers to the root logger, if any are defined using the
            // .handlers property.
            if (rootHandlers != null) {
                StringTokenizer tok2 = new StringTokenizer(rootHandlers, ",");
                while (tok2.hasMoreTokens()) {
                    String handlerName = (tok2.nextToken().trim());
                    Handler handler = (Handler) info.handlers.get(handlerName);
                    if (handler != null) {
                        localRootLogger.addHandler(handler);
                    }
                }
            }

        }
    }

    /**
     * Set parent child relationship between the two specified loggers.
     *
     * @param logger the child logger.
     * @param parent the new parent logger.
     */
    protected static void doSetParentLogger(final Logger logger, final Logger parent) {
        AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                logger.setParent(parent);
                return null;
            }
        });
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

    /**
     * Creates a new RootLogger. Intended to be overriden by subclasses with
     * different Loggers.
     *
     * @return one new root logger.
     */
    protected Logger createRootLogger() {
        return new RootLogger();
    }

    // ---------------------------------------------------- LogNode Inner Class

    /**
     * A LogNode represents one step in a namespace of Loggers.
     * In example org.x4juli.sample has three steps (org, juli, and sample).
     * LogNode represents one of them.
     * @author Boris Unckel
     * @since 0.6
     */
    protected static final class LogNode {
        Logger logger;

        final Map children = new HashMap();

        final LogNode parent;

        /**
         * Constructor.
         *
         * @param parent of the new LogNode.
         * @param logger contained logger.
         */
        LogNode(final LogNode parent, final Logger logger) {
            this.parent = parent;
            this.logger = logger;
        }

        /**
         * Constructor for LogNodes without Logger.
         *
         * @param parent of the new LogNode.
         */
        LogNode(final LogNode parent) {
            this(parent, null);
        }

        /**
         * Prints directly information about the current LogNode tree to
         * System.out.
         *
         * @param prefix for recursive printing.
         * @param node the node to start with.
         */
        void printTree(final String prefix, final LogNode node) {
            String printprefix = prefix;
            if (printprefix == null) {
                printprefix = "";
            } else {
                printprefix = printprefix + "-";
            }
            System.out.println(printprefix + " " + String.valueOf(node.logger));
            Map childMap = node.children;
            if (node.children == null || node.children.size() == 0) {
                return;
            }
            Collection childs = childMap.values();
            Iterator iter = childs.iterator();
            while (iter.hasNext()) {
                LogNode childNode = (LogNode) iter.next();
                printTree(printprefix, childNode);
            }
        }

        /**
         * Searches for a special logNode.
         *
         * @param name to lookup.
         * @return the logNode which is nearest to the name.
         */
        LogNode findNode(final String name) {
            String lookup = name;
            assert lookup != null && lookup.length() > 0;
            LogNode currentNode = this;
            while (lookup != null) {
                final int dotIndex = lookup.indexOf('.');
                final String nextName;
                if (dotIndex < 0) {
                    nextName = lookup;
                    lookup = null;
                } else {
                    nextName = lookup.substring(0, dotIndex);
                    lookup = lookup.substring(dotIndex + 1);
                }
                LogNode childNode = (LogNode) currentNode.children.get(nextName);
                if (childNode == null) {
                    childNode = new LogNode(currentNode);
                    currentNode.children.put(nextName, childNode);
                }
                currentNode = childNode;
            }
            return currentNode;
        }

        /**
         * Searches for the parent logger in the LogNode tree.
         *
         * @return the parent logger.
         */
        Logger findParentLogger() {
            Logger loggerToFind = null;
            LogNode node = this.parent;
            while (node != null && loggerToFind == null) {
                loggerToFind = node.logger;
                node = node.parent;
            }
            assert loggerToFind != null;
            return loggerToFind;
        }

        /**
         * Sets the parent where adaquate.
         *
         * @param parentLogger to set.
         */
        void setParentLogger(final Logger parentLogger) {
            for (final Iterator iter = this.children.values().iterator(); iter.hasNext();) {
                final LogNode childNode = (LogNode) iter.next();
                if (childNode.logger == null) {
                    childNode.setParentLogger(parentLogger);
                } else {
                    doSetParentLogger(childNode.logger, parentLogger);
                }
            }
        }

    }

    // -------------------------------------------- ClassLoaderInfo Inner Class

    /**
     * Repository for loggers, nodes, handlers, properties for each classloader.
     *
     * @since 0.5
     */
    protected static final class ClassLoaderLogInfo {
        final LogNode rootNode;

        /**
         * References to the loggers.
         */
        final Map loggers = new HashMap();

        /**
         * References to the handlers.
         */
        final Map handlers = new HashMap();

        /**
         * Config.
         */
        final Properties props = new Properties();

        /**
         * Constructor.
         *
         * @param rootNode of this classloader.
         */
        ClassLoaderLogInfo(final LogNode rootNode) {
            this.rootNode = rootNode;
        }

    }

    // ------------------------------------------------- RootLogger Inner Class

    /**
     * This class is needed to instantiate the root of each per classloader
     * hierarchy.
     */
    private static final class RootLogger extends Logger {
        public RootLogger() {
            super("", null);
        }
    }

}

// EOF ClassLoaderLogManager.java
