/*
 * @(#)MavenArtifactDto.java 18 feb. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.itest.business.domain;

import nl.tranquilizedquality.adm.commons.business.domain.ArtifactType;

/**
 * Representation of a Maven artifact.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 18 feb. 2013
 */
public class MavenArtifactDto {

    /** The name of the Maven module. */
    private String name;

    /** The type of artifact. */
    private ArtifactType type;

    /** The maven group of the module. */
    private String group;

    /** The maven artifact id. */
    private String artifactId;

    /** The version of the artifact. */
    private String version;

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

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

}
