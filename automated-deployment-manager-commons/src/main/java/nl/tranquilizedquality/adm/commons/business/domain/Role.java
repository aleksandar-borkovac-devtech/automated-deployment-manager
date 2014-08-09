package nl.tranquilizedquality.adm.commons.business.domain;

import java.util.List;
import java.util.Set;

import nl.tranquilizedquality.adm.commons.domain.UpdatableDomainObject;

/**
 * Represents a role that can be assigned to a {@link User}.
 * 
 * @author Salomo Petrus
 * @since 24 nov. 2011
 */
public interface Role extends UpdatableDomainObject<Long> {

    /**
     * @return the name
     */
    String getName();

    /**
     * @param name
     *        the name to set
     */
    void setName(String name);

    /**
     * @return the description
     */
    String getDescription();

    /**
     * @param description
     *        the description to set
     */
    void setDescription(String description);

    /**
     * @return the valid
     */
    Boolean isValid();

    /**
     * @param valid
     *        the valid to set
     */
    void setValid(Boolean valid);

    /**
     * @return the scope
     */
    Scope getScope();

    /**
     * @param scope
     *        the scope to set
     */
    void setScope(Scope scope);

    /**
     * @return the users
     */
    List<User> getUsers();

    /**
     * @return the user roles
     */
    List<UserRole> getUserRoles();

    /**
     * @param userRoles
     *        the user roles to set
     */
    void setUserRoles(List<UserRole> userRoles);

    /**
     * @return the privileges
     */
    Set<Privilege> getPrivileges();

    /**
     * @param privileges
     *        the privileges to set
     */
    void setPrivileges(Set<Privilege> privileges);

    /**
     * @return the frozen
     */
    Boolean isFrozen();

    /**
     * @param frozen
     *        the frozen to set
     */
    void setFrozen(Boolean frozen);

}
