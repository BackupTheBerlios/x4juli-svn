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

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.logging.Logger;



/**
 * The basic interface of an LogManager. This is intended to be extended
 * and made use in all internal Juli classes. To be done.
 * It just has package visibilty to avoid outside world using it.
 * @author Boris Unckel
 * @since 0.5
 */
interface LogManager {

  /**
   * @see java.util.logging.LogManager#addPropertyChangeListener(java.beans.PropertyChangeListener)
   * @since 0.5
   */
  void addPropertyChangeListener(PropertyChangeListener l) throws SecurityException;

  /**
   * @see java.util.logging.LogManager#removePropertyChangeListener(java.beans.PropertyChangeListener)
   * @since 0.5
   */
  void removePropertyChangeListener(PropertyChangeListener l) throws SecurityException;

  /**
   * @see java.util.logging.LogManager#addLogger(java.util.logging.Logger)
   * @since 0.5
   */
  boolean addLogger(Logger logger);

  /**
   * @see java.util.logging.LogManager#getLogger(java.lang.String)
   * @since 0.5
   */
  Logger getLogger(String name);

  /**
   * @see java.util.logging.LogManager#getLoggerNames()
   * @since 0.5
   */
  Enumeration getLoggerNames();

  /**
   * @see java.util.logging.LogManager#readConfiguration()
   * @since 0.5
   */
  void readConfiguration() throws IOException, SecurityException;

  /**
   * @see java.util.logging.LogManager#reset()
   * @since 0.5
   */
  void reset() throws SecurityException;

  /**
   * @see java.util.logging.LogManager#readConfiguration(java.io.InputStream)
   * @since 0.5
   */
  void readConfiguration(InputStream ins) throws IOException, SecurityException;

  /**
   * @see java.util.logging.LogManager#getProperty(java.lang.String)
   * @since 0.5
   */
  String getProperty(String name);

  /**
   * @see java.util.logging.LogManager#checkAccess()
   * @since 0.5
   */
  void checkAccess() throws SecurityException;

}

//EOF LogManager.java
