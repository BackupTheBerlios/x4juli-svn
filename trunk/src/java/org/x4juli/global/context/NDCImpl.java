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
final class NDCImpl implements NDC {

    // -------------------------------------------------------------- Variables

    /**
     * The synchronized keyword is not used in this class. This may seem dangerous, especially since
     * the class will be used by multiple-threads. This is OK since java Stacks are thread safe.
     * More importantly, when inheriting diagnostic contexts the child thread is handed a clone of
     * the parent's NDC. It follows that each thread has its own NDC (i.e. stack).
     */
    private static final ThreadLocal TL = new ThreadLocal();

    // after the latest call to lazyRemove
    private static final int REAP_THRESHOLD = 5;

    private static int pushCounter = 0; // the number of times push has been called

    // ----------------------------------------------------------- Constructors

    /**
     * Instantiation through Factory Method.
     */
    NDCImpl() {
        super();
    }

    // --------------------------------------------------------- Public Methods

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void clear() {
        Stack stack = (Stack) TL.get();

        if (stack != null) {
            stack.setSize(0);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return Stack A clone of the current thread's diagnostic context.
     * @since 0.7
     *
     */
    public Stack cloneStack() {
        Object o = TL.get();

        if (o == null) {
            return null;
        } else {
            Stack stack = (Stack) o;

            return (Stack) stack.clone();
        }
    }

    /**
     * {@inheritDoc}
     * @param stack The diagnostic context of the parent thread.
     * @since 0.7
     */
    public void inherit(Stack stack) {
        if (stack != null) {
            TL.set(stack);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return message of the NDC.
     * @since 0.7
     */
    public String get() {
        Stack s = (Stack) TL.get();

        if ((s != null) && !s.isEmpty()) {
            return ((DiagnosticContext) s.peek()).fullMessage;
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return the maximal depth.
     * @see #setMaxDepth
     * @since 0.7
     */
    public int getDepth() {
        Stack stack = (Stack) TL.get();

        if (stack == null) {
            return 0;
        } else {
            return stack.size();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return String The innermost diagnostic context.
     * @since 0.7
     */
    public String pop() {
        Stack stack = (Stack) TL.get();

        if ((stack != null) && !stack.isEmpty()) {
            return ((DiagnosticContext) stack.pop()).message;
        } else {
            return "";
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return String The innermost diagnostic context.
     * @since 0.7
     */
    public String peek() {
        Stack stack = (Stack) TL.get();

        if ((stack != null) && !stack.isEmpty()) {
            return ((DiagnosticContext) stack.peek()).message;
        } else {
            return "";
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param message The new diagnostic context information.
     * @since 0.7
     */
    public void push(String message) {
        Stack stack = (Stack) TL.get();

        if (stack == null) {
            DiagnosticContext dc = new DiagnosticContext(message, null);
            stack = new Stack();
            TL.set(stack);
            stack.push(dc);
        } else if (stack.isEmpty()) {
            DiagnosticContext dc = new DiagnosticContext(message, null);
            stack.push(dc);
        } else {
            DiagnosticContext parent = (DiagnosticContext) stack.peek();
            stack.push(new DiagnosticContext(message, parent));
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param maxDepth of ndc.
     * @see #getDepth
     * @since 0.7
     */
    public void setMaxDepth(int maxDepth) {
        Stack stack = (Stack) TL.get();

        if ((stack != null) && (maxDepth < stack.size())) {
            stack.setSize(maxDepth);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.7
     */
    private final class DiagnosticContext {
        String fullMessage;

        String message;

        /**
         * Constructor.
         *
         * @param message for this diagnostic context.
         * @param parent of this diagnostic context.
         */
        DiagnosticContext(String message, DiagnosticContext parent) {
            this.message = message;

            if (parent != null) {
                fullMessage = parent.fullMessage + ' ' + message;
            } else {
                fullMessage = message;
            }
        }
    }
}

// EOF NDC.java
