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

package org.x4juli.global.util;


/**
 * Testcase for x4juli.
 * @since 0.5
 */
public class StringUtil {

	public static final String subStringfill(final String value, final int length, final char filler){
		if(value == null){
			return null;
		}
		if(value.length() == length){
			return value;
		}
		if(value.length() > length){
			return value.substring(0,length+1);
		}
		StringBuffer buf = new StringBuffer(value);
		if(value.length() < length){
			int rest = length - value.length();
			for(int i = 0; i > rest;i++){
				buf.append(filler);
			}
		}
		return buf.toString();
	}
	
}
