/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 28 jul. 2011 File: ReleaseHistoryManagerImpl.java
 * Package: nl.tranquilizedquality.adm.core.business.manager.impl
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
package nl.tranquilizedquality.adm.core.business.manager.impl;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.ReleaseExecutionSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.DeployStatus;
import nl.tranquilizedquality.adm.commons.business.domain.Environment;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifactSnapshot;
import nl.tranquilizedquality.adm.commons.business.domain.Release;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecution;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecutionLog;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseStepExecution;
import nl.tranquilizedquality.adm.core.business.manager.ReleaseHistoryManager;
import nl.tranquilizedquality.adm.core.business.manager.ReleaseSnapshotFactory;
import nl.tranquilizedquality.adm.core.business.manager.exception.InvalidReleaseException;
import nl.tranquilizedquality.adm.core.persistence.dao.ReleaseExecutionDao;
import nl.tranquilizedquality.adm.core.persistence.dao.ReleaseExecutionLogDao;
import nl.tranquilizedquality.adm.core.persistence.dao.ReleaseStepExecutionDao;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Manager that manages the release history.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 jul. 2011
 */
public class ReleaseHistoryManagerImpl implements ReleaseHistoryManager {

    /** Logger for this class. */
    private static final Log LOGGER = LogFactory.getLog(ReleaseHistoryManagerImpl.class);

    /** DAO that manages the release history. */
    private ReleaseExecutionDao<ReleaseExecution> releaseExecutionDao;

    /** DAO that registers steps in a release. */
    private ReleaseStepExecutionDao<ReleaseStepExecution> releaseStepExecutionDao;

    /** DAO that registers the logs of a release execution. */
    private ReleaseExecutionLogDao<ReleaseExecutionLog> releaseExecutionLogDao;

    /** Factory used to create release snapshots. */
    private ReleaseSnapshotFactory snapshotFactory;

    @Override
    public void registerActivity(final ReleaseExecution execution, final String description) {
        registerActivity(execution, description, "");
    }

