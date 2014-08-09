/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 3 jun. 2011 File: NoDeployerFoundException.java
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
 * Exception thrown when no deployer could be found.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 */
public class NoDeployerFoundException extends RuntimeException {

	/**
	 * Unique identifier used for serialization.
	 */
	private static final long serialVersionUID = 4267135796428751566L;

	/**
	 * Default constructor.
	 */
	public NoDeployerFoundException() {
	}

	/**
	 * Constructor where you can specify the error message.
	 * 
	 * @param msg
	 *            The error message that will be used.
	 */
	public NoDeployerFoundException(final String msg) {
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
	public NoDeployerFoundException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

}
