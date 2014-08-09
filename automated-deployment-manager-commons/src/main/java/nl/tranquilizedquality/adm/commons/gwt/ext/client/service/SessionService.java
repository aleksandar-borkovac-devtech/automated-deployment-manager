package nl.tranquilizedquality.adm.commons.gwt.ext.client.service;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Service to get any information from the HTTP session.
 *
 * @author e-pragt (erik.pragt@Tranquilized Quality.com)
 * @since 10/25/12
 */
@RemoteServiceRelativePath("SessionService.rpc")
public interface SessionService extends RemoteService {

    /**
     * Utility class for simplifying access to the instance of async service.
     */
    public static class Util {

        private static SessionServiceAsync instance;

        public static SessionServiceAsync getInstance() {
            if (instance == null) {
                instance = GWT.create(SessionService.class);
            }
            return instance;
        }
    }

    /**
     * Gets the user session timeout in milliseconds.
     */
    Integer getUserSessionTimeout();

}
