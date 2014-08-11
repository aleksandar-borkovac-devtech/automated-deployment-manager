/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 5 jun. 2011 File: UnsupportedProtocolException.java
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

/**
 * Exception thrown when a request is done for an unsupported protocol.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 5 jun. 2011
 */
public class UnsupportedProtocolException extends RuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2409066931440572999L;

    /**
     * Default constructor.
     */
    public UnsupportedProtocolException() {
    }

    /**
     * Constructor where you can specify the error message.
     * 
     * @param msg
     *            The error message that will be used.
     */
    public UnsupportedProtocolException(final String msg) {
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
    public UnsupportedProtocolException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

}
