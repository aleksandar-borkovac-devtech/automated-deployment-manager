/*
 * @(#)HibernateDeployerDao.java 12 dec. 2012
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.core.persistence.db.hibernate.dao;

import nl.tranquilizedquality.adm.commons.hibernate.dao.AbstractHibernateBaseDao;
import nl.tranquilizedquality.adm.core.persistence.dao.DeployerDao;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDeployer;

/**
 * Hibernate DAO that manages {@link HibernateDeployer} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 12 dec. 2012
 */
public class HibernateDeployerDao extends AbstractHibernateBaseDao<HibernateDeployer, Long> implements
        DeployerDao<HibernateDeployer> {

    @Override
    protected Class<HibernateDeployer> getDomainClass() {
        return HibernateDeployer.class;
    }

    @Override
    public HibernateDeployer newDomainObject() {
        return new HibernateDeployer();
    }

}
