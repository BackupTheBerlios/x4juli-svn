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
package org.x4juli;

import java.util.logging.Logger;

/**
 * Offers access to internal informations.
 * @since 0.5
 */
public class DiagnosticLogManager extends ClassLoaderLogManager {

    /**
     * 
     */
    public DiagnosticLogManager() {
        super();
    }

    /**
     * @see org.x4juli.ClassLoaderLogManager#getFQCNofLogger()
     * @since 0.5
     */
    public String getFQCNofLogger() {
        return "org.x4juli.DiagnosticLogger";
    }

    /**
     * @see org.x4juli.ClassLoaderLogManager#getLoggerClass()
     * @since 0.5
     */
    public Class getLoggerClass() {
        return org.x4juli.DiagnosticLogger.class;
    }

    /**
     * The method <code>getLogger</code> was overwritten. Different to the
     * super implementation it adds the Logger with
     * <code>addLogger(Logger)</code> to the repository. This was needed to
     * have compatible use with
     * <code>java.util.logging.Logger.getLogger(String name)</code>.
     * 
     * @see org.x4juli.ClassLoaderLogManager#getLogger(java.lang.String)
     * @since 0.5
     */
    public synchronized Logger getLogger(final String name) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Logger logger = (Logger) getClassLoaderInfo(classLoader).loggers.get(name);
        if (logger == null) {
            logger = new DiagnosticLogger(name);
//            System.out.println("DiagnosticLogManager: adding["+name+"]");
//            System.out.println("DiagnosticLogManager: Printing LogNodeTree");
//            printLogNodeTree();
//            System.out.println("DiagnosticLogManager: Printing LogNodeTree for["+name+"] ended");
            addLogger(logger);
        }
        return logger;
    }

    public synchronized void printLogNodeTree() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        LogNode node = getClassLoaderInfo(classLoader).rootNode;
        node.printTree(null, node);
    }
 
    /**
     * {@inheritDoc}
     * @since 0.5
     */
    protected Logger createRootLogger() {
        return new RootLogger();
    }

    // -------------------------------------------------------- Private Methods

    // ------------------------------------------------- RootLogger Inner Class

    /**
     * This class is needed to instantiate the root of each per classloader
     * hierarchy.
     */
    private class RootLogger extends DiagnosticLogger {
        public RootLogger() {
            super("", null);
        }
    }

}

// EOF DiagnosticLogManager.java
