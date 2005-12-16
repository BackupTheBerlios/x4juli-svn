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
package org.x4juli.global.helper;

import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A convenience class to convert property values to specific types.
 * <p>
 * Logging API as a whole was originally done for <a
 * href="http://logging.apache.org/log4j/">Apache log4j</a>. <b>Juli</b> is a
 * port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging
 * APIs</a>. All credits for initial idea, design, implementation,
 * documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This
 * file was originally published by <i>Ceki G&uuml;lc&uuml;, Simon Kitching,
 * Anders Kristensen, Avy Sharell</i>. Please use exclusively the
 * <i>appropriate</i> mailing lists for questions, remarks and contribution.
 * </p>
 *
 * @author Boris Unckel
 * @since 0.5
 */
public final class OptionConverter {

    // -------------------------------------------------------------- Variables

    /**
     * Constant for delimiter start.
     * <br/>
     * Value is {@value}.
     */
    static final String DELIM_START = "${";

    /**
     * Constant for delimiter stop.
     * <br/>
     * Value is {@value}.
     */
    static final char DELIM_STOP = '}';

    /**
     * Constant for delimiter start length.
     * <br/>
     * Value is {@value}.
     */
    static final int DELIM_START_LEN = 2;

    /**
     * Constant for delimiter stop length.
     * <br/>
     * Value is {@value}.
     */
    static final int DELIM_STOP_LEN = 1;

    // ------------------------------------------------------------ Constructor

