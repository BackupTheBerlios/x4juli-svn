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

/**
 * Modifies the output of a pattern converter for a specified minimum and
 * maximum width and alignment.
 * <p>
 * Logging API as a whole was originally done for <a
 * href="http://logging.apache.org/log4j/">Apache log4j</a>. <b>Juli</b> is a
 * port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging
 * APIs</a>. All credits for initial idea, design, implementation,
 * documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This
 * file was originally published by <i>Jim Cakalic, Ceki G&uuml;lc&uuml;, Curt
 * Arnold</i>. Please use exclusively the <i>appropriate</i> mailing lists for
 * questions, remarks and contribution.
 * </p>
 *
 * @author Boris Unckel
 * @since 0.5
 */
public class FormattingInfo {

    // -------------------------------------------------------------- Variables

    /**
     * Array of spaces.
     */
    private static final char[] SPACES = new char[] { ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' };

    /**
     * Default instance.
     */
    private static final FormattingInfo DEFAULT = new FormattingInfo(false, 0, Integer.MAX_VALUE);

    /**
     * Minimum length.
     */
    private final int minLength;

    /**
     * Maximum length.
     */
    private final int maxLength;

    /**
     * Alignment.
     */
    private final boolean leftAlign;

    // ----------------------------------------------------------- Constructors

    /**
     * Creates new instance.
     *
     * @param leftAlign left align if true.
     * @param minLength minimum length.
     * @param maxLength maximum length.
     * @since 0.5
     */
    public FormattingInfo(final boolean leftAlign, final int minLength, final int maxLength) {
        this.leftAlign = leftAlign;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Gets default instance.
     *
     * @return default instance.
     * @since 0.5
     */
    public static FormattingInfo getDefault() {
        return DEFAULT;
    }

    /**
     * Determine if left aligned.
     *
     * @return true if left aligned.
     * @since 0.5
     */
    public boolean isLeftAligned() {
        return this.leftAlign;
    }

    /**
     * Get minimum length.
     *
     * @return minimum length.
     * @since 0.5
     */
    public int getMinLength() {
        return this.minLength;
    }

    /**
     * Get maximum length.
     *
     * @return maximum length.
     * @since 0.5
     */
    public int getMaxLength() {
        return this.maxLength;
    }

    /**
     * Adjust the content of the buffer based on the specified lengths and
     * alignment.
     *
     * @param fieldStart start of field in buffer.
     * @param buffer buffer to be modified.
     * @since 0.5
     */
    public final void format(final int fieldStart, final StringBuffer buffer) {
        final int rawLength = buffer.length() - fieldStart;

        if (rawLength > this.maxLength) {
            buffer.delete(fieldStart, buffer.length() - this.maxLength);
        } else if (rawLength < this.minLength) {
            if (this.leftAlign) {
                final int fieldEnd = buffer.length();
                buffer.setLength(fieldStart + this.minLength);

                for (int i = fieldEnd; i < buffer.length(); i++) {
                    buffer.setCharAt(i, ' ');
                }
            } else {
                int padLength = this.minLength - rawLength;

                for (; padLength > 8; padLength -= 8) {
                    buffer.insert(fieldStart, SPACES);
                }

                buffer.insert(fieldStart, SPACES, 0, padLength);
            }
        }
    }

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public String toString() {
        StringBuffer buf = new StringBuffer("FormattingInfo:");
        buf.append("LeftAligned[");
        buf.append(isLeftAligned());
        buf.append("] MinLength[");
        buf.append(getMinLength());
        buf.append("] MaxLength[");
        buf.append(getMaxLength());
        buf.append("]");
        return buf.toString();
    }

}

// EOF FormattingInfo.java
