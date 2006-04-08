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
import org.x4juli.global.helper.Option;
import org.x4juli.global.helper.OptionConverter;
import org.x4juli.global.plugins.Plugin;
import org.x4juli.global.spi.ErrorItem;
import org.x4juli.global.spi.LoggerRepository;
import org.x4juli.global.spi.OptionHandler;
import org.xml.sax.Attributes;

/**
 * Instantiates an plugin of the given class and sets its name.
 * 
 * The plugin thus generated is placed in the ExecutionContext plugin bag.
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
public class PluginAction extends AbstractAction {
    private Plugin plugin;

    /**
     * {@inheritDoc}
     * 
     * @since 0.7
     */
    public void begin(ExecutionContext ec, String name, Attributes attributes)
            throws ActionException {
        String className = attributes.getValue(CLASS_ATTRIBUTE);

        try {
            getLogger().finer("About to instantiate plugin of type [" + className + "]");

            plugin = (Plugin) OptionConverter.instantiateByClassName(className,
                    org.x4juli.global.plugins.Plugin.class, null);

            String pluginName = attributes.getValue(NAME_ATTRIBUTE);

            if (Option.isEmpty(pluginName)) {
                getLogger().warning("No plugin name given for plugin of type " + className + "].");
            } else {
                plugin.setName(pluginName);
                getLogger().finer("plugin named as [" + pluginName + "]");
            }

            LoggerRepository rep = (LoggerRepository) ec.getObject(0);

            repository.getPluginRegistry().addPlugin(plugin);
            plugin.setLoggerRepository(rep);

            getLogger().finer("Pushing plugin on to the object stack.");
            ec.pushObject(plugin);
        } catch (Exception oops) {
            inError = true;
            getLogger().log(Level.SEVERE, "Could not create a plugin. Reported error follows.",
                    oops);
            ec.addError(new ErrorItem("Could not create plugin of type " + className + "]."));
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

        if (plugin instanceof OptionHandler) {
            ((OptionHandler) plugin).activateOptions();
        }

        Object o = ec.peekObject();

        if (o != plugin) {
            getLogger().warning(
                    "The object at the of the stack is not the plugin named [" + plugin.getName()
                            + "] pushed earlier.");
        } else {
            getLogger().log(Level.FINER, "Popping plugin named [{0}] from the object stack",
                    plugin.getName());
            ec.popObject();
        }
    }

}

// EOF PluginAction.java
