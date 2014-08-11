/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: Aug 25, 2012 File: fClientUserSearchCommand.java
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

import nl.tranquilizedquality.adm.security.business.command.UserSearchCommand;

import com.extjs.gxt.ui.client.data.BeanModelTag;

/**
 * Client side representation of user search criteria.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 */
public class ClientUserSearchCommand extends UserSearchCommand implements BeanModelTag {

    /**
     * Unique identifier used for serialization.
     */
    private static final long serialVersionUID = -2602818142768516203L;

    /**
     * Determines if this is the initial search done when the grid is being
     * rendered.
     */
    private boolean initSearch = true;

    public boolean isInitSearch() {
        return initSearch;
    }

    public void setInitSearch(final boolean initSearch) {
        this.initSearch = initSearch;
    }

}
