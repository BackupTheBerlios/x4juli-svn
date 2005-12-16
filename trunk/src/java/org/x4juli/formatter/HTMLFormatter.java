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
package org.x4juli.formatter;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.x4juli.formatter.pattern.FormattingInfo;
import org.x4juli.formatter.pattern.LiteralPatternConverter;
import org.x4juli.formatter.pattern.LogRecordPatternConverter;
import org.x4juli.formatter.pattern.PatternConverter;
import org.x4juli.formatter.pattern.PatternParser;
import org.x4juli.global.LoggerRepositoryHolder;
import org.x4juli.global.SystemUtils;
import org.x4juli.global.components.AbstractFormatter;
import org.x4juli.global.helper.Transform;
import org.x4juli.global.spi.ExtendedLogRecord;
import org.x4juli.global.spi.ThrowableInformation;

/**
 * <p>
 * Logging API as a whole was originally done for <a
 * href="http://logging.apache.org/log4j/">Apache log4j</a>. <b>Juli</b> is a
 * port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging
 * APIs</a>. All credits for initial idea, design, implementation,
 * documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This
 * file was originally published by <i>Ceki G&uuml;lc&uuml;, Steve Mactaggart
 * </i>. Please use exclusively the <i>appropriate</i> mailing lists for
 * questions, remarks and contribution.
 * </p>
 *
 * @author Boris Unckel
 * @since 0.5
 */
public class HTMLFormatter extends AbstractFormatter {

    // -------------------------------------------------------------- Variables

    /**
     * Prefix for Stacktraces.
     */
    protected static final String TRACE_PREFIX = "<br>&nbsp;&nbsp;&nbsp;&nbsp;";

    /**
     * Customized pattern conversion rules are stored under this key in the
     * {@link LoggerRepositoryHolder} object store.
     */
    private static final String PATTERN_RULE_REGISTRY = "PATTERN_RULE_REGISTRY";

    private String pattern;

    // Unused Field?
    //private PatternConverter head;

    // Unused Field?
    //private String timezone;

    private String title = "x4juli Log Messages";

    private boolean internalCSS = false;

    private String url2ExternalCSS = "http://logging.apache.org/log4j/docs/css/eventTable-1.0.css";

    // Does our PatternConverter chain handle throwable on its own?
    private boolean chainHandlesThrowable;

    // counter keeping track of the rows output
    private long counter = 0;

    private LogRecordPatternConverter[] patternConverters;
    private FormattingInfo[] patternFields;

    // ----------------------------------------------------------- Constructors

    /**
     * Constructs a PatternLayout using the
     * <code>PatternFormatter.DEFAULT_CONVERSION_PATTERN</code>.
     *
     * The default pattern just produces the application supplied message.
     */
    public HTMLFormatter() {
        this(PatternFormatter.DEFAULT_CONVERSION_PATTERN);
    }

    /**
     * Constructs a PatternLayout using the supplied conversion pattern.
     * @param pattern to format logrecords.
     */
    public HTMLFormatter(final String pattern) {
        this.pattern = pattern;
        activateOptions();
    }

    // --------------------------------------------------------- Public Methods

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public String doFormat(final ExtendedLogRecord extRecord) {
        LogRecord record = (LogRecord) extRecord;
        StringWriter output = new StringWriter();
        try {
            boolean odd = true;
            if (((this.counter++) & 1) == 0) {
                odd = false;
            }
            String level = record.getLevel().toString().toLowerCase();
            StringBuffer buf = new StringBuffer();
            buf.append(SystemUtils.LINE_SEPARATOR);
            buf.append("<tr class=\"");
            buf.append(level);
            if (odd) {
              buf.append(" odd\">");
            } else {
              buf.append(" even\">");
            }
            buf.append(SystemUtils.LINE_SEPARATOR);

            for (int i = 0; i < this.patternConverters.length; i++) {
                PatternConverter c = this.patternConverters[i];
                buf.append("<td class=\"");
                buf.append(c.getStyleClass(extRecord).toLowerCase());
                buf.append("\">");
                int fieldStart = buf.length();
                c.format(extRecord, buf);
                this.patternFields[i].format(fieldStart, buf);
                buf.append("</td>");
                buf.append(SystemUtils.LINE_SEPARATOR);
            }
            buf.append("</tr>");
            buf.append(SystemUtils.LINE_SEPARATOR);
            output.write(buf.toString());
            // if the pattern chain handles throwables then no need to do it
            // again
            // here.
            if (!this.chainHandlesThrowable) {
                String[] s = null;
                if (record.getThrown() != null) {
                    ThrowableInformation ti = new ThrowableInformation(record.getThrown());
                    s = ti.getThrowableStrRep();
                }
                if (s != null) {
                    output.write("<tr><td class=\"exception\" colspan=\"6\">");
                    appendThrowableAsHTML(s, output);
                    output.write("</td></tr>" + SystemUtils.LINE_SEPARATOR);
                }
            }
        } catch (IOException e) {
            // Should not occure in an StringWriter so something curious is
            // wrong
            String errtxt = "format StringWritter IOException";
            getLogger().log(Level.SEVERE, MessageText.Unexcpected_IO_Exception_during_formating, e);
            throw new RuntimeException(errtxt, e);
        }
        return output.toString();
    }

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public void activateOptions() {
        List converters = new ArrayList();
        List fields = new ArrayList();
        Map converterRegistry = null;
        if (this.repository != null) {
            converterRegistry = (Map) this.repository.getObject(PATTERN_RULE_REGISTRY);
        }
        PatternParser.parse(this.pattern, converters, fields, converterRegistry, PatternParser
                .getPatternLayoutRules());

        this.patternConverters = new LogRecordPatternConverter[converters.size()];
        this.patternFields = new FormattingInfo[converters.size()];

        int i = 0;
        Iterator converterIter = converters.iterator();
        Iterator fieldIter = fields.iterator();
        while (converterIter.hasNext()) {
            Object converter = converterIter.next();
            if (converter instanceof LogRecordPatternConverter) {
                this.patternConverters[i] = (LogRecordPatternConverter) converter;
                this.chainHandlesThrowable |= this.patternConverters[i].handlesThrowable();
            } else {
                this.patternConverters[i] = new LiteralPatternConverter("");
            }
            if (fieldIter.hasNext()) {
                this.patternFields[i] = (FormattingInfo) fieldIter.next();
            } else {
                this.patternFields[i] = FormattingInfo.getDefault();
            }
            i++;
        }
    }

