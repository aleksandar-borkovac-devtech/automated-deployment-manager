/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 6 okt. 2011 File: HibernateMavenArtifactSnapshot.java
 * Package: nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean
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
package nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import nl.tranquilizedquality.adm.commons.business.domain.ArtifactType;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifactSnapshot;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecution;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.hibernate.bean.AbstractInsertableDomainObject;

import org.hibernate.annotations.ForeignKey;

import com.google.code.simplestuff.annotation.BusinessField;

/**
 * Hibernate implementation of a {@link MavenArtifactSnapshot}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 6 okt. 2011
 */
@Entity()
@Table(name = "MAVEN_ARTIFACT_SNAPSHOTS", schema = "ADM")
public class HibernateMavenArtifactSnapshot extends AbstractInsertableDomainObject<Long> implements MavenArtifactSnapshot {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3608076331420877341L;

    /** The name of the Maven module. */
    @BusinessField
    private String name;

    /** The type of artifact. */
    @BusinessField
    private ArtifactType type;

    /** The maven group of the module. */
    @BusinessField
    private String group;

    /** The maven artifact id. */
    @BusinessField
    private String artifactId;

    /** The version of the module. */
    @BusinessField
    private String version;

    /**
     * Is used for distribution packages since they have a suffix to uniquely
     * identify the distribution package.
     */
    @BusinessField
    private String identifier;

    /** The rank this artifact had when the release was done. */
    @BusinessField
    private Integer rank;

    /** The release where this artifact is part of. */
    @BusinessField
    private ReleaseExecution releaseExecution;

    @Override
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "MAVEN_ARTIFACT_SNAPSHOTS_SEQ_GEN")
    @SequenceGenerator(name = "MAVEN_ARTIFACT_SNAPSHOTS_SEQ_GEN", initialValue = 1, allocationSize = 1, sequenceName = "ADM.MAVEN_ARTIFACT_SNAPSHOTS_SEQ")
    public Long getId() {
        return id;
    }

    /**
     * @see nl.tranquilizedquality.adm.commons.business.domain.MavenModule#getName()
     */
    @Override
    @Column(name = "NAME", length = 200, nullable = false, unique = false)
    public String getName() {
        return name;
    }

    /**
     * @param name
     *        the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the type
     */
    @Override
    @Enumerated(EnumType.STRING)
    @Column(name = "ARTIFACT_TYPE", nullable = false, length = 10)
    public ArtifactType getType() {
        return type;
    }

    /**
     * @param type
     *        the type to set
     */
    public void setType(final ArtifactType type) {
        this.type = type;
    }

    /**
     * @see nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact#getGroup()
     */
    @Override
    @Column(name = "MAVEN_GROUP", nullable = false, length = 200)
    public String getGroup() {
        return group;
    }

    /**
     * @param group
     *        the group to set
     */
    public void setGroup(final String group) {
        this.group = group;
    }

    /**
     * @see nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact#getArtifactId()
     */
    @Override
    @Column(name = "MAVEN_ARTIFACT_ID", nullable = false, length = 200)
    public String getArtifactId() {
        return artifactId;
    }

    /**
     * @param artifactId
     *        the artifactId to set
     */
    public void setArtifactId(final String artifactId) {
        this.artifactId = artifactId;
    }

    /**
     * @see nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact#getVersion()
     */
    @Override
    @Column(name = "MAVEN_VERSION", length = 100, nullable = false)
    public String getVersion() {
        return version;
    }

    /**
     * @param version
     *        the version to set
     */
    public void setVersion(final String version) {
        this.version = version;
    }

    /**
     * @return the release
     */
    @Override
    @ForeignKey(name = "FK_ARTIFACT_SNAPSHOT_RELEASE_EXECUTION")
    @JoinColumn(name = "EXECUTION_ID")
    @ManyToOne(targetEntity = HibernateReleaseExecution.class, optional = false)
    public ReleaseExecution getReleaseExecution() {
        return releaseExecution;
    }

    /**
     * @param release
     *        the release to set
     */
    public void setReleaseExecution(final ReleaseExecution release) {
        this.releaseExecution = release;
    }

    /**
     * @return the rank
     */
    @Override
    @Column(name = "RANK", nullable = false)
    public Integer getRank() {
        return rank;
    }

    /**
     * @param rank
     *        the rank to set
     */
    public void setRank(final Integer rank) {
        this.rank = rank;
    }

    /**
     * @return the identifier
     */
    @Override
    @Column(name = "SUFFIX", nullable = true, length = 100)
    public String getIdentifier() {
        return identifier;
    }

    /**
     * @param identifier
     *        the identifier to set
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
            this.releaseExecution = new HibernateReleaseExecution();
            this.releaseExecution.copy(releaseExecution);
        }

    }

}
