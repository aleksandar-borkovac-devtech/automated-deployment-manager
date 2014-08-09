package nl.tranquilizedquality.adm.commons.gwt.ext.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Service to get any information from the HTTP session.
 *
 * @author e-pragt (erik.pragt@Tranquilized Quality.com)
 * @since 10/25/12
 */
public interface SessionServiceAsync {

    /**
     * Gets the user session timeout in seconds.
     */
    void getUserSessionTimeout(AsyncCallback<Integer> callback);

}
