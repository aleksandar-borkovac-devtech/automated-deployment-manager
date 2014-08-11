package nl.tranquilizedquality.adm.gwt.gui.server.rest;

import java.io.InputStream;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Service that can retrieve images securely from the file system.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 26, 2012
 * 
 */
@Path("/images/")
@Produces("image/jpg")
public interface ImageService {

    /**
     * Retrieves a user image from the member with the specified identifier.
     * 
     * @param id
     *            The member identifier.
     * @return Returns the image as an {@link InputStream}.
     */
    @GET
    @Path("/user/{id}")
    Response getUserPicture(@PathParam("id") Long id);

    /**
     * Adds a user picture.
     * 
     * @param body
     *            The body containing the user picture.
     * @param id
     *            The member identifier.
     * @return Returns a {@link Response} object.
     */
    @POST
    @Path("/user/{id}")
    Response addUserPicture(InputStream output, @PathParam("id") Long id);

}
