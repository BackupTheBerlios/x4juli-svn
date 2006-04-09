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

import org.x4juli.global.spi.ExtendedFilter;
import org.x4juli.global.spi.ExtendedLogRecord;

/**
 * This is a very simple filter based on level matching, which can be used to
 * reject messages with priorities outside a certain range.
 * <p>
 * Logging API as a whole was originally done for <a
 * href="http://logging.apache.org/log4j/">Apache log4j</a>. <b>Juli</b> is a
 * port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging
 * APIs</a>. All credits for initial idea, design, implementation,
 * documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This
 * file was originally published by <i>Simon Kitching</i>. Please use
 * exclusively the <i>appropriate</i> mailing lists for questions, remarks and
 * contribution.
 * </p>
 *
 * @author Boris Unckel
 * @since 0.5
 */
public class LevelRangeFilter extends AbstractFilter {

    // -------------------------------------------------------------- Variables

    /**
     * Do we return ACCEPT when a match occurs. Default is <code>false</code>,
     * so that later filters get run by default
     */
    boolean acceptOnMatch = false;

    Level levelMin;

    Level levelMax;

    // ----------------------------------------------------------- Constructors

    /**
     * Default constructor, does not activateOptions. 
     */
    public LevelRangeFilter() {
        super();
    }

    // --------------------------------------------------------- Public Methods

    /**
     * @see org.x4juli.filter.AbstractFilter#decide(org.x4juli.global.spi.ExtendedLogRecord)
     * @since 0.5
     */
    public int decide(final ExtendedLogRecord record) {
        Level recordLevel = record.getLevel();
        if (this.levelMin != null) {
            if (isGreaterOrEqual(recordLevel, this.levelMin) == false) {
                // level of event is less than minimum
                return ExtendedFilter.X4JULI_DENY;
            }
        }

        if (this.levelMax != null) {
            if (recordLevel.intValue() > this.levelMax.intValue()) {
                // level of event is greater than maximum
                // Alas, there is no Level.isGreater method. and using
                // a combo of isGreaterOrEqual && !Equal seems worse than
                // checking the int values of the level objects..
                return ExtendedFilter.X4JULI_DENY;
            }
        }

        if (this.acceptOnMatch) {
            // this filter set up to bypass later filters and always return
            // accept if level in range
            return ExtendedFilter.X4JULI_ACCEPT;
        } else {
            return ExtendedFilter.X4JULI_NEUTRAL;
        }
    }

    /**
     * Get the value of the <code>LevelMax</code> option.
     *
     * @since 0.5
     */
    public Level getLevelMax() {
        return this.levelMax;
    }

    /**
     * Get the value of the <code>LevelMin</code> option.
     *
     * @since 0.5
     */
    public Level getLevelMin() {
        return this.levelMin;
    }

    /**
     * Get the value of the <code>AcceptOnMatch</code> option.
     *
     * @since 0.5
     */
    public boolean getAcceptOnMatch() {
        return this.acceptOnMatch;
    }

    /**
     * Set the <code>LevelMax</code> option.
     *
     * @since 0.5
     */
    public void setLevelMax(Level levelMax) {
        this.levelMax = levelMax;
    }

    /**
     * Set the <code>LevelMin</code> option.
     *
     * @since 0.5
     */
    public void setLevelMin(Level levelMin) {
        this.levelMin = levelMin;
    }

    /**
     * Set the <code>AcceptOnMatch</code> option.
     *
     * @since 0.5
     */
    public void setAcceptOnMatch(boolean acceptOnMatch) {
        this.acceptOnMatch = acceptOnMatch;
    }

    // ------------------------------------------------------ Protected Methods

    // -------------------------------------------------------- Private Methods

    private static boolean isGreaterOrEqual(Level left, Level right) {
        return (left.intValue() >= right.intValue());
    }

}
