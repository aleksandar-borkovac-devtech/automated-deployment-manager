/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 4 jun. 2011 File: FailedToConnectException.java
 * Package: nl.tranquilizedquality.adm.core.business.deployer.connector.exception
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
package nl.tranquilizedquality.adm.core.business.deployer.connector.exception;

/**
 * Exception thrown when the connection to the host failed.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 4 jun. 2011
 */
public class FailedToConnectException extends RuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5058295679954355407L;

    /**
     * Default constructor.
     */
    public FailedToConnectException() {
    }

    /**
     * Constructor where you can specify the error message.
     * 
     * @param msg
     *            The error message that will be used.
     */
    public FailedToConnectException(final String msg) {
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
    public FailedToConnectException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

}
