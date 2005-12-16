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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;

import org.x4juli.global.Constants;
import org.x4juli.global.helper.OptionConverter;
import org.x4juli.global.spi.ExtendedLogRecord;
import org.x4juli.global.spi.ExtendedLogRecordImpl;

/**
 * FileHandler publishes log records to a file. Please refer also to parent classes
 * for further configuration information. <table border="1" cellspacing="0"
 * cellpadding="2">
 * <tr>
 * <th valign="top" scope="col">Attribute</th>
 * <th valign="top" scope="col">Description</th>
 * <th valign="top" scope="col">Required</th>
 * </tr>
 * <tr>
 * <td valign="top">.append</td>
 * <td valign="top">Append to existing files. Allowed values "true" or "false".</td>
 * <td valign="top">No. Default false.</td>
 * </tr>
 * <tr>
 * <td valign="top">.filename</td>
 * <td valign="top">Full or relative Path and Name of the file.</td>
 * <td valign="top">YES. Default value of "user.home"/juli.log</td>
 * </tr>
 * <tr>
 * <td valign="top">.bufferedIO</td>
 * <td valign="top">To buffer write access to the file. Allowed values "true"
 * or "false".</td>
 * <td valign="top">No. Default false.</td>
 * </tr>
 * <tr>
 * <td valign="top">.buffersize</td>
 * <td valign="top">Size of the IO buffer. Allowed value is an integer.</td>
 * <td valign="top">No. Default 8kb.</td>
 * </tr>
 * </table>
 * 
 * <p>
 * <code>FileHandler</code> can be configured programattically or using
 * configuration by file. <b>Do not use the default constructor in
 * programattically use.</b>
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

public class FileHandler extends WriterHandler {
    // -------------------------------------------------------------- Variables

    /**
     * Append to or truncate the file? The default value for this variable is
     * <code>true</code>, meaning that by default a <code>FileAppender</code>
     * will append to an existing file and not truncate it.
     *
     * <p>
     * This option is meaningful only if the FileAppender opens the file.
     */
    protected boolean fileAppend;

    /**
     * The name of the log file.
     */
    protected String fileName;
    /**
     * Do we do bufferedIO?
     */
    protected boolean bufferedIO;

    /**
     * Default Size of an Buffered IO.
     */
    protected static final int DEFAULT_BUFFER_SIZE = 8 * 1024;

    /**
     * The size of the IO buffer. Default is 8K.
     */
    protected int bufferSize;

    // ----------------------------------------------------------- Constructors

    /**
     * Default Constructor instantiation used for configuration by file. This
     * automaticaly activatesOptions(). Avoid in programmatically use.
     */
    public FileHandler() {
        super();
    }

    /**
     * Utility Constructor. All properties must be set programmatically. Finally
     * you need to call actiavtesOptions().
     *
     * @param handlerName of the current instance.
     * @since 0.5
     */
    public FileHandler(String handlerName) {
        super(handlerName);
    }

    // --------------------------------------------------------- Public Methods

    /**
     * {@inheritDoc}
     *
     * @since 0.5
     */
    public String getFullQualifiedClassName() {
        return "org.x4juli.handlers.FileHandler";
    }

    /**
     * The <b>File</b> property takes a string value which should be the name
     * of the file to append to.
     *
     * <p>
     * Note: Actual opening of the file is made when {@link #activateOptions} is
     * called, not when the options are set.
     * </p>
     *
     * @param file name of the file to use.
     * @since 0.5
     */
    public void setFile(String file) {
        // Trim spaces from both ends. The users probably does not want
        // trailing spaces in file names.
        String val = file.trim();
        this.fileName = OptionConverter.stripDuplicateBackslashes(val);
    }

    /**
     * Returns the value of the <b>Append</b> option.
     *
     * @since 0.5
     */
    public boolean getAppend() {
        return this.fileAppend;
    }

    /**
     * Returns the value of the <b>File</b> option.
     *
     * @since 0.5
     */
    public synchronized String getFile() {
        return this.fileName;
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.5
     */
    public void activateOptions() {
        int errors = 0;
        if (this.fileName != null) {
            try {
                setFile(this.fileName, this.fileAppend, this.bufferedIO, this.bufferSize);
            } catch (java.io.IOException e) {
                errors++;
                ExtendedLogRecord record = new ExtendedLogRecordImpl(Level.SEVERE, MessageText.Set_file_call_failed);
                record.setParameters(new Object[] { this.fileName, Boolean.valueOf(this.fileAppend) });
                record.setThrown(e);
                getLogger().log(record);
            }
        } else {
            errors++;
            getLogger().log(Level.SEVERE, MessageText.File_option_not_set_for_handler, this.name);
        }
        if (errors == 0) {
            super.activateOptions();
        }

    }

    /**
     * {@inheritDoc}
     *
     * @since 0.5
     */
    public void configure() {
        super.configure();
        final String className = this.getClass().getName();

        // File append
        String key = className + ".append";
        setAppend(getProperty(key, false));

        // File name
        key = className + ".filename";
        String fileNameValue = getProperty(key, null);
        final String defaultFileName = Constants.DEFAULT_LOG_FILE + "-" + getName() + ".log";
        if(fileNameValue != null){
            setFile(fileNameValue);
        } else {
            getLogger().log(Level.WARNING,
                    MessageText.No_output_stream_or_file_set_for_the_handler_using_default,
                    new Object[]{this.name,defaultFileName});
            setFile(defaultFileName);
        }

        // Buffered IO
        key = className + ".bufferedIO";
        setBufferedIO(getProperty(key,false));

        // Buffersize
        key = className + ".buffersize";
        setBufferSize(getProperty(key,DEFAULT_BUFFER_SIZE));
    }

    /**
     * Get the value of the <b>BufferedIO</b> option.
     *
     * <p>
     * BufferedIO will significatnly increase performance on heavily loaded
     * systems.
     * </p>
     *
     * @since 0.5
     */
    public boolean getBufferedIO() {
        return this.bufferedIO;
    }

    /**
     * Get the size of the IO buffer.
     *
     * @since 0.5
     */
    public int getBufferSize() {
        return this.bufferSize;
    }

    /**
     * The <b>Append</b> option takes a boolean value. It is set to
     * <code>true</code> by default. If true, then <code>File</code> will be
     * opened in append mode by {@link #setFile(String)} (see above). Otherwise,
     * {@link #setFile(String)} will open <code>File</code> in truncate mode.
     *
     * <p>
     * Note: Actual opening of the file is made when {@link #activateOptions} is
     * called, not when the options are set.
     *
     * @param flag to append or not
     * @since 0.5
     */
    public void setAppend(final boolean flag) {
        this.fileAppend = flag;
    }

    /**
     * The <b>BufferedIO</b> option takes a boolean value. It is set to
     * <code>false</code> by default. If true, then <code>File</code> will
     * be opened and the resulting {@link java.io.Writer} wrapped around a
     * {@link BufferedWriter}.
     *
     * BufferedIO will significatnly increase performance on heavily loaded
     * systems.
     *
     * @param bufferedIO to buffer IO or not
     * @since 0.5
     */
    public void setBufferedIO(final boolean bufferedIO) {
        this.bufferedIO = bufferedIO;

        if (bufferedIO) {
            this.immediateFlush = false;
        }
    }

    /**
     * Set the size of the IO buffer.
     *
     * @param bufferSize size of the buffer, used if buffered IO is true
     * @since 0.5
     */
    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    /**
     * <p>
     * Sets and <i>opens</i> the file where the log output will go. The
     * specified file must be writable.
     * </p>
     * <p>
     * If there was already an opened file, then the previous file is closed
     * first.
     * </p>
     * <p>
     * <b>Do not use this method directly. To configure a FileAppender or one of
     * its subclasses, set its properties one by one and then call
     * activateOptions.</b>
     * </p>
     *
     * @param filename The path to the log file.
     * @param append If true will append to fileName. Otherwise will truncate
     *            fileName.
     * @param bufferedIO to buffer IO or not.
     * @param bufferSize size of the buffer for IO.
     *
     * @throws IOException if file does not exist and cannot be created.
     * @since 0.5
     *
     */
    public synchronized void setFile(String filename, boolean append, boolean bufferedIO,
            int bufferSize) throws IOException {
        getLogger().log(Level.FINER, MessageText.SetFile_called,
                new Object[] { this.fileName, Boolean.valueOf(append) });

        // It does not make sense to have immediate flush and bufferedIO.
        if (bufferedIO) {
            setImmediateFlush(false);
        }

        closeWriter();

        FileOutputStream ostream = null;
        try {
            //
            // attempt to create file
            //
            ostream = new FileOutputStream(filename, append);
        } catch (FileNotFoundException ex) {
            //
            // if parent directory does not exist then
            // attempt to create it and try to create file
            // see bug 9150
            //
            File parentDir = new File(new File(filename).getParent());
            if (!parentDir.exists() && parentDir.mkdirs()) {
                ostream = new FileOutputStream(filename, append);
            } else {
                throw ex;
            }
        }
        this.writer = createWriter(ostream);

        if (bufferedIO) {
            this.writer = new BufferedWriter(this.writer, bufferSize);
        }

        this.fileAppend = append;
        this.bufferedIO = bufferedIO;
        this.fileName = filename;
        this.bufferSize = bufferSize;
        writeHeader();
        getLogger().log(Level.FINER, MessageText.SetFile_ended);
    }

    // ------------------------------------------------------ Protected Methods


    // -------------------------------------------------------- Private Methods

}

// EOF FileHandler.java
