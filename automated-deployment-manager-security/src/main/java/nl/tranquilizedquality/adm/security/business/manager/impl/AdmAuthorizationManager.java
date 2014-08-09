package nl.tranquilizedquality.adm.security.business.manager.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nl.tranquilizedquality.adm.security.business.manager.AuthorizationManager;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Implementation of the {@link AuthorizationManager} which checks if the
 * currently logged in user is authorized for a certain action.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 */
public class AdmAuthorizationManager implements AuthorizationManager {

    /**
     * Retrieves the granted authorities of the logged in user.
     * 
     * @return Returns a {@link Collection} containing {@link GrantedAuthority} objects.
     */
    private Collection<GrantedAuthority> findGrantedAuthorities() {
        /*
         * Retrieve the security context of the current thread.
         */
        final SecurityContext context = SecurityContextHolder.getContext();

        /*
         * Retrieve the current authentication.
         */
        final Authentication authentication = context.getAuthentication();

        /*
         * Retrieve the granted authorities for the logged in user.
         */
        final Collection<GrantedAuthority> authorities = authentication.getAuthorities();

        return authorities;
    }

    @Override
    public boolean isLoggedInUserAuthorized(final String authority) {

        final Collection<GrantedAuthority> authorities = findGrantedAuthorities();

        /*
         * Check if the authority is present in the the granted authorities.
         */
        for (final GrantedAuthority grantedAuthority : authorities) {
            final String granted = grantedAuthority.toString();
            if (granted.equals(authority)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public List<String> findLoggedInUserPrivileges() {

        final Collection<GrantedAuthority> authorities = findGrantedAuthorities();
        final List<String> privileges = new ArrayList<String>();

        for (final GrantedAuthority grantedAuthority : authorities) {
            final String granted = grantedAuthority.toString();
            privileges.add(granted);
        }

        return privileges;
    }

}
