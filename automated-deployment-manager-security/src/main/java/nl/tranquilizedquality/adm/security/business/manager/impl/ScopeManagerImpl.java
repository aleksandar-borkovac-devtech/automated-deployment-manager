package nl.tranquilizedquality.adm.security.business.manager.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.tranquilizedquality.adm.commons.business.command.RoleSearchCommand;
import nl.tranquilizedquality.adm.commons.business.command.ScopeSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.Privilege;
import nl.tranquilizedquality.adm.commons.business.domain.Role;
import nl.tranquilizedquality.adm.commons.business.domain.Scope;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.security.business.exception.ScopeManagerException;
import nl.tranquilizedquality.adm.security.business.manager.PrivilegeManager;
import nl.tranquilizedquality.adm.security.business.manager.RoleManager;
import nl.tranquilizedquality.adm.security.business.manager.ScopeManager;
import nl.tranquilizedquality.adm.security.persistence.db.dao.ScopeDao;
import nl.tranquilizedquality.adm.security.persistence.db.dao.UserDao;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernatePrivilege;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateRole;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

/**
 * Implementation of the {@link ScopeManager}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 */
public class ScopeManagerImpl implements ScopeManager {

    /** Logger for this class. */
    private static final Log LOG = LogFactory.getLog(ScopeManagerImpl.class);

    /** DAO to retrieve {@link Scope} objects. */
    private ScopeDao<Scope> scopeDao;

    /** DAO to retrieve {@link User} objects. */
    private UserDao<User> userDao;

    /** Manager used to retrieve role information. */
    private RoleManager roleManager;

    /** Manager used to manage {@link Privilege} objects. */
    private PrivilegeManager privilegeManager;

    /** {@link Validator} that validates {@link Scope} objects. */
    private Validator scopeValidator;

    /**
     * The application scopeName definition used to retrieve only authorities
     * from the specified scopeName.
     */
    private String scopeName;

    /** The maximum number of managed scopes that can be retrieved. */
    private Integer maximumManagedScopes;

    /**
     * Default constructor
     */
    public ScopeManagerImpl() {
        maximumManagedScopes = 150;

        if (LOG.isDebugEnabled()) {
            LOG.debug("Maximum managed scopes to display: " + maximumManagedScopes);
        }
    }

