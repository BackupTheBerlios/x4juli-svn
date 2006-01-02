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

/**
 * Provides access to the factory for context related classes (NDC, MDC). The factory implementation
 * can be configured by the property <code>org.x4juli.global.context.ContextFactoryClass</code>.
 * This class has to implement <code>org.x4juli.global.context.ContextFactory</code>.
 * 
 * @author Boris Unckel
 * @since 0.7
 */
public final class ContextFactoryProvider {

    private static final ContextFactory INSTANCE;

    private static final String CONTEXT_FACTORY_CLASS_KEY = "org.x4juli.global.context.ContextFactoryClass";

    private static final LogManager manager = LogManager.getLogManager();

    static {
        String prop = manager.getProperty(CONTEXT_FACTORY_CLASS_KEY);
        synchronized (ContextFactoryProvider.class) {
            ContextFactory tempCF = null;
            if (prop == null) {
                tempCF = new ContextFactoryImpl();
            } else {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                if (classLoader == null) {
                    classLoader = ClassLoader.getSystemClassLoader();
                }
                try {
                    tempCF = (ContextFactory) classLoader.loadClass(prop.trim()).newInstance();
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
    private ContextFactoryProvider() {
    }

    /**
     * The ContextFactory for context access objects.
     * 
     * @return the ContextFactory singleton.
     */
    public static ContextFactory getContextFactory() {
        return INSTANCE;
    }

}

// EOF ContextFactoryProvider.java
