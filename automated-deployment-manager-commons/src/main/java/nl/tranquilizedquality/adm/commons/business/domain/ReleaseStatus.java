/**
 * <pre>
 * Project: automated-deployment-manager-commons Created on: 4 sep. 2011 File: ReleaseStatus.java
 * Package: nl.tranquilizedquality.adm.commons.business.domain
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
package nl.tranquilizedquality.adm.commons.business.domain;

/**
 * All available statuses of a {@link Release}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 4 sep. 2011
 */
public enum ReleaseStatus {

    /** Release was successfully performed. */
    SUCCESS,

    /** Errors occurred during the release. */
    FAILED,

    /** Release is in the process of releasing. */
    RELEASING,

    /** Release is still to be fully configured so it can't be released yet. */
    DRAFT,

    /** Ready to be released. */
    READY;

}
