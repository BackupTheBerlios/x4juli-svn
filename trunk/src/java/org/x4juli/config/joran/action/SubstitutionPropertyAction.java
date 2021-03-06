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
package org.x4juli.config.joran.action;

import java.util.Properties;

import org.x4juli.config.joran.spi.ExecutionContext;

/**
 * This action sets new substitution properties for the execution context by name, value pair, or
 * adds all the properties passed in the {@link Properties} argument.
 * <p>
 * Logging API as a whole was originally done for <a href="http://logging.apache.org/log4j/">Apache
 * log4j</a>. <b>x4uli</b> is a port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging APIs</a>. All
 * credits for initial idea, design, implementation, documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This file was originally
 * published by <i>Ceki G&uuml;lc&uuml;</i>. Please use exclusively the <i>appropriate</i> mailing
 * lists for questions, remarks and contribution.
 * </p>
 * 
 * @author Boris Unckel
 * @since 0.7
 */
public class SubstitutionPropertyAction extends PropertyAction {

    /**
     * @param inherited tells the action to skip due to inherited config or not.
     */
    public SubstitutionPropertyAction(boolean inherited) {
        super(inherited);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void setProperties(ExecutionContext ec, Properties props) {
        ec.addProperties(props);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void setProperty(ExecutionContext ec, String key, String value) {
        ec.addProperty(key, value);
    }

}

// EOF SubstitutionPropertyAction.java
