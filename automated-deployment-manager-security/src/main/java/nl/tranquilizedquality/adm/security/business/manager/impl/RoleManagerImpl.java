package nl.tranquilizedquality.adm.security.business.manager.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.tranquilizedquality.adm.commons.business.command.RoleSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.Privilege;
import nl.tranquilizedquality.adm.commons.business.domain.Role;
import nl.tranquilizedquality.adm.commons.business.domain.Scope;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.business.domain.UserRole;
import nl.tranquilizedquality.adm.security.business.exception.RoleManagerException;
import nl.tranquilizedquality.adm.security.business.manager.RoleManager;
import nl.tranquilizedquality.adm.security.persistence.db.dao.RoleDao;
import nl.tranquilizedquality.adm.security.persistence.db.dao.ScopeDao;
import nl.tranquilizedquality.adm.security.persistence.db.dao.UserDao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Implementation of the {@link RoleManager}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 */
public class RoleManagerImpl implements RoleManager {

    /** Logger for this class. */
    private static final Log LOG = LogFactory.getLog(RoleManagerImpl.class);

    /** DAO to retrieve {@link Role} objects. */
    private RoleDao<Role> roleDao;

    /** DAO to retrieve {@link User} objects. */
    private UserDao<User> userDao;

    /** DAO to retrieve {@link Scope} objects. */
    private ScopeDao<Scope> scopeDao;

    /** Validates a {@link Role}. */
    private Validator roleValidator;

    @Override
    public Role storeRole(final Role role, final Errors errors) throws RoleManagerException {

        roleValidator.validate(role, errors);

        if (errors.hasErrors()) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Role has validation errors: " + role);
            }
            throw new RoleManagerException("Invalid role!");
        }

        if (role.isPersistent()) {
            final Role foundRole = roleDao.findById(role.getId());

            foundRole.copy(role);

            final Role savedRole = roleDao.save(foundRole);

            initialize(savedRole);

            return savedRole;
        } else {
            final Role savedRole = roleDao.save(role);

            initialize(savedRole);

            return savedRole;
        }

    }

    @Override
    public void deleteRole(final Role role, final Errors errors) throws RoleManagerException {
        if (!role.isPersistent()) {
            final String msg = "Can't delete a role that is not persistent!";
            errors.reject("role.invalid-role", msg);

            throw new RoleManagerException(msg);
        }

        final Role foundRole = findRoleById(role.getId());

        /*
         * Delete role privileges.
         */
        foundRole.setPrivileges(new HashSet<Privilege>());
        roleDao.save(foundRole);

        final List<UserRole> users = foundRole.getUserRoles();

        if (users == null || users.isEmpty()) {
            roleDao.refresh(foundRole);
            roleDao.delete(foundRole);
        } else {
            final String msg = "Can't delete a role that is in use!";
            errors.reject("role.in-use", msg);

            throw new RoleManagerException(msg);
        }
    }

    /**
     * Initializes the role collections.
     * 
     * @param role
     *            The {@link Role} that will be initialized.
     */
    private void initialize(final Role role) {
        final Set<Privilege> privileges = role.getPrivileges();
        if (privileges != null) {
            for (final Privilege privilege : privileges) {
                privilege.getId();
            }
        }

        final List<UserRole> userRoles = role.getUserRoles();
        if (userRoles != null) {
            for (final UserRole userRole : userRoles) {
                userRole.getId();
            }
        }

    }

    @Override
    public Role findRoleById(final Long id) {
        final Role role = roleDao.findById(id);

        initialize(role);

        return role;
    }

    @Override
    public List<Role> findRoles(final RoleSearchCommand sc) {
        final List<Role> foundRoles = roleDao.findRoles(sc);

        for (final Role role : foundRoles) {
            initialize(role);
        }

        return foundRoles;
    }

    @Override
    public List<Role> findGrantableRolesByLoggedInUserAndScope(final RoleSearchCommand sc) {
        /*
         * Setup search criteria.
         */
        final Scope scope = sc.getScope();
        if (scope != null) {
            final Scope originalScope = scopeDao.findById(scope.getId());
            sc.setScope(originalScope);
        }

        final User user = sc.getUser();
        sc.setUser(null);
        sc.setValid(true);

        /*
         * Search for the roles.
         */
        final List<Role> scopeRoles = roleDao.findRoles(sc);

        /*
         * Initialize the roles.
         */
        for (final Role role : scopeRoles) {
            initialize(role);
        }

        if (user != null) {
            final Long userId = user.getId();
            final User originalUser = userDao.findById(userId);
            sc.setUser(originalUser);
        }
        final List<Role> userRoles = roleDao.findRoles(sc);

        final List<Role> filteredRoles = new ArrayList<Role>(scopeRoles);
        filteredRoles.removeAll(userRoles);

        return filteredRoles;
    }

    @Override
    public int findNumberOfRoles(final RoleSearchCommand sc) {
        return roleDao.findNumberOfRoles(sc);
    }

    /**
     * @param roleDao
     *            the roleDao to set
     */
    @Required
    public void setRoleDao(final RoleDao<Role> roleDao) {
        this.roleDao = roleDao;
    }

    /**
     * @param roleValidator
     *            the roleValidator to set
     */
    @Required
    public void setRoleValidator(final Validator roleValidator) {
        this.roleValidator = roleValidator;
    }

    /**
     * @param userDao
     *            the userDao to set
     */
    @Required
    public void setUserDao(final UserDao<User> userDao) {
        this.userDao = userDao;
    }

    /**
     * @param scopeDao
     *            the scopeDao to set
     */
    @Required
    public void setScopeDao(final ScopeDao<Scope> scopeDao) {
        this.scopeDao = scopeDao;
    }

}
