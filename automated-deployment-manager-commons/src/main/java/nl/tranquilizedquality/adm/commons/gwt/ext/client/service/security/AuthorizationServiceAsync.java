package nl.tranquilizedquality.adm.commons.gwt.ext.client.service.security;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The asycn service used within the GUI to call the remote authorization
 * service.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 */
public interface AuthorizationServiceAsync {

    /**
     * Checks if the logged in user is authorized.
     * 
     * @param authority
     *        The authority to check on.
     * @param callback
     *        The {@link AsyncCallback} used to return true if the user is
     *        authorized or false if the user is not authorized.
     */
    void isLoggedInUserAuthorized(String authority, AsyncCallback<Boolean> callback);

    /**
     * Retrieves all privileges the logged in user has.
     * 
     * @param callback
     *        The {@link AsyncCallback} used to return the privileges of a logged in user.
     */
    void findLoggedInUserPrivileges(AsyncCallback<List<String>> callback);

}
