/*
 * @(#)HibernateDeployerParameterType.java 30 jan. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameterTemplate;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameterType;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.hibernate.bean.AbstractUpdatableDomainObject;

import org.hibernate.annotations.Type;

import com.google.code.simplestuff.annotation.BusinessField;

/**
 * Hibernate implementation of a parameter template configuration that can be used to determine what
 * parameters are allowed/needed for a specific deployer.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 30 jan. 2013
 */
@Entity()
@Table(name = "DEPLOYER_PARAMETER_TEMPLATES", schema = "ADM")
public class HibernateDeployerParameterTemplate extends AbstractUpdatableDomainObject<Long> implements DeployerParameterTemplate {

    /** The parameter type that is allowed. */
    @BusinessField
    private DeployerParameterType type;

    /** Determines if the parameter is mandatory for the deployer to work. */
    @BusinessField
    private Boolean mandatory;

    @Id
    @Override
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    @Override
    @Enumerated(EnumType.STRING)
    @Column(name = "DEPLOYER_TYPE", nullable = false, length = 255)
    public DeployerParameterType getType() {
        return type;
    }

    public void setType(final DeployerParameterType type) {
        this.type = type;
    }

    @Type(type = "yes_no")
    @Column(name = "MANDATORY", nullable = false, length = 1)
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
