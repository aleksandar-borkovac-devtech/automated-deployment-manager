/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: Sep 2, 2012 File: fClientProgress.java
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

import java.io.Serializable;

import com.extjs.gxt.ui.client.data.BeanModelTag;

/**
 * Client side representation of the progress of a deployment.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Sep 2, 2012
 * 
 */
public class ClientProgress implements Serializable, BeanModelTag {

    /**
     * Unique identifier used for serialization.
     */
    private static final long serialVersionUID = -7493284599761012903L;

    /** The current progress. */
    private int progress;

    /** The description of the current activity. */
    private String description;

    public int getProgress() {
        return progress;
    }

    public void setProgress(final int progress) {
        this.progress = progress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

}
