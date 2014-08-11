/*
 * @(#)FailedToExtractContextFileException.java 21 mrt. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.core.business.plugin;

/**
 * Exception thrown when for some reason the plugin context file cannot be
 * extracted from the plugin JAR.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 21 mrt. 2013
 */
public class FailedToExtractContextFileException extends RuntimeException {

    /**
     * Default constructor.
     */
    public FailedToExtractContextFileException() {
    }

    /**
     * Constructor where you can specify the error message.
     * 
     * @param msg
     *            The error message that will be used.
     */
    public FailedToExtractContextFileException(final String msg) {
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
    public FailedToExtractContextFileException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

}
