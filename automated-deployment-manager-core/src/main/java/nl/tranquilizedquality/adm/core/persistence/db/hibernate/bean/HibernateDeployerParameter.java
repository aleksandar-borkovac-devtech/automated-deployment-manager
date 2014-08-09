/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 7 jul. 2011 File: HibernateDestinationLocation.java
 * Package: nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean
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
package nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameter;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameterType;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.hibernate.bean.AbstractUpdatableDomainObject;

import org.hibernate.annotations.Index;

import com.google.code.simplestuff.annotation.BusinessField;

/**
 * Hibernate implementation of a parameter that can be passed to a deployer. *
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 7 jul. 2011
 */
@Entity()
@Table(name = "DEPLOYER_PARAMETERS", schema = "ADM")
public class HibernateDeployerParameter extends AbstractUpdatableDomainObject<Long> implements DeployerParameter {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8729045218468778647L;

    /** The path on the server. */
    @BusinessField
    private String value;

    /** The type of location. */
    @BusinessField
    private DeployerParameterType type;

    /**
     * Determines the rank of the parameter. e.g. if you specify 2 it will be the second parameter
     * passed to the script.
     */
    @BusinessField
    private Integer rank;

    @Override
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "DEPLOYER_PARAMETERS_SEQ_GEN")
    @SequenceGenerator(name = "DEPLOYER_PARAMETERS_SEQ_GEN", initialValue = 1, allocationSize = 1, sequenceName = "ADM.DEPLOYER_PARAMETERS_SEQ")
    public Long getId() {
        return id;
    }

    @Override
    @Column(name = "PATH", nullable = false, length = 256)
    public String getValue() {
        return value;
    }

    /**
     * @param value
     *        the value of the parameter.
     */
    public void setValue(final String value) {
        this.value = value;
    }

    @Override
    @Enumerated(EnumType.STRING)
    @Index(name = "LOCATION_TYPE_IDX")
    @Column(name = "LOCATION_TYPE", nullable = false, length = 30)
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
    @Index(name = "PARAM_RANK_IDX")
    @Column(name = "RANK", nullable = false)
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

}
