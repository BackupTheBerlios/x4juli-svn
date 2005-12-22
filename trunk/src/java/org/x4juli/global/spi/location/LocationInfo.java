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

import java.util.logging.LogRecord;

import org.x4juli.global.Constants;

/**
 * In <code>java.util.logging.LogRecord</code> are line number and source file
 * as information missing. They have to be obtained, which is an expensive
 * operation due to creating a new Throwable.
 * <p>
 * Logging API as a whole was originally done for <a
 * href="http://logging.apache.org/log4j/">Apache log4j</a>. <b>Juli</b> is a
 * port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging
 * APIs</a>. All credits for initial idea, design, implementation,
 * documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This
 * file was originally published by <i>Mathias Rupprecht</i>. Please use
 * exclusively the <i>appropriate</i> mailing lists for questions, remarks and
 * contribution.
 * </p>
 *
 * @author Boris Unckel
 * @since 0.5
 */

public class LocationInfo implements java.io.Serializable {

    // -------------------------------------------------------------- Variables

    /**
     *
     */
    private static final long serialVersionUID = 3545798797201649716L;

    /**
     * Caller's line number.
     */
    String lineNumber = null;

    /**
     * Caller's file name.
     */
    String fileName = null;

    /**
     * Caller's fully qualified class name.
     */
    String className = null;

    /**
     * Caller's method name.
     */
    String methodName = null;

    /**
     * All available caller information, in the format
     * <code>fully.qualified.classname.of.caller.methodName(Filename.java:line)</code>
     */
    transient String fullInfo = null;

    /**
     * NA_LOCATION_INFO is used in conjunction with deserialized LogRecords
     * without real location info available.
     *
     * @since 0.5
     */
    public static final LocationInfo NA_LOCATION_INFO = new LocationInfo(Constants.NOT_AVAILABLE_CHAR,
            Constants.NOT_AVAILABLE_CHAR, Constants.NOT_AVAILABLE_CHAR,
            Constants.NOT_AVAILABLE_CHAR);

    // ------------------------------------------------------------ Constructor

    public LocationInfo(String fileName, String className, String methodName, String lineNumber) {
        this.fileName = fileName;
        this.className = className;
        this.methodName = methodName;
        this.lineNumber = lineNumber;
    }

    public LocationInfo(LogRecord record) {
        this.fileName = Constants.NOT_AVAILABLE_CHAR;
        this.className = record.getSourceClassName();
        this.methodName = record.getSourceMethodName();
        this.lineNumber = Constants.NOT_AVAILABLE_CHAR;
    }

    /**
     * Instantiate location information based on a Throwable. We expect the
     * Throwable <code>t</code>, to be in the format
     *
     * <pre>
     *
     *
     *
     *       java.lang.Throwable
     *       ...
     *       at org.apache.log4j.PatternLayout.format(PatternLayout.java:413)
     *       at org.apache.log4j.FileAppender.doAppend(FileAppender.java:183)
     *       at org.apache.log4j.Category.callAppenders(Category.java:131)
     *       at org.apache.log4j.Category.log(Category.java:512)
     *       at callers.fully.qualified.className.methodName(FileName.java:74)
     *       ...
     *
     *
     *
     * </pre>
     *
     * <p>
     * However, we can also deal with JIT compilers that "lose" the location
     * information, especially between the parentheses.
     *
     * @param t the throwable to check.
     * @param fqnOfInvokingClass the full qualified name of the caller. (In Juli
     *            varying).
     * @since 0.5
     */
    public LocationInfo(Throwable t, String fqnOfInvokingClass) {
        if (t == null) {
            return;
        }
        StackTraceElementExtractor.extract(this, t, fqnOfInvokingClass);
    }

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public boolean equals(Object o) {
        // LogLog.info("equals called");
        if (this == o) {
            return true;
        }

        if (!(o instanceof LocationInfo)) {
            // LogLog.info("inequality point 1");
            return false;
        }

        LocationInfo r = (LocationInfo) o;

        if (!getClassName().equals(r.getClassName())) {
            // LogLog.info("inequality point 2");
            return false;
        }

        if (!getFileName().equals(r.getFileName())) {
            // LogLog.info("inequality point 3");
            return false;
        }

        if (!getMethodName().equals(r.getMethodName())) {
            // LogLog.info("inequality point 4");
            return false;
        }

        if (!getLineNumber().equals(r.getLineNumber())) {
            // LogLog.info("inequality point 5");
            return false;
        }

        return true;
    }

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public int hashCode() {
        assert false : "hashCode not designed";
        final int answer_of_life = 42;
        return answer_of_life; // any arbitrary constant will do
    }

    /**
     * Return the fully qualified class name of the caller making the logging
     * request.
     * @since 0.5
     */
    public String getClassName() {
        return this.className;
    }

    /**
     * Return the file name of the caller.
     *
     * <p>
     * This information is not always available.
     * </p>
     * @return file name of the caller.
     * @since 0.5
     */
    public String getFileName() {
        return this.fileName;
    }

    /**
     * Returns the line number of the caller.
     *
     * <p>
     * This information is not always available.
     * </p>
     * @return line number of the caller.
     * @since 0.5
     */
    public String getLineNumber() {
        return this.lineNumber;
    }

    /**
     * Returns the method name of the caller.
     * @return method name of the caller.
     * @since 0.5
     */
    public String getMethodName() {
        return this.methodName;
    }

    /**
     * Returns the full info.
     * fullInfo format is:
     * <code>fully.qualified.classname.of.caller.methodName(Filename.java:line)</code>
     * @return full location info.
     * @since 0.5
     */
    public String getFullInfo() {
        if (this.fullInfo == null) {
            this.fullInfo = getClassName() + "." + getMethodName() + "(" + getFileName() + ":"
                    + getLineNumber() + ")";
        }
        return this.fullInfo;
    }

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public String toString() {
        return "(class=" + getClassName() + ", file=" + getFileName() + ", line=" + getLineNumber()
                + ", methodName=" + getMethodName();
    }
}
