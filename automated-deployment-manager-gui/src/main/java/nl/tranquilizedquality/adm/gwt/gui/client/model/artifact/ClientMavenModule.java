/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 17 sep. 2011 File: ClientMavenModule.java
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

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.ArtifactType;
import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.business.domain.MavenModule;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.AbstractUpdatableBeanModel;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientDestination;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUserGroup;

/**
 * Client side representation of a {@link MavenModule}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 17 sep. 2011
 */
public class ClientMavenModule extends AbstractUpdatableBeanModel<Long> implements MavenModule {

    /**
     * Unique identifier used for serialization.
     */
    private static final long serialVersionUID = 5984415321880787465L;

    /** The name of the Maven module. */
    private String name;

    /** The type of artifact. */
    private ArtifactType type;

    /** The maven group of the module. */
    private String group;

    /** The maven artifact id. */
    private String artifactId;

    /** Determines if the target system should be stopped before deployment. */
    private Boolean targetSystemShutdown;

    /** Determines if the target system should be started up after deployment. */
    private Boolean targetSystemStartup;

    /** The destination where the artifact should be deployed to. */
    private List<Destination> destinations;

    /**
     * Is used for distribution packages since they have a suffix to uniquely
     * identify the distribution package.
     */
    private String identifier;

    /** The group where this maven artifacts belongs to */
    private UserGroup userGroup;

    /** The modules where this module depend on for succesful deployment. */
    private List<MavenModule> deploymentDependencies;

    /**
     * Default constructor.
     */
    public ClientMavenModule() {
        destinations = new ArrayList<Destination>();
        targetSystemShutdown = false;
        targetSystemStartup = false;
    }

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
     *        the name to set
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
     *        the type to set
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
     *        the group to set
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
     *        the artifactId to set
     */
    public void setArtifactId(final String artifactId) {
        this.artifactId = artifactId;
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
     * @return the destination
     */
    @Override
    public List<Destination> getDestinations() {
        return destinations;
    }

    /**
     * @param destination
     *        the destination to set
     */
    @Override
    public void setDestinations(final List<Destination> destinations) {
        this.destinations = destinations;
    }

    @Override
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
    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(final UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    @Override
    public List<MavenModule> getDeploymentDependencies() {
        return deploymentDependencies;
    }

    @Override
    public void setDeploymentDependencies(final List<MavenModule> deploymentDependencies) {
        this.deploymentDependencies = deploymentDependencies;
    }

    public void shallowCopy(final MavenModule module) {
        super.copy(module);

        this.name = module.getName();
        this.type = module.getType();
        this.artifactId = module.getArtifactId();
        this.group = module.getGroup();
        this.targetSystemShutdown = module.getTargetSystemShutdown();
        this.targetSystemStartup = module.getTargetSystemStartup();
        this.identifier = module.getIdentifier();

        final UserGroup newUserGroup = module.getUserGroup();
        this.userGroup = new ClientUserGroup();
        this.userGroup.copy(newUserGroup);
        this.destinations = new ArrayList<Destination>();
        this.deploymentDependencies = new ArrayList<MavenModule>();
    }

    @Override
    public void copy(final DomainObject<Long> object) {
        if (object instanceof MavenModule) {
            final MavenModule module = (MavenModule) object;
            shallowCopy(module);

            final List<Destination> destinations = module.getDestinations();
            if (destinations != null) {
                for (final Destination destination : destinations) {
                    final ClientDestination newDestination = new ClientDestination();
                    newDestination.copy(destination);
                    this.destinations.add(newDestination);
                }
            }

            final List<MavenModule> dependencies = module.getDeploymentDependencies();
            if (dependencies != null) {
                for (final MavenModule mavenModule : dependencies) {
                    final ClientMavenModule newMavenModule = new ClientMavenModule();
                    newMavenModule.shallowCopy(mavenModule);
                    this.deploymentDependencies.add(newMavenModule);
                }
            }
        }
    }

    @Override
    public boolean equals(final Object obj) {

        if (obj instanceof ClientMavenModule) {
            final ClientMavenModule module = (ClientMavenModule) obj;

            if (this.id != null && !this.id.equals(module.getId())) {
                return false;
            } else if (this.id == null && module.getId() != null) {
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
