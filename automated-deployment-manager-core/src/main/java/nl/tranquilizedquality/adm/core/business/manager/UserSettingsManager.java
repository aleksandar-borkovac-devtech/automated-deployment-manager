package nl.tranquilizedquality.adm.core.business.manager;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.EnvironmentNotificationSetting;
import nl.tranquilizedquality.adm.commons.business.domain.User;

/**
 * Manager that manages the personal settings for a user.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 30 okt. 2012
 */
public interface UserSettingsManager {

    /**
     * Stores all the settings for a user.
     * 
     * @param settings
     *        The list of settings that will be stored.
     */
    void storeSettings(List<EnvironmentNotificationSetting> settings);

    /**
     * Searches for all the settings for a user.
     * 
     * @param user
     *        The user where the settings will be retrieved for.
     * @return Returns a {@link List} containing the settings of a user.
     */
    List<EnvironmentNotificationSetting> findSettingsForUser(User user);

    /**
     * Creates the default settings for a user.
     * 
     * @param user
     *        The user where the settings will be created for.
     * @return Returns a {@link List} containing the default settings of the user.
     */
    List<EnvironmentNotificationSetting> createDefaultNotificationSettingsForUser(User user);

}
