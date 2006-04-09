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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.logging.Level;

import org.x4juli.global.helper.Loader;
import org.x4juli.global.spi.ExtendedLogRecord;
import org.x4juli.handlers.rolling.RollingPolicy;
import org.x4juli.handlers.rolling.RolloverDescription;
import org.x4juli.handlers.rolling.TimeBasedRollingPolicy;
import org.x4juli.handlers.rolling.TriggeringPolicy;
import org.x4juli.handlers.rolling.helper.Action;
import org.x4juli.handlers.rolling.nop.NOPRollingPolicy;
import org.x4juli.handlers.rolling.nop.NOPTriggeringPolicy;

/**
 * <code>RollingFileHandler</code> extends {@link FileHandler} to backup the
 * log files depending on {@link RollingPolicy} and {@link TriggeringPolicy}.
 * <p>
 * To be of any use, a <code>RollingFileHandler</code> instance must have both
 * a <code>RollingPolicy</code> and a <code>TriggeringPolicy</code> set up.
 * However, if its <code>RollingPolicy</code> also implements the
 * <code>TriggeringPolicy</code> interface, then only the former needs to be
 * set up. For example, {@link TimeBasedRollingPolicy} acts both as a
 * <code>RollingPolicy</code> and a <code>TriggeringPolicy</code>.
 * <p>
 * <code>RollingFileHandler</code> can be configured programattically or using
 * configuration by file. <b>Do not use the default constructor in programattically use.</b>
 * </p>
 * 
 * <table border="1" cellspacing="0" cellpadding="2">
 * <tr>
 * <th valign="top" scope="col">Attribute</th>
 * <th valign="top" scope="col">Description</th>
 * <th valign="top" scope="col">Required</th>
 * </tr>
 * <tr>
 * <td valign="top">.triggeringPolicy</td>
 * <td valign="top">The triggering policy is needed to say when or if there is
 * a rolling needed. Value is the full qualified classname of the triggering
 * policy.</td>
 * <td valign="top">YES. The default will lead to fatal errors or in minimum no
 * rolling.</td>
 * </tr>
 * <tr>
 * <td valign="top">.rollingPolicy</td>
 * <td valign="top">The rolling policy specifies what to do for rollover. Value
 * is the full qualified classname of the rolling policy.</td>
 * <td valign="top">YES. The default will lead to fatal errors or in minimum no
 * rolling.</td>
 * </tr>
 * </table>
 *
 * <p>
 * Logging API as a whole was originally done for <a
 * href="http://logging.apache.org/log4j/">Apache log4j</a>. <b>Juli</b> is a
 * port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging
 * APIs</a>. All credits for initial idea, design, implementation,
 * documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This
 * file was originally published by <i>Heinz Richter, Ceki G&uuml;lc&uuml;</i>.
 * Please use exclusively the <i>appropriate</i> mailing lists for questions,
 * remarks and contribution.
 * </p>
 *
 * @author Boris Unckel
 * @since 0.5
 */
public final class RollingFileHandler extends FileHandler {

    // -------------------------------------------------------------- Variables
    /**
     * Triggering policy.
     */
    private TriggeringPolicy triggeringPolicy;

    /**
     * Rolling policy.
     */
    private RollingPolicy rollingPolicy;

    /**
     * Length of current active log file.
     */
    private long fileLength;

    /**
     * Asynchronous action (like compression) from previous rollover.
     */
    private Action lastRolloverAsyncAction = null;

    // ----------------------------------------------------------- Constructors

    /**
     * Default constructor, does not configure or activateOptions.
     * @since 0.7
     */
    public RollingFileHandler() {
        super();
    }

    /**
     * Utility constructor, does not configure or activateOptions.
     * @param handlerName of this instance.
     * @since 0.5
     */
    public RollingFileHandler(String handlerName) {
        super(handlerName);
    }

