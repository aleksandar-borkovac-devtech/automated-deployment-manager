/**
 * <pre>
 * Project: automated-deployment-manager-commons Created on: 7 jul. 2011 File: LocationType.java
 * Package: nl.tranquilizedquality.adm.commons.business.domain
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
package nl.tranquilizedquality.adm.commons.business.domain;

/**
 * All supported location types.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 7 jul. 2011
 */
public enum DeployerParameterType {

    /** Location where JAR files are stored. */
    JAR_LOCATION,

    /** Location of the HOME directory of the application server. */
    APP_SERVER_LOCATION,

    /** Location of the temporary storage on a server. */
    TMP_LOCATION,

    /** Location where backup files can be stored. */
    BACKUP_LOCATION,

    /** Location where web application can be deployed. */
    WEB_APPS_LOCATION,

    /** The context path that will be used when a WAR is deployed. */
    CONTEXT_PATH,

    /** A parameter that can be passed to a shell script. */
    SCRIPT_PARAMETER;

}
