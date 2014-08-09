/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 11 sep. 2011 File: EnvironmentFacotry.java
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

import nl.tranquilizedquality.adm.commons.business.domain.Environment;
import nl.tranquilizedquality.adm.commons.domain.factory.AbstractFactory;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateEnvironment;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientEnvironment;

/**
 * Factory that transforms client beans into persistent beans and vice versa.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 11 sep. 2011
 */
public class EnvironmentFacotry extends
		AbstractFactory<ClientEnvironment, HibernateEnvironment, Environment> {

	@Override
	protected ClientEnvironment createNewClientBean() {
		return new ClientEnvironment();
	}

	@Override
	protected HibernateEnvironment createNewPersistentBean() {
		return new HibernateEnvironment();
	}

}
