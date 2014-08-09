/**
 * <pre>
 * Project: automated-deployment-manager-jenkins Created on: 9 aug. 2014 File: fDescriptor.java
 * Package: nl.tranquilzedquality.adm.jenkins.deployer
 *
 * Copyright (c) 2014 Tranquilized Quality www.tr-quality.com All rights
 * reserved.
 *
 * This software is the confidential and proprietary information of Dizizid
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Tranquilized Quality.
 * </pre>
 */
package nl.tranquilzedquality.adm.jenkins.deployer;

import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;

/**
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 9 aug. 2014
 *
 */
public class Descriptor extends BuildStepDescriptor<Publisher> {

    public Descriptor() {
        load();
    }

    /*
     * (non-Javadoc)
     * 
     * @see hudson.tasks.BuildStepDescriptor#isApplicable(java.lang.Class)
     */
    @Override
    public boolean isApplicable(final Class<? extends AbstractProject> arg0) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see hudson.model.Descriptor#getDisplayName()
     */
    @Override
    public String getDisplayName() {
        return null;
    }

}
