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

import java.util.logging.Handler;
import java.util.logging.LogRecord;



/**
 * This Interface specifies the operations of <code>java.util.logging.Formatter</code>
 * and allow Juli to use this without extending the original <code>java.util.logging.Formatter</code>.
 * For proper functionality Juli uses the extended version of this. It just has package
 * visibilty to avoid outside world using it.
 * @see org.x4juli.global.spi.ExtendedFormatter
 * @author Boris Unckel
 * @since 0.5
 */
interface Formatter {

	/**
	 * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
	 * @since 0.5
	 */
	String format(LogRecord record);

	/**
	 * @see java.util.logging.Formatter#getHead(java.util.logging.Handler)
	 * @since 0.5
	 */
	String getHead(Handler h);

	/**
	 * @see java.util.logging.Formatter#getTail(java.util.logging.Handler)
	 * @since 0.5
	 */
	String getTail(Handler h);

	/**
	 * @see java.util.logging.Formatter#formatMessage(java.util.logging.LogRecord)
	 * @since 0.5
	 */
	String formatMessage(LogRecord record);

}

//EOF Formatter.java
