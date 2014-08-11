/**
 * <pre>
 * Project: automated-deployment-manager-security Created on: 24 nov. 2011 File: fHibernateUser.java
 * Package: nl.Tranquilized Quality.adm.security.persistence.db.hibernate.bean
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
package nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import nl.tranquilizedquality.adm.commons.business.domain.Role;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.business.domain.UserRole;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.hibernate.bean.AbstractUpdatableDomainObject;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;

import com.google.code.simplestuff.annotation.BusinessField;

/**
 * Hibernate implementation of a user of ADM.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 24 nov. 2011
 */
@Entity()
@Table(name = "ADM_USERS", schema = "SEC")
public class HibernateUser extends AbstractUpdatableDomainObject<Long> implements User {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4382070706781958804L;

    /** The name of the user. */
    @BusinessField
    private String name;

    /** The user name of the user. */
    @BusinessField
    private String userName;

    /** The password of the user. */
    @BusinessField
    private String password;

    /** Email address of the user. */
    @BusinessField
    private String email;

    /** The mobile phone number this user can be reached on. */
    @BusinessField
    private String mobilePhoneNumber;

    /** Determines if the user is active or not. */
    @BusinessField
    private Boolean active;

    /** Determines if the user is blocked of not. */
    @BusinessField
    private Boolean blocked;

    /** The date on which the user started being active. */
    @BusinessField
    private Date activeFrom;

    /** Determines if the password was sent to the user. */
    @BusinessField
    private Boolean passwordSent;

    /** The new password the user wants to use. */
    private String newPassword;

    /** The verification password which should be the same as the new password. */
    private String verificationPassword;

    /** The date when the user was logged in the last time. */
    @BusinessField
    private Date lastLoginDate;

    /**
     * The date until the user was active. The user is still active if this date
     * is null.
     */
    @BusinessField
    private Date activeUntil;

    /** Additional comments on the user account. */
    @BusinessField
    private String comments;

    /** The roles the user has. */
    private Set<UserRole> userRoles;

    @Id
    @Override
    @Column(name = "ID")
    @SequenceGenerator(name = "USERS_SEQ_GEN", initialValue = 1, allocationSize = 1, sequenceName = "SEC.USERS_SEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "USERS_SEQ_GEN")
    public Long getId() {
        return id;
    }

    /**
     * Default constructor.
     */
    public HibernateUser() {
        this.passwordSent = false;
    }

    @Override
    @Type(type = "yes_no")
    @Index(name = "IDX_USER_ACTIVE")
    @Column(name = "ACTIVE", unique = false, nullable = false)
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
    @Type(type = "yes_no")
    @Index(name = "IDX_BLOCKED")
    @Column(name = "BLOCKED", unique = false, nullable = false)
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
    @Type(type = "timestamp")
    @Column(name = "ACTIVE_FROM", unique = false, nullable = true)
    public Date getActiveFrom() {
        return activeFrom;
    }

    @Override
    public void setActiveFrom(final Date activeFrom) {
        this.activeFrom = activeFrom;
    }

    @Override
    @Type(type = "timestamp")
    @Index(name = "IDX_ACTIVE_UNTIL")
    @Column(name = "ACTIVE_UNTIL", unique = false, nullable = true)
    public Date getActiveUntil() {
        return activeUntil;
    }

    @Override
    public void setActiveUntil(final Date activeUntil) {
        this.activeUntil = activeUntil;
    }

    @Override
    @Column(name = "COMMENTS", unique = false, nullable = true)
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
    @OneToMany(mappedBy = "user", targetEntity = HibernateUserRole.class)
    @ForeignKey(name = "FK_USER_ROLE")
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
    @Type(type = "yes_no")
    @Index(name = "IDX_PASSWORD_SENT")
    @Column(name = "PASSWORD_SENT", unique = false, nullable = false)
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
    @Type(type = "timestamp")
    @Index(name = "IDX_LAST_LOGIN_DATE")
    @Column(name = "LAST_LOGIN_DATE", unique = false, nullable = true)
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
    @Column(name = "NAME")
    @Index(name = "IDX_NAME")
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    @Column(name = "USERNAME", nullable = false)
    @Index(name = "IDX_USER_NAME")
    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    @Override
    @Column(name = "PASSWORD", nullable = false)
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(final String password) {
        this.password = password;
    }

    @Override
    @Column(name = "EMAIL_ADDRESS")
    @Index(name = "IDX_EMAIL")
    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    @Override
    @Column(name = "MOBILE_PHONE_NUMBER")
    @Index(name = "IDX_MOBILE_PHONE_NUMBER")
    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(final String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    @Override
    @Transient
    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(final String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    @Transient
    public String getVerificationPassword() {
        return verificationPassword;
    }

    public void setVerificationPassword(final String verificationPassword) {
        this.verificationPassword = verificationPassword;
    }

    @Override
    public void copy(final DomainObject<Long> object) {
        super.copy(object);

        if (object instanceof User) {
            final User user = (User) object;
            this.name = user.getName();
            this.userName = user.getUserName();
            this.password = user.getPassword();
            this.email = user.getEmail();
            this.active = user.isActive();
            this.blocked = user.isBlocked();
            this.passwordSent = user.getPasswordSent();
            this.activeFrom = user.getActiveFrom();
            this.activeUntil = user.getActiveUntil();
            this.comments = user.getComments();
            this.lastLoginDate = user.getLastLoginDate();
            this.mobilePhoneNumber = user.getMobilePhoneNumber();
            this.newPassword = user.getNewPassword();
            this.verificationPassword = user.getVerificationPassword();
            this.setCreated(user.getCreated());
            this.setCreatedBy(user.getCreatedBy());
            this.setAltered(user.getAltered());
            this.setAlteredBy(user.getAlteredBy());

            final Set<UserRole> originalUserRoles = user.getUserRoles();
            this.userRoles = new HashSet<UserRole>();
            if (originalUserRoles != null) {
                for (final UserRole role : originalUserRoles) {
                    final HibernateUserRole hibernateUserRole = new HibernateUserRole();
                    hibernateUserRole.copy(role);

                    this.userRoles.add(hibernateUserRole);
                }
            }
        }

    }

}
