/*
 * Copyright 1999,2005 The Apache Software Foundation.
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
 * Exception if something ugly happens during configuration or a
 * wrong configuration input.
 * @author Boris Unckel
 * @since 0.7
 */
public class LogConfigurationException extends LogException {

    /**
     * 
     */
    private static final long serialVersionUID = -109319771715844945L;

    /**
     * Constructs a new LogException with the reason for it.
     * @param message reason why the exception is thrown.
     * @since 0.7
     */
    public LogConfigurationException(String message) {
        super(message);
    }

    /**
     * Constructs a new LogException with the reason and
     * the exception causing this.
     * @param message reason why the exception is thrown.
     * @param cause exception catched and reason for throwing this exception.
     * @since 0.7
     */
    public LogConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

}

// EOF LogConfigurationException.java
