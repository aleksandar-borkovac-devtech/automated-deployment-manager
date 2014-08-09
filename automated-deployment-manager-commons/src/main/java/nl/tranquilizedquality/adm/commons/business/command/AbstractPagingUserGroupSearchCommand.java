/**
 * <pre>
 * Project: automated-deployment-manager-commons Created on: Aug 30, 2012 File: AbstractPagingUserGroupSearchCommand.java
 * Package: nl.tranquilizedquality.adm.commons.business.command
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
package nl.tranquilizedquality.adm.commons.business.command;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.commons.domain.PagingSearchCommand;

/**
 * Abstract base class for a search command that is filtering on user groups.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 30, 2012
 */
public abstract class AbstractPagingUserGroupSearchCommand extends PagingSearchCommand {

    /**
     * Unique identifier used for serialization.
     */
    private static final long serialVersionUID = 7900785733014245828L;

    /** The user group to filter on. */
    private List<UserGroup> userGroups;

    public List<UserGroup> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(final List<UserGroup> userGroups) {
        this.userGroups = userGroups;
    }

}
