/*
 * Copyright 2005, x4juli.org.
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

import java.util.logging.LogRecord;

import org.x4juli.global.helper.LoggerUtil;
import org.x4juli.global.spi.AbstractComponent;
import org.x4juli.global.spi.ExtendedFilter;
import org.x4juli.global.spi.ExtendedLogRecord;

/**
 * <p>
 * The abstract class for every <code>org.x4juli.filter</code> class. The
 * class provides the basic implementation for cascading filters. Different to
 * log4j, <code>java.util.logging.Filter</code> works with booleans (and so
 * has two states) - log4j offers three.
 * </p>
 *
 * @author Boris Unckel
 * @since 0.5
 */
public abstract class AbstractFilter extends AbstractComponent implements ExtendedFilter {

    // -------------------------------------------------------------- Variables

    /**
     * Points to the next filter in the filter chain.
     */
    private ExtendedFilter next;

    // ----------------------------------------------------------- Constructors

    // --------------------------------------------------------- Public Methods

    /**
     * Usually filters options become active when set. We provide a default
     * do-nothing implementation for convenience.
     * @see org.x4juli.global.spi.OptionHandler#activateOptions()
     * @since 0.5
     */
    public void activateOptions() {
        //NOP
    }

    /**
     * @see org.x4juli.global.spi.ExtendedFilter#setNext(org.x4juli.global.spi.ExtendedFilter)
     * @since 0.5
     */
    public void setNext(ExtendedFilter next) {
        this.next = next;
    }

    /**
     * @see org.x4juli.global.spi.ExtendedFilter#getNext()
     * @since 0.5
     */
    public ExtendedFilter getNext() {
        return this.next;
    }

    /**
     * <p>
     * If the decision is <code>DENY</code>, then the event will be dropped.
     * If the decision is <code>NEUTRAL</code>, then the next filter, if any,
     * will be invoked. If the decision is ACCEPT then the event will be logged
     * without consulting with other filters in the chain.
     *
     * @param record
     *            The LoggingEvent to decide upon.
     * @since 0.5
     */
    public abstract int decide(final ExtendedLogRecord record);

    /**
     * @see java.util.logging.Filter#isLoggable(java.util.logging.LogRecord)
     * @since 0.5
     */
    public final boolean isLoggable(final LogRecord record) {
        ExtendedLogRecord toDecideRecord = LoggerUtil.wrapLogRecord(record);
        switch (decide(toDecideRecord)) {
        case ExtendedFilter.X4JULI_ACCEPT:
            return true;
        case ExtendedFilter.X4JULI_DENY:
        case ExtendedFilter.X4JULI_NEUTRAL:
        default:
            return false;
        }
    }

}
