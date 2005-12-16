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

import java.util.logging.Filter;

/**
 * Interface how filtering in x4juli works.
 * @author Boris Unckel
 * @since 0.5
 */
public interface ExtendedFilter extends Filter, OptionHandler {

    /**
   * The log event must be dropped immediately without consulting with the
   * remaining filters, if any, in the chain.
   */
  static final boolean DENY = false;
  /**
   * The log event must be logged immediately without consulting with the
   * remaining filters, if any, in the chain.
   */
  static final boolean ACCEPT = true;
  /**
   * The log event must be dropped immediately without consulting with the
   * remaining filters, if any, in the chain. </br>Concept of "int-decisions"
   * done for enhanced possibilities.
   */
  static final int X4JULI_DENY = -1;
  /**
   * This filter is neutral with respect to the log event. The remaining
   * filters, if any, should be consulted for a final decision.</br>Concept
   * of "int-decisions" done for enhanced possibilities.
   */
  static final int X4JULI_NEUTRAL = 0;
  /**
   * The log event must be logged immediately without consulting with the
   * remaining filters, if any, in the chain.</br>Concept of "int-decisions"
   * done for enhanced possibilities.
   */
  static final int X4JULI_ACCEPT = 1;

  /**
     * <p>
     * If the decision is <code>DENY</code>, then the event will be dropped.
     * If the decision is <code>NEUTRAL</code>, then the next filter, if any,
     * will be invoked. If the decision is ACCEPT then the event will be logged
     * without consulting with other filters in the chain.
     *
     * @param record
     *            The LoggingEvent to decide upon.
     * @since 0.5
     */
    int decide(final ExtendedLogRecord record);

    /**
     * Set the next filter pointer.
     *
     * @since 0.5
     */
    void setNext(ExtendedFilter next);

    /**
     * Return the pointer to the next filter;
     *
     * @since 0.5
     */
    ExtendedFilter getNext();


}
