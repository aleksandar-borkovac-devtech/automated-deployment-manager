/*
 * @(#)PluginManager.java 20 mrt. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.api.plugin;

import java.util.List;

/**
 * Manager that manages all types of plugins within ADM.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 20 mrt. 2013
 */
public interface PluginManager {

    /**
     * Registers a plugin so it can be used by ADM.
     * 
     * @param plugin
     *        The plugin that will be registered.
     */
    void registerPlugin(Plugin plugin);

    /**
     * Removes the plugin from the plugin manager registry so it cannot be used anymore within ADM.
     * 
     * @param plugin
     *        The plugin that will be removed from the registry.
     */
    void unregisterPlugin(Plugin plugin);

    /**
     * Installs all plugins in that are plugin location.
     */
    void installPlugins();

    /**
     * Retrieves all registered plugins.
     * 
     * @return Returns a {@link List} containing all the registered plugins or an empty one if none
     *         are registered.
     */
    List<Plugin> findRegisteredPlugins();

    /**
     * Retrieves a plugin with the specified name.
     * 
     * @param name
     *        The name of the plugin that needs to be retrieved.
     * @return Returns a {@link Plugin}.
     */
    Plugin findPluginByName(String name);

}
