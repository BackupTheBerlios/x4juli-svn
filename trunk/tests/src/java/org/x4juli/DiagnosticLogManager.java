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

import org.x4juli.global.Constants;
import org.x4juli.global.spi.ClassLoaderLogManager;
import org.x4juli.global.spi.LoggerFactory;
import org.x4juli.global.spi.LoggerRepository;

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
     * @see org.x4juli.global.spi.ClassLoaderLogManager#getFQCNofLogger()
     * @since 0.5
     */
    public String getFQCNofLogger() {
        return Constants.FQCN_JUL_LOGGER;
    }

    /**
     * The method <code>getLogger</code> was overwritten. Different to the
     * super implementation it adds the Logger with
     * <code>addLogger(Logger)</code> to the repository. This was needed to
     * have compatible use with
     * <code>java.util.logging.Logger.getLogger(String name)</code>.
     * 
     * @see org.x4juli.global.spi.ClassLoaderLogManager#getLogger(java.lang.String)
     * @since 0.5
     */
    public synchronized Logger getLogger(final String name) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        LoggerRepository repository = getClassLoaderInfo(classLoader).repository;
        return (Logger) repository.getLogger(name);
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    protected LoggerFactory getLoggerFactory() {
        // TODO Auto-generated method stub
        return null;
    }

    public synchronized void printLogNodeTree() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        //TODO Logik wiederherstellen
        //Configurator configurator = createConfigurator();
        //LogNode node = configurator.getClassLoaderInfo(classLoader).rootNode;
        //node.printTree(null, node);
    }
 
    // -------------------------------------------------------- Private Methods

}

// EOF DiagnosticLogManager.java
