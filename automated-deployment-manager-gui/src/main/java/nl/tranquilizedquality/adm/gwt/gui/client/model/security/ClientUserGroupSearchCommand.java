/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: Aug 24, 2012 File: fClientUserGroupSearchCommand.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.model.security
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
package nl.tranquilizedquality.adm.gwt.gui.client.model.security;

import nl.tranquilizedquality.adm.commons.business.command.UserGroupSearchCommand;

import com.extjs.gxt.ui.client.data.BeanModelTag;

/**
 * Client side representation of the search criteria used to search for user
 * groups.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 24, 2012
 * 
 */
public class ClientUserGroupSearchCommand extends UserGroupSearchCommand implements BeanModelTag {

    /**
     * Unique identifier used for serialization.
     */
    private static final long serialVersionUID = 2700885586470683336L;

}
