package nl.tranquilizedquality.adm.core.business.manager;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.Environment;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.Release;

/**
 * Manager that can deploy complete releases to different environments.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 */
public interface DeploymentManager {

    /**
     * Deploys the specified artifacts from the specified release. With this
     * method you are able to deploy a subset of a release.
     * 
     * @param artifacts
     *            The artifacts that will be released.
     * @param release
     *            The release where the artifacts are part off.
     * @param environment
     *            The environment to deploy to.
     * @return Returns the {@link Release} that was deployed.
     */
    Release deployArtifacts(List<MavenArtifact> artifacts, Release release, Environment environment);

    /**
     * Deploys the whole release to the specified environment.
     * 
     * @param release
     *            The release that will be deployed.
     * @param environment
     *            The environment to deploy to.
     */
    void deployRelease(Release release, Environment environment);

    /**
     * Deploys the specified artifact to the specified environment.
     * 
     * @param artifact
     *            The artifact that will be deployed.
     * @param environment
     *            The environment to deploy to.
     */
    void deployArtifact(MavenArtifact artifact, Environment environment);

}
