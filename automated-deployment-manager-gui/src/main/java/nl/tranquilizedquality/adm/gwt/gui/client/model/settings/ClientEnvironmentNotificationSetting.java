/*
 * @(#)ClientNotificationSettings.java 23 okt. 2012
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.gwt.gui.client.model.settings;

import nl.tranquilizedquality.adm.commons.business.domain.Environment;
import nl.tranquilizedquality.adm.commons.business.domain.EnvironmentNotificationSetting;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.AbstractUpdatableBeanModel;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientEnvironment;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUser;

/**
 * Client side representation of
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 23 okt. 2012
 */
public class ClientEnvironmentNotificationSetting extends AbstractUpdatableBeanModel<Long> implements
        EnvironmentNotificationSetting {

    /** The user where these settings are for. */
    private User user;

    /** Environment where these settings apply to. */
    private Environment environment;

    /**
     * Determines if an email notification needs to be sent for this environment
     * when a deployment was done.
     */
    private boolean emailNotification;

    /**
     * Determines if an SMS notification needs to be sent for this environment
     * when a deployment was done.
     */
    private boolean smsNotification;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    @Override
    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(final Environment environment) {
        this.environment = environment;
    }

    public String getEnvironmentName() {
        if (environment != null) {
            return environment.getName();
        }
        return "N/A";
    }

    @Override
    public boolean isEmailNotification() {
        return emailNotification;
    }

    public void setEmailNotification(final boolean emailNotification) {
        this.emailNotification = emailNotification;
    }

    @Override
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
            this.environment = new ClientEnvironment();
            this.environment.copy(originalEnvironment);

            final User originalUser = settings.getUser();
            this.user = new ClientUser();
            this.user.copy(originalUser);
        }
    }

}
