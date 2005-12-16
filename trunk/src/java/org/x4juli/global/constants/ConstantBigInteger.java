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

import java.io.ObjectStreamException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Constant super class for BigInteger values.
 *
 * @author Boris Unckel
 * @since 0.5
 */
public class ConstantBigInteger implements Constant {

    // -------------------------------------------------------------- Variables
    private static final long serialVersionUID = -696105337069864714L;

    /**
     * Holds the grand constants index map. Contains references to all constants
     * indexes that are instantiated indexed by constant value.
     */
    private static final Map CONSTANTS_MASTER_INDEX = new HashMap();

    /** Holds value of property value. */
    private final BigInteger value;

    // ----------------------------------------------------------- Constructors

    /**
     * Creates a new instance of ConstantObject.
     *
     * @param value The value for the constant object.
     *
     * @throws NullPointerException If null is given for the constant value.
     * @throws IllegalArgumentException If the user passes a null value.
     */
    protected ConstantBigInteger(final BigInteger value) {
        if (value == null) {
            throw new NullPointerException("The value may not be null."); //$NON-NLS-1$
        }
        Map constMap = (Map) CONSTANTS_MASTER_INDEX.get(getClass());
        if (constMap == null) {
            constMap = new HashMap();
            CONSTANTS_MASTER_INDEX.put(getClass(), constMap);
        }
        if (constMap.containsKey(value)) {
            throw new IllegalArgumentException(ERR_DUP_NAME);
        }
        this.value = value;
        constMap.put(value, this);
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Get the constant for the given class and id.
     *
     * @param dataType The class for which to retrieve the index.
     * @param value The value of the constant.
     * @return The constant object for the given class and value.
     * @since 0.5
     */
    public static final ConstantBigInteger lookup(final Class dataType, final BigInteger value) {
        return (ConstantBigInteger) ((Map) CONSTANTS_MASTER_INDEX.get(dataType)).get(value);
    }

    /**
     * Get the constants index for the given class.
     *
     * @param dataType The class for which to retrieve the index.
     * @return An unmodifiable map of constants for the given class.
     * @since 0.5
     */
    public static final Map lookup(final Class dataType) {
        return Collections.unmodifiableMap((Map) CONSTANTS_MASTER_INDEX.get(dataType));
    }

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public Object getValue() {
        return this.value;
    }

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public Class getValueType() {
        return BigInteger.class;
    }

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public String getValueAsString() {
        return this.value.toString();
    }

    /**
     * Getter for property value.
     * @return Value of property value.
     * @since 0.5
     */
    public final BigInteger getValueAsObject() {
        return this.value;
    }

    /**
     * {@inheritDoc}
     * @since 0.5
     */
    public String toString() {
        return this.getClass().getName() + "." + this.value;
    }

    /**
     * Resolve a read in object.
     *
     * @return The resolved object read in.
     * @throws ObjectStreamException if there is a problem reading the object.
     * @throws RuntimeException If the read object doesnt exist.
     * @since 0.5
     */
    protected Object readResolve() throws ObjectStreamException {
        Object result = lookup(getClass(), getValueAsObject());
        if (result == null) {
            throw new RuntimeException("Constant not found for value: "
                    + getValueAsString()); //$NON-NLS-1$
        }
        return result;
    }

}
