/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 3 jun. 2011 File: HibernateReleaseStepExecutionDao.java
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

import nl.tranquilizedquality.adm.commons.hibernate.dao.AbstractHibernateBaseDao;
import nl.tranquilizedquality.adm.core.persistence.dao.ReleaseStepExecutionDao;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateReleaseStepExecution;

/**
 * Hibernate DAO that manages {@link HibernateReleaseStepExecution} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 */
public class HibernateReleaseStepExecutionDao extends
		AbstractHibernateBaseDao<HibernateReleaseStepExecution, Long> implements
		ReleaseStepExecutionDao<HibernateReleaseStepExecution> {

	@Override
	protected Class<HibernateReleaseStepExecution> getDomainClass() {
		return HibernateReleaseStepExecution.class;
	}

	@Override
	public HibernateReleaseStepExecution newDomainObject() {
		return new HibernateReleaseStepExecution();
	}

}
