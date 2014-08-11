/*
 * @(#)RemoteJettyPluginManager.java 29 mrt. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.core.business.plugin;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nl.tranquilizedquality.adm.api.plugin.Plugin;
import nl.tranquilizedquality.adm.api.plugin.PluginManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.deploy.DeploymentManager;
import org.eclipse.jetty.deploy.providers.WebAppProvider;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.springframework.beans.factory.annotation.Required;

/**
 * Plugin manager that uses the Jetty embedded servlet container to load plugins
 * as WAR files. The WAR files will be started and the plugins will register
 * themsevles through an HTTP REST service.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 29 mrt. 2013
 */
public class RemoteJettyPluginManager implements PluginManager {

    /** Logger for this class. */
    private static final Log LOGGER = LogFactory.getLog(RemoteJettyPluginManager.class);

    /**
     * Jetty server that will be started to load plugins that register
     * themselves to ADM.
     */
    private final Server jettyServer;

    /** The directory where plugins are located. */
    private String pluginDirectory;

    /** Map containing all active plugins that registered themselved. */
    private final Map<String, Plugin> registeredPlugins;

    /** Jetty deployment manager. */
    private final DeploymentManager deploymentManager;

    /** Jetty context handler collection. */
    private final ContextHandlerCollection contextHandlerCollection;

    /**
     * Initializes the Jetty server on localhost:1212 but doesn't start it up
     * yet.
     * 
     * @throws Exception
     *             Is thrown if something goes wrong during initialization.
     */
    public RemoteJettyPluginManager() throws Exception {
        registeredPlugins = new HashMap<String, Plugin>();

        jettyServer = new Server(new InetSocketAddress("127.0.0.1", 1212));
        jettyServer.setStopAtShutdown(true);

        contextHandlerCollection = new ContextHandlerCollection();
        deploymentManager = new DeploymentManager();
        deploymentManager.setContexts(contextHandlerCollection);

        final HandlerCollection handlers = new HandlerCollection();
        final RequestLogHandler requestLogHandler = new RequestLogHandler();
        handlers.setHandlers(new Handler[] {contextHandlerCollection, new DefaultHandler(), requestLogHandler });
        final StatisticsHandler stats = new StatisticsHandler();
        stats.setHandler(handlers);
        jettyServer.setHandler(stats);
    }

    @Override
    public void registerPlugin(final Plugin plugin) {
        final String name = plugin.getName();
        registeredPlugins.put(name, plugin);
    }

    @Override
    public void unregisterPlugin(final Plugin plugin) {
        final String name = plugin.getName();
        registeredPlugins.remove(name);
    }

    @Override
    public void installPlugins() {
        try {
            final WebAppProvider provider = new WebAppProvider();
            provider.setMonitoredDirName(pluginDirectory);
            provider.setScanInterval(20);
            provider.setExtractWars(true);
            deploymentManager.addAppProvider(provider);

            jettyServer.addBean(deploymentManager);
            jettyServer.addBean(contextHandlerCollection);
            jettyServer.addBean(provider);

            jettyServer.start();
        } catch (final Exception e) {
            final String msg = "Failed to install plugins!";
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg, e);
            }
        }
    }

    @Override
    public List<Plugin> findRegisteredPlugins() {
        final List<Plugin> plugins = new ArrayList<Plugin>();
        final Set<Entry<String, Plugin>> entrySet = registeredPlugins.entrySet();
        for (final Entry<String, Plugin> entry : entrySet) {
            final Plugin plugin = entry.getValue();
            plugins.add(plugin);
        }

        return plugins;
    }

    @Override
    public Plugin findPluginByName(final String name) {
        return registeredPlugins.get(name);
    }

    @Required
    public void setPluginDirectory(final String pluginDirectory) {
        this.pluginDirectory = pluginDirectory;
    }

}
