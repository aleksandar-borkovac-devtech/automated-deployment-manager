/*
 * @(#)MavenModuleMangementPage.java 13 feb. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.itest.selenium.page.module;

import nl.tranquilizedquality.adm.itest.business.domain.MavenModuleDto;
import nl.tranquilizedquality.adm.itest.selenium.page.AbstractAdmSeleniumPage;

import org.apache.commons.lang.StringUtils;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.condition.ConditionRunner;

/**
 * Represenation of the tab where you can search for maven modules.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 13 feb. 2013
 */
public class MavenModuleMangementPage extends AbstractAdmSeleniumPage {

    private static final String FIRST_SEARCH_RESULT_SECOND_COLUMN =
            "//div[@id='maven-module-search-result-table']/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[2]/div[1]/table[1]/tbody/tr/td[2]/div";

    /**
     * Constructor taking the selenium object so we can perform actions on the
     * page and the condition runner that drives the test case.
     * 
     * @param selenium
     *            The selenium object used to perform action on the login page.
     * @param conditionRunner
     *            The condition runner used to run the test case.
     */
    public MavenModuleMangementPage(final Selenium selenium, final ConditionRunner conditionRunner) {
        super(selenium, conditionRunner);
    }

    public void openDetailsPanelForModule(final MavenModuleDto module) {
        final String group = module.getGroup();
        final String artifactId = module.getArtifactId();
        final String name = module.getName();

        if (StringUtils.isNotBlank(group)) {
            typeTextInTextField("maven-module-management-artifact-group-input", group);
        }

        if (StringUtils.isNotBlank(artifactId)) {
            typeTextInTextField("maven-module-management-artifact-id-input", artifactId);
        }

        if (StringUtils.isNotBlank(name)) {
            typeTextInTextField("maven-module-management-artifact-name-input", name);
        }

        click("maven-module-management-search-btn");
        pause(1000);
        mouseDown(FIRST_SEARCH_RESULT_SECOND_COLUMN);
        pause(1000);
        click(FIRST_SEARCH_RESULT_SECOND_COLUMN);
        pause(1000);
        doubleClick(FIRST_SEARCH_RESULT_SECOND_COLUMN);
    }

}
