/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 1 okt. 2011 File: ClientReleaseExecution.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.model.release
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
package nl.tranquilizedquality.adm.gwt.gui.client.model.release;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.DeployStatus;
import nl.tranquilizedquality.adm.commons.business.domain.Environment;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifactSnapshot;
import nl.tranquilizedquality.adm.commons.business.domain.Release;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecution;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecutionLog;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseStepExecution;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.AbstractInsertableBeanModel;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientEnvironment;

import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * Client side representation of a {@link ReleaseExecution}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 1 okt. 2011
 */
public class ClientReleaseExecution extends AbstractInsertableBeanModel<Long> implements ReleaseExecution {

    /**
     * Unique identifier used for serialization.
     */
    private static final long serialVersionUID = -7024283137311986133L;

    /** The release where the history is from. */
    private Release release;

    /** The environment where the releas is deployed to. */
    private Environment environment;

    /** The status of the release. */
    private DeployStatus releaseStatus;

    /** The date on which the release was done. */
    private Date releaseDate;

    /** All the steps that were done during the release deployment. */
    private List<ReleaseStepExecution> stepExecutions;

    /** The artifacts that should be deployed. */
    private List<MavenArtifactSnapshot> artifacts;

    /** The logs of this execution. */
    private List<ReleaseExecutionLog> logs;

    /**
     * Default constructor.
     */
    public ClientReleaseExecution() {
        stepExecutions = new ArrayList<ReleaseStepExecution>();
        artifacts = new ArrayList<MavenArtifactSnapshot>();
        releaseDate = new Date();
    }

    @Override
    public Long getId() {
        return id;
    }

    /**
     * @see nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecution#getRelease()
     */
    @Override
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
    public Environment getEnvironment() {
        return environment;
    }

    @Override
    public void setEnvironment(final Environment environment) {
        this.environment = environment;
    }

    public String getEnvironmentName() {
        return this.environment.getName();
    }

    /**
     * @see nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecution#getReleaseStatus()
     */
    @Override
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
    public List<ReleaseExecutionLog> getLogs() {
        return logs;
    }

    /**
     * @param logs
     *            the logs to set
     */
    @Override
    public void setLogs(final List<ReleaseExecutionLog> logs) {
        this.logs = logs;
    }

    /**
     * @see nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecution#getStepExecutions()
     */
    @Override
    public List<ReleaseStepExecution> getStepExecutions() {
        return stepExecutions;
    }

    /**
     * @param stepExecutions
     *            the stepExecutions to set
     */
    public void setStepExecutions(final List<ReleaseStepExecution> stepExecutions) {
        this.stepExecutions = stepExecutions;
    }

    public int getNumberOfSteps() {
        if (stepExecutions != null) {
            return this.stepExecutions.size();
        }
        return 0;
    }

    @Override
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

    public int getNumberOfArtifacts() {
        if (artifacts != null) {
            return this.artifacts.size();
        }
        return 0;
    }

    public String getName() {
        return release.getName();
    }

    public String getReleaseDateFormatted() {
        final DateTimeFormat format = DateTimeFormat.getShortDateTimeFormat();
        final Date releaseDate = getReleaseDate();
        return format.format(releaseDate);
    }

    @Override
    public void copy(final DomainObject<Long> object) {
        if (object instanceof ReleaseExecution) {
            final ReleaseExecution releaseExecution = (ReleaseExecution) object;

            shallowCopy(releaseExecution);

            logs = new ArrayList<ReleaseExecutionLog>();
            final List<ReleaseExecutionLog> logs = releaseExecution.getLogs();
            for (final ReleaseExecutionLog log : logs) {
                final ClientReleaseExecutionLog hibernateLog = new ClientReleaseExecutionLog();
                hibernateLog.copy(log);

                this.logs.add(hibernateLog);
            }

            this.artifacts = new ArrayList<MavenArtifactSnapshot>();
            final List<MavenArtifactSnapshot> artifacts = releaseExecution.getArtifacts();
            for (final MavenArtifactSnapshot mavenArtifact : artifacts) {
                final ClientMavenArtifactSnapshot artifact = new ClientMavenArtifactSnapshot();
                artifact.copy(mavenArtifact);

                this.artifacts.add(artifact);
            }

            this.stepExecutions = new ArrayList<ReleaseStepExecution>();
            final List<ReleaseStepExecution> executions = releaseExecution.getStepExecutions();
            for (final ReleaseStepExecution releaseStepExecution : executions) {
                final ClientReleaseStepExecution step = new ClientReleaseStepExecution();
                step.copy(releaseStepExecution);

                this.stepExecutions.add(step);
            }
        }
    }

    /**
     * Copies the none manyToOne relations.
     * 
     * @param releaseExecution
     *            The execution that will be copied.
     */
    public void shallowCopy(final ReleaseExecution releaseExecution) {
        super.copy(releaseExecution);

        this.releaseDate = releaseExecution.getReleaseDate();
        this.releaseStatus = releaseExecution.getReleaseStatus();

        this.release = new ClientRelease();
        final Release release = releaseExecution.getRelease();
        this.release.copy(release);

        this.environment = new ClientEnvironment();
        final Environment environment = releaseExecution.getEnvironment();
        this.environment.copy(environment);

        this.artifacts = new ArrayList<MavenArtifactSnapshot>();
        this.stepExecutions = new ArrayList<ReleaseStepExecution>();
        this.logs = new ArrayList<ReleaseExecutionLog>();
    }

}
