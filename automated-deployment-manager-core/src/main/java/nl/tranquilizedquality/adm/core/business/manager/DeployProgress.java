/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: Sep 2, 2012 File: fDeployProgressManager.java
 * Package: nl.tranquilizedquality.adm.core.business.manager.impl
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
package nl.tranquilizedquality.adm.core.business.manager;

/**
 * Bean that keeps track how far a specific release is.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Sep 2, 2012
 */
public interface DeployProgress {

    /**
     * Retrieves the current progress.
     * 
     * @return Returns an {@link Integer} value between 0 and 100.
     */
    Integer getProgress();

    /**
     * Adds the specified progress to the current progress.
     * 
     * @param progress
     *        The progress that will be added.
     */
    void addProgress(Integer progress);

    /**
     * Marks the progress as 100%.
     */
    void complete();

    /**
     * Registers a new activity for the deployment progress.
     * 
     * @param activity
     *        The activity that will be registered.
     */
    void registerActivity(String activity);

    /**
     * Retrieves the activity that is currently happening.
     * 
     * @return Returns a {@link String} representation of the activity description.
     */
    String getActivityDescription();

}
