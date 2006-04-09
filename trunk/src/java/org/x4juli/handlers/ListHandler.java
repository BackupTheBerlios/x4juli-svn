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
package org.x4juli.handlers;

import java.util.ArrayList;
import java.util.List;

import org.x4juli.global.spi.ExtendedLogRecord;

/**
 * A very basic handler that takes the events and stores them in to a java.util.List for late
 * retrieval.
 * 
 * Note: This implemenation intentionally does not allow direct modification of the internal List
 * model to reduce the synchronization complexity that this would require.
 * 
 * <p>
 * Logging API as a whole was originally done for <a href="http://logging.apache.org/log4j/">Apache
 * log4j</a>. <b>x4uli</b> is a port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging APIs</a>. All
 * credits for initial idea, design, implementation, documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This file was originally
 * published by <i>Paul Smith</i>. Please use exclusively the <i>appropriate</i> mailing lists for
 * questions, remarks and contribution.
 * </p>
 * 
 * @author Boris Unckel
 * @since 0.7
 */
public class ListHandler extends AbstractHandler {

    private List list = new ArrayList();

    /**
     * Constructs a list appender.
     */
    public ListHandler() {
        super();
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    protected void appendLogRecord(ExtendedLogRecord record) {
        synchronized (list) {
            list.add(record);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void close() throws SecurityException {
        this.closed = true;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void flush() {
        // NOP
    }

    /**
     * Removes all the Events from the model
     */
    public void clearList() {
        synchronized (list) {
            list.clear();
        }
    }

    /**
     * Returns a writeable, BUT cloned List of all the LoggingEvents that are contained in the
     * internal model. You are free to modify this list without worry of synchronization, but note
     * that any modifications to the returned list that you do will have NO impact on the internal
     * model of this Appender.
     * 
     * @return Modifiable List
     */
    public final List getList() {
        synchronized (list) {
            return new ArrayList(list);
        }
    }

}

// EOF ListHandler.java
