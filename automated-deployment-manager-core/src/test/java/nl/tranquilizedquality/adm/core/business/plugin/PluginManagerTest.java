/*
 * @(#)PluginManagerTest.java 21 mrt. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.core.business.plugin;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.tranquilizedquality.adm.api.plugin.Plugin;
import nl.tranquilizedquality.adm.api.plugin.PluginType;
import nl.tranquilizedquality.adm.api.plugin.postrelease.PostReleaseCheckPlugin;
import nl.tranquilizedquality.adm.api.plugin.postrelease.PostReleaseCheckResults;
import nl.tranquilizedquality.adm.core.business.plugin.PluginManagerImpl;

import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test for {@link PluginManagerImpl}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 21 mrt. 2013
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:plugin-test-application-context.xml" })
public class PluginManagerTest extends EasyMockSupport {

    /** Manager that will be tested. */
    @Autowired
    private PluginManagerImpl pluginManager;

    /** The plugin that will be used within the tests. */
    private Plugin plugin;

    @Before
    public void setUp() {
        plugin = new Plugin() {

            @Override
            public void afterPropertiesSet() throws Exception {
            }

            @Override
            public String getVersion() {
                return null;
            }

            @Override
            public PluginType getType() {
                return null;
            }

            @Override
            public String getName() {
                return null;
            }
        };
    }

    @After
    public void cleanUp() {
        final List<Plugin> registeredPlugins = pluginManager.findRegisteredPlugins();
        for (final Plugin plugin : registeredPlugins) {
            pluginManager.unregisterPlugin(plugin);
        }
    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.plugin.PluginManagerImpl#installPlugins()}
     * .
     */
    @Test
    @Ignore
    public void testInstallPlugins() {
        pluginManager.installPlugins();

        final List<Plugin> registeredPlugins = pluginManager.findRegisteredPlugins();

        assertEquals("Invalid number of registered plugins!", 1, registeredPlugins.size());

        for (final Plugin plugin : registeredPlugins) {
            if (plugin instanceof PostReleaseCheckPlugin) {
                final PostReleaseCheckPlugin postReleaseCheckPlugin = (PostReleaseCheckPlugin) plugin;
                final Map<String, String> params = new HashMap<String, String>();
                params.put("PLUGIN_WORK_DIRECTORY", "target/");
                params.put("PLUGIN_DIRECTORY", "plugins/");
                final PostReleaseCheckResults checkResults = postReleaseCheckPlugin.executePostReleaseChecks(params);
                System.out.println("---------------------------------------------------------------------------");
                System.out.println(checkResults.getLogs());
                System.out.println("---------------------------------------------------------------------------");
            }
        }
    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.plugin.PluginManagerImpl#registerPlugin(nl.tranquilizedquality.adm.api.plugin.Plugin)}
     * .
     */
    @Test
    @Ignore
    public void testRegisterPlugin() {

        pluginManager.registerPlugin(plugin);

        final List<Plugin> registeredPlugins = pluginManager.findRegisteredPlugins();

        assertEquals("Invalid number of registered plugins!", 1, registeredPlugins.size());
    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.plugin.PluginManagerImpl#unregisterPlugin(nl.tranquilizedquality.adm.api.plugin.Plugin)}
     * .
     */
    @Test
    @Ignore
    public void testUnregisterPlugin() {
        pluginManager.registerPlugin(plugin);

        List<Plugin> registeredPlugins = pluginManager.findRegisteredPlugins();

        assertEquals("Invalid number of registered plugins!", 1, registeredPlugins.size());

        pluginManager.unregisterPlugin(plugin);

        registeredPlugins = pluginManager.findRegisteredPlugins();

        assertEquals("Invalid number of registered plugins!", 0, registeredPlugins.size());
    }

    @Test
    @Ignore
    public void test() {
        pluginManager.registerPlugin(plugin);

        final Plugin foundPlugin = pluginManager.findPluginByName(plugin.getName());

        assertNotNull("No plugin found!", foundPlugin);
        assertEquals("Invalid plugin name!", plugin.getName(), foundPlugin.getName());
    }

}
