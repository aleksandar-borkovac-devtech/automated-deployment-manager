/*
 * @(#)ClientDeployerParameterTemplate.java 30 jan. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.gwt.gui.client.model.environment;

import javax.persistence.Transient;

import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameterTemplate;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameterType;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.AbstractUpdatableBeanModel;

/**
 * Client side representation of a deployer parameter template that is used to
 * determine which parameter types to display when configuring a destination.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 30 jan. 2013
 */
public class ClientDeployerParameterTemplate extends AbstractUpdatableBeanModel<Long> implements DeployerParameterTemplate {

    /** The parameter type that is allowed. */
    private DeployerParameterType type;

    /** Determines if the parameter is mandatory for the deployer to work. */
    private Boolean mandatory;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public DeployerParameterType getType() {
        return type;
    }

    public void setType(final DeployerParameterType type) {
        this.type = type;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

    @Override
    @Transient
    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(final Boolean mandatory) {
        this.mandatory = mandatory;
    }

    @Override
    public void copy(final DomainObject<Long> object) {
        if (object instanceof DeployerParameterTemplate) {
            super.copy(object);

            final DeployerParameterTemplate template = (DeployerParameterTemplate) object;
            this.mandatory = template.isMandatory();
            this.type = template.getType();
        }
    }

}
