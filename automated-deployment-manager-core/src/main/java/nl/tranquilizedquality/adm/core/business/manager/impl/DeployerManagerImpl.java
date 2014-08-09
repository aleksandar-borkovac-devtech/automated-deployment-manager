/*
 * @(#)DeployerManagerImpl.java 12 dec. 2012
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.core.business.manager.impl;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.Deployer;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameterTemplate;
import nl.tranquilizedquality.adm.core.business.manager.DeployerManager;
import nl.tranquilizedquality.adm.core.persistence.dao.DeployerDao;

import org.springframework.beans.factory.annotation.Required;

/**
 * Manager that manages the available deployers.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 12 dec. 2012
 */
public class DeployerManagerImpl implements DeployerManager {

    /** DAO used to manage deployers. */
    private DeployerDao<Deployer> deployerDao;

    @Override
    public List<Deployer> findAvailableDeployers() {
        final List<Deployer> deployers = deployerDao.findAll();
        for (final Deployer deployer : deployers) {
            final List<DeployerParameterTemplate> parameters = deployer.getParameters();
            for (final DeployerParameterTemplate deployerParameterTemplate : parameters) {
                deployerParameterTemplate.getId();
            }
        }
        return deployers;
    }

    @Required
    public void setDeployerDao(final DeployerDao<Deployer> deployerDao) {
        this.deployerDao = deployerDao;
    }

}
