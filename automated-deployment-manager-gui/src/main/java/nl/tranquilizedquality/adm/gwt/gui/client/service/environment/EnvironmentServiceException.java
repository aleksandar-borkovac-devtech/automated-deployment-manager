/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 11 sep. 2011 File: EnvironmentServiceException.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.service.environment
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
package nl.tranquilizedquality.adm.gwt.gui.client.service.environment;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.AbstractServiceException;

/**
 * Exception thrown by the environment service.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 11 sep. 2011
 */
public class EnvironmentServiceException extends AbstractServiceException {

    /**
     * Unique identifier used for serialization.
     */
    private static final long serialVersionUID = -7593725265833443942L;

    /**
     * Default constructor.
     */
    public EnvironmentServiceException() {
        errors = new ArrayList<String>();
    }

    /**
     * Constructor where you can specify the error message.
     * 
     * @param msg
     *            The error message that will be used.
     */
    public EnvironmentServiceException(final String msg) {
        super(msg);

        errors = new ArrayList<String>();
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
    public EnvironmentServiceException(final String msg, final Throwable cause) {
        super(msg, cause);

        errors = new ArrayList<String>();
    }

    /**
     * Constructor where you can specify the error message.
     * 
     * @param msg
     *            The error message that will be used.
     */
    public EnvironmentServiceException(final String msg, final List<String> errors) {
        super(msg);

        this.errors = errors;
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
    public EnvironmentServiceException(final String msg, final Throwable cause,
            final List<String> errors) {
        super(msg, cause);

        this.errors = errors;
    }

}
