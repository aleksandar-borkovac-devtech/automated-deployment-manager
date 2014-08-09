/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 5 jun. 2011 File: UnsupportedArtifactTypeException.java
 * Package: nl.tranquilizedquality.adm.core.business.deployer.exception
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
package nl.tranquilizedquality.adm.core.business.deployer.exception;

import nl.tranquilizedquality.adm.commons.business.domain.ArtifactType;

/**
 * Exception thrown when a request is done with an {@link ArtifactType} that is
 * not supported.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 5 jun. 2011
 */
public class UnsupportedArtifactTypeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6112675757448786006L;

	/**
	 * Default constructor.
	 */
	public UnsupportedArtifactTypeException() {
	}

	/**
	 * Constructor where you can specify the error message.
	 * 
	 * @param msg
	 *            The error message that will be used.
	 */
	public UnsupportedArtifactTypeException(final String msg) {
		super(msg);
	}

	/**
	 * Constructor where you can specify the error message and the cause of this
	 * exception.
	 * 
	 * @param msg
	 *            The error message that will be used.
	 * @param cause
	 *            The {@link Throwable} that caused this exception.
	 */
	public UnsupportedArtifactTypeException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

}
