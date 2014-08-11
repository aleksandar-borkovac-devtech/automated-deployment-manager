package nl.tranquilizedquality.adm.commons.business.domain;

import java.util.Date;

import nl.tranquilizedquality.adm.commons.domain.UpdatableDomainObject;

/**
 * Represents a role that can be assigned to a {@link UserRole}.
 * 
 * @author Salomo Petrus
 * @since 24 nov. 2011
 * 
 */
public interface UserRole extends UpdatableDomainObject<Long> {

    /**
     * @return the active
     */
    Boolean isActive();

    /**
     * @param active
     *            the active to set
     */
    void setActive(Boolean active);

    /**
     * @return the active from date
     */
    Date getActiveFrom();

    /**
     * @param activeFrom
     *            the active from date to set
     */
    void setActiveFrom(Date activeFrom);

    /**
     * @return the active until date
     */
    Date getActiveUntil();

    /**
     * @param activeUntil
     *            the active until date to set
     */
    void setActiveUntil(Date activeUntil);

    /**
     * @return the user
     */
    User getUser();

    /**
     * @param user
     *            the user to set
     */
    void setUser(User user);

    /**
     * @return the role
     */
    Role getRole();

    /**
     * @param role
     *            the role to set
     */
    void setRole(Role role);

}