    // make sure a new transaction will be started on this method.
    @Override
    public void registerActivity(final ReleaseExecution execution, final String description, final String errorMessage) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Checking if release has history...");
        }

        /*
         * Configure step.
         */
        final ReleaseStepExecution stepExecution = releaseStepExecutionDao.newDomainObject();
        stepExecution.setName(description);
        stepExecution.setReleaseExecution(execution);

        if (!StringUtils.isEmpty(errorMessage)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Registering error message...");
            }

            stepExecution.setErrorMessage(errorMessage);
        }

        releaseStepExecutionDao.save(stepExecution);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Activity registered...");
        }
    }

    @Override
    public void registerActivity(final ReleaseExecution execution, final String description, final DeployStatus deployStatus) {
        registerActivity(execution, description);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Update status to " + deployStatus);
        }

        execution.setReleaseStatus(deployStatus);
        releaseExecutionDao.save(execution);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Status updated...");
        }
    }

    @Override
    public ReleaseExecution createHistory(final Release release, final Environment environment, final List<MavenArtifact> artifacts) {

        /*
         * Create the release execution.
         */
        final ReleaseExecution execution = releaseExecutionDao.newDomainObject();
        execution.setRelease(release);
        execution.setReleaseStatus(DeployStatus.ONGOING);
        execution.setEnvironment(environment);

        /*
         * Create the snapshot of the artifacts.
         */
        final List<MavenArtifactSnapshot> snapshotArtifacts = snapshotFactory.createSnapshot(execution, artifacts);
        execution.setArtifacts(snapshotArtifacts);

        /*
         * Save the execution.
         */
        releaseExecutionDao.save(execution);

        return execution;
    }

    @Override
    public int findNumberOfReleaseExecutions(final ReleaseExecutionSearchCommand sc) {
        return releaseExecutionDao.findNumberOfReleaseExecutionsBySearchCommand(sc);
    }

    @Override
    public List<ReleaseExecution> findReleaseHistory(final ReleaseExecutionSearchCommand sc) {

        final Release release = sc.getRelease();
        if (release == null || !release.isPersistent()) {
            final String msg = "Can't retrieve history from a release that is not persistent!";
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }

            throw new InvalidReleaseException(msg);
        }

        /*
         * Retrieve the release history.
         */
        final List<ReleaseExecution> releaseExecutions = releaseExecutionDao.findBySearchCommand(sc);

        /*
         * Initialize the release history.
         */
        for (final ReleaseExecution releaseExecution : releaseExecutions) {
            final Release actualRelease = releaseExecution.getRelease();
            actualRelease.setArtifacts(new ArrayList<MavenArtifact>());

            final List<ReleaseStepExecution> stepExecutions = releaseExecution.getStepExecutions();
            for (final ReleaseStepExecution releaseStepExecution : stepExecutions) {
                releaseStepExecution.getId();
            }

            final List<MavenArtifactSnapshot> artifacts = releaseExecution.getArtifacts();
            for (final MavenArtifactSnapshot mavenArtifactSnapshot : artifacts) {
                mavenArtifactSnapshot.getId();
            }

            final List<ReleaseExecutionLog> logs = releaseExecution.getLogs();
            for (final ReleaseExecutionLog releaseExecutionLog : logs) {
                releaseExecutionLog.getId();

                final ReleaseExecution execution = releaseExecutionLog.getReleaseExecution();
                execution.setLogs(new ArrayList<ReleaseExecutionLog>());
                final List<MavenArtifactSnapshot> snapshotArtifacts = execution.getArtifacts();
                for (final MavenArtifactSnapshot mavenArtifactSnapshot : snapshotArtifacts) {
                    mavenArtifactSnapshot.getId();
                }
            }
        }

        return releaseExecutions;
    }

    @Override
    public ReleaseExecution findReleaseHistoryById(final Long id) {
        final ReleaseExecution releaseExecution = releaseExecutionDao.findById(id);

        final List<MavenArtifactSnapshot> artifacts = releaseExecution.getArtifacts();
        for (final MavenArtifactSnapshot mavenArtifactSnapshot : artifacts) {
            mavenArtifactSnapshot.getId();
        }

        final List<ReleaseStepExecution> executions = releaseExecution.getStepExecutions();
        for (final ReleaseStepExecution releaseStepExecution : executions) {
            releaseStepExecution.getId();
        }

        final List<ReleaseExecutionLog> logs = releaseExecution.getLogs();
        for (final ReleaseExecutionLog releaseExecutionLog : logs) {
            releaseExecutionLog.getId();

            final ReleaseExecution execution = releaseExecutionLog.getReleaseExecution();
            final List<MavenArtifactSnapshot> snapshots = execution.getArtifacts();
            for (final MavenArtifactSnapshot mavenArtifactSnapshot : snapshots) {
                mavenArtifactSnapshot.getId();
                final ReleaseExecution snapshotExecution = mavenArtifactSnapshot.getReleaseExecution();
                final Release release = snapshotExecution.getRelease();
                release.setArtifacts(new ArrayList<MavenArtifact>());
            }
        }

        final Release release = releaseExecution.getRelease();
        release.setArtifacts(new ArrayList<MavenArtifact>());

        return releaseExecution;
    }

    @Override
    public void registerLogs(final ReleaseExecution execution, final MavenArtifactSnapshot artifact, final String logs) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Storing logs of execution..");
            LOGGER.debug(logs);
        }

        /*
         * Add the logs.
         */
        final ReleaseExecutionLog log = ReleaseExecutionLogFactory.createReleaseExecutionLog(execution, artifact, logs);
        releaseExecutionLogDao.save(log);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Logs stored..");
        }
    }

    @Override
    public ReleaseExecutionLog findReleaseExecutionLogById(final Long id) {
        final ReleaseExecutionLog executionLog = releaseExecutionLogDao.findById(id);

        final ReleaseExecution releaseExecution = executionLog.getReleaseExecution();
        releaseExecution.setArtifacts(new ArrayList<MavenArtifactSnapshot>());
        releaseExecution.setLogs(new ArrayList<ReleaseExecutionLog>());

        final Release release = releaseExecution.getRelease();
        release.setArtifacts(new ArrayList<MavenArtifact>());

        return executionLog;
    }

    /**
     * @param releaseStepExecutionDao
     *        the releaseStepExecutionDao to set
     */
    @Required
    public void setReleaseStepExecutionDao(final ReleaseStepExecutionDao<ReleaseStepExecution> releaseStepExecutionDao) {
        this.releaseStepExecutionDao = releaseStepExecutionDao;
    }

    /**
     * @param snapshotFactory
     *        the snapshotFactory to set
     */
    @Required
    public void setSnapshotFactory(final ReleaseSnapshotFactory snapshotFactory) {
        this.snapshotFactory = snapshotFactory;
    }

    @Required
    public void setReleaseExecutionDao(final ReleaseExecutionDao<ReleaseExecution> releaseExecutionDao) {
        this.releaseExecutionDao = releaseExecutionDao;
    }

    @Required
    public void setReleaseExecutionLogDao(final ReleaseExecutionLogDao<ReleaseExecutionLog> releaseExecutionLogDao) {
        this.releaseExecutionLogDao = releaseExecutionLogDao;
    }

}
