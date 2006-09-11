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
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.x4juli.global.SystemUtils;
import org.x4juli.global.helper.IOUtil;
import org.x4juli.global.spi.ExtendedLogRecord;
import org.x4juli.global.spi.ExtendedLogRecordImpl;
import org.x4juli.global.spi.ThrowableInformation;
import org.x4juli.logger.NOPLogger;

/**
 * WriterHandler appends log events to a {@link java.io.Writer}.
 * 
 * <table border="1" cellspacing="0" cellpadding="2">
 * <tr>
 * <th valign="top" scope="col">Attribute</th>
 * <th valign="top" scope="col">Description</th>
 * <th valign="top" scope="col">Required</th>
 * </tr>
 * <tr>
 * <td valign="top">.autoflush</td>
 * <td valign="top">Flush the writer after every record publishing or not.
 * Should correspond to buffered IO. Allowed values "true" or "false".</td>
 * <td valign="top">No. Default is true.</td>
 * </tr>
 * <tr>
 * <td valign="top">.encoding</td>
 * <td valign="top">Specifies the encoding of the logfile. Allowed are all
 * supported encodings of the platform. <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/intl/encoding.doc.html"
 * title="Supported Encodings">Supported Encodings</a></td>
 * <td valign="top">No. Default is UTF-8</td>
 * </tr>
 * <tr>
 * <td valign="top">.writer</td>
 * <td valign="top">Writer to publish logrecords to. Value is the full
 * qualified classname of the writer. Do not specifiy any writer if you are
 * using a subclass (FileHandler, RollingFileHandler).</td>
 * <td valign="top">YES. Default is <code>null</code>, which will lead to
 * errors.</td>
 * </tr>
 * </table>
 *
 * <p>
 * <code>WriterHandler</code> can be configured programattically or using
 * configuration by file. <b>Do not use the default constructor in programattically use.</b>
 * </p>
 *
 * <p>
 * Logging API as a whole was originally done for <a
 * href="http://logging.apache.org/log4j/">Apache log4j</a>. <b>Juli</b> is a
 * port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging
 * APIs</a>. All credits for initial idea, design, implementation,
 * documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This
 * file was originally published by <i>Ceki G&uuml;lc&uuml;</i>. Please use
 * exclusively the <i>appropriate</i> mailing lists for questions, remarks and
 * contribution.
 * </p>
 *
 * @author Boris Unckel
 * @since 0.5
 */
public class WriterHandler extends AbstractHandler {

    // -------------------------------------------------------------- Variables

    /**
     * Immediate flush means that the underlying writer or output stream will be
     * flushed at the end of each append operation. Immediate flush is slower
     * but ensures that each append request is actually written. If
     * <code>immediateFlush</code> is set to <code>false</code>, then there
     * is a good chance that the last few logs events are not actually written
     * to persistent media if and when the application crashes.
     *
     * <p>
     * The <code>immediateFlush</code> variable is set to <code>true</code>
     * by default.
     * </p>
     */
    protected boolean immediateFlush;

    /**
     * This is the {@link Writer} where we will write to.
     */
    protected Writer writer;

    // ----------------------------------------------------------- Constructors

    /**
     * Default constructor, does not configure or activateOptions.
     * @since 0.7
     */
    public WriterHandler() {
        super();
    }

    /**
     * Utility constructor, does not configure or activateOptions.
     * @param handlerName of this instance.
     * @since 0.5
     */
    public WriterHandler(String handlerName) {
        super(handlerName);
    }

    // --------------------------------------------------------- Public Methods

    /**
     * {@inheritDoc}
     */
    public synchronized void close() throws SecurityException {
        if (this.closed) {
            return;
        }
        this.closed = true;
        closeWriter();

    }

    /**
     * Close the underlying {@link java.io.Writer}.
     */
    protected void closeWriter() {
        try {
            if (this.writer != null) {
                // before closing we have to output out layout's footer
                writeFooter();
                flush();
            }
        } finally {
            IOUtil.closeWriter(this.writer);
            this.writer = null;
        }

    }

