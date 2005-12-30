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

package org.x4juli.formatter.pattern;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.x4juli.formatter.pattern.FormattingInfo;
import org.x4juli.formatter.pattern.PatternConverter;
import org.x4juli.formatter.pattern.PatternParser;
import org.x4juli.global.LoggerRepositoryHolder;
import org.x4juli.global.SystemUtils;
import org.x4juli.global.spi.ExtendedLogRecord;
import org.x4juli.global.spi.ExtendedLogRecordWrapper;
import org.x4juli.global.spi.ObjectStore;


/**
 * Testcase for x4juli.
 * @since 0.5
 */
public class PatternParserTest extends TestCase {
    Logger logger = Logger.getLogger("org.foobar");
    LogRecord record;
    long now;
    ObjectStore repository;
    
    public PatternParserTest(String name) {
      super(name);
      repository = ((LoggerRepositoryHolder)LogManager.getLogManager()).getLoggerRepository();
      now = System.currentTimeMillis() + 13;

      LogRecord temp = new LogRecord(Level.INFO,"msg 1");
      temp.setLoggerName(logger.getName());
      temp.setMillis(now);
      record = (LogRecord) new ExtendedLogRecordWrapper(temp);
    }

    private static String convert(
                   String pattern,
                   Map registry,
                   LogRecord event) {
      List converters = new ArrayList();
      List fields = new ArrayList();
      PatternParser.parse(pattern, converters, fields,
              registry,
              PatternParser.getPatternLayoutRules());
      assertEquals(converters.size(), fields.size());

      StringBuffer buf = new StringBuffer();
      Iterator converterIter = converters.iterator();
      Iterator fieldIter = fields.iterator();
      while(converterIter.hasNext()) {
          int fieldStart = buf.length();
          ((PatternConverter) converterIter.next()).format(event, buf);
          ((FormattingInfo) fieldIter.next()).format(fieldStart, buf);
      }
      return buf.toString();
    }

    public void testNewWord() throws Exception {
      HashMap ruleRegistry = new HashMap(5);
      ruleRegistry.put("z343", Num343PatternConverter.class.getName());
      String result = convert("%z343", ruleRegistry, record);
      assertEquals("343", result);
    }

    /* Test whether words starting with the letter 'n' are treated differently,
     * which was previously the case by mistake.
     */
    public void testNewWord2() throws Exception {
      HashMap ruleRegistry = new HashMap(5);
      ruleRegistry.put("n343", Num343PatternConverter.class.getName());
      String result = convert("%n343", ruleRegistry, record);
      assertEquals("343", result);
    }

    public void testBogusWord1() throws Exception {
      String result = convert("%, foobar", null, record);
      assertEquals("%, foobar", result);
    }

    public void testBogusWord2() throws Exception {
      String result = convert("xyz %, foobar", null, record);
      assertEquals("xyz %, foobar", result);
    }

    public void testBasic1() throws Exception {
      String result = convert("hello %-5level - %m%n", null, record);
      assertEquals("hello INFO  - msg 1" + SystemUtils.LINE_SEPARATOR, result);
    }

    public void testBasic2() throws Exception {
      String result = convert("%relative %-5level [%thread] %logger - %m%n", null, record);

      long expectedRelativeTime = now - ((ExtendedLogRecord)record).getStartTime();
      String expected = expectedRelativeTime + " INFO  [main] "+logger.getName()+" - msg 1" + SystemUtils.LINE_SEPARATOR; 
      assertEquals(expected, result);
    }

    public void testMultiOption() throws Exception {
      String result = convert("%d{HH:mm:ss}{GMT} %d{HH:mm:ss} %c  - %m", null, record);

      SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
      String localTime = dateFormat.format(new Date(record.getMillis()));
      dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
      String utcTime = dateFormat.format(new Date(record.getMillis()));
      StringBuffer buf = new StringBuffer(utcTime);
      buf.append(' ');
      buf.append(localTime);
      buf.append(" org.foobar  - msg 1");
      assertEquals(buf.toString(), result);
    }

    public void testBogus() throws Exception {
        String result = convert("%bogus", null, record);
        assertEquals("%bogus", result);
      }

    public void testMore() throws Exception {
          String result = convert("%more", null, record);
          assertEquals("msg 1ore", result);
    }

      /**
       * Options with missing close braces will be treated as a literal.
       * Endless looped with earlier code.
       *
       */
      public void testMalformedOption() {
          String result = convert("foo%m{yyyy.MM.dd", null, record);
          assertEquals("foomsg 1{yyyy.MM.dd", result);
      }


    private void assertFactories(Map rules) throws Exception {
        assertTrue(rules.size() > 0);
        Iterator iter = rules.values().iterator();
        Class[] factorySig = new Class[] { Class.forName("[Ljava.lang.String;") };
        Object[] factoryArg = new Object[] { null };
        while(iter.hasNext()) {
            Class ruleClass = (Class) iter.next();
            Method factory =  ruleClass.getMethod("newInstance", factorySig);
            Object converter = factory.invoke(null, factoryArg);
            assertTrue(converter != null);
        }
    }

    public void testPatternLayoutFactories() throws Exception {
        assertFactories(PatternParser.getPatternLayoutRules());
    }

    public void testFileNamePatternFactories() throws Exception {
          assertFactories(PatternParser.getFileNamePatternRules());
    }

}
