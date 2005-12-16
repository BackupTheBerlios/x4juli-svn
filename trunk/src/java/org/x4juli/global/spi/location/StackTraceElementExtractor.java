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

import java.lang.reflect.Method;

import org.x4juli.global.Constants;

/**
 * A faster extractor based on StackTraceElements introduced in JDK 1.4.
 *
 * The present code uses reflection. Thus, it should compile on all platforms.
 * <p>
 * Logging API as a whole was originally done for <a
 * href="http://logging.apache.org/log4j/">Apache log4j</a>. <b>Juli</b> is a
 * port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging
 * APIs</a>. All credits for initial idea, design, implementation,
 * documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This
 * file was originally published by <i>Martin Schulz, Ceki G&uuml;lc&uuml;</i>.
 * Please use exclusively the <i>appropriate</i> mailing lists for questions,
 * remarks and contribution.
 * </p>
 * <p>
 * This is just a plain copy.
 * </p>
 *
 * @author Martin Schulz
 * @author Ceki G&uuml;lc&uuml;
 * @since 0.5
 */
public class StackTraceElementExtractor {
    //Unused?
    //private static boolean haveStackTraceElement = false;

    private static Method getStackTrace = null;

    private static Method getClassName = null;

    private static Method getFileName = null;

    private static Method getMethodName = null;

    private static Method getLineNumber = null;

    private static Object[] nullArgs = new Object[] {};

    static {
        try {
            Class cStackTraceElement = Class
                    .forName("java.lang.StackTraceElement");
            Class[] nullClassArray = new Class[] {};
            getStackTrace = Throwable.class.getDeclaredMethod("getStackTrace",
                    nullClassArray);
            getClassName = cStackTraceElement.getDeclaredMethod("getClassName",
                    nullClassArray);
            getFileName = cStackTraceElement.getDeclaredMethod("getFileName",
                    nullClassArray);
            getMethodName = cStackTraceElement.getDeclaredMethod(
                    "getMethodName", nullClassArray);
            getLineNumber = cStackTraceElement.getDeclaredMethod(
                    "getLineNumber", nullClassArray);
            //haveStackTraceElement = true;
        } catch (Throwable e) {
            // we should never get here
        }
    }

    static void extract(LocationInfo li, Throwable t, String fqnOfInvokingClass) {
        if (t == null) {
            return;
        }

        Object location = null;
        try {
            Object[] stes = (Object[]) getStackTrace.invoke(t, nullArgs);

            boolean match = false;
            for (int i = 0; i < stes.length; i++) {
                if (((String) getClassName.invoke(stes[i], nullArgs))
                        .equals(fqnOfInvokingClass)) {
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
            setClassName(li, location);
            setFileName(li, location);
            setMethodName(li, location);
            setLineNumber(li, location);
        }
    }

    /**
     * Return the fully qualified class name of the caller making the logging
     * request.
     */
    static void setClassName(LocationInfo li, Object location) {
        try {
            li.className = (String) getClassName.invoke(location, nullArgs);
        } catch (Throwable e) {
            li.className = Constants.NOT_AVAILABLE_CHAR;
        }
    }

    static void setFileName(LocationInfo li, Object location) {
        try {
            li.fileName = (String) getFileName.invoke(location, nullArgs);
        } catch (Throwable e) {
            li.fileName = Constants.NOT_AVAILABLE_CHAR;
        }
    }

    static void setLineNumber(LocationInfo li, Object location) {
        Integer ln = null;
        try {
            ln = (Integer) getLineNumber.invoke(location, nullArgs);
            if (ln.intValue() >= 0) {
                li.lineNumber = ln.toString();
            }
        } catch (Throwable e) {
            li.lineNumber = Constants.NOT_AVAILABLE_CHAR;
        }
    }

    static void setMethodName(LocationInfo li, Object location) {
        try {
            li.methodName = (String) getMethodName.invoke(location, nullArgs);
        } catch (Throwable e) {
            li.methodName = Constants.NOT_AVAILABLE_CHAR;
        }
    }
}
