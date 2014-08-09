/*
 * @(#)MavenModule.java 12 feb. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.itest.business.domain;

import nl.tranquilizedquality.adm.commons.business.domain.ArtifactType;

/**
 * Representation of a maven module that can be configured in ADM.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 12 feb. 2013
 */
public class MavenModuleDto {

    /** The name of the Maven module. */
    private String name;

    /** The type of artifact. */
    private ArtifactType type;

    /** The maven group of the module. */
    private String group;

    /** The maven artifact id. */
    private String artifactId;

    /**
     * Is used for distribution packages since they have a suffix to uniquely
     * identify the distribution package.
     */
    private String identifier;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public ArtifactType getType() {
        return type;
    }

    public void setType(final ArtifactType type) {
        this.type = type;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(final String group) {
        this.group = group;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(final String artifactId) {
        this.artifactId = artifactId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(final String identifier) {
        this.identifier = identifier;
    }

}
