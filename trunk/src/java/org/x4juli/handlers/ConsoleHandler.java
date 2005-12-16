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
package org.x4juli.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;

/**
 * ConsoleAppender appends log events to <code>System.out</code> or
 * <code>System.err</code> using a layout specified by the user. The default
 * target is <code>System.out</code>. <table border="1" cellspacing="0"
 * cellpadding="2">
 * <tr>
 * <th valign="top" scope="col">Attribute</th>
 * <th valign="top" scope="col">Description</th>
 * <th valign="top" scope="col">Required</th>
 * </tr>
 * <tr>
 * <td valign="top">.follow</td>
 * <td valign="top">Sets whether the appender honors reassignments of
 * System.out or System.err made after configuration.</td>
 * <td valign="top">No. Default false.</td>
 * </tr>
 * <tr>
 * <td valign="top">.target</td>
 * <td valign="top">Recognized values are "System.out" and "System.err". Any
 * other value will be ignored.</td>
 * <td valign="top">No. Default value is System.out</td>
 * </tr>
 * </table>
 * <p>
 * Logging API as a whole was originally done for <a
 * href="http://logging.apache.org/log4j/">Apache log4j</a>. <b>Juli</b> is a
 * port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging
 * APIs</a>. All credits for initial idea, design, implementation,
 * documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This
 * file was originally published by <i>Ceki G&uuml;lc&uuml;, Curt Arnold</i>.
 * Please use exclusively the <i>appropriate</i> mailing lists for questions,
 * remarks and contribution.
 * </p>
 * 
 * @author Boris Unckel
 * @since 0.6
 */
public class ConsoleHandler extends WriterHandler {

    // -------------------------------------------------------------- Variables
    public static final String SYSTEM_OUT = "System.out";

    public static final String SYSTEM_ERR = "System.err";

    protected String target = SYSTEM_OUT;

    /**
     * Determines if the appender honors reassignments of System.out or
     * System.err made after configuration.
     */
    private boolean follow = false;

    // ----------------------------------------------------------- Constructors

    /**
     * 
     */
    public ConsoleHandler() {
        super();
    }

    /**
     * @param handlerName
     */
    public ConsoleHandler(String handlerName) {
        super(handlerName);
    }

    // --------------------------------------------------------- Public Methods

    /**
     * {@inheritDoc}
     * 
     * @since 0.6
     */
    public void configure() {
        super.configure();
        final String className = this.getClass().getName();

        // File append
        String key = className + ".follow";
        setFollow(getProperty(key, false));

        // File name
        key = className + ".target";
        String targetValue = getProperty(key, ConsoleHandler.SYSTEM_OUT);
        setTarget(targetValue);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.6
     */
    public void activateOptions() {
        if (this.follow) {
            if (this.target.equals(SYSTEM_ERR)) {
                setWriter(createWriter(new SystemErrStream()));
            } else {
                setWriter(createWriter(new SystemOutStream()));
            }
        } else {
            if (this.target.equals(SYSTEM_ERR)) {
                setWriter(createWriter(System.err));
            } else {
                setWriter(createWriter(System.out));
            }
        }
        super.activateOptions();
    }

    /**
     * Sets the value of the <b>Target</b> option. Recognized values are
     * "System.out" and "System.err". Any other value will be ignored.
     * 
     * @since 0.6
     */
    public void setTarget(final String value) {
        String v = value.trim();

        if (SYSTEM_OUT.equalsIgnoreCase(v)) {
            this.target = SYSTEM_OUT;
        } else if (SYSTEM_ERR.equalsIgnoreCase(v)) {
            this.target = SYSTEM_ERR;
        } else {
            getLogger().log(Level.WARNING, MessageText.Value_should_be_Systemout_or_System_err,
                    value);
            getLogger().log(Level.WARNING,
                    MessageText.Using_previously_set_target_Systemout_by_default, getTarget());
        }
    }

    /**
     * Returns the current value of the <b>Target</b> property. The default
     * value of the option is "System.out".
     * 
     * See also {@link #setTarget}.
     * 
     * @since 0.6
     */
    public String getTarget() {
        return this.target;
    }

    /**
     * Sets whether the appender honors reassignments of System.out or
     * System.err made after configuration.
     * 
     * @param newValue if true, appender will use value of System.out or
     *            System.err in force at the time when logging events are
     *            appended.
     * @since 0.6
     */
    public final void setFollow(final boolean newValue) {
        this.follow = newValue;
    }

    /**
     * Gets whether the appender honors reassignments of System.out or
     * System.err made after configuration.
     * 
     * @return true if appender will use value of System.out or System.err in
     *         force at the time when logging events are appended.
     * @since 0.6
     */
    public final boolean getFollow() {
        return this.follow;
    }

    /**
     * {@inheritDoc}
     * @since 0.6
     */
    public String getFullQualifiedClassName() {
        return "org.x4juli.handlers.ConsoleHandler";
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * {@inheritDoc}
     * 
     * @since 0.6
     */
    protected final void closeWriter() {
        if (this.follow) {
            super.closeWriter();
        }
    }

    /**
     * An implementation of OutputStream that redirects to the current
     * System.err.
     * 
     * @since 0.6
     */
    private static class SystemErrStream extends OutputStream {
        public SystemErrStream() {
            //NOP
        }

        public void close() {
            //NOP
        }

        public void flush() {
            System.err.flush();
        }

        public void write(final byte[] b) throws IOException {
            System.err.write(b);
        }

        public void write(final byte[] b, final int off, final int len) throws IOException {
            System.err.write(b, off, len);
        }

        public void write(final int b) throws IOException {
            System.err.write(b);
        }
    }

    /**
     * An implementation of OutputStream that redirects to the current
     * System.out.
     * 
     * @since 0.6
     */
    private static class SystemOutStream extends OutputStream {
        public SystemOutStream() {
            //NOP
        }

        public void close() {
            //NOP
        }

        public void flush() {
            System.out.flush();
        }

        public void write(final byte[] b) throws IOException {
            System.out.write(b);
        }

        public void write(final byte[] b, final int off, final int len) throws IOException {
            System.out.write(b, off, len);
        }

        public void write(final int b) throws IOException {
            System.out.write(b);
        }
    }

}

// EOF ConsoleHandler.java
