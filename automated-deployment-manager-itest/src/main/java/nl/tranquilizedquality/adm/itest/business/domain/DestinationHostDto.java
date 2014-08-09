/*
 * @(#)DestinationHostDto.java 8 mrt. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.itest.business.domain;

import nl.tranquilizedquality.adm.commons.business.domain.Protocol;

/**
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 8 mrt. 2013
 */
public class DestinationHostDto {

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
    private String userGroupName;

    /** The terminal to use when connecting to the host. e.g. "gogrid". */
    private String terminal;

    public String getHostName() {
        return hostName;
    }

    public void setHostName(final String hostName) {
        this.hostName = hostName;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(final Integer port) {
        this.port = port;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(final Protocol protocol) {
        this.protocol = protocol;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(final String privateKey) {
        this.privateKey = privateKey;
    }

    public String getUserGroupName() {
        return userGroupName;
    }

    public void setUserGroupName(final String userGroupName) {
        this.userGroupName = userGroupName;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(final String terminal) {
        this.terminal = terminal;
    }

}
