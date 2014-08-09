/*
 * @(#)RemoteJettyPluginManagerTest.java 29 mrt. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.core.business.plugin;

import nl.tranquilizedquality.adm.core.business.plugin.RemoteJettyPluginManager;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 29 mrt. 2013
 */
public class RemoteJettyPluginManagerTest {

    private RemoteJettyPluginManager pluginManager;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        pluginManager = new RemoteJettyPluginManager();
        pluginManager.setPluginDirectory("plugins/");
    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.plugin.RemoteJettyPluginManager#registerPlugin(nl.tranquilizedquality.adm.api.plugin.Plugin)}
     * .
     */
    @Test
    public void testRegisterPlugin() throws Exception {
        pluginManager.installPlugins();

        Thread.sleep(5000);
    }

}
