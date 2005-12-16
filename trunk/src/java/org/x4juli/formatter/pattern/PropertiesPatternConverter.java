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
public class PropertiesPatternConverter extends CurrentlyNotAvailableConverter {

    // -------------------------------------------------------------- Variables

    // ----------------------------------------------------------- Constructors

    /**
     * Private constructor.
     */
    private PropertiesPatternConverter(
      final String[] options) {
      super("Properties", "property");
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Obtains an instance of PropertiesPatternConverter.
     * @param options options, may be null or first element contains name of property to format.
     * @return instance of PropertiesPatternConverter.
     */
    public static PropertiesPatternConverter newInstance(
      final String[] options) {
      return new PropertiesPatternConverter(options);
    }

}

// EOF PropertiesPatternConverter.java
