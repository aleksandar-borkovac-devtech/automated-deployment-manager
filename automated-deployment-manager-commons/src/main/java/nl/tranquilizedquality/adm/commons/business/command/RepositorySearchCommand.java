/**
 * <pre>
 * Project: automated-deployment-manager-commons Created on: 22 okt. 2012 File: RepositorySearchCommand.java
 * Package: nl.tranquilizedquality.adm.commons.business.command
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
package nl.tranquilizedquality.adm.commons.business.command;

import nl.tranquilizedquality.adm.commons.business.domain.Repository;
import nl.tranquilizedquality.adm.commons.domain.PagingSearchCommand;

/**
 * Search criteria for a {@link Repository}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 22 okt. 2012
 */
public class RepositorySearchCommand extends PagingSearchCommand {

    /** Unique identifier used for serialization. */
    private static final long serialVersionUID = -4017171534335040852L;

    /** The name to search for. */
    private String name;

    /** The enabled state to search on. */
    private Boolean enabled;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *        the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the enabled
     */
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * @param enabled
     *        the enabled to set
     */
    public void setEnabled(final Boolean enabled) {
        this.enabled = enabled;
    }

}
