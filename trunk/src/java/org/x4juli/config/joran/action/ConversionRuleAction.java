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
import java.util.Map;
import java.util.logging.Level;

import org.x4juli.config.joran.spi.ActionException;
import org.x4juli.config.joran.spi.ExecutionContext;
import org.x4juli.formatter.PatternFormatter;
import org.x4juli.formatter.pattern.PatternConverter;
import org.x4juli.global.helper.Option;
import org.x4juli.global.spi.ErrorItem;
import org.x4juli.global.spi.ExtendedFormatter;
import org.x4juli.global.spi.LoggerRepository;
import org.xml.sax.Attributes;

/**
 * Action for Component {@link PatternConverter}.
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
 * 
 * @author Boris Unckel
 * @since 0.7
 */
public class ConversionRuleAction extends AbstractAction {
    ExtendedFormatter formatter;

    /**
     * @param inherited tells the action to skip due to inherited config or not.
     */
    public ConversionRuleAction(boolean inherited) {
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
        String conversionWord = attributes.getValue(ActionConst.CONVERSION_WORD_ATTRIBUTE);
        String converterClass = attributes.getValue(ActionConst.CONVERTER_CLASS_ATTRIBUTE);

        if (Option.isEmpty(conversionWord)) {
            inError = true;
            errorMsg = "No 'conversionWord' attribute in <conversionRule>";
            getLogger().warning(errorMsg);
            ec.addError(new ErrorItem(errorMsg));

            return;
        }

        if (Option.isEmpty(converterClass)) {
            inError = true;
            errorMsg = "No 'converterClass' attribute in <conversionRule>";
            getLogger().warning(errorMsg);
            ec.addError(new ErrorItem(errorMsg));

            return;
        }

        try {
            getLogger().log(Level.FINER, "About to add conversion rule [{0}, {1}] to formatter",
                    new Object[] { conversionWord, converterClass });

            LoggerRepository repos = (LoggerRepository) ec.getObjectStack().get(0);

            Map ruleRegistry = (Map) repos.getObject(PatternFormatter.PATTERN_RULE_REGISTRY);
            if (ruleRegistry == null) {
                ruleRegistry = new HashMap();
                repos.putObject(PatternFormatter.PATTERN_RULE_REGISTRY, ruleRegistry);
            }
            // put the new rule into the rule registry
            ruleRegistry.put(conversionWord, converterClass);

        } catch (Exception oops) {
            inError = true;
            errorMsg = "Could not add conversion rule to PatternLayout.";
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
        // TODO Auto-generated method stub

    }

}

// EOF ConversionRuleAction.java
