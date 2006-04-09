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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.x4juli.global.helper.Loader;
import org.x4juli.global.resources.MessageProperties;
import org.x4juli.global.spi.AbstractComponent;
import org.x4juli.global.spi.ExtendedLogRecord;
import org.x4juli.global.spi.ExtendedLogRecordImpl;

/**
 * Most of the work of the {@link org.x4juli.formatter.PatternFormatter}
 * class is delegated to the PatternParser class.
 * <p>
 * It is this class that parses conversion patterns and creates a chained list
 * of {@link PatternConverter PatternConverters}.
 * </p>
 * <p>
 * Logging API as a whole was originally done for <a
 * href="http://logging.apache.org/log4j/">Apache log4j</a>. <b>Juli</b> is a
 * port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging
 * APIs</a>. All credits for initial idea, design, implementation,
 * documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This
 * file was originally published by <i>James P. Cakalic, Ceki G&uuml;lc&uuml;,
 * Anders Kristensen, Paul Smith, Curt Arnold</i>. Please use exclusively the
 * <i>appropriate</i> mailing lists for questions, remarks and contribution.
 * </p>
 *
 * @author Boris Unckel
 * @since 0.5
 */
public final class PatternParser extends AbstractComponent {

    // -------------------------------------------------------------- Variables
    /**
     * Escape character for format specifier.
     */
    private static final char ESCAPE_CHAR = '%';

    /**
     * Literal state.
     */
    private static final int LITERAL_STATE = 0;

    /**
     * In converter name state.
     */
    private static final int CONVERTER_STATE = 1;

    /**
     * Dot state.
     */
    private static final int DOT_STATE = 3;

    /**
     * Min state.
     */
    private static final int MIN_STATE = 4;

    /**
     * Max state.
     */
    private static final int MAX_STATE = 5;

    /**
     * Standard format specifiers for PatternLayout.
     */
    private static final Map PATTERN_LAYOUT_RULES;

    /**
     * Standard format specifiers for rolling file appenders.
     */
    private static final Map FILENAME_PATTERN_RULES;

    private static final PatternParser INSTANCE;
    // ----------------------------------------------------------- Constructors

    static {
        // We set the global rules in the static initializer of PatternParser
        // class
        final int correctSize = 17;
        Map rules = new HashMap(correctSize);
        rules.put("c", LoggerPatternConverter.class);
        rules.put("logger", LoggerPatternConverter.class);

        rules.put("C", ClassNamePatternConverter.class);
        rules.put("class", ClassNamePatternConverter.class);

        rules.put("d", DatePatternConverter.class);
        rules.put("date", DatePatternConverter.class);

        rules.put("F", FileLocationPatternConverter.class);
        rules.put("file", FileLocationPatternConverter.class);

        rules.put("l", FullLocationPatternConverter.class);

        rules.put("L", LineLocationPatternConverter.class);
        rules.put("line", LineLocationPatternConverter.class);

        rules.put("m", MessagePatternConverter.class);
        rules.put("message", MessagePatternConverter.class);

        rules.put("n", LineSeparatorPatternConverter.class);

        rules.put("M", MethodLocationPatternConverter.class);
        rules.put("method", MethodLocationPatternConverter.class);

        rules.put("p", LevelPatternConverter.class);
        rules.put("level", LevelPatternConverter.class);

        rules.put("r", RelativeTimePatternConverter.class);
        rules.put("relative", RelativeTimePatternConverter.class);

        rules.put("t", ThreadPatternConverter.class);
        rules.put("thread", ThreadPatternConverter.class);

        rules.put("x", NDCPatternConverter.class);
        rules.put("ndc", NDCPatternConverter.class);

        rules.put("X", PropertiesPatternConverter.class);
        rules.put("properties", PropertiesPatternConverter.class);

        rules.put("sn", SequenceNumberPatternConverter.class);
        rules.put("sequenceNumber", SequenceNumberPatternConverter.class);

        rules.put("throwable", ThrowableInformationPatternConverter.class);
        PATTERN_LAYOUT_RULES = new ReadOnlyMap(rules);

        final int countRules = 4;
        Map fnameRules = new HashMap(countRules);
        fnameRules.put("d", FileDatePatternConverter.class);
        fnameRules.put("date", FileDatePatternConverter.class);
        fnameRules.put("i", IntegerPatternConverter.class);
        fnameRules.put("index", IntegerPatternConverter.class);

        FILENAME_PATTERN_RULES = new ReadOnlyMap(fnameRules);

        INSTANCE = new PatternParser();
    }



