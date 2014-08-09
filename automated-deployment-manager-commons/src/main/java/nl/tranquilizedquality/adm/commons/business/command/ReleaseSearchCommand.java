/**
 * <pre>
 * Project: automated-deployment-manager-commons Created on: 22 okt. 2012 File: ReleaseSearchCommand.java
 * Package: nl.tranquilizedquality.adm.commons.business.command
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
package nl.tranquilizedquality.adm.commons.business.command;

import java.util.Date;

import nl.tranquilizedquality.adm.commons.business.domain.ArtifactType;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseStatus;

/**
 * The search criteria of a release.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 22 okt. 2012
 */
public class ReleaseSearchCommand extends AbstractPagingUserGroupSearchCommand {

    /**
     * Unique identifier for serialization.
     */
    private static final long serialVersionUID = 115304524010938474L;

    /** The name of the Maven module. */
    private String name;

    /** The type of artifact. */
    private ArtifactType type;

    /** The maven group of the module. */
    private String group;

    /** The maven artifact id. */
    private String artifactId;

    /** The name of the release. */
    private String releaseName;

    /** The status of the release. */
    private ReleaseStatus status;

    /** The release date start period. */
    private Date releaseDateStart;

    /** The release date end period. */
    private Date releaseDateEnd;

    /** Determines if archived releases should be retrieved. */
    private boolean archived;

    /**
     * @return the name
     */
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
     * @return the status
     */
    public ReleaseStatus getStatus() {
        return status;
    }

    /**
     * @param status
     *        the status to set
     */
    public void setStatus(final ReleaseStatus status) {
        this.status = status;
    }

    /**
     * @return the releaseDateStart
     */
    public Date getReleaseDateStart() {
        return releaseDateStart;
    }

    /**
     * @param releaseDateStart
     *        the releaseDateStart to set
     */
    public void setReleaseDateStart(final Date releaseDateStart) {
        this.releaseDateStart = releaseDateStart;
    }

    /**
     * @return the releaseDateEnd
     */
    public Date getReleaseDateEnd() {
        return releaseDateEnd;
    }

    /**
     * @param releaseDateEnd
     *        the releaseDateEnd to set
     */
    public void setReleaseDateEnd(final Date releaseDateEnd) {
        this.releaseDateEnd = releaseDateEnd;
    }

    /**
     * @return the type
     */
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
     * @return the group
     */
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
     * @return the artifactId
     */
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
     * @return the releaseName
     */
    public String getReleaseName() {
        return releaseName;
    }

    /**
     * @param releaseName
     *        the releaseName to set
     */
    public void setReleaseName(final String releaseName) {
        this.releaseName = releaseName;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(final boolean archived) {
        this.archived = archived;
    }

}
