/**
 * <pre>
 * Project: automated-deployment-manager-security Created on: Aug 25, 2012 File: fUserSearchCommand.java
 * Package: nl.Tranquilized Quality.adm.security.business.command
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
package nl.tranquilizedquality.adm.security.business.command;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.Role;
import nl.tranquilizedquality.adm.commons.business.domain.Scope;
import nl.tranquilizedquality.adm.commons.domain.PagingSearchCommand;

/**
 * Search criteria used for searching for users.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public class UserSearchCommand extends PagingSearchCommand {

    /**
     * Unique identifier used to search for users.
     */
    private static final long serialVersionUID = -3591229733503541264L;

    /** The name of the users to search for. */
    private String name;

    /** The user name to search for. */
    private String userName;

    /** The state of the user account. */
    private Boolean active;

    /** The scope where the user has a role in. */
    private Scope scope;

    private String activeValue;

    /** The roles a user should have. */
    private List<Role> userRoles;

    /**
     * Default constructor.
     */
    public UserSearchCommand() {
        active = true;
        userRoles = new ArrayList<Role>();
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(final Boolean active) {
        this.active = active;
    }

    /**
     * @return the activeValue
     */
    public String getActiveValue() {
        return activeValue;
    }

    /**
     * @param activeValue
     *            the activeValue to set
     */
    public void setActiveValue(final String activeValue) {
        if ("Active".equals(activeValue)) {
            this.active = true;
        }
        else if ("Non Active".equals(activeValue)) {
            this.active = false;
        }
        else {
            this.active = null;
        }

        this.activeValue = activeValue;
    }

    /**
     * @return the scope
     */
    public Scope getScope() {
        return scope;
    }

    /**
     * @param scope
     *            the scope to set
     */
    public void setScope(final Scope scope) {
        this.scope = scope;
    }

    /**
     * @return the userRoles
     */
    public List<Role> getUserRoles() {
        return userRoles;
    }

    /**
     * @param userRoles
     *            the userRoles to set
     */
    public void setUserRoles(final List<Role> userRoles) {
        this.userRoles = userRoles;
    }

    public void addRole(final Role role) {
        this.userRoles.add(role);
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

}
