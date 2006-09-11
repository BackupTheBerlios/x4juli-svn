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
import org.x4juli.global.spi.ErrorItem;
import org.x4juli.global.spi.ExtendedHandler;
import org.x4juli.global.spi.ExtendedLogger;
import org.x4juli.global.spi.HandlerAttachable;
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
 * @author Boris Unckel
 * @since 0.7
 */
public class HandlerRefAction extends AbstractAction {


    /**
     *
     * @param inherited tells the action to skip due to inherited config or not.
     */
    public HandlerRefAction(boolean inherited) {
        super(inherited);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void begin(ExecutionContext ec, String name, Attributes attributes)
            throws ActionException {
        if(isInheritedMode()) {
            return;
        }
        // Let us forget about previous errors (in this object)
        inError = false;

        //logger.debug("begin called");

        Object o = ec.peekObject();

        if (!(o instanceof HandlerAttachable)) {
          String errMsg =
            "Could not find an HandlerAttachable at the top of execution stack. Near <"
            + name + "> line " + getLineNumber(ec);

          getLogger().warning(errMsg);
          inError = true;
          ec.addError(new ErrorItem(errMsg));

          return;
        }

        HandlerAttachable appenderAttachable = (HandlerAttachable) o;

        String appenderName = attributes.getValue(ActionConst.REF_ATTRIBUTE);

        if (Option.isEmpty(appenderName)) {
          // print a meaningful error message and return
          String errMsg = "Missing handler ref attribute in <appender-ref> tag.";

          getLogger().warning(errMsg);
          inError = true;
          ec.addError(new ErrorItem(errMsg));

          return;
        }

        HashMap handlerBag =
          (HashMap) ec.getObjectMap().get(ActionConst.HANDLER_BAG);
        ExtendedHandler handler = (ExtendedHandler) handlerBag.get(appenderName);

        if (handler == null) {
          String msg = "Could not find an handler named ["+appenderName+
          "]. Did you define it below in the config file?";
          getLogger().warning(msg);
          //TODO Korrektes Logging
          //getLogger().warn("See {}#appender_order for more details.", Constants.CODES_HREF);
          inError = true;
          ec.addError(new ErrorItem(msg));

          return;
        }

        if (appenderAttachable instanceof ExtendedLogger) {
          getLogger().log(Level.FINER,
            "Attaching handler named [{0}] to logger named [{1}].", new Object[]{appenderName, (
                (ExtendedLogger) appenderAttachable).getName()});
        } else {
          getLogger().log(Level.FINER,
            "Attaching appender named [{0}] to {1}.", new Object[]{appenderName, appenderAttachable});
        }

        appenderAttachable.addHandler(handler);


    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void end(ExecutionContext ec, String name) throws ActionException {
        // NOP
    }

}

// EOF HandlerRefAction.java
