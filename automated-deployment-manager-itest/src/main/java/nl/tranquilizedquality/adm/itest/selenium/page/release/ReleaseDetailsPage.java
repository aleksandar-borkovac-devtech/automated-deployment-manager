/*
 * @(#)ReleaseDetailsPage.java 15 feb. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.itest.selenium.page.release;

import java.text.SimpleDateFormat;
import java.util.Date;

import nl.tranquilizedquality.adm.commons.business.domain.ReleaseStatus;
import nl.tranquilizedquality.adm.itest.business.domain.MavenArtifactDto;
import nl.tranquilizedquality.adm.itest.business.domain.ReleaseDto;
import nl.tranquilizedquality.adm.itest.selenium.page.AbstractAdmSeleniumPage;

import org.apache.commons.lang.StringUtils;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.condition.ConditionRunner;

/**
 * Page where the release details are displayed on.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 15 feb. 2013
 */
public class ReleaseDetailsPage extends AbstractAdmSeleniumPage {

    private static final String DEPLOY_RELEASE_BTN = "deploy-release-btn";

    private static final String ARTIFACT_SELECTION_SAVE_ARTIFACT_BTN = "artifact-selection-save-artifact-btn";

    private static final String ARTIFACT_SELECTION_DETAILS_PNL_VERSION_INPUT = "artifact-selection-details-pnl-version-input";

    private static final String MAVEN_MODULE_SELECTION_SEARCH_BTN = "maven-module-selection-search-btn";

    private static final String MAVEN_MODULE_SELECTION_ARTIFACT_NAME_INPUT = "maven-module-selection-artifact-name-input";

    private static final String RELEASE_DETAILS_ADD_ARTIFACT_BTN = "release-details-add-artifact-btn";

    /** Identifier for the save release button. */
    private static final String SAVE_RELEASE_BTN = "save-release-btn";

    /** Identifier for the release date input field. */
    private static final String RELEASE_DETAILS_PNL_RELEASE_DATE_INPUT = "release-details-pnl-release-date-input";

    /** Identifier for the release name input field. */
    private static final String RELEASE_DETAILS_PNL_NAME_INPUT = "release-details-pnl-name-input";

    /**
     * XPath definition for the combobox arrow button to show the items you can
     * select from.
     */
    private static final String GROUP_COMBO_ARROW = "//div[@id='release-details-pnl-user-group']/img[1]";

    /** XPath definition for the first group combo list item. */
    private static final String GROUP_COMBO_LIST_ITEM = "//html/body/div[7]/div[1]/div[1]";

    /**
     * XPath definition for the combobox arrow button to show the items you can
     * select from.
     */
    private static final String RELEASE_STATUS_COMBO_ARROW = "//div[@id='release-details-pnl-status']/img[1]";

    /** XPath definition for the first group combo list item. */
    private static final String RELEASE_STATUS_COMBO_LIST_ITEM = "//html/body/div[7]/div[1]/div[1]";

    /**
     * XPath definition for the confirmation button on the pop-up window when
     * saving a release.
     */
    private static final String RELEASE_SAVE_CONFIRMATION_BTN =
            "//html/body/div[7]/div[2]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/table[1]/tbody/tr[1]/td[1]/table[1]/tbody/tr[1]/td[1]/table[1]/tbody/tr[2]/td[2]/em/button";

    private static final String FIRST_SEARCH_RESULT_FIRST_COLUMN =
            "//div[@id='maven-module-selection-table']/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[2]/div[1]/table[1]/tbody/tr/td[1]/div";

    private static final String DEPLOY_RELEASE_CONFIRMATION_BTN =
            "//html/body/div[7]/div[2]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/table[1]/tbody/tr[1]/td[1]/table[1]/tbody/tr[1]/td[1]/table[1]/tbody/tr[2]/td[2]/em/button";

    /**
     * Constructor taking the selenium object so we can perform actions on the
     * page and the condition runner that drives the test case.
     * 
     * @param selenium
     *            The selenium object used to perform action on the login page.
     * @param conditionRunner
     *            The condition runner used to run the test case.
     */
    public ReleaseDetailsPage(final Selenium selenium, final ConditionRunner conditionRunner) {
        super(selenium, conditionRunner);
    }

    /**
     * Fills in the details based on the passed in {@link ReleaseDto} and stores
     * the release.
     * 
     * @param release
     *            The release that will be added.
     */
    public void addRelease(final ReleaseDto release) {
        final String releaseName = release.getName();
        final Date releaseDate = release.getReleaseDate();
        final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        final String formattedDate = formatter.format(releaseDate);

        typeTextInTextField(RELEASE_DETAILS_PNL_NAME_INPUT, releaseName);
        typeTextInTextField(RELEASE_DETAILS_PNL_RELEASE_DATE_INPUT, formattedDate);
        expandAndselectItemFromCombo(GROUP_COMBO_ARROW, GROUP_COMBO_LIST_ITEM);

        expandComboBoxItemList(RELEASE_STATUS_COMBO_ARROW);
        pause(1000);
        final int size = ReleaseStatus.values().length;
        for (int i = 1; i <= size; i++) {
            final String locator = "//html/body/div[7]/div[1]/div[" + i + "]";
            final String selectionStatus = getText(locator);
            if (StringUtils.equals(ReleaseStatus.READY.name(), selectionStatus)) {
                selectItemFromCombo(RELEASE_STATUS_COMBO_ARROW, locator);
                break;
            }
        }

        pause(1000);
        click(SAVE_RELEASE_BTN);

        pause(1000);
        click(RELEASE_SAVE_CONFIRMATION_BTN);
    }

    public void addArtifactToRelease(final ReleaseDto release, final MavenArtifactDto artifact) {
        click(RELEASE_DETAILS_ADD_ARTIFACT_BTN);
        pause(1000);

        final String version = artifact.getVersion();
        final String artifactName = artifact.getName();
        typeTextInTextField(MAVEN_MODULE_SELECTION_ARTIFACT_NAME_INPUT, artifactName);

        click(MAVEN_MODULE_SELECTION_SEARCH_BTN);
        pause(1000);
        mouseDown(FIRST_SEARCH_RESULT_FIRST_COLUMN);
        pause(1000);
        click(FIRST_SEARCH_RESULT_FIRST_COLUMN);
        pause(1000);
        doubleClick(FIRST_SEARCH_RESULT_FIRST_COLUMN);

        pause(1000);
        typeTextInTextField(ARTIFACT_SELECTION_DETAILS_PNL_VERSION_INPUT, version);

        pause(1000);
        click(ARTIFACT_SELECTION_SAVE_ARTIFACT_BTN);
    }

    public void deployReleaseToEnvironment(final ReleaseDto release, final String environmentName) {
        click(DEPLOY_RELEASE_BTN);
        pause(1000);

        click(DEPLOY_RELEASE_CONFIRMATION_BTN);
        pause(1000);

        for (int i = 1; i < 5; i++) {
            final String locator =
                    "//div[@id='environment-selection-table']/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/div[" + i
                            + "]/table[1]/tbody/tr/td[2]/div";
            final String selectionEnvironmentName = getText(locator);
            if (StringUtils.equals(environmentName, selectionEnvironmentName)) {
                mouseDown(locator);
                pause(1000);
                click(locator);
                pause(1000);
                doubleClick(locator);
                break;
            }
        }
        pause(10000);

    }

}
