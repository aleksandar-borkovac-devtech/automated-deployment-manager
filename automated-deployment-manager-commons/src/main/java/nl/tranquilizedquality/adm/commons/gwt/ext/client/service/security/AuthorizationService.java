package nl.tranquilizedquality.adm.commons.gwt.ext.client.service.security;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Authorization service to deterimine if the logged in user can do certain
 * stuff within the GUI.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 */
@RemoteServiceRelativePath("AuthorizationService.rpc")
public interface AuthorizationService extends RemoteService {

    /**
     * Utility class for simplifying access to the instance of async service.
     */
    class Util {

        /** The async service. */
        private static AuthorizationServiceAsync instance;

        /**
         * Retrieves an instance of the {@link AuthorizationServiceAsync}.
         * 
         * @return Returns a {@link AuthorizationServiceAsync}.
         */
        public static AuthorizationServiceAsync getInstance() {
            if (instance == null) {
                instance = GWT.create(AuthorizationService.class);
            }
            return instance;
        }
    }

    /**
     * Determines if the loggedin user is authorized to do the stuff the
     * specified authority allows you to do.
     * 
     * @param authority
     *        The authority which will be checked on the logged in user.
     * @return Returns true if the logged in user is authorized otherwise it
     *         will return false.
     */
    Boolean isLoggedInUserAuthorized(String authority);

    /**
     * Retrieves all privileges the logged in user has.
     * 
     * @return
     *         Returns a list of privileges the user has.
     */
    List<String> findLoggedInUserPrivileges();

}
