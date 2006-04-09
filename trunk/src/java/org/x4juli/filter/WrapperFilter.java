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

import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.x4juli.global.resources.MessageProperties;
import org.x4juli.global.spi.ExtendedFilter;
import org.x4juli.global.spi.ExtendedLogRecord;

/**
 * A wrapper around a <code>java.util.logging.Filter</code>. If the wrappedFilter is already of
 * type ExtendedFilter, the wrapper will not wrap but redirect method calls
 * directly to the (ExtendedFilter) wrappedFilter.
 *
 * A WrapperFilter is an immutable object.
 *
 * @author Boris Unckel
 * @since 0.5
 */
public class WrapperFilter extends AbstractFilter {

    // -------------------------------------------------------------- Variables
    protected final Filter wrappedFilter;

    protected final ExtendedFilter extendedFilter;

    // ----------------------------------------------------------- Constructors

    /**
     * Constructs a wrapper around a Filter. If the wrappedFilter is already of
     * type ExtendedFilter, the wrapper will not wrap but redirect method calls
     * directly to the (ExtendedFilter) wrappedFilter.
     *
     * @param wrappedFilter
     *            the <code>java.util.Logging.Filter</code> to wrap.
     */
    public WrapperFilter(final Filter wrappedFilter) {
        super();
        if (wrappedFilter instanceof ExtendedFilter) {
            getLogger()
                    .log(
                            Level.WARNING,
                            MessageText.Filter_configuration_wrapps_an_Juli_ExtendedFilter);
            this.extendedFilter = (ExtendedFilter) wrappedFilter;
            this.wrappedFilter = null;
        } else {
            this.extendedFilter = null;
            this.wrappedFilter = wrappedFilter;
        }

    }

    // --------------------------------------------------------- Public Methods

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public int decide(final ExtendedLogRecord record) {
        // Decision is taken by the wrapped filter xor extendedFilter
        if (this.wrappedFilter != null) {
            if (this.wrappedFilter.isLoggable((LogRecord) record)) {
                return ExtendedFilter.X4JULI_ACCEPT;
            } else {
                return ExtendedFilter.X4JULI_DENY;
            }
        } else if (this.extendedFilter != null) {
            return this.extendedFilter.decide(record);
        }

        //There is no decision, this is an programming error
        throw new InvalidFilterException(
                "The filter has neither a wrapper nor a redirection");
    }

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public MessageProperties getMessageProperties() {
        return MessageProperties.PROPERTIES_FILTER;
    }

}

// EOF WrapperFilter.java
