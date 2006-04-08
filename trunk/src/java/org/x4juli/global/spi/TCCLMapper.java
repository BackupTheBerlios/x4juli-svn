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
 * This interface specifies operations to map the hashcode of
 * an classloader with an identifier and retrieve the identifier
 * afterwards. 
 * @author Boris Unckel
 * @since 0.7
 */
public interface TCCLMapper {

    /**
     * @param hashcode of the classloader.
     * @param identifier coupled with the hashcode.
     */
    void addClassLoaderId(int hashcode, String identifier);

    /**
     * Retrieves the identifier for a given classloader's hashcode.
     * @param hashcode of the classloader.
     * @return the coupled identifier, if not found null.
     */
    String getIdentifier(int hashcode);

}

// EOF TCCLMapper.java
