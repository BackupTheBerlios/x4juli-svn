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

import java.util.logging.Level;

import org.x4juli.global.components.AbstractFilter;
import org.x4juli.global.spi.ExtendedFilter;
import org.x4juli.global.spi.ExtendedLogRecord;

/**
 * This is a very simple filter based on level matching.
 * <p>
 * Logging API as a whole was originally done for <a
 * href="http://logging.apache.org/log4j/">Apache log4j</a>. <b>Juli</b> is a
 * port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging
 * APIs</a>. All credits for initial idea, design, implementation,
 * documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This
 * file was originally published by <i>Ceki G&uuml;lc&uuml;</i>. Please use
 * exclusively the <i>appropriate</i> mailing lists for questions, remarks and
 * contribution.
 * </p>
 *
 * @author Boris Unckel
 * @since 0.5
 */
public class LevelMatchFilter extends AbstractFilter {
    // -------------------------------------------------------------- Variables

    /**
     * Do we return ACCEPT when a match occurs. Default is <code>true</code>.
     */
    boolean acceptOnMatch = true;

    /**
     */
    Level levelToMatch;

    // ----------------------------------------------------------- Constructors

    // --------------------------------------------------------- Public Methods

    /**
     * Return the decision of this filter.
     *
     * Returns {@link ExtendedFilter#DENY} if the <b>LevelToMatch</b> option is
     * not set or if there is not match. Otherwise, if there is a match, then
     * the returned decision is {@link ExtendedFilter#ACCEPT} if the
     * <b>AcceptOnMatch</b> property is set to <code>true</code>. The
     * returned decision is {@link ExtendedFilter#DENY} if the <b>AcceptOnMatch</b>
     * property is set to false.
     *
     * @see org.x4juli.global.components.AbstractFilter#decide(org.x4juli.global.spi.ExtendedLogRecord)
     * @since 0.5
     */
    public int decide(final ExtendedLogRecord record) {
        if (this.levelToMatch == null) {
            return ExtendedFilter.X4JULI_NEUTRAL;
        }

        boolean matchOccured = false;

        if (this.levelToMatch.equals(record.getLevel())) {
            matchOccured = true;
        }

        if (matchOccured) {
            if (this.acceptOnMatch) {
                return ExtendedFilter.X4JULI_ACCEPT;
            } else {
                return ExtendedFilter.X4JULI_DENY;
            }
        } else {
            return ExtendedFilter.X4JULI_NEUTRAL;
        }
    }

    /**
     * @param level
     * @since 0.5
     */
    public void setLevelToMatch(String level) {
        try {
            this.levelToMatch = Level.parse(level);
        } catch (Exception e) {
            this.levelToMatch = null;
        }
    }

    /**
     * @return String representation of the level to match
     * @since 0.5
     */
    public String getLevelToMatch() {
        return (this.levelToMatch == null) ? null : this.levelToMatch.toString();
    }

    /**
     * @param acceptOnMatch
     * @since 0.5
     */
    public void setAcceptOnMatch(boolean acceptOnMatch) {
        this.acceptOnMatch = acceptOnMatch;
    }

    /**
     * @return whether matches or not
     * @since 0.5
     */
    public boolean getAcceptOnMatch() {
        return this.acceptOnMatch;
    }

}
