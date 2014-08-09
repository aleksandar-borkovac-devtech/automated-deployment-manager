/*
 * @(#)PostReleaseCheckManagerTest.java 22 mrt. 2013
 *
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.core.business.plugin.postrelease;

import static junit.framework.Assert.assertEquals;
import static org.easymock.EasyMock.expect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.tranquilizedquality.adm.api.plugin.Plugin;
import nl.tranquilizedquality.adm.api.plugin.PluginManager;
import nl.tranquilizedquality.adm.api.plugin.PluginType;
import nl.tranquilizedquality.adm.api.plugin.postrelease.AbstractDefaultPostReleaseCheckPlugin;
import nl.tranquilizedquality.adm.api.plugin.postrelease.PostReleaseCheckPlugin;
import nl.tranquilizedquality.adm.api.plugin.postrelease.PostReleaseCheckResults;
import nl.tranquilizedquality.adm.api.plugin.postrelease.PostReleaseCheckStatus;

import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link PostReleaseCheckManager}.
 *
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 22 mrt. 2013
 */
public class PostReleaseCheckManagerTest extends EasyMockSupport {

    /** Manager that will be tested. */
    private PostReleaseCheckManagerImpl postReleaseCheckManager;

    /** Mocked manager. */
    private PluginManager pluginManager;

    /**
     * Test plugin class.
     *
     * @author Salomo Petrus (salomo.petrus@tr-quality.com)
     * @since 22 mrt. 2013
     */
    private class ADMPostReleaseChecksPlugin extends AbstractDefaultPostReleaseCheckPlugin {

        @Override
        public PostReleaseCheckResults executePostReleaseChecks(final Map<String, String> parameters) {

            final PostReleaseCheckResults results = new PostReleaseCheckResults("logs", PostReleaseCheckStatus.PASSED);
            return results;
        }

        @Override
        public String getName() {
            return "ADM Sanity Checks";
        }

        @Override
        public String getVersion() {
            return "1.0.0";
        }

        @Override
        public PluginType getType() {
            return PluginType.POST_RELEASE_CHECK;
        }

    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        postReleaseCheckManager = new PostReleaseCheckManagerImpl();

        pluginManager = createMock(PluginManager.class);

        postReleaseCheckManager.setPluginManager(pluginManager);
    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.plugin.postrelease.PostReleaseCheckManagerImpl#findPostReleaseCheckPlugins()}
     * .
     */
    @Test
    public void testFindPostReleaseCheckPlugins() {

        final ADMPostReleaseChecksPlugin plugin = new ADMPostReleaseChecksPlugin();
        final List<Plugin> plugins = new ArrayList<Plugin>();
        plugins.add(plugin);

        expect(pluginManager.findRegisteredPlugins()).andReturn(plugins);

        replayAll();

        final List<PostReleaseCheckPlugin> releaseCheckPlugins = postReleaseCheckManager.findPostReleaseCheckPlugins();

        verifyAll();

        assertEquals("Invalid number of plugins!", 1, releaseCheckPlugins.size());
    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.plugin.postrelease.PostReleaseCheckManagerImpl#executePostReleaseCheck(java.lang.String)}
     * .
     */
    @Test
    public void testExecutePostReleaseCheck() {

        final ADMPostReleaseChecksPlugin plugin = new ADMPostReleaseChecksPlugin();
        expect(pluginManager.findPluginByName("ADM Sanity Checks")).andReturn(plugin);

        replayAll();

        final PostReleaseCheckResults results = postReleaseCheckManager.executePostReleaseCheck("ADM Sanity Checks");

        verifyAll();

        final String logs = results.getLogs();
        final PostReleaseCheckStatus status = results.getStatus();

        assertEquals("Invalid logs!", "logs", logs);
        assertEquals("Invalid status!", PostReleaseCheckStatus.PASSED, status);
    }

}
