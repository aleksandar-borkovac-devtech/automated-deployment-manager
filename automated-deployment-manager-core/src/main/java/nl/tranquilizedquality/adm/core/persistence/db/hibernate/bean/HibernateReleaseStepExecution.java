package nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import nl.tranquilizedquality.adm.commons.business.domain.DeployStatus;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecution;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseStepExecution;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.hibernate.bean.AbstractInsertableDomainObject;

import org.hibernate.annotations.ForeignKey;

import com.google.code.simplestuff.annotation.BusinessField;

/**
 * Hibernate implementation of a step execution from a release.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 */
@Entity()
@Table(name = "RELEASE_STEP_EXECUTIONS", schema = "ADM")
public class HibernateReleaseStepExecution extends AbstractInsertableDomainObject<Long> implements ReleaseStepExecution {

    /**
     * 
     */
    private static final long serialVersionUID = -2356851056822960768L;

    /** The step name. */
    @BusinessField
    private String name;

    /** The execution date. */
    @BusinessField
    private Date executionDate;

    /** The status of the execution step. */
    @BusinessField
    private DeployStatus status;

    /** The error message if the release failed. */
    @BusinessField
    private String errorMessage;

    /** The release history where this execution is from. */
    @BusinessField
    private ReleaseExecution releaseExecution;

    /**
     * Default constructor.
     */
    public HibernateReleaseStepExecution() {
        executionDate = new Date();
        status = DeployStatus.SUCCESS;
    }

    @Override
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "RELEASE_STEP_EXECUTIONS_SEQ_GEN")
    @SequenceGenerator(name = "RELEASE_STEP_EXECUTIONS_SEQ_GEN", initialValue = 1, allocationSize = 1, sequenceName = "ADM.RELEASE_STEP_EXECUTIONS_SEQ")
    public Long getId() {
        return id;
    }

    /**
     * @see nl.tranquilizedquality.adm.commons.business.domain.ReleaseStepExecution#getName()
     */
    @Override
    @Column(name = "NAME", nullable = false, length = 200)
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    @Override
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @see nl.tranquilizedquality.adm.commons.business.domain.ReleaseStepExecution#getExecutionDate()
     */
    @Override
    @Column(name = "EXEUCTION_DATE", nullable = false)
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
    @Enumerated(EnumType.STRING)
    @Column(name = "DEPLOY_STATUS", nullable = false)
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
    @Column(name = "ERROR_MESSAGE", nullable = true, length = 256)
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage
     *            the errorMessage to set
     */
    @Override
    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
        this.status = DeployStatus.FAILED;
    }

    /**
     * @return the releaseExecution
     */
    @Override
    @ForeignKey(name = "FK_STEP_EXECUTION_RELEASE_EXECUTION")
    @JoinColumn(name = "RELEASE_EXECUTION_ID")
    @ManyToOne(targetEntity = HibernateReleaseExecution.class, optional = false)
    public ReleaseExecution getReleaseExecution() {
        return releaseExecution;
    }

    /**
     * @param releaseExecution
     *            the releaseExecution to set
     */
    @Override
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

            this.releaseExecution = new HibernateReleaseExecution();
            final ReleaseExecution releaseExecution = execution.getReleaseExecution();
            this.releaseExecution.copy(releaseExecution);

        }
    }

}
