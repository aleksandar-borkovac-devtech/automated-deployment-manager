/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 17 sep. 2011 File: InvalidReleaseException.java
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

import nl.tranquilizedquality.adm.commons.business.domain.Release;

/**
 * Exception that will be thrown when an invalid {@link Release} is detected.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 17 sep. 2011
 */
public class InvalidReleaseException extends RuntimeException {

    /**
     * Unique identifier used for serialization.
     */
    private static final long serialVersionUID = 6095232259058186272L;

    /**
     * Default constructor.
     */
    public InvalidReleaseException() {
    }

    /**
     * Constructor where you can specify the error message.
     * 
     * @param msg
     *            The error message that will be used.
     */
    public InvalidReleaseException(final String msg) {
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
    public InvalidReleaseException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

}
