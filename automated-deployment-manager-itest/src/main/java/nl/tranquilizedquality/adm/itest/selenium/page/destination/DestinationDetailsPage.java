/*
 * @(#)DestinationDetailsPage.java 1 feb. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.itest.selenium.page.destination;

import nl.tranquilizedquality.adm.itest.selenium.page.AbstractAdmSeleniumPage;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.condition.ConditionRunner;

/**
 * Page where the details of a destination are displayed on.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 1 feb. 2013
 */
public class DestinationDetailsPage extends AbstractAdmSeleniumPage {

    /** XPath definition for the menu item to logout with. */
    private static final String DESTINATION_NAME_FIELD = "destination-details-pnl-name-input";

    /** XPath definition for the combobox arrow button to show the items you can select from. */
    private static final String HOST_COMBO_ARROW = "//div[@id='destination-details-pnl-host']/img[1]";

    /** XPath definition for the first host combo list item. */
    private static final String HOST_COMBO_LIST_ITEM = "//html/body/div[6]/div[1]/div[1]";

    /** XPath definition for the combobox arrow button to show the items you can select from. */
    private static final String PROTOCOL_COMBO_ARROW = "//div[@id='destination-details-pnl-protocol']/img[1]";

    /** XPath definition for the SSH combo list item. */
    private static final String SSH_COMBO_LIST_ITEM = "//html/body/div[6]/div[1]/div[3]";

    /** XPath definition for the combobox arrow button to show the items you can select from. */
    private static final String GROUP_COMBO_ARROW = "//div[@id='destination-details-pnl-user-group']/img[1]";

    /** XPath definition for the first group combo list item. */
    private static final String GROUP_COMBO_LIST_ITEM = "//html/body/div[6]/div[1]/div[1]";

    /** XPath definition for the combobox arrow button to show the items you can select from. */
    private static final String DEPLOYER_COMBO_ARROW = "//div[@id='destination-details-pnl-deployer-id']/img[1]";

    /** XPath definition for the Shell script combo list item. */
    private static final String SHELL_SCRIPT_COMBO_LIST_ITEM = "//html/body/div[6]/div[1]/div[2]";

    /** XPath definition for the combobox arrow button to show the items you can select from. */
    private static final String ENVIRONMENT_COMBO_ARROW = "//div[@id='destination-details-pnl-environment']/img[1]";

    /** XPath definition for the Tomcat combo list item. */
    private static final String ENVIRONMENT_COMBO_LIST_ITEM = "//html/body/div[6]/div[1]/div[1]";

    /** XPath definition for the save destination button. */
    private static final String SAVE_DESTINATION_BTN = "save-destination-btn";

    /** XPath definition for the OK button on the confirmation pop-up. */
    private static final String SAVE_DESTINATION_CONFIRMATION_BTN =
            "//div[6]/div[2]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/table[1]/tbody/tr[1]/td[1]/table[1]/tbody/tr[1]/td[1]/table[1]/tbody/tr[2]/td[2]/em/button";

    /** XPath definition for the add parameter button. */
    private static final String ADD_DEPLOYER_PARAMETER_BTN = "add-deployer-parameter-btn";

    /** XPath definition for the delete parameter button. */
    private static final String DELETE_DEPLOYER_PARAMETER_BTN = "delete-deployer-parameter-btn";

    /** XPath definition for the deployer parameter value field. */
    private static final String DEPLOYER_PARAMETER_VALUE_FIELD = "deployer-parameter-details-pnl-value-input";

    /** XPath definition for the combobox arrow button to show the items you can select from. */
    private static final String DEPLOYER_PARAMETER_TYPE_COMBO_ARROW = "//div[@id='deployer-parameter-details-pnl-type']/img[1]";

    /** XPath definition for the first parameter type combo list item. */
    private static final String DEPLOYER_PARAMETER_TYPE_COMBO_LIST_ITEM = "//html/body/div[8]/div[1]/div[1]";

    /** XPath definition for the deployer parameter save button. */
    private static final String DEPLOYER_PARAMETER_SAVE_BTN = "save-deployer-parameter-btn";

    private static final String DEPLOYER_PARAMETER_CONFIRMATION_BTN =
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
    public DestinationDetailsPage(final Selenium selenium, final ConditionRunner conditionRunner) {
        super(selenium, conditionRunner);
    }

    /**
     * Adds a default shell script based destination.
     * 
     * @param destinationName
     *        The name of the destination to use that will be created.
     */
    public void addDefaultShellScriptDesitnation(final String destinationName) {

        typeTextInTextField(DESTINATION_NAME_FIELD, destinationName);

        expandAndselectItemFromCombo(HOST_COMBO_ARROW, HOST_COMBO_LIST_ITEM);
        expandAndselectItemFromCombo(PROTOCOL_COMBO_ARROW, SSH_COMBO_LIST_ITEM);
        expandAndselectItemFromCombo(GROUP_COMBO_ARROW, GROUP_COMBO_LIST_ITEM);
        expandAndselectItemFromCombo(DEPLOYER_COMBO_ARROW, SHELL_SCRIPT_COMBO_LIST_ITEM);
        expandAndselectItemFromCombo(ENVIRONMENT_COMBO_ARROW, ENVIRONMENT_COMBO_LIST_ITEM);

        pause(1000);

        selenium.click(SAVE_DESTINATION_BTN);

        waitForElementToBePresent("No confirmation button present.", SAVE_DESTINATION_CONFIRMATION_BTN);
        selenium.click(SAVE_DESTINATION_CONFIRMATION_BTN);
    }

    /**
     * Adds a deployer parameter to a destination. NOTE: Make sure you are already on the
     * destination details panel.
     * 
     * @param parameterValue
     *        The value of the parameter that will be used.
     */
    public void addParameter(final String parameterValue) {
        waitForElementToBePresent("No add deployer parameter button!", ADD_DEPLOYER_PARAMETER_BTN);
        selenium.click(ADD_DEPLOYER_PARAMETER_BTN);

        typeTextInTextField(DEPLOYER_PARAMETER_VALUE_FIELD, parameterValue);

        expandAndselectItemFromCombo(DEPLOYER_PARAMETER_TYPE_COMBO_ARROW, DEPLOYER_PARAMETER_TYPE_COMBO_LIST_ITEM);

        pause(1000);
        selenium.click(DEPLOYER_PARAMETER_SAVE_BTN);

        pause(1000);
        selenium.click(DEPLOYER_PARAMETER_CONFIRMATION_BTN);
    }

}
