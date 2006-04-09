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
import org.x4juli.global.helper.OptionConverter;
import org.x4juli.global.spi.ErrorItem;
import org.x4juli.global.spi.ExtendedLogger;
import org.x4juli.global.spi.LevelAttachable;
import org.xml.sax.Attributes;

/**
 * Action for Component {@link ExtendedLogger}.
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
public class LevelAction extends AbstractAction {

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void begin(ExecutionContext ec, String name, Attributes attributes)
            throws ActionException {
        Object o = ec.peekObject();

        if (!(o instanceof LevelAttachable)) {
            getLogger().warning("Could not find a LevelAttachable at the top of execution stack.");
            inError = true;
            ec.addError(new ErrorItem(
                    "For element <level>, could not find a LevelAttachable at the top of execution stack."));

            return;
        }

        LevelAttachable l = (LevelAttachable) o;

        String levelAttachableName = l.getName();

        String levelStr = attributes.getValue(ActionConst.VALUE_ATTR);
        if (getLogger().isLoggable(Level.FINER)) {
            getLogger().finer(
                    "Encapsulating logger name is [" + levelAttachableName + "], levelvalue is  ["
                            + levelStr + "].");
        }
        if (ActionConst.INHERITED.equalsIgnoreCase(levelStr)
                || ActionConst.NULL.equalsIgnoreCase(levelStr)) {
            l.setLevel(null);
        } else {
            String className = attributes.getValue(ActionConst.CLASS_ATTR);

            if ((className == null) || ActionConst.EMPTY_STR.equals(className)) {
                Level myLevel = OptionConverter.toLevel(levelStr, Level.FINER);
                l.setLevel(myLevel);
            } else {
                getLogger().finer("Desired Level sub-class: [" + className + ']');

                try {
                    Class clazz = Loader.loadClass(className);
                    Method toLevelMethod = clazz.getMethod("parse", ActionConst.ONE_STRING_PARAM);
                    Level pri = (Level) toLevelMethod.invoke(null, new Object[] { levelStr });
                    l.setLevel(pri);
                } catch (Exception oops) {
                    getLogger().log(Level.SEVERE,
                            "Could not create level [" + levelStr + "]. Reported error follows.",
                            oops);

                    return;
                }
            }
        }
        if (getLogger().isLoggable(Level.FINER)) {
            getLogger().finer(levelAttachableName + " level set to " + l.getLevel());
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

// EOF LevelAction.java
