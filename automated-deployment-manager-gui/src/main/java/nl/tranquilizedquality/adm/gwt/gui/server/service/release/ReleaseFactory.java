/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 17 sep. 2011 File: ReleaseFactory.java
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

import nl.tranquilizedquality.adm.commons.business.domain.Release;
import nl.tranquilizedquality.adm.commons.domain.factory.AbstractFactory;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateRelease;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientRelease;

/**
 * Factory that transforms client beans into persistent beans and vice versa.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 17 sep. 2011
 */
public class ReleaseFactory extends AbstractFactory<ClientRelease, HibernateRelease, Release> {

	@Override
	protected ClientRelease createNewClientBean() {
		return new ClientRelease();
	}

	@Override
	protected HibernateRelease createNewPersistentBean() {
		return new HibernateRelease();
	}

}
