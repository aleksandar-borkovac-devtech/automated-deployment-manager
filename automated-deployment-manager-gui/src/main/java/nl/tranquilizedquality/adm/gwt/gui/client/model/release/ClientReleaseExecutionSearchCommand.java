/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: Aug 6, 2012 File: fClientReleaseExecutionSearchCommand.java
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

import nl.tranquilizedquality.adm.commons.business.command.ReleaseExecutionSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecution;

import com.extjs.gxt.ui.client.data.BeanModelTag;

/**
 * Client side representation of the search criteria that is used to search for
 * {@link ReleaseExecution} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 6, 2012
 * 
 */
public class ClientReleaseExecutionSearchCommand extends ReleaseExecutionSearchCommand implements BeanModelTag {

    /**
     * Unique identifier used for serialization.
     */
    private static final long serialVersionUID = 5203284778323880841L;

}
