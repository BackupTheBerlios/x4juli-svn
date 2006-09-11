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

import java.lang.reflect.Method;
import java.util.logging.Level;

import org.x4juli.config.joran.spi.ActionException;
import org.x4juli.config.joran.spi.ExecutionContext;
import org.x4juli.global.helper.Loader;
import org.x4juli.global.helper.Option;
import org.x4juli.global.helper.OptionConverter;
import org.x4juli.global.spi.ErrorItem;
import org.x4juli.global.spi.ExtendedLogger;
import org.x4juli.global.spi.LoggerRepository;
import org.xml.sax.Attributes;

/**
 * Missing documentation.
 * @todo Missing documentation.
 * @author Boris Unckel
 * @since 0.7
 */
public class LoggerAction extends AbstractAction {

    /**
     * @param inherited tells the action to skip due to inherited config or not.
     */
    public LoggerAction(boolean inherited) {
        super(inherited);
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void begin(ExecutionContext ec, String name, Attributes attributes)
            throws ActionException {
        // Let us forget about previous errors (in this object)
        inError = false;

        LoggerRepository repos = (LoggerRepository) ec.getObject(0);

        // Create a new org.apache.log4j.Category object from the <category> element.
        String loggerName = attributes.getValue(NAME_ATTRIBUTE);

        if (Option.isEmpty(loggerName)) {
          inError = true;

          String line =
            ", around line " + getLineNumber(ec) + " column "
            + getColumnNumber(ec);

          String errorMsg = "No 'name' attribute in element " + name + line;

          getLogger().warning(errorMsg);
          ec.addError(new ErrorItem(errorMsg));

          return;
        }

        getLogger().finer("Logger name is [" + loggerName + "].");

        ExtendedLogger l;

        String className = attributes.getValue(CLASS_ATTRIBUTE);

        if (Option.isEmpty(className)) {
          getLogger().finer("Retreiving an instance of org.x4juli.global.spi.ExtendedLogger.");
          l = repos.getLogger(loggerName);
        } else {
          getLogger().finer("Desired logger sub-class: [" + className + ']');

          try {
            Class clazz = Loader.loadClass(className);
            Method getInstanceMethod =
              clazz.getMethod("getLogger", ActionConst.ONE_STRING_PARAM);
            l = (ExtendedLogger) getInstanceMethod.invoke(
                null, new Object[] { loggerName });
          } catch (Exception oops) {
            getLogger().log(Level.SEVERE,
              "Could not retrieve category [" + loggerName
              + "]. Reported error follows.", oops);

            return;
          }
        }

        boolean additivity =
          OptionConverter.toBoolean(
            attributes.getValue(ActionConst.ADDITIVITY_ATTRIBUTE), true);
        if(getLogger().isLoggable(Level.FINER)) {
        getLogger().finer(
          "Setting [" + l.getName() + "] additivity to [" + additivity + "].");
        }
        l.setUseParentHandlers(additivity);

        getLogger().finer("Pushing logger named [" + loggerName + "].");
        ec.pushObject(l);
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void end(ExecutionContext ec, String name) throws ActionException {
        getLogger().finer("end() called.");

        if (!inError) {
          getLogger().finer("Removing logger from stack.");
          ec.popObject();
        }
    }

}

// EOF LoggerAction.java
