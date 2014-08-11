package nl.tranquilizedquality.adm.security.business.manager.impl;

import java.util.List;
import java.util.Set;

import nl.tranquilizedquality.adm.commons.business.command.PrivilegeSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.Privilege;
import nl.tranquilizedquality.adm.commons.business.domain.Role;
import nl.tranquilizedquality.adm.commons.business.domain.Scope;
import nl.tranquilizedquality.adm.security.business.exception.PrivilegeManagerException;
import nl.tranquilizedquality.adm.security.business.manager.PrivilegeManager;
import nl.tranquilizedquality.adm.security.persistence.db.dao.PrivilegeDao;
import nl.tranquilizedquality.adm.security.persistence.db.dao.ScopeDao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * {@link PrivilegeManager} implementation that manages {@link Privilege}
 * objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public class PrivilegeManagerImpl implements PrivilegeManager {

    /** Logger for this class. */
    private static final Log LOG = LogFactory.getLog(PrivilegeManagerImpl.class);

    /** DAO used to manage {@link Privilege} objects. */
    private PrivilegeDao<Privilege> privilegeDao;

    /** DAO used to manage {@link Scope} objects. */
    private ScopeDao<Scope> scopeDao;

    /** {@link Validator} used to validate a privilege. */
    private Validator privilegeValidator;

    public Privilege storePrivilege(final Privilege privilege, final Errors errors) throws PrivilegeManagerException {

        privilegeValidator.validate(privilege, errors);

        if (errors.hasErrors()) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Invalid privilege: " + errors.getErrorCount());
            }
            throw new PrivilegeManagerException("Invalid privilege.");
        }

        /*
         * Check if we are editing or creating a privilege.
         */
        if (privilege.isPersistent()) {
            final Privilege originalPrivilege = privilegeDao.findById(privilege.getId());
            originalPrivilege.copy(privilege);

            final Privilege savedPrivilege = privilegeDao.save(originalPrivilege);

            initialize(savedPrivilege);

            return savedPrivilege;
        }
        else {
            final Privilege savedPrivilege = privilegeDao.save(privilege);

            return savedPrivilege;
        }
    }

    private void initialize(final Privilege privilege) {

        final Set<Role> roles = privilege.getRoles();
        if (roles != null) {
            for (final Role role : roles) {
                role.getId();
            }
        }
    }

    public List<Privilege> findPrivileges(final PrivilegeSearchCommand sc) {
        final Scope scope = sc.getScope();
        if (scope != null) {
            final Scope scopeById = scopeDao.findById(scope.getId());
            sc.setScope(scopeById);
        }

        final List<Privilege> privileges = privilegeDao.findPrivileges(sc);

        for (final Privilege privilege : privileges) {
            initializePrivilege(privilege);
        }

        return privileges;
    }

    public int findNumberOfPrivileges(final PrivilegeSearchCommand sc) {
        return privilegeDao.findNumberOfPrivileges(sc);
    }

    @Override
    public Privilege findPrivilegeById(final Long id) {
        final Privilege privilege = privilegeDao.findById(id);

        initializePrivilege(privilege);

        return privilege;
    }

    /**
     * @param privilege
     */
    private void initializePrivilege(final Privilege privilege) {
        /*
         * Initialize collection.
         */
        final Set<Role> roles = privilege.getRoles();
        if (roles != null) {
            for (final Role role : roles) {
                role.getId();
            }
        }
    }

    /**
     * @param privilegeDao
     *            the privilegeDao to set
     */
    @Required
    public void setPrivilegeDao(final PrivilegeDao<Privilege> privilegeDao) {
        this.privilegeDao = privilegeDao;
    }

    /**
     * @param scopeDao
     *            the scopeDao to set
     */
    @Required
    public void setScopeDao(final ScopeDao<Scope> scopeDao) {
        this.scopeDao = scopeDao;
    }

    /**
     * @param privilegeValidator
     *            the privilegeValidator to set
     */
    @Required
    public void setPrivilegeValidator(final Validator privilegeValidator) {
        this.privilegeValidator = privilegeValidator;
    }

}
