/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 3 jun. 2011 File: NoRepositoryFoundException.java
 * Package: nl.tranquilizedquality.adm.core.business.manager.exception
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
package nl.tranquilizedquality.adm.core.business.manager.exception;

/**
 * Thrown when trying to delete a release that is already in use.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 * 
 */
public class ReleaseAlreadyInUseException extends RuntimeException {

	/**
	 * Unique identifier used for serialization.
	 */
	private static final long serialVersionUID = 6841806704691698406L;

	/**
	 * Default constructor.
	 */
	public ReleaseAlreadyInUseException() {
	}

	/**
	 * Constructor where you can specify the error message.
	 * 
	 * @param msg
	 *            The error message that will be used.
	 */
	public ReleaseAlreadyInUseException(final String msg) {
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
	public ReleaseAlreadyInUseException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

}
