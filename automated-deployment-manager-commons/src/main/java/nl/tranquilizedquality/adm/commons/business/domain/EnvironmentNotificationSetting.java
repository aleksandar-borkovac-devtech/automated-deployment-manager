package nl.tranquilizedquality.adm.commons.business.domain;

import nl.tranquilizedquality.adm.commons.domain.UpdatableDomainObject;

/**
 * Representation of the notification settings of a user.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 23 okt. 2012
 */
public interface EnvironmentNotificationSetting extends UpdatableDomainObject<Long> {

    /**
     * The user where these settings are for.
     * 
     * @return Returns a {@link User} object.
     */
    User getUser();

    /**
     * Retrieves the environment these settings are for.
     * 
     * @return Returns an {@link Environment} object.
     */
    Environment getEnvironment();

    /**
     * Determines if an email notification should be sent.
     * 
     * @return Returns true if an email notification should be sent otherwise no
     *         email should be sent.
     */
    boolean isEmailNotification();

    /**
     * Determines if a SMS notification should be sent.
     * 
     * @return Returns true if a SMS notification should be sent otherwise no
     *         SMS notification should be sent.
     */
    boolean isSmsNotification();

}
