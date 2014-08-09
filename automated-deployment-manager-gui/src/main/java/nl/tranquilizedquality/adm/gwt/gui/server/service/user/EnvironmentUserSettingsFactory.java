/*
 * @(#)EnvironmentUserSettingsFactory.java 23 okt. 2012
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.gwt.gui.server.service.user;

import nl.tranquilizedquality.adm.commons.business.domain.EnvironmentNotificationSetting;
import nl.tranquilizedquality.adm.commons.domain.factory.AbstractFactory;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateEnvironmentNotificationSetting;
import nl.tranquilizedquality.adm.gwt.gui.client.model.settings.ClientEnvironmentNotificationSetting;

/**
 * Factory that transforms client side beans into persistent beans.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 23 okt. 2012
 */
class EnvironmentUserSettingsFactory extends
        AbstractFactory<ClientEnvironmentNotificationSetting, HibernateEnvironmentNotificationSetting, EnvironmentNotificationSetting> {

    @Override
    protected ClientEnvironmentNotificationSetting createNewClientBean() {
        return new ClientEnvironmentNotificationSetting();
    }

    @Override
    protected HibernateEnvironmentNotificationSetting createNewPersistentBean() {
        return new HibernateEnvironmentNotificationSetting();
    }

}
