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
package org.x4juli.global.spi;

import org.x4juli.global.resources.MessageProperties;

/**
 * A common interface shared by juli components.
 * <p>
 * Logging API as a whole was originally done for <a
 * href="http://logging.apache.org/log4j/">Apache log4j</a>. <b>Juli</b> is a
 * port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging
 * APIs</a>. All credits for initial idea, design, implementation,
 * documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This
 * file was originally published by <i>Ceki G&uuml;lc&uuml;</i>. Please use
 * exclusively the <i>appropriate</i> mailing lists for questions, remarks and
 * contribution.
 * </p>
 *
 * @author Boris Unckel
 * @since 0.5
 */

public interface Component {

    /**
     * Set owning object repository for this component. This operation can only
     * be performed once. Once set, the owning repository cannot be changed.
     *
     * @param repository
     *            The repository where the configuration is stored
     * @throws IllegalStateException
     *             If you try to change the repository after it has been set.
     */
    public void setObjectStore(ObjectStore repository);

	/**
	 * Specifiy Properties for the component. Default Implementation returns null.
	 * @return the key for the Properties for the component
	 * @since 0.5
	 */
	public MessageProperties getMessageProperties();



}
