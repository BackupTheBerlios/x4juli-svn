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
import org.x4juli.config.joran.spi.Pattern;
import org.x4juli.global.helper.Option;
import org.x4juli.global.spi.ErrorItem;
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
public class NewRuleAction extends AbstractAction {

    /**
     * @param inherited tells the action to skip due to inherited config or not.
     */
    public NewRuleAction(boolean inherited) {
        super(inherited);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void begin(ExecutionContext ec, String name, Attributes attributes)
            throws ActionException {
        // Let us forget about previous errors (in this object)
        inError = false;
        String errorMsg;
        String pattern = attributes.getValue(Action.PATTERN_ATTRIBUTE);
        String actionClass = attributes.getValue(Action.ACTION_CLASS_ATTRIBUTE);

        if (Option.isEmpty(pattern)) {
            inError = true;
            errorMsg = "No 'pattern' attribute in <newRule>";
            getLogger().warning(errorMsg);
            ec.addError(new ErrorItem(errorMsg));
            return;
        }

        if (Option.isEmpty(actionClass)) {
            inError = true;
            errorMsg = "No 'actionClass' attribute in <newRule>";
            getLogger().warning(errorMsg);
            ec.addError(new ErrorItem(errorMsg));
            return;
        }

        try {
            getLogger().log(Level.FINER, "About to add new Joran parsing rule [{0},{1}].",
                    new Object[] { pattern, actionClass });
            // getLogger().debug("About to add new Joran parsing rule
            // ["+pattern+","+actionClass+"].");
            ec.getJoranInterpreter().getRuleStore().addRule(new Pattern(pattern), actionClass);
        } catch (Exception oops) {
            inError = true;
            errorMsg = "Could not add new Joran parsing rule [" + pattern + "," + actionClass + "]";
            getLogger().log(Level.SEVERE, errorMsg, oops);
            ec.addError(new ErrorItem(errorMsg));
        }
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

// EOF NewRuleAction.java
