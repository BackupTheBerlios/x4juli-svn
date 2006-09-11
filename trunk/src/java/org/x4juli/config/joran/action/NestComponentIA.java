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

import java.util.Stack;
import java.util.logging.Level;

import org.x4juli.config.PropertySetter;
import org.x4juli.config.joran.spi.ActionException;
import org.x4juli.config.joran.spi.ExecutionContext;
import org.x4juli.config.joran.spi.Pattern;
import org.x4juli.global.helper.Loader;
import org.x4juli.global.helper.Option;
import org.x4juli.global.spi.Component;
import org.x4juli.global.spi.ErrorItem;
import org.x4juli.global.spi.OptionHandler;
import org.xml.sax.Attributes;

/**
 * ActionDataStack contains ActionData instances We use a stack of ActionData objects in order to
 * support nested elements which are handled by the same NestComponentIA instance. We push a
 * ActionData instance in the isApplicable method (if the action is applicable) and pop it in the
 * end() method. The XML well-formedness property will guarantee that a push will eventually be
 * followed by the corresponding pop.
 * 
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
public class NestComponentIA extends ImplicitAction {
    private Stack actionDataStack = new Stack();

    /**
     * @param inherited tells the action to skip due to inherited config or not.
     */
    public NestComponentIA(boolean inherited) {
        super(inherited);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public boolean isApplicable(final Pattern currentPattern, final Attributes attributes,
            final ExecutionContext ec) {
        getLogger().finer("in NestComponentIA.isApplicable <" + currentPattern + ">");
        String nestedElementTagName = currentPattern.peekLast();

        Object o = ec.peekObject();
        PropertySetter parentBean = new PropertySetter(o);

        int containmentType = parentBean.canContainComponent(nestedElementTagName);

        switch (containmentType) {
        case PropertySetter.NOT_FOUND:
            return false;

            // we only push action data if NestComponentIA is applicable
        case PropertySetter.AS_COLLECTION:
        case PropertySetter.AS_PROPERTY:
            ActionData ad = new ActionData(parentBean, containmentType);
            actionDataStack.push(ad);

            return true;
        default:
            ec.addError(new ErrorItem("PropertySetter.canContainComponent returned "
                    + containmentType));
            return false;
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void begin(final ExecutionContext ec, final String name, final Attributes attributes)
            throws ActionException {
        getLogger().finer("in NestComponentIA begin method");
        // get the action data object pushed in isApplicable() method call
        ActionData actionData = (ActionData) actionDataStack.peek();

        String className = attributes.getValue(CLASS_ATTRIBUTE);

        // perform variable name substitution
        className = ec.subst(className);

        if (Option.isEmpty(className)) {
            actionData.inError = true;

            String errMsg = "No class name attribute in <" + name + ">";
            getLogger().severe(errMsg);
            ec.addError(new ErrorItem(errMsg));

            return;
        }

        try {
            getLogger().log(Level.FINER, "About to instantiate component <{0}> of type [{1}]",
                    new Object[] { name, className });

            actionData.nestedComponent = Loader.loadClass(className).newInstance();

            // pass along the repository
            if (actionData.nestedComponent instanceof Component) {
                ((Component) actionData.nestedComponent).setLoggerRepository(this.repository);
            }
            getLogger().log(Level.FINER, "Pushing component <{0}> on top of the object stack.",
                    name);
            ec.pushObject(actionData.nestedComponent);
        } catch (Exception oops) {
            actionData.inError = true;

            String msg = "Could not create component <" + name + ">.";
            getLogger().log(Level.SEVERE, msg, oops);
            ec.addError(new ErrorItem(msg));
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void end(final ExecutionContext ec, final String name) throws ActionException {
        getLogger().finer("entering end method");

        // pop the action data object pushed in isApplicable() method call
        // we assume that each this begin
        ActionData actionData = (ActionData) actionDataStack.pop();

        if (actionData.inError) {
            return;
        }

        if (actionData.nestedComponent instanceof OptionHandler) {
            ((OptionHandler) actionData.nestedComponent).activateOptions();
        }

        Object o = ec.peekObject();

        if (o != actionData.nestedComponent) {
            getLogger().warning(
                    "The object on the top the of the stack is not the component pushed earlier.");
        } else {
            getLogger().finer("Removing component from the object stack");
            ec.popObject();

            // Now let us attach the component
            switch (actionData.containmentType) {
            case PropertySetter.AS_PROPERTY:
                getLogger().log(Level.FINER, "Setting [{0}] to parent of type [{1}]",
                        new Object[] { name, actionData.parentBean.getObjClass() });
                actionData.parentBean.setComponent(name, actionData.nestedComponent);

                break;
            case PropertySetter.AS_COLLECTION:
                getLogger().log(Level.FINER, "Adding [{0}] to parent of type [{1}]",
                        new Object[] { name, actionData.parentBean.getObjClass() });
                actionData.parentBean.addComponent(name, actionData.nestedComponent);

                break;
            }
        }
    }

    private static final class ActionData {
        PropertySetter parentBean;

        Object nestedComponent;

        int containmentType;

        boolean inError;

        ActionData(PropertySetter parentBean, int containmentType) {
            this.parentBean = parentBean;
            this.containmentType = containmentType;
        }
    }

}

// EOF NestComponentIA.java
