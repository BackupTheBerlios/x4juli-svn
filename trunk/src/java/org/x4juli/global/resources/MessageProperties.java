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
package org.x4juli.global.resources;

import org.x4juli.global.constants.ConstantString;

/**
 * Global constant store for Strings pointing to ResourceBundles.
 * @author Boris Unckel
 * @since 0.5
 */
public final class MessageProperties extends ConstantString {

	/**
	 *
	 */
	private static final long serialVersionUID = 3616732681732632886L;

	/**
	 * Creating new MessageProperties. Only internal instanciation wanted.
	 * @param value String pointing to ResourceBundle
	 */
	private MessageProperties(String value) {
		super(value);
	}

	public static final MessageProperties PROPERTIES_FORMATTER = new MessageProperties("org.x4juli.global.resources.Juli_Formatter");
	public static final MessageProperties PROPERTIES_FORMATTER_PATTERN = new MessageProperties("org.x4juli.global.resources.Juli_Formatter_Pattern");
	public static final MessageProperties PROPERTIES_HANDLER = new MessageProperties("org.x4juli.global.resources.Juli_Handler");
    public static final MessageProperties PROPERTIES_FILTER = new MessageProperties("org.x4juli.global.resources.Juli_Filter");
    public static final MessageProperties PROPERTIES_GLOBAL_SPI = new MessageProperties("org.x4juli.global.resources.Juli_Global_Spi");

}
