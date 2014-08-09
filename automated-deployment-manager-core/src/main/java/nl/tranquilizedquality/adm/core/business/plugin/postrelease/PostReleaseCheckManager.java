package nl.tranquilizedquality.adm.core.business.plugin.postrelease;

import java.util.List;

import nl.tranquilizedquality.adm.api.plugin.postrelease.PostReleaseCheckPlugin;
import nl.tranquilizedquality.adm.api.plugin.postrelease.PostReleaseCheckResults;

/**
 * Manager that can execute post release checks.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 21 mrt. 2013
 */
public interface PostReleaseCheckManager {

    /**
     * Searches for post release check plugins.
     * 
     * @return Returns a {@link List} containing {@link PostReleaseCheckPlugin} objects or an empty
     *         one if there are none.
     */
    List<PostReleaseCheckPlugin> findPostReleaseCheckPlugins();

    /**
     * Executes the post release checks for the plugin with the specified name.
     * 
     * @param pluginName
     *        The name of the plugin that can perform post release checks.
     * @return Returns a {@link PostReleaseCheckResults} containing the check results.
     */
    PostReleaseCheckResults executePostReleaseCheck(String pluginName);

}