    @Override
    public List<Scope> findScopes(final ScopeSearchCommand sc) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Searching for scopes...");
        }

        final List<Scope> scopes = scopeDao.findScopesBySearchCommand(sc);

        if (LOG.isInfoEnabled()) {
            LOG.info("Found " + scopes.size() + " scope(s).");
        }

        /*
         * Initialize all the collections since when searching for users we are
         * not interested in this data.
         */
        for (final Scope scope : scopes) {
            scope.getRoles();
            scope.getPrivileges();
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Collections initialized...");
        }

        return scopes;
    }

    @Override
    public List<Scope> findManagedScopesByUser(User user) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Searching for managed scopes for user: " + user);
        }

        /*
         * Find the user so the privilege check can be done on the user.
         */
        final User foundUser = userDao.findById(user.getId());

        if (LOG.isDebugEnabled()) {
            LOG.debug("Found user...");
        }

        /*
         * Loop over all the roles of the user to determine if the user is scope
         * manager for all scopes or not.
         */
        final Set<Role> userRoles = foundUser.getRoles();
        for (final Role role : userRoles) {
            final Scope scope = role.getScope();

            if (scopeName.equals(scope.getName())) {
                final Set<Privilege> privileges = role.getPrivileges();
                for (final Privilege privilege : privileges) {
                    /*
                     * If the user has this privilege it means that he/she is
                     * scope manager for all available scopes.
                     */
                    final String name = privilege.getName();
                    if ("ADD_ALL_SCOPE_MANAGERS".equals(name)) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("ADD_ALL_SCOPE_MANAGERS found for user: " + user.getName());
                        }
                        user = null;
                        break;
                    }
                }
                break;
            }
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Setup search criteria...");
        }

        /*
         * Setup search criteria.
         */
        final ScopeSearchCommand sc = new ScopeSearchCommand();
        sc.setAsc(false);
        sc.setOrderBy("name");
        sc.setUser(user);
        sc.setStart(0);
        sc.setMaxResults(maximumManagedScopes);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Searching for scopes...");
        }

        /*
         * Find the scopes.
         */
        final List<Scope> scopes = scopeDao.findScopesBySearchCommand(sc);

        if (LOG.isDebugEnabled()) {
            LOG.debug(scopes.size() + " scopes found...");
        }

        /*
         * Initialize the scopes.
         */
        for (final Scope scope : scopes) {
            initialize(scope);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Scopes initialized...");
        }

        return scopes;
    }

    @Override
    public List<Scope> findManagedScopesWithGrantableRolesByUser(final User user) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Searching for managed scopes with grantable roles by user: " + user);
        }

        /*
         * Retrieve managed scopes from the specified user.
         */
        final List<Scope> managedScopesByUser = findManagedScopesByUser(user);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Found " + managedScopesByUser.size() + " managed scope(s).");
        }

        /*
         * Loop over all managed scopes to filter out roles that the user is not
         * allowed to grant if applicable.
         */
        for (final Scope scope : managedScopesByUser) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Setup role search criteria...");
            }

            /*
             * Setup the search criteria.
             */
            final RoleSearchCommand sc = new RoleSearchCommand();
            sc.setScope(scope);
            sc.setUser(user);

            if (LOG.isDebugEnabled()) {
                LOG.debug("About to search for grantable roles...");
            }

            /*
             * Retrieve all roles that the user can grant in the specific
             * scopeName.
             */
            final List<Role> grantableRoles = roleManager.findRoles(sc);

            /*
             * Remove all roles that are not in the grantableRoles list. Only do
             * this when there are grantable roles because when there are none
             * this means that the scopeName manager is allowed to grant any
             * role to other users in the specific scopeName he manages.
             */
            if (!grantableRoles.isEmpty()) {
                final Set<Role> roles = scope.getRoles();
                // roles.retainAll(grantableRoles);
            }
        }

        return managedScopesByUser;
    }

    /**
     * Initializes the passed in scope to avoid lazy initialization exceptions.
     * 
     * @param scope
     *        The {@link Scope} object that will be initialized.
     */
    private void initialize(final Scope scope) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Initializing privileges...");
        }

        final Set<Privilege> privileges = scope.getPrivileges();
        if (privileges != null) {
            for (final Privilege privilege : privileges) {
                privilege.getId();
            }
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Privileges initialized...");
            LOG.debug("Initializing roles...");
        }

        final Set<Role> roles = scope.getRoles();
        if (roles != null) {
            for (final Role role : roles) {
                role.getId();
            }
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Roles initialized...");
        }

    }

    @Override
    public Scope findScopeById(final Long id) {
        final Scope scope = scopeDao.findById(id);

        initialize(scope);

        return scope;
    }

    @Override
    public int findNumberOfScopes(final ScopeSearchCommand sc) {
        return scopeDao.findNumberOfScopesBySearchCommand(sc);
    }

    @Override
    public Scope storeScope(final Scope scope, final Errors errors) throws ScopeManagerException {

        scopeValidator.validate(scope, errors);

        if (errors.hasErrors()) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Scope has validation errors: " + errors.getErrorCount());
            }

            throw new ScopeManagerException("Failed to store a Scope");
        }

        if (scope.isPersistent()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Retrieve scope...");
            }

            /*
             * Retrieve the original scopeName.
             */
            final Scope originalScope = scopeDao.findById(scope.getId());

            if (LOG.isDebugEnabled()) {
                LOG.debug("Scope retrieved..");
            }

            /*
             * Save the scopeName itself.
             */
            if (LOG.isDebugEnabled()) {
                LOG.debug("Updating scope: " + scope);
            }
            originalScope.copy(scope);

            final Scope savedScope = scopeDao.save(originalScope);

            if (LOG.isDebugEnabled()) {
                LOG.debug("Intializing scope...");
            }

            initialize(savedScope);

            if (LOG.isDebugEnabled()) {
                LOG.debug("Scope initialized...");
            }

            return savedScope;
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Storing new scope: " + scope);
            }

            final Scope savedScope = scopeDao.save(scope);

            if (LOG.isDebugEnabled()) {
                LOG.debug("Intializing scope...");
            }

            initialize(savedScope);

            if (LOG.isDebugEnabled()) {
                LOG.debug("Scope initialized...");
            }

            return savedScope;
        }

    }

    @Override
    @SuppressWarnings("unchecked")
    public Scope importScope(final Scope scope, final Errors errors) throws ScopeManagerException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Validating scope: " + scope);
        }

        /*
         * Validate scope.
         */
        scopeValidator.validate(scope, errors);

        if (errors.hasErrors()) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Scope has validation errors: " + errors.getErrorCount());
            }

            throw new ScopeManagerException("Failed to store a Scope");
        }

        /*
         * Check for duplicate scope.
         */
        final ScopeSearchCommand sc = new ScopeSearchCommand();
        sc.setName(scope.getName());

        final List<Scope> scopes = scopeDao.findScopesBySearchCommand(sc);
        if (scopes.size() > 0) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Duplicate scope import!");
            }

            throw new ScopeManagerException("Duplicate scope import!");
        }

        final Scope savedScope = scopeDao.save(scope);

        /*
         * Store privileges.
         */
        final Set<Privilege> privileges = scope.getPrivileges();
        final Set<Privilege> storedPrivileges = new HashSet<Privilege>();

        if (LOG.isDebugEnabled()) {
            LOG.debug("About to store privileges [" + privileges.size() + "]");
        }

        for (final Privilege privilege : privileges) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Storing privilege: " + privilege);
            }

            final HibernatePrivilege hibernatePrivilege = (HibernatePrivilege) privilege;
            hibernatePrivilege.setScope(savedScope);

            final Errors privilegeErrors = new BindException(privilege, privilege.getClass().getName());

            final Privilege storedPrivilege = privilegeManager.storePrivilege(privilege, privilegeErrors);
            storedPrivileges.add(storedPrivilege);

            if (LOG.isDebugEnabled()) {
                LOG.debug("Stored privilege...");
            }

            if (privilegeErrors.hasErrors()) {

                final List<ObjectError> allErrors = privilegeErrors.getAllErrors();

                for (final ObjectError object : allErrors) {
                    final String errorCode = object.getCode();
                    final String defaultMessage = object.getDefaultMessage();

                    errors.reject(errorCode, defaultMessage);
                }

                if (LOG.isWarnEnabled()) {
                    LOG.warn("Errors occured during storing of a privilege!");
                }

            }
        }

        /*
         * Store roles.
         */
        final Set<Role> roles = scope.getRoles();

        if (LOG.isDebugEnabled()) {
            LOG.debug("About to store roles [" + roles.size() + "]");
        }

        for (final Role role : roles) {
            final HibernateRole hibernateRole = (HibernateRole) role;
            hibernateRole.setScope(savedScope);

            final Set<Privilege> transientPrivileges = hibernateRole.getPrivileges();
            final Set<Privilege> storedRolePrivileges = new HashSet<Privilege>();

            if (LOG.isDebugEnabled()) {
                LOG.debug("Retrieve stored privileges..");
            }

            /*
             * Loop through the transient privileges and retrieve the stored
             * counterpart.
             */
            for (final Privilege transientPrivilege : transientPrivileges) {
                for (final Privilege privilege : storedPrivileges) {
                    if (privilege.getName().equals(transientPrivilege.getName())) {
                        storedRolePrivileges.add(privilege);
                        break;
                    }
                }
            }
            hibernateRole.setPrivileges(storedRolePrivileges);

            if (LOG.isDebugEnabled()) {
                LOG.debug("About to store role: " + role);
            }

            final Errors roleErrors = new BindException(role, role.getClass().getName());

            roleManager.storeRole(role, roleErrors);

            if (LOG.isDebugEnabled()) {
                LOG.debug("Role stored: " + role);
            }

            if (roleErrors.hasErrors()) {

                final List<ObjectError> allErrors = roleErrors.getAllErrors();

                for (final ObjectError object : allErrors) {
                    final String errorCode = object.getCode();
                    final String defaultMessage = object.getDefaultMessage();

                    errors.reject(errorCode, defaultMessage);
                }

                if (LOG.isWarnEnabled()) {
                    LOG.warn("Errors occured during storing of a role!");
                }

            }
        }

        return savedScope;
    }

    /**
     * @param scopeDao
     *        the scopeDao to set
     */
    @Required
    public void setScopeDao(final ScopeDao<Scope> scopeDao) {
        this.scopeDao = scopeDao;
    }

    /**
     * @param userDao
     *        the userDao to set
     */
    @Required
    public void setUserDao(final UserDao<User> userDao) {
        this.userDao = userDao;
    }

    /**
     * @param scopeValidator
     *        the scopeValidator to set
     */
    @Required
    public void setScopeValidator(final Validator scopeValidator) {
        this.scopeValidator = scopeValidator;
    }

    /**
     * @param roleManager
     *        the roleManager to set
     */
    @Required
    public void setRoleManager(final RoleManager roleManager) {
        this.roleManager = roleManager;
    }

    /**
     * @param scopeName
     *        the scopeName to set
     */
    @Required
    public void setScopeName(final String scopeName) {
        this.scopeName = scopeName;
    }

    /**
     * @param maximumManagedScopes
     *        the maximumManagedScopes to set
     */
    public void setMaximumManagedScopes(final Integer maximumManagedScopes) {
        this.maximumManagedScopes = maximumManagedScopes;
    }

    /**
     * @param privilegeManager
     *        the privilegeManager to set
     */
    @Required
    public void setPrivilegeManager(final PrivilegeManager privilegeManager) {
        this.privilegeManager = privilegeManager;
    }

}
