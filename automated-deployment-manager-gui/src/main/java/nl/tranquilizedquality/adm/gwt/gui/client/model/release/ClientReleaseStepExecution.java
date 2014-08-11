/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 8 okt. 2011 File: ClientReleaseStepExecution.java
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

import java.util.Date;

import nl.tranquilizedquality.adm.commons.business.domain.DeployStatus;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecution;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseStepExecution;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.AbstractInsertableBeanModel;

/**
 * Client side representation of a {@link ReleaseStepExecution}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 8 okt. 2011
 */
public class ClientReleaseStepExecution extends AbstractInsertableBeanModel<Long> implements
        ReleaseStepExecution {

    /**
     * Unique identifier used for serialization.
     */
    private static final long serialVersionUID = -1849505070926535054L;

    /** The step name. */
    private String name;

    /** The execution date. */
    private Date executionDate;

    /** The status of the execution step. */
    private DeployStatus status;

    /** The error message if the release failed. */
    private String errorMessage;

    /** The release history where this execution is from. */
    private ReleaseExecution releaseExecution;

    /**
     * Default constructor.
     */
    public ClientReleaseStepExecution() {
        executionDate = new Date();
    }

    @Override
    public Long getId() {
        return id;
    }

    /**
     * @see nl.tranquilizedquality.adm.commons.business.domain.ReleaseStepExecution#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @see nl.tranquilizedquality.adm.commons.business.domain.ReleaseStepExecution#getExecutionDate()
     */
    @Override
    public Date getExecutionDate() {
        return executionDate;
    }

    /**
     * @param executionDate
     *            the executionDate to set
     */
    public void setExecutionDate(final Date executionDate) {
        this.executionDate = executionDate;
    }

    /**
     * @see nl.tranquilizedquality.adm.commons.business.domain.ReleaseStepExecution#getStatus()
     */
    @Override
    public DeployStatus getStatus() {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(final DeployStatus status) {
        this.status = status;
    }

    /**
     * @see nl.tranquilizedquality.adm.commons.business.domain.ReleaseStepExecution#getErrorMessage()
     */
    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage
     *            the errorMessage to set
     */
    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
        this.status = DeployStatus.FAILED;
    }

    /**
     * @return the releaseExecution
     */
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
    public void copy(final DomainObject<Long> object) {
        super.copy(object);

        if (object instanceof ReleaseStepExecution) {
            final ReleaseStepExecution execution = (ReleaseStepExecution) object;
            this.errorMessage = execution.getErrorMessage();
            this.executionDate = execution.getExecutionDate();
            this.name = execution.getName();
            this.status = execution.getStatus();

            final ClientReleaseExecution newReleaseExecution = new ClientReleaseExecution();
            final ReleaseExecution releaseExecution = execution.getReleaseExecution();
            newReleaseExecution.shallowCopy(releaseExecution);
            this.releaseExecution = newReleaseExecution;
        }
    }

}
