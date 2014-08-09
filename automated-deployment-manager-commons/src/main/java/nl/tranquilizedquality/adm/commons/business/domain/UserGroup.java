/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: Aug 23, 2012 File: fUserGroup.java
 * Package: nl.Tranquilized Quality.adm.core.persistence.db.hibernate.bean
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
package nl.tranquilizedquality.adm.commons.business.domain;

import java.util.List;

import nl.tranquilizedquality.adm.commons.domain.UpdatableDomainObject;

/**
 * Representation of a group of users that is used to filter out data that is
 * only interesting for that group.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 23, 2012
 */
public interface UserGroup extends UpdatableDomainObject<Long> {

    /**
     * Retrieves the name of the group.
     * 
     * @return Returns a {@link String} value of the name of the group.
     */
    String getName();

    /**
     * Retrieves the list of users that are part of the group.
     * 
     * @return Returns a {@link List} containing {@link User} objects or an
     *         empty one if no users are part of the group.
     */
    List<User> getUsers();

    /**
     * Sets the users that are part of this user group.
     * 
     * @param users
     *        The users that will be set.
     */
    void setUsers(List<User> users);

}
