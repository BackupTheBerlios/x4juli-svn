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

import org.x4juli.global.spi.ClassLoaderLogManager;
import org.x4juli.global.spi.LoggerFactory;
import org.x4juli.global.spi.LoggerRepository;
import org.x4juli.logger.DefaultJDKLoggerFactory;

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
     * {@inheritDoc}
     * @since 0.7
     */
    protected LoggerFactory getLoggerFactory(final ClassLoader classLoader) {
        boolean jclAvailable = false;
        boolean slf4jAvailable = false;
        LoggerFactory factory = new DefaultJDKLoggerFactory();
        try {
            classLoader.loadClass("org.apache.commons.logging.Log");
            jclAvailable = true;
        } catch (ClassNotFoundException e) {
            jclAvailable = false;
        }
        try {
            classLoader.loadClass("org.slf4j.Logger");
            slf4jAvailable = true;
        } catch (ClassNotFoundException e) {
            slf4jAvailable = false;
        }
        try {
            if(jclAvailable && slf4jAvailable) {
                Class x4juliFactoryClass = classLoader.loadClass("org.x4juli.logger.X4JuliLoggerFactory");
                factory = (LoggerFactory) x4juliFactoryClass.newInstance();
            }
            else if(jclAvailable) {
                Class jclFactoryClass = classLoader.loadClass("org.x4juli.jcl.JCLLoggerFactory");
                factory = (LoggerFactory) jclFactoryClass.newInstance();
            }
            else if(slf4jAvailable) {
                Class slf4jFactoryClass = classLoader.loadClass("org.x4juli.slf4j.Slf4jLoggerFactory");
                factory = (LoggerFactory) slf4jFactoryClass.newInstance();
            }
        } catch (Exception e) {
            //TODO sysout entfernen
            e.printStackTrace();
        } 
        return factory;
        
    }

    // ------------------------------------------------------ Protected Methods

    // -------------------------------------------------------- Private Methods

    // ------------------------------------------------- RootLogger Inner Class

}

// EOF X4JuliLogManager.java
