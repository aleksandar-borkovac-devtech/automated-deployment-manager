/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 8 okt. 2011 File: ReleaseSnapshotFactoryImpl.java
 * Package: nl.tranquilizedquality.adm.core.business.manager.impl
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
package nl.tranquilizedquality.adm.core.business.manager.impl;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.ArtifactType;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifactSnapshot;
import nl.tranquilizedquality.adm.commons.business.domain.MavenModule;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecution;
import nl.tranquilizedquality.adm.core.business.manager.ReleaseSnapshotFactory;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateMavenArtifactSnapshot;

/**
 * Factory that can create release snapshots from a release.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 8 okt. 2011
 */
public class ReleaseSnapshotFactoryImpl implements ReleaseSnapshotFactory {

    @Override
    public List<MavenArtifactSnapshot> createSnapshot(final ReleaseExecution execution, final List<MavenArtifact> artifacts) {
        /*
         * Setup artifacts.
         */
        final List<MavenArtifactSnapshot> snapshotArtifacts = new ArrayList<MavenArtifactSnapshot>();
        for (final MavenArtifact mavenArtifact : artifacts) {
            final MavenModule parentModule = mavenArtifact.getParentModule();
            final String artifactId = parentModule.getArtifactId();
            final String group = parentModule.getGroup();
            final ArtifactType type = parentModule.getType();
            final String name = parentModule.getName();
            final String version = mavenArtifact.getVersion();
            final Integer rank = mavenArtifact.getRank();
            final String identifier = parentModule.getIdentifier();

            final HibernateMavenArtifactSnapshot snapshotArtifact = new HibernateMavenArtifactSnapshot();
            snapshotArtifact.setArtifactId(artifactId);
            snapshotArtifact.setGroup(group);
            snapshotArtifact.setType(type);
            snapshotArtifact.setName(name);
            snapshotArtifact.setReleaseExecution(execution);
            snapshotArtifact.setVersion(version);
            snapshotArtifact.setRank(rank);
            snapshotArtifact.setIdentifier(identifier);

            snapshotArtifacts.add(snapshotArtifact);
        }

        return snapshotArtifacts;
    }

}
