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
package org.x4juli.formatter.pattern;

/**
 * Defines the constants for i18n juli internal logging.
 * There is one MessageText interface per package.
 * @author Boris Unckel
 * @since 0.
 */
public interface MessageText {

	public static final String Error_creating_converter = "JFP_RUNTIME_1";
	public static final String Exception_is = "JFP_RUNTIME_2";
	public static final String Empty_conversion_specifier = "JFP_RUNTIME_3";
    public static final String Class_for_conversion_pattern_not_found = "JFP_RUNTIME_4";
    public static final String Bad_map_entry_for_conversion_pattern = "JFP_RUNTIME_5";
    public static final String Class_does_not_extend_PatternConverter = "JFP_RUNTIME_6";
    public static final String Could_not_instantiate_SimpleDateFormat_with_pattern = "JFP_RUNTIME_7";
    public static final String Unrecognized_conversion_specifier = "JFP_CONFIG_1";
    public static final String Error_occured_in_position_Expecting_digit_got_char = "JFP_CONFIG_2";

}
