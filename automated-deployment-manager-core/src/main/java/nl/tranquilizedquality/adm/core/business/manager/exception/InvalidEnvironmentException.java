/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 11 sep. 2011 File: InvalidEnvironmentException.java
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

import nl.tranquilizedquality.adm.commons.business.domain.Environment;

/**
 * Exception that will be thrown when an invalid {@link Environment} is
 * detected.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 11 sep. 2011
 */
public class InvalidEnvironmentException extends RuntimeException {

    /**
     * Unique identifier used for serialization.
     */
    private static final long serialVersionUID = 6420577417091423859L;

    /**
     * Default constructor.
     */
    public InvalidEnvironmentException() {
    }

    /**
     * Constructor where you can specify the error message.
     * 
     * @param msg
     *            The error message that will be used.
     */
    public InvalidEnvironmentException(final String msg) {
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
    public InvalidEnvironmentException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

}
