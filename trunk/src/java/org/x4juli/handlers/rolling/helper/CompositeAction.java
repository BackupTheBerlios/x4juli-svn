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
import java.util.List;
import java.util.logging.Level;

import org.x4juli.handlers.MessageText;

/**
 * A group of Actions to be executed in sequence.
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
public class CompositeAction extends AbstractAction {

    // -------------------------------------------------------------- Variables

    /**
     * Actions to perform.
     */
    private final Action[] actions;

    /**
     * Stop on error.
     */
    private final boolean stopOnError;

    // ----------------------------------------------------------- Constructors
    /**
     * Construct a new composite action.
     *
     * @param actions
     *            list of actions, may not be null.
     * @param stopOnError
     *            if true, stop on the first false return value or exception.
     */
    public CompositeAction(final List actions, final boolean stopOnError) {
        this.actions = new Action[actions.size()];
        actions.toArray(this.actions);
        this.stopOnError = stopOnError;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * {@inheritDoc}
     */
    public void run() {
        try {
            execute();
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE,
                    MessageText.Exception_during_file_rollover, ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean execute() throws IOException {
        if (this.stopOnError) {
            for (int i = 0; i < this.actions.length; i++) {
                if (!this.actions[i].execute()) {
                    return false;
                }
            }
            return true;
        } else {
            boolean status = true;
            IOException exception = null;

            for (int i = 0; i < this.actions.length; i++) {
                try {
                    status &= this.actions[i].execute();
                } catch (IOException ex) {
                    status = false;

                    if (exception == null) {
                        exception = ex;
                    }
                }
            }

            if (exception != null) {
                throw exception;
            }

            return status;
        }
    }

}

// EOF CompositeAction.java
