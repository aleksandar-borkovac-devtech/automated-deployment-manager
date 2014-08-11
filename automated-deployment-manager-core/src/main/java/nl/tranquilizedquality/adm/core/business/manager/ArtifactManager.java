/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 3 jun. 2011 File: ArtifactManager.java
 * Package: nl.tranquilizedquality.adm.core.business.manager
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
package nl.tranquilizedquality.adm.core.business.manager;

import java.io.File;

import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;

/**
 * Manager that manages artifacts.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 */
public interface ArtifactManager {

    /**
     * Retrieves the artifact from the repository.
     * 
     * @param artifact
     *            The artifact that needs to be retrieved.
     * @param release
     *            The release name used for storing artifacts in a sub
     *            directory.
     * @return Returns a {@link File} which points to the retrieved artifact.
     */
    File retrieveArtifact(MavenArtifact artifact, String release);

}
