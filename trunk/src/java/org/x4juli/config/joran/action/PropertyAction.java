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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

import org.x4juli.config.joran.spi.ActionException;
import org.x4juli.config.joran.spi.ExecutionContext;
import org.x4juli.global.helper.Option;
import org.x4juli.global.helper.OptionConverter;
import org.x4juli.global.spi.ErrorItem;
import org.xml.sax.Attributes;

/**
 * This class serves as a base for other actions, which similar to the ANT property task which
 * add/set properties of a given object.
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
public abstract class PropertyAction extends AbstractAction {

    static String INVALID_ATTRIBUTES = "In <property> element, either the \"file\" attribute or both the \"name\" and \"value\" attributes must be set.";

    public abstract void setProperties(ExecutionContext ec, Properties props);

    public abstract void setProperty(ExecutionContext ec, String key, String value);

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void begin(final ExecutionContext ec, final String name, final Attributes attributes)
            throws ActionException {
        String lname = attributes.getValue(NAME_ATTRIBUTE);
        String value = attributes.getValue(NAME_ATTRIBUTE);
        String fileName = attributes.getValue(FILE_ATTRIBUTE);

        if (!Option.isEmpty(fileName) && (Option.isEmpty(lname) && Option.isEmpty(value))) {
            Properties props = new Properties();

            try {
                FileInputStream istream = new FileInputStream(fileName);
                props.load(istream);
                istream.close();
                setProperties(ec, props);
            } catch (IOException e) {
                String errMsg = "Could not read properties file [" + fileName + "].";
                getLogger().log(Level.SEVERE, errMsg, e);
                ec.addError(new ErrorItem(INVALID_ATTRIBUTES, e));
                getLogger().severe("Ignoring configuration file [" + fileName + "].");

            }
        } else if (!(Option.isEmpty(lname) || Option.isEmpty(value)) && Option.isEmpty(fileName)) {
            value = OptionConverter.convertSpecialChars(value);
            // now remove both leading and trailing spaces
            value = value.trim();
            setProperty(ec, lname, value);
        } else {
            getLogger().severe(INVALID_ATTRIBUTES);
            ec.addError(new ErrorItem(INVALID_ATTRIBUTES));
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void end(final ExecutionContext ec, final String name) throws ActionException {
        // NOP

    }

}

// EOF PropertyAction.java
