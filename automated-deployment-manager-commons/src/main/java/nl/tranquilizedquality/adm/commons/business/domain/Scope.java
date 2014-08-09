package nl.tranquilizedquality.adm.commons.business.domain;

import java.util.Set;

import nl.tranquilizedquality.adm.commons.domain.InsertableDomainObject;

/**
 * Represents a scope of {@link Role}s and {@link Privilege}s.
 * 
 * @author Salomo Petrus
 * @since 24 nov. 2011
 */
public interface Scope extends InsertableDomainObject<Long> {

    /**
     * Retrieves the name of the scope.
     * 
     * @return the name
     */
    String getName();

    /**
     * @return the description
     */
    String getDescription();

    /**
     * @return the privileges
     */
    Set<Privilege> getPrivileges();

    /**
     * @return the roles
     */
    Set<Role> getRoles();

}
