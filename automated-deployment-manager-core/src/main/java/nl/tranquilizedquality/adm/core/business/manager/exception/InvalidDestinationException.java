/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 11 sep. 2011 File: InvalidDestinationException.java
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
 * Exception that is thrown when an invalid desitnation is detected.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 11 sep. 2011
 */
public class InvalidDestinationException extends RuntimeException {

    /**
     * Unique identifier used for serialization.
     */
    private static final long serialVersionUID = -5687497719781691758L;

    /**
     * Default constructor.
     */
    public InvalidDestinationException() {
    }

    /**
     * Constructor where you can specify the error message.
     * 
     * @param msg
     *            The error message that will be used.
     */
    public InvalidDestinationException(final String msg) {
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
    public InvalidDestinationException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

}
