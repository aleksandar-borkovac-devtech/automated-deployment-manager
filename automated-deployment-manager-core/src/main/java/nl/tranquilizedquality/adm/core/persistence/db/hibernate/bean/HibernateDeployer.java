/*
 * @(#)HibernateDeployer.java 12 dec. 2012
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import nl.tranquilizedquality.adm.commons.business.domain.Deployer;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameterTemplate;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.hibernate.bean.AbstractUpdatableDomainObject;

import org.hibernate.annotations.ForeignKey;

import com.google.code.simplestuff.annotation.BusinessField;

/**
 * Hibernate implementation of a deployer in the application.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 12 dec. 2012
 */
@Entity()
@Table(name = "DEPLOYERS", schema = "ADM")
public class HibernateDeployer extends AbstractUpdatableDomainObject<Long> implements Deployer {

    /** The name of the deployer. */
    @BusinessField
    private String name;

    /** The parameters that can be used for this deployer. */
    private List<DeployerParameterTemplate> parameters;

    /**
     * Default constructor.
     */
    public HibernateDeployer() {
        parameters = new ArrayList<DeployerParameterTemplate>();
    }

    @Override
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "DEPLOYERS_SEQ_GEN")
    @SequenceGenerator(name = "DEPLOYERS_SEQ_GEN", initialValue = 1, allocationSize = 1, sequenceName = "ADM.DEPLOYERS_SEQ")
    public Long getId() {
        return id;
    }

    @Override
    @Column(name = "NAME", nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    @ForeignKey(name = "FK_DEPLOYER_PARAMETER_TEMPLATES")
    @JoinColumn(name = "DEPLOYER_ID", insertable = true, updatable = true, nullable = false)
    @OrderBy(value = "id")
    @OneToMany(targetEntity = HibernateDeployerParameterTemplate.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<DeployerParameterTemplate> getParameters() {
        return parameters;
    }

    @Override
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

            final List<DeployerParameterTemplate> newParameters = deployer.getParameters();
            for (final DeployerParameterTemplate deployerParameterTemplate : newParameters) {
                final HibernateDeployerParameterTemplate template = new HibernateDeployerParameterTemplate();
                template.copy(deployerParameterTemplate);
            }
        }

    }

}