    /**
     * Set the <b>ConversionPattern </b> option. This is the string which
     * controls formatting and consists of a mix of literal content and
     * conversion specifiers.
     * @param conversionPattern controls formatting.
     * @since 0.5
     */
    public void setConversionPattern(final String conversionPattern) {
        this.pattern = conversionPattern;
    }

    /**
     * The conversionpattern.
     * @return the value of the <b>ConversionPattern </b> option.
     * @since 0.5
     */
    public String getConversionPattern() {
        return this.pattern;
    }

    /**
     * The <b>Title </b> option takes a String value. This option sets the
     * document title of the generated HTML document.
     *
     * <p>
     * Defaults to 'x4juli Log Messages'.
     * </p>
     * @param title of the page.
     * @since 0.5
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * Returns the current value of the <b>Title </b> option.
     * @return the title.
     * @since 0.5
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Returns the value of the internalCSS option. See {@link #setInternalCSS}
     * method for details about the meaning of this option.
     *
     * @return boolean Value of internalCSS option
     * @since 0.5
     */
    public boolean isInternalCSS() {
        return this.internalCSS;
    }

    /**
     * Set the value of the internalCSS option. If set to true, the generated
     * HTML ouput will include an internal cascading style sheet. Otherwise, the
     * generated HTML output will include a reference to an external CSS.
     * <p>
     * By default, <code>internalCSS</code> value is set to false, that is, by
     * default, only a link to an external CSS file will be generated.
     *
     * @see #setURL2ExternalCSS
     *
     * @param internalCSS to use or not.
     * @since 0.5
     */
    public void setInternalCSS(final boolean internalCSS) {
        this.internalCSS = internalCSS;
    }

    /**
     * Return the URL to the external CSS file. See {@link #setURL2ExternalCSS}
     * method for details about the meaning of this option.
     *
     * @return URL to the external CSS file.
     * @since 0.5
     */
    public String getURL2ExternalCSS() {
        return this.url2ExternalCSS;
    }

    /**
     * Set the URL for the external CSS file. By default, the external CSS file
     * is set to "http://logging.apache.org/log4j/docs/css/eventTable-1.0.css".
     * @param url2ExternalCss URL where to find the CSS.
     * @since 0.5
     */
    public void setURL2ExternalCSS(final String url2ExternalCss) {
        this.url2ExternalCSS = url2ExternalCss;
    }

    /**
     * The Content type.
     * @return the content type output by this layout, i.e "text/html".
     * @since 0.5
     */
    public String getContentType() {
        return "text/html";
    }

