package nl.tranquilizedquality.adm.gwt.gui.server.rest.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response;

import nl.tranquilizedquality.adm.gwt.gui.server.rest.ImageService;
import nl.tranquilizedquality.adm.security.business.manager.UserManager;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Implementation of the image service that retrieves images from the file
 * system.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 26, 2012
 * 
 */
public class ImageServiceImpl implements ImageService {

	/** Logger for this class. */
	private static final Log LOG = LogFactory.getLog(ImageServiceImpl.class);

	/** Manager that manages users. */
	private UserManager userManager;

	@Override
	public Response getUserPicture(final Long id) {

		try {
			final File memberPicture = userManager.findUserPicture(id);

			final FileInputStream fis = new FileInputStream(memberPicture);

			final CacheControl control = new CacheControl();
			control.setNoCache(true);

			return Response.ok(fis).cacheControl(control).build();
		}
		catch (final Exception e) {
			if (LOG.isErrorEnabled()) {
				LOG.error(e.getMessage(), e);
			}

			final CacheControl control = new CacheControl();
			control.setNoCache(true);
			return Response.serverError().entity(e.getMessage()).cacheControl(control).build();
		}
	}

	@Override
	public Response addUserPicture(final InputStream input, final Long id) {

		try {
			final byte[] image = IOUtils.toByteArray(input);

			userManager.storeUserPicture(id, image);

			final CacheControl control = new CacheControl();
			control.setNoCache(true);

			return Response.ok().cacheControl(control).build();
		}
		catch (final Exception e) {
			if (LOG.isErrorEnabled()) {
				LOG.error(e.getMessage(), e);
			}

			final CacheControl control = new CacheControl();
			control.setNoCache(true);

			return Response.serverError().entity(e.getMessage()).cacheControl(control).build();
		}

	}

	/**
	 * @param userManager
	 *            the userManager to set
	 */
	@Required
	public void setUserManager(final UserManager userManager) {
		this.userManager = userManager;
	}

}
