/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 27 jul. 2011 File: HibernateRepositoryDaoTest.java
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.RepositorySearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.Repository;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateRepository;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.dao.HibernateRepositoryDao;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test for {@link HibernateRepositoryDao}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 27 jul. 2011
 */
public class HibernateRepositoryDaoTest extends AbstractDaoTest {

	/** DAO that will be tested. */
	@Autowired
	private HibernateRepositoryDao repositoryDao;

	@Before
	public void setUp() {
		final HibernateRepository repo = new HibernateRepository();
		repo.setName("Nexus");
		repo.setEnabled(true);
		repo.setRepositoryUrl("http://cybertron/nexus/content/groups/public/");
		repo.setCreated(new Date());
		repo.setCreatedBy("s-petrus");

		repositoryDao.save(repo);
		repositoryDao.flush();
	}

	@Test
	public void testFindAll() {
		final List<HibernateRepository> repos = repositoryDao.findAll();

		assertNotNull("No repositories!", repos);
		assertEquals("Invalid number of repositories!", 1, repos.size());
	}

	@Test
	public void testFindBySearchCommand() {
		final RepositorySearchCommand sc = new RepositorySearchCommand();
		sc.setEnabled(true);

		List<Repository> repos = repositoryDao.findBySearchCommand(sc);

		assertEquals("Invalid number of repositories!", 1, repos.size());

		sc.setEnabled(false);

		repos = repositoryDao.findBySearchCommand(sc);

		assertEquals("Invalid number of repositories!", 0, repos.size());
	}

	@Test
	public void testFindNumberOfRepositories() {
		final RepositorySearchCommand sc = new RepositorySearchCommand();
		sc.setEnabled(true);

		int count = repositoryDao.findNumberOfRepositories(sc);

		assertEquals("Invalid number of repositories!", 1, count);

		sc.setEnabled(false);

		count = repositoryDao.findNumberOfRepositories(sc);

		assertEquals("Invalid number of repositories!", 0, count);
	}

}
