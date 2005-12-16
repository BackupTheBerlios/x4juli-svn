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
package org.x4juli.filter;

import org.x4juli.global.components.AbstractFilter;
import org.x4juli.global.spi.ExtendedFilter;
import org.x4juli.global.spi.ExtendedLogRecord;

/**
 * <p>
 * The filter admits two options <b>StringToMatch</b> and <b>AcceptOnMatch</b>.
 * If there is a match between the value of the StringToMatch option and the
 * message of the {@link java.util.logging.LogRecord}, then the {@link #decide} method returns
 * {@link ExtendedFilter#ACCEPT} if the <b>AcceptOnMatch</b> option value is
 * true, if it is false then {@link ExtendedFilter#DENY} is returned. If there
 * is no match, {@link ExtendedFilter#DENY} is returned.
 * <p>
 * Logging API as a whole was originally done for <a
 * href="http://logging.apache.org/log4j/">Apache log4j</a>. <b>Juli</b> is a
 * port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging
 * APIs</a>. All credits for initial idea, design, implementation,
 * documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This
 * file was originally published by <i>Ceki G&uuml;lc&uuml</i>. Please use
 * exclusively the <i>appropriate</i> mailing lists for questions, remarks and
 * contribution.
 * </p>
 *
 * @author Boris Unckel
 * @since 0.5
 *
 */
public class StringMatchFilter extends AbstractFilter {

    // -------------------------------------------------------------- Variables

    boolean acceptOnMatch = true;

    String stringToMatch;

    // ----------------------------------------------------------- Constructors

    // --------------------------------------------------------- Public Methods

    /**
     * @see org.x4juli.global.components.AbstractFilter#decide(org.x4juli.global.spi.ExtendedLogRecord)
     * @since 0.5
     */
    public int decide(final ExtendedLogRecord record) {
        String msg = record.getMessage();

        if (msg == null || this.stringToMatch == null)
            return ExtendedFilter.X4JULI_NEUTRAL;

        if (msg.indexOf(this.stringToMatch) == -1) {
            return ExtendedFilter.X4JULI_NEUTRAL;
        } else { // we've got a match
            if (this.acceptOnMatch) {
                return ExtendedFilter.X4JULI_ACCEPT;
            } else {
                return ExtendedFilter.X4JULI_DENY;
            }
        }
    }

    /**
     * @param s
     * @since 0.5
     */
    public void setStringToMatch(String s) {
        this.stringToMatch = s;
    }

    /**
     * @return the string to match
     * @since 0.5
     */
    public String getStringToMatch() {
        return this.stringToMatch;
    }

    /**
     * @param acceptOnMatch
     * @since 0.5
     */
    public void setAcceptOnMatch(boolean acceptOnMatch) {
        this.acceptOnMatch = acceptOnMatch;
    }

    /**
     * @return whether accepted or not
     */
    public boolean getAcceptOnMatch() {
        return this.acceptOnMatch;
    }

}
