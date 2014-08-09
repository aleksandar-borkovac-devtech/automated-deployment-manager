package nl.tranquilzedquality.adm.ws.service.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * Service that provides remote deployment facilities.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 1 okt. 2012
 */
@Path("/")
public interface DeployRestService {

    /**
     * Deploys an artifact using the passed in parameters to determine which one it needs to deploy.
     * 
     * @param releaseName
     *        The name of the release this artifact should be part of.
     * @param groupId
     *        The group identifier where this artifact is part of.
     * @param artifactId
     *        The artifact identifier used to retrieve the correct
     * @param version
     *        The specific version that needs to be deployed.
     * @param environment
     *        The environment to deploy to.
     * @return Returns a {@link Response} object.
     */
    @POST
    @Path("/release/{releaseName}/artifact/{groupId}/{artifactId}/{version}/deploy/{environment}")
    Response deployArtifact(@PathParam("releaseName") String releaseName, @PathParam("groupId") String groupId,
            @PathParam("artifactId") String artifactId, @PathParam("version") String version, @PathParam("environment") String environment);

    /**
     * Deploy the release with the specified name to the specified environment.
     * 
     * @param releaseName
     *        The name of the release.
     * @param version
     *        The specific version that needs to be deployed.
     * @return Returns a {@link Response} object.
     */
    @POST
    @Path("/release/{releaseName}/deploy/{environment}")
    Response deployRelease(@PathParam("releaseName") String releaseName, @PathParam("environment") String environment);

}
