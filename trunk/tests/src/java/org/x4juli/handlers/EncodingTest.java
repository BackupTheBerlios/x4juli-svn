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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.x4juli.formatter.PatternFormatter;
import org.x4juli.global.components.AbstractJuliTestCase;
import org.x4juli.global.helper.LoggerUtil;
import org.x4juli.global.util.BinaryCompare;
import org.x4juli.handlers.FileHandler;
import org.x4juli.logger.NOPLogger;

/**
 * Testcase for x4juli.
 * @since 0.5
 */
public class EncodingTest extends AbstractJuliTestCase {

    /**
     * 
     */
    public EncodingTest() {
        super();
    }

    /**
     * @param name
     */
    public EncodingTest(String name) {
        super(name);
    }
    
    /**
     * Test us-ascii encoding.
     * @throws Exception if test failure
     */
    public void testASCII() throws Exception {
        Logger logger = createLogger("output/ascii.log","US-ASCII");
        common(logger);
        boolean result= BinaryCompare.compare("output/ascii.log",
        "witness/encoding/ascii.log");
        assertTrue(result);
    }
    /**
     * Test iso-8859-1 encoding.
     * @throws Exception if test failure
     */
    public void testLatin1() throws Exception {
        Logger logger = createLogger("output/latin1.log", "iso-8859-1");
        common(logger);
        assertTrue(BinaryCompare.compare("output/latin1.log",
                "witness/encoding/latin1.log"));
    }

    /**
     * Test utf-8 encoding.
     * @throws Exception if test failure.
     */
    public void testUtf8() throws Exception {
        Logger logger = createLogger("output/UTF-8.log", "UTF-8");
        common(logger);
        assertTrue(BinaryCompare.compare("output/UTF-8.log",
                "witness/encoding/UTF-8.log"));
    }

    /**
     * Test utf-16 encoding.
     * @throws Exception if test failure.
     */
    public void testUtf16() throws Exception {
        Logger logger = createLogger("output/UTF-16.log", "UTF-16");
        common(logger);
        assertTrue(BinaryCompare.compare("output/UTF-16.log",
                "witness/encoding/UTF-16.log"));
    }

    /**
     * Test utf-16be encoding.
     * @throws Exception if test failure.
     */
    public void testUtf16BE() throws Exception {
        Logger logger = createLogger("output/UTF-16BE.log", "UTF-16BE");
        common(logger);
        assertTrue(BinaryCompare.compare("output/UTF-16BE.log",
                "witness/encoding/UTF-16BE.log"));
    }

    /**
     * Test utf16-le encoding.
     * @throws Exception if test failure.
     */
    public void testUtf16LE() throws Exception {
        Logger logger = createLogger("output/UTF-16LE.log", "UTF-16LE");
        common(logger);
        assertTrue(BinaryCompare.compare("output/UTF-16LE.log",
                "witness/encoding/UTF-16LE.log"));
    }
    /**
     * Common logging requests.
     * @param logger logger
     */
    private void common(final Logger logger) {
        logger.info("Hello, World");

        // pi can be encoded in iso-8859-1
        logger.info("\u00b9");

        //  one each from Latin, Arabic, Armenian, Bengali, CJK and Cyrillic
        logger.info("A\u0605\u0530\u0986\u4E03\u0400");
    }
    
    private Logger createLogger(final String filename, final String encoding){
        Logger logger = Logger.getLogger(encoding);
        FileHandler fh = new FileHandler(encoding);
        PatternFormatter pf = new PatternFormatter("%p - %m\n");
        pf.activateOptions();
        fh.setAppend(false);
        fh.setBufferedIO(false);
        fh.setImmediateFlush(true);
        try {
            fh.setEncoding(encoding);
        } catch (Exception e) {
            // Ignore Exception, to recognize it, use IDE Debugger Breakpoint in NOPLogger
            NOPLogger.NOP_LOGGER.log(Level.FINEST,"Ignored exception",e);
        } 
        fh.setFile(filename);
        LoggerUtil.configureLogger(logger, pf,fh,null,Level.ALL);
        return logger;
    }

}

// EOF EncodingTest.java