    /**
     * Private constructor.
     */
    private PatternParser() {
        super();
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Get standard format specifiers for PatternLayout.
     *
     * @return read-only map of format converter classes keyed by format
     *         specifier strings.
     */
    public static Map getPatternLayoutRules() {
        return PATTERN_LAYOUT_RULES;
    }

    /**
     * Get standard format specifiers for rolling file appender file
     * specification.
     *
     * @return read-only map of format converter classes keyed by format
     *         specifier strings.
     */
    public static Map getFileNamePatternRules() {
        return FILENAME_PATTERN_RULES;
    }

    /**
     * Parse a format specifier.
     *
     * @param pattern pattern to parse.
     * @param patternConverters list to receive pattern converters.
     * @param formattingInfos list to receive field specifiers corresponding to
     *            pattern converters.
     * @param converterRegistry map of user-supported pattern converters keyed
     *            by format specifier, may be null.
     * @param rules map of stock pattern converters keyed by format specifier.
     */
    public static void parse(final String pattern, final List patternConverters,
            final List formattingInfos, final Map converterRegistry, final Map rules) {
        PatternParser.INSTANCE.doParse(pattern, patternConverters,
                formattingInfos, converterRegistry, rules);
    }

    /**
     * Parse a format specifier.
     *
     * @param pattern pattern to parse.
     * @param patternConverters list to receive pattern converters.
     * @param formattingInfos list to receive field specifiers corresponding to
     *            pattern converters.
     * @param converterRegistry map of user-supported pattern converters keyed
     *            by format specifier, may be null.
     * @param rules map of stock pattern converters keyed by format specifier.
     */
    private synchronized void doParse(final String pattern, final List patternConverters,
            final List formattingInfos, final Map converterRegistry, final Map rules) {
        if (pattern == null) {
            throw new NullPointerException("pattern");
        }

        final int initialSize = 32;
        final int ten = 10;
        StringBuffer currentLiteral = new StringBuffer(initialSize);

        int patternLength = pattern.length();
        int state = LITERAL_STATE;
        char c;
        int i = 0;
        FormattingInfo formattingInfo = FormattingInfo.getDefault();

        while (i < patternLength) {
            c = pattern.charAt(i++);

            switch (state) {
            case LITERAL_STATE:

                // In literal state, the last char is always a literal.
                if (i == patternLength) {
                    currentLiteral.append(c);

                    continue;
                }

                if (c == ESCAPE_CHAR) {
                    // peek at the next char.
                    switch (pattern.charAt(i)) {
                    case ESCAPE_CHAR:
                        currentLiteral.append(c);
                        i++; // move pointer

                        break;

                    default:

                        if (currentLiteral.length() != 0) {
                            patternConverters.add(new LiteralPatternConverter(currentLiteral
                                    .toString()));
                            formattingInfos.add(FormattingInfo.getDefault());
                        }

                        currentLiteral.setLength(0);
                        currentLiteral.append(c); // append %
                        state = CONVERTER_STATE;
                        formattingInfo = FormattingInfo.getDefault();
                    }
                } else {
                    currentLiteral.append(c);
                }

                break;

            case CONVERTER_STATE:
                currentLiteral.append(c);

                switch (c) {
                case '-':
                    formattingInfo = new FormattingInfo(true, formattingInfo.getMinLength(),
                            formattingInfo.getMaxLength());

                    break;

                case '.':
                    state = DOT_STATE;

                    break;

                default:

                    if ((c >= '0') && (c <= '9')) {
                        formattingInfo = new FormattingInfo(formattingInfo.isLeftAligned(),
                                c - '0', formattingInfo.getMaxLength());
                        state = MIN_STATE;
                    } else {
                        i = finalizeConverter(c, pattern, i, currentLiteral,
                                formattingInfo, converterRegistry, rules, patternConverters,
                                formattingInfos);

                        // Next pattern is assumed to be a literal.
                        state = LITERAL_STATE;
                        formattingInfo = FormattingInfo.getDefault();
                        currentLiteral.setLength(0);
                    }
                } // switch

                break;

            case MIN_STATE:
                currentLiteral.append(c);

                if ((c >= '0') && (c <= '9')) {
                    formattingInfo = new FormattingInfo(formattingInfo.isLeftAligned(),
                            (formattingInfo.getMinLength() * ten) + (c - '0'), formattingInfo
                                    .getMaxLength());
                } else if (c == '.') {
                    state = DOT_STATE;
                } else {
                    i = finalizeConverter(c, pattern, i, currentLiteral, formattingInfo,
                            converterRegistry, rules, patternConverters, formattingInfos);
                    state = LITERAL_STATE;
                    formattingInfo = FormattingInfo.getDefault();
                    currentLiteral.setLength(0);
                }

                break;

            case DOT_STATE:
                currentLiteral.append(c);

                if ((c >= '0') && (c <= '9')) {
                    formattingInfo = new FormattingInfo(formattingInfo.isLeftAligned(),
                            formattingInfo.getMinLength(), c - '0');
                    state = MAX_STATE;
                } else {
                        getLogger().log(Level.SEVERE,
                           MessageText.Error_occured_in_position_Expecting_digit_got_char,
                                        new Object[] {new Integer(i), new Character(c) });
                    state = LITERAL_STATE;
                }

                break;

            case MAX_STATE:
                currentLiteral.append(c);
                if ((c >= '0') && (c <= '9')) {
                    formattingInfo = new FormattingInfo(formattingInfo.isLeftAligned(),
                            formattingInfo.getMinLength(), (formattingInfo.getMaxLength() * ten)
                                    + (c - '0'));
                } else {
                    i = finalizeConverter(c, pattern, i, currentLiteral, formattingInfo,
                            converterRegistry, rules, patternConverters, formattingInfos);
                    state = LITERAL_STATE;
                    formattingInfo = FormattingInfo.getDefault();
                    currentLiteral.setLength(0);
                }

                break;
            default:
              //should never occure.
              assert (false);
            } // switch
        }

        // while
        if (currentLiteral.length() != 0) {
            patternConverters.add(new LiteralPatternConverter(currentLiteral.toString()));
            formattingInfos.add(FormattingInfo.getDefault());
        }
    }

    /**
     * {@inheritDoc}
     * @since
     */
    public MessageProperties getMessageProperties() {
        return MessageProperties.PROPERTIES_FORMATTER_PATTERN;
    }

    /**
     * Creates a new PatternConverter.
     *
     *
     * @param converterId converterId.
     * @param currentLiteral literal to be used if converter is unrecognized or
     *            following converter if converterId contains extra characters.
     * @param converterRegistry map of user-supported pattern converters keyed
     *            by format specifier, may be null.
     * @param rules map of stock pattern converters keyed by format specifier.
     * @param options converter options.
     * @return converter or null.
     */
    private static PatternConverter createConverter(final String converterId,
            final StringBuffer currentLiteral, final Map converterRegistry, final Map rules,
            final List options) {
        return PatternParser.INSTANCE.doCcreateConverter(converterId, currentLiteral,
                converterRegistry, rules, options);
    }

    /**
     * Creates a new PatternConverter.
     *
     *
     * @param converterId converterId.
     * @param currentLiteral literal to be used if converter is unrecognized or
     *            following converter if converterId contains extra characters.
     * @param converterRegistry map of user-supported pattern converters keyed
     *            by format specifier, may be null.
     * @param rules map of stock pattern converters keyed by format specifier.
     * @param options converter options.
     * @return converter or null.
     */
    private synchronized PatternConverter doCcreateConverter(final String converterId,
            final StringBuffer currentLiteral, final Map converterRegistry, final Map rules,
            final List options) {
        String converterName = converterId;
        Object converterObj = null;

        for (int i = converterId.length(); (i > 0) && (converterObj == null); i--) {
            converterName = converterName.substring(0, i);

            if (converterRegistry != null) {
                converterObj = converterRegistry.get(converterName);
            }

            if ((converterObj == null) && (rules != null)) {
                converterObj = rules.get(converterName);
            }
        }

        if (converterObj == null) {
            getLogger().log(Level.SEVERE, MessageText.Unrecognized_conversion_specifier,
                                new Object[]{"UNDEFINED", converterId});
            return null;
        }

        Class converterClass = null;

        if (converterObj instanceof Class) {
            converterClass = (Class) converterObj;
        } else {
            if (converterObj instanceof String) {
                try {
                    converterClass = Loader.loadClass((String) converterObj);
                } catch (ClassNotFoundException ex) {
                    ExtendedLogRecord rec = new ExtendedLogRecordImpl(Level.WARNING,
                            MessageText.Class_for_conversion_pattern_not_found);
                    rec.setParameters(new Object[]{converterName});
                    rec.setThrown(ex);
                    getLogger().log(rec);

                    return null;
                }
            } else {
                getLogger().log(Level.WARNING, MessageText.Bad_map_entry_for_conversion_pattern,
                            converterName);

                return null;
            }
        }

        try {
            Method factory = converterClass.getMethod("newInstance", new Class[] {
                    Class.forName("[Ljava.lang.String;")});
            String[] optionsArray = new String[options.size()];
            optionsArray = (String[]) options.toArray(optionsArray);

            Object newObj = factory.invoke(null, new Object[] {optionsArray});

            if (newObj instanceof PatternConverter) {
                currentLiteral.delete(0, currentLiteral.length()
                        - (converterId.length() - converterName.length()));

                return (PatternConverter) newObj;
            } else {
                getLogger().log(Level.WARNING, MessageText.Class_does_not_extend_PatternConverter,
                            converterClass.getName());
            }
        } catch (Exception ex) {
            ExtendedLogRecord rec1 = new ExtendedLogRecordImpl(Level.SEVERE,
                                           MessageText.Error_creating_converter);
            rec1.setParameters(new Object[]{converterId});
            rec1.setThrown(ex);
            getLogger().log(rec1);

            try {
                //
                // try default constructor
                PatternConverter pc = (PatternConverter) converterClass.newInstance();
                currentLiteral.delete(0, currentLiteral.length()
                        - (converterId.length() - converterName.length()));

                return pc;
            } catch (Exception ex2) {
                ExtendedLogRecord rec2 = new ExtendedLogRecordImpl(Level.SEVERE,
                        MessageText.Error_creating_converter);
                rec2.setParameters(new Object[]{converterId});
                rec2.setThrown(ex);
                getLogger().log(rec2);

            }
        }

        return null;
    }

    // ------------------------------------------------------ Protected Methods

    // -------------------------------------------------------- Private Methods

    /**
     * Extract the converter identifier found at position i.
     *
     * After this function returns, the variable i will point to the first char
     * after the end of the converter identifier.
     *
     * If i points to a char which is not a character acceptable at the start of
     * a unicode identifier, the value null is returned.
     *
     * @param lastChar last processed character.
     * @param pattern format string.
     * @param j current index into pattern format.
     * @param convBuf buffer to receive conversion specifier.
     * @param currentLiteral literal to be output in case format specifier in
     *            unrecognized.
     * @return position in pattern after converter.
     * @since 0.5
     */
    private int extractConverter(final char lastChar, final String pattern, final int j,
            final StringBuffer convBuf, final StringBuffer currentLiteral) {
        convBuf.setLength(0);
        int index = j;
        // When this method is called, lastChar points to the first character of
        // the
        // conversion word. For example:
        // For "%hello" lastChar = 'h'
        // For "%-5hello" lastChar = 'h'
        // System.out.println("lastchar is "+lastChar);
        if (!Character.isUnicodeIdentifierStart(lastChar)) {
            return index;
        }

        convBuf.append(lastChar);

        while ((index < pattern.length())
                && Character.isUnicodeIdentifierPart(pattern.charAt(index))) {
            convBuf.append(pattern.charAt(index));
            currentLiteral.append(pattern.charAt(index));

            // System.out.println("conv buffer is now ["+convBuf+"].");
            index++;
        }

        return index;
    }

    /**
     * Extract options.
     *
     * @param pattern conversion pattern.
     * @param j start of options.
     * @param options array to receive extracted options
     * @return position in pattern after options.
     * @since 0.5
     */
    private static int extractOptions(final String pattern, final int j, final List options) {
        int i = j;
        while ((i < pattern.length()) && (pattern.charAt(i) == '{')) {
            int end = pattern.indexOf('}', i);

            if (end == -1) {
                break;
            }

            String r = pattern.substring(i + 1, end);
            options.add(r);
            i = end + 1;
        }

        return i;
    }

    /**
     * Processes a format specifier sequence.
     *
     * @param c initial character of format specifier.
     * @param pattern conversion pattern
     * @param i current position in conversion pattern.
     * @param currentLiteral current literal.
     * @param formattingInfo current field specifier.
     * @param converterRegistry map of user-provided pattern converters keyed by
     *            format specifier, may be null.
     * @param rules map of stock pattern converters keyed by format specifier.
     * @param patternConverters list to receive parsed pattern converter.
     * @param formattingInfos list to receive corresponding field specifier.
     * @return position after format specifier sequence.
     */
    private static int finalizeConverter(final char c, final String pattern, final int i,
            final StringBuffer currentLiteral, final FormattingInfo formattingInfo,
            final Map converterRegistry, final Map rules, final List patternConverters,
            final List formattingInfos) {

        return PatternParser.INSTANCE.doFinalizeConverter(c, pattern, i, currentLiteral,
                formattingInfo, converterRegistry, rules, patternConverters, formattingInfos);
    }
    /**
     * Processes a format specifier sequence.
     *
     * @param c initial character of format specifier.
     * @param pattern conversion pattern
     * @param j current position in conversion pattern.
     * @param currentLiteral current literal.
     * @param formattingInfo current field specifier.
     * @param converterRegistry map of user-provided pattern converters keyed by
     *            format specifier, may be null.
     * @param rules map of stock pattern converters keyed by format specifier.
     * @param patternConverters list to receive parsed pattern converter.
     * @param formattingInfos list to receive corresponding field specifier.
     * @return position after format specifier sequence.
     */
    private int doFinalizeConverter(final char c, final String pattern, final int j,
            final StringBuffer currentLiteral, final FormattingInfo formattingInfo,
            final Map converterRegistry, final Map rules, final List patternConverters,
            final List formattingInfos) {
        StringBuffer convBuf = new StringBuffer();
        int i = extractConverter(c, pattern, j, convBuf, currentLiteral);

        String converterId = convBuf.toString();

        List options = new ArrayList();
        i = extractOptions(pattern, i, options);

        PatternConverter pc = createConverter(converterId, currentLiteral, converterRegistry,
                rules, options);

        if (pc == null) {
            if ((converterId == null) || (converterId.length() == 0)) {
                    getLogger()
                            .log(Level.SEVERE, MessageText.Empty_conversion_specifier,
                                    new Integer(i));
            } else {
                getLogger().log(Level.SEVERE, MessageText.Unrecognized_conversion_specifier,
                            new Object[] {new Integer(i), converterId });
            }

            patternConverters.add(new LiteralPatternConverter(currentLiteral.toString()));
            formattingInfos.add(FormattingInfo.getDefault());
        } else {
            patternConverters.add(pc);
            formattingInfos.add(formattingInfo);

            if (currentLiteral.length() > 0) {
                patternConverters.add(new LiteralPatternConverter(currentLiteral.toString()));
                formattingInfos.add(FormattingInfo.getDefault());
            }
        }

        currentLiteral.setLength(0);

        return i;
    }

    // -------------------------------------------------------- Inner Classes

    /**
     * The class wraps another Map but throws exceptions on any attempt to
     * modify the map.
     *
     * @since 0.5
     */
    private static class ReadOnlyMap implements Map {
        /**
         * Wrapped map.
         */
        private final Map map;

        /**
         * Constructor.
         *
         * @param src map to wrap.
         */
        public ReadOnlyMap(final Map src) {
            this.map = src;
        }

        /**
         * {@inheritDoc}
         */
        public void clear() {
            throw new UnsupportedOperationException();
        }

        /**
         * {@inheritDoc}
         */
        public boolean containsKey(final Object key) {
            return this.map.containsKey(key);
        }

        /**
         * {@inheritDoc}
         */
        public boolean containsValue(final Object value) {
            return this.map.containsValue(value);
        }

        /**
         * {@inheritDoc}
         */
        public Set entrySet() {
            return this.map.entrySet();
        }

        /**
         * {@inheritDoc}
         */
        public Object get(final Object key) {
            return this.map.get(key);
        }

        /**
         * {@inheritDoc}
         */
        public boolean isEmpty() {
            return this.map.isEmpty();
        }

        /**
         * {@inheritDoc}
         */
        public Set keySet() {
            return this.map.keySet();
        }

        /**
         * {@inheritDoc}
         */
        public Object put(final Object key, final Object value) {
            throw new UnsupportedOperationException();
        }

        /**
         * {@inheritDoc}
         */
        public void putAll(final Map t) {
            throw new UnsupportedOperationException();
        }

        /**
         * {@inheritDoc}
         */
        public Object remove(final Object key) {
            throw new UnsupportedOperationException();
        }

        /**
         * {@inheritDoc}
         */
        public int size() {
            return this.map.size();
        }

        /**
         * {@inheritDoc}
         */
        public Collection values() {
            return this.map.values();
        }
    }
}

// EOF PatternParser.java
