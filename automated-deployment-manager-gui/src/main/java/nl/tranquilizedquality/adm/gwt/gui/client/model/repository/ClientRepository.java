/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 30 aug. 2011 File: ClientRepository.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.model.repository
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
package nl.tranquilizedquality.adm.gwt.gui.client.model.repository;

import javax.persistence.Transient;

import nl.tranquilizedquality.adm.commons.business.domain.Repository;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.AbstractUpdatableBeanModel;

import com.google.code.simplestuff.annotation.BusinessField;

/**
 * Client side representation of a {@link Repository}
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 30 aug. 2011
 */
public class ClientRepository extends AbstractUpdatableBeanModel<Long> implements Repository {

    /** Unique identifier used for serialization. */
    private static final long serialVersionUID = -6562653340751679130L;

    /** The name of the repository. */
    private String name;

    /** The URL to the repository. */
    private String repositoryUrl;

    /** Determines if this repository is to be used or not. */
    private Boolean enabled;

    /**
     * The unique name that is used in Sonatype Nexus to identify a repository e.g. publi-snapshots,
     * releases, snapshots etc.
     */
    @BusinessField
    private String repositoryId;

    @Override
    public Long getId() {
        return id;
    }

    /**
     * @see nl.tranquilizedquality.adm.commons.business.domain.Repository#getName()
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
     * @see nl.tranquilizedquality.adm.commons.business.domain.Repository#getRepositoryUrl()
     */
    @Override
    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    /**
     * @param repositoryUrl
     *        the repositoryUrl to set
     */
    public void setRepositoryUrl(final String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    @Override
    public Boolean isEnabled() {
        return enabled;
    }

    /**
     * @return the enabled
     */
    @Transient
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * @param enabled
     *        the enabled to set
     */
    public void setEnabled(final Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(final String repositoryId) {
        this.repositoryId = repositoryId;
    }

    @Override
    public void copy(final DomainObject<Long> object) {
        super.copy(object);

        if (object instanceof Repository) {
            final Repository repo = (Repository) object;

            this.enabled = repo.isEnabled();
            this.name = repo.getName();
            this.repositoryUrl = repo.getRepositoryUrl();
            this.repositoryId = repo.getRepositoryId();
        }
    }

}
