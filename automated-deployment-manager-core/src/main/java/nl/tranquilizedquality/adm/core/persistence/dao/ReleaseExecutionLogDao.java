/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: Aug 3, 2012 File: fReleaseExecutionLogDao.java
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
package nl.tranquilizedquality.adm.core.persistence.dao;

import nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecutionLog;
import nl.tranquilizedquality.adm.commons.hibernate.dao.BaseDao;

/**
 * DAO that manages {@link ReleaseExecutionLog} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 3, 2012
 * 
 * @param <T>
 *            The implementation type.
 */
public interface ReleaseExecutionLogDao<T extends ReleaseExecutionLog> extends BaseDao<T, Long> {

}
