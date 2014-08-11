package nl.tranquilizedquality.adm.commons.business.domain;

import java.util.Set;

import nl.tranquilizedquality.adm.commons.domain.UpdatableDomainObject;

/**
 * Represents a privilege a {@link User} can have through its {@link Role}s.
 * 
 * @author Salomo Petrus
 * @since 17 mei 2010
 */
public interface Privilege extends UpdatableDomainObject<Long> {

    /**
     * @return the name
     */
    String getName();

    /**
     * @return the description
     */
    String getDescription();

    /**
     * @return the valid
     */
    Boolean isValid();

    /**
     * @param valid
     *            the valid to set
     */
    void setValid(Boolean valid);

    /**
     * @return the scope
     */
    Scope getScope();

    /**
     * @return the roles
     */
    Set<Role> getRoles();

    /**
     * @param roles
     *            the roles to set
     */
    void setRoles(Set<Role> roles);

}
