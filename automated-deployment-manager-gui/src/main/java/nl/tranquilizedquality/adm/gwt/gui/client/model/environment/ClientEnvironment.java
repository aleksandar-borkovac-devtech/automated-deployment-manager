/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 11 sep. 2011 File: ClientEnvironment.java
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

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.Environment;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.AbstractUpdatableBeanModel;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUser;

/**
 * Client representation of an {@link Environment}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 11 sep. 2011
 */
public class ClientEnvironment extends AbstractUpdatableBeanModel<Long> implements Environment {

    /**
     * Unique identifier used for serialization.
     */
    private static final long serialVersionUID = 2034197655515481576L;

    /** The name of the environment. */
    private String name;

    /** The description of the environment. */
    private String description;

    /**
     * Determines if this environment is a production environment so alerts will
     * be displayed when deploying to it.
     */
    private boolean production;

    /** The users that are allowed to deploy to this environment. */
    private List<User> users;

    /**
     * Default constructor.
     */
    public ClientEnvironment() {
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

    /**
     * @param name
     *            the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public void setUsers(final List<User> users) {
        this.users = users;
    }

    @Override
    public boolean isProduction() {
        return production;
    }

    public void setProduction(final boolean production) {
        this.production = production;
    }

    public void shallowCopy(final Environment environment) {
        super.copy(environment);
        name = environment.getName();
        description = environment.getDescription();
        users = new ArrayList<User>();
        production = environment.isProduction();
    }

    @Override
    public void copy(final DomainObject<Long> object) {
        if (object instanceof Environment) {
            final Environment environment = (Environment) object;
            shallowCopy(environment);

            final List<User> originalUsers = environment.getUsers();
            for (final User user : originalUsers) {
                final ClientUser newUser = new ClientUser();
                newUser.copy(user);
                users.add(newUser);
            }
        }
    }

}
