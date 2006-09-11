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

import java.util.logging.Level;

import org.x4juli.config.PropertySetter;
import org.x4juli.config.joran.spi.ActionException;
import org.x4juli.config.joran.spi.ExecutionContext;
import org.x4juli.global.spi.ErrorItem;
import org.x4juli.global.spi.LoggerRepository;
import org.xml.sax.Attributes;

/**
 * Missing documentation.
 * <p>
 * Logging API as a whole was originally done for <a href="http://logging.apache.org/log4j/">Apache
 * log4j</a>. <b>x4uli</b> is a port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging APIs</a>. All
 * credits for initial idea, design, implementation, documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This file was originally
 * published by <i>unknown</i>. Please use exclusively the <i>appropriate</i> mailing lists for
 * questions, remarks and contribution.
 * </p>
 * 
 * @todo Missing documentation.
 * @author Boris Unckel
 * @since 0.7
 */
public class ParamAction extends AbstractAction {
    private static final String NO_NAME = "No name attribute in <param> element";

    private static final String NO_VALUE = "No value attribute in <param> element";

    /**
     * @param inherited tells the action to skip due to inherited config or not.
     */
    public ParamAction(boolean inherited) {
        super(inherited);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void begin(final ExecutionContext ec, final String name, final Attributes attributes)
            throws ActionException {
        if(isInheritedMode()) {
            return;
        }
        String lname = attributes.getValue(NAME_ATTRIBUTE);
        String value = attributes.getValue(VALUE_ATTRIBUTE);

        if (lname == null) {
            inError = true;
            getLogger().severe(NO_NAME);
            ec.addError(new ErrorItem(NO_NAME));
            return;
        }

        if (value == null) {
            inError = true;
            getLogger().severe(NO_VALUE);
            ec.addError(new ErrorItem(NO_VALUE));
            return;
        }

        // remove both leading and trailing spaces
        value = value.trim();
        
        LoggerRepository repo = (LoggerRepository) ec.getObjectStack().get(0);
        
        Object o = ec.peekObject();
        PropertySetter propSetter = new PropertySetter(o);
        propSetter.setLoggerRepository(repo);
        value = ec.subst(value);

        // allow for variable substitution for name as well
        lname = ec.subst(lname);

        getLogger().log(Level.FINER, "In ParamAction setting parameter [{0}] to value [{1}].",
                new Object[] { lname, value });
        propSetter.setProperty(lname, value);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void end(final ExecutionContext ec, final String name) throws ActionException {
        // TODO Auto-generated method stub

    }

}

// EOF ParamAction.java
