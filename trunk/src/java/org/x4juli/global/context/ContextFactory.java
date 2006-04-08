/*
 * Copyright 2006, x4juli.org.
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

import java.util.logging.LogManager;

import org.x4juli.global.spi.ContextFactoryIf;
import org.x4juli.global.spi.MDC;
import org.x4juli.global.spi.NDC;
import org.x4juli.global.spi.TCCLMapper;

/**
 * Provides access to the factory for context related classes (NDC, MDC). The factory implementation
 * can be configured by the property <code>org.x4juli.global.context.ContextFactoryClass</code>.
 * This class has to implement <code>org.x4juli.global.context.ContextFactoryIf</code>.
 * 
 * @author Boris Unckel
 * @since 0.7
 */
public final class ContextFactory {

    private static final ContextFactoryIf INSTANCE;

    private static final String CONTEXT_FACTORY_CLASS_KEY = "org.x4juli.global.context.ContextFactoryClass";

    static {
        String prop = System.getProperty(CONTEXT_FACTORY_CLASS_KEY);
        synchronized (ContextFactory.class) {
            ContextFactoryIf tempCF = null;
            if (prop == null) {
                tempCF = new ContextFactoryImpl();
            } else {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                if (classLoader == null) {
                    classLoader = ClassLoader.getSystemClassLoader();
                }
                try {
                    Class cfclass = classLoader.loadClass(prop.trim());
                    tempCF = (ContextFactoryIf) cfclass.newInstance();
                } catch (Exception e) {
                    System.err.println("Could not load or instantiate class[" + prop + "]" + e);
                    System.err.println("Fallback to default class.");
                    e.printStackTrace();
                    tempCF = new ContextFactoryImpl();
                }
            }
            INSTANCE = tempCF;
        }
    }

    /**
     * No instantitation wanted.
     */
    private ContextFactory() {
    }

    /**
     * The ContextFactoryIf for context access objects.
     * 
     * @return the ContextFactoryIf singleton.
     */
    public static ContextFactoryIf getContextFactory() {
        return INSTANCE;
    }

    /**
     * Returns the instance to manage the nested diagnostic context.
     * @return the nested diagnostic context object.
     * @since 0.7
     */
    public static NDC getNestedDiagnosticContext() {
        return getContextFactory().getNestedDiagnosticContext();
    }

    /**
     * Returns the instance to manage the mapped diagnostic context.
     * @return the mapped diagnostic context object.
     * @since 0.7
     */
    public static MDC getMappedDiagnosticContext() {
        return getContextFactory().getMappedDiagnosticContext();
    }
    
    /**
     * Returns the instance to manage the ThreadContextClassLoader mapping.
     * @return the TCCLMapper context object.
     */
    public static TCCLMapper getThreadContextClassLoaderMapper() {
        return getContextFactory().getTCCLMapper();
    }

}

// EOF ContextFactory.java
