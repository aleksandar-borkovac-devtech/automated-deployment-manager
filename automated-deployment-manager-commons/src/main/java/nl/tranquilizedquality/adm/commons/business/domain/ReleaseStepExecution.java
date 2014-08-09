package nl.tranquilizedquality.adm.commons.business.domain;

import java.util.Date;

import nl.tranquilizedquality.adm.commons.domain.InsertableDomainObject;

/**
 * A step execution in a release.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 */
public interface ReleaseStepExecution extends InsertableDomainObject<Long> {

	/**
	 * Retrieves the name of the step.
	 * 
	 * @return Returns a {@link String} representation of the name.
	 */
	String getName();

	/**
	 * Retrieves the date of execution.
	 * 
	 * @return Returns a {@link Date}.
	 */
	Date getExecutionDate();

	/**
	 * Retrieves the status of the step.
	 * 
	 * @return Returns a {@link DeployStatus}.
	 */
	DeployStatus getStatus();

	/**
	 * Retrieves the error message of the step if something went wrong.
	 * 
	 * @return Returns a {@link String} representation of the error message.
	 */
	String getErrorMessage();

	/**
	 * Sets the name of the step.
	 * 
	 * @param name
	 *            The name that will be set.
	 */
	void setName(String name);

	/**
	 * Sets the error message if something went wrong and updates the status to
	 * failed.
	 * 
	 * @param errorMessage
	 *            The error message that will be set.
	 */
	void setErrorMessage(String errorMessage);

	/**
	 * Sets the history object where this step belongs to.
	 * 
	 * @param releaseExecution
	 *            The history object that will be used.
	 */
	void setReleaseExecution(ReleaseExecution releaseExecution);

	/**
	 * Retrieves the release execution where this step belongs to.
	 * 
	 * @return Returns the {@link ReleaseExecution}.
	 */
	ReleaseExecution getReleaseExecution();

}