    /**
     * No instanciation wanted.
     */
    private OptionConverter() {
        //NOP
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Concatanate two String arrays. Fast due to use of
     * <code>System.arraycopy</code>.
     *
     * @param l first array
     * @param r second arry
     * @return concated array
     */
    public static String[] concatanateArrays(final String[] l, final String[] r) {
        int len = l.length + r.length;
        String[] a = new String[len];

        System.arraycopy(l, 0, a, 0, l.length);
        System.arraycopy(r, 0, a, l.length, r.length);

        return a;
    }

    /**
     * White- and Controlchar conversion.
     *
     * @param s the string to convert
     * @return the string with converted chars
     */
    public static String convertSpecialChars(final String s) {
        char c;
        int len = s.length();
        StringBuffer sbuf = new StringBuffer(len);

        int i = 0;

        while (i < len) {
            c = s.charAt(i++);

            if (c == '\\') {
                c = s.charAt(i++);

                if (c == 'n') {
                    c = '\n';
                } else if (c == 'r') {
                    c = '\r';
                } else if (c == 't') {
                    c = '\t';
                } else if (c == 'f') {
                    c = '\f';
                } else if (c == '\b') {
                    c = '\b';
                } else if (c == '\"') {
                    c = '\"';
                } else if (c == '\'') {
                    c = '\'';
                } else if (c == '\\') {
                    c = '\\';
                }
            }

            sbuf.append(c);
        }

        return sbuf.toString();
    }

    /**
     * Very similar to <code>System.getProperty</code> except that the
     * {@link SecurityException} is hidden.
     *
     * @param key The key to search for.
     * @param def The default value to return.
     * @return the string value of the system property, or the default value if
     *         there is no property with that key.
     *
     * @since 0.5
     */
    public static String getSystemProperty(final String key, final String def) {
        try {
            return System.getProperty(key, def);
        } catch (Throwable e) { // MS-Java throws
            // com.ms.security.SecurityExceptionEx
            return def;
        }
    }

    /**
     * Instantiate an object by given keys.
     *
     * @param props to look in and replace.
     * @param key to search for.
     * @param superClass of the object.
     * @param defaultValue default Object, if instantion fails.
     * @return the instantiated Object.
     * @since 0.5
     */
    public static Object instantiateByKey(final Properties props, final String key, 
            final Class superClass, final Object defaultValue) {
        // Get the value of the property in string form
        String className = findAndSubst(key, props);

        if (className == null) {
            getLogger().log(Level.SEVERE, "Could not find value for key {0}", key);

            return defaultValue;
        }

        // Trim className to avoid trailing spaces that cause problems.
        return instantiateByClassName(className.trim(), superClass, defaultValue);
    }

    /**
     * Converts a String to an boolean.
     * <p>
     * If <code>value</code> is "true", then <code>true</code> is returned.
     * If <code>value</code> is "false", then <code>true</code> is returned.
     * Otherwise, <code>default</code> is returned.
     * </p>
     * <p>
     * Case of value is unimportant.
     * </p>
     *
     * @param value the value
     * @param dEfault returned if parsing fails
     * @since 0.5
     */
    public static boolean toBoolean(final String value, final boolean dEfault) {
        if (value == null) {
            return dEfault;
        }

        String trimmedVal = value.trim();

        if ("true".equalsIgnoreCase(trimmedVal)) {
            return true;
        }

        if ("false".equalsIgnoreCase(trimmedVal)) {
            return false;
        }

        return dEfault;
    }

    /**
     * Converts a string to an int.
     *
     * @param value
     * @param dEfault
     * @return the parsed int, on error the default
     * @since 0.5
     */
    public static int toInt(final String value, final int dEfault) {
        if (value != null) {
            String s = value.trim();

            try {
                return Integer.valueOf(s).intValue();
            } catch (NumberFormatException e) {
                getLogger().log(Level.SEVERE, "[{0}] is not in proper int form.", s);
            }
        }

        return dEfault;
    }

    /**
     * Converts a String to a long.
     *
     * @param value
     * @param dEfault
     * @return the parsed long, on error the default
     * @since 0.5
     */
    public static long toLong(final String value, final long dEfault) {
        if (value != null) {
            String s = value.trim();

            try {
                return Long.parseLong(value);
            } catch (NumberFormatException e) {
                getLogger().log(Level.SEVERE, "[{0}] is not in proper long form.", s);
            }
        }

        return dEfault;
    }

    /**
     * Parsing a string with given KB, MB, or GB filesize.
     *
     * @param value the string with the size (i.e. 9KB, 10MB, 1GB...)
     * @param dEfault the value returned if parsing fails
     * @return the parsed filesize
     * @since 0.5
     */
    public static long toFileSize(final String value, final long dEfault) {
        if (value == null) {
            return dEfault;
        }

        String s = value.trim().toUpperCase(Locale.ENGLISH);
        long multiplier = 1;
        int index;
        final long kilo = 1024;

        if ((index = s.indexOf("KB")) != -1) {
            multiplier = kilo;
            s = s.substring(0, index);
        } else if ((index = s.indexOf("MB")) != -1) {
            multiplier = kilo * kilo;
            s = s.substring(0, index);
        } else if ((index = s.indexOf("GB")) != -1) {
            multiplier = kilo * kilo * kilo;
            s = s.substring(0, index);
        }

        if (s != null) {
            try {
                return Long.valueOf(s).longValue() * multiplier;
            } catch (NumberFormatException e) {
                getLogger().log(Level.SEVERE, "[{0}] is not in proper int form.", s);
                getLogger().log(Level.SEVERE, "[" + value + "] not in expected format.", e);
            }
        }

        return dEfault;
    }

    /**
     * Find the value corresponding to <code>key</code> in <code>props</code>.
     * Then perform variable substitution on the found value.
     *
     * @since 0.5
     */
    public static String findAndSubst(final String key, final Properties props) {
        String value = props.getProperty(key);

        if (value == null) {
            return null;
        }

        try {
            return substVars(value, props);
        } catch (IllegalArgumentException e) {
            getLogger().log(Level.SEVERE, "Bad option value [" + value + "].", e);

            return value;
        }
    }

    /**
     * Instantiate an object given a class name. Check that the
     * <code>className</code> is a subclass of <code>superClass</code>. If
     * that test fails or the object could not be instantiated, then
     * <code>defaultValue</code> is returned.
     *
     * @param className The fully qualified class name of the object to
     *            instantiate.
     * @param superClass The class to which the new object should belong.
     * @param defaultValue The object to return in case of non-fulfillment
     * @since 0.5
     */
    public static Object instantiateByClassName(final String className, final Class superClass,
            final Object defaultValue) {
        if (className != null) {
            try {
                Class classObj = Loader.loadClass(className);

                if (!superClass.isAssignableFrom(classObj)) {
                    getLogger().log(
                            Level.SEVERE,
                            "A \"" + className + "\" object is not assignable to a \""
                                    + superClass.getName() + "\" variable.");
                    getLogger().log(Level.SEVERE,
                            "The class \"" + superClass.getName() + "\" was loaded by ");
                    getLogger().log(Level.SEVERE,
                            "[" + superClass.getClassLoader() + "] whereas object of type ");
                    getLogger().log(
                            Level.SEVERE,
                            "\"" + classObj.getName() + "\" was loaded by ["
                                    + classObj.getClassLoader() + "].");

                    return defaultValue;
                }

                // System.out.println("About to call classObj.newInstance(),
                // "+classObj.getName());

                return classObj.newInstance();
            } catch (NoClassDefFoundError ncfe) {
                getLogger().log(Level.SEVERE,
                        "Could not instantiate object of class [" + className + "].", ncfe);
            } catch (Throwable e) {
                getLogger().log(Level.SEVERE,
                        "Could not instantiate object of class [" + className + "].", e);
            }
        }

        return defaultValue;
    }

    /**
     * Perform variable substitution in string <code>val</code> from the
     * values of keys found the properties passed as parameter or in the system
     * propeties.
     *
     * <p>
     * The variable substitution delimeters are <b>${</b> and <b>}</b>.
     *
     * <p>
     * For example, if the properties parameter contains a property "key1" set
     * as "value1", then the call
     *
     * <pre>
     * String s = OptionConverter.substituteVars(&quot;Value of key is ${key1}.&quot;);
     * </pre>
     *
     * will set the variable <code>s</code> to "Value of key is value1.".
     *
     * <p>
     * If no value could be found for the specified key, then the system
     * properties are searched, if the value could not be found there, then
     * substitution defaults to the empty string.
     *
     * <p>
     * For example, if system propeties contains no value for the key
     * "inexistentKey", then the call
     *
     * <pre>
     * String s = OptionConverter.subsVars(&quot;Value of inexistentKey is [${inexistentKey}]&quot;);
     * </pre>
     *
     * will set <code>s</code> to "Value of inexistentKey is []".
     *
     * <p>
     * Nevertheless, it is possible to specify a default substitution value
     * using the ":-" operator. For example, the call
     *
     * <pre>
     * String s = OptionConverter.subsVars(&quot;Value of key is [${key2:-val2}]&quot;);
     * </pre>
     *
     * will set <code>s</code> to "Value of key is [val2]" even if the "key2"
     * property is unset.
     *
     * <p>
     * An {@link java.lang.IllegalArgumentException} is thrown if
     * <code>val</code> contains a start delimeter "${" which is not balanced
     * by a stop delimeter "}".
     * </p>
     *
     * @param val The string on which variable substitution is performed.
     * @throws IllegalArgumentException if <code>val</code> is malformed.
     * @since 0.5
     */
    public static String substVars(final String val, final Properties props) {

        StringBuffer sbuf = new StringBuffer();

        int i = 0;
        int j;
        int k;

        while (true) {
            j = val.indexOf(DELIM_START, i);

            if (j == -1) {
                // no more variables
                if (i == 0) { // this is a simple string

                    return val;
                } else { // add the tail string which contails no variables
                    // and return the result.
                    sbuf.append(val.substring(i, val.length()));

                    return sbuf.toString();
                }
            } else {
                sbuf.append(val.substring(i, j));
                k = val.indexOf(DELIM_STOP, j);

                if (k == -1) {
                    throw new IllegalArgumentException('"' + val
                            + "\" has no closing brace. Opening brace at position " + j + '.');
                } else {
                    j += DELIM_START_LEN;

                    String rawKey = val.substring(j, k);

                    // Massage the key to extract a default replacement if there
                    // is one
                    String[] extracted = extractDefaultReplacement(rawKey);
                    String key = extracted[0];
                    String defaultReplacement = extracted[1]; // can be null

                    String replacement = null;

                    // first try the props passed as parameter
                    if (props != null) {
                        replacement = props.getProperty(key);
                    }

                    // then try in System properties
                    if (replacement == null) {
                        replacement = getSystemProperty(key, null);
                    }

                    // if replacement is still null, use the defaultReplacement
                    // which
                    // still be null
                    if (replacement == null) {
                        replacement = defaultReplacement;
                    }

                    if (replacement != null) {
                        // Do variable substitution on the replacement string
                        // such that we can solve "Hello ${x2}" as "Hello p1"
                        // where the properties are
                        // x1=p1
                        // x2=${x1}
                        String recursiveReplacement = substVars(replacement, props);
                        sbuf.append(recursiveReplacement);
                    }

                    i = k + DELIM_STOP_LEN;
                }
            }
        }
    }

    /**
     * Splits up a given String into in String[2] Array.
     *
     * @param key format must be FIRST:-SECOND
     * @return the splited array, in case of parse error the key
     * @since 0.5
     */
    static public String[] extractDefaultReplacement(final String key) {
        String[] result = new String[2];
        result[0] = key;
        int d = key.indexOf(":-");
        if (d != -1) {
            result[0] = key.substring(0, d);
            result[1] = key.substring(d + 2);
        }
        return result;
    }

    /**
     * Replaces double backslashes. (Except the leading doubles in UNC's) with
     * single backslashes for compatibility with existing path specifications
     * that were working around use of OptionConverter.convertSpecialChars in
     * XML configuration files.
     *
     * @param src source string
     * @return source string with double backslashes replaced
     *
     * @since 0.5
     */
    public static String stripDuplicateBackslashes(final String src) {
        int i = src.lastIndexOf('\\');
        if (i > 0) {
            StringBuffer buf = new StringBuffer(src);
            for (; i > 0; i = src.lastIndexOf('\\', i - 1)) {
                //
                // if the preceding character is a slash then
                // remove the preceding character
                // and continue processing with the earlier part of the string
                if (src.charAt(i - 1) == '\\') {
                    buf.deleteCharAt(i);
                    i--;
                    if (i == 0)
                        break;
                } else {
                    //
                    // if there was a single slash then
                    // the string was not trying to work around
                    // convertSpecialChars
                    //
                    return src;
                }
            }
            return buf.toString();
        }
        return src;
    }

    /**
     * Converts a standard or custom priority level to a Level object.
     * <p>
     * If <code>value</code> is of form "level#classname", then the specified
     * class' toLevel method is called to process the specified level string; if
     * no '#' character is present, then the default
     * {@link java.util.logging.Level} class is used to process the level value.
     *
     * <p>
     * As a special case, if the <code>value</code> parameter is equal to the
     * string "NULL", then the value <code>null</code> will be returned.
     *
     * <p>
     * If any error occurs while converting the value to a level, the
     * <code>defaultValue</code> parameter, which may be <code>null</code>,
     * is returned.
     *
     * <p>
     * Case of <code>value</code> is insignificant for the level level, but is
     * significant for the class name part, if present.
     *
     * @since 0.5
     */
    public static Level toLevel(final String value, final Level defaultValue) {
        if (value == null) {
            return defaultValue;
        }

        int hashIndex = value.indexOf('#');

        if (hashIndex == -1) {
            if ("NULL".equalsIgnoreCase(value)) {
                return null;
            } else {
                // no class name specified : use standard Level class
                try {
                    Level myLevel = Level.parse(value);
                    return myLevel;
                } catch (IllegalArgumentException e) {
                    return defaultValue;
                }

            }
        }

        Level result = defaultValue;

        String clazz = value.substring(hashIndex + 1);
        String levelName = value.substring(0, hashIndex);

        // This is degenerate case but you never know.
        if ("NULL".equalsIgnoreCase(levelName)) {
            return null;
        }

        try {
            Class customLevel = Loader.loadClass(clazz);

            // get a ref to the specified class' static method
            // toLevel(String, org.apache.log4j.Level)
            Class[] paramTypes = new Class[] {String.class, java.util.logging.Level.class};
            java.lang.reflect.Method toLevelMethod = customLevel.getMethod("toLevel", paramTypes);

            // now call the toLevel method, passing level string + default
            Object[] params = new Object[] { levelName };
            Object o = toLevelMethod.invoke(null, params);

            result = (Level) o;
        } catch (ClassNotFoundException e) {
            getLogger().log(Level.WARNING, "custom level class [" + clazz + "] not found.");
        } catch (NoSuchMethodException e) {
            getLogger().log(
                    Level.WARNING,
                    "custom level class [" + clazz + "]"
                            + " does not have a constructor which takes one string parameter", e);
        } catch (java.lang.reflect.InvocationTargetException e) {
            getLogger().log(Level.WARNING,
                    "custom level class [" + clazz + "]" + " could not be instantiated", e);
        } catch (ClassCastException e) {
            getLogger().log(Level.WARNING,
                    "class [" + clazz + "] is not a subclass of java.util.logging.Level", e);
        } catch (IllegalAccessException e) {
            getLogger().log(Level.WARNING,
                    "class [" + clazz + "] cannot be instantiated due to access restrictions", e);
        } catch (Exception e) {
            getLogger().log(Level.WARNING,
                    "class [" + clazz + "], level [" + levelName + "] conversion failed.", e);
        }

        return result;
    }

    // ------------------------------------------------------ Protected Methods

    // -------------------------------------------------------- Private Methods

    /**
     * @return the internal logger for this class
     * @since 0.5
     */
    private static Logger getLogger() {
        // TODO: this method should be removed if OptionConverter becomes a
        // static class
        return Logger.getLogger(OptionConverter.class.getName());
    }

}
