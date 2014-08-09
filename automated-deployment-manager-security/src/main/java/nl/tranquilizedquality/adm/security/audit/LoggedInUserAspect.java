package nl.tranquilizedquality.adm.security.audit;

import java.util.Collection;
import java.util.Date;

import nl.tranquilizedquality.adm.commons.hibernate.bean.AbstractInsertableDomainObject;
import nl.tranquilizedquality.adm.commons.hibernate.bean.AbstractUpdatableDomainObject;
import nl.tranquilizedquality.adm.security.persistence.db.dao.UserDao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

/**
 * This aspect sets the logged in user on domain objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 20 okt. 2011
 */
public class LoggedInUserAspect {

	/** Logger for this class. */
	private static final Log log = LogFactory.getLog(LoggedInUserAspect.class);

	/** DAO used to retrieve the ADM user. */
	private UserDao userDao;

	/**
	 * @return
	 */
	private nl.tranquilizedquality.adm.commons.business.domain.User findLoggedInUser() {
		/*
		 * Set the logged in user. This is extracted from the manager itself
		 * since it contains security specific code which should not be mingled
		 * in the manager class. This way you separate the concerns and it's
		 * easier to switch to other type of security.
		 */
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		/*
		 * Return null if there is no logged in user.
		 */
		if (authentication == null) {
			if (log.isWarnEnabled()) {
				log.warn("No authentication available! No user logged in.");
			}

			return null;
		}

		final User principal = (User) authentication.getPrincipal();
		final String username = principal.getUsername();
		principal.getAuthorities();

		final nl.tranquilizedquality.adm.commons.business.domain.User boaamUser = userDao.findActiveUserByUserName(username);

		return boaamUser;
	}

	@SuppressWarnings("rawtypes")
	public void processDomainObject(final AbstractInsertableDomainObject object) {
		if (log.isTraceEnabled()) {
			log.trace("Processing domain object " + object.toString());
		}

		final nl.tranquilizedquality.adm.commons.business.domain.User damUser = findLoggedInUser();

		/*
		 * Return if there is no logged in user.
		 */
		if (damUser == null) {
			return;
		}

		object.setCreated(new Date());

		final String createdBy = damUser.getUserName();
		object.setCreatedBy(createdBy);
	}

	@SuppressWarnings({ "rawtypes" })
	public void processUpdateAbleObject(final AbstractUpdatableDomainObject object) {
		/*
		 * Only perform this if it's a newly created domain object.
		 */
		if (!object.isPersistent()) {
			processDomainObject(object);
		}

		if (log.isTraceEnabled()) {
			log.trace("Processing updatable object " + object.toString());
		}

		final nl.tranquilizedquality.adm.commons.business.domain.User damUser = findLoggedInUser();

		/*
		 * Return if there is no logged in user or if the object is a new
		 * object. The altered by stuff will only be filled in when updating a
		 * user.
		 */
		if (damUser == null || !object.isPersistent()) {
			return;
		}

		object.setAltered(new Date());

		final String alteredBy = damUser.getUserName();
		object.setAlteredBy(alteredBy);
	}

	@SuppressWarnings("rawtypes")
	public void processCollection(final Collection<AbstractInsertableDomainObject> collection) {
		for (final AbstractInsertableDomainObject domainObject : collection) {
			processDomainObject(domainObject);
		}
	}

	/**
	 * @param userDao
	 *            the userDao to set
	 */
	@Required
	public void setUserDao(final UserDao userDao) {
		this.userDao = userDao;
	}

}
