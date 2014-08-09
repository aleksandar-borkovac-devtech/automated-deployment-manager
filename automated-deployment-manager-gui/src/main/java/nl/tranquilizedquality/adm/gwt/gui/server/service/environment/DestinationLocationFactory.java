/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 17 sep. 2011 File: DestinationLocationFactory.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.server.service.environment
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
package nl.tranquilizedquality.adm.gwt.gui.server.service.environment;

import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameter;
import nl.tranquilizedquality.adm.commons.domain.factory.AbstractFactory;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDeployerParameter;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientDeployerParameter;

/**
 * Factory that transforms client beans into persistent beans and vice versa.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 17 sep. 2011
 */
public class DestinationLocationFactory
		extends
		AbstractFactory<ClientDeployerParameter, HibernateDeployerParameter, DeployerParameter> {

	@Override
	protected ClientDeployerParameter createNewClientBean() {
		return new ClientDeployerParameter();
	}

	@Override
	protected HibernateDeployerParameter createNewPersistentBean() {
		return new HibernateDeployerParameter();
	}

}
