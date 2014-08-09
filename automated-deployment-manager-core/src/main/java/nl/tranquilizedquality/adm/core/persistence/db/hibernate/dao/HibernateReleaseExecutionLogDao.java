/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: Aug 3, 2012 File: fHibernateReleaseExecutionLogDao.java
 * Package: nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean
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
package nl.tranquilizedquality.adm.core.persistence.db.hibernate.dao;

import nl.tranquilizedquality.adm.commons.hibernate.dao.AbstractHibernateBaseDao;
import nl.tranquilizedquality.adm.core.persistence.dao.ReleaseExecutionLogDao;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateReleaseExecutionLog;

/**
 * Hibernate DAO implementation that manages
 * {@link HibernateReleaseExecutionLog} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 3, 2012
 * 
 */
public class HibernateReleaseExecutionLogDao extends AbstractHibernateBaseDao<HibernateReleaseExecutionLog, Long> implements ReleaseExecutionLogDao<HibernateReleaseExecutionLog> {

	@Override
	protected Class<HibernateReleaseExecutionLog> getDomainClass() {
		return HibernateReleaseExecutionLog.class;
	}

	@Override
	public HibernateReleaseExecutionLog newDomainObject() {
		return new HibernateReleaseExecutionLog();
	}

}
