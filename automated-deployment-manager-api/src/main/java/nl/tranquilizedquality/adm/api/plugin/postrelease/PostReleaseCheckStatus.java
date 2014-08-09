/*
 * @(#)PostReleaseCheckStatus.java 20 mrt. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.api.plugin.postrelease;

/**
 * The available statuses for a release check.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 20 mrt. 2013
 */
public enum PostReleaseCheckStatus {

    /** Determines that the post release check passed. */
    PASSED,

    /** The post release check failed. */
    FAILED;

}
