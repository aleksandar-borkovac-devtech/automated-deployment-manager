/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 28 jul. 2011 File: ReleaseHistoryManagerTest.java
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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.DeployStatus;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifactSnapshot;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecution;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecutionLog;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseStepExecution;
import nl.tranquilizedquality.adm.core.business.manager.ReleaseHistoryManager;
import nl.tranquilizedquality.adm.core.business.manager.ReleaseSnapshotFactory;
import nl.tranquilizedquality.adm.core.business.manager.impl.ReleaseHistoryManagerImpl;
import nl.tranquilizedquality.adm.core.persistence.dao.ReleaseExecutionDao;
import nl.tranquilizedquality.adm.core.persistence.dao.ReleaseExecutionLogDao;
import nl.tranquilizedquality.adm.core.persistence.dao.ReleaseStepExecutionDao;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateEnvironment;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateMavenArtifact;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateMavenArtifactSnapshot;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateRelease;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateReleaseExecution;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateReleaseExecutionLog;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateReleaseStepExecution;

import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link ReleaseHistoryManager}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 jul. 2011
 */
public class ReleaseHistoryManagerTest {

    /** Manager that will be tested. */
    private ReleaseHistoryManagerImpl releaseHistoryManager;

    /** Mocked DAO */
    private ReleaseExecutionDao<ReleaseExecution> releaseExecutionDao;

    /** Mocked DAO */
    private ReleaseStepExecutionDao<ReleaseStepExecution> releaseStepExecutionDao;

    /** Mocked DAO */
    private ReleaseExecutionLogDao<ReleaseExecutionLog> releaseExecutionLogDao;

    /** Mocked Factory */
    private ReleaseSnapshotFactory factory;

    private HibernateRelease release;

    private HibernateReleaseExecution releaseExecution;

    private HibernateReleaseExecution execution;

    /**
     * @throws java.lang.Exception
     */
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        releaseHistoryManager = new ReleaseHistoryManagerImpl();

        releaseExecutionDao = createMock(ReleaseExecutionDao.class);
        releaseStepExecutionDao = createMock(ReleaseStepExecutionDao.class);
        releaseExecutionLogDao = createMock(ReleaseExecutionLogDao.class);
        factory = createMock(ReleaseSnapshotFactory.class);

        releaseHistoryManager.setReleaseExecutionDao(releaseExecutionDao);
        releaseHistoryManager.setReleaseStepExecutionDao(releaseStepExecutionDao);
        releaseHistoryManager.setSnapshotFactory(factory);
        releaseHistoryManager.setReleaseExecutionLogDao(releaseExecutionLogDao);

        execution = new HibernateReleaseExecution();

        release = new HibernateRelease();
        release.setName("Sprint 1");
        release.setReleaseDate(new Date());

        releaseExecution = new HibernateReleaseExecution();
        releaseExecution.setRelease(release);
        releaseExecution.setReleaseDate(new Date());
        releaseExecution.setReleaseStatus(DeployStatus.SUCCESS);
    }

    /**
     * Verifies all the mocks expected behavior.
     */
    private void verifyMocks() {
        verify(releaseExecutionDao);
        verify(factory);
        verify(releaseStepExecutionDao);
        verify(releaseExecutionLogDao);
    }

    /**
     * Replays the mocks expected behavior.
     */
    private void replayMocks() {
        replay(releaseExecutionDao);
        replay(factory);
        replay(releaseStepExecutionDao);
        replay(releaseExecutionLogDao);
    }

    @Test
    public void testCreateHistory() {

        final List<MavenArtifact> artifacts = new ArrayList<MavenArtifact>();
        final HibernateMavenArtifact artifact = new HibernateMavenArtifact();
        artifacts.add(artifact);
        final HibernateEnvironment environment = new HibernateEnvironment();

        expect(releaseExecutionDao.newDomainObject()).andReturn(execution);
        expect(factory.createSnapshot(execution, artifacts)).andReturn(new ArrayList<MavenArtifactSnapshot>());
        expect(releaseExecutionDao.save(isA(ReleaseExecution.class))).andReturn(new HibernateReleaseExecution());

        replayMocks();

        final ReleaseExecution releaseExecution = releaseHistoryManager.createHistory(release, environment, artifacts);

        verifyMocks();

        assertNotNull("No release execution!", releaseExecution);
    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.manager.impl.ReleaseHistoryManagerImpl#registerActivity(nl.tranquilizedquality.adm.commons.business.domain.Release, java.lang.String)}
     * .
     */
    @Test
    public void testRegisterActivityRelease() {

        expect(releaseStepExecutionDao.newDomainObject()).andReturn(new HibernateReleaseStepExecution());
        expect(releaseStepExecutionDao.save(isA(ReleaseStepExecution.class))).andReturn(new HibernateReleaseStepExecution());

        replayMocks();

        releaseHistoryManager.registerActivity(execution, "OK");

        verifyMocks();
    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.manager.impl.ReleaseHistoryManagerImpl#registerActivity(nl.tranquilizedquality.adm.commons.business.domain.Release, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testRegisterActivityReleaseError() {
        expect(releaseStepExecutionDao.newDomainObject()).andReturn(new HibernateReleaseStepExecution());
        expect(releaseStepExecutionDao.save(isA(ReleaseStepExecution.class))).andReturn(new HibernateReleaseStepExecution());

        replayMocks();

        releaseHistoryManager.registerActivity(execution, "Do it!", "Errorrrrr!");

        verifyMocks();
    }

    @Test
    public void testRegisterLogs() {

        expect(releaseExecutionLogDao.save(isA(ReleaseExecutionLog.class))).andReturn(new HibernateReleaseExecutionLog());

        replayMocks();

        releaseHistoryManager.registerLogs(execution, new HibernateMavenArtifactSnapshot(), "Errorrrrr!");

        verifyMocks();
    }

}
