/*
 * @(#)AbstractAdmSeleniumPage.java 25 jan. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.itest.selenium.page;

import org.jbehave.web.selenium.SeleniumPage;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.Wait;
import com.thoughtworks.selenium.Wait.WaitTimedOutException;
import com.thoughtworks.selenium.condition.ConditionRunner;

/**
 * Abstract base class for a ADM selenium page.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 25 jan. 2013
 */
public abstract class AbstractAdmSeleniumPage extends SeleniumPage {

    /**
     * Constructor taking the selenium object so we can perform actions on the page and the
     * condition runner that drives the test case.
     * 
     * @param selenium
     *        The selenium object used to perform action on the login page.
     * @param conditionRunner
     *        The condition runner used to run the test case.
     */
    public AbstractAdmSeleniumPage(final Selenium selenium, final ConditionRunner conditionRunner) {
        super(selenium, conditionRunner);
    }

    /**
     * Pauses for the specified number of miliseconds.
     * 
     * @param miliseconds
     *        The miliseconds the current thread will wait.
     */
    protected void pause(final long miliseconds) {
        try {
            Thread.sleep(miliseconds);
        } catch (final InterruptedException e) {
        }
    }

    /**
     * Wait for a specific element to become present on a page, before continuing.
     * 
     * @param messageToShowIfTimeOut
     *        The message that should be shown when a time out occurs.
     * @param elementLocator
     *        The locator id used by selenium to see if a specific element is present.
     * @throws InterruptedException
     *         Is thrown when something goes wrong.
     */
    protected void waitForElementToBePresent(final String messageToShowIfTimeOut, final String elementLocator) {

        try {
            new Wait(messageToShowIfTimeOut) {

                @Override
                public boolean until() {
                    return AbstractAdmSeleniumPage.this.selenium.isElementPresent(elementLocator);
                }
            };
        } catch (final WaitTimedOutException e) {
            // Wait for 5 additional seconds, to be certain everything gets loaded.
            try {
                Thread.sleep(5000);
            } catch (final InterruptedException e1) {
            }
        }
        // Always wait 2 additional seconds, to be certain everything gets loaded.
        try {
            Thread.sleep(2000);
        } catch (final InterruptedException e) {
        }
    }

    /**
     * Types text in a GXT text field.
     * 
     * @param fieldIdentifier
     *        The identifier of the field.
     * @param value
     *        The value that will be typed.
     */
    protected void typeTextInTextField(final String fieldIdentifier, final String value) {
        selenium.focus(fieldIdentifier);
        selenium.type(fieldIdentifier, value);
        selenium.fireEvent(fieldIdentifier, "blur");
    }

    /**
     * Clicks on the combo arrow button to expand the values you can choose from.
     * 
     * @param comboArrowLocator
     *        The locator that points to the combo arrow button.
     */
    protected void expandComboBoxItemList(final String comboArrowLocator) {
        waitForElementToBePresent("Dropdown not found!", comboArrowLocator);
        selenium.click(comboArrowLocator);
    }

    /**
     * Selects an item from an expanded combo box.
     * 
     * @param comboArrowLocator
     *        The locator that points to the combo arrow button.
     * @param comboListItemToSelect
     *        The locator id of the list item that should be selected.
     */
    protected void selectItemFromCombo(final String comboArrowLocator, final String comboListItemToSelect) {
        waitForElementToBePresent("Item not found in combo list!", comboListItemToSelect);
        selenium.mouseDown(comboListItemToSelect);
    }

    /**
     * Selects an item from a combo list.
     * 
     * @param comboArrowLocator
     *        The locator id of the arrow button in the combo list.
     * @param comboListItemToSelect
     *        The locator id of the list item that should be selected.
     */
    protected void expandAndselectItemFromCombo(final String comboArrowLocator, final String comboListItemToSelect) {
        expandComboBoxItemList(comboArrowLocator);
        selectItemFromCombo(comboArrowLocator, comboListItemToSelect);
    }

}
