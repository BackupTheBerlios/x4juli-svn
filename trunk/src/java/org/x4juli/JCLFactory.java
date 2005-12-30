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
package org.x4juli;

import java.util.Hashtable;
import java.util.Map;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.Jdk14Logger;

/**
 * A implementation of <code>org.apache.commons.logging.LogFactory</code>.
 * This class provides a x4juli native access version for the 
 * <a href="http://jakarta.apache.org/commons/logging/">Apache Jakarta Commons Logging API</a>.
 * <br/>
 * To configure you have to:
 * <br/>
 * 1) Setup x4juli (Details: {@link org.x4juli.X4JuliLogManager}).
 * <br/>
 * 2) Put commons-logging.jar on appropriate place on the classpath
 * <br/>
 * 3a) Use a system property -Dorg.apache.commons.logging.LogFactory=org.x4juli.JCLFactory
 * <br/>
 * or
 * <br/>
 * 3b) Use a config file commons-logging.properties on appropriate place on the classpath which
 * includes  org.apache.commons.logging.LogFactory=org.x4juli.JCLFactory
 * 
 * @author Boris Unckel
 * @since 0.6
 */
public class JCLFactory extends LogFactory {

    // -------------------------------------------------------------- Variables
    static {
        try {
            LogManager manager = LogManager.getLogManager();
            manager.getClass();
        } catch (Throwable t) {
            System.err.println("JCLFactory: Error getting LogManager.");
            System.err.println("JCLFactory: You have got system parameters or classpath or logging config problems.");
            t.printStackTrace();
            throw new LogConfigurationException("java.util.logging or x4juli setup is wrong. Check system parameters, classpath and logging.properties",t);
        }
    }

    private static final boolean juliAvailable;
    static {
        boolean temp = false;
        try {
            Logger toTest = Logger.getLogger("org.x4juli.JCLFactory.test");
            if(toTest instanceof Log){
                temp = true;
            } else {
                temp = false;
            }
        } catch (Throwable t) {
            temp = false;
        }
        juliAvailable = temp;
    }
    
    /**
     * The {@link org.apache.commons.logging.Log} instances that have
     * already been created, keyed by logger name.
     */
    protected Map instances = new Hashtable();

    // ----------------------------------------------------------- Constructors
    
    /**
     * Create a new Factory. 
     */
    public JCLFactory() {
        super();
    }

    // --------------------------------------------------------- Public Methods

    /**
     * {@inheritDoc}
     * @since 0.6
     */
    public Object getAttribute(final String name) {
        return null;
    }

    /**
     * {@inheritDoc}
     * @since
     */
    public String[] getAttributeNames() {
        return new String[]{};
    }

    /**
     * {@inheritDoc}
     * @since
     */
    public Log getInstance(final Class clazz) throws LogConfigurationException {
        return getInstance(clazz.getName());
    }

    /**
     * {@inheritDoc}
     * @since
     */
    public Log getInstance(String name) throws LogConfigurationException {
        if(juliAvailable){
            return (Log) Logger.getLogger(name);
        }
        Log instance = (Log) this.instances.get(name);
        if (instance == null) {
            instance = new Jdk14Logger(name);
            this.instances.put(name, instance);
        }
        return (instance);
    }

    /**
     * {@inheritDoc}
     * @since
     */
    public void release() {
        this.instances = new Hashtable();
    }

    /**
     * {@inheritDoc}
     * @since
     */
    public void removeAttribute(String name) {
        //NOP
    }

    /**
     * {@inheritDoc}
     * @since
     */
    public void setAttribute(String name, Object value) {
        //NOP
    }

}

// EOF JCLFactory.java
