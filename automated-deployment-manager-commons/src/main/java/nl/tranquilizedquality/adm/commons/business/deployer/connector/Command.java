/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 22 okt. 2012 File: Command.java
 * Package: nl.Tranquilized Quality.adm.core.business.deployer.connector
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
package nl.tranquilizedquality.adm.commons.business.deployer.connector;

/**
 * Representation of a command that can be executed.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 22 okt. 2012
 */
public interface Command {

    /**
     * Retrieves the actual command that should be executed.
     * 
     * @return Returns a {@link String} representation of the command.
     */
    String getCommand();

}
