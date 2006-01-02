/*
 * Copyright 2006 x4juli.org.
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
 * @author Boris Unckel
 * @since 0.7
 */
public interface MDC {

    /**
     * Put a context value (the <code>val</code> parameter) as identified with the
     * <code>key</code> parameter into the current thread's context map.
     *
     * <p>
     * If the current thread does not have a context map it is created as a side effect.
     * </p>
     *
     * @param key to store information.
     * @param val value to store.
     * @since 0.7
     */
    void put(String key, String val);

    /**
     * Get the context identified by the <code>key</code> parameter.
     * 
     * <p>
     * This method has no side effects.
     * </p>
     * 
     * @param key as identifier.
     * @return the context.
     * @since 0.7
     */
    String get(String key);

    /**
     * Remove the the context identified by the <code>key</code> parameter.
     * @param key as identifier.
     * @since 0.7
     */
    void remove(String key);

    /**
     * Clear all entries in the MDC.
     * 
     * @since 0.7
     */
    void clear();

    /**
     * Get the current thread's MDC as a hashtable. This method is intended to be used internally.
     * @return current MDC as hastable.
     * @since 0.7
     */
    Hashtable getContext();

    /**
     * Returns the keys in the MDC as an {@link Enumeration}.
     * @return current keys in the MDC. Can be null.
     * @since 0.7
     */
    Enumeration getKeys();
}

// EOF MDC.java
