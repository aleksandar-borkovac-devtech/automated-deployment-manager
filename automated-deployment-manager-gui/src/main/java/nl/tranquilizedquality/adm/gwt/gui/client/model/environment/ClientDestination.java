/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 11 sep. 2011 File: ClientDestination.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.model.environment
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
package nl.tranquilizedquality.adm.gwt.gui.client.model.environment;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.Deployer;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameter;
import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.business.domain.DestinationHost;
import nl.tranquilizedquality.adm.commons.business.domain.Environment;
import nl.tranquilizedquality.adm.commons.business.domain.Protocol;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.AbstractUpdatableBeanModel;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUserGroup;

import com.google.code.simplestuff.annotation.BusinessField;

/**
 * Client side representation of a {@link Destination}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 11 sep. 2011
 */
public class ClientDestination extends AbstractUpdatableBeanModel<Long> implements Destination {

    /**
     * Unique identifier used for serialization.
     */
    private static final long serialVersionUID = -2512945676118906138L;

    /** The name of the destination. */
    private String name;

    /**
     * The unique identifier of a the deployer that should be used to deploy an
     * artifact to this destination.
     */
    private Deployer deployer;

    /** The environment this destination represents. */
    private Environment environment;

    /** The server to deploy to. */
    private DestinationHost destinationHost;

    /** Location configurations on a destination server. */
    private List<DeployerParameter> deployerParameters;

    /** The group where this destination belongs to */
    private UserGroup userGroup;

    /**
     * The prefix that will be used when retrieving configuration files from a
     * distribution module.
     */
    @BusinessField
    private String prefix;

    /**
     * Default constructor.
     */
    public ClientDestination() {
        deployerParameters = new ArrayList<DeployerParameter>();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getHostName() {
        if (destinationHost != null) {
            return destinationHost.getHostName();
        }
        return "";
    }

    public Protocol getProtocol() {
        if (destinationHost != null) {
            return destinationHost.getProtocol();
        }
        return null;
    }

    /**
     * @return the deployerId
     */
    @Override
    public Deployer getDeployer() {
        return deployer;
    }

    /**
     * @param deployer
     *        the deployer to set
     */
    public void setDeployer(final Deployer deployer) {
        this.deployer = deployer;
    }

    /**
     * @return the environment
     */
    @Override
    public Environment getEnvironment() {
        return environment;
    }

    /**
     * @param environment
     *        the environment to set
     */
    public void setEnvironment(final Environment environment) {
        this.environment = environment;
    }

    public String getEnvironmentName() {
        if (this.environment != null) {
            return environment.getName();
        }
        return "";
    }

    @Override
    public DestinationHost getDestinationHost() {
        return destinationHost;
    }

    public void setDestinationHost(final DestinationHost destinationHost) {
        this.destinationHost = destinationHost;
    }

    /**
     * @return the parameters
     */
    @Override
    public List<DeployerParameter> getDeployerParameters() {
        return deployerParameters;
    }

    /**
     * @param parameters
     *        the parameters to set
     */
    @Override
    public void setDeployerParameters(final List<DeployerParameter> parameters) {
        this.deployerParameters = parameters;
    }

    @Override
    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(final UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    /**
     * @param prefix
     *        the prefix to set
     */
    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }

    public String getDestinationHostName() {
        return this.destinationHost.getHostName();
    }

    public void shallowCopy(final Destination destination) {
        super.copy(destination);

        final Deployer deployer = destination.getDeployer();
        this.deployer = new ClientDeployer();
        this.deployer.copy(deployer);
        this.name = destination.getName();
        this.prefix = destination.getPrefix();
        this.deployerParameters = new ArrayList<DeployerParameter>();

        final DestinationHost originalDestinationHost = destination.getDestinationHost();
        this.destinationHost = new ClientDestinationHost();
        this.destinationHost.copy(originalDestinationHost);

        final Environment originalEnvironment = destination.getEnvironment();
        this.environment = new ClientEnvironment();
        this.environment.copy(originalEnvironment);

        final UserGroup newUserGroup = destination.getUserGroup();
        this.userGroup = new ClientUserGroup();
        this.userGroup.copy(newUserGroup);
    }

    @Override
    public void copy(final DomainObject<Long> object) {

        if (object instanceof Destination) {
            final Destination destination = (Destination) object;

            shallowCopy(destination);

            this.deployerParameters = new ArrayList<DeployerParameter>();
            final List<DeployerParameter> origanalsParameters = destination.getDeployerParameters();
            for (final DeployerParameter parameter : origanalsParameters) {
                final ClientDeployerParameter location = new ClientDeployerParameter();
                location.copy(parameter);

                this.deployerParameters.add(location);
            }

        }
    }

    @Override
    public boolean equals(final Object obj) {

        if (obj instanceof ClientDestination) {
            final ClientDestination destination = (ClientDestination) obj;

            if (this.id != null && !this.id.equals(destination.getId())) {
                return false;
            } else if (this.id == null && destination.getId() != null) {
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
