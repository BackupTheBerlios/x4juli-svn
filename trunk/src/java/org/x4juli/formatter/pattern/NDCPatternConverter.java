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

/**
 * Currently not available. Returns always "?".
 * @author Boris Unckel
 * @since 0.5
 */
public class NDCPatternConverter extends CurrentlyNotAvailableConverter {

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

}

// EOF NDCPatternConverter.java
