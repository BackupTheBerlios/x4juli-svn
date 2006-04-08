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

/**
 * This class contains costants used by other Actions.
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
public final class ActionConst {
    
    public static final String HANDLER_TAG = "handler";
    public static final String REF_ATTRIBUTE = "ref";
    public static final String ADDITIVITY_ATTRIBUTE = "additivity";
    public static final String CONVERTER_CLASS_ATTRIBUTE = "converterClass";
    public static final String CONVERSION_WORD_ATTRIBUTE = "conversionWord";
    public static final String PATTERN_ATTRIBUTE = "pattern";
    public static final String ACTION_CLASS_ATTRIBUTE = "actionClass";
    public static final String VALUE_ATTR = "value";
    public static final String CLASS_ATTR = "class";
    public static final String EMPTY_STR = "";
    
    static final String INHERITED = "INHERITED";
    static final String NULL = "NULL";
    static final Class[] ONE_STRING_PARAM = new Class[] { String.class };

    public static final String HANDLER_BAG = "HANDLER_BAG";
    public static final String FILTER_CHAIN_BAG = "FILTER_CHAIN_BAG";

    /**
     * No instantiation wanted.
     */
    private ActionConst() {
    }

}

// EOF ActionConst.java
