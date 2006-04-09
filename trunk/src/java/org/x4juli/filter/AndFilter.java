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

import org.x4juli.global.spi.ExtendedFilter;
import org.x4juli.global.spi.ExtendedLogRecord;

/**
 * A filter that 'and's the results of any number of contained filters together.
 * For the filter to process events, all contained filters must return
 * Filter.ACCEPT.
 *
 * If the contained filters do not return {@link ExtendedFilter#X4JULI_ACCEPT},
 * {@link ExtendedFilter#X4JULI_NEUTRAL} is returned.
 *
 * If acceptOnMatch is set to true, {@link ExtendedFilter#X4JULI_ACCEPT} is returned. If
 * acceptOnMatch is set to false, @link ExtendedFilter#X4JULI_DENY} is returned.
 * <p>
 * Logging API as a whole was originally done for <a
 * href="http://logging.apache.org/log4j/">Apache log4j</a>. <b>Juli</b> is a
 * port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging
 * APIs</a>. All credits for initial idea, design, implementation,
 * documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This
 * file was originally published by <i>Scott Deboy</i>. Please use exclusively
 * the <i>appropriate</i> mailing lists for questions, remarks and
 * contribution.
 * </p>
 *
 * @author Boris Unckel
 * @since 0.5
 *
 */
public class AndFilter extends AbstractFilter {

    // -------------------------------------------------------------- Variables

    AbstractFilter headFilter = null;

    AbstractFilter tailFilter = null;

    boolean acceptOnMatch = true;


    // ----------------------------------------------------------- Constructors

    // --------------------------------------------------------- Public Methods

    /**
     * @see org.x4juli.filter.AbstractFilter#decide(org.x4juli.global.spi.ExtendedLogRecord)
     * @since 0.5
     */
    public int decide(final ExtendedLogRecord record) {
        boolean accepted = true;
        ExtendedFilter f = this.headFilter;
        while (f != null) {
            accepted = accepted
                    && (ExtendedFilter.X4JULI_ACCEPT == f.decide(record));
            f = f.getNext();
        }
        if (accepted) {
            if (this.acceptOnMatch) {
                return ExtendedFilter.X4JULI_ACCEPT;
            }
            return ExtendedFilter.X4JULI_DENY;
        }
        return ExtendedFilter.X4JULI_NEUTRAL;
    }

    /**
     * @param filter
     *            to add
     * @since 0.5
     */
    public void addFilter(AbstractFilter filter) {
        getLogger().finer("add" + filter);
        if (this.headFilter == null) {
            this.headFilter = filter;
            this.tailFilter = filter;
        } else {
            this.tailFilter.setNext(filter);
        }
    }

    /**
     * @param acceptOnMatch
     *            whether to accept or not
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
