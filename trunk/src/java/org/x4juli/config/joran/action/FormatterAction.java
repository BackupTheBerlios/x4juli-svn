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
package org.x4juli.config.joran.action;

import java.util.logging.Level;

import org.x4juli.config.joran.spi.ActionException;
import org.x4juli.config.joran.spi.ExecutionContext;
import org.x4juli.global.helper.OptionConverter;
import org.x4juli.global.spi.ErrorItem;
import org.x4juli.global.spi.ExtendedFormatter;
import org.x4juli.global.spi.ExtendedHandler;
import org.x4juli.global.spi.LoggerRepository;
import org.x4juli.global.spi.OptionHandler;
import org.xml.sax.Attributes;

/**
 * Missing documentation.
 * <p>
 * Logging API as a whole was originally done for <a href="http://logging.apache.org/log4j/">Apache
 * log4j</a>. <b>x4uli</b> is a port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging APIs</a>. All
 * credits for initial idea, design, implementation, documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This file was originally
 * published by <i>unknown</i>. Please use exclusively the <i>appropriate</i> mailing lists for
 * questions, remarks and contribution.
 * </p>
 * 
 * @todo Missing documentation.
 * @author Boris Unckel
 * @since 0.7
 */
public class FormatterAction extends AbstractAction {
    ExtendedFormatter formatter;
    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void begin(ExecutionContext ec, String name, Attributes attributes)
            throws ActionException {
        // Let us forget about previous errors (in this object)
        inError = false;

        String className = attributes.getValue(CLASS_ATTRIBUTE);
        try {
          getLogger().finer(
            "About to instantiate layout of type [" + className + "]");

        
          formatter = (ExtendedFormatter)
            OptionConverter.instantiateByClassName(
              className, org.x4juli.global.spi.ExtendedFormatter.class, null);

          LoggerRepository repo = (LoggerRepository) ec.getObjectStack().get(0);
          formatter.setLoggerRepository(repo);

          getLogger().finer("Pushing layout on top of the object stack.");
          ec.pushObject(formatter);
        } catch (Exception oops) {
          inError = true;
          getLogger().log(Level.SEVERE,
            "Could not create an Layout. Reported error follows.", oops);
          ec.addError(
            new ErrorItem("Could not create layout of type " + className + "]."));
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void end(ExecutionContext ec, String name) throws ActionException {
        if (inError) {
            return;
          }

          if (formatter instanceof OptionHandler) {
            ((OptionHandler) formatter).activateOptions();
          }

          Object o = ec.peekObject();

          if (o != formatter) {
            getLogger().warning(
              "The object on the top the of the stack is not the layout pushed earlier.");
          } else {
            getLogger().finer("Popping layout from the object stack");
            ec.popObject();

            try {
              getLogger().finer(
                "About to set the layout of the containing appender.");
              ExtendedHandler handler = (ExtendedHandler) ec.peekObject();
              handler.setFormatter(formatter);
            } catch (Exception ex) {
              getLogger().log(Level.SEVERE,
                "Could not set the layout for containing appender.", ex);
            }
          }
    }

}

// EOF FormatterAction.java
