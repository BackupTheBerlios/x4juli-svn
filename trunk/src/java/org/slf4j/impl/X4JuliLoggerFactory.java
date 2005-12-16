/* 
 * Copyright (c) 2005      x4juli.org
 * Copyright (c) 2004-2005 SLF4J.ORG
 * Copyright (c) 2004-2005 QOS.ch
 *
 * All rights reserved.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute, and/or sell copies of  the Software, and to permit persons
 * to whom  the Software is furnished  to do so, provided  that the above
 * copyright notice(s) and this permission notice appear in all copies of
 * the  Software and  that both  the above  copyright notice(s)  and this
 * permission notice appear in supporting documentation.
 * 
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR  A PARTICULAR PURPOSE AND NONINFRINGEMENT
 * OF  THIRD PARTY  RIGHTS. IN  NO EVENT  SHALL THE  COPYRIGHT  HOLDER OR
 * HOLDERS  INCLUDED IN  THIS  NOTICE BE  LIABLE  FOR ANY  CLAIM, OR  ANY
 * SPECIAL INDIRECT  OR CONSEQUENTIAL DAMAGES, OR  ANY DAMAGES WHATSOEVER
 * RESULTING FROM LOSS  OF USE, DATA OR PROFITS, WHETHER  IN AN ACTION OF
 * CONTRACT, NEGLIGENCE  OR OTHER TORTIOUS  ACTION, ARISING OUT OF  OR IN
 * CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 * 
 * Except as  contained in  this notice, the  name of a  copyright holder
 * shall not be used in advertising or otherwise to promote the sale, use
 * or other dealings in this Software without prior written authorization
 * of the copyright holder.
 *
 */

package org.slf4j.impl;

import org.slf4j.ILoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.LogManager;

/**
 * X4JuliLoggerFactory is an implementation of {@link ILoggerFactory}
 * returning the appropriately named {@link JDK14LoggerAdapter} instance.
 *
 * @author <a href="http://www.qos.ch/log4j/">Ceki G&uuml;lc&uuml;</a>
 */
public class X4JuliLoggerFactory implements ILoggerFactory {

    // -------------------------------------------------------------- Variables
    private static LogManager manager = LogManager.getLogManager();
    private static final boolean juliAvailable;
    static {
        boolean temp = false;
        try {
            java.util.logging.Logger toTest = java.util.logging.Logger.getLogger("org.slf4j.X4JuliLoggerFactory.test");
            if(toTest instanceof org.slf4j.Logger){
                temp = true;
            } else {
                temp = false;
            }
        } catch (Throwable t) {
            temp = false;
        }
        juliAvailable = temp;
    }

  // key: name (String), value: a JDK14LoggerAdapter;
  protected Map loggerMap;

  public X4JuliLoggerFactory() {
    loggerMap = new HashMap();
  }

  /* (non-Javadoc)
   * @see org.slf4j.ILoggerFactory#getLogger(java.lang.String)
   */
  public org.slf4j.Logger getLogger(final String name) {
      if(juliAvailable){
          return (org.slf4j.Logger) java.util.logging.Logger.getLogger(name);
      }
      org.slf4j.Logger ulogger = (org.slf4j.Logger) loggerMap.get(name);
    if (ulogger == null) {
      java.util.logging.Logger logger = java.util.logging.Logger.getLogger(name);
      ulogger = new JDK14LoggerAdapter(logger);
     loggerMap.put(name, ulogger);
    }
    return ulogger;
  }
}
