/*
 * @(#)AbstractDefaultPostReleaseCheckPlugin.java 20 mrt. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.api.plugin.postrelease;

import nl.tranquilizedquality.adm.api.plugin.PluginManager;

import org.springframework.beans.factory.annotation.Required;

/**
 * Abstract base class for a post release check plugin that registers itself to
 * ADM.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 20 mrt. 2013
 */
public abstract class AbstractDefaultPostReleaseCheckPlugin implements PostReleaseCheckPlugin {

    /** Manager used to register the plugin. */
    private PluginManager pluginManager;

    @Override
    public void intialize() {
        pluginManager.registerPlugin(this);
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    @Required
    public void setPluginManager(final PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        intialize();
    }

}
