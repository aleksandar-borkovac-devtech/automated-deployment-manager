package nl.tranquilizedquality.adm.security.business.manager.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import nl.tranquilizedquality.adm.security.business.manager.impl.AdmAuthorizationManager;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Test for {@link DamAuthorizationManager}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 */
public class AdmAuthorizationManagerTest {

    /** The manager that will be tested. */
    private AdmAuthorizationManager authorizationManager;

    private class TestingAuthenticationToken extends AbstractAuthenticationToken {

        /**
         * 
         */
        private static final long serialVersionUID = 2562611504888074619L;

        public TestingAuthenticationToken(final Collection<GrantedAuthority> authorities) {
            super(authorities);
            final UserDetails userDetails = new User("username", "123456", true, false, false, false, authorities);
            this.setDetails(userDetails);
        }

        public Object getCredentials() {
            return "123456";
        }

        public Object getPrincipal() {
            return this.getDetails();
        }

    };

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        authorizationManager = new AdmAuthorizationManager();

        final Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        grantedAuthorities.add(new GrantedAuthorityImpl("ROLE_CUSTOMER"));
        final Authentication authentication = new TestingAuthenticationToken(grantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void testIsLoggedInUserAuthorized() {
        boolean authorized = authorizationManager.isLoggedInUserAuthorized("ROLE_CUSTOMER");

        assertTrue(authorized);

        authorized = authorizationManager.isLoggedInUserAuthorized("ROLE_CUSTOMER_");

        assertFalse(authorized);
    }

}
