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
package org.x4juli.formatter.helper;

import java.util.logging.LogRecord;

import org.x4juli.global.spi.ExtendedLogRecord;
import org.x4juli.global.spi.ExtendedLogRecordWrapper;

/**
 * Utility methods for formatting purposes.
 * 
 * @author Boris Unckel
 * @since 0.5
 */
public final class FormatterUtil {

    // -------------------------------------------------------------- Variables

    // ----------------------------------------------------------- Constructors

    /**
     * No instanciation wanted.
     */
    private FormatterUtil() {
        //NOP
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Uses <code>java.util.logging.Formatter</code> to format a message.
     * 
     * @see java.util.logging.Formatter#formatMessage(java.util.logging.LogRecord)
     * @param record to format.
     * @return the localized, formatted message as String.
     */
    public static String formatMessage(final LogRecord record) {
        if (record instanceof ExtendedLogRecord) {
            return formatMessage((ExtendedLogRecord) record);
        } else {
            return formatMessage((ExtendedLogRecord) new ExtendedLogRecordWrapper(record));
        }
    }

    /**
     * A fully independent formatting of the raw Message. If the raw message
     * does not contain any java.text parameters like {0} or the ResourceBundle
     * is missing key and value, the parameters are appended on the raw message
     * with [Number=Value]. The Method will try to use the cache of the formatted
     * message and fill it after formatting.
     * 
     * @param record containing raw message and optional parameters.
     * @return the formatted message String.
     */
    public static String formatMessage(final ExtendedLogRecord record) {
        if (record.getFormattedMessage() != null) {
            return record.getFormattedMessage();
        }
        String format = record.getMessage();
        if (format == null) {
            return String.valueOf(format);
        }
        java.util.ResourceBundle resBundle = record.getResourceBundle();
        if (resBundle != null) {
            try {
                format = resBundle.getString(record.getMessage());
            } catch (java.util.MissingResourceException ex) {
                // Not nice - ResourceBundle is missing the key.
                // Use record message as format.
                format = record.getMessage();
            }
        }
        // Formatting.
        Object parameters[] = record.getParameters();
        String ret = null;
        try {
            if (parameters == null || parameters.length == 0) {
                // No parameters. Just return format string.
                // First default case.
                return format;
            }
            // Is this a java.text style format?
            if (format.indexOf("{0") >= 0) {
                // Second default case
                ret = java.text.MessageFormat.format(format, parameters);
            }
        } catch (Exception ex) {
            // Case of formatting exception.
            ret = appendParamsToString(format, parameters);
        }
        // Case format String is not valid for java.text.MessageFormat
        // AND there are parameters
        if(ret == null) {
            ret = appendParamsToString(format, parameters);
        }
        record.setFormattedMessage(ret);
        return ret;
    }

    private static String appendParamsToString(final String format, final Object[] parameters) {
        StringBuffer buf = new StringBuffer(format);
        for (int i = 0; i < parameters.length; i++) {
            buf.append("[");
            buf.append(i);
            buf.append("=");
            try {
                buf.append(String.valueOf(parameters[i]));
            } catch (RuntimeException e) {
                buf.append("Error in format[");
                buf.append(e);
                buf.append("]");
            }
            buf.append("]");
        }
        return buf.toString();
    }

}

// EOF FormatterUtil.java
