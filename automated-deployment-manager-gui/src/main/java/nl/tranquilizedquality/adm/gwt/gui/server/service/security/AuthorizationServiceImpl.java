package nl.tranquilizedquality.adm.gwt.gui.server.service.security;

import java.util.List;

import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.security.AuthorizationService;
import nl.tranquilizedquality.adm.security.business.manager.AuthorizationManager;

import org.springframework.beans.factory.annotation.Required;

/**
 * Remote service that can be accessed by the GUI to check if the logged in user
 * is authorized to do certain actions within the GUI.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 */
public class AuthorizationServiceImpl implements AuthorizationService {

    /** The manager that can retrieve authorization information. */
    private AuthorizationManager authorizationManager;

    @Override
    public Boolean isLoggedInUserAuthorized(final String authority) {
        return authorizationManager.isLoggedInUserAuthorized(authority);
    }

    @Override
    public List<String> findLoggedInUserPrivileges() {
        return authorizationManager.findLoggedInUserPrivileges();
    }

    /**
     * @param authorizationManager
     *        the authorizationManager to set
     */
    @Required
    public void setAuthorizationManager(final AuthorizationManager authorizationManager) {
        this.authorizationManager = authorizationManager;
    }

}
