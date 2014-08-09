package nl.tranquilizedquality.adm.gwt.gui.client.service.artifact;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.MavenModule;
import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenArtifact;
import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenArtifactSearchCommand;
import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenModule;
import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenModuleSearchCommand;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Service that provides maven module related services.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 24 sep. 2011
 */
@RemoteServiceRelativePath("ArtifactService.rpc")
public interface ArtifactService extends RemoteService {

    /**
     * Utility class for simplifying access to the instance of async service.
     */
    public static class Util {

        /** The async service. */
        private static ArtifactServiceAsync instance;

        /**
         * Retrieves an instance of the {@link ArtifactServiceAsync}.
         * 
         * @return Returns a {@link ArtifactServiceAsync}.
         */
        public static ArtifactServiceAsync getInstance() {
            if (instance == null) {
                instance = GWT.create(ArtifactService.class);
            }
            return instance;
        }
    }

    /**
     * Retrieves the {@link ClientMavenModule} with the specified id.
     * 
     * @param id
     *        The unique identifier.
     * @return Returns a {@link ClientMavenModule} or null if none could be
     *         found.
     */
    ClientMavenModule findMavenModuleById(Long id);

    /**
     * Retrieves all available {@link MavenModule} objects.
     * 
     * @return Returns a {@link List} of {@link ClientMavenModule} objects.
     */
    List<ClientMavenModule> findMavenModules();

    /**
     * Retrieves {@link MavenModule} objects based on the specified search
     * criteria.
     * 
     * @param config
     *        The paging configuration.
     * @param sc
     *        The search criteria.
     * @return Returns a {@link PagingLoadResult} containing the results.
     */
    PagingLoadResult<ClientMavenModule> findMavenModules(PagingLoadConfig config, ClientMavenModuleSearchCommand sc);

    /**
     * Retrieves {@link MavenArtifact} objects based on the specified search
     * criteria.
     * 
     * @param config
     *        The paging configuration.
     * @param sc
     *        The search criteria.
     * @return Returns a {@link PagingLoadResult} containing the results.
     */
    PagingLoadResult<ClientMavenArtifact> findMavenArtifacts(PagingLoadConfig config, ClientMavenArtifactSearchCommand sc);

    /**
     * Saves the specified artifact.
     * 
     * @param artifact
     *        The artifact that will be stored.
     * @return Returns the stored artifact.
     * @throws ArtifactServiceException
     *         Will be thrown when someting goes wrong during storage.
     */
    ClientMavenArtifact saveMavenArtifact(ClientMavenArtifact artifact) throws ArtifactServiceException;

    /**
     * Saves the specified module.
     * 
     * @param module
     *        The module that will be stored.
     * @return Returns the stored artifact.
     * @throws ArtifactServiceException
     *         Will be thrown when something goes wrong during storage.
     */
    ClientMavenModule saveMavenModule(ClientMavenModule module) throws ArtifactServiceException;

    /**
     * Retrieves the artifact with the specified id.
     * 
     * @param id
     *        The unique identifier of the artifact.
     * @return Returns a {@link ClientMavenArtifact} or null if none could be
     *         found.
     */
    ClientMavenArtifact findMavenArtifactById(Long id);

    /**
     * Deletes the specified artifact.
     * 
     * @param artifact
     *        The artifact that will be deleted.
     * @throws ArtifactServiceException
     *         Is thrown when something goes wrong during deletion.
     */
    void deleteMavenArtifact(ClientMavenArtifact artifact) throws ArtifactServiceException;

    /**
     * Searches for all available maven modules excluding the one that has the specified unique
     * identifier since you cannot have a dependency on yourself.
     * 
     * @param excludeMavenModuleId
     *        The unique identifier of the maven module that should not be included.
     * @return Returns a {@link List} containing {@link MavenModule} objects or an empty one if
     *         there are no dependencies available.
     */
    List<ClientMavenModule> findAvailableMavenModules(Long excludeMavenModuleId);

}
