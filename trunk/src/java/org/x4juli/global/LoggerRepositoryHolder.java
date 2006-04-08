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
package org.x4juli.global;

import org.x4juli.global.spi.LoggerRepository;


/**
 * Juli is just working with a ObjectStore.
 * To allow independent extensions of an LogManager,
 * the new LogManager must implement this interface.
 *
 * @author Boris Unckel
 *
 */
public interface LoggerRepositoryHolder {

	/**
     * Get the actual repository for objects in the system.
	 * @return the actual ObjectStore.
	 */
	LoggerRepository getLoggerRepository();
}
