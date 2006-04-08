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
package org.x4juli.config;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.logging.Level;

/**
 * Used for inferring configuration information for a x4juli component.
 * <p>
 * Logging API as a whole was originally done for <a href="http://logging.apache.org/log4j/">Apache
 * log4j</a>. <b>x4uli</b> is a port of main parts of that to complete the <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/">Java Logging APIs</a>. All
 * credits for initial idea, design, implementation, documentation belong to the <a
 * href="http://logging.apache.org/site/who-we-are.html">log4j crew</a>. This file was originally
 * published by <i>Anders Kristensen</i>. Please use exclusively the <i>appropriate</i> mailing
 * lists for questions, remarks and contribution.
 * </p>
 * 
 * @author Boris Unckel
 * @since 0.7
 */
public class PropertyGetter {

    protected static final Object[] NULL_ARG = new Object[] {  };
    protected Object obj;
    protected PropertyDescriptor[] props;

    /**
      Create a new PropertyGetter for the specified Object. This is done
      in prepartion for invoking {@link
      #getProperties(PropertyGetter.PropertyCallback, String)} one or
      more times.

      @param obj the object for which to set properties */
    public PropertyGetter(Object obj) throws IntrospectionException {
      BeanInfo bi = Introspector.getBeanInfo(obj.getClass());
      props = bi.getPropertyDescriptors();
      this.obj = obj;
    }

    public static void getProperties(
      Object obj, PropertyCallback callback, String prefix) {
      try {
        new PropertyGetter(obj).getProperties(callback, prefix);
      } catch (IntrospectionException ex) {
          //TODO NOPLogger
      }
    }

    public void getProperties(PropertyCallback callback, String prefix) {
      for (int i = 0; i < props.length; i++) {
        Method getter = props[i].getReadMethod();
        if (getter == null) {
          continue;
        }
        if (!isHandledType(getter.getReturnType())) {
          continue;
        }
        String name = props[i].getName();
        try {
          Object result = getter.invoke(obj, NULL_ARG);

          if (result != null) {
            callback.foundProperty(obj, prefix, name, result);
          }
        } catch (Exception ex) {
            //TODO NopLog 
        }
      }
    }

    protected boolean isHandledType(Class type) {
      return String.class.isAssignableFrom(type)
      || Integer.TYPE.isAssignableFrom(type) || Long.TYPE.isAssignableFrom(type)
      || Boolean.TYPE.isAssignableFrom(type)
      || Level.class.isAssignableFrom(type);
    }

    public interface PropertyCallback {
      void foundProperty(Object obj, String prefix, String name, Object value);
    }

}

// EOF PropertyGetter.java
