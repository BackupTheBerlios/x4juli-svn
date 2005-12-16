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
package org.x4juli.handlers.rolling.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.x4juli.global.helper.IOUtil;
import org.x4juli.global.spi.ExtendedLogRecordImpl;
import org.x4juli.global.spi.ExtendedLogger;
import org.x4juli.handlers.MessageText;

/**
 * Compresses a file using Zip compression.
 * <p>
 * Logging API as a whole was originally done for <a
 * href="http://logging.apache.org/log4j/">Apache log4j</a>. <b>Juli</b> is a
 * port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging
 * APIs</a>. All credits for initial idea, design, implementation,
 * documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This
 * file was originally published by <i>Curt Arnold</i>. Please use exclusively
 * the <i>appropriate</i> mailing lists for questions, remarks and
 * contribution.
 * </p>
 * 
 * @author Boris Unckel
 * @since 0.5
 */
public class ZipCompressAction extends AbstractAction {

    // -------------------------------------------------------------- Variables

    /**
     * Source file.
     */
    private final File source;

    /**
     * Destination file.
     */
    private final File destination;

    /**
     * If true, attempt to delete file on completion.
     */
    private final boolean deleteSource;

    // ----------------------------------------------------------- Constructors

    /**
     * Create new instance of GZCompressAction.
     * 
     * @param source file to compress, may not be null.
     * @param destination compressed file, may not be null.
     * @param deleteSource if true, attempt to delete file on completion.
     *            Failure to delete does not cause an exception to be thrown or
     *            affect return value.
     * @since 0.5
     */
    public ZipCompressAction(final File source, final File destination, final boolean deleteSource) {
        if (source == null) {
            throw new NullPointerException("File source");
        }

        if (destination == null) {
            throw new NullPointerException("File destination");
        }

        this.source = source;
        this.destination = destination;
        this.deleteSource = deleteSource;
    }

    // --------------------------------------------------------- Public Methods
    /**
     * {@inheritDoc}
     */
    public boolean execute() throws IOException {
        return execute(this.source, this.destination, this.deleteSource, getLogger());
    }

    /**
     * Compress a file.
     * 
     * @param source file to compress, may not be null.
     * @param destination compressed file, may not be null.
     * @param deleteSource if true, attempt to delete file on completion.
     *            Failure to delete does not cause an exception to be thrown or
     *            affect return value.
     * @return true if source file compressed.
     * @throws IOException on IO exception.
     */
    public static boolean execute(final File source, final File destination,
            final boolean deleteSource, final ExtendedLogger logger) throws IOException {
        if (source.exists()) {
            FileInputStream fis = null;
            ZipOutputStream zos = null;
            try {
                fis = new FileInputStream(source);
                FileOutputStream fos = new FileOutputStream(destination);
                zos = new ZipOutputStream(fos);

                ZipEntry zipEntry = new ZipEntry(source.getName());
                zos.putNextEntry(zipEntry);

                byte[] inbuf = new byte[8102];
                int n;

                while ((n = fis.read(inbuf)) != -1) {
                    zos.write(inbuf, 0, n);
                }
            } finally {
                IOUtil.closeInputStream(fis);
                IOUtil.closeOutputStream(zos);
            }
            if (deleteSource) {
                if (!source.delete()) {
                    logger.log(Level.INFO, MessageText.Unable_to_delete, source);
                }
            }

            return true;
        }

        return false;
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Capture exception.
     * 
     * @param ex exception.
     */
    protected void reportException(final Exception ex) {
        LogRecord rec = new ExtendedLogRecordImpl(Level.INFO,MessageText.Exception_during_compression_of);
        rec.setParameters(new Object[]{this.source});
        rec.setThrown(ex);
        getLogger().log(rec);
    }
}

// EOF ZipCompressAction.java
