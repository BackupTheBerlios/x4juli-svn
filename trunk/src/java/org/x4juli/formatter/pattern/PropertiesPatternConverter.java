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

import java.util.Iterator;
import java.util.Set;

import org.x4juli.global.spi.ExtendedLogRecord;

/**
 * Currently not available. Returns always "?".
 * 
 * @author Boris Unckel
 * @since 0.5
 */
public class PropertiesPatternConverter extends LogRecordPatternConverter {

    // -------------------------------------------------------------- Variables
    /**
     * Name of property to output.
     */
    private final String option;

    // ----------------------------------------------------------- Constructors

    /**
     * Private constructor.
     */
    private PropertiesPatternConverter(final String[] options) {
        super(((options != null) && (options.length > 0)) ? ("Property{" + options[0] + "}")
                : "Properties", "property");
        if ((options != null) && (options.length > 0)) {
            option = options[0];
        } else {
            option = null;
        }
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Obtains an instance of PropertiesPatternConverter.
     * 
     * @param options options, may be null or first element contains name of property to format.
     * @return instance of PropertiesPatternConverter.
     * @since 0.5
     */
    public static PropertiesPatternConverter newInstance(final String[] options) {
        return new PropertiesPatternConverter(options);
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void format(final ExtendedLogRecord logRecord, final StringBuffer toAppendTo) {
      // if there is no additional options, we output every single
      // Key/Value pair for the MDC in a similar format to Hashtable.toString()
      if (option == null) {
        toAppendTo.append("{");

        Set keySet = logRecord.getPropertyKeySet();

        for (Iterator i = keySet.iterator(); i.hasNext();) {
          Object item = i.next();
          Object val = logRecord.getProperty(String.valueOf(item));
          toAppendTo.append("{").append(item).append(",").append(val).append(
            "}");
        }

        toAppendTo.append("}");
      } else {
        // otherwise they just want a single key output
        Object val = logRecord.getProperty(option);

        if (val != null) {
          toAppendTo.append(val);
        }
      }
    }
}

// EOF PropertiesPatternConverter.java
