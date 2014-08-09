/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 17 sep. 2011 File: HibernateDestinationLocationDao.java
 * Package: nl.tranquilizedquality.adm.core.persistence.db.hibernate.dao
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
package nl.tranquilizedquality.adm.core.persistence.db.hibernate.dao;

import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameter;
import nl.tranquilizedquality.adm.commons.hibernate.dao.AbstractHibernateBaseDao;
import nl.tranquilizedquality.adm.core.persistence.dao.DeployerParameterDao;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDeployerParameter;

/**
 * DAO that manages {@link DeployerParameter} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 17 sep. 2011
 */
public class HibernateDeployerParameterDao extends
		AbstractHibernateBaseDao<HibernateDeployerParameter, Long> implements
		DeployerParameterDao<HibernateDeployerParameter> {

	@Override
	protected Class<HibernateDeployerParameter> getDomainClass() {
		return HibernateDeployerParameter.class;
	}

	@Override
	public HibernateDeployerParameter newDomainObject() {
		return new HibernateDeployerParameter();
	}

}
