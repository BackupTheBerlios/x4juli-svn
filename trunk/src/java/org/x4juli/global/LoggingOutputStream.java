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
package org.x4juli.global;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Delegates a System.out or System.err to a logger. By default, the original
 * output will be sent to System.out or System.err. This behavior can be turned
 * off with care (avoiding a circle of ConsoleHandler output and
 * LoggingOutstream input to the logging system.
 * 
 * @author Boris Unckel
 * @since 0.6
 */
class LoggingOutputStream extends OutputStream {

    /**
     * Used to maintain the contract of {@link #close()}.
     */
    protected boolean hasBeenClosed = false;

    /**
     * The internal buffer where data is stored.
     */
    protected byte[] buf;

    /**
     * The number of valid bytes in the buffer. This value is always in the
     * range <tt>0</tt> through <tt>buf.length</tt>; elements
     * <tt>buf[0]</tt> through <tt>buf[count-1]</tt> contain valid byte
     * data.
     */
    protected int count;

    /**
     * Remembers the size of the buffer for speed.
     */
    private int bufLength;

    /**
     * The default number of bytes in the buffer. =2048
     */
    public static final int DEFAULT_BUFFER_LENGTH = 2048;

    /**
     * The Logger to write to.
     */
    protected Logger logger;

    /**
     * The level to use when writing to the Category.
     */
    protected Level level;

    private LoggingOutputStream() {
        // illegal
    }

    /**
     * Creates the LoggingOutputStream to flush to the given Category.
     * 
     * @param logger the Logger to write to
     * 
     * @param level the Level to use when writing to the Category
     * 
     * @exception IllegalArgumentException if cat == null or priority == null
     */
    public LoggingOutputStream(final Logger logger, final Level level)
            throws IllegalArgumentException {
        if (logger == null) {
            throw new IllegalArgumentException("cat == null");
        }
        if (level == null) {
            throw new IllegalArgumentException("priority == null");
        }

        this.logger = logger;
        this.level = level;
        this.bufLength = DEFAULT_BUFFER_LENGTH;
        this.buf = new byte[DEFAULT_BUFFER_LENGTH];
        this.count = 0;
    }

    /**
     * Closes this output stream and releases any system resources associated
     * with this stream. The general contract of <code>close</code> is that it
     * closes the output stream. A closed stream cannot perform output
     * operations and cannot be reopened.
     */
    public void close() {
        flush();
        this.hasBeenClosed = true;
    }

    /**
     * Writes the specified byte to this output stream. The general contract for
     * <code>write</code> is that one byte is written to the output stream.
     * The byte to be written is the eight low-order bits of the argument
     * <code>b</code>. The 24 high-order bits of <code>b</code> are
     * ignored.
     * 
     * @param b the <code>byte</code> to write
     * 
     * @exception IOException if an I/O error occurs. In particular, an
     *                <code>IOException</code> may be thrown if the output
     *                stream has been closed.
     */
    public void write(final int b) throws IOException {
        if (this.hasBeenClosed) {
            throw new IOException("The stream has been closed.");
        }

        // don't log nulls
        if (b == 0) {
            return;
        }

        // would this be writing past the buffer?
        if (this.count == this.bufLength) {
            // grow the buffer
            final int newBufLength = this.bufLength + DEFAULT_BUFFER_LENGTH;
            final byte[] newBuf = new byte[newBufLength];

            System.arraycopy(this.buf, 0, newBuf, 0, this.bufLength);

            this.buf = newBuf;
            this.bufLength = newBufLength;
        }

        this.buf[this.count] = (byte) b;
        this.count++;
    }

    /**
     * Flushes this output stream and forces any buffered output bytes to be
     * written out. The general contract of <code>flush</code> is that calling
     * it is an indication that, if any bytes previously written have been
     * buffered by the implementation of the output stream, such bytes should
     * immediately be written to their intended destination.
     */
    public void flush() {
        if (this.count == 0) {
            return;
        }

        // don't print out blank lines; flushing from PrintStream puts out these
        if (this.count == Constants.LINE_SEP_LEN) {
            if (((char) this.buf[0]) == SystemUtils.LINE_SEPARATOR.charAt(0) && ((this.count == 1) || // <-
                                                                                    // Unix
                                                                                    // &
                                                                                    // Mac,
                                                                                    // ->
                                                                                    // Windows
                    ((this.count == 2) && ((char) this.buf[1]) == SystemUtils.LINE_SEPARATOR.charAt(1)))) {
                reset();
                return;
            }
        }

        final byte[] theBytes = new byte[this.count];

        System.arraycopy(this.buf, 0, theBytes, 0, this.count);

        this.logger.log(this.level, String.valueOf(theBytes));

        reset();
    }

    private void reset() {
        // not resetting the buffer -- assuming that if it grew that it
        // will likely grow similarly again
        this.count = 0;
    }
}

// EOF LoggingOutputStream.java
