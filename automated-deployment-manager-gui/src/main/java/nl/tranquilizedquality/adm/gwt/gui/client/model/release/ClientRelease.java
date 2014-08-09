/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 17 sep. 2011 File: ClientRelease.java
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.Release;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseStatus;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.AbstractUpdatableBeanModel;
import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenArtifact;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUserGroup;

import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * Client side representation of a release.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 17 sep. 2011
 */
public class ClientRelease extends AbstractUpdatableBeanModel<Long> implements Release {

    /**
     * Unique identifier used for serialization.
     */
    private static final long serialVersionUID = 7996844992537036541L;

    /** The name of the release. */
    private String name;

    /** The release date. */
    private Date releaseDate;

    /** The status of the release. */
    private ReleaseStatus status;

    /** The number of times the release was released. */
    private Integer releaseCount;

    /** The number of times the release failed to release. */
    private Integer releaseFailureCount;

    /** Date on which this release was released the last time. */
    private Date lastReleasedDate;

    /** The group where this release belongs to */
    private UserGroup userGroup;

    /** Determines if the release is archived or not. */
    private boolean archived;

    /** The artifacts that should be deployed. */
    private List<MavenArtifact> artifacts;

    /**
     * Default constructor.
     */
    public ClientRelease() {
        status = ReleaseStatus.DRAFT;
        artifacts = new ArrayList<MavenArtifact>();
        releaseFailureCount = 0;
        releaseCount = 0;
    }

    @Override
    public Long getId() {
        return id;
    }

    /**
     * @see nl.tranquilizedquality.adm.commons.business.domain.Release#getName()
     */
    @Override
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
     * @see nl.tranquilizedquality.adm.commons.business.domain.Release#getReleaseDate()
     */
    @Override
    public Date getReleaseDate() {
        return releaseDate;
    }

    /**
     * @param releaseDate
     *        the releaseDate to set
     */
    public void setReleaseDate(final Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    /**
     * @return the artifacts
     */
    @Override
    public List<MavenArtifact> getArtifacts() {
        return artifacts;
    }

    /**
     * @param artifacts
     *        the artifacts to set
     */
    @Override
    public void setArtifacts(final List<MavenArtifact> artifacts) {
        this.artifacts = artifacts;
    }

    /**
     * @return the status
     */
    @Override
    public ReleaseStatus getStatus() {
        return status;
    }

    /**
     * @param status
     *        the status to set
     */
    @Override
    public void setStatus(final ReleaseStatus status) {
        this.status = status;
    }

    /**
     * @return the releaseCount
     */
    @Override
    public Integer getReleaseCount() {
        return releaseCount;
    }

    /**
     * @param releaseCount
     *        the releaseCount to set
     */
    public void setReleaseCount(final Integer releaseCount) {
        this.releaseCount = releaseCount;
    }

    /**
     * @return the releaseFailureCount
     */
    @Override
    public Integer getReleaseFailureCount() {
        return releaseFailureCount;
    }

    /**
     * @param releaseFailureCount
     *        the releaseFailureCount to set
     */
    public void setReleaseFailureCount(final Integer releaseFailureCount) {
        this.releaseFailureCount = releaseFailureCount;
    }

    /**
     * @return the lastReleasedDate
     */
    @Override
    public Date getLastReleasedDate() {
        return lastReleasedDate;
    }

    @Override
    public void addReleasedCount() {
        if (this.releaseCount == null) {
            this.releaseCount = 1;
        } else {
            this.releaseCount += 1;
        }
    }

    @Override
    public void addReleaseFailureCount() {
        if (this.releaseFailureCount == null) {
            this.releaseFailureCount = 1;
        } else {
            this.releaseFailureCount += 1;
        }
    }

    @Override
    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(final UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    @Override
    public boolean isArchived() {
        return archived;
    }

    public void setArchived(final boolean archived) {
        this.archived = archived;
    }

    /**
     * @param lastReleasedDate
     *        the lastReleasedDate to set
     */
    @Override
    public void setLastReleasedDate(final Date lastReleasedDate) {
        this.lastReleasedDate = lastReleasedDate;
    }

    public String getLastReleasedDateFormatted() {
        final Date lastReleasedDate = getLastReleasedDate();
        if (lastReleasedDate != null) {
            final DateTimeFormat format = DateTimeFormat.getShortDateTimeFormat();
            return format.format(lastReleasedDate);
        }
        return "N/A";
    }

    public void shallowCopy(final Release release) {
        super.copy(release);

        this.lastReleasedDate = release.getLastReleasedDate();
        this.name = release.getName();
        this.releaseCount = release.getReleaseCount();
        this.releaseDate = release.getReleaseDate();
        this.releaseFailureCount = release.getReleaseFailureCount();
        this.status = release.getStatus();
        this.archived = release.isArchived();

        final UserGroup newUserGroup = release.getUserGroup();
        final ClientUserGroup userGroup = new ClientUserGroup();
        userGroup.shallowCopy(newUserGroup);
        this.userGroup = userGroup;

        this.artifacts = new ArrayList<MavenArtifact>();
    }

    @Override
    public void copy(final DomainObject<Long> object) {

        if (object instanceof Release) {
            final Release release = (Release) object;

            shallowCopy(release);

            final List<MavenArtifact> artifacts = release.getArtifacts();
            for (final MavenArtifact mavenArtifact : artifacts) {
                final ClientMavenArtifact artifact = new ClientMavenArtifact();
                artifact.copy(mavenArtifact);

                this.artifacts.add(artifact);
            }
        }
    }

    @Override
    public boolean equals(final Object obj) {

        if (obj instanceof ClientRelease) {
            final ClientRelease release = (ClientRelease) obj;

            if (this.id != null && !this.id.equals(release.getId())) {
                return false;
            } else if (this.id == null && release.getId() != null) {
                return false;
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + (id == null ? 0 : id.intValue());

        return result;
    }

}