    /**
     * Returns appropriate HTML headers.
     * @return the header.
     * @since 0.5
     */
    public String getHeader() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"");
        sbuf.append(" \"http://www.w3.org/TR/html4/loose.dtd\">");
        sbuf.append(SystemUtils.LINE_SEPARATOR);
        sbuf.append("<html>");
        sbuf.append(SystemUtils.LINE_SEPARATOR);
        sbuf.append("<head>");
        sbuf.append(SystemUtils.LINE_SEPARATOR);
        sbuf.append("<title>" + this.title + "</title>");
        sbuf.append(SystemUtils.LINE_SEPARATOR);
        if (this.internalCSS) {
            getInternalCSS(sbuf);
        } else {
            sbuf.append("<LINK REL=StyleSheet HREF=\"" + this.url2ExternalCSS
                         + "\" TITLE=\"Basic\">");
        }
        sbuf.append(SystemUtils.LINE_SEPARATOR);
        sbuf.append("</head>");
        sbuf.append(SystemUtils.LINE_SEPARATOR);
        sbuf.append("<body>");
        sbuf.append(SystemUtils.LINE_SEPARATOR);

        sbuf.append("<hr size=\"1\" noshade>");
        sbuf.append(SystemUtils.LINE_SEPARATOR);

        sbuf.append("Log session start time " + new java.util.Date() + "<br>");
        sbuf.append(SystemUtils.LINE_SEPARATOR);
        sbuf.append("<br>");
        sbuf.append(SystemUtils.LINE_SEPARATOR);
        sbuf.append("<table cellspacing=\"0\">");
        sbuf.append(SystemUtils.LINE_SEPARATOR);

        sbuf.append("<tr class=\"header\">");
        sbuf.append(SystemUtils.LINE_SEPARATOR);
        for (int i = 0; i < this.patternConverters.length; i++) {
            PatternConverter c = this.patternConverters[i];
            sbuf.append("<td class=\"");
            sbuf.append(c.getStyleClass(null).toLowerCase());
            sbuf.append("\">");
            sbuf.append(c.getName());
            sbuf.append("</td>");
            sbuf.append(SystemUtils.LINE_SEPARATOR);
        }
        sbuf.append("</tr>");
        sbuf.append(SystemUtils.LINE_SEPARATOR);

        return sbuf.toString();
    }

    /**
     * The footer.
     * @return the appropriate HTML footers.
     * @since 0.5
     */
    public String getFooter() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("</table>" + SystemUtils.LINE_SEPARATOR);
        sbuf.append("<br>" + SystemUtils.LINE_SEPARATOR);
        sbuf.append("</body></html>");
        return sbuf.toString();
    }

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public String getHead(final Handler h) {
        return getHeader();
    }

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public String getTail(final Handler h) {
        return getFooter();
    }

    /**
     * The HTML layout handles the throwable contained in logging events. Hence,
     * this method return <code>false</code>.
     * @return false
     * @since 0.5
     */
    public boolean ignoresThrowable() {
        return false;
    }

    // -----------------------------------------------Package Protected Methods

    /**
     * Write an Throwable as HTML code.
     * @param s the throwable as String array.
     * @param sbuf where to append the output.
     * @throws IOException if write fails.
     * @since 0.5
     */
    void appendThrowableAsHTML(final String[] s, final Writer sbuf) throws IOException {
        if (s != null) {
            int len = s.length;
            if (len == 0) {
                return;
            }
            Transform.escapeTags(s[0], sbuf);
            sbuf.write(SystemUtils.LINE_SEPARATOR);
            for (int i = 1; i < len; i++) {
                sbuf.write(TRACE_PREFIX);
                Transform.escapeTags(s[i], sbuf);
                sbuf.write(SystemUtils.LINE_SEPARATOR);
            }
        }
    }

    /**
     * Generate an internal CSS file.
     *
     * @param buf The StringBuffer where the CSS file will be placed.
     * @since 0.5
     */
    void getInternalCSS(final StringBuffer buf) {

        buf.append("<STYLE  type=\"text/css\">");
        buf.append(SystemUtils.LINE_SEPARATOR);
        buf.append("table { margin-left: 2em; margin-right: 2em; border-left: 2px solid #AAA; }");
        buf.append(SystemUtils.LINE_SEPARATOR);

        buf.append("TR.even { background: #FFFFFF; }");
        buf.append(SystemUtils.LINE_SEPARATOR);

        buf.append("TR.odd { background: #DADADA; }");
        buf.append(SystemUtils.LINE_SEPARATOR);

        buf.append("TR.warn TD.level, TR.error TD.level,");
        buf.append("TR.fatal TD.level {font-weight: bold; color: #FF4040 }");
        buf.append(SystemUtils.LINE_SEPARATOR);

        buf.append("TD { padding-right: 1ex; padding-left: 1ex; border-right: 2px solid #AAA; }");
        buf.append(SystemUtils.LINE_SEPARATOR);

        buf.append("TD.time, TD.date { text-align: right; font-family: ");
        buf.append("courier, monospace; font-size: smaller; }");
        buf.append(SystemUtils.LINE_SEPARATOR);

        buf.append("TD.sn { text-align: right; width: 5ex; font-family: ");
        buf.append("courier, monospace; font-size: smaller; }");
        buf.append(SystemUtils.LINE_SEPARATOR);

        buf.append("TD.thread { text-align: left; }");
        buf.append(SystemUtils.LINE_SEPARATOR);

        buf.append("TD.level { text-align: right; }");
        buf.append(SystemUtils.LINE_SEPARATOR);

        buf.append("TD.logger { text-align: left; }");
        buf.append(SystemUtils.LINE_SEPARATOR);

        buf.append("TR.header { background: #9090FF; color: ");
        buf.append("#FFF; font-weight: bold; font-size: larger; }");
        buf.append(SystemUtils.LINE_SEPARATOR);

        buf.append("TD.exception { background: #C0C0F0; font-family: courier, monospace;}");
        buf.append(SystemUtils.LINE_SEPARATOR);

        buf.append("</STYLE>");
        buf.append(SystemUtils.LINE_SEPARATOR);

    }

    // ------------------------------------------------------ Protected Methods


}
