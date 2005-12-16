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
package org.x4juli.handlers.rolling;

import java.io.File;
import java.util.Date;
import java.util.Random;
import java.util.logging.Handler;
import java.util.logging.Level;

import org.x4juli.formatter.pattern.PatternConverter;
import org.x4juli.global.Constants;
import org.x4juli.global.spi.ExtendedLogRecord;
import org.x4juli.handlers.MessageText;
import org.x4juli.handlers.rolling.helper.Action;
import org.x4juli.handlers.rolling.helper.FileRenameAction;
import org.x4juli.handlers.rolling.helper.GZCompressAction;
import org.x4juli.handlers.rolling.helper.ZipCompressAction;

/**
 * <code>TimeBasedRollingPolicy</code> is both easy to configure and quite
 * powerful.
 * <p>
 * The <b>active file is specified by the using RollingFileHandler</b>  fileName property, 
 * which is required. It represents the name
 * of the file where current logging output will be written. The
 * <b>filenamePattern</b> (required) option represents the file name pattern for the
 * archived (rolled over) log files.
 * </p>
 * <p>
 * In order to use <code>TimeBasedRollingPolicy</code>, the
 * <b>FileNamePattern</b> option must be set. It basically specifies the name
 * of the rolled log files. The value <code>FileNamePattern</code> should
 * consist of the name of the file, plus a suitably placed <code>%d</code>
 * conversion specifier. The <code>%d</code> conversion specifier may contain
 * a date and time pattern as specified by the
 * {@link java.text.SimpleDateFormat} class. If the date and time pattern is
 * ommitted, then the default pattern of "yyyy-MM-dd" is assumed. The following
 * examples should clarify the point.
 *
 * <p>
 * <table cellspacing="5px" border="1">
 * <tr>
 * <th><code>filenamePattern</code> value</th>
 * <th>Rollover schedule</th>
 * <th>Example</th>
 * </tr>
 * <tr>
 * <td nowrap="true"><code>/wombat/folder/foo.%d</code></td>
 * <td>Daily rollover (at midnight). Due to the omission of the optional time
 * and date pattern for the %d token specifier, the default pattern of
 * "yyyy-MM-dd" is assumed, which corresponds to daily rollover. </td>
 * <td>During November 23rd, 2004, logging output will go to the file
 * <code>/wombat/foo.2004-11-23</code>. At midnight and for the rest of the
 * 24th, logging output will be directed to <code>/wombat/foo.2004-11-24</code>.
 * </td>
 * </tr>
 * <tr>
 * <td nowrap="true"><code>/wombat/foo.%d{yyyy-MM}.log</code></td>
 * <td>Rollover at the beginning of each month.</td>
 * <td>During the month of October 2004, logging output will go to
 * <code>/wombat/foo.2004-10.log</code>. After midnight of October 31st and
 * for the rest of November, logging output will be directed to
 * <code>/wombat/foo.2004-11.log</code>. </td>
 * </tr>
 * </table>
 * <h2>Automatic file compression</h2>
 * <code>TimeBasedRollingPolicy</code> supports automatic file compression.
 * This feature is enabled if the value of the <b>FileNamePattern</b> option
 * ends with <code>.gz</code> or <code>.zip</code>.
 * <p>
 * <table cellspacing="5px" border="1">
 * <tr>
 * <th><code>FileNamePattern</code> value</th>
 * <th>Rollover schedule</th>
 * <th>Example</th>
 * </tr>
 * <tr>
 * <td nowrap="true"><code>/wombat/foo.%d.gz</code></td>
 * <td>Daily rollover (at midnight) with automatic GZIP compression of the
 * archived files.</td>
 * <td>During November 23rd, 2004, logging output will go to the file
 * <code>/wombat/foo.2004-11-23</code>. However, at midnight that file will
 * be compressed to become <code>/wombat/foo.2004-11-23.gz</code>. For the
 * 24th of November, logging output will be directed to
 * <code>/wombat/folder/foo.2004-11-24</code> until its rolled over at the
 * beginning of the next day. </td>
 * </tr>
 * </table>
 *
 * <p>
 * If configuring programatically, do not forget to call
 * {@link #activateOptions} method before using this policy. Moreover,
 * {@link #activateOptions} of <code> TimeBasedRollingPolicy</code> must be
 * called <em>before</em> calling the {@link #activateOptions} method of the
 * owning <code>RollingFileHandler</code>.
 * <p>
 * Logging API as a whole was originally done for <a
 * href="http://logging.apache.org/log4j/">Apache log4j</a>. <b>Juli</b> is a
 * port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging
 * APIs</a>. All credits for initial idea, design, implementation,
 * documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This
 * file was originally published by <i>Ceki G&uuml;lc&uuml;, Curt Arnold</i>.
 * Please use exclusively the <i>appropriate</i> mailing lists for questions,
 * remarks and contribution.
 * </p>
 *
 * @author Boris Unckel
 * @since 0.5
 */
public class TimeBasedRollingPolicy extends AbstractRollingPolicy implements TriggeringPolicy {
    // -------------------------------------------------------------- Variables

