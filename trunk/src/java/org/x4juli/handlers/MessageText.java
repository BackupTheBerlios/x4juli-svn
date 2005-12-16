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
package org.x4juli.handlers;

/**
 * Defines the constants for i18n juli internal logging.
 * There is one MessageText interface per package.
 * @author Boris Unckel
 * @since 0.5
 */
public interface MessageText {

	public static final String Exception_is = "JH_RUNTIME_1";
	public static final String Unsupported_encoding = "JH_RUNTIME_3";
	public static final String Error_initializing_output_writer = "JH_RUNTIME_4";
	public static final String Failed_to_write_footer_for_Handler = "JH_RUNTIME_5";
	public static final String Failed_to_write_header_for_Handler = "JH_RUNTIME_6";
	public static final String Flush_operation_failed_in_handler = "JH_RUNTIME_7";
	public static final String Not_allowed_to_write_to_a_closed_handler = "JH_RUNTIME_8";
    public static final String Not_allowed_to_write_to_an_inactive_handler = "JH_RUNTIME_9";
    public static final String IO_failure_for_handler_named = "JH_RUNTIME_12";
    public static final String Set_file_call_failed="JH_RUNTIME_13";
    public static final String Exception_in_post_close_rollover_action = "JH_RUNTIME_14";
    public static final String Exception_during_rollover_attempt = "JH_RUNTIME_15";
    public static final String Exception_during_rollover_rollover_deferred = "JH_RUNTIME_16";
    public static final String Exception_during_compression_of ="JH_RUNTIME_17";
    public static final String Unable_to_delete = "JH_RUNTIME_18";
    public static final String Exception_during_file_rollover = "JH_RUNTIME_19";
    public static final String Exception_during_purge = "JH_RUNTIME_20";
	public static final String No_output_stream_or_file_set_for_the_handler = "JH_CONFIG_1";
    public static final String No_output_stream_or_file_set_for_the_handler_using_default = "JH_CONFIG_2";
	public static final String No_formatter_set_for_the_appender = "JH_CONFIG_3";
	public static final String No_writer_set_for_the_appender = "JH_CONFIG_4";
	public static final String Please_set_a_TriggeringPolicy_for_the_RollingFileHandler ="JH_CONFIG_5";
	public static final String Please_set_a_rolling_policy = "JH_CONFIG_6";
    public static final String The_ActiveFile_name_option_must_be_set_before_using_this_rolling_policy = "JH_CONFIG_7";
    public static final String MaxIndex_cannot_be_smaller_than_MinIndex = "JH_CONFIG_8";
    public static final String Large_window_sizes_are_not_allowed = "JH_CONFIG_9";
    public static final String Property_has_not_an_int_value = "JH_CONFIG_10";
    public static final String File_option_not_set_for_handler = "JH_CONFIG_11";
    public static final String Value_should_be_Systemout_or_System_err = "JH_CONFIG_12";
    public static final String Using_previously_set_target_Systemout_by_default = "JH_CONFIG_13";
    public static final String No_fileNamePattern_set_for_rolling = "JH_CONFIG_14";
    public static final String Finalizing_handler_named = "JH_INFO_1";
    public static final String Setting_maxIndex_equal_to_minIndex = "JH_INFO_2";
    public static final String MaxIndex_reduced_to = "JH_INFO_3";
    public static final String SetFile_called = "JH_INFO_4";
    public static final String SetFile_ended = "JH_INFO_5";
    

}
