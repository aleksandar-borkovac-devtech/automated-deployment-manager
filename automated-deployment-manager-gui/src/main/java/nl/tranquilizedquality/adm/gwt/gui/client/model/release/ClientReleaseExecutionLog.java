/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: Aug 3, 2012 File: fClientReleaseExecutionLog.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.model.release
 * 
 * Copyright (c) 2012 Tranquilized Quality www.Tranquilized Quality.nl All rights
 * reserved.
 * 
 * This software is the confidential and proprietary information of Tranquilized Quality
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Tranquilized Quality.
 * </pre>
 */
package nl.tranquilizedquality.adm.gwt.gui.client.model.release;

import javax.persistence.Id;

import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifactSnapshot;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecution;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecutionLog;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.AbstractInsertableBeanModel;

import com.google.code.simplestuff.annotation.BusinessField;

/**
 * Client side representation of a {@link ReleaseExecutionLog}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 3, 2012
 */
public class ClientReleaseExecutionLog extends AbstractInsertableBeanModel<Long> implements ReleaseExecutionLog {

    /**
     * Unique identifier used for serialization.
     */
    private static final long serialVersionUID = 7853381846316484451L;

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
    public Long getId() {
        return id;
    }

    @Override
    public ReleaseExecution getReleaseExecution() {
        return releaseExecution;
    }

    /**
     * @param releaseExecution
     *        the releaseExecution to set
     */
    public void setReleaseExecution(final ReleaseExecution releaseExecution) {
        this.releaseExecution = releaseExecution;
    }

    @Override
    public String getLogs() {
        return logs;
    }

    /**
     * @param logs
     *        the logs to set
     */
    public void setLogs(final String logs) {
        this.logs = logs;
    }

    @Override
    public MavenArtifactSnapshot getMavenArtifact() {
        return mavenArtifact;
    }

    /**
     * @param mavenArtifact
     *        the mavenArtifact to set
     */
    public void setMavenArtifact(final MavenArtifactSnapshot mavenArtifact) {
        this.mavenArtifact = mavenArtifact;
    }

    public String getArtifactName() {
        return mavenArtifact.getName();
    }

    @Override
    public void copy(final DomainObject<Long> object) {
        if (object instanceof ReleaseExecutionLog) {
            super.copy(object);

            final ReleaseExecutionLog log = (ReleaseExecutionLog) object;
            this.logs = log.getLogs();

            final MavenArtifactSnapshot originalArtifact = log.getMavenArtifact();
            final ClientMavenArtifactSnapshot clientArtifact = new ClientMavenArtifactSnapshot();
            clientArtifact.copy(originalArtifact);
            this.mavenArtifact = clientArtifact;

            final ReleaseExecution originalReleaseExecution = log.getReleaseExecution();
            final ClientReleaseExecution clientReleaseExecution = new ClientReleaseExecution();
            clientReleaseExecution.shallowCopy(originalReleaseExecution);
            this.releaseExecution = clientReleaseExecution;
        }
    }

}
