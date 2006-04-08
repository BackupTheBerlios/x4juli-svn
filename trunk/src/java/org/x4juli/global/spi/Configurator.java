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

import java.io.InputStream;
import java.net.URL;

/**
 * Implemented by classes capable of configuring x4juli using a URL.
 * <p>
 * Logging API as a whole was originally done for <a href="http://logging.apache.org/log4j/">Apache
 * log4j</a>. <b>x4uli</b> is a port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging APIs</a>. All
 * credits for initial idea, design, implementation, documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This file was originally
 * published by <i>Anders Kristensen</i>. Please use exclusively the <i>appropriate</i> mailing
 * lists for questions, remarks and contribution.
 * </p>
 * 
 * @author Boris Unckel
 * @since 0.7
 */
public interface Configurator {
    /**
     * Special level value signifying inherited behaviour. The current value of this string constant
     * is <b>inherited</b>. {@link #NULL} is a synonym.
     */
    public static final String INHERITED = "inherited";

    /**
     * Special level signifying inherited behaviour, same as {@link #INHERITED}. The current value
     * of this string constant is <b>null</b>.
     */
    public static final String NULL = "null";

    /**
     * Interpret a resource pointed by a URL and set up x4juli accordingly.
     *
     * The configuration is done relative to the <code>hierarchy</code> parameter.
     *
     * @since 0.7
     * @param url The URL to parse
     * @param repository The repository to operate upon.
     */
    void doConfigure(URL url, LoggerRepository repository);

    /**
     * Use an InputStream as a source for configuration and set up x4juli accordingly.
     * 
     * The configuration is done relative to the <code>hierarchy</code> parameter.
     * 
     * @since 0.7
     * 
     * @param stream The input stream to use for configuration data.
     * @param repository The repository to operate upon.
     */
    void doConfigure(InputStream stream, LoggerRepository repository);

}

// EOF Configurator.java
