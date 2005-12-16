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

package org.x4juli.sample.standalone.jcl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class demonstrates use of Jakarta Commons Logging.
 * 
 * @author Boris Unckel
 * @since 0.6
 */
public class JclDemo {
    private static final Log LOG = LogFactory.getLog(JclDemo.class);

    public void doDemoLog(String parameter) {
        System.out.println("JclSample: Using class " + LOG.getClass().getName());
        //Additional check of enabled log level, to avoid unnessesary string concatination.
        if (LOG.isTraceEnabled()) {
            LOG.trace("Entered method doDemoLog parameter[" + parameter + "]");
        }
        LOG.trace("JclDemo: Using method LOG.trace(Object arg)");
        LOG.debug("JclDemo: Using method LOG.debug(Object arg)");
        LOG.info("JclDemo: Using method LOG.info(Object arg)");
        LOG.error("JclDemo: Using method LOG.error(Object arg)");
        LOG.fatal("JclDemo: Using method LOG.fatal(Object arg)");
        LOG.fatal("JclDemo: Using method LOG.fatal(Object arg, Throwable t)", new Exception(
                "JclDemo Exception, don't care about it"));

    }

}

// EOF JclDemo.java
