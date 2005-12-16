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
package org.x4juli.global.helper;

import java.util.Properties;

/**
 * This class provides information about the runtime platform.
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
 *
 * @author Boris Unckel
 * @since 0.5
 */
public final class PlatformInfo {

	// -------------------------------------------------------------- Variables

    private static final int UNINITIALIZED = -1;

    // Check if we are running in IBM's visual age.
    private static int inVisualAge = UNINITIALIZED;

    private static int onAS400 = UNINITIALIZED;

	// ------------------------------------------------------------ Constructor

	/**
	 * No instanciation wanted.
	 */
	private PlatformInfo(){
        //NOP
	}

	// --------------------------------------------------------- Public Methods
    public static boolean isInVisualAge() {
        if (inVisualAge == UNINITIALIZED) {
            try {
                Class dummy = Class.forName("com.ibm.uvm.tools.DebugSupport");
                inVisualAge = 1;
                dummy.getClass();
            } catch (Throwable e) {
                inVisualAge = 0;
            }
        }
        return (inVisualAge == 1);
    }

    /**
     * Are we running on AS400?
     */
    public static boolean isOnAS400() {
        if (onAS400 == UNINITIALIZED) {
            try {
                Properties p = System.getProperties();
                String osname = p.getProperty("os.name");
                if ((osname != null) && (osname.equals("OS/400"))) {
                    onAS400 = 1;
                } else {
                    onAS400 = 0;
                }
            } catch (Throwable e) {
                // This should not happen, but if it does, assume we are not on
                // AS400.
                onAS400 = 0;
            }
        }
        return (onAS400 == 1);
    }

    /**
     * Juli just runs with java.util.logging, established in JDK 1.4
     * @return always true
     */
    public static boolean isJDK14OrLater() {
        return true;
    }
}
