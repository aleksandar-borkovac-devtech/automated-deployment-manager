package nl.tranquilizedquality.adm.security.business.manager.impl;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.Privilege;
import nl.tranquilizedquality.adm.commons.business.domain.Scope;
import nl.tranquilizedquality.adm.security.persistence.db.dao.PrivilegeDao;
import nl.tranquilizedquality.adm.security.persistence.db.dao.ScopeDao;
import nl.tranquilizedquality.adm.security.persistence.db.dao.UserDao;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Custom implementation of the {@link UserDetailsService}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 */
public class AdmUserDetailsService implements UserDetailsService {

	/**
	 * The application scope definition used to retrieve only authorities from
	 * the specified scope.
	 */
	private String scope;

	/** DAO used to retrieve users. */
	private UserDao<nl.tranquilizedquality.adm.commons.business.domain.User> userDao;

	/** DAO used to retrieve user privileges. */
	private PrivilegeDao<Privilege> privilegeDao;

	/** DAO used to retrieve a scope. */
	private ScopeDao<Scope> scopeDao;

	@Override
	@SuppressWarnings("deprecation")
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException,
			DataAccessException {

		/*
		 * Find the active user with the specified user name.
		 */
		final nl.tranquilizedquality.adm.commons.business.domain.User boUser = userDao.findActiveUserByUserName(username);

		/*
		 * Check if there is an active user with the specified user name.
		 */
		if (boUser == null) {
			throw new UsernameNotFoundException("The user with the user name: " + username
					+ " could not be found!");
		}

		/*
		 * Update the last login date of the specified user.
		 */
		userDao.updateLastLoginDate(boUser);
		userDao.flush();

		/*
		 * Retrieve scope.
		 */
		final Scope foundScope = scopeDao.findByName(scope);
		scopeDao.flush();

		/*
		 * Retrieve the privileges from the specified user and scope.
		 */
		final List<Privilege> privileges = privilegeDao.findByUserAndScope(boUser, foundScope);
		privilegeDao.flush();

		/*
		 * Transform the found privileges into GrantedAuthority objects.
		 */
		final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		for (final Privilege privilege : privileges) {
			authorities.add(new GrantedAuthorityImpl(privilege.getName()));
		}

		/*
		 * Create a Spring security user based on the logged in user.
		 */
		final String userName = boUser.getUserName();
		final String password = boUser.getPassword();
		final boolean nonLocked = !boUser.isBlocked();

		final GrantedAuthority[] authoritiesArray = new GrantedAuthority[authorities.size()];
		final User user = new User(userName, password, true, true, true, nonLocked, authorities.toArray(authoritiesArray));

		return user;
	}

	/**
	 * @param scope
	 *            the scope to set
	 */
	@Required
	public void setScope(final String scope) {
		this.scope = scope;
	}

	/**
	 * @param userDao
	 *            the userDao to set
	 */
	@Required
	public void setUserDao(
			final UserDao<nl.tranquilizedquality.adm.commons.business.domain.User> userDao) {
		this.userDao = userDao;
	}

	/**
	 * @param privilegeDao
	 *            the privilegeDao to set
	 */
	@Required
	public void setPrivilegeDao(final PrivilegeDao<Privilege> privilegeDao) {
		this.privilegeDao = privilegeDao;
	}

	@Required
	public void setScopeDao(final ScopeDao<Scope> scopeDao) {
		this.scopeDao = scopeDao;
	}

}
