/**
 * <pre>
 * Project: automated-deployment-manager-commons Created on: 6 okt. 2011 File: MavenArtifactSnapshot.java
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

import nl.tranquilizedquality.adm.commons.domain.InsertableDomainObject;

/**
 * Snapshot of a {@link MavenArtifact} that was deployed within a release.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 6 okt. 2011
 */
public interface MavenArtifactSnapshot extends InsertableDomainObject<Long> {

    /**
     * Retrieves the name of the module.
     * 
     * @return Returns a {@link String} representation of the module name.
     */
    String getName();

    /**
     * Retrieve the type of artifact this module is.
     * 
     * @return Returns the {@link ArtifactType}.
     */
    ArtifactType getType();

    /**
     * Retrieves the group this artifact belongs to.
     * 
     * @return Returns a {@link String} representation of the group.
     */
    String getGroup();

    /**
     * Retrieves the artifact id.
     * 
     * @return Returns a {@link String} representation of the artifact id.
     */
    String getArtifactId();

    /**
     * Retrieves the version of the artifact.
     * 
     * @return Returns a {@link String} representation of the version.
     */
    String getVersion();

    /**
     * Retrieves the release execution where this artifact is part of.
     * 
     * @return Returns the {@link ReleaseExecution}.
     */
    ReleaseExecution getReleaseExecution();

    /**
     * Retrieves the rank this artifact had when the release was done.
     * 
     * @return Returns an integer value greater than 0.
     */
    Integer getRank();

    /**
     * Retrieves the identifier for distribution pacakges since a single Maven
     * distribution module can have multiple TARGZ distribution files. The
     * distribution files are uniquely identified by a suffix in the name of the
     * artifact.
     * 
     * @return Returns a {@link String} or null if no identifier is set.
     */
    String getIdentifier();

}
