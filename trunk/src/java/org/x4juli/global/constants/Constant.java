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
package org.x4juli.global.constants;

import java.io.Serializable;

/**
 * Interface for constant objects.
 *
 * @author Boris Unckel
 * @since 0.5
 */
public interface Constant extends Serializable {

    /**
     * Error message when user passes a value that is already defined for
     * <tt>this</tt> class.
     */
    final String ERR_DUP_NAME = "ERROR: Constants of"
                           + "the same type have duplicate names."; //$NON-NLS-1$

    /**
     * Object way to access the value.
     * @return the value of the constant as java.lang.Object
     */
    Object getValue();

    /**
     * Determine the type.
     * @return the type of the constant as Class
     */
    Class getValueType();

    /**
     * Return the plain value as String.
     * <code>getValueAsString</code> differs from toString():
     * <br/><code>getValueAsString</code>
     * is just the plain value without technical content.
     *
     * @return the value of the constant as java.lang.String
     */
    String getValueAsString();

}
