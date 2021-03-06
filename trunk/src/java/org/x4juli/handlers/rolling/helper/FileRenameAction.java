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
package org.x4juli.handlers.rolling.helper;

import java.io.File;

/**
 * File rename action.
 * <p>
 * Logging API as a whole was originally done for <a
 * href="http://logging.apache.org/log4j/">Apache log4j</a>. <b>Juli</b> is a
 * port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging
 * APIs</a>. All credits for initial idea, design, implementation,
 * documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This
 * file was originally published by <i>Curt Arnold</i>. Please use exclusively
 * the <i>appropriate</i> mailing lists for questions, remarks and
 * contribution.
 * </p>
 *
 * @author Boris Unckel
 * @since 0.5
 */
public class FileRenameAction extends AbstractAction {

    // -------------------------------------------------------------- Variables

    /**
     * Source.
     */
    private final File source;

    /**
     * Destination.
     */
    private final File destination;

    /**
     * If true, rename empty files, otherwise delete empty files.
     */
    private final boolean renameEmptyFiles;

    // ----------------------------------------------------------- Constructors

    /**
     * Creates an FileRenameAction.
     *
     * @param src current file name.
     * @param dst new file name.
     * @param renameEmptyFiles if true, rename file even if empty, otherwise delete empty files.
     */
    public FileRenameAction(
      final File src, final File dst, boolean renameEmptyFiles) {
      this.source = src;
      this.destination = dst;
      this.renameEmptyFiles = renameEmptyFiles;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Rename file.
     * @param source current file name.
     * @param destination new file name.
     * @param renameEmptyFiles if true, rename file even if empty, otherwise delete empty files.
     * @return true if successfully renamed.
     */
    public static boolean execute(
      final File source, final File destination, boolean renameEmptyFiles) {
      if (renameEmptyFiles || (source.length() > 0)) {
        return source.renameTo(destination);
      }

      return source.delete();
    }


    /**
     * Rename file.
     *
     * @return true if successfully renamed.
     */
    public boolean execute() {
      return execute(this.source, this.destination, this.renameEmptyFiles);
    }

}

// EOF FileRenameAction.java
