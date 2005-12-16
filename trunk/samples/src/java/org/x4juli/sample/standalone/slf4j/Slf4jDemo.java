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
package org.x4juli.sample.standalone.slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Missing documentation.
 * @todo Missing documentation.
 * @author Boris Unckel
 * @since 0.6
 */
public class Slf4jDemo {
    
    private static final Logger LOG = LoggerFactory.getLogger(Slf4jDemo.class);
    
    public void doDemoLog(final String parameter) {
        System.out.println("Slf4jDemo: Using class "+LOG.getClass().getName());
        //No additional check of enabled log level, unnessesary string concatination is avoided in log method itself.
        LOG.debug("Entered method doDemoLog param[{}]", parameter);
        
        LOG.debug("Slf4jDemo: Using method LOG.debug(String msg)");
        LOG.info("Slf4jDemo: Using method LOG.info(String msg)");
        LOG.warn("Slf4jDemo: Using method LOG.warn(String msg)");
        LOG.error("Slf4jDemo: Using method LOG.error(String msg)");

        LOG.info("Slf4jDemo: Using method LOG.info(String msg, Object arg1) Object0[{}]",
                new Integer(4711));
        LOG.warn("Slf4jDemo: Using method LOG.warn(String msg, Object arg1, Object arg2) Object0[{}] Object1[{}]",
                    new Integer(4711), new Double("815.5"));
        LOG.info("Slf4jDemo: Using method LOG.info(String msg, Throwable t",new Exception("Slf4j Demo Exception, don't care about it"));
    }
}

// EOF Slf4jDemo.java