    /**
     * {@inheritDoc}
     */
    public void flush() {
        if (this.writer != null) {
            try {
                this.writer.flush();
            } catch (IOException e) {
                this.active = false;
                ExtendedLogRecord record = new ExtendedLogRecordImpl(Level.WARNING,MessageText.Flush_operation_failed_in_handler);
                record.setParameters(new Object[]{this.name});
                record.setThrown(e);
                getLogger().log(record);
            }
        }
    }

    /**
     * <p>
     * Sets the Writer where the log output will go. The specified Writer must
     * be opened by the user and be writable.
     *
     * <p>
     * The <code>java.io.Writer</code> will be closed when the appender
     * instance is closed.
     *
     *
     * <p>
     * <b>WARNING:</b> Logging to an unopened Writer will fail.
     * <p>
     *
     * @param writer An already opened Writer.
     * @since 0.5
     */
    public synchronized void setWriter(Writer writer) {
        // close any previously opened writer
        close();

        this.writer = writer;
        // this.tp = new TracerPrintWriter(qw);
        writeHeader();
    }

    /**
     * Returns an OutputStreamWriter when passed an OutputStream. The encoding
     * used will depend on the value of the <code>encoding</code> property. If
     * the encoding value is specified incorrectly the writer will be opened
     * using the default system encoding (an error message will be printed to
     * the x4juli internal logging.
     *
     * @param os to wrap.
     * @return writer wrapping the outputstream.
     * @since 0.5
     */
    protected OutputStreamWriter createWriter(OutputStream os) {
        OutputStreamWriter retval = null;

        String enc = getEncoding();

        if (enc != null) {
            try {
                retval = new OutputStreamWriter(os, enc);
            } catch (IOException e) {
                getLogger().log(Level.WARNING, MessageText.Error_initializing_output_writer);
                getLogger().log(Level.WARNING, MessageText.Unsupported_encoding, enc);
            }
        }

        if (retval == null) {
            retval = new OutputStreamWriter(os);
        }

        return retval;
    }

    /**
     * If the <b>ImmediateFlush</b> option is set to <code>true</code>, the
     * appender will flush at the end of each write. This is the default
     * behavior. If the option is set to <code>false</code>, then the
     * underlying stream can defer writing to physical medium to a later time.
     *
     * <p>
     * Avoiding the flush operation at the end of each append results in a
     * performance gain of 10 to 20 percent. However, there is safety tradeoff
     * involved in skipping flushing. Indeed, when flushing is skipped, then it
     * is likely that the last few log events will not be recorded on disk when
     * the application exits. This is a high price to pay even for a 20%
     * performance gain.
     * </p>
     * @param value ImmediateFlush option.
     * @since 0.5
     */
    public void setImmediateFlush(boolean value) {
        this.immediateFlush = value;
    }

