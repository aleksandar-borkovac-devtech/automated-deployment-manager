/*
 * @(#)RemotePluginServiceImpl.java 29 mrt. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.gwt.gui.server.rest.impl;

import nl.tranquilizedquality.adm.api.plugin.Plugin;
import nl.tranquilizedquality.adm.api.plugin.PluginManager;
import nl.tranquilizedquality.adm.api.plugin.RemotePluginService;

import org.springframework.beans.factory.annotation.Required;

/**
 * Implementation of the REST service that allows you to register and unregister plugins.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 29 mrt. 2013
 */
public class RemotePluginServiceImpl implements RemotePluginService {

    /** The plugin manager that can register and unregister plugins. */
    private PluginManager pluginManager;

    @Override
    public void registerPlugin(final Plugin plugin) {
        pluginManager.registerPlugin(plugin);
    }

    @Override
    public void unregisterPlugin(final Plugin plugin) {
        pluginManager.unregisterPlugin(plugin);
    }

    @Required
    public void setPluginManager(final PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

}
