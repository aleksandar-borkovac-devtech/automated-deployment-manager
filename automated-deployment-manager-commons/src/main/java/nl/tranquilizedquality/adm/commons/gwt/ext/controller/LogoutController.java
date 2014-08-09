package nl.tranquilizedquality.adm.commons.gwt.ext.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * 
 * Controller that makes sure you can logout. This is introduced because of IE
 * behaving differently from firefox, chrome and other browsers doing a
 * javascript window replace. Now we catch the logout URL IE will create.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 27 jan. 2011
 * 
 * 
 */
public class LogoutController extends AbstractController {

	/** logger for this class */
	private static final Log LOGGER = LogFactory.getLog(LogoutController.class);

	/** The name of the view. */
	private String viewName;

	@Override
	protected ModelAndView handleRequestInternal(final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		final ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Redirecting to " + viewName);
		}

		return mav;
	}

	/**
	 * @param viewName
	 *            the viewName to set
	 */
	@Required
	public void setViewName(final String viewName) {
		this.viewName = viewName;
	}

}
