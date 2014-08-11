/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 30 okt. 2011 File: ClientDestinationHost.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.model.environment
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
package nl.tranquilizedquality.adm.gwt.gui.client.model.environment;

import nl.tranquilizedquality.adm.commons.business.domain.DestinationHost;
import nl.tranquilizedquality.adm.commons.business.domain.Protocol;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.AbstractUpdatableBeanModel;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUserGroup;

/**
 * Client side representation of a {@link DestinationHost}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 30 okt. 2011
 */
public class ClientDestinationHost extends AbstractUpdatableBeanModel<Long> implements DestinationHost {

    /**
     * Unique identifier used for serialization.
     */
    private static final long serialVersionUID = -4422210444472519996L;

    /** The host name of the destination server. */
    private String hostName;

    /** The port to connect to on the host. */
    private Integer port;

    /** The protocol to use for deployment. */
    private Protocol protocol;

    /** The user name to login with. */
    private String username;

    /** The password to login with. */
    private String password;

    /** The private key to use for authentication. */
    private String privateKey;

    /** The group where this destination host belongs to */
    private UserGroup userGroup;

    /** The terminal to use when connecting to the host. e.g. "gogrid". */
    private String terminal;

    @Override
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

    @Override
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

    @Override
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     *            the username to set
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     *            the password to set
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * @return the port
     */
    @Override
    public Integer getPort() {
        return port;
    }

    /**
     * @param port
     *            the port to set
     */
    public void setPort(final Integer port) {
        this.port = port;
    }

    @Override
    public String getPrivateKey() {
        return privateKey;
    }

    @Override
    public void setPrivateKey(final String privateKey) {
        /*
         * Do nothing by default so the private key is not available on the
         * client side.
         */
    }

    public void setAuthorizedPrivateKey(final String privateKey) {
        this.privateKey = privateKey;
    }

    public String getAuthorizedPrivateKey() {
        return this.privateKey;
    }

    @Override
    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(final UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    @Override
    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(final String terminal) {
        this.terminal = terminal;
    }

    @Override
    public void copy(final DomainObject<Long> object) {
        super.copy(object);

        if (object instanceof DestinationHost) {
            final DestinationHost destinationHost = (DestinationHost) object;

            this.hostName = destinationHost.getHostName();
            this.password = destinationHost.getPassword();
            this.username = destinationHost.getUsername();
            this.port = destinationHost.getPort();
            this.protocol = destinationHost.getProtocol();
            this.setPrivateKey(destinationHost.getPrivateKey());
            this.terminal = destinationHost.getTerminal();

            final UserGroup newUserGroup = destinationHost.getUserGroup();
            this.userGroup = new ClientUserGroup();
            this.userGroup.copy(newUserGroup);
        }
    }

    @Override
    public boolean equals(final Object obj) {

        if (obj instanceof ClientDestination) {
            final ClientDestination destination = (ClientDestination) obj;

            if (this.id != null && !this.id.equals(destination.getId())) {
                return false;
            } else if (this.id == null && destination.getId() != null) {
                return false;
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + (id == null ? 0 : id.intValue());

        return result;
    }

}
