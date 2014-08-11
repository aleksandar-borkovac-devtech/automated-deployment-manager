/*
 * @(#)PostReleaseCheckManagerImpl.java 19 mrt. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.core.business.plugin.postrelease;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.tranquilizedquality.adm.api.plugin.Plugin;
import nl.tranquilizedquality.adm.api.plugin.PluginDirectoryType;
import nl.tranquilizedquality.adm.api.plugin.PluginManager;
import nl.tranquilizedquality.adm.api.plugin.postrelease.PostReleaseCheckPlugin;
import nl.tranquilizedquality.adm.api.plugin.postrelease.PostReleaseCheckResults;

import org.springframework.beans.factory.annotation.Required;

/**
 * Manager that can execute post release checks.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 19 mrt. 2013
 */
public class PostReleaseCheckManagerImpl implements PostReleaseCheckManager {

    /** Manager used to retrieve the post release check plugins. */
    private PluginManager pluginManager;

    /** The directory where plugins are located. */
    private String pluginDirectory;

    /**
     * The work directory for plugins that will be used for e.g. reporting of a
     * plugin.
     */
    private String pluginWorkDirectory;

    @Override
    public List<PostReleaseCheckPlugin> findPostReleaseCheckPlugins() {

        final List<PostReleaseCheckPlugin> postReleaseCheckPlugins = new ArrayList<PostReleaseCheckPlugin>();
        final List<Plugin> registeredPlugins = pluginManager.findRegisteredPlugins();
        for (final Plugin plugin : registeredPlugins) {

            if (plugin instanceof PostReleaseCheckPlugin) {
                postReleaseCheckPlugins.add((PostReleaseCheckPlugin) plugin);
            }
        }

        return postReleaseCheckPlugins;
    }

    @Override
    public PostReleaseCheckResults executePostReleaseCheck(final String pluginName) {
        final Plugin plugin = pluginManager.findPluginByName(pluginName);
        if (plugin instanceof PostReleaseCheckPlugin) {
            final PostReleaseCheckPlugin postReleaseCheckPlugin = (PostReleaseCheckPlugin) plugin;

            final Map<String, String> parameters = new HashMap<String, String>();
            parameters.put(PluginDirectoryType.PLUGIN_DIRECTORY.name(), pluginDirectory);
            parameters.put(PluginDirectoryType.PLUGIN_WORK_DIRECTORY.name(), pluginWorkDirectory);
            return postReleaseCheckPlugin.executePostReleaseChecks(parameters);
        }

        throw new InvalidPluginTypeException("Invalid plugin type! " + pluginName);
    }

    @Required
    public void setPluginManager(final PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    @Required
    public void setPluginWorkDirectory(final String pluginWorkDirectory) {
        this.pluginWorkDirectory = pluginWorkDirectory;
    }

}
