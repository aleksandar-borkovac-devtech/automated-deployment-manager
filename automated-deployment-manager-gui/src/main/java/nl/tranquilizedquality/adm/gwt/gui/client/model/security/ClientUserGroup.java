/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 17 sep. 2011 File: ClientUserGroup.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.model.release
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
package nl.tranquilizedquality.adm.gwt.gui.client.model.security;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.AbstractUpdatableBeanModel;

/**
 * Client side representation of a user group.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 24 aug. 2012
 */
public class ClientUserGroup extends AbstractUpdatableBeanModel<Long> implements UserGroup {

    /**
     * Unique identifier used for serialization.
     */
    private static final long serialVersionUID = -6162633327289244076L;

    /** The name of the user group. */
    private String name;

    /** The users that are part of this group. */
    private List<User> users;

    /**
     * Default constructor.
     */
    public ClientUserGroup() {
        users = new ArrayList<User>();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public void setUsers(final List<User> users) {
        this.users = users;
    }

    public void shallowCopy(final UserGroup userGroup) {
        super.copy(userGroup);

        this.name = userGroup.getName();
        this.users = new ArrayList<User>();
    }

    @Override
    public void copy(final DomainObject<Long> object) {
        if (object instanceof UserGroup) {
            final UserGroup group = (UserGroup) object;

            shallowCopy(group);

            final List<User> newUsers = group.getUsers();
            for (final User user : newUsers) {
                final ClientUser newUser = new ClientUser();
                newUser.copy(user);
                this.users.add(newUser);
            }
        }
    }

}
