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
 * A filter which denys everything
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
public class DenyAllFilter extends AbstractFilter {

    // -------------------------------------------------------------- Variables

    // ----------------------------------------------------------- Constructors

    // --------------------------------------------------------- Public Methods

    /**
     * Deny All
     *
     * @see org.x4juli.global.components.AbstractFilter#decide(org.x4juli.global.spi.ExtendedLogRecord)
     * @return always false
     * @since 0.5
     *
     */
    public int decide(final ExtendedLogRecord record) {
        return ExtendedFilter.X4JULI_DENY;
    }

}
