/*
 * @(#)MavenModuleDetailsPage.java 12 feb. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.itest.selenium.page.module;

import static junit.framework.Assert.assertEquals;
import nl.tranquilizedquality.adm.commons.business.domain.ArtifactType;
import nl.tranquilizedquality.adm.itest.business.domain.MavenModuleDto;
import nl.tranquilizedquality.adm.itest.selenium.page.AbstractAdmSeleniumPage;

import org.apache.commons.lang.StringUtils;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.condition.ConditionRunner;

/**
 * Representation of the page where you can manage a Maven module.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 12 feb. 2013
 */
public class MavenModuleDetailsPage extends AbstractAdmSeleniumPage {

    private static final String ADD_AVAILABLE_DEPENDENCY_BTN = "add-available-dependency-btn";

    private static final String FIRST_AVAILABLE_DEPENDENCY =
            "//div[@id='maven-module-available-dependencies-tbl']/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div/div/table/tbody/tr/td[3]/div";

    /** Locator that identifies the button that can be used to save a maven module. */
    private static final String SAVE_ARTIFACT_BTN = "save-artifact-btn";

    /** XPath definition for the maven module identifier text field. */
    private static final String MAVEN_MODULE_DETAILS_PNL_IDENTIFIER_INPUT = "maven-module-details-pnl-identifier-input";

    /** XPath definition for the maven module artifact id text field. */
    private static final String MAVEN_MODULE_DETAILS_PNL_ARTIFACT_ID_INPUT = "maven-module-details-pnl-artifact-id-input";

    /** XPath definition for the maven module group text field. */
    private static final String MAVEN_MODULE_DETAILS_PNL_GROUP_INPUT = "maven-module-details-pnl-group-input";

    /** XPath definition for the maven module name text field. */
    private static final String MAVEN_MODULE_DETAILS_PNL_NAME_INPUT = "maven-module-details-pnl-name-input";

    /** XPath definition for the combobox arrow button to show the items you can select from. */
    private static final String MAVEN_MODULE_TYPE_COMBO_ARROW = "//div[@id='maven-module-details-pnl-type']/img[1]";

    /** XPath definition for the combobox arrow button to show the items you can select from. */
    private static final String MAVEN_MODULE_GROUP_COMBO_ARROW = "//div[@id='maven-module-details-pnl-user-group']/img[1]";

    /** XPath definition for the first group combo list item. */
    private static final String MAVEN_MODULE_GROUP_COMBO_LIST_ITEM = "//html/body/div[6]/div[1]/div[1]";

    /**
     * XPath definition for the confirmation button on the pop-up window when saving a maven module.
     */
    private static final String MAVEN_MODULE_SAVE_CONFIRMATION_BTN =
            "//html/body/div[6]/div[2]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/table[1]/tbody/tr[1]/td[1]/table[1]/tbody/tr[1]/td[1]/table[1]/tbody/tr[2]/td[2]/em/button";

    /**
     * XPath definition for the confirmation button on the pop-up window when saving a maven module.
     */
    private static final String MAVEN_MODULE_SAVE_CONFIRMATION_ADD_DEPENDENCY_BTN =
            "//html/body/div[7]/div[2]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/table[1]/tbody/tr[1]/td[1]/table[1]/tbody/tr[1]/td[1]/table[1]/tbody/tr[2]/td[2]/em/button";

    /**
     * Constructor taking the selenium object so we can perform actions on the page and the
     * condition runner that drives the test case.
     * 
     * @param selenium
     *        The selenium object used to perform action on the login page.
     * @param conditionRunner
     *        The condition runner used to run the test case.
     */
    public MavenModuleDetailsPage(final Selenium selenium, final ConditionRunner conditionRunner) {
        super(selenium, conditionRunner);
    }

    /**
     * Adds a maven module with the specified input data.
     * 
     * @param mavenModule
     *        The input data that will be used.
     */
    public void addMavenModule(final MavenModuleDto mavenModule) {
        final String name = mavenModule.getName();
        final String group = mavenModule.getGroup();
        final String artifactId = mavenModule.getArtifactId();
        final String identifier = mavenModule.getIdentifier();
        final ArtifactType artifactType = mavenModule.getType();

        typeTextInTextField(MAVEN_MODULE_DETAILS_PNL_NAME_INPUT, name);
        typeTextInTextField(MAVEN_MODULE_DETAILS_PNL_GROUP_INPUT, group);
        typeTextInTextField(MAVEN_MODULE_DETAILS_PNL_ARTIFACT_ID_INPUT, artifactId);
        if (StringUtils.isNotBlank(identifier)) {
            typeTextInTextField(MAVEN_MODULE_DETAILS_PNL_IDENTIFIER_INPUT, identifier);
        }

        expandComboBoxItemList(MAVEN_MODULE_TYPE_COMBO_ARROW);
        pause(1000);
        final ArtifactType[] values = ArtifactType.values();
        final int numberOfTypes = values.length + 1;
        for (int i = 1; i <= numberOfTypes; i++) {
            final String locator = "//html/body/div[6]/div[1]/div[" + i + "]";
            final String value = getText(locator);
            if (artifactType.name().equals(value)) {
                selectItemFromCombo(MAVEN_MODULE_TYPE_COMBO_ARROW, locator);
            }
        }

        expandAndselectItemFromCombo(MAVEN_MODULE_GROUP_COMBO_ARROW, MAVEN_MODULE_GROUP_COMBO_LIST_ITEM);

        pause(1000);
        click(SAVE_ARTIFACT_BTN);

        pause(1000);
        click(MAVEN_MODULE_SAVE_CONFIRMATION_BTN);
    }

    /**
     * Adds a dependency to the maven module with the name that is specified in the
     * {@link MavenModuleDto}.
     * 
     * @param dependency
     *        The module that should be selected as dependency.
     */
    public void addDependency(final MavenModuleDto dependency) {
        pause(1000);
        mouseDown(FIRST_AVAILABLE_DEPENDENCY);
        final String moduleName = getText(FIRST_AVAILABLE_DEPENDENCY);
        assertEquals("Invalid module selected!", dependency.getName(), moduleName);
        pause(100);
        click(ADD_AVAILABLE_DEPENDENCY_BTN);
        pause(1000);
        click(MAVEN_MODULE_SAVE_CONFIRMATION_ADD_DEPENDENCY_BTN);
    }

}
