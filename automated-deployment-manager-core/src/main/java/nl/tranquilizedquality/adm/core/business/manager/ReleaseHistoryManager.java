/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 28 jul. 2011 File: ReleaseHistoryManager.java
 * Package: nl.tranquilizedquality.adm.core.business.manager
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
package nl.tranquilizedquality.adm.core.business.manager;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.ReleaseExecutionSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.DeployStatus;
import nl.tranquilizedquality.adm.commons.business.domain.Environment;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifactSnapshot;
import nl.tranquilizedquality.adm.commons.business.domain.Release;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecution;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecutionLog;

/**
 * Manager that manages the history of a release.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 jul. 2011
 */
public interface ReleaseHistoryManager {

    /**
     * Creates a history record for the specified {@link Release}.
     * 
     * @param release
     *        The release where history will be created for.
     * @param environment
     *        The environment where the release will be deployed to.
     * @param artifacts
     *        Artifacts that are going to be deployed from this release.
     * @return Returns a {@link ReleaseExecution} which holds the release
     *         history.
     */
    ReleaseExecution createHistory(Release release, Environment environment, List<MavenArtifact> artifacts);

    /**
     * Registers an activity for the specified release.
     * 
     * @param release
     *        The release execution where an activity will be logged for.
     * @param description
     *        The description of the activity that will be logged.
     */
    void registerActivity(ReleaseExecution release, String description);

    /**
     * Registers an activity for the specified release.
     * 
     * @param release
     *        The release execution where an activity will be logged for.
     * @param description
     *        The description of the activity that will be logged.
     * @param errorMessage
     *        The error message when something went wrong.
     */
    void registerActivity(ReleaseExecution release, String description, String errorMessage);

    /**
     * Registers an activity changing the status of the release execution.
     * 
     * @param release
     *        The release execution where an activity will be logged for.
     * @param description
     *        The description of the activity that will be logged.
     * @param deployStatus
     *        The status that will be set.
     */
    void registerActivity(ReleaseExecution release, String description, DeployStatus deployStatus);

    /**
     * Retrieves the release history from the specified release.
     * 
     * @param sc
     *        The search criteria to use for searching for release
     *        executions.
     * @return Returns a {@link List} containing {@link ReleaseExecution} objects or an empty one if
     *         none could be found.
     */
    List<ReleaseExecution> findReleaseHistory(ReleaseExecutionSearchCommand sc);

    /**
     * Searches for release history with the specified unique identifier.
     * 
     * @param id
     *        The unique identifier of the {@link ReleaseExecution}.
     * @return Returns a {@link ReleaseExecution} or null if none could be
     *         found.
     */
    ReleaseExecution findReleaseHistoryById(Long id);

    /**
     * Stores the logs of a {@link ReleaseExecution}.
     * 
     * @param execution
     *        The {@link ReleaseExecution}.
     * @param artifact
     *        The artifact where the logs will be registered for.
     * @param logs
     *        The logs that will be stored.
     */
    void registerLogs(ReleaseExecution execution, MavenArtifactSnapshot artifact, String logs);

    /**
     * Searches for a {@link ReleaseExecutionLog} based on the specified unique
     * identifier.
     * 
     * @param id
     *        The unique identifier used to retrieve the {@link ReleaseExecutionLog}.
     * @return Returns a {@link ReleaseExecutionLog} or null if none could be
     *         found.
     */
    ReleaseExecutionLog findReleaseExecutionLogById(Long id);

    /**
     * Counts the number of release executions based on the passed in search
     * criteria.
     * 
     * @param sc
     *        The search criteria that will be used to count the number of
     *        release executions.
     * @return Return an integer value of 0 or greater.
     */
    int findNumberOfReleaseExecutions(ReleaseExecutionSearchCommand sc);

}
