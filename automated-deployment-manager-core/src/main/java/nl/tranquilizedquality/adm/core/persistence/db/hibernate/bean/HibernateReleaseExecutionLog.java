/**
 * <pre>
 * Project: automated-deployment-manager-core 
 * Created on: Aug 3, 2012
 * File: HibernateReleaseExecutionLog.java
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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifactSnapshot;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecution;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecutionLog;
import nl.tranquilizedquality.adm.commons.hibernate.bean.AbstractInsertableDomainObject;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Type;

import com.google.code.simplestuff.annotation.BusinessField;

/**
 * Hibernate implementation of logs from a specific artifcat that was released
 * in the current release execution.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 3, 2012
 */
@Entity()
@Table(name = "RELEASE_EXECUTION_LOGS", schema = "ADM")
public class HibernateReleaseExecutionLog extends AbstractInsertableDomainObject<Long> implements ReleaseExecutionLog {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1864430966941999333L;

    /** The release execution this log is part of. */
    @BusinessField
    private ReleaseExecution releaseExecution;

    /** The actual log of the release execution. */
    @BusinessField
    private String logs;

    /** The Maven artifact that was deployed. */
    @BusinessField
    private MavenArtifactSnapshot mavenArtifact;

    @Id
    @Override
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "REL_EXEC_LOGS_SEQ_GEN")
    @SequenceGenerator(name = "REL_EXEC_LOGS_SEQ_GEN", initialValue = 1, allocationSize = 1, sequenceName = "ADM.REL_EXEC_LOGS_SEQ")
    public Long getId() {
        return id;
    }

    @Override
    @ForeignKey(name = "FK_RELEASE_EXECUTION_EXECUTIONS")
    @JoinColumn(name = "RELEASE_EXECUTION_ID")
    @ManyToOne(targetEntity = HibernateReleaseExecution.class, optional = false)
    public ReleaseExecution getReleaseExecution() {
        return releaseExecution;
    }

    /**
     * @param releaseExecution
     *            the releaseExecution to set
     */
    public void setReleaseExecution(final ReleaseExecution releaseExecution) {
        this.releaseExecution = releaseExecution;
    }

    @Override
    @Type(type = "text")
    @Column(name = "LOGS", nullable = false)
    public String getLogs() {
        return logs;
    }

    /**
     * @param logs
     *            the logs to set
     */
    public void setLogs(final String logs) {
        this.logs = logs;
    }

    @Override
    @JoinColumn(name = "MAVEN_ARTIFACT_ID")
    @ForeignKey(name = "FK_RELEASE_EXECUTION_LOGS_ARTIFACTS")
    @ManyToOne(targetEntity = HibernateMavenArtifactSnapshot.class, optional = false)
    public MavenArtifactSnapshot getMavenArtifact() {
        return mavenArtifact;
    }

    /**
     * @param mavenArtifact
     *            the mavenArtifact to set
     */
    public void setMavenArtifact(final MavenArtifactSnapshot mavenArtifact) {
        this.mavenArtifact = mavenArtifact;
    }

}
