/*
 * Copyright 2006 x4juli.orgs.
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
 * Base class for all exceptions in x4juli.
 * @author Boris Unckel
 * @since 0.7
 */
public abstract class LogException extends RuntimeException {

    /**
     * Constructs a new LogException with the reason for it.
     * @param message reason why the exception is thrown.
     * @since 0.7
     */
    public LogException(String message) {
        super(message);
    }

    /**
     * Constructs a new LogException with the reason and
     * the exception causing this.
     * @param message reason why the exception is thrown.
     * @param cause exception catched and reason for throwing this exception.
     * @since 0.7
     */
    public LogException(String message, Throwable cause) {
        super(message, cause);
    }

}

// EOF LogException.java
