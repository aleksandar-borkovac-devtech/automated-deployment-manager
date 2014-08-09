/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 30 okt. 2011 File: HibernateDestinationHost.java
 * Package: nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean
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
package nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import nl.tranquilizedquality.adm.commons.business.domain.DestinationHost;
import nl.tranquilizedquality.adm.commons.business.domain.Protocol;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.hibernate.bean.AbstractUpdatableDomainObject;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUserGroup;

import org.hibernate.annotations.Index;

import com.google.code.simplestuff.annotation.BusinessField;

/**
 * Hibernate implementation of a {@link DestinationHost}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 30 okt. 2011
 */
@Entity()
@Table(name = "DESTINATION_HOSTS", schema = "ADM")
public class HibernateDestinationHost extends AbstractUpdatableDomainObject<Long> implements DestinationHost {

    /**
     * 
     */
    private static final long serialVersionUID = -7668040483432399985L;

    /** The host name of the destination server. */
    @BusinessField
    private String hostName;

    /** The port to connect to on the host. */
    @BusinessField
    private Integer port;

    /** The protocol to use for deployment. */
    @BusinessField
    private Protocol protocol;

    /** The user name to login with. */
    @BusinessField
    private String username;

    /** The password to login with. */
    @BusinessField
    private String password;

    /** The private key to use for authentication. */
    @BusinessField
    private String privateKey;

    /** The group where this destination host belongs to */
    @BusinessField
    private UserGroup userGroup;

    /** The terminal to use when connecting to the host. e.g. "gogrid". */
    @BusinessField
    private String terminal;

    @Override
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "DESTINATION_HOSTS_SEQ_GEN")
    @SequenceGenerator(name = "DESTINATION_HOSTS_SEQ_GEN", initialValue = 1, allocationSize = 1, sequenceName = "ADM.DESTINATION_HOSTS_SEQ")
    public Long getId() {
        return id;
    }

    @Override
    @Index(name = "HOST_NAME_IDX")
    @Column(name = "HOST_NAME", nullable = false, length = 250)
    public String getHostName() {
        return hostName;
    }

    /**
     * @param hostName
     *        the hostName to set
     */
    public void setHostName(final String hostName) {
        this.hostName = hostName;
    }

    @Override
    @Enumerated(EnumType.STRING)
    @Column(name = "PROTOCOL", nullable = false, length = 4)
    public Protocol getProtocol() {
        return protocol;
    }

    /**
     * @param protocol
     *        the protocol to set
     */
    public void setProtocol(final Protocol protocol) {
        this.protocol = protocol;
    }

    @Override
    @Column(name = "USER_NAME", nullable = true, length = 256)
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     *        the username to set
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    @Override
    @Column(name = "PASSWORD", nullable = true, length = 256)
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     *        the password to set
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    @Override
    @Column(name = "PORT", nullable = false)
    public Integer getPort() {
        return port;
    }

    /**
     * @param port
     *        the port to set
     */
    public void setPort(final Integer port) {
        this.port = port;
    }

    @Override
    @Column(name = "PRIVATE_KEY", nullable = true, length = 3350)
    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(final String privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    @JoinColumn(name = "USER_GROUP_ID")
    @ManyToOne(targetEntity = HibernateUserGroup.class, optional = false)
    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(final UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    @Override
    @Column(name = "TERMINAL", nullable = true)
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
            final DestinationHost destination = (DestinationHost) object;
            this.hostName = destination.getHostName();
            this.password = destination.getPassword();
            this.username = destination.getUsername();
            this.port = destination.getPort();
            this.protocol = destination.getProtocol();
            this.privateKey = destination.getPrivateKey();
            this.terminal = destination.getTerminal();

            final UserGroup newUserGroup = destination.getUserGroup();
            this.userGroup = new HibernateUserGroup();
            this.userGroup.copy(newUserGroup);
        }
    }

}
