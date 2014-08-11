/*
 * @(#)HibernateEnvironmentNotificationSetting.java 22 okt. 2012
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import nl.tranquilizedquality.adm.commons.business.domain.Environment;
import nl.tranquilizedquality.adm.commons.business.domain.EnvironmentNotificationSetting;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.hibernate.bean.AbstractUpdatableDomainObject;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUser;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;

import com.google.code.simplestuff.annotation.BusinessField;

/**
 * Hibernate bean implementation where the notification settings are configured
 * for a specific user.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 22 okt. 2012
 */
@Entity()
@Table(name = "ENVIRONMENT_NOTIFICATION_SETTINGS", schema = "ADM")
public class HibernateEnvironmentNotificationSetting extends AbstractUpdatableDomainObject<Long> implements
        EnvironmentNotificationSetting {

    /** The user where these settings are for. */
    @BusinessField
    private User user;

    /** Environment where these settings apply to. */
    @BusinessField
    private Environment environment;

    /**
     * Determines if an email notification needs to be sent for this environment
     * when a deployment was done.
     */
    @BusinessField
    private boolean emailNotification;

    /**
     * Determines if an SMS notification needs to be sent for this environment
     * when a deployment was done.
     */
    @BusinessField
    private boolean smsNotification;

    @Id
    @Override
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ENVIRONMENT_NOTIFICATION_SETTINGS_SEQ_GEN")
    @SequenceGenerator(name = "ENVIRONMENT_NOTIFICATION_SETTINGS_SEQ_GEN", initialValue = 1, allocationSize = 1, sequenceName = "ADM.ENVIRONMENT_NOTIFICATION_SETTINGS_SEQ")
    public Long getId() {
        return id;
    }

    @Override
    @Index(name = "ENS_USER_IDX")
    @JoinColumn(name = "USER_ID")
    @ManyToOne(targetEntity = HibernateUser.class, optional = false)
    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    @Override
    @JoinColumn(name = "ENVIRONMENT_ID")
    @ManyToOne(targetEntity = HibernateEnvironment.class, optional = false)
    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(final Environment environment) {
        this.environment = environment;
    }

    @Override
    @Type(type = "yes_no")
    @Column(name = "EMAIL_NOTIFICATION", nullable = false)
    public boolean isEmailNotification() {
        return emailNotification;
    }

    public void setEmailNotification(final boolean emailNotification) {
        this.emailNotification = emailNotification;
    }

    @Override
    @Type(type = "yes_no")
    @Column(name = "SMS_NOTIFICATION", nullable = false)
    public boolean isSmsNotification() {
        return smsNotification;
    }

    public void setSmsNotification(final boolean smsNotification) {
        this.smsNotification = smsNotification;
    }

    @Override
    public void copy(final DomainObject<Long> object) {
        if (object instanceof EnvironmentNotificationSetting) {
            super.copy(object);
            final EnvironmentNotificationSetting settings = (EnvironmentNotificationSetting) object;

            this.emailNotification = settings.isEmailNotification();
            this.smsNotification = settings.isSmsNotification();

            final Environment originalEnvironment = settings.getEnvironment();
            this.environment = new HibernateEnvironment();
            this.environment.copy(originalEnvironment);

            final User originalUser = settings.getUser();
            this.user = new HibernateUser();
            this.user.copy(originalUser);
        }
    }

}
