package nl.tranquilzedquality.adm.ws.service.rest;

import java.util.List;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response;

import nl.tranquilizedquality.adm.commons.business.command.MavenArtifactSearchCommand;
import nl.tranquilizedquality.adm.commons.business.command.ReleaseSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.Environment;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.Release;
import nl.tranquilizedquality.adm.core.business.manager.DeploymentManager;
import nl.tranquilizedquality.adm.core.business.manager.DestinationManager;
import nl.tranquilizedquality.adm.core.business.manager.MavenArtifactManager;
import nl.tranquilizedquality.adm.core.business.manager.ReleaseManager;

import org.springframework.beans.factory.annotation.Required;

/**
 * Implementation of a REST service that provides facilities to deploy an artifact.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 1 okt. 2012
 */
public class DeployRestServiceImpl implements DeployRestService {

    /** Manager used to deploy an artifact or a whole release. */
    private DeploymentManager deploymentManager;

    /** Manager used to retrieve the artifact that needs to be released. */
    private MavenArtifactManager mavenArtifactManager;

    /** Manager used to retrieve a release. */
    private ReleaseManager releaseManager;

    /** Manager used to retrieve environments. */
    private DestinationManager destinationManager;

    @Override
    public Response deployArtifact(final String releaseName, final String groupId, final String artifactId, final String version,
            final String environment) {
        final CacheControl control = new CacheControl();
        control.setNoCache(true);

        final MavenArtifactSearchCommand sc = new MavenArtifactSearchCommand();
        sc.setArtifactId(artifactId);
        sc.setVersion(version);
        sc.setGroup(groupId);
        sc.setReleaseName(releaseName);

        final List<MavenArtifact> artifacts = mavenArtifactManager.findArtifacts(sc);
        if (artifacts.isEmpty()) {
            return Response.serverError().entity("No matching artifact found!").cacheControl(control).build();
        }

        if (artifacts.size() == 1) {
            Environment targetEnvironment = null;
            final List<Environment> deployEnvironments = destinationManager.findDeployEnvironments();
            for (final Environment target : deployEnvironments) {
                final String environmentName = target.getName();
                if (environmentName.equals(environment)) {
                    targetEnvironment = target;
                    break;
                }
            }

            if (targetEnvironment != null) {
                try {
                    final MavenArtifact mavenArtifact = artifacts.get(0);
                    deploymentManager.deployArtifact(mavenArtifact, targetEnvironment);
                } catch (final Exception e) {
                    final String message = e.getMessage();
                    return Response.serverError().entity(message).cacheControl(control).build();
                }
            }
        }

        return Response.ok("artifact deployed!").cacheControl(control).build();
    }

    @Override
    public Response deployRelease(final String releaseName, final String environmentName) {
        final CacheControl control = new CacheControl();
        control.setNoCache(true);

        final ReleaseSearchCommand sc = new ReleaseSearchCommand();
        sc.setReleaseName(releaseName);
        final List<Release> releases = releaseManager.findReleases(sc);
        if (releases.isEmpty()) {
            return Response.serverError().entity("No matching release found!").cacheControl(control).build();
        }

        try {
            final Release release = releases.get(0);

            Environment targetEnvironment = null;
            final List<Environment> deployEnvironments = destinationManager.findDeployEnvironments();
            for (final Environment target : deployEnvironments) {
                final String name = target.getName();
                if (name.equals(environmentName)) {
                    targetEnvironment = target;
                    break;
                }
            }

            if (targetEnvironment != null) {
                deploymentManager.deployRelease(release, targetEnvironment);
            }
        } catch (final Exception e) {
            final String message = e.getMessage();
            return Response.serverError().entity(message).cacheControl(control).build();
        }

        return Response.ok("Release deployed!").cacheControl(control).build();
    }

    @Required
    public void setDeploymentManager(final DeploymentManager deploymentManager) {
        this.deploymentManager = deploymentManager;
    }

    @Required
    public void setMavenArtifactManager(final MavenArtifactManager mavenArtifactManager) {
        this.mavenArtifactManager = mavenArtifactManager;
    }

    @Required
    public void setDestinationManager(final DestinationManager destinationManager) {
        this.destinationManager = destinationManager;
    }

    @Required
    public void setReleaseManager(final ReleaseManager releaseManager) {
        this.releaseManager = releaseManager;
    }

}
