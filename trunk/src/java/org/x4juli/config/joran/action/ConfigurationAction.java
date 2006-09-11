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

import java.util.List;
import java.util.logging.Level;

import org.x4juli.config.AbstractConfigurator;
import org.x4juli.config.joran.spi.ActionException;
import org.x4juli.config.joran.spi.ExecutionContext;
import org.x4juli.global.Constants;
import org.x4juli.global.spi.ExtendedLogger;
import org.x4juli.global.spi.LoggerRepository;
import org.xml.sax.Attributes;

/**
 * Missing documentation.
 * @todo Missing documentation.
 * @author Boris Unckel
 * @since 0.7
 */
public class ConfigurationAction extends AbstractAction {

    private static final String INTERNAL_DEBUG_ATTR = "debug";
    private boolean attachment = false;

    /**
     * @param inherited tells the action to skip due to inherited config or not.
     */
    public ConfigurationAction(boolean inherited) {
        super(inherited);
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void begin(final ExecutionContext ec, final String name, final Attributes attributes)
            throws ActionException {
        String debugAttrib = attributes.getValue(INTERNAL_DEBUG_ATTR);
        LoggerRepository repos = (LoggerRepository) ec.getObject(0);
        ExtendedLogger ll = repos.getLogger(Constants.X4JULI_PACKAGE_NAME);
        if (
          (debugAttrib == null) || debugAttrib.equals("")
            || debugAttrib.equals("false") || debugAttrib.equals("null")) {
          getLogger().finer("Ignoring " + INTERNAL_DEBUG_ATTR + " attribute.");
        } else {
          AbstractConfigurator.attachTemporaryConsoleAppender(repos);
          getLogger().finer("Starting internal logs on console.");
          attachment = true;
        }
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void end(final ExecutionContext ec, final String name) throws ActionException {
        getLogger().finer("Will stop writing internal logs on console.");
        if(!attachment){
            return;
        }
        LoggerRepository repos = (LoggerRepository) ec.getObject(0);
        List errorList = ec.getErrorList();
        AbstractConfigurator.detachTemporaryConsoleAppender(repos, errorList);
    }

}

// EOF ConfigurationAction.java
