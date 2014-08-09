/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 17 sep. 2011 File: MavenModuleFactory.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.server.service.release
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
package nl.tranquilizedquality.adm.gwt.gui.server.service.release;

import nl.tranquilizedquality.adm.commons.business.domain.MavenModule;
import nl.tranquilizedquality.adm.commons.domain.factory.AbstractFactory;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateMavenModule;
import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenModule;

/**
 * Factory that transforms client beans into persistent beans and vice versa.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 17 sep. 2011
 */
public class MavenModuleFactory extends
		AbstractFactory<ClientMavenModule, HibernateMavenModule, MavenModule> {

	@Override
	protected ClientMavenModule createNewClientBean() {
		return new ClientMavenModule();
	}

	@Override
	protected HibernateMavenModule createNewPersistentBean() {
		return new HibernateMavenModule();
	}

}
