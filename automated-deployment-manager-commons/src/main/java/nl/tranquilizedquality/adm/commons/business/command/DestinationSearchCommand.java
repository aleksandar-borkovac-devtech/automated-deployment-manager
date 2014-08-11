/**
 * <pre>
 * Project: automated-deployment-manager-commons Created on: 22 okt. 2012 File: DestinationSearchCommand.java
 * Package: nl.tranquilizedquality.adm.commons.business.command
 * 
 * Copyright (c) 2011 Tranquilized Quality www.Tranquilized Quality.nl All rights
 * reserved.
 * 
 * This software is the confidential and proprietary information of Tranquilized Quality
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Tranquilized Quality.
 * </pre>
 */
package nl.tranquilizedquality.adm.commons.business.command;

import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.business.domain.Protocol;

/**
 * Search criteria for a {@link Destination}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 22 okt. 2012
 */
public class DestinationSearchCommand extends AbstractPagingUserGroupSearchCommand {

    /**
     * Unique identifier used for serialization.
     */
    private static final long serialVersionUID = -728244222603301802L;

    /** The name of the destination */
    private String name;

    /** The host name of the server. */
    private String hostName;

    /** The environment name to search on. */
    private String environment;

    /** The protocol to search on. */
    private Protocol protocol;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

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
     * @return the environment
     */
    public String getEnvironment() {
        return environment;
    }

    /**
     * @param environment
     *            the environment to set
     */
    public void setEnvironment(final String environment) {
        this.environment = environment;
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
