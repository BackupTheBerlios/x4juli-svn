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
 * The LogManager to enable X4Juli.
 * <br/>
 * Configuration:
 * <br/>
 * 1) Put x4juli-x.y.jar on the system classpath, another classpath will not work.
 * (x.y means your current version)
 * <br/>
 * 2) Set parameter -Djava.util.logging.manager=org.x4juli.X4JuliLogManager
 * on the startup line (in your script, or corresponding config) for the JVM start.
 * See {@link java.util.logging.LogManager} for details.
 * <br/>
 * Through that you activate the extensions provided by x4juli.
 *
 * @author Boris Unckel
 * @since 0.5
 */
public class X4JuliLogManager extends ClassLoaderLogManager {

    // -------------------------------------------------------------- Variables

    // ------------------------------------------------------------ Constructor

    /**
     * 
     */
    public X4JuliLogManager() {
        super();
    }

    // --------------------------------------------------------- Public Methods

    /**
     * @see org.x4juli.ClassLoaderLogManager#getFQCNofLogger()
     * @since 0.5
     */
    public String getFQCNofLogger() {
        return "org.x4juli.X4JuliLogger";
    }

    /**
     * @see org.x4juli.ClassLoaderLogManager#getLoggerClass()
     * @since 0.5
     */
    public Class getLoggerClass() {
        return org.x4juli.X4JuliLogger.class;
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
            logger = new X4JuliLogger(name);
            addLogger(logger);
        }
        return logger;
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * {@inheritDoc}
     * 
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
    private static final class RootLogger extends X4JuliLogger {
        public RootLogger() {
            super("", null);
        }
    }

}

// EOF X4JuliLogManager.java
