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
package org.x4juli.config.joran.spi;

import org.x4juli.global.spi.LogConfigurationException;

/**
 * By throwing an exception an action can signal the Interpreter to skip processing, all the nested
 * elements nested within the element throwing the exception or skip all following sibling elements
 * in the document.
 * <p>
 * Logging API as a whole was originally done for <a href="http://logging.apache.org/log4j/">Apache
 * log4j</a>. <b>x4uli</b> is a port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging APIs</a>. All
 * credits for initial idea, design, implementation, documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This file was originally
 * published by <i>Ceki G&uuml;lcu&uuml;</i>. Please use exclusively the <i>appropriate</i>
 * mailing lists for questions, remarks and contribution.
 * </p>
 * 
 * @author Boris Unckel
 * @since 0.7
 */
public final class ActionException extends LogConfigurationException {

    /**
     * 
     */
    private static final long serialVersionUID = 2625054239337694278L;

    /**
     * SKIP_CHILDREN signals the {@link Interpreter} to skip processing all the
     * nested elements contained within the element causing this ActionException.
     */
    public static final int SKIP_CHILDREN = 1;

    /**
     * SKIP_SIBLINGS signals the {@link Interpreter} to skip processing all the
     * children of this element as well as all the siblings of this elements,
     * including any children they may have.
     */
    public static final int SKIP_SIBLINGS = 2;
    
    private final int skipCode;
    /**
     * @param message
     */
    public ActionException(final String message, final int skipCode) {
        super(message);
        this.skipCode = skipCode;
    }

    /**
     * @param message
     * @param cause
     */
    public ActionException(final String message, final int skipCode, final Throwable cause) {
        super(message, cause);
        this.skipCode = skipCode;
    }

    public int getSkipCode() {
      return skipCode;
    }

}

// EOF ActionException.java
