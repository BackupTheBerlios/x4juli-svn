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
package org.x4juli.config.joran.spi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.x4juli.config.joran.action.AbstractAction;
import org.x4juli.config.joran.action.Action;
import org.x4juli.global.helper.OptionConverter;
import org.x4juli.global.spi.LoggerRepository;

/**
 * Missing documentation.
 * @todo Missing documentation.
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
 * @author Boris Unckel
 * @since 0.7
 */
public class SimpleRuleStore implements RuleStore {
    // key: Pattern instance, value: ArrayList containing actions
    HashMap rules = new HashMap();
    LoggerRepository repository;

    public SimpleRuleStore() {
    }

    public SimpleRuleStore(LoggerRepository repository) {
      this.repository = repository;
    }
    
    /**
     * Add a new rule, i.e. a pattern, action pair to the rule store.
     * <p>
     * Note that the added action's LoggerRepository will be set in the
     * process.
     */
    public void addRule(Pattern pattern, Action action) {
      AbstractAction myaction = (AbstractAction) action; 
      myaction.setLoggerRepository(repository);

      List a4p = (List) rules.get(pattern);

      if (a4p == null) {
        a4p = new ArrayList();
        rules.put(pattern, a4p);
      }

      a4p.add(action);
    }

    public void addRule(Pattern pattern, String actionClassName) {
      Action action =
        (Action) OptionConverter.instantiateByClassName(
          actionClassName, Action.class, null);

      if(action != null) {
          addRule(pattern, action);
        }
    }

    public List matchActions(Pattern pattern) {
      //System.out.println("pattern to search for:" + pattern + ", hashcode: " + pattern.hashCode());
      //System.out.println("rules:" + rules);
      ArrayList a4p = (ArrayList) rules.get(pattern);

      if (a4p != null) {
        return a4p;
      } else {
        Iterator patternsIterator = rules.keySet().iterator();
        int max = 0;
        Pattern longestMatch = null;

        while (patternsIterator.hasNext()) {
          Pattern p = (Pattern) patternsIterator.next();

          if ((p.size() > 1) && p.get(0).equals("*")) {
            int r = pattern.tailMatch(p);

            //System.out.println("tailMatch " +r);
            if (r > max) {
              //System.out.println("New longest match "+p);
              max = r;
              longestMatch = p;
            }
          }
        }

        if (longestMatch != null) {
          return (ArrayList) rules.get(longestMatch);
        } else {
          return null;
        }
      }
    }

}

// EOF SimpleRuleStore.java
