/**
 * <pre>
 * Project: automated-deployment-manager-commons Created on: Aug 23, 2012 File: fUserGroupSearchCommand.java
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

import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;

/**
 * Search criteria used to search for {@link UserGroup} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 23, 2012
 * 
 */
public class UserGroupSearchCommand extends AbstractPagingUserGroupSearchCommand {

	/**
	 * Unique identifier for serialization.
	 */
	private static final long serialVersionUID = 2599232055785547928L;

	/** The name of the user group. */
	private String name;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

}
