package nl.tranquilizedquality.adm.security.business.manager;

import nl.tranquilizedquality.adm.commons.business.domain.User;

/**
 * Manager that manages the security context.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public interface SecurityContextManager {

	/**
	 * Searches for the logged in User.
	 * 
	 * @return Returns the {@link User} which is currently logged in.
	 */
	User findLoggedInUser();

}