    /**
     * Returns value of the <b>ImmediateFlush</b> option.
     * @return ImmediateFlush option.
     * @since 0.5
     */
    public boolean getImmediateFlush() {
        return this.immediateFlush;
    }

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public void activateOptions() {
        int errors = 0;
        if (this.extFormatter == null && getFormatter() == null) {
            getLogger().log(Level.SEVERE, MessageText.No_formatter_set_for_the_appender, this.name);
            errors++;
        }

        if (this.writer == null) {
            getLogger().log(Level.SEVERE, MessageText.No_writer_set_for_the_appender, this.name);
            errors++;
        }

        // only error free appenders should be activated
        if (errors == 0) {
            super.activateOptions();
        }
    }

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public String getFullQualifiedClassName() {
        return "org.x4juli.handlers.WriterHandler";
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * {@inheritDoc}
     */
    protected void appendLogRecord(final ExtendedLogRecord record) {
        // publish(LogRecord record)
        // - create ExtendedLogRecord
        // - publish(ExtendedLogRecord record);
        // --- check level
        // --- filter
        // --- appendLogRecord(ExtendedLogRecord record);
        // ----- checkEntryConditions();
        // ----- subAppend();
        if (!checkEntryConditions()) {
            return;
        }

        subAppend(record);
    }

    /**
     * Actual writing occurs here.
     * <p>
     * Most subclasses of <code>WriterAppender</code> will need to override
     * this method.
     * </p>
     * @param record to write.
     * @since 0.5
     */
    protected void subAppend(final ExtendedLogRecord record) {
        if (!isActive()) {
            return;
        }

        try {
            String toAppend = null;
            // Case: There is an ExtendedFormatter
            if (this.extFormatter != null) {
                toAppend = this.extFormatter.format(record);

                this.writer.write(toAppend);

                if (this.extFormatter.ignoresThrowable()) {
                    ThrowableInformation ti = record.getThrowableInformation();
                    if (ti != null) {
                        String[] s = ti.getThrowableStrRep();
                        if (s != null) {
                            int len = s.length;

                            for (int i = 0; i < len; i++) {
                                this.writer.write(s[i]);
                                this.writer.write(SystemUtils.LINE_SEPARATOR);
                            }
                        }
                    }
                }
            } else {
                // Case: There is not an ExtendedFormatter but
                // java.util.logging.Formatter
                toAppend = getFormatter().format((LogRecord) record);
                this.writer.write(toAppend);
            }

            if (this.immediateFlush) {
                this.writer.flush();
            }
        } catch (IOException ioe) {
            this.active = false;
            ExtendedLogRecord rec = new ExtendedLogRecordImpl(Level.SEVERE, MessageText.IO_failure_for_handler_named);
            record.setParameters(new Object[]{this.name});
            record.setThrown(ioe);
            getNonFloodingLogger().log(rec);
        }
    }

    /**
     * This method determines if there is a sense in attempting to append.
     *
     * <p>
     * It checks whether there is a set output target and also if there is a set
     * layout. If these checks fail, then the boolean value <code>false</code>
     * is returned.
     * </p>
     * @return appending is ok or not.
     * @since 0.5
     */
    protected boolean checkEntryConditions() {
        if (this.closed) {
            getNonFloodingLogger().log(Level.WARNING,
                    MessageText.Not_allowed_to_write_to_a_closed_handler, this.name);
            return false;
        }

        if (this.writer == null) {
            getNonFloodingLogger().log(Level.SEVERE,
                    MessageText.No_output_stream_or_file_set_for_the_handler, this.name);
            return false;
        }
        return true;
    }

    /**
     * Write a footer as produced by the embedded formatter's
     * {@link Formatter#getTail(java.util.logging.Handler)} method.
     * @since 0.5
     */
    protected void writeFooter() {
        if (getFormatter() != null) {
            String f = getFormatter().getTail(this);

            if ((f != null) && (this.writer != null)) {
                try {
                    this.writer.write(f);
                } catch (IOException ioe) {
                    this.active = false;
                    ExtendedLogRecord record = new ExtendedLogRecordImpl(Level.SEVERE, MessageText.Failed_to_write_footer_for_Handler);
                    record.setParameters(new Object[]{this.name});
                    record.setThrown(ioe);
                    getLogger().log(record);
                }
            }
        }
    }

    /**
     * Write a header as produced by the embedded formatter's
     * {@link Formatter#getHead(java.util.logging.Handler)} method.
     * @since 0.5
     */
    protected void writeHeader() {
        if (getFormatter() != null) {
            String h = getFormatter().getHead(this);

            if ((h != null) && (this.writer != null)) {
                try {
                    this.writer.write(h);
                } catch (IOException ioe) {
                    this.active = false;
                    ExtendedLogRecord record = new ExtendedLogRecordImpl(Level.SEVERE, MessageText.Failed_to_write_header_for_Handler);
                    record.setParameters(new Object[]{this.name});
                    record.setThrown(ioe);
                    getLogger().log(record);
                }
            }
        }
    }
}

// EOF WriterAppender.java
