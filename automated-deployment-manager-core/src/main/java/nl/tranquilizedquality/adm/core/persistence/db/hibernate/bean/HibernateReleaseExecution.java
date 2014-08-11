package nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import nl.tranquilizedquality.adm.commons.business.domain.DeployStatus;
import nl.tranquilizedquality.adm.commons.business.domain.Environment;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifactSnapshot;
import nl.tranquilizedquality.adm.commons.business.domain.Release;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecution;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecutionLog;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseStepExecution;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.hibernate.bean.AbstractInsertableDomainObject;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.google.code.simplestuff.annotation.BusinessField;

/**
 * Hibernate implementation of release executions.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 */
@Entity()
@Table(name = "RELEASE_EXECUTIONS", schema = "ADM")
public class HibernateReleaseExecution extends AbstractInsertableDomainObject<Long> implements ReleaseExecution {

    /**
     * 
     */
    private static final long serialVersionUID = -1356629280444560438L;

    /** The release where the history is from. */
    @BusinessField
    private Release release;

    /** The environment where the releas is deployed to. */
    @BusinessField
    private Environment environment;

    /** The status of the release. */
    @BusinessField
    private DeployStatus releaseStatus;

    /** The date on which the release was done. */
    @BusinessField
    private Date releaseDate;

    /** The logs of this execution. */
    private List<ReleaseExecutionLog> logs;

    /** All the steps that were done during the release deployment. */
    private List<ReleaseStepExecution> stepExecutions;

    /** The artifacts that should be deployed. */
    private List<MavenArtifactSnapshot> artifacts;

    /**
     * Default constructor.
     */
    public HibernateReleaseExecution() {
        stepExecutions = new ArrayList<ReleaseStepExecution>();
        logs = new ArrayList<ReleaseExecutionLog>();
        releaseDate = new Date();
    }

    @Override
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "RELEASE_EXECUTION_SEQ_GEN")
    @SequenceGenerator(name = "RELEASE_EXECUTION_SEQ_GEN", initialValue = 1, allocationSize = 1, sequenceName = "ADM.RELEASE_EXECUTIONS_SEQ")
    public Long getId() {
        return id;
    }

    /**
     * @see nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecution#getRelease()
     */
    @Override
    @ForeignKey(name = "FK_RELEASE_EXECUTION_RELEASE")
    @JoinColumn(name = "RELEASE_ID")
    @ManyToOne(targetEntity = HibernateRelease.class, optional = false)
    public Release getRelease() {
        return release;
    }

    /**
     * @param release
     *            the release to set
     */
    @Override
    public void setRelease(final Release release) {
        this.release = release;
    }

    @Override
    @ForeignKey(name = "FK_RELEASE_EXECUTION_ENVIRONMENT")
    @JoinColumn(name = "ENVIRONMENT_ID")
    @ManyToOne(targetEntity = HibernateEnvironment.class, optional = false)
    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(final Environment environment) {
        this.environment = environment;
    }

    /**
     * @see nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecution#getReleaseStatus()
     */
    @Override
    @Enumerated(EnumType.STRING)
    @Column(name = "DEPLOY_STATUS", nullable = false)
    public DeployStatus getReleaseStatus() {
        return releaseStatus;
    }

    /**
     * @param releaseStatus
     *            the releaseStatus to set
     */
    @Override
    public void setReleaseStatus(final DeployStatus releaseStatus) {
        this.releaseStatus = releaseStatus;
    }

    /**
     * @see nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecution#getReleaseDate()
     */
    @Override
    @Column(name = "RELEASE_DATE", nullable = false)
    public Date getReleaseDate() {
        return releaseDate;
    }

    /**
     * @param releaseDate
     *            the releaseDate to set
     */
    public void setReleaseDate(final Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    /**
     * @return the logs
     */
    @Override
    @OrderBy("id")
    @OneToMany(targetEntity = HibernateReleaseExecutionLog.class, mappedBy = "releaseExecution", cascade = CascadeType.ALL)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public List<ReleaseExecutionLog> getLogs() {
        return Collections.unmodifiableList(logs);
    }

    @Override
    public void setLogs(final List<ReleaseExecutionLog> logs) {
        this.logs = logs;
    }

    /**
     * @see nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecution#getStepExecutions()
     */
    @Override
    @OrderBy("id")
    @OneToMany(targetEntity = HibernateReleaseStepExecution.class, mappedBy = "releaseExecution", cascade = CascadeType.ALL)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public List<ReleaseStepExecution> getStepExecutions() {
        return Collections.unmodifiableList(stepExecutions);
    }

    /**
     * @param stepExecutions
     *            the stepExecutions to set
     */
    public void setStepExecutions(final List<ReleaseStepExecution> stepExecutions) {
        this.stepExecutions = stepExecutions;
    }

    /**
     * @return the artifacts
     */
    @Override
    @OneToMany(targetEntity = HibernateMavenArtifactSnapshot.class, mappedBy = "releaseExecution", cascade = CascadeType.ALL)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public List<MavenArtifactSnapshot> getArtifacts() {
        return artifacts;
    }

    /**
     * @param artifacts
     *            the artifacts to set
     */
    @Override
    public void setArtifacts(final List<MavenArtifactSnapshot> artifacts) {
        this.artifacts = artifacts;
    }

    @Override
    public void copy(final DomainObject<Long> object) {
        super.copy(object);

        if (object instanceof ReleaseExecution) {
            final ReleaseExecution releaseExecution = (ReleaseExecution) object;
            releaseDate = releaseExecution.getReleaseDate();
            releaseStatus = releaseExecution.getReleaseStatus();
            logs = releaseExecution.getLogs();

            release = new HibernateRelease();
            final Release release = releaseExecution.getRelease();
            this.release.copy(release);

            this.environment = new HibernateEnvironment();
            final Environment environment = releaseExecution.getEnvironment();
            this.environment.copy(environment);

            logs = new ArrayList<ReleaseExecutionLog>();
            final List<ReleaseExecutionLog> logs = releaseExecution.getLogs();
            for (final ReleaseExecutionLog log : logs) {
                final HibernateReleaseExecutionLog hibernateLog = new HibernateReleaseExecutionLog();
                hibernateLog.copy(log);

                this.logs.add(hibernateLog);
            }

            artifacts = new ArrayList<MavenArtifactSnapshot>();
            final List<MavenArtifactSnapshot> artifacts = releaseExecution.getArtifacts();
            for (final MavenArtifactSnapshot mavenArtifact : artifacts) {
                final HibernateMavenArtifactSnapshot artifact = new HibernateMavenArtifactSnapshot();
                artifact.copy(mavenArtifact);

                this.artifacts.add(artifact);
            }

            stepExecutions = new ArrayList<ReleaseStepExecution>();
            final List<ReleaseStepExecution> executions = releaseExecution.getStepExecutions();
            for (final ReleaseStepExecution releaseStepExecution : executions) {
                final HibernateReleaseStepExecution step = new HibernateReleaseStepExecution();
                step.copy(releaseStepExecution);

                stepExecutions.add(step);
            }
        }
    }

}
