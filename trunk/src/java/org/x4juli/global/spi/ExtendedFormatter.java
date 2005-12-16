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
package org.x4juli.global.spi;


/**
 * This interface specifies the operations for Juli formatters.
 * @author Boris Unckel
 * @since 0.5
 */
public interface ExtendedFormatter extends Formatter {

	/**
	 * Formats a given ExtendedLogRecord.
	 * @param record containing the information to generate and beautify Output
	 * @return the formatted String
	 * @since 0.5
	 */
	String format(ExtendedLogRecord record);

    /**
     * @return whether throwables are ignored or not
     * @since 0.5
     */
    boolean ignoresThrowable();


}
