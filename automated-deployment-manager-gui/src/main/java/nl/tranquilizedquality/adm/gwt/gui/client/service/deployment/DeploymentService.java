package nl.tranquilizedquality.adm.gwt.gui.client.service.deployment;

import java.util.List;

import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenArtifact;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientDeployer;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientEnvironment;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientProgress;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientRelease;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Remote service that provides deployment services.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 6 okt. 2011
 */
@RemoteServiceRelativePath("DeploymentService.rpc")
public interface DeploymentService extends RemoteService {

    /**
     * Utility class for simplifying access to the instance of async service.
     */
    public static class Util {

        /** The async service. */
        private static DeploymentServiceAsync instance;

        /**
         * Retrieves an instance of the {@link DeploymentServiceAsync}.
         * 
         * @return Returns a {@link DeploymentServiceAsync}.
         */
        public static DeploymentServiceAsync getInstance() {
            if (instance == null) {
                instance = GWT.create(DeploymentService.class);
            }
            return instance;
        }
    }

    /**
     * Deploys the specified release to the specified environment.
     * 
     * @param release
     *        The release that will be deployed.
     * @param environment
     *        The environment where the release will be deployed to.
     * @return Returns the released release.
     */
    ClientRelease deployRelease(ClientRelease release, ClientEnvironment environment);

    /**
     * Deploys the specified artifacts of the specified release to the specified
     * environment.
     * 
     * @param artifacts
     *        The artifacts that will be deployed.
     * @param release
     *        The release where the artifacts are part off.
     * @param environment
     *        The environment where the artifacts will be deployed to.
     * @return Returns the released release.
     */
    ClientRelease deployArtifacts(List<ClientMavenArtifact> artifacts, ClientRelease release, ClientEnvironment environment);

    /**
     * @return
     */
    ClientProgress getProgress();

    List<ClientDeployer> findAvailableDeployers();

}
