package nl.tranquilizedquality.adm.core.persistence.dao;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.EnvironmentNotificationSetting;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.hibernate.dao.BaseDao;

/**
 * DAO that manages {@link EnvironmentNotificationSetting} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 23 okt. 2012
 * @param <T>
 *        The implementation type.
 */
public interface EnvironmentNotificationSettingDao<T extends EnvironmentNotificationSetting> extends BaseDao<T, Long> {

    /**
     * Retrieves all settings for a user.
     * 
     * @param user
     *        The user where the settings should be retrieved for.
     * @return Returns a {@link List} containing {@link EnvironmentNotificationSetting} objects.
     */
    List<EnvironmentNotificationSetting> findSettingsForUser(User user);

}
