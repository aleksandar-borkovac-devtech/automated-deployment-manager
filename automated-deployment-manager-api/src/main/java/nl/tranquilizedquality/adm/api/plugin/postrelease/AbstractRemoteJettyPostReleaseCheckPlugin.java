/*
 * @(#)AbstractRemoteJettyPostReleaseCheckPlugin.java 29 mrt. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.api.plugin.postrelease;

import nl.tranquilizedquality.adm.api.plugin.RemotePluginService;

import org.springframework.beans.factory.annotation.Required;

/**
 * Abstract base class for plugins that are bundled in a WAR that register themselves to a REST
 * service of ADM.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 29 mrt. 2013
 */
public abstract class AbstractRemoteJettyPostReleaseCheckPlugin implements PostReleaseCheckPlugin {

    /** Service used to register a plugin. */
    private RemotePluginService pluginService;

    @Override
    public void intialize() {
        pluginService.registerPlugin(this);
    }

    public RemotePluginService getPluginService() {
        return pluginService;
    }

    @Required
    public void setPluginService(final RemotePluginService pluginService) {
        this.pluginService = pluginService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        intialize();
    }

}
