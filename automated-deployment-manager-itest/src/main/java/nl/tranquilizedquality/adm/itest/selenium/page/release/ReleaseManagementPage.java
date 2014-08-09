/*
 * @(#)ReleaseManagementPage.java 15 feb. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.itest.selenium.page.release;

import nl.tranquilizedquality.adm.itest.business.domain.ReleaseDto;
import nl.tranquilizedquality.adm.itest.selenium.page.AbstractAdmSeleniumPage;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.condition.ConditionRunner;

/**
 * Page where releases are displayed and you can add new ones.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 15 feb. 2013
 */
public class ReleaseManagementPage extends AbstractAdmSeleniumPage {

    /** Identifier for the release name search field. */
    private static final String RELEASE_MANAGEMENT_RELEASE_NAME_INPUT = "release-management-release-name-input";

    /** Identifier for the search button. */
    private static final String SEARCH_RELEASE_BTN = "search-release-btn";

    /** Identifier of the add release button. */
    private static final String ADD_NEW_RELEASE_BTN = "add-new-release-btn";

    private static final String FIRST_SEARCH_RESULT_FIRST_COLUMN =
            "//div[@id='release-search-result-table']/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[2]/div[1]/table[1]/tbody/tr/td[1]/div";

    /**
     * Constructor taking the selenium object so we can perform actions on the page and the
     * condition runner that drives the test case.
     * 
     * @param selenium
     *        The selenium object used to perform action on the login page.
     * @param conditionRunner
     *        The condition runner used to run the test case.
     */
    public ReleaseManagementPage(final Selenium selenium, final ConditionRunner conditionRunner) {
        super(selenium, conditionRunner);
    }

    /**
     * Opens the add release panel.
     */
    public void openAddReleasePanel() {
        click(ADD_NEW_RELEASE_BTN);
    }

    /**
     * Edits the specified release.
     * 
     * @param release
     *        The release that will be edited.
     */
    public void openDetailsPanelForRelease(final ReleaseDto release) {
        final String releaseName = release.getName();
        typeTextInTextField(RELEASE_MANAGEMENT_RELEASE_NAME_INPUT, releaseName);

        click(SEARCH_RELEASE_BTN);
        pause(1000);
        mouseDown(FIRST_SEARCH_RESULT_FIRST_COLUMN);
        pause(1000);
        click(FIRST_SEARCH_RESULT_FIRST_COLUMN);
        pause(1000);
        doubleClick(FIRST_SEARCH_RESULT_FIRST_COLUMN);
    }

}
