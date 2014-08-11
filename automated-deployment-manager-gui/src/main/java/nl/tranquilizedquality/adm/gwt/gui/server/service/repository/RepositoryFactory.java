/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 30 aug. 2011 File: RepositoryFactory.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.server.service.repository
 * 
 * Copyright (c) 2011 Tranquilized Quality www.Tranquilized Quality.nl All rights
 * reserved.
 * 
 * This software is the confidential and proprietary information of Tranquilized Quality
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Tranquilized Quality.
 * </pre>
 */
package nl.tranquilizedquality.adm.gwt.gui.server.service.repository;

import nl.tranquilizedquality.adm.commons.business.domain.Repository;
import nl.tranquilizedquality.adm.commons.domain.factory.AbstractFactory;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateRepository;
import nl.tranquilizedquality.adm.gwt.gui.client.model.repository.ClientRepository;

/**
 * Factory that transforms client beans into persistent beans and vice versa.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 30 aug. 2011
 */
public class RepositoryFactory extends
        AbstractFactory<ClientRepository, HibernateRepository, Repository> {

    @Override
    protected ClientRepository createNewClientBean() {
        return new ClientRepository();
    }

    @Override
    protected HibernateRepository createNewPersistentBean() {
        return new HibernateRepository();
    }

}
