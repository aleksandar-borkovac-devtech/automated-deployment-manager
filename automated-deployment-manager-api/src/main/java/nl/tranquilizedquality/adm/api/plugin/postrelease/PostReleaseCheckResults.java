/*
 * @(#)PostReleaseCheckResults.java 20 mrt. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.api.plugin.postrelease;

/**
 * Results from the performed post release checks.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 20 mrt. 2013
 */
public class PostReleaseCheckResults {

    /**
     * The log messages to show in the GUI for the user so he/she can check what
     * went wrong.
     */
    private final String logs;

    /** The status of the checks that were performed. */
    private final PostReleaseCheckStatus status;

    /**
     * Constructor taking the logs and the status.
     * 
     * @param logs
     *            The logs to be shown in the GUI.
     * @param status
     *            The status of the checks.
     */
    public PostReleaseCheckResults(final String logs, final PostReleaseCheckStatus status) {
        this.logs = logs;
        this.status = status;
    }

    /**
     * Retrieves the post release checks logs.
     * 
     * @return Returns a {@link String} value containing the logs.
     */
    public String getLogs() {
        return logs;
    }

    /**
     * Retrieves the status of the release checks to see if they passed or not.
     * 
     * @return Returns a {@link PostReleaseCheckStatus}.
     */
    public PostReleaseCheckStatus getStatus() {
        return status;
    }

}
