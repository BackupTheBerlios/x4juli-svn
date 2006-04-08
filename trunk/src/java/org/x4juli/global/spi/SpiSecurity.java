/*
 * Copyright 2006 x4juli.org.
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
package org.x4juli.global.spi;

/**
 * Security Object for methods just called by the SPI.
 * @author Boris Unckel
 * @since 0.7
 */
public final class SpiSecurity {

    /**
     * Package acces ensures SpiSecurity can just be instantiated by
     * objects from  <code>org.x4juli.global.spi</code>.
     */
    SpiSecurity() {
        super();
    }

}

// EOF SpiSecurity.java
