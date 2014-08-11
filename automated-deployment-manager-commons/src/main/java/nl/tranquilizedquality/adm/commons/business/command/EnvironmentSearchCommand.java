/**
 * <pre>
 * Project: automated-deployment-manager-commons 
 * Created on: Aug 1, 2012
 * File: EnvironmentSearchCommand.java
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

import nl.tranquilizedquality.adm.commons.domain.PagingSearchCommand;

/**
 * Search criteria that can be used to find environments.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 1, 2012
 */
public class EnvironmentSearchCommand extends PagingSearchCommand {

    /**
	 * 
	 */
    private static final long serialVersionUID = 9004466077898144626L;

    /** The name of the environment. */
    private String name;

    /** The prefix that will be used for the environment. */
    private String prefix;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * @param prefix
     *            the prefix to set
     */
    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }

}
