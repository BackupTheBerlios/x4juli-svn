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
package org.x4juli.global.spi;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.Vector;

import org.x4juli.global.helper.PlatformInfo;

/**
 * ThrowableInformation is Juli's internal representation of throwables. It
 * essentially consists of a string array, called 'rep', where the first
 * element, that is rep[0], represents the string representation of the
 * throwable (i.e. the value you get when you do throwable.toString()) and
 * subsequent elements correspond the stack trace with the top most entry of the
 * stack corresponding to the second entry of the 'rep' array that is rep[1].
 *
 * Note that ThrowableInformation does not store the throwable it represents.
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
 * <p>
 * This is just a copy of <code>org.apache.log4j.pattern.NameAbbreviator</code>.
 * </p>
 *
 * @author Boris Unckel
 * @since 0.5
 *
 *
 */
public class ThrowableInformation implements Serializable {
    private static final long serialVersionUID = -4748765566864322735L;

    // private transient Throwable throwable;
    private String[] rep;

    public ThrowableInformation(Throwable throwable) {
        VectorWriter vw = new VectorWriter();
        extractStringRep(throwable, vw);
        this.rep = vw.toStringArray();
    }

    public ThrowableInformation(String[] rep) {
        this.rep = (String[]) rep.clone();
    }

    // public Throwable getThrowable() {
    // return throwable;
    // }

    public void extractStringRep(Throwable t, VectorWriter vw) {
        t.printStackTrace(vw);

        // Check if the Throwable t has a nested Throwable. If so, invoke
        // extractStringRep recursively.
        // Note that the Throwable.getCause was added in JDK 1.4. The
        // printStackTrace
        // method was modified in JDK 1.4 to handle the nested throwable
        // returned
        // by Throwable.getCause.
        try {
            Class tC = t.getClass();
            Method[] mA = tC.getMethods();
            Method nextThrowableMethod = null;
            for (int i = 0; i < mA.length; i++) {
                if (("getCause".equals(mA[i].getName()) && !PlatformInfo.isJDK14OrLater())
                        || "getRootCause".equals(mA[i].getName())
                        || "getNextException".equals(mA[i].getName())
                        || "getException".equals(mA[i].getName())) {
                    // check param types
                    Class[] params = mA[i].getParameterTypes();
                    if ((params == null) || (params.length == 0)) {
                        // just found the getter for the nested throwable
                        nextThrowableMethod = mA[i];
                        break; // no need to search further
                    }
                }
            }

            if (nextThrowableMethod != null) {
                // get the nested throwable and log it
                Throwable nextT = (Throwable) nextThrowableMethod.invoke(t, new Object[0]);
                if (nextT != null) {
                    vw.print("Root cause follows.");
                    extractStringRep(nextT, vw);
                }
            }
        } catch (Exception e) {
            // do nothing
        }
    }

    /**
     * Retun a clone of the string representation of the exceptopn (throwable)
     * that this object represents.
     */
    public String[] getThrowableStrRep() {
        return (String[]) this.rep.clone();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof ThrowableInformation)) {
            return false;
        }

        ThrowableInformation r = (ThrowableInformation) o;

        if (this.rep == null) {
            if (r.rep != null) {
                return false;
            } else {
                return true;
            }
        }

        // at this point we know that both rep and r.rep are non-null.
        if (this.rep.length != r.rep.length) {
            return false;
        }

        int len = this.rep.length;
        for (int i = 0; i < len; i++) {
            if (!this.rep[i].equals(r.rep[i])) {
                return false;
            }
        }

        return true;
    }

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public int hashCode() {
        assert false : "hashCode not designed";
        final int answer_of_life = 42;
        return answer_of_life; // any arbitrary constant will do
    }
}

/**
 * VectorWriter is a seemingly trivial implemtantion of PrintWriter. The
 * throwable instance that we are trying to represnt is asked to print itself to
 * a VectorWriter.
 *
 * By our design choice, r string representation of the throwable does not
 * contain any line separators. It follows that println() methods of
 * VectorWriter ignore the 'ln' part.
 */
class VectorWriter extends PrintWriter {
    private Vector v;

    VectorWriter() {
        super(new NullWriter());
        this.v = new Vector();
    }

    public void print(Object o) {
        this.v.addElement(o.toString());
    }

    public void print(char[] chars) {
        this.v.addElement(new String(chars));
    }

    public void print(String s) {
        this.v.addElement(s);
    }

    public void println(Object o) {
        this.v.addElement(o.toString());
    }

    // JDK 1.1.x apprenly uses this form of println while in
    // printStackTrace()
    public void println(char[] chars) {
        this.v.addElement(new String(chars));
    }

    public void println(String s) {
        this.v.addElement(s);
    }

    public void write(char[] chars) {
        this.v.addElement(new String(chars));
    }

    public void write(char[] chars, int off, int len) {
        this.v.addElement(new String(chars, off, len));
    }

    public void write(String s, int off, int len) {
        this.v.addElement(s.substring(off, off + len));
    }

    public void write(String s) {
        this.v.addElement(s);
    }

    public String[] toStringArray() {
        int len = this.v.size();
        String[] sa = new String[len];

        for (int i = 0; i < len; i++) {
            sa[i] = (String) this.v.elementAt(i);
        }

        return sa;
    }
}

class NullWriter extends Writer {
    public void close() {
        // blank
    }

    public void flush() {
        // blank
    }

    public void write(char[] cbuf, int off, int len) {
        // blank
    }
}
