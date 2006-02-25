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
package org.x4juli.global.context;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * The MDC class is similar to the {@link NDC} class except that it is based on a map instead of a
 * stack. It provides <em>mapped diagnostic contexts</em>. A <em>Mapped Diagnostic Context</em>,
 * or MDC in short, is an instrument for distinguishing interleaved log output from different
 * sources. Log output is typically interleaved when a server handles multiple clients
 * near-simultaneously.
 * 
 * <p>
 * <b><em>The MDC is managed on a per thread basis</em></b>. A child thread automatically
 * inherits a <em>copy</em> of the mapped diagnostic context of its parent.
 * </p>
 * 
 * <p>
 * Logging API as a whole was originally done for <a href="http://logging.apache.org/log4j/">Apache
 * log4j</a>. <b>Juli</b> is a port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging APIs</a>. All
 * credits for initial idea, design, implementation, documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This file was originally
 * published by <i>Ceki G&uuml;lc&uuml;</i>. Please use exclusively the <i>appropriate</i> mailing
 * lists for questions, remarks and contribution.
 * </p>
 * 
 * @author Boris Unckel
 * @since 0.7
 */
final class MDCImpl implements MDC {
    
    // -------------------------------------------------------------- Variables

    private static final int HT_SIZE = 7;

    private static final ThreadLocalMap TLM = new ThreadLocalMap();

    // ----------------------------------------------------------- Constructors

    /**
     * Instantiation through factory.
     */
    MDCImpl() {
    }

    // --------------------------------------------------------- Public Methods

    /**
     * {@inheritDoc}
     * @param key to store information.
     * @param val value to store.
     * @since 0.7
     */
    public void put(String key, String val) {
        Hashtable ht = (Hashtable) TLM.get();

        if (ht == null) {
            ht = new Hashtable(HT_SIZE);
            TLM.set(ht);
        }

        ht.put(key, val);
    }

    /**
     * {@inheritDoc}
     * 
     * @param key as identifier.
     * @return the context.
     * @since 0.7
     */
    public String get(String key) {
        Hashtable ht = (Hashtable) TLM.get();

        if ((ht != null) && (key != null)) {
            return (String) ht.get(key);
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     * @param key as identifier.
     * @since 0.7
     */
    public void remove(String key) {
        Hashtable ht = (Hashtable) TLM.get();

        if (ht != null) {
            ht.remove(key);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void clear() {
        Hashtable ht = (Hashtable) TLM.get();

        if (ht != null) {
            ht.clear();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return current MDC as hastable.
     * @since 0.7
     */
    public Hashtable getContext() {
        return (Hashtable) TLM.get();
    }

    /**
     * {@inheritDoc}
     *
     * @return current keys in the MDC. Can be null.
     * @since 0.7
     */
    public Enumeration getKeys() {
        Hashtable ht = (Hashtable) TLM.get();

        if (ht != null) {
            return ht.keys();
        } else {
            return null;
        }
    }
}

// EOF MDC.java
