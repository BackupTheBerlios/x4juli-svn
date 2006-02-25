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
package org.x4juli.sample.standalone;

import java.util.logging.Logger;

import org.x4juli.sample.standalone.jcl.JclDemo;
import org.x4juli.sample.standalone.jul.JulDemo;
import org.x4juli.sample.standalone.jul.sub.AnotherJulDemo;
import org.x4juli.sample.standalone.slf4j.Slf4jDemo;

/**
 * Main class for demo code.
 * @author Boris Unckel
 * @since 0.7
 */
public class X4JuliDemo {
    
    private static final Logger LOG = Logger.getLogger(X4JuliDemo.class.getName());
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("X4JuliDemo: Starting with sample, System.out");
        System.err.println("X4JuliDemo: Starting with sample, System.err");
        
        JulDemo juld = new JulDemo();
        juld.doDemoLog("demoParameterJul");
        
        AnotherJulDemo ajuld = new AnotherJulDemo();
        ajuld.doDemoLog("anotherParam");
        
        JclDemo jcld = new JclDemo();
        jcld.doDemoLog("theJclParam");
        
        Slf4jDemo slf4jDemo = new Slf4jDemo();
        slf4jDemo.doDemoLog("paramForSl4j");
        
        Logger.global.fine("X4JuliDemo: Using method Logger.global.info(String msg)");
        Logger anonymous = Logger.getAnonymousLogger();
        anonymous.info("X4JuliDemo: Using method anonymous.info(String msg)");
        
        System.out.println("X4JuliDemo: End of sample, System.out");
        System.err.println("X4JuliDemo: End of sample, System.err");
    }

}

// EOF JulDemo.java
