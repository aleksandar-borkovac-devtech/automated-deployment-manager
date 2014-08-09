package nl.tranquilizedquality.adm.security.business.manager.impl;

import nl.tranquilizedquality.adm.security.business.manager.SecurityContextManager;
import nl.tranquilizedquality.adm.security.persistence.db.dao.UserDao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

/**
 * Manager that provides sercurity services.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 17 jan. 2012
 * 
 */
public class SecurityContextManagerImpl implements SecurityContextManager {

	/** Logger for this class. */
	private static final Log LOGGER = LogFactory.getLog(SecurityContextManagerImpl.class);

	/** DAO used to retrieve the user. */
	private UserDao<nl.tranquilizedquality.adm.commons.business.domain.User> userDao;

	@Override
	public nl.tranquilizedquality.adm.commons.business.domain.User findLoggedInUser() {
		/*
		 * Retrieve the security context of the current thread.
		 */
		final SecurityContext context = SecurityContextHolder.getContext();

		/*
		 * Retrieve the current authentication.
		 */
		final Authentication authentication = context.getAuthentication();

		/*
		 * Return null if there is no logged in user.
		 */
		if (authentication == null) {
			final String msg = "No authentication available! No user logged in.";
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn(msg);
			}

			throw new SecurityException(msg);
		}

		final User principal = (User) authentication.getPrincipal();
		final String username = principal.getUsername();

		final nl.tranquilizedquality.adm.commons.business.domain.User member = userDao.findActiveUserByUserName(username);

		return member;
	}

	@Required
	public void setUserDao(final UserDao<nl.tranquilizedquality.adm.commons.business.domain.User> userDao) {
		this.userDao = userDao;
	}

}
