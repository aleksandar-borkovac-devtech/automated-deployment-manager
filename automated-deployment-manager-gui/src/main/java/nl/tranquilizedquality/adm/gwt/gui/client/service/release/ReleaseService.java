package nl.tranquilizedquality.adm.gwt.gui.client.service.release;

import java.util.List;

import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenArtifact;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientRelease;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientReleaseExecution;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientReleaseExecutionLog;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientReleaseExecutionSearchCommand;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientReleaseSearchCommand;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Service that provides release related services.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 17 sep. 2011
 */
@RemoteServiceRelativePath("ReleaseService.rpc")
public interface ReleaseService extends RemoteService {

    /**
     * Utility class for simplifying access to the instance of async service.
     */
    public static class Util {

        /** The async service. */
        private static ReleaseServiceAsync instance;

        /**
         * Retrieves an instance of the {@link ReleaseServiceAsync}.
         * 
         * @return Returns a {@link ReleaseServiceAsync}.
         */
        public static ReleaseServiceAsync getInstance() {
            if (instance == null) {
                instance = GWT.create(ReleaseService.class);
            }
            return instance;
        }
    }

    /**
     * Searches for a {@link ClientRelease} with the specified id.
     * 
     * @param id
     *        The unique identifier.
     * @return Returns {@link ClientRelease} or null if none could be found.
     */
    ClientRelease findReleaseById(Long id);

    /**
     * Stores the specified release.
     * 
     * @param release
     *        The release that will be stored.
     * @return Returns the stored release.
     * @throws ReleaseServiceException
     *         Is thrown when something goes wrong.
     */
    ClientRelease saveRelease(ClientRelease release) throws ReleaseServiceException;

    /**
     * Searches for releases based on the specified search criteria.
     * 
     * @param config
     *        The paging configuration.
     * @param sc
     *        The search criteria.
     * @return Returns a {@link PagingLoadResult} containing the results.
     */
    PagingLoadResult<ClientMavenArtifact> findReleasesAndArtifacts(PagingLoadConfig config, ClientReleaseSearchCommand sc);

    /**
     * Finds the release history from the specified release.
     * 
     * @param config
     *        Paging configuration.
     * @param sc
     *        The search criteria to use for searching for {@link ClientReleaseExecution} objects.
     * @return Returns a {@link List} containing the release history or an empty
     *         one if there is no history.
     */
    PagingLoadResult<ClientReleaseExecution> findReleaseHistory(PagingLoadConfig config, ClientReleaseExecutionSearchCommand sc);

    /**
     * Retrieves the {@link ClientReleaseExecution} with the specified unique
     * identifier.
     * 
     * @param id
     *        The unique identifier of the {@link ClientReleaseExecution}.
     * @return Returns a {@link ClientReleaseExecution}.
     */
    ClientReleaseExecution findReleaseExecutionById(Long id);

    /**
     * Removes the specified release if it's not in use yet.
     * 
     * @param release
     *        The release that will be removed.
     * @throws ReleaseServiceException
     *         Throws exception when removing a release fails.
     */
    void removeRelease(ClientRelease release) throws ReleaseServiceException;

    /**
     * Retrieves the release execution log with the specified identifier.
     * 
     * @param id
     *        The unique identifier of the release execution log that needs
     *        to be retrieved.
     * @return Returns a {@link ClientReleaseExecutionLog} or null if none could
     *         be found.
     */
    ClientReleaseExecutionLog findReleaseExecutionLogById(Long id);

    /**
     * Archives the specified release.
     * 
     * @param release
     *        The release that is archived.
     * @throws ReleaseServiceException
     *         Exception thrown when something goes wrong during archiving.
     */
    void archiveRelease(ClientRelease release) throws ReleaseServiceException;

    /**
     * Unarchives the specified release.
     * 
     * @param release
     *        The release that will be unarchived.
     * @throws ReleaseServiceException
     *         Exception thrown when something goes wrong during unarchiving.
     */
    void unArchiveRelease(ClientRelease release) throws ReleaseServiceException;

}
