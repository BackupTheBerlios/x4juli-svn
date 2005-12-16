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

import java.io.IOException;

import org.x4juli.global.components.AbstractComponent;

/**
 * Abstract base class for implementations of Action.
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
public abstract class AbstractAction extends AbstractComponent implements Action {

    // -------------------------------------------------------------- Variables
    /**
     * Is action complete.
     */
    private boolean complete = false;

    /**
     * Is action interrupted.
     */
    private boolean interrupted = false;

    // ----------------------------------------------------------- Constructors

    /**
     *
     */
    protected AbstractAction() {
        super();
    }

    // --------------------------------------------------------- Public Methods

    /**
     * {@inheritDoc}
     */
    public synchronized void close() {
        this.interrupted = true;
    }

    /**
     * Tests if the action is complete.
     * @return true if action is complete.
     */
    public synchronized boolean isComplete() {
      return this.complete;
    }

    /**
     * Perform action.
     *
     * @throws IOException if IO error.
     * @return true if successful.
     */
    public abstract boolean execute() throws IOException;

    /**
     * {@inheritDoc}
     */
    public synchronized void run() {
      if (!this.interrupted) {
        try {
          execute();
        } catch (IOException ex) {
          reportException(ex);
        }

        this.complete = true;
        this.interrupted = true;
      }
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Capture exception.
     *
     * @param ex exception.
     */
    protected void reportException(final Exception ex) {
        //NOP
    }

}

// EOF AbstractAction.java
