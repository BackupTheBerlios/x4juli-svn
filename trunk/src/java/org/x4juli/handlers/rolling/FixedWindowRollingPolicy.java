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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import org.x4juli.formatter.pattern.PatternConverter;
import org.x4juli.global.Constants;
import org.x4juli.handlers.MessageText;
import org.x4juli.handlers.rolling.helper.Action;
import org.x4juli.handlers.rolling.helper.FileRenameAction;
import org.x4juli.handlers.rolling.helper.GZCompressAction;
import org.x4juli.handlers.rolling.helper.ZipCompressAction;

/**
 * When rolling over, <code>FixedWindowRollingPolicy</code> renames files
 * according to a fixed window algorithm as described below.
 * 
 * <p>
 * The <b>active file is specified by the using RollingFileHandler</b> fileName
 * property, which is required. It represents the name of the file where current
 * logging output will be written. The <b>filenamePattern</b> (required) option
 * represents the file name pattern for the archived (rolled over) log files. If
 * present, the <b>FileNamePattern</b> option must include an integer token,
 * that is the string "%i" somewhere within the pattern.
 * 
 * <p>
 * Let <em>max</em> and <em>min</em> represent the values of respectively
 * the <b>MaxIndex</b> and <b>MinIndex</b> options. Let "foo.log" be the value
 * of the <b>ActiveFile</b> option and "foo.%i.log" the value of
 * <b>FileNamePattern</b>. Then, when rolling over, the file
 * <code>foo.<em>max</em>.log</code> will be deleted, the file
 * <code>foo.<em>max-1</em>.log</code> will be renamed as
 * <code>foo.<em>max</em>.log</code>, the file
 * <code>foo.<em>max-2</em>.log</code> renamed as
 * <code>foo.<em>max-1</em>.log</code>, and so on, the file
 * <code>foo.<em>min+1</em>.log</code> renamed as
 * <code>foo.<em>min+2</em>.log</code>. Lastly, the active file
 * <code>foo.log</code> will be renamed as <code>foo.<em>min</em>.log</code>
 * and a new active file name <code>foo.log</code> will be created.
 * 
 * <p>
 * Given that this rollover algorithm requires as many file renaming operations
 * as the window size, large window sizes are discouraged. The current
 * implementation will automatically reduce the window size to 12 when larger
 * values are specified by the user.
 * <p>
 * Logging API as a whole was originally done for <a
 * href="http://logging.apache.org/log4j/">Apache log4j</a>. <b>Juli</b> is a
 * port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging
 * APIs</a>. All credits for initial idea, design, implementation,
 * documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This
 * file was originally published by <i>Ceki G&uuml;lc&uuml;</i>. Please use
 * exclusively the <i>appropriate</i> mailing lists for questions, remarks and
 * contribution.
 * </p>
 * 
 * @author Boris Unckel
 * @since 0.5
 */
public class FixedWindowRollingPolicy extends AbstractRollingPolicy {
    // -------------------------------------------------------------- Variables

    /**
     * It's almost always a bad idea to have a large window size, say over 12.
     */
    private static final int MAX_WINDOW_SIZE = 12;

    /**
     * Index for oldest retained log file.
     */
    private int maxIndex = 7;

    /**
     * Index for most recent log file.
     */
    private int minIndex = 1;

    /**
     * if true, then an explicit name for the active file was specified using
     * RollingFileHandler.filename
     */
    private boolean explicitActiveFile;

    // ----------------------------------------------------------- Constructors
    /**
     * Constructor for use with file based configuration.
     */
    public FixedWindowRollingPolicy() {
        super();
    }

    /**
     * Constructor for use in programmatically configuration.
     * 
     * @param fileNamePattern pattern to determine the filename.
     */
    public FixedWindowRollingPolicy(String fileNamePattern) {
        super(fileNamePattern);
    }

    /**
     * {@inheritDoc}
     * 
     * @since
     */
    public RolloverDescription initialize(final String file, final boolean append) throws SecurityException {
        String newActiveFile = file;
        this.explicitActiveFile = false;

        if (this.activeFileName != null) {
            this.explicitActiveFile = true;
            newActiveFile = this.activeFileName;
        }

        if (file != null) {
            this.explicitActiveFile = true;
            newActiveFile = file;
        }

        if (!this.explicitActiveFile) {
            StringBuffer buf = new StringBuffer();
            formatFileName(new Integer(this.minIndex), buf);
            newActiveFile = buf.toString();
        }

        return new RolloverDescriptionImpl(newActiveFile, append, null, null);
    }

