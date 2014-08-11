/*
 * @(#)ClientDeployer.java 9 nov. 2012
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.gwt.gui.client.model.environment;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.Deployer;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameterTemplate;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.AbstractUpdatableBeanModel;

/**
 * Client representation of a deployer that can be chosen to deploy an artifact
 * to a specific destination.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 9 nov. 2012
 */
public class ClientDeployer extends AbstractUpdatableBeanModel<Long> implements Deployer {

    /** The name of the deployer. */
    private String name;

    /** The parameters that can be used for this deployer. */
    private List<DeployerParameterTemplate> parameters;

    /**
     * Default constructor.
     */
    public ClientDeployer() {
        parameters = new ArrayList<DeployerParameterTemplate>();
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

    @Override
    public List<DeployerParameterTemplate> getParameters() {
        return parameters;
    }

    public void setParameters(final List<DeployerParameterTemplate> parameters) {
        this.parameters = parameters;
    }

    @Override
    public void copy(final DomainObject<Long> object) {
        if (object instanceof Deployer) {
            super.copy(object);
            final Deployer deployer = (Deployer) object;
            this.name = deployer.getName();

            this.parameters = new ArrayList<DeployerParameterTemplate>();
            final List<DeployerParameterTemplate> originalParams = deployer.getParameters();
            for (final DeployerParameterTemplate deployerParameterTemplate : originalParams) {
                final ClientDeployerParameterTemplate clientDeployerParameterTemplate = new ClientDeployerParameterTemplate();
                clientDeployerParameterTemplate.copy(deployerParameterTemplate);
                this.parameters.add(clientDeployerParameterTemplate);
            }
        }

    }

    @Override
    public boolean equals(final Object obj) {

        if (obj instanceof ClientDeployer) {
            final ClientDeployer deployer = (ClientDeployer) obj;

            if (this.id != null && !this.id.equals(deployer.getId())) {
                return false;
            } else if (this.id == null && deployer.getId() != null) {
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
