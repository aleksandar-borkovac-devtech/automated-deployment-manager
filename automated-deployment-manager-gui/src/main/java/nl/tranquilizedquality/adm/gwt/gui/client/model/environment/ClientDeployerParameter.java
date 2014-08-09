/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 11 sep. 2011 File: ClientDestinationLocation.java
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

import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameter;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameterType;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.AbstractUpdatableBeanModel;

/**
 * Client side representation of a destination location.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 11 sep. 2011
 */
public class ClientDeployerParameter extends AbstractUpdatableBeanModel<Long> implements DeployerParameter {

    /**
     * Unique identifier used for serialization.
     */
    private static final long serialVersionUID = -6246707721460463385L;

    /** The path on the server. */
    private String value;

    /** The type of location. */
    private DeployerParameterType type;

    /**
     * Determines the rank of the parameter. e.g. if you specify 2 it will be the second parameter
     * passed to the script.
     */
    private Integer rank;

    /**
     * Default constructor.
     */
    public ClientDeployerParameter() {
        rank = 0;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getValue() {
        return value;
    }

    /**
     * @param path
     *        the path to set
     */
    public void setValue(final String path) {
        this.value = path;
    }

    @Override
    public DeployerParameterType getType() {
        return type;
    }

    /**
     * @param type
     *        the type to set
     */
    public void setType(final DeployerParameterType type) {
        this.type = type;
    }

    @Override
    public Integer getRank() {
        return rank;
    }

    public void setRank(final Integer rank) {
        this.rank = rank;
    }

    @Override
    public void copy(final DomainObject<Long> object) {
        super.copy(object);

        if (object instanceof DeployerParameter) {
            final DeployerParameter location = (DeployerParameter) object;
            this.value = location.getValue();
            this.type = location.getType();
            this.rank = location.getRank();
        }
    }

    @Override
    public boolean equals(final Object obj) {

        if (obj instanceof ClientDeployerParameter) {
            final ClientDeployerParameter location = (ClientDeployerParameter) obj;

            if (this.id != null && !this.id.equals(location.getId())) {
                return false;
            } else if (this.id == null && location.getId() != null) {
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
