package nl.tranquilizedquality.adm.security.persistence.db.jdbc.dao;

import static junit.framework.Assert.assertEquals;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.Privilege;
import nl.tranquilizedquality.adm.commons.business.domain.Scope;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.security.persistence.db.dao.PrivilegeDao;
import nl.tranquilizedquality.adm.security.persistence.db.dao.ScopeDao;
import nl.tranquilizedquality.adm.security.persistence.db.dao.UserDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test for the {@link PrivilegeDao}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 sep 2009
 */
public class PrivilegeDaoTest extends AbstractDaoTest {

	/** The DAO that will be tested. */
	@Autowired
	private PrivilegeDao<Privilege> apiPrivilegeDao;

	@Autowired
	private UserDao<User> apiUserDao;

	@Autowired
	private ScopeDao<Scope> scopeDao;

	@Before
	public void setUp() {
		executeSqlScript("adm-test-data-cleanup.sql", false);
		executeSqlScript("adm-test-data.sql", false);
	}

	@After
	public void cleanUp() {
		executeSqlScript("adm-test-data-cleanup.sql", false);
	}

	@Test
	public void testFindByUserAndScope() {
		final User user = apiUserDao.findActiveUserByUserName("s-petrus");

		final Scope scope = scopeDao.findByName("ADM");

		final List<Privilege> privileges = apiPrivilegeDao.findByUserAndScope(user, scope);

		assertEquals(2, privileges.size());
	}
}
