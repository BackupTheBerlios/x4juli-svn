/*
 * Copyright 2006 x4juli.org.
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

import org.x4juli.global.context.ContextFactory;
import org.x4juli.global.spi.ExtendedFilter;
import org.x4juli.global.spi.ExtendedLogRecord;
import org.x4juli.global.spi.LogIllegalStateException;
import org.x4juli.global.spi.TCCLMapper;

/**
 * The filter is based on the hashcode of the ThreadContextClassloder
 * stored in the ExtendedLogRecord. It compares an external provided
 * identifier with the id.
 * @author Boris Unckel
 * @since 0.7
 */
public class TCCLMapFilter extends AbstractFilter {
    // -------------------------------------------------------------- Variables

    /**
     * Do we return ACCEPT when a match occurs. Default is <code>true</code>.
     */
    boolean acceptOnMatch = true;
    
    String tcclToMatch;
    
    TCCLMapper mapper = ContextFactory.getThreadContextClassLoaderMapper();

    // ----------------------------------------------------------- Constructors
    /**
     * 
     */
    public TCCLMapFilter() {
        super();
    }

    // --------------------------------------------------------- Public Methods

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public int decide(ExtendedLogRecord record) {
        if (this.tcclToMatch == null) {
            return ExtendedFilter.X4JULI_NEUTRAL;
        }

        boolean matchOccured = false;
        
        final String identifier = mapper.getIdentifier(record.getClassloaderId());

        if (this.tcclToMatch.equals(identifier)) {
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
     * Returns the TCCL Identifier.
     * @return the tcclToMatch
     */
    public String getTcclToMatch() {
        return this.tcclToMatch;
    }

    /**
     * Sets the TCCL Identifier.
     * @param tcclToMatch the tcclToMatch to set
     */
    public void setClassloaderId(String tcclToMatch) {
        this.tcclToMatch = tcclToMatch;
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void activateOptions() {
        if(this.tcclToMatch == null) {
            throw new LogIllegalStateException("The filter is not valid without an id. id is null");
        }
        super.activateOptions();
    }

}

// EOF TCCLMapFilter.java
