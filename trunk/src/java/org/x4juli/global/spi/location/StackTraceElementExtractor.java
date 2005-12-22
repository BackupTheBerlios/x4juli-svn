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

package org.x4juli.global.spi.location;

import org.x4juli.global.Constants;

/**
 * A faster extractor based on StackTraceElements introduced in JDK 1.4.
 * Since <code>java.util.logging</code> was introduced in JDK 1.4, x4juli
 * fully depends on <code>java.lang.StackTraceElement</code> and does
 * not use reflection to determine the LocationInfo.
 * <p>
 * Logging API as a whole was originally done for <a href="http://logging.apache.org/log4j/">Apache
 * log4j</a>. <b>Juli</b> is a port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging APIs</a>. All
 * credits for initial idea, design, implementation, documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This file was originally
 * published by <i>Martin Schulz, Ceki G&uuml;lc&uuml;</i>. Please use exclusively the
 * <i>appropriate</i> mailing lists for questions, remarks and contribution.
 * </p>
 * <p>
 * </p>
 * 
 * @author Boris Unckel
 * @since 0.5
 */
public class StackTraceElementExtractor {

    static void extract(final LocationInfo li, final Throwable t, final String fqnOfInvokingClass) {
        if (t == null) {
            return;
        }

        StackTraceElement location = null;
        try {
            // Object[] stes = (Object[]) t.getStackTrace();
            StackTraceElement[] stes = t.getStackTrace();
            boolean match = false;
            for (int i = 0; i < stes.length; i++) {

                if (stes[i].getClassName().equals(fqnOfInvokingClass)) {
                    match = true;
                } else if (match) {
                    location = stes[i];
                    break;
                }
            }
        } catch (Throwable e) {
            // Extraction failed, not much we could do now. We can't event log
            // this
            // failure because if there is one failure, there may be many others
            // which
            // are likely to follow. As location extraction is done on a
            // best-effort
            // basis, silence is preferable to overwhelming the user...
        }

        // If we failed to extract the location line, then default to
        // LocationInfo.NOT_AVAILABLE_CHAR
        if (location == null) {
            li.className = Constants.NOT_AVAILABLE_CHAR;
            li.fileName = Constants.NOT_AVAILABLE_CHAR;
            li.lineNumber = Constants.NOT_AVAILABLE_CHAR;
            li.methodName = Constants.NOT_AVAILABLE_CHAR;
        } else { // otherwise, get the real info
            setClassName(location, li);
            setMethodName(location, li);
            setFileName(location, li);
            setLineNumber(location, li);
        }
    }

    private static void setClassName(final StackTraceElement location, final LocationInfo li) {
        try {
            li.className = location.getClassName();
        } catch (Exception e) {
            li.className = Constants.NOT_AVAILABLE_CHAR;
        }
    }

    private static void setMethodName(final StackTraceElement location, final LocationInfo li) {
        try {
            li.methodName = location.getMethodName();
        } catch (Exception e) {
            li.methodName = Constants.NOT_AVAILABLE_CHAR;
        }
    }

    private static void setFileName(final StackTraceElement location, final LocationInfo li) {
        try {
            li.fileName = location.getFileName();
        } catch (Exception e) {
            li.fileName = Constants.NOT_AVAILABLE_CHAR;
        }
    }

    private static void setLineNumber(final StackTraceElement location, final LocationInfo li) {
        try {
            int lnr = location.getLineNumber();
            if (lnr > 0) {
                li.lineNumber = String.valueOf(lnr);
            } else {
                li.lineNumber = Constants.NOT_AVAILABLE_CHAR;
            }
        } catch (Exception e) {
            li.lineNumber = Constants.NOT_AVAILABLE_CHAR;
        }
    }

}
