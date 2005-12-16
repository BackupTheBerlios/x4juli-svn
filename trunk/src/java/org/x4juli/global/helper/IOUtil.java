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

package org.x4juli.global.helper;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.util.logging.Level;

import org.x4juli.NOPLogger;

/**
 * Utility class for IO Operations. Currently for null safe close without exceptions.
 * @author Boris Unckel
 * @since 0.5
 */
public final class IOUtil {

    /**
     * No instantiation wanted.
     */
    private IOUtil() {
        //NOP
    }

    /**
     * Null safe close, does not throw any exception.
     * @param stream to close.
     * @since 0.5
     */
    public static void closeInputStream(final InputStream stream) {
        if (stream == null) {
            return;
        }
        try {
            stream.close();
        } catch (Exception e) {
            // Ignore Exception, to recognize it, use IDE Debugger Breakpoint in
            // NOPLogger
            NOPLogger.NOP_LOGGER.log(Level.FINEST, "Ignored exception", e);
        }
    }

    /**
     * Null safe close, does not throw any exception.
     * @param stream to close.
     * @since 0.5
     */
    public static void closeOutputStream(final OutputStream stream) {
        if (stream == null) {
            return;
        }
        try {
            stream.close();
        } catch (Exception e) {
            // Ignore Exception, to recognize it, use IDE Debugger Breakpoint in
            // NOPLogger
            NOPLogger.NOP_LOGGER.log(Level.FINEST, "Ignored exception", e);
        }
    }

    /**
     * Null safe close, does not throw any exception.
     * @param writer to close.
     * @since 0.5
     */
    public static void closeWriter(final Writer writer) {
        if (writer == null) {
            return;
        }
        try {
            writer.close();
        } catch (Exception e) {
            // Ignore Exception, to recognize it, use IDE Debugger Breakpoint in
            // NOPLogger
            NOPLogger.NOP_LOGGER.log(Level.FINEST, "Ignored exception", e);
        }
    }

    /**
     * Null safe close, does not throw any exception.
     * @param reader to close.
     * @since 0.5
     */
    public static void closeReader(final Reader reader) {
        if (reader == null) {
            return;
        }
        try {
            reader.close();
        } catch (Exception e) {
            // Ignore Exception, to recognize it, use IDE Debugger Breakpoint in
            // NOPLogger
            NOPLogger.NOP_LOGGER.log(Level.FINEST, "Ignored exception", e);
        }
    }
    
    /**
     * Null safe close, does not throw any exception.
     * @param socket to close.
     * @since 0.5
     */
    public static void closeSocket(final Socket socket) {
        if (socket == null) {
            return;
        }
        try {
            socket.close();
        } catch (Exception e) {
            // Ignore Exception, to recognize it, use IDE Debugger Breakpoint in
            // NOPLogger
            NOPLogger.NOP_LOGGER.log(Level.FINEST, "Ignored exception", e);
        }
    }
    

}

// EOF IOUtil.java