    /**
     * Time for next determination if time for rollover.
     */
    private long nextCheck = 0;

    /**
     * File name at last rollover.
     */
    private String lastFileName = null;

    /**
     * Length of any file type suffix (.gz, .zip).
     */
    private int suffixLength = 0;

    // ----------------------------------------------------------- Constructors

    /**
     * Constructor for use with file based configuration.
     */
    public TimeBasedRollingPolicy() {
        super();
    }

    /**
     * Constructor for use in programmatically configuration.
     * @param fileNamePattern pattern to determine the filename.
     */
    public TimeBasedRollingPolicy(String fileNamePattern) {
        super(fileNamePattern);
    }

    // --------------------------------------------------------- Public Methods

    /**
     * {@inheritDoc}
     *
     * @since 0.5
     */
    public boolean isTriggeringEvent(final Handler handler, final ExtendedLogRecord record,
            final String filename, final long fileLength) {
        boolean ret = System.currentTimeMillis() >= this.nextCheck;
        return ret;
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.5
     */
    public RolloverDescription initialize(final String currentActiveFile, final boolean append)
            throws SecurityException {
        long n = System.currentTimeMillis();
        this.nextCheck = ((n / 1000) + 1) * 1000;

        StringBuffer buf = new StringBuffer();
        formatFileName(new Date(n), buf);
        this.lastFileName = buf.toString();

        //
        // RollingPolicyBase.activeFileName duplicates RollingFileHandler.file
        // and should be removed.
        //
        RolloverDescriptionImpl ret = null;
        if (this.activeFileName != null) {
            ret = new RolloverDescriptionImpl(this.activeFileName, append, null, null);
            return ret;
        } else if (currentActiveFile != null) {
            ret = new RolloverDescriptionImpl(currentActiveFile, append, null, null);
            return ret;
        } else {
            ret = new RolloverDescriptionImpl(this.lastFileName.substring(0, this.lastFileName.length()
                    - this.suffixLength), append, null, null);
            return ret;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.5
     */
    public RolloverDescription rollover(final String currentActiveFile) throws SecurityException {
        long n = System.currentTimeMillis();
        this.nextCheck = ((n / 1000) + 1) * 1000;

        StringBuffer buf = new StringBuffer();
        formatFileName(new Date(n), buf);

        String newFileName = buf.toString();

        //
        // if file names haven't changed, no rollover
        //
        if (newFileName.equals(this.lastFileName)) {
            return null;
        }

        Action renameAction = null;
        Action compressAction = null;
        String lastBaseName = this.lastFileName.substring(0, this.lastFileName.length() - this.suffixLength);
        String nextActiveFile = newFileName.substring(0, newFileName.length() - this.suffixLength);

        //
        // if currentActiveFile is not lastBaseName then
        // active file name is not following file pattern
        // and requires a rename plus maintaining the same name
        if (!currentActiveFile.equals(lastBaseName)) {
            renameAction = new FileRenameAction(new File(currentActiveFile),
                    new File(lastBaseName), true);
            nextActiveFile = currentActiveFile;
        }

        if (this.suffixLength == 3) {
            compressAction = new GZCompressAction(new File(lastBaseName), new File(this.lastFileName),
                    true);
        }

        if (this.suffixLength == 4) {
            compressAction = new ZipCompressAction(new File(lastBaseName), new File(this.lastFileName),
                    true);
        }

        this.lastFileName = newFileName;
        RolloverDescriptionImpl ret = new RolloverDescriptionImpl(nextActiveFile, false, renameAction, compressAction);
        return ret;
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.5
     */
    public void activateOptions() {
        super.activateOptions();
        PatternConverter dtc = getDatePatternConverter();

        if (dtc == null) {
            throw new IllegalStateException("FileNamePattern [" + getFileNamePattern()
                    + "] does not contain a valid date format specifier");
        }

        long n = System.currentTimeMillis();
        StringBuffer buf = new StringBuffer();
        formatFileName(new Date(n), buf);
        this.lastFileName = buf.toString();

        this.suffixLength = 0;

        if (this.lastFileName.endsWith(".gz")) {
            this.suffixLength = 3;
        } else if (this.lastFileName.endsWith(".zip")) {
            this.suffixLength = 4;
        }
    }

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public void configure() {
        super.configure();
        //FileNamePattern
        final String className = this.getClass().getName();
        String key = className + ".filenamePattern";
        Random random = new Random(Constants.RANDOM_FOR_SEED.nextLong());
        final int maxRandomInt = 9999;
        int randomInt = random.nextInt(maxRandomInt);
        final String defaultfileName = Constants.DEFAULT_LOG_FILE + "-" + randomInt +"-%d{yyyy-MM-dd}.log";
        String fileNameValue = getProperty(key, null);
        if(fileNameValue != null){
            setFileNamePattern(fileNameValue);
        } else {
            getLogger().log(Level.WARNING,
                    MessageText.No_fileNamePattern_set_for_rolling,
                    new Object[]{defaultfileName});
            setFileNamePattern(defaultfileName);
        }
    }

}

// EOF TimeBasedRollingPolicy.java
