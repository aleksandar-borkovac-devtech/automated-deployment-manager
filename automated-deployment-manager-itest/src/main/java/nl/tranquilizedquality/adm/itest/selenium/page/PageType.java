/*
 * @(#)PageType.java 25 jan. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.itest.selenium.page;

/**
 * All available page types in ADM.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 25 jan. 2013
 */
public enum PageType {

    /** The login page of ADM. */
    LOGIN_PAGE,

    /** The main dashboard of ADM. */
    DASH_BOARD,

    /** Tab where the details of a destination are displayed on. */
    DESTINATION_DETAILS_TAB,

    /** Tab where the details of a maven module are displayed on. */
    MAVEN_MODULE_DETAILS_TAB,

    /** Tab where maven modules can be looked up and added. */
    MAVEN_MODULE_MANAGEMENT_TAB,

    /** Tab where releases can be looked up and added. */
    RELEASE_MANAGEMENT_TAB,

    /** Tab where the details of a release are displayed on. */
    RELEASE_DETAILS_TAB,

    /** Tab where you can see the details of a host. */
    DESTINATION_HOST_DETAILS_TAB,

    /** Tab where you can search and manage hosts. */
    DESTINATION_HOST_MANAGEMENT_TAB;

}
