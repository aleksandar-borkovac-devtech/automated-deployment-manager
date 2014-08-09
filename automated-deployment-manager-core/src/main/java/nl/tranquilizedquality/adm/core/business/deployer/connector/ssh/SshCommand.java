/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 4 jun. 2011 File: SshCommand.java
 * Package: nl.tranquilizedquality.adm.core.business.deployer.connector
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
package nl.tranquilizedquality.adm.core.business.deployer.connector.ssh;

import nl.tranquilizedquality.adm.commons.business.deployer.connector.Command;

/**
 * Supported SSH commands.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 4 jun. 2011
 */
public enum SshCommand implements Command {

    /** Change directory. */
    CHANGE_DIRECTORY("cd"),

    /** List. */
    LIST("ls"),

    /** Check where you are. */
    PWD("pwd"),

    /** Removes a file. */
    REMOVE("rm"),

    /** Removes a file or directory. */
    RECURSIVE_REMOVE("rm -R"),

    /** Removes a file or directory. */
    RECURSIVE_REMOVE_FORCED("rm -Rf"),

    /** Copies a file. */
    COPY("cp"),

    /** Copies a file or directory. */
    RECURSIVE_COPY("cp -R"),

    /** Copies a file or directory. */
    RECURSIVE_COPY_FORCED("cp -Rf"),

    /** Moves a file or directory. */
    MOVE("mv"),

    /** Creates a new directory. */
    MAKE_DIRECTORY("mkdir -p");

    /** The actual command to perform. */
    private String command;

    /**
     * Constructor taking the command that will be executed.
     * 
     * @param command
     *        The command that will be set.
     */
    private SshCommand(final String command) {
        this.command = command;
    }

    /**
     * @return the command
     */
    @Override
    public String getCommand() {
        return command;
    }

}
