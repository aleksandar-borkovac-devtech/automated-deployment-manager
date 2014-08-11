/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 24 sep. 2012 File: ClientUser.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.model.release
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
package nl.tranquilizedquality.adm.gwt.gui.client.model.security;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Transient;

import nl.tranquilizedquality.adm.commons.business.domain.Role;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.business.domain.UserRole;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.AbstractUpdatableBeanModel;
import nl.tranquilizedquality.adm.gwt.gui.client.model.role.ClientUserRole;

/**
 * Client side representation of a user.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 24 aug. 2012
 */
public class ClientUser extends AbstractUpdatableBeanModel<Long> implements User {

    /**
     * Unique identifier used for serialization.
     */
    private static final long serialVersionUID = -9026497938134917156L;

    /** The name of the user. */
    private String name;

    /** The user name of the user. */
    private String userName;

    /** The password of the user. */
    private String password;

    /** Email address of the user. */
    private String email;

    /** The mobile phone number this user can be reached on. */
    private String mobilePhoneNumber;

    /** Determines if the user is active or not. */
    private Boolean active;

    /** Determines if the user is blocked of not. */
    private Boolean blocked;

    /** The date on which the user started being active. */
    private Date activeFrom;

    /** Determines if the password was sent to the user. */
    private Boolean passwordSent;

    /** The new password the user wants to use. */
    private String newPassword;

    /** The verification password which should be the same as the new password. */
    private String verificationPassword;

    /** The date when the user was logged in the last time. */
    private Date lastLoginDate;

    /**
     * The date until the user was active. The user is still active if this date
     * is null.
     */
    private Date activeUntil;

    /** Additional comments on the user account. */
    private String comments;

    /** The roles the user has. */
    private Set<UserRole> userRoles;

    /**
     * Default constructor.
     */
    public ClientUser() {
        this.active = true;
        this.activeFrom = new Date();
        this.blocked = false;
        this.passwordSent = false;

        this.userRoles = new HashSet<UserRole>();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Boolean isActive() {
        return active;
    }

    @Transient
    // Used by equals()
    public Boolean getActive() {
        return active;
    }

    @Override
    public void setActive(final Boolean active) {
        this.active = active;
    }

    @Override
    public Boolean isBlocked() {
        return blocked;
    }

    @Transient
    // Used by equals()
    public Boolean getBlocked() {
        return blocked;
    }

    @Override
    public void setBlocked(final Boolean blocked) {
        this.blocked = blocked;
    }

    @Override
    public Date getActiveFrom() {
        return activeFrom;
    }

    @Override
    public void setActiveFrom(final Date activeFrom) {
        this.activeFrom = activeFrom;
    }

    @Override
    public Date getActiveUntil() {
        return activeUntil;
    }

    @Override
    public void setActiveUntil(final Date activeUntil) {
        this.activeUntil = activeUntil;
    }

    @Override
    public String getComments() {
        return comments;
    }

    @Override
    public void setComments(final String comments) {
        this.comments = comments;
    }

    /**
     * @return the roles
     */
    @Override
    @Transient
    // Used by equals()
    public Set<Role> getRoles() {
        final Set<Role> roles = new HashSet<Role>();
        for (final UserRole userRole : getUserRoles()) {
            roles.add(userRole.getRole());
        }
        return roles;
    }

    /**
     * @return the user roles
     */
    @Override
    public Set<UserRole> getUserRoles() {
        return userRoles;
    }

    /**
     * @param userRoles
     *            the user roles to set
     */
    @Override
    public void setUserRoles(final Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    /**
     * @return the passwordSent
     */
    @Override
    public Boolean getPasswordSent() {
        return passwordSent;
    }

    /**
     * @param passwordSent
     *            the passwordSent to set
     */
    @Override
    public void setPasswordSent(final Boolean passwordSent) {
        this.passwordSent = passwordSent;
    }

    /**
     * @return the lastLoginDate
     */
    @Override
    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    /**
     * @param lastLoginDate
     *            the lastLoginDate to set
     */
    @Override
    public void setLastLoginDate(final Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(final String password) {
        this.password = password;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    @Override
    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(final String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    @Override
    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(final String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String getVerificationPassword() {
        return verificationPassword;
    }

    public void setVerificationPassword(final String verificationPassword) {
        this.verificationPassword = verificationPassword;
    }

    /**
     * Copy the fields of the given {@link User} object into this object. For
     * each collection field it will add an empty collection.
     * 
     * @param user
     *            the origin object.
     */
    public void shallowCopy(final User user) {
        super.copy(user);

        this.name = user.getName();
        this.userName = user.getUserName();
        this.password = user.getPassword();
        this.active = user.isActive();
        this.blocked = user.isBlocked();
        this.passwordSent = user.getPasswordSent();
        this.activeFrom = user.getActiveFrom();
        this.activeUntil = user.getActiveUntil();
        this.comments = user.getComments();
        this.lastLoginDate = user.getLastLoginDate();
        this.email = user.getEmail();
        this.mobilePhoneNumber = user.getMobilePhoneNumber();
        this.newPassword = user.getNewPassword();
        this.verificationPassword = user.getVerificationPassword();
        this.setCreated(user.getCreated());
        this.setCreatedBy(user.getCreatedBy());
        this.setAltered(user.getAltered());
        this.setAlteredBy(user.getAlteredBy());

        this.userRoles = new HashSet<UserRole>();
    }

    @Override
    public void copy(final DomainObject<Long> object) {
        if (object instanceof User) {
            final User user = (User) object;

            shallowCopy(user);

            final Set<UserRole> originalUserRoles = user.getUserRoles();
            this.userRoles = new HashSet<UserRole>();
            if (originalUserRoles != null) {
                for (final UserRole role : originalUserRoles) {
                    final ClientUserRole clientUserRole = new ClientUserRole();
                    clientUserRole.shallowCopy(role);

                    this.userRoles.add(clientUserRole);
                }
            }
        }
    }

    @Override
    public boolean equals(final Object obj) {

        if (obj instanceof ClientUser) {
            final ClientUser user = (ClientUser) obj;

            if (this.id != null && !this.id.equals(user.getId())) {
                return false;
            }
            else if (this.id == null && user.getId() != null) {
                return false;
            }

            if (this.active != null && !this.active.equals(user.getActive())) {
                return false;
            }
            else if (this.active == null && user.getActive() != null) {
                return false;
            }

            if (this.name != null && !this.name.equals(user.getName())) {
                return false;
            }
            else if (this.name == null && user.getName() != null) {
                return false;
            }

            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + (active == null ? 0 : active.hashCode());
        result = prime * result + (name == null ? 0 : name.hashCode());
        result = prime * result + (id == null ? 0 : id.intValue());

        return result;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
