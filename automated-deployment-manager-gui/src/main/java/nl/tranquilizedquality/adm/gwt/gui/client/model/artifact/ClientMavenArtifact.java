/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 17 sep. 2011 File: ClientMavenArtifact.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.model.artifact
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
package nl.tranquilizedquality.adm.gwt.gui.client.model.artifact;

import java.util.Date;

import javax.persistence.Transient;

import nl.tranquilizedquality.adm.commons.business.domain.ArtifactType;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.MavenModule;
import nl.tranquilizedquality.adm.commons.business.domain.Release;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseStatus;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.AbstractUpdatableBeanModel;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientRelease;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUserGroup;

/**
 * Client side representation of a {@link MavenArtifact}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 17 sep. 2011
 */
public class ClientMavenArtifact extends AbstractUpdatableBeanModel<Long> implements MavenArtifact {

    /**
     * Unique identifier used for serialization.
     */
    private static final long serialVersionUID = -2425534328253236200L;

    /** The version of the module. */
    private String version;

    /** The parent module. */
    private MavenModule parentModule;

    /** The release where this artifact is part of. */
    private Release release;

    /** The file name. */
    private String file;

    /** Determines if the target system should be stopped before deployment. */
    private Boolean targetSystemShutdown;

    /** Determines if the target system should be started up after deployment. */
    private Boolean targetSystemStartup;

    /** The ranking order of this artifact in a release. */
    private Integer rank;

    /** The group where this maven artifacts belongs to */
    private UserGroup userGroup;

    @Override
    public Long getId() {
        return id;
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
     *        the version to set
     */
    public void setVersion(final String version) {
        this.version = version;
    }

    /**
     * @see nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact#getParentModule()
     */
    @Override
    public MavenModule getParentModule() {
        return parentModule;
    }

    /**
     * @param parentModule
     *        the parentModule to set
     */
    public void setParentModule(final MavenModule parentModule) {
        this.parentModule = parentModule;
    }

    /**
     * @return the release
     */
    @Override
    public Release getRelease() {
        return release;
    }

    /**
     * @param release
     *        the release to set
     */
    public void setRelease(final Release release) {
        this.release = release;
    }

    /**
     * @return the file
     */
    @Override
    @Transient
    public String getFile() {
        return file;
    }

    /**
     * @param file
     *        the file to set
     */
    @Override
    public void setFile(final String file) {
        this.file = file;
    }

    /**
     * Retrieves the name of the module.
     * 
     * @return Returns a {@link String} representation of the module name.
     */
    public String getModuleName() {
        if (parentModule != null) {
            return parentModule.getName();
        }
        return "";
    }

    /**
     * Retrieve the type of artifact this module is.
     * 
     * @return Returns the {@link ArtifactType}.
     */
    public ArtifactType getType() {
        if (parentModule != null) {
            return parentModule.getType();
        }
        return null;
    }

    /**
     * Retrieves the group this artifact belongs to.
     * 
     * @return Returns a {@link String} representation of the group.
     */
    public String getGroup() {
        if (parentModule != null) {
            return parentModule.getGroup();
        }
        return "";
    }

    /**
     * Retrieves the artifact id.
     * 
     * @return Returns a {@link String} representation of the artifact id.
     */
    public String getArtifactId() {
        if (parentModule != null) {
            return parentModule.getArtifactId();
        }
        return "No artifacts available";
    }

    /**
     * Retrieves the name of the name of the release.
     * 
     * @return Returns a {@link String} representation of the name.
     */
    public String getReleaseName() {
        if (release != null) {
            return release.getName();
        }
        return "No Release";
    }

    public Date getReleaseDate() {
        if (release != null) {
            return release.getReleaseDate();
        }
        return null;
    }

    public ReleaseStatus getReleaseStatus() {
        if (release != null) {
            return release.getStatus();
        }
        return null;
    }

    public int getReleaseCount() {
        if (release != null) {
            return release.getReleaseCount();
        }
        return 0;
    }

    /**
     * @return the targetSystemShutdown
     */
    @Override
    public Boolean getTargetSystemShutdown() {
        return targetSystemShutdown;
    }

    /**
     * @param targetSystemShutdown
     *        the targetSystemShutdown to set
     */
    public void setTargetSystemShutdown(final Boolean targetSystemShutdown) {
        this.targetSystemShutdown = targetSystemShutdown;
    }

    /**
     * @return the targetSystemStartup
     */
    @Override
    public Boolean getTargetSystemStartup() {
        return targetSystemStartup;
    }

    /**
     * @param targetSystemStartup
     *        the targetSystemStartup to set
     */
    public void setTargetSystemStartup(final Boolean targetSystemStartup) {
        this.targetSystemStartup = targetSystemStartup;
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
     *        the rank to set
     */
    @Override
    public void setRank(final Integer rank) {
        this.rank = rank;
    }

    @Override
    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(final UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    @Override
    public void copy(final DomainObject<Long> object) {
        super.copy(object);

        if (object instanceof MavenArtifact) {
            final MavenArtifact artifact = (MavenArtifact) object;

            this.file = artifact.getFile();
            this.version = artifact.getVersion();
            this.targetSystemShutdown = artifact.getTargetSystemShutdown();
            this.targetSystemStartup = artifact.getTargetSystemStartup();
            this.rank = artifact.getRank();

            final MavenModule module = artifact.getParentModule();
            this.parentModule = new ClientMavenModule();
            this.parentModule.copy(module);

            /*
             * Call shallow copy to prevent infinite loop.
             */
            final Release release = artifact.getRelease();
            final ClientRelease newRelease = new ClientRelease();
            newRelease.shallowCopy(release);
            this.release = newRelease;

            final UserGroup newUserGroup = artifact.getUserGroup();
            this.userGroup = new ClientUserGroup();
            this.userGroup.copy(newUserGroup);
        }

    }

}
