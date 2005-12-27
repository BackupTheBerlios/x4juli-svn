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
package org.x4juli.formatter.pattern;

import org.x4juli.global.spi.ExtendedLogRecord;

/**
 * Return the event's NDC in a StringBuffer.
 * @author Boris Unckel
 * @since 0.5
 */
public class NDCPatternConverter extends LogRecordPatternConverter {

    // -------------------------------------------------------------- Variables

    /**
     *   Singleton.
     */
    private static final NDCPatternConverter INSTANCE =
      new NDCPatternConverter();

    // ----------------------------------------------------------- Constructors

    /**
     * Private constructor.
     */
    private NDCPatternConverter() {
      super("NDC", "ndc");
    }

    /**
     * Obtains an instance of NDCPatternConverter.
     * @param options options, may be null.
     * @return instance of NDCPatternConverter.
     * @since 0.5
     */
    public static NDCPatternConverter newInstance(
      final String[] options) {
      return INSTANCE;
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void format(final ExtendedLogRecord record, final StringBuffer toAppendTo) {
        toAppendTo.append(record.getNDC());
    }

}

// EOF NDCPatternConverter.java
