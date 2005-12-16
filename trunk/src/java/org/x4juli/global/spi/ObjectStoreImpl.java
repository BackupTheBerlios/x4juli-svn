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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * The object store is needed because there is nothing like Hierarchy (the Log4j
 * ObjectStore Implementation) in java.util.logging.
 * <p>
 * The <code>java.util.logging.LogManager</code> does not contain a global
 * repository for objects.
 * </p>
 * <p>
 * To get a global ObjectStoreImpl
 * <code>org.x4juli.ClassLoaderLogManager</code> implements
 * <code>org.x4juli.global.LoggerRepositoryHolder</code>. If you want to
 * implement your own LogManager this Interface is needed.
 * </p>
 *
 * @author Boris Unckel
 * @since 0.5
 */
public final class ObjectStoreImpl implements ObjectStore {

    // -------------------------------------------------------------- Variables


	/**
	 * Is syncronized access really needed? To be checked.
	 * First idea was the all syncronized java.util.Logger methods.
	 */
	private static Map store = Collections.synchronizedMap(new HashMap());

    // ----------------------------------------------------------- Constructors

    public ObjectStoreImpl() {
        //NOP
    }

    // --------------------------------------------------------- Public Methods

    /**
     * @see org.x4juli.global.spi.ObjectStore#getObject(java.lang.String)
     * @since 0.5
     */
    public Object getObject(String key) {
        return store.get(key);
    }

    /**
     * @see org.x4juli.global.spi.ObjectStore#putObject(java.lang.String,
     *      java.lang.Object)
     * @since 0.5
     */
    public void putObject(String key, Object value) {
        store.put(key, value);

    }

}
