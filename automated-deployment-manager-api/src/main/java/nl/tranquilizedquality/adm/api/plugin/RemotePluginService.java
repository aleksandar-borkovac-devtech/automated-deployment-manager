/*
 * @(#)RemotePluginService.java 29 mrt. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.api.plugin;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Plugin service that provides functionality to register and unregister
 * plugins.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 29 mrt. 2013
 */
@Path("plugins/")
public interface RemotePluginService {

    /**
     * Registers a plugin so it can be used by ADM.
     * 
     * @param plugin
     *            The plugin that will be registered.
     */
    @POST
    void registerPlugin(Plugin plugin);

    /**
     * Removes the plugin from the plugin manager registry so it cannot be used
     * anymore within ADM.
     * 
     * @param plugin
     *            The plugin that will be removed from the registry.
     */
    @DELETE
    void unregisterPlugin(Plugin plugin);

}
