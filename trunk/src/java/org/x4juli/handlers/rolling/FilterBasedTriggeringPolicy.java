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
package org.x4juli.handlers.rolling;

import java.util.logging.Handler;

import org.x4juli.global.spi.AbstractComponent;
import org.x4juli.global.spi.ExtendedFilter;
import org.x4juli.global.spi.ExtendedLogRecord;

/**
 * FilterBasedTriggeringPolicy determines if rolling should be triggered by
 * evaluating the current message against a set of filters. Unless a filter
 * rejects a message, a rolling event will be triggered.
 * <p>
 * Logging API as a whole was originally done for <a
 * href="http://logging.apache.org/log4j/">Apache log4j</a>. <b>Juli</b> is a
 * port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging
 * APIs</a>. All credits for initial idea, design, implementation,
 * documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This
 * file was originally published by <i>Curt Arnold</i>. Please use exclusively
 * the <i>appropriate</i> mailing lists for questions, remarks and
 * contribution.
 * </p>
 *
 * @author Boris Unckel
 * @since 0.5
 */
public final class FilterBasedTriggeringPolicy extends AbstractComponent implements TriggeringPolicy {
    // -------------------------------------------------------------- Variables

    /**
     * The first filter in the filter chain. Set to <code>null</code>
     * initially.
     */
    private ExtendedFilter headFilter;

    /**
     * The last filter in the filter chain.
     */
    private ExtendedFilter tailFilter;

    // ----------------------------------------------------------- Constructors

    /**
     * Creates a new FilterBasedTriggeringPolicy.
     */
    public FilterBasedTriggeringPolicy() {
        super();
    }

    // --------------------------------------------------------- Public Methods

    /**
     * {@inheritDoc}
     *
     * @since 0.5
     */
    public boolean isTriggeringEvent(Handler handler, ExtendedLogRecord record, String filename,
            long fileLength) {
        //
        // in the abnormal case of no contained filters
        // always return true to avoid each logging event
        // from having its own file.
        if (this.headFilter == null) {
            return false;
        }

        //
        // otherwise loop through the filters
        //
        for (ExtendedFilter f = this.headFilter; f != null; f = f.getNext()) {
            switch (f.decide(record)) {
            case ExtendedFilter.X4JULI_DENY:
                return false;

            case ExtendedFilter.X4JULI_ACCEPT:
                return true;
            }
        }

        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.5
     */
    public void activateOptions() {
        for (ExtendedFilter f = this.headFilter; f != null; f = f.getNext()) {
            f.activateOptions();
        }
    }

    /**
     * Add a filter to end of the filter list.
     *
     * @param newFilter filter to add to end of list.
     */
    public void addFilter(final ExtendedFilter newFilter) {
        if (this.headFilter == null) {
            this.headFilter = newFilter;
            this.tailFilter = newFilter;
        } else {
            this.tailFilter.setNext(newFilter);
            this.tailFilter = newFilter;
        }
    }

    /**
     * Clear the filters chain.
     *
     */
    public void clearFilters() {
        this.headFilter = null;
        this.tailFilter = null;
    }

    /**
     * Returns the head Filter.
     *
     * @return head of filter chain, may be null.
     *
     */
    public ExtendedFilter getFilter() {
        return this.headFilter;
    }

}

// EOF FilterBasedTriggeringPolicy.java
