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
package org.x4juli.global.spi;

import java.util.Stack;

/**
 * The NDC class implements <i>nested diagnostic contexts</i> as defined by Neil Harrison in the
 * article "Patterns for Logging Diagnostic Messages" part of the book "<i>Pattern Languages of
 * Program Design 3</i>" edited by Martin et al.
 *
 * <p>
 * A Nested Diagnostic Context, or NDC in short, is an instrument to distinguish interleaved log
 * output from different sources. Log output is typically interleaved when a server handles multiple
 * clients near-simultaneously.
 * </p>
 *
 * <p>
 * Interleaved log output can still be meaningful if each log entry from different contexts had a
 * distinctive stamp. This is where NDCs come into play.
 * </p>
 * <p>
 * <em><b>Note that NDCs are managed on a per thread basis</b></em>. NDC operations such as
 * {@link #push push}, {@link #pop}, {@link #clear}, {@link #getDepth} and {@link #setMaxDepth}
 * affect the NDC of the <em>current</em> thread only. NDCs of other threads remain unaffected.
 * </p>
 * <p>
 * For example, a servlet can build a per client request NDC consisting the clients host name and
 * other information contained in the the request. <em>Cookies</em> are another source of
 * distinctive information. To build an NDC one uses the {@link #push push} operation. Simply put,
 * </p>
 * <p>
 * <ul>
 * <li>Contexts can be nested.</li>
 *
 * <li>When entering a context, call <code>NDC.push</code>. As a side effect, if there is no
 * nested diagnostic context for the current thread, this method will create it.</li>
 *
 *
 * <li>When leaving a context, call <code>NDC.pop</code>.</li>
 *
 * </ul>
 * </p>
 * <p>
 * There is no penalty for forgetting to match each <code>push</code> operation with a
 * corresponding <code>pop</code>, except the obvious mismatch between the real application
 * context and the context set in the NDC.
 * </p>
 * <p>
 * If configured to do so, {@link org.x4juli.formatter.PatternFormatter} instances automatically retrieve the nested
 * diagnostic context for the current thread without any user intervention. Hence, even if a servlet
 * is serving multiple clients simultaneously, the logs emanating from the same code (belonging to
 * the same category) can still be distinguished because each client request will have a different
 * NDC tag.
 * </p>
 * <p>
 * A thread may inherit the nested diagnostic context of another (possibly parent) thread using the
 * {@link #inherit inherit} method. A thread may obtain a copy of its NDC with the
 * {@link #cloneStack cloneStack} method and pass the reference to any other thread, in particular
 * to a child.
 * </p>
 *
 * @author Boris Unckel
 * @since 0.7
 */
public interface NDC {
    
    /**
     * Clear any nested diagnostic information if any. This method is useful in cases where the same
     * thread can be potentially used over and over in different unrelated contexts.
     *
     * <p>
     * This method is equivalent to calling the {@link #setMaxDepth} method with a zero
     * <code>maxDepth</code> argument.
     * </p>
     *
     * @since 0.7
     */
    void clear();
    
    /**
     * Clone the diagnostic context for the current thread.
     *
     * <p>
     * Internally a diagnostic context is represented as a stack. A given thread can supply the
     * stack (i.e. diagnostic context) to a child thread so that the child can inherit the parent
     * thread's diagnostic context.
     * </p>
     *
     * <p>
     * The child thread uses the {@link #inherit inherit} method to inherit the parent's diagnostic
     * context.
     * </p>
     *
     * @return Stack A clone of the current thread's diagnostic context.
     * @since 0.7
     *
     */
    Stack cloneStack();

    /**
     * Inherit the diagnostic context of another thread.
     *
     * <p>
     * The parent thread can obtain a reference to its diagnostic context using the
     * {@link #cloneStack} method. It should communicate this information to its child so that it
     * may inherit the parent's diagnostic context.
     * </p>
     * <p>
     * The parent's diagnostic context is cloned before being inherited. In other words, once
     * inherited, the two diagnostic contexts can be managed independently.
     * </p>
     * <p>
     * In java, a child thread cannot obtain a reference to its parent, unless it is directly handed
     * the reference. Consequently, there is no client-transparent way of inheriting diagnostic
     * contexts. Do you know any solution to this problem?
     * </p>
     *
     * @param stack The diagnostic context of the parent thread.
     * @since 0.7
     */
    void inherit(Stack stack);
    
    /**
     * <font color="#FF4040"><b>Never use this method directly, use the {@link
     * org.x4juli.global.spi.ExtendedLogRecord#getNDC()} method instead</b></font>.
     * @return message of the NDC.
     * @since 0.7
     */
    String get();
    
    /**
     * Get the current nesting depth of this diagnostic context.
     *
     * @return the maximal depth.
     * @see #setMaxDepth
     * @since 0.7
     */
    int getDepth();

    /**
     * Clients should call this method before leaving a diagnostic context.
     *
     * <p>
     * The returned value is the value that was pushed last. If no context is available, then the
     * empty string "" is returned.
     * </p>
     *
     * @return String The innermost diagnostic context.
     * @since 0.7
     */
    String pop();

    /**
     * Looks at the last diagnostic context at the top of this NDC without removing it.
     *
     * <p>
     * The returned value is the value that was pushed last. If no context is available, then the
     * empty string "" is returned.
     * </p>
     *
     * @return String The innermost diagnostic context.
     * @since 0.7
     */
    String peek();

    /**
     * Push new diagnostic context information for the current thread.
     *
     * <p>
     * The contents of the <code>message</code> parameter is determined solely by the client.
     * </p>
     *
     * @param message The new diagnostic context information.
     * @since 0.7
     */
    void push(String message);

    /**
     * Set maximum depth of this diagnostic context. If the current depth is smaller or equal to
     * <code>maxDepth</code>, then no action is taken.
     *
     * <p>
     * This method is a convenient alternative to multiple {@link #pop} calls. Moreover, it is often
     * the case that at the end of complex call sequences, the depth of the NDC is unpredictable.
     * The <code>setMaxDepth</code> method circumvents this problem.
     * </p>
     * <p>
     * For example, the combination
     *
     * <pre>
     *    void foo() {
     *        int depth = NDC.getDepth();
     *
     *        ... complex sequence of calls
     *
     *        NDC.setMaxDepth(depth);
     *     }
     * </pre>
     *
     * ensures that between the entry and exit of foo the depth of the diagnostic stack is
     * conserved.
     * </p>
     *
     * @param maxDepth of ndc.
     * @see #getDepth
     * @since 0.7
     */
    void setMaxDepth(int maxDepth);
}

// EOF NDC.java
