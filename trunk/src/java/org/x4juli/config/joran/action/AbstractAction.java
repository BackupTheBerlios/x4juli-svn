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

import org.x4juli.config.joran.spi.ActionException;
import org.x4juli.config.joran.spi.ExecutionContext;
import org.x4juli.config.joran.spi.Interpreter;
import org.x4juli.global.spi.AbstractComponent;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

/**
 * Most of the work for configuring log4j is done by Actions.
 * 
 * Methods of an Action are invoked while an XML file is parsed through.
 * 
 * This class is largely copied from the relevant class in the commons-digester project of the
 * Apache Software Foundation.
 * <p>
 * Logging API as a whole was originally done for <a href="http://logging.apache.org/log4j/">Apache
 * log4j</a>. <b>x4uli</b> is a port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging APIs</a>. All
 * credits for initial idea, design, implementation, documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This file was originally
 * published by <i>Craig McClanahan, Christopher Lenz, Ceki G&uuml;lc&uuml;</i>. Please use
 * exclusively the <i>appropriate</i> mailing lists for questions, remarks and contribution.
 * </p>
 * 
 * @author Boris Unckel
 * @since 0.7
 */
public abstract class AbstractAction extends AbstractComponent implements Action {

    /**
     * Signaling an error during action.
     */
    protected boolean inError = false;
    
    private boolean inheritedMode = false;
    
    /**
     * Default, NOP Constructor.
     * @param inherited tells the action to skip due to inherited config or not.
     * @since 0.7
     */
    public AbstractAction(final boolean inherited) {
        super();
        this.inheritedMode = inherited;
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public String toString() {
        return this.getClass().getName();
    }

    protected int getColumnNumber(ExecutionContext ec) {
        Interpreter jp = ec.getJoranInterpreter();
        Locator locator = jp.getLocator();
        if (locator != null) {
            return locator.getColumnNumber();
        }
        return -1;
    }

    protected int getLineNumber(ExecutionContext ec) {
        Interpreter jp = ec.getJoranInterpreter();
        Locator locator = jp.getLocator();
        if (locator != null) {
            return locator.getLineNumber();
        }
        return -1;
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public boolean isInheritedMode() {
        return this.inheritedMode;
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void setInheritedMode(boolean inherited) {
        this.inheritedMode = inherited;
    }

}

// EOF AbstractAction.java
