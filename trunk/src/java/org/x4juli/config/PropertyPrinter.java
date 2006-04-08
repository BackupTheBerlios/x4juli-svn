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
package org.x4juli.config;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.x4juli.global.spi.ExtendedHandler;

/**
 * Prints the configuration of the x4juli default hierarchy (which needs to be auto-initialized) as a
 * propoperties file on a {@link PrintWriter}.
 * 
 * <p>
 * Logging API as a whole was originally done for <a href="http://logging.apache.org/log4j/">Apache
 * log4j</a>. <b>x4uli</b> is a port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging APIs</a>. All
 * credits for initial idea, design, implementation, documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This file was originally
 * published by <i>Anders Kristensen</i>. Please use exclusively the <i>appropriate</i> mailing
 * lists for questions, remarks and contribution.
 * </p>
 * 
 * @author Boris Unckel
 * @since 0.7
 */
public class PropertyPrinter implements PropertyGetter.PropertyCallback {
    protected int numAppenders = 0;

    protected Hashtable appenderNames = new Hashtable();

    protected Hashtable layoutNames = new Hashtable();

    protected PrintWriter out;

    protected boolean doCapitalize;

    public PropertyPrinter(PrintWriter out) {
        this(out, false);
    }

    public PropertyPrinter(PrintWriter out, boolean doCapitalize) {
        this.out = out;
        this.doCapitalize = doCapitalize;

        // print(out);
        out.flush();
    }

    protected String genAppName() {
        return "A" + numAppenders++;
    }

    /**
     * Returns true if the specified appender name is considered to have been generated, i.e. if it
     * is of the form A[0-9]+.
     */
    protected boolean isGenAppName(String name) {
        if (name.length() < 2 || name.charAt(0) != 'A')
            return false;

        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) < '0' || name.charAt(i) > '9')
                return false;
        }
        return true;
    }

    /**
     * Prints the configuration of the default log4j hierarchy as a Java properties file on the
     * specified Writer.
     * 
     * <p>
     * N.B. print() can be invoked only once!
     */
    // public void print(PrintWriter out) {
    // printOptions(out, Category.getRoot());
    //
    // Enumeration cats = Category.getCurrentCategories();
    // while (cats.hasMoreElements()) {
    // printOptions(out, (Category) cats.nextElement());
    // }
    // }
    protected void printOptions(PrintWriter out, Logger cat) {
        Handler[] myHandlers = cat.getHandlers();
        List myList = Arrays.asList(myHandlers);
        Enumeration handlers = Collections.enumeration(myList);   
        
        Level prio = cat.getLevel();
        String appenderString = (prio == null ? "" : prio.toString());

        while (handlers.hasMoreElements()) {
            ExtendedHandler app = (ExtendedHandler) handlers.nextElement();
            String name;

            if ((name = (String) appenderNames.get(app)) == null) {

                // first assign name to the appender
                if ((name = app.getName()) == null || isGenAppName(name)) {
                    name = genAppName();
                }
                appenderNames.put(app, name);

                printOptions(out, app, "x4juli.handler." + name);
                if (app.getFormatter() != null) {
                    printOptions(out, app.getFormatter(), "x4juli.handler." + name + ".formatter");
                }
            }
            appenderString += ", " + name;
        }
        String catKey = (cat == Logger.getLogger("")) ? "x4juli.rootLogger" : "x4juli.logger."
                + cat.getName();
        if (appenderString != "") {
            out.println(catKey + "=" + appenderString);
        }
    }

    protected void printOptions(PrintWriter out, Object obj, String fullname) {
        out.println(fullname + "=" + obj.getClass().getName());
        PropertyGetter.getProperties(obj, this, fullname + ".");
    }

    public void foundProperty(Object obj, String prefix, String name, Object value) {
        // XXX: Properties encode value.toString()
        if (obj instanceof ExtendedHandler && "name".equals(name)) {
            return;
        }
        if (doCapitalize) {
            name = capitalize(name);
        }
        out.println(prefix + name + "=" + value.toString());
    }

    public static String capitalize(String name) {
        if (Character.isLowerCase(name.charAt(0))) {
            if (name.length() == 1 || Character.isLowerCase(name.charAt(1))) {
                StringBuffer newname = new StringBuffer(name);
                newname.setCharAt(0, Character.toUpperCase(name.charAt(0)));
                return newname.toString();
            }
        }
        return name;
    }

    // for testing
    public static void main(String[] args) {
        new PropertyPrinter(new PrintWriter(System.out));
    }

}

// EOF PropertyPrinter.java
