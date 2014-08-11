package nl.tranquilizedquality.adm.gwt.gui.client.service.logtail;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Async service responsible for managing the log tailing.
 *
 * @author e-pragt (erik.pragt@Tranquilized Quality.com)
 * @since 10/30/12
 */
public interface LogTailServiceAsync {

    /**
     * Start tailing the log file.
     *
     * @param logFile
     *            The log file to tail
     * @param async
     *            The async callback listener
     */
    void startTailing(String logFile, AsyncCallback<Void> async);

    /**
     * Start tailing the system log file.
     */
    void startSystemLogTailing(AsyncCallback<Void> async);

    /**
     * Get the content of the read file.
     *
     * @param async
     *            The async callback listener
     */
    void getContent(AsyncCallback<String> async);

    /**
     * Stop tailing the current file.
     *
     * @param async
     *            The async callback listener
     */
    void stopTailing(AsyncCallback<Void> async);
}
