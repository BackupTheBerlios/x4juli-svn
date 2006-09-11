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

import org.x4juli.config.joran.spi.ActionException;
import org.x4juli.config.joran.spi.ExecutionContext;
import org.x4juli.global.spi.ExtendedLogger;
import org.x4juli.global.spi.LoggerRepository;
import org.xml.sax.Attributes;

/**
 * Missing documentation.
 * 
 * @todo Missing documentation.
 * @author Boris Unckel
 * @since 0.7
 */
public class RootLoggerAction extends AbstractAction {
    ExtendedLogger root;

    /**
     * @param inherited tells the action to skip due to inherited config or not.
     */
    public RootLoggerAction(boolean inherited) {
        super(inherited);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void begin(ExecutionContext ec, String name, Attributes attributes)
            throws ActionException {
        inError = false;

        LoggerRepository rep = (LoggerRepository) ec.getObject(0);
        root = rep.getRootLogger();

        getLogger().finer("Pushing root logger on stack");
        ec.pushObject(root);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void end(ExecutionContext ec, String name) throws ActionException {
        if (inError) {
            return;
        }

        Object o = ec.peekObject();

        if (o != root) {
            getLogger().warning("The object on the top the of the stack is not the root logger");
            getLogger().log(Level.WARNING, "It is [{0}] ", o);
        } else {
            getLogger().finer("Removing root logger from top of stack.");
            ec.popObject();
        }
    }

}

// EOF RootLoggerAction.java
