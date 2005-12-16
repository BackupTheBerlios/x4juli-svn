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
package org.x4juli.formatter.pattern;

import org.x4juli.global.Constants;
import org.x4juli.global.spi.ExtendedLogRecord;

/**
 * Super Class for currently not available converters.
 * @author Boris Unckel
 * @since 0.5
 */
public class CurrentlyNotAvailableConverter extends LogRecordPatternConverter {


    /**
     * @param name
     * @param style
     */
    protected CurrentlyNotAvailableConverter(String name, String style) {
        super(name, style);
    }

    /**
     * {@inheritDoc}
     * @since
     */
    public final void format(ExtendedLogRecord record, StringBuffer toAppendTo) {
        toAppendTo.append(Constants.NOT_AVAILABLE_CHAR);

    }

}

// EOF CurrentlyNotAvailableConverter.java
