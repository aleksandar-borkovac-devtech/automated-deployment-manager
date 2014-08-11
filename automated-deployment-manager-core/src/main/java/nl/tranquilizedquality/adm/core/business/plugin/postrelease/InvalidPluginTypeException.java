/*
 * @(#)InvalidPluginTypeException.java 21 mrt. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.core.business.plugin.postrelease;

/**
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 21 mrt. 2013
 */
public class InvalidPluginTypeException extends RuntimeException {

    /**
     * Default constructor.
     */
    public InvalidPluginTypeException() {
    }

    /**
     * Constructor where you can specify the error message.
     * 
     * @param msg
     *            The error message that will be used.
     */
    public InvalidPluginTypeException(final String msg) {
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
    public InvalidPluginTypeException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

}
