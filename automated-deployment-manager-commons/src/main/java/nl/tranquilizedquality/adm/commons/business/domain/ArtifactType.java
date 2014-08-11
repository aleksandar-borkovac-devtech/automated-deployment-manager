/**
 * <pre>
 * Project: automated-deployment-manager-commons Created on: 3 jun. 2011 File: ArtifactType.java
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
 * All available artifact types.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 */
public enum ArtifactType {

    /** A web archive. */
    WAR(".war"),

    /** Tape archive. */
    TAR(".tar"),

    /** Zip file. */
    ZIP(".zip"),

    /** Java Archive. */
    JAR(".jar"),

    /** Enterprise Archive. */
    EAR(".ear"),

    /** Tape arhive gzipped. */
    TAR_GZIP(".tar.gz");

    /** The file exention. */
    private String extention;

    /**
     * Constructor taking the file extention.
     * 
     * @param extention
     *            The file extentions to use.
     */
    private ArtifactType(final String extention) {
        this.extention = extention;
    }

    /**
     * @return the extention
     */
    public String getExtention() {
        return extention;
    }

}
