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
package org.x4juli.global.helper;

import java.net.URL;
import java.util.logging.Level;

import org.x4juli.logger.NOPLogger;

/**
 * Load resources (or images) from various sources.
 * <p>
 * Logging API as a whole was originally done for <a
 * href="http://logging.apache.org/log4j/">Apache log4j</a>. <b>Juli</b> is a
 * port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging
 * APIs</a>. All credits for initial idea, design, implementation,
 * documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This
 * file was originally published by <i>Ceki G&uuml;lc&uuml;</i>. Please use
 * exclusively the <i>appropriate</i> mailing lists for questions, remarks and
 * contribution.
 * </p>
 *
 * @author Boris Unckel
 * @since 0.5
 *
 */
public final class Loader {

    // -------------------------------------------------------------- Variables

    // ------------------------------------------------------------ Constructor

    /**
     * No instanciation wanted.
     */
    private Loader() {
        //NOP
    }

    // --------------------------------------------------------- Public Methods

    /**
     * This method will search for <code>resource</code> in different places.
     * The rearch order is as follows:
     *
     * <ol>
     *
     * <p>
     * <li>Search for <code>resource</code> using the thread context class
     * loader under Java2. This step is performed only if the <code> skipTCL</code>
     * parameter is false.</li>
     *
     * <p>
     * <li>If the aboved step failed, search for <code>resource</code> using
     * the class loader that loaded this class (<code>Loader</code>).</li>
     *
     * <p>
     * <li>Try one last time with
     * <code>ClassLoader.getSystemResource(resource)</code>, that is is using
     * the system class loader in JDK 1.2 and virtual machine's built-in class
     * loader in JDK 1.1.
     *
     * </ol>
     * @param resource to find.
     * @return the url of the resource.
     */
    public static URL getResource(final String resource) {
        ClassLoader classLoader = null;
        URL url = null;

        try {
            classLoader = getTCL();

            if (classLoader != null) {
                url = classLoader.getResource(resource);

                if (url != null) {
                    return url;
                }
            }

            // We could not find resource. Ler us now try with the
            // classloader that loaded this class.
            classLoader = Loader.class.getClassLoader();

            if (classLoader != null) {
                url = classLoader.getResource(resource);

                if (url != null) {
                    return url;
                }
            }
        } catch (Throwable t) {
            // Ignored Exception, to recognize it, use IDE Debugger Breakpoint in NOPLogger
            NOPLogger.NOP_LOGGER.log(Level.FINEST,"Ignored exception",t);
        }

        // Last ditch attempt: get the resource from the class path. It
        // may be the case that clazz was loaded by the Extentsion class
        // loader which the parent of the system class loader. Hence the
        // code below.
        return ClassLoader.getSystemResource(resource);
    }

    /**
     * If running under JDK 1.2 load the specified class using the
     * <code>Thread</code> <code>contextClassLoader</code> if that fails try
     * Class.forname. Under JDK 1.1 only Class.forName is used.
     *
     * @param clazz to load.
     * @return the class object.
     * @throws ClassNotFoundException if class was not found in the contextClassLoader.
     */
    public static Class loadClass(final String clazz) throws ClassNotFoundException {
        try {
            return getTCL().loadClass(clazz);
        } catch (Throwable e) {
            // we reached here because tcl was null or because of a
            // security exception, or because clazz could not be loaded...
            // In any case we now try one more time
            return Class.forName(clazz);

        }
    }

    // ------------------------------------------------------ Protected Methods

    // -------------------------------------------------------- Private Methods

    /**
     * Get the Thread Context Loader which is a JDK 1.2 feature. If we are
     * running under JDK 1.1 or anything else goes wrong the method returns
     * <code>null</code>.
     * @return the classloader of the current Thread.
     */
    private static ClassLoader getTCL() {
        return Thread.currentThread().getContextClassLoader();
    }

}
