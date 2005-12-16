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
package org.x4juli;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.x4juli.formatter.SimpleFormatter;
import org.x4juli.global.components.AbstractJuliTestCase;
import org.x4juli.global.helper.LoggerUtil;
import org.x4juli.global.util.Compare;
import org.x4juli.global.util.Filter;
import org.x4juli.global.util.JunitTestRunnerFilter;
import org.x4juli.global.util.LineNumberFilter;
import org.x4juli.global.util.SunReflectFilter;
import org.x4juli.global.util.Transformer;
import org.x4juli.handlers.FileHandler;


/**
 * Testcase for x4juli.
 * @since 0.5
 */
public class MinimumTest extends AbstractJuliTestCase {
    static String FILTERED = "output/filtered";

    static String EXCEPTION1 = "java.lang.Exception: Just testing";

    static String EXCEPTION2 = "\\s*at .*\\(.*:\\d{1,4}\\)";

    static String EXCEPTION3 = "\\s*at .*\\(Native Method\\)";

    // 18 fevr. 2002 20:02:41,551 [main] FATAL ERR - Message 0
    static String TTCC_PAT = Filter.ABSOLUTE_DATE_AND_TIME_PAT
            + " \\[main]\\ (DEBUG|INFO|WARN|ERROR|FATAL) .* - Message \\d{1,2}";

    static String TTCC2_PAT = Filter.ABSOLUTE_DATE_AND_TIME_PAT
            + " \\[main]\\ (DEBUG|INFO|WARN|ERROR|FATAL) .* - Messages should bear numbers 0 through 23\\.";

    /**
     * 
     */
    public MinimumTest() {
        super();
    }

    /**
     * @param name
     */
    public MinimumTest(String name) {
        super(name);
    }
    
    public void testSimple() throws Exception {
        Logger minimumTest = Logger.getLogger("MinimumTest");
        minimumTest.setLevel(Level.INFO);
        minimumTest.setUseParentHandlers(false);
        SimpleFormatter pf = new SimpleFormatter();
        FileHandler fh = new FileHandler("foo");
        fh.setFormatter(pf);
        fh.setAppend(false);
        fh.setBufferedIO(false);
        fh.setImmediateFlush(true);
        fh.setFile("output/simple");
        fh.activateOptions();
        LoggerUtil.removeAllHandlers(minimumTest);
        minimumTest.addHandler(fh);
        
        common();

        Transformer.transform(
          "output/simple", FILTERED,
          new Filter[] { new LineNumberFilter(), new SunReflectFilter(), 
                         new JunitTestRunnerFilter() });

        assertTrue(Compare.compare(FILTERED, "witness/simple"));
      }

    private void common() {

        // In the lines below, the category names are chosen as an aid in
        // remembering their level values. In general, the category names
        // have no bearing to level values.
        Logger ERR = Logger.getLogger("MinimumTest.SEV");
        ERR.setLevel(Level.SEVERE);
        ERR.setUseParentHandlers(true);

        Logger INF = Logger.getLogger("MinimumTest.INF");
        INF.setLevel(Level.INFO);
        INF.setUseParentHandlers(true);

        Logger INF_ERR = Logger.getLogger("MinimumTest.INF.SEV");
        INF_ERR.setLevel(Level.SEVERE);
        INF_ERR.setUseParentHandlers(true);

        Logger DEB = Logger.getLogger("MinimumTest.FIN");
        DEB.setLevel(Level.FINER);
        DEB.setUseParentHandlers(true);

        // Note: categories with undefined level
        Logger INF_UNDEF = Logger.getLogger("MinimumTest.INF.UNDEF");
        INF_UNDEF.setUseParentHandlers(true);
        Logger INF_ERR_UNDEF = Logger.getLogger("MinimumTest.INF.SEV.UNDEF");
        INF_ERR_UNDEF.setUseParentHandlers(true);
        Logger UNDEF = Logger.getLogger("MinimumTest.UNDEF");
        UNDEF.setUseParentHandlers(true);
        
//        DiagnosticLogManager diaM = (DiagnosticLogManager) LogManager.getLogManager();
//        diaM.printLogNodeTree();

        // These should all log.----------------------------
        int i = 0;
        ERR.log(Level.SEVERE, "Message " + i);
        i++; // 1
        ERR.severe("Message " + i);
        i++;

        INF.log(Level.SEVERE, "Message " + i);
        i++; // 3
        INF.severe("Message " + i);
        i++;
        INF.warning("Message " + i);
        i++;
        INF.info("Message " + i);
        i++;

        INF_UNDEF.log(Level.SEVERE, "Message " + i);
        i++; // 7
        INF_UNDEF.severe("Message " + i);
        i++;
        INF_UNDEF.warning("Message " + i);
        i++;
        INF_UNDEF.info("Message " + i);
        i++;

        INF_ERR.log(Level.SEVERE, "Message " + i);
        i++; // 11
        INF_ERR.severe("Message " + i);
        i++;

        INF_ERR_UNDEF.log(Level.SEVERE, "Message " + i);
        i++;
        INF_ERR_UNDEF.severe("Message " + i);
        i++;

        DEB.log(Level.SEVERE, "Message " + i);
        i++; // 15
        DEB.severe("Message " + i);
        i++;
        DEB.warning("Message " + i);
        i++;
        DEB.info("Message " + i);
        i++;
        DEB.finer("Message " + i);
        i++;

        // defaultLevel=INFO
        UNDEF.log(Level.SEVERE, "Message " + i);
        i++; // 20
        UNDEF.severe("Message " + i);
        i++;
        UNDEF.warning("Message " + i);
        i++;
        UNDEF.info("Message " + i);
        i++;
        UNDEF.log(Level.INFO, "Message " + i, new Exception("Just testing with "+i));
        i++;

        // -------------------------------------------------
        // The following should not log
        ERR.warning("Message " + i);
        i++; // 25
        ERR.info("Message " + i);
        i++;
        ERR.finer("Message " + i);
        i++;

        INF.finer("Message " + i);
        i++; // 27
        INF_UNDEF.finer("Message " + i);
        i++;

        INF_ERR.warning("Message " + i);
        i++; // 29
        INF_ERR.info("Message " + i);
        i++;
        INF_ERR.finer("Message " + i);
        i++;
        INF_ERR_UNDEF.warning("Message " + i);
        i++;
        INF_ERR_UNDEF.info("Message " + i);
        i++; // 33
        INF_ERR_UNDEF.finer("Message " + i);

        // -------------------------------------------------
        INF.info("Messages should bear numbers 0 through 23.");
    }

}

// EOF MinimumTest.java
