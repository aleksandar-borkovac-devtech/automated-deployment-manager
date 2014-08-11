/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: Sep 2, 2012 File: fDeployProgressManagerImpl.java
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
package nl.tranquilizedquality.adm.core.business.manager.impl;

import nl.tranquilizedquality.adm.core.business.manager.DeployProgress;

/**
 * Stateful bean that keeps track how far a specific release is.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Sep 2, 2012
 * 
 */
public class DeployProgressImpl implements DeployProgress {

    /** Indicates on a scale of 100 how far the deployment is. */
    private Integer progress;

    /** Description of the current activity of the deployment. */
    private String activityDescription;

    /**
     * Default constructor.
     */
    public DeployProgressImpl() {
        progress = 0;
    }

    @Override
    public Integer getProgress() {
        if (progress >= 100) {
            progress = 0;
            return 100;
        }
        return progress;
    }

    public void setProgress(final Integer progress) {
        this.progress = progress;
    }

    @Override
    public void addProgress(final Integer progress) {
        this.progress += progress;
    }

    @Override
    public void complete() {
        this.progress = 100;
    }

    @Override
    public void registerActivity(final String activity) {
        this.activityDescription = activity;
    }

    @Override
    public String getActivityDescription() {
        return activityDescription;
    }
}
