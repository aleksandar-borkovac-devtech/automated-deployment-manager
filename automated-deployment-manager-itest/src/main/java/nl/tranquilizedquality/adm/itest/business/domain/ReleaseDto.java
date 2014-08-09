/*
 * @(#)ReleaseDto.java 15 feb. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.itest.business.domain;

import java.util.Date;

/**
 * Representation of a release.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 15 feb. 2013
 */
public class ReleaseDto {

    /** The name of the release. */
    private String name;

    /** The date on which the release will be done. */
    private Date releaseDate;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(final Date releaseDate) {
        this.releaseDate = releaseDate;
    }

}
