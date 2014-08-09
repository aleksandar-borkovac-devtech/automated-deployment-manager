package nl.tranquilizedquality.adm.gwt.gui.server.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import nl.tranquilizedquality.adm.commons.business.domain.Scope;

/**
 * Interface representing the service that can import data into ADM
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 26, 2012
 * 
 */
@Path("/scopes")
@Produces("text/html")
public interface ImportService {

	/**
	 * Imports a non persistent scope.
	 * 
	 * @param scope
	 *            The {@link Scope} that will be imported.
	 * @return Returns a {@link Response} object with the appropriate response
	 *         information.
	 */
	@POST
	@Path("/import")
	@Consumes("multipart/form-data")
	Response importScope(@FormParam("scope") Scope scope);

}
