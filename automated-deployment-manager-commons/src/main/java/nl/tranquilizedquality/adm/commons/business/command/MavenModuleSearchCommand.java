/**
 * <pre>
 * Project: automated-deployment-manager-commons Created on: 22 okt. 2012 File: MavenModuleSearchCommand.java
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

import nl.tranquilizedquality.adm.commons.business.domain.ArtifactType;
import nl.tranquilizedquality.adm.commons.business.domain.MavenModule;

/**
 * Search criteria for a {@link MavenModule}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 22 okt. 2012
 */
public class MavenModuleSearchCommand extends AbstractPagingUserGroupSearchCommand {

    /**
     * Unique identifier for serialization.
     */
    private static final long serialVersionUID = 6554046981748294146L;

    /** The name of the Maven module. */
    private String name;

    /** The type of artifact. */
    private ArtifactType type;

    /** The maven group of the module. */
    private String group;

    /** The maven artifact id. */
    private String artifactId;

    /** The unique identifier of the maven module that should be excluded. */
    private Long excludeMavenModuleId;

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
     * @return the type
     */
    public ArtifactType getType() {
        return type;
    }

    /**
     * @param type
     *        the type to set
     */
    public void setType(final ArtifactType type) {
        this.type = type;
    }

    /**
     * @return the group
     */
    public String getGroup() {
        return group;
    }

    /**
     * @param group
     *        the group to set
     */
    public void setGroup(final String group) {
        this.group = group;
    }

    /**
     * @return the artifactId
     */
    public String getArtifactId() {
        return artifactId;
    }

    /**
     * @param artifactId
     *        the artifactId to set
     */
    public void setArtifactId(final String artifactId) {
        this.artifactId = artifactId;
    }

    public Long getExcludeMavenModuleId() {
        return excludeMavenModuleId;
    }

    public void setExcludeMavenModuleId(final Long excludeMavenModuleId) {
        this.excludeMavenModuleId = excludeMavenModuleId;
    }

}
