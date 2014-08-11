/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 8 okt. 2011 File: ClientMavenArtifactSnapshot.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.model.release
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
package nl.tranquilizedquality.adm.gwt.gui.client.model.release;

import nl.tranquilizedquality.adm.commons.business.domain.ArtifactType;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifactSnapshot;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecution;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.AbstractInsertableBeanModel;

/**
 * Client side representation of a {@link MavenArtifactSnapshot}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 8 okt. 2011
 */
public class ClientMavenArtifactSnapshot extends AbstractInsertableBeanModel<Long> implements MavenArtifactSnapshot {

    /**
     * Unique identifier used for serialization.
     */
    private static final long serialVersionUID = 1118914522625363455L;

    /** The name of the Maven module. */
    private String name;

    /** The type of artifact. */
    private ArtifactType type;

    /** The maven group of the module. */
    private String group;

    /** The maven artifact id. */
    private String artifactId;

    /** The version of the module. */
    private String version;

    /**
     * Is used for distribution packages since they have a suffix to uniquely
     * identify the distribution package.
     */
    private String identifier;

    /** The rank this artifact had when the release was done. */
    private Integer rank;

    /** The release where this artifact is part of. */
    private ReleaseExecution releaseExecution;

    @Override
    public Long getId() {
        return id;
    }

    /**
     * @see nl.tranquilizedquality.adm.commons.business.domain.MavenModule#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the type
     */
    @Override
    public ArtifactType getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(final ArtifactType type) {
        this.type = type;
    }

    /**
     * @see nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact#getGroup()
     */
    @Override
    public String getGroup() {
        return group;
    }

    /**
     * @param group
     *            the group to set
     */
    public void setGroup(final String group) {
        this.group = group;
    }

    /**
     * @see nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact#getArtifactId()
     */
    @Override
    public String getArtifactId() {
        return artifactId;
    }

    /**
     * @param artifactId
     *            the artifactId to set
     */
    public void setArtifactId(final String artifactId) {
        this.artifactId = artifactId;
    }

    /**
     * @see nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact#getVersion()
     */
    @Override
    public String getVersion() {
        return version;
    }

    /**
     * @param version
     *            the version to set
     */
    public void setVersion(final String version) {
        this.version = version;
    }

    /**
     * @return the release
     */
    @Override
    public ReleaseExecution getReleaseExecution() {
        return releaseExecution;
    }

    /**
     * @param release
     *            the release to set
     */
    public void setReleaseExecution(final ReleaseExecution release) {
        this.releaseExecution = release;
    }

    /**
     * @return the rank
     */
    @Override
    public Integer getRank() {
        return rank;
    }

    /**
     * @param rank
     *            the rank to set
     */
    public void setRank(final Integer rank) {
        this.rank = rank;
    }

    /**
     * @return the identifier
     */
    @Override
    public String getIdentifier() {
        return identifier;
    }

    /**
     * @param identifier
     *            the identifier to set
     */
    public void setIdentifier(final String identifier) {
        this.identifier = identifier;
    }

    @Override
    public void copy(final DomainObject<Long> object) {
        super.copy(object);

        if (object instanceof MavenArtifactSnapshot) {
            final MavenArtifactSnapshot artifact = (MavenArtifactSnapshot) object;

            this.version = artifact.getVersion();
            this.artifactId = artifact.getArtifactId();
            this.group = artifact.getGroup();
            this.name = artifact.getName();
            this.type = artifact.getType();
            this.rank = artifact.getRank();
            this.identifier = artifact.getIdentifier();

            final ReleaseExecution releaseExecution = artifact.getReleaseExecution();
            final ClientReleaseExecution newReleaseExecution = new ClientReleaseExecution();
            newReleaseExecution.shallowCopy(releaseExecution);
            this.releaseExecution = newReleaseExecution;
        }

    }

}