    // --------------------------------------------------------- Public Methods

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public String getFullQualifiedClassName() {
        return "org.x4juli.handlers.RollingFileHandler";
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.5
     */
    public void activateOptions() {
        if (this.rollingPolicy == null) {
            getLogger().log(Level.WARNING, MessageText.Please_set_a_rolling_policy, this.name);
            return;
        }

        //
        // if no explicit triggering policy and rolling policy is both.
        //
        if ((this.triggeringPolicy == null) && this.rollingPolicy instanceof TriggeringPolicy) {
            this.triggeringPolicy = (TriggeringPolicy) this.rollingPolicy;
        }

        if (this.triggeringPolicy == null) {
            getLogger().log(Level.WARNING,
                    MessageText.Please_set_a_TriggeringPolicy_for_the_RollingFileHandler, this.name);
            return;
        }

        synchronized (this) {
            this.triggeringPolicy.activateOptions();
            this.rollingPolicy.activateOptions();
            try {
                RolloverDescription rollover = this.rollingPolicy.initialize(getFile(), getAppend());

                if (rollover != null) {
                    Action syncAction = rollover.getSynchronous();

                    if (syncAction != null) {
                        syncAction.execute();
                    }

                    setFile(rollover.getActiveFileName());
                    setAppend(rollover.getAppend());
                    this.lastRolloverAsyncAction = rollover.getAsynchronous();

                    if (this.lastRolloverAsyncAction != null) {
                        Thread runner = new Thread(this.lastRolloverAsyncAction);
                        runner.start();
                    }
                }

                File activeFile = new File(getFile());

                if (getAppend()) {
                    this.fileLength = activeFile.length();
                } else {
                    this.fileLength = 0;
                }

                super.activateOptions();
            } catch (Exception ex) {
                getLogger().log(Level.WARNING, MessageText.Exception_is, ex);
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.5
     */
    protected void subAppend(final ExtendedLogRecord record) {
        // The rollover check must precede actual writing. This is the
        // only correct behavior for time driven triggers.
        if (this.triggeringPolicy.isTriggeringEvent(this, record, getFile(), getFileLength())) {
            //
            // wrap rollover request in try block since
            // rollover may fail in case read access to directory
            // is not provided. However appender should still be in good
            // condition and the append should still happen.
            try {
                //System.out.println("START RollingFileHandler Active FileName is["+this.fileName+"]");
                rollover();
                //System.out.println("END RollingFileHandler Active FileName is["+this.fileName+"]");
                //System.out.flush();
            } catch (Exception ex) {
                getLogger().log(Level.INFO, MessageText.Exception_during_rollover_attempt, ex);
            }
        }

        super.subAppend(record);
    }

    /**
     * Implements the usual roll over behaviour.
     *
     * <p>
     * If <code>MaxBackupIndex</code> is positive, then files {<code>File.1</code>,
     * ..., <code>File.MaxBackupIndex -1</code>} are renamed to {<code>File.2</code>,
     * ..., <code>File.MaxBackupIndex</code>}. Moreover, <code>File</code>
     * is renamed <code>File.1</code> and closed. A new <code>File</code> is
     * created to receive further log output.
     *
     * <p>
     * If <code>MaxBackupIndex</code> is equal to zero, then the
     * <code>File</code> is truncated with no backup files created.
     *
     * @return true if rollover performed.
     * @since 0.5
     */
    public boolean rollover() {
        //
        // can't roll without a policy
        //
        if (this.rollingPolicy != null) {
            Exception exception = null;

            synchronized (this) {
                //
                // if a previous async task is still running
                // }
                if (this.lastRolloverAsyncAction != null) {
                    //
                    // block until complete
                    //
                    this.lastRolloverAsyncAction.close();

                    //
                    // or don't block and return to rollover later
                    //
                    // if (!lastRolloverAsyncAction.isComplete()) return false;
                }

                try {
                    RolloverDescription rollover = this.rollingPolicy.rollover(getFile());

                    if (rollover != null) {
                        if (rollover.getActiveFileName().equals(getFile())) {
                            close();

                            boolean success = true;

                            if (rollover.getSynchronous() != null) {
                                success = false;

                                try {
                                    success = rollover.getSynchronous().execute();
                                } catch (Exception ex) {
                                    exception = ex;
                                }
                            }

                            if (success) {
                                if (rollover.getAppend()) {
                                    this.fileLength = new File(rollover.getActiveFileName()).length();
                                } else {
                                    this.fileLength = 0;
                                }

                                if (rollover.getAsynchronous() != null) {
                                    this.lastRolloverAsyncAction = rollover.getAsynchronous();
                                    new Thread(this.lastRolloverAsyncAction).start();
                                }

                                setFile(rollover.getActiveFileName(), rollover.getAppend(),
                                        this.bufferedIO, this.bufferSize);
                            } else {
                                setFile(rollover.getActiveFileName(), true, this.bufferedIO, this.bufferSize);

                                if (exception != null) {
                                    getLogger().log(Level.WARNING,
                                            MessageText.Exception_in_post_close_rollover_action,
                                            exception);
                                }
                            }
                            this.closed = false;
                        } else {
                            Writer newWriter = createWriter(new FileOutputStream(rollover
                                    .getActiveFileName(), rollover.getAppend()));
                            close();
                            setFile(rollover.getActiveFileName());
                            this.writer = newWriter;

                            boolean success = true;

                            if (rollover.getSynchronous() != null) {
                                success = false;

                                try {
                                    success = rollover.getSynchronous().execute();
                                } catch (Exception ex) {
                                    exception = ex;
                                }
                            }

                            if (success) {
                                if (rollover.getAppend()) {
                                    this.fileLength = new File(rollover.getActiveFileName()).length();
                                } else {
                                    this.fileLength = 0;
                                }

                                if (rollover.getAsynchronous() != null) {
                                    this.lastRolloverAsyncAction = rollover.getAsynchronous();
                                    new Thread(this.lastRolloverAsyncAction).start();
                                }
                            }
                            this.closed = false;
                            writeHeader();
                        }

                        return true;
                    }
                } catch (Exception ex) {
                    exception = ex;
                }
            }

            if (exception != null) {
                getLogger().log(Level.WARNING,
                        MessageText.Exception_during_rollover_rollover_deferred, exception);
            }
        }

        return false;
    }

    /**
     * Get rolling policy.
     *
     * @return rolling policy.
     * @since 0.5
     */
    public RollingPolicy getRollingPolicy() {
        return this.rollingPolicy;
    }

    /**
     * Get triggering policy.
     *
     * @return triggering policy.
     * @since 0.5
     */
    public TriggeringPolicy getTriggeringPolicy() {
        return this.triggeringPolicy;
    }

    /**
     * Sets the rolling policy.
     *
     * @param policy rolling policy.
     * @since 0.5
     */
    public void setRollingPolicy(final RollingPolicy policy) {
        this.rollingPolicy = policy;
    }

    /**
     * Set triggering policy.
     *
     * @param policy triggering policy.
     * @since 0.5
     */
    public void setTriggeringPolicy(final TriggeringPolicy policy) {
        this.triggeringPolicy = policy;
    }

    /**
     * Close appender. Waits for any asynchronous file compression actions to be
     * completed.
     *
     * @since 0.5
     */
    public void close() {
        synchronized (this) {
            if (this.lastRolloverAsyncAction != null) {
                this.lastRolloverAsyncAction.close();
            }
        }
        super.close();
    }

    /**
     * Get byte length of current active log file.
     *
     * @return byte length of current active log file.
     * @since 0.5
     */
    public synchronized long getFileLength() {
        return this.fileLength;
    }

    /**
     * Increments estimated byte length of current active log file.
     *
     * @param increment additional bytes written to log file.
     * @since 0.5
     */
    public synchronized void incrementFileLength(int increment) {
        this.fileLength += increment;
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Returns an OutputStreamWriter when passed an OutputStream. The encoding
     * used will depend on the value of the <code>encoding</code> property. If
     * the encoding value is specified incorrectly the writer will be opened
     * using the default system encoding (an error message will be printed to
     * the loglog.
     *
     * @param os output stream, may not be null.
     * @return new writer.
     * @since 0.5
     */
    protected OutputStreamWriter createWriter(final OutputStream os) {
        return super.createWriter(new CountingOutputStream(os, this));
    }

    // -------------------------------------------------------- Private Methods

    // ---------------------------------------------------------- Inner Classes

    /**
     * Wrapper for OutputStream that will report all write operations back to
     * this class for file length calculations.
     *
     * @since 0.5
     */
    private static class CountingOutputStream extends OutputStream {
        /**
         * Wrapped output stream.
         */
        private final OutputStream os;

        /**
         * Rolling file appender to inform of stream writes.
         */
        private final RollingFileHandler rfa;

        /**
         * Constructor.
         *
         * @param os output stream to wrap.
         * @param rfa rolling file appender to inform.
         */
        public CountingOutputStream(final OutputStream os, final RollingFileHandler rfa) {
            this.os = os;
            this.rfa = rfa;
        }

        /**
         * {@inheritDoc}
         *
         * @since 0.5
         */
        public void close() throws IOException {
            this.os.close();
        }

        /**
         * {@inheritDoc}
         *
         * @since 0.5
         */
        public void flush() throws IOException {
            this.os.flush();
        }

        /**
         * {@inheritDoc}
         *
         * @since 0.5
         */
        public void write(final byte[] b) throws IOException {
            this.os.write(b);
            this.rfa.incrementFileLength(b.length);
        }

        /**
         * {@inheritDoc}
         *
         * @since 0.5
         */
        public void write(final byte[] b, final int off, final int len) throws IOException {
            this.os.write(b, off, len);
            this.rfa.incrementFileLength(len);
        }

        /**
         * {@inheritDoc}
         *
         * @since 0.5
         */
        public void write(final int b) throws IOException {
            this.os.write(b);
            this.rfa.incrementFileLength(1);
        }
    }

}

// EOF RollingFileHandler.java
