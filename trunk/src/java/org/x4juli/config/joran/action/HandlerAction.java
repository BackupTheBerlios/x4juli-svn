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

import java.util.HashMap;
import java.util.logging.Level;

import org.x4juli.config.joran.spi.ActionException;
import org.x4juli.config.joran.spi.ExecutionContext;
import org.x4juli.global.helper.Option;
import org.x4juli.global.helper.OptionConverter;
import org.x4juli.global.spi.Component;
import org.x4juli.global.spi.ErrorItem;
import org.x4juli.global.spi.ExtendedHandler;
import org.x4juli.global.spi.LoggerRepository;
import org.x4juli.global.spi.OptionHandler;
import org.xml.sax.Attributes;

/**
 * Action for Component {@link ExtendedHandler}.
 * 
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
 * @author Boris Unckel
 * @since 0.7
 */
public class HandlerAction extends AbstractAction {

    private ExtendedHandler handler;

    /**
     * 
     */
    public HandlerAction() {
        super();
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void begin(ExecutionContext ec, String name, Attributes attributes)
            throws ActionException {
        String className = attributes.getValue(CLASS_ATTRIBUTE);

        // We are just beginning, reset variables
        handler = null;
        inError = false;

        try {
            getLogger().log(Level.FINER, "About to instantiate appender of type [{0}]", className);

            handler = (ExtendedHandler) OptionConverter.instantiateByClassName(className,
                    ExtendedHandler.class, null);

            LoggerRepository repo = (LoggerRepository) ec.getObjectStack().get(0);
            ((Component)handler).setLoggerRepository(repo);

            String handlerName = attributes.getValue(NAME_ATTRIBUTE);

            if (Option.isEmpty(handlerName)) {
                getLogger()
                        .warning("No handler name given for handler of type " + className + "].");
            } else {
                handler.setName(handlerName);
                getLogger().finer("Appender named as [" + handlerName + "]");
            }

            // The execution context contains a bag which contains the handler
            // created thus far.
            HashMap handlerBag = (HashMap) ec.getObjectMap().get(ActionConst.HANDLER_BAG);

            // add the handler just created to the appender bag.
            handlerBag.put(handlerName, handler);

            getLogger().finer("Pushing appender on to the object stack.");
            ec.pushObject(handler);
        } catch (Exception oops) {
            inError = true;
            String errmes = "Could not create an Appender. Reported error follows.";
            getLogger().log(Level.SEVERE, errmes, oops);
            ec.addError(new ErrorItem("Could not create appender of type " + className + "]."));
            throw new ActionException(errmes, ActionException.SKIP_CHILDREN, oops);
        }

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

        if (handler instanceof OptionHandler) {
            ((OptionHandler) handler).activateOptions();
        }

        Object o = ec.peekObject();

        if (o != handler) {
            getLogger().warning(
                    "The object at the of the stack is not the appender named ["
                            + handler.getName() + "] pushed earlier.");
        } else {
            getLogger().finer(
                    "Popping appender named [" + handler.getName() + "] from the object stack");
            ec.popObject();
        }
    }

}

// EOF HandlerAction.java
