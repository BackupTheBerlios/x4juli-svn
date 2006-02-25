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

/**
 * The default implementation for the ContextFactoryIf.
 * @author Boris Unckel
 * @since 0.7
 */
final class ContextFactoryImpl implements ContextFactoryIf {

    private final MDC mdc;
    
    private final NDC ndc;
    
    /**
     * 
     */
    public ContextFactoryImpl() {
        super();
        this.mdc = (MDC) new MDCImpl();
        this.ndc = (NDC) new NDCImpl();
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public MDC getMappedDiagnosticContext() {
        return mdc;
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public NDC getNestedDiagnosticContext() {
        return ndc;
    }

}

// EOF ContextFactoryImpl.java
