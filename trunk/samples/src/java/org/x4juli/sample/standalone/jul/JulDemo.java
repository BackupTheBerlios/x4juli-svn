/*
 * Copyright 2005, x4juli.org.
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
package org.x4juli.sample.standalone.jul;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class demonstrates the use of java.util.logging.Logger
 * @author Boris Unckel
 * @since 0.6
 */
public class JulDemo {
    
    private static final Logger LOG = Logger.getLogger(JulDemo.class.getName());
    
    public void doDemoLog(String parameter) {
        // No additional check of enabled log level, unnessesary string concatination is avoided in log method itself.
        LOG.entering(JulDemo.class.getName(),"doDemoLog",parameter);
        
        LOG.log(Level.FINEST, "JulDemo: Using method LOG.log(Level.FINEST, String msg)");
        LOG.log(Level.INFO, "JulDemo: Using method LOG.log(Level.INFO, String msg)");
        LOG.log(Level.SEVERE, "JulDemo: Using method LOG.log(Level.SEVERE, String msg)");
        LOG.log(Level.ALL, "JulDemo: Using method LOG.log(Level.ALL, String msg)");

        LOG.config("JulDemo: Using method LOG.config(String msg)");
        LOG.finer("JulDemo: Using method LOG.finer(String msg)");
        LOG.warning("JulDemo: Using method LOG.warning(String msg)");
        
        LOG.log(Level.SEVERE, "JulDemo: Using method LOG.log(Level.SEVERE, String msg)", 
                 new Exception("JulDemo Exception, don't care about it"));
        
        LOG.log(Level.INFO, "JulDemo: Using method LOG.log(Level.INFO, String msg, Object arg) Integer[{0}]",
                new Integer(4711));
        LOG.log(Level.WARNING, "JulDemo: Using method LOG.log(Level.WARNING, String msg, Object[] params) Integer[{0}]Double[{1}]",
                new Object[]{new Integer(4711), new Double("815.5")});

        LOG.exiting(JulDemo.class.getName(),"doDemoLog");

    }

}

// EOF JulDemo.java
