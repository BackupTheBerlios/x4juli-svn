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
package org.x4juli.global.context;

import java.util.HashMap;

import org.x4juli.global.spi.TCCLMapper;

/**
 * Missing documentation.
 * @todo Missing documentation.
 * @author Boris Unckel
 * @since 0.7
 */
class TCCLMapperImpl implements TCCLMapper {

    private final HashMap mapper = new HashMap();
    
    /**
     * Default constructor.
     */
    public TCCLMapperImpl() {
        super();
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void addClassLoaderId(final int hashcode, final String identifier) {
        Integer key = new Integer(hashcode);
        mapper.put(key, identifier);
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public String getIdentifier(int hashcode) {
        Integer key = new Integer(hashcode);
        return (String) mapper.get(key);
    }

}

// EOF TCCLMapperImpl.java
