/**
 * <pre>
 * Project: automated-deployment-manager-commons Created on: 22 okt. 2012 File: DestinationHostSearchCommand.java
 * Package: nl.tranquilizedquality.adm.commons.business.command
 * 
 * Copyright (c) 2012 Tranquilized Quality www.Tranquilized Quality.nl All rights
 * reserved.
 * 
 * This software is the confidential and proprietary information of Tranquilized Quality
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Tranquilized Quality.
 * </pre>
 */
package nl.tranquilizedquality.adm.commons.business.command;

import nl.tranquilizedquality.adm.commons.business.domain.DestinationHost;
import nl.tranquilizedquality.adm.commons.business.domain.Protocol;

/**
 * Search criteria used for searching on {@link DestinationHost} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 22 okt. 2012
 */
public class DestinationHostSearchCommand extends AbstractPagingUserGroupSearchCommand {

    /**
     * Unique identifier used for serialization.
     */
    private static final long serialVersionUID = 6778813021664075090L;

    /** The host name of the server. */
    private String hostName;

    /** The protocol to search on. */
    private Protocol protocol;

    /**
     * @return the hostName
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * @param hostName
     *            the hostName to set
     */
    public void setHostName(final String hostName) {
        this.hostName = hostName;
    }

    /**
     * @return the protocol
     */
    public Protocol getProtocol() {
        return protocol;
    }

    /**
     * @param protocol
     *            the protocol to set
     */
    public void setProtocol(final Protocol protocol) {
        this.protocol = protocol;
    }

}
