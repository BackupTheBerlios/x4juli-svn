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

package org.x4juli.performance;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.x4juli.global.helper.LoggerUtil;

/**
 * Testcase for x4juli.
 * @since 0.5
 */
public class LoggingLoop {
    static int runLength;

    static int command;

    static final Logger parent = Logger.getLogger("Parent");
    static final Logger logger = Logger.getLogger("Parent.child");

    static final double MILLION = 1000 * 1000.0;

    static final int WARM = 1000 * 100;

    static final int ALL = 0;

    static final int NOLOG_BAD = 1;

    static final int NOLOG_BETTER = 2;

    static final int NOLOG_NOPARAM = 3;

    static final int LOG_BAD = 4;

    static final int LOG_BETTER = 5;

    static final int LOG_NOPARAM = 6;

    /**
     * 
     */
    public LoggingLoop() {
        super();
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        parent.setUseParentHandlers(false);
        logger.setUseParentHandlers(true);
        logger.setLevel(Level.ALL);

        if (args.length == 2) {
          init(args[0], args[1]);
        } else {
          usage("Wrong number of arguments.");
        }
        
        switch(command) {
          case ALL:
          case NOLOG_BAD: 
            parent.setLevel(Level.OFF);
            loopBad(); 
            if(command != ALL) break; 
          case NOLOG_BETTER: 
            parent.setLevel(Level.OFF);
            loopBetter(); 
            if(command != ALL) break; 
          case NOLOG_NOPARAM: 
            parent.setLevel(Level.OFF);
            loopNoParam(); 
            if(command != ALL) break; 
          case LOG_BAD: 
            setNullAppender();
//            loopBad(); 
            if(command != ALL) break; 
          case LOG_BETTER: 
            setNullAppender();
//            loopBetter(); 
            if(command != ALL) break; 
          case LOG_NOPARAM: 
            setNullAppender();
//            loopNoParam(); 
            if(command != ALL) break; 
        }
        System.out.println("Done.");

    }

    static void init(String runLengthStr, String commandStr) throws Exception {
        runLength = Integer.parseInt(runLengthStr);
        if ("nolog-bad".equalsIgnoreCase(commandStr)) {
            command = NOLOG_BAD;
        } else if ("nolog-better".equalsIgnoreCase(commandStr)) {
            command = NOLOG_BETTER;
        } else if ("nolog-noparam".equalsIgnoreCase(commandStr)) {
            command = NOLOG_NOPARAM;
        } else if ("log-bad".equalsIgnoreCase(commandStr)) {
            command = LOG_BAD;
        } else if ("log-better".equalsIgnoreCase(commandStr)) {
            command = LOG_BETTER;
        } else if ("log-noparam".equalsIgnoreCase(commandStr)) {
            command = LOG_NOPARAM;
        } else if ("all".equalsIgnoreCase(commandStr)) {
            command = ALL;
        }
    }
    
    static void usage(String msg) {
        System.err.println(msg);
        System.err.println(
          "Usage: java " + LoggingLoop.class.getName() + " runLength configFile");
        System.err.println("\trunLength (integer) is the length of test loop.");

        System.exit(1);
      }

    static void setNullAppender() throws Exception {
        Handler na = new NOPHandler("NOP");
        LoggerUtil.removeAllHandlers(parent);
        parent.addHandler(na);
        parent.setLevel(Level.FINEST);
    }
    
    static void loopBad() {
        Integer x = new Integer(10);
        for (int i = 0; i < WARM; i++) {
          logger.log(Level.FINER,"Entry number: {0} is {1}.", new Object[]{x, x});
        }
        
        Runtime.getRuntime().gc();
        long before = System.currentTimeMillis();
        for (int i = 0; i < runLength; i++) {
          logger.log(Level.FINER,"Entry number: {0} is {1}.", new Object[]{x, x});
        }
        long elapsedTime = System.currentTimeMillis() - before;

        double average = (elapsedTime * MILLION) / runLength;
        System.out.println(
          "Bad loop completed in [" + elapsedTime + "] milliseconds, or ["
          + average + "] nanoseconds per log.");
      }

    static void loopBetter() {
        Integer x = new Integer(5);
        for (int i = 0; i < WARM; i++) {
          logger.log(Level.FINER,"Entry number: {0} is {1}.", new Object[]{x, new Integer(i)});

        }
        Runtime.getRuntime().gc();
        long before = System.currentTimeMillis();
        for (int i = 0; i < runLength; i++) {
          logger.log(Level.FINER,"Entry number: {0} is {1}.", new Object[]{x, new Integer(i)});
        }
        long elapsedTime = System.currentTimeMillis() - before;
        double average = (elapsedTime * MILLION) / runLength;
        System.out.println(
          "Better loop completed in [" + elapsedTime + "] milliseconds, or ["
          + average + "] nanoseconds per log.");
      }
    
    static void loopNoParam() {
        String msg = "Some message of medium length.";

        Runtime.getRuntime().gc();
        long before = System.currentTimeMillis();
        for (int i = 0; i < runLength; i++) {
          logger.finer(msg);
        }
        long elapsedTime = System.currentTimeMillis() - before;
        double average = (elapsedTime * MILLION) / runLength;
        System.out.println(
          "No parameter loop completed in [" + elapsedTime
          + "] milliseconds, or [" + average + "] nanoseconds per log.");
      }

}

// EOF LoggingLoop.java
