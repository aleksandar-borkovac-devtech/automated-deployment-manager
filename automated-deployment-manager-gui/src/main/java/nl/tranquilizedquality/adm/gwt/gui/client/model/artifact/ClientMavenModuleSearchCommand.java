/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 24 sep. 2011 File: ClientMavenModuleSearchCommand.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.model.artifact
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
package nl.tranquilizedquality.adm.gwt.gui.client.model.artifact;

import nl.tranquilizedquality.adm.commons.business.command.MavenModuleSearchCommand;

import com.extjs.gxt.ui.client.data.BeanModelTag;

/**
 * Client side representation of a {@link MavenModuleSearchCommand}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 24 sep. 2011
 */
public class ClientMavenModuleSearchCommand extends MavenModuleSearchCommand implements
        BeanModelTag {

    /**
     * Unique identifier used for serialization.
     */
    private static final long serialVersionUID = -4699996311194262792L;

}
