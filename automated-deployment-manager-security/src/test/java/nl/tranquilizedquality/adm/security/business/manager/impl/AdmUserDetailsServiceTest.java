package nl.tranquilizedquality.adm.security.business.manager.impl;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Collections;

import nl.tranquilizedquality.adm.commons.business.domain.Privilege;
import nl.tranquilizedquality.adm.commons.business.domain.Scope;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.security.business.manager.impl.AdmUserDetailsService;
import nl.tranquilizedquality.adm.security.persistence.db.dao.PrivilegeDao;
import nl.tranquilizedquality.adm.security.persistence.db.dao.ScopeDao;
import nl.tranquilizedquality.adm.security.persistence.db.dao.UserDao;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateScope;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUser;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Test for {@link DamUserDetailsService}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 */
public class AdmUserDetailsServiceTest {

    /** {@link UserDetailsService} that is going to be tested. */
    private AdmUserDetailsService userDetailsService;

    /** Mocked {@link PrivilegeDao}. */
    private PrivilegeDao<Privilege> privilegeDao;

    /** Mocked {@link UserDao}. */
    private UserDao<User> userDao;

    /** Mocked {@link ScopeDao}. */
    private ScopeDao<Scope> scopeDao;

    /**
     * @throws java.lang.Exception
     */
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        userDetailsService = new AdmUserDetailsService();
        privilegeDao = createMock(PrivilegeDao.class);
        userDao = createMock(UserDao.class);
        scopeDao = createMock(ScopeDao.class);

        userDetailsService.setPrivilegeDao(privilegeDao);
        userDetailsService.setUserDao(userDao);
        userDetailsService.setScope("DAM API unit test");
        userDetailsService.setScopeDao(scopeDao);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testLoadUserByUsername() {
        final HibernateUser user = new HibernateUser();
        user.setId(1L);
        user.setActive(true);
        user.setUserName("apiunittest");
        user.setPassword("apiunittest");
        user.setName("API unit test");
        user.setBlocked(false);

        final HibernateScope scope = new HibernateScope();
        scope.setName("DAM API unit test");

        expect(userDao.findActiveUserByUserName("apiunittest")).andReturn(user);
        userDao.updateLastLoginDate(user);
        expectLastCall().once();
        userDao.flush();
        expectLastCall().once();
        expect(scopeDao.findByName(scope.getName())).andReturn(scope);
        scopeDao.flush();
        expectLastCall().once();
        expect(privilegeDao.findByUserAndScope(user, scope)).andReturn(Collections.EMPTY_LIST);
        privilegeDao.flush();
        expectLastCall().once();

        replay(userDao);
        replay(privilegeDao);
        replay(scopeDao);

        final UserDetails userDetails = userDetailsService.loadUserByUsername("apiunittest");
        final String username = userDetails.getUsername();
        final String password = userDetails.getPassword();

        assertEquals(user.getUserName(), username);
        assertEquals(user.getPassword(), password);

        verify(userDao);
        verify(privilegeDao);
        verify(scopeDao);
    }

}