    /**
     * {@inheritDoc}
     * 
     * @since
     */
    public RolloverDescription rollover(final String currentFileName) throws SecurityException {
        if (this.maxIndex >= 0) {
            int purgeStart = this.minIndex;

            if (!this.explicitActiveFile) {
            purgeStart++;
            }

            if (!purge(purgeStart, this.maxIndex)) {
                return null;
            }

            StringBuffer buf = new StringBuffer();
            formatFileName(new Integer(purgeStart), buf);

            String renameTo = buf.toString();
            String compressedName = renameTo;
            Action compressAction = null;

            if (renameTo.endsWith(".gz")) {
                renameTo = renameTo.substring(0, renameTo.length() - 3);
                compressAction = new GZCompressAction(new File(renameTo), new File(compressedName),
                        true);
            } else if (renameTo.endsWith(".zip")) {
                renameTo = renameTo.substring(0, renameTo.length() - 4);
                compressAction = new ZipCompressAction(new File(renameTo),
                        new File(compressedName), true);
            }

            FileRenameAction renameAction = new FileRenameAction(new File(currentFileName),
                    new File(renameTo), false);

            return new RolloverDescriptionImpl(currentFileName, false, renameAction, compressAction);
        }

        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.5
     */
    public void activateOptions() {
        super.activateOptions();

        if (this.maxIndex < this.minIndex) {
            getLogger().log(Level.WARNING, MessageText.MaxIndex_cannot_be_smaller_than_MinIndex,
                    new Integer[] { new Integer(this.maxIndex), new Integer(this.minIndex) });
            getLogger().log(Level.WARNING, MessageText.Setting_maxIndex_equal_to_minIndex);
            this.maxIndex = this.minIndex;
        }

        if ((this.maxIndex - this.minIndex) > MAX_WINDOW_SIZE) {
            getLogger().log(Level.WARNING, MessageText.Large_window_sizes_are_not_allowed);
            this.maxIndex = this.minIndex + MAX_WINDOW_SIZE;
            getLogger().log(Level.WARNING, MessageText.MaxIndex_reduced_to, new Integer(this.maxIndex));
        }

        PatternConverter itc = getIntegerPatternConverter();

        if (itc == null) {
            throw new IllegalStateException("FileNamePattern [" + getFileNamePattern()
                    + "] does not contain a valid integer format specifier");
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.5
     */
    public void configure() {
        super.configure();
        final String className = this.getClass().getName();

        // MinIndex
        String key = className + ".minIndex";
        setMinIndex(getProperty(key, 1));
        // MaxIndex
        key = className + ".maxIndex";
        setMaxIndex(getProperty(key, 7));

        // FileNamePattern
        key = className + ".filenamePattern";
        Random random = new Random(Constants.RANDOM_FOR_SEED.nextLong());
        final int maxRandomInt = 9999;
        final int randomInt = random.nextInt(maxRandomInt);
        final String defaultfileName = Constants.DEFAULT_LOG_FILE + "-" + randomInt + ".log.%i";
        String fileNameValue = getProperty(key, null);
        if (fileNameValue != null) {
            setFileNamePattern(fileNameValue);
        } else {
            getLogger().log(Level.WARNING, MessageText.No_fileNamePattern_set_for_rolling,
                    new Object[] { defaultfileName });
            setFileNamePattern(defaultfileName);
        }

    }

    /**
     * Get index of oldest log file to be retained.
     * 
     * @return index of oldest log file.
     */
    public int getMaxIndex() {
        return this.maxIndex;
    }

    /**
     * Get index of most recent log file.
     * 
     * @return index of oldest log file.
     */
    public int getMinIndex() {
        return this.minIndex;
    }

    /**
     * Set index of oldest log file to be retained.
     * 
     * @param maxIndex index of oldest log file to be retained.
     */
    public void setMaxIndex(final int maxIndex) {
        this.maxIndex = maxIndex;
    }

    /**
     * Set index of most recent log file.
     * 
     * @param minIndex Index of most recent log file.
     */
    public void setMinIndex(final int minIndex) {
        this.minIndex = minIndex;
    }

    // ------------------------------------------------------ Protected Methods

    // -------------------------------------------------------- Private Methods

    /**
     * Purge and rename old log files in preparation for rollover
     * 
     * @param lowIndex low index
     * @param highIndex high index. Log file associated with high index will be
     *            deleted if needed.
     * @return true if purge was successful and rollover should be attempted.
     */
    private boolean purge(final int lowIndex, final int highIndex) {
        int suffixLength = 0;

        List renames = new ArrayList();
        StringBuffer buf = new StringBuffer();
        formatFileName(new Integer(lowIndex), buf);

        String lowFilename = buf.toString();

        if (lowFilename.endsWith(".gz")) {
            suffixLength = 3;
        } else if (lowFilename.endsWith(".zip")) {
            suffixLength = 4;
        }

        for (int i = lowIndex; i <= highIndex; i++) {
            File toRename = new File(lowFilename);
            boolean isBase = false;

            if (suffixLength > 0) {
                File toRenameBase = new File(lowFilename.substring(0, lowFilename.length()
                        - suffixLength));

                if (toRename.exists()) {
                    if (toRenameBase.exists()) {
                        toRenameBase.delete();
                    }
                } else {
                    toRename = toRenameBase;
                    isBase = true;
                }
            }

            if (toRename.exists()) {
                //
                // if at upper index then
                // attempt to delete last file
                // if that fails then abandon purge
                if (i == highIndex) {
                    if (!toRename.delete()) {
                        return false;
                    }

                    break;
                }

                //
                // if intermediate index
                // add a rename action to the list
                buf.setLength(0);
                formatFileName(new Integer(i + 1), buf);

                String highFilename = buf.toString();
                String renameTo = highFilename;

                if (isBase) {
                    renameTo = highFilename.substring(0, highFilename.length() - suffixLength);
                }

                renames.add(new FileRenameAction(toRename, new File(renameTo), true));
                lowFilename = highFilename;
            } else {
                break;
            }
        }

        //
        // work renames backwards
        //
        for (int i = renames.size() - 1; i >= 0; i--) {
            Action action = (Action) renames.get(i);

            try {
                if (!action.execute()) {
                    return false;
                }
            } catch (Exception ex) {
                getLogger().log(Level.INFO, MessageText.Exception_during_purge, ex);
                return false;
            }
        }

        return true;
    }
}

// EOF FixedWindowRollingPolicy.java
