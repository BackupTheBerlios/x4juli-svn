/*
 * Copyright 2006 x4juli.org.
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
package org.x4juli.jcl;

import org.x4juli.global.spi.ExtendedLogger;
import org.x4juli.global.spi.LoggerFactory;

/**
 * Factory for JCLLogger inside x4juli. Not for use with jcl itself.
 * @author Boris Unckel
 * @since 0.7
 */
public class JCLLoggerFactory implements LoggerFactory {

    /**
     * 
     */
    public JCLLoggerFactory() {
        super();
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public ExtendedLogger makeNewLoggerInstance(final String name, final String resourcebundleName) {
        return new JCLLogger(name, resourcebundleName);
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public String getFQCNofLogger() {
        return "org.x4juli.jcl.JCLLogger";
    }

}

// EOF JCLLoggerFactory.java
