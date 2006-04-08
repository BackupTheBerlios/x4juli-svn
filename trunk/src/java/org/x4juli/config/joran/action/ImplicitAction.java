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
import org.x4juli.config.joran.spi.Pattern;
import org.xml.sax.Attributes;

/**
 * ImplcitActions are like normal (explicit) actions except that are applied by the parser when no
 * other pattern applies. Since there can be many implcit actions, each action is asked whether it
 * applies in the given context. The first impplcit action to respond postively will be applied. See
 * also the {@link #isApplicable} method.
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
public abstract class ImplicitAction extends AbstractAction {

    /**
     * Check whether this implicit action is apprioriate in the current context.
     * 
     * 
     * @param currentPattern This pattern contains the tag name of the current 
     * element being parsed at the top of the stack.
     * @param attributes The attributes of the current element to process.
     * @param ec
     * @return Whether the implicit action is applicable in the current context
     */
    public abstract boolean isApplicable(
      Pattern currentPattern, Attributes attributes, ExecutionContext ec);

}

// EOF ImplicitAction.java
