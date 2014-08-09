/*
 * @(#)DeployerFactoryImpl.java 12 dec. 2012
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.gwt.gui.server.service.deployment;

import nl.tranquilizedquality.adm.commons.business.domain.Deployer;
import nl.tranquilizedquality.adm.commons.domain.factory.AbstractFactory;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDeployer;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientDeployer;

/**
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 12 dec. 2012
 */
public class DeployerFactory extends AbstractFactory<ClientDeployer, HibernateDeployer, Deployer> {

    @Override
    protected ClientDeployer createNewClientBean() {
        return new ClientDeployer();
    }

    @Override
    protected HibernateDeployer createNewPersistentBean() {
        return new HibernateDeployer();
    }

}
