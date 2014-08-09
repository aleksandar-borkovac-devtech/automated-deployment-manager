/**
 * <pre>
 * Project: automated-deployment-manager-commons Created on: Aug 6, 2012 File: fReleaseExecutionSearchCommand.java
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

import nl.tranquilizedquality.adm.commons.business.domain.Release;
import nl.tranquilizedquality.adm.commons.domain.PagingSearchCommand;

/**
 * Search criteria used to search for release executions.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 6, 2012
 * 
 */
public class ReleaseExecutionSearchCommand extends PagingSearchCommand {

	/**
	 * Unique identifier for serialization.
	 */
	private static final long serialVersionUID = 5983076911681716640L;

	/** The release to retrieve the release executions for. */
	private Release release;

	public Release getRelease() {
		return release;
	}

	public void setRelease(final Release release) {
		this.release = release;
	}

}
