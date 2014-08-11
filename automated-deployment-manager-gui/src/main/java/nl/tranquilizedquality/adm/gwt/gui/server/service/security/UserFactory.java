/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: Aug 25, 2012 File: fUserFactory.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.server.service.security
 * 
 * Copyright (c) 2012 Tranquilized Quality www.Tranquilized Quality.nl All rights
 * reserved.
 * 
 * This software is the confidential and proprietary information of Tranquilized Quality
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Tranquilized Quality.
 * </pre>
 */
package nl.tranquilizedquality.adm.gwt.gui.server.service.security;

import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.domain.factory.AbstractFactory;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUser;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUser;

/**
 * Factory that transforms client beans into persistent beans and vice versa.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public class UserFactory extends AbstractFactory<ClientUser, HibernateUser, User> {

    @Override
    protected ClientUser createNewClientBean() {
        return new ClientUser();
    }

    @Override
    protected HibernateUser createNewPersistentBean() {
        return new HibernateUser();
    }

}
