/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: Aug 23, 2012 File: fInvalidUserGroupException.java
 * Package: nl.Tranquilized Quality.adm.core.business.manager.exception
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
package nl.tranquilizedquality.adm.security.business.exception;

/**
 * Exception thrown when an invalid user group is detected.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 23, 2012
 * 
 */
public class InvalidUserGroupException extends RuntimeException {

	/**
	 * Unique identifier used for serialization.
	 */
	private static final long serialVersionUID = 3272639865245325136L;

	/**
	 * Default constructor.
	 */
	public InvalidUserGroupException() {
	}

	/**
	 * Constructor where you can specify the error message.
	 * 
	 * @param msg
	 *            The error message that will be used.
	 */
	public InvalidUserGroupException(final String msg) {
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
	public InvalidUserGroupException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

}
