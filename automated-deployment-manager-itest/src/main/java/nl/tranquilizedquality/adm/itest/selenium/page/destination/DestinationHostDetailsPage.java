/*
 * @(#)DestinationHostDetailsPage.java 8 mrt. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.itest.selenium.page.destination;

import nl.tranquilizedquality.adm.commons.business.domain.Protocol;
import nl.tranquilizedquality.adm.itest.business.domain.DestinationHostDto;
import nl.tranquilizedquality.adm.itest.selenium.page.AbstractAdmSeleniumPage;

import org.apache.commons.lang.StringUtils;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.condition.ConditionRunner;

/**
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 8 mrt. 2013
 */
public class DestinationHostDetailsPage extends AbstractAdmSeleniumPage {

    /** XPath definition for the menu item to logout with. */
    private static final String DESTINATION_HOST_NAME_FIELD = "destination-host-details-pnl-host-name-input";

    private static final String DESTINATION_HOST_PORT_FIELD = "destination-host-details-pnl-port-input";

    private static final String DESTINATION_USERNAME_FIELD = "destination-host-details-pnl-host-user-name-input";

    private static final String DESTINATION_PASSWORD_FIELD = "destination-host-details-pnl-password-input";

    private static final String DESTINATION_PRIVATE_KEY_FIELD = "destination-host-details-pnl-private-key-input";

    /**
     * XPath definition for the combobox arrow button to show the items you can
     * select from.
     */
    private static final String PROTOCOL_COMBO_ARROW = "//div[@id='destination-host-details-pnl-protocol']/img[1]";

    /**
     * XPath definition for the combobox arrow button to show the items you can
     * select from.
     */
    private static final String GROUP_COMBO_ARROW = "//div[@id='destination-host-details-pnl-group']/img[1]";

    /** XPath definition for the first group combo list item. */
    private static final String GROUP_COMBO_LIST_ITEM = "//html/body/div[6]/div[1]/div[1]";

    private static final String DESTINATION_TERMINAL_FIELD = "destination-host-details-pnl-terminal-input";

    private static final String SAVE_DESTINATION_HOST_BTN = "save-destination-host-btn";

    private static final String DESTINATION_HOST_SAVE_CONFIRMATION_BTN =
            "//html/body/div[6]/div[2]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/table[1]/tbody/tr[1]/td[1]/table[1]/tbody/tr[1]/td[1]/table[1]/tbody/tr[2]/td[2]/em/button";

    /**
     * Constructor taking the selenium object so we can perform actions on the
     * page and the condition runner that drives the test case.
     * 
     * @param selenium
     *            The selenium object used to perform action on the login page.
     * @param conditionRunner
     *            The condition runner used to run the test case.
     */
    public DestinationHostDetailsPage(final Selenium selenium, final ConditionRunner conditionRunner) {
        super(selenium, conditionRunner);
    }

    public void addDestinationHost(final DestinationHostDto destinationHost) {
        final String hostName = destinationHost.getHostName();
        final Integer port = destinationHost.getPort();
        final String username = destinationHost.getUsername();
        final String password = destinationHost.getPassword();
        final String privateKey = destinationHost.getPrivateKey();
        final Protocol protocol = destinationHost.getProtocol();
        final String terminal = destinationHost.getTerminal();

        if (StringUtils.isNotBlank(hostName)) {
            typeTextInTextField(DESTINATION_HOST_NAME_FIELD, hostName);
        }

        if (port != null) {
            typeTextInTextField(DESTINATION_HOST_PORT_FIELD, port.toString());
        }

        if (StringUtils.isNotBlank(username)) {
            typeTextInTextField(DESTINATION_USERNAME_FIELD, username);
        }

        if (StringUtils.isNotBlank(password)) {
            typeTextInTextField(DESTINATION_PASSWORD_FIELD, password);
        }

        if (protocol != null) {
            expandComboBoxItemList(PROTOCOL_COMBO_ARROW);
            pause(1000);
            final Protocol[] values = Protocol.values();
            final int numberOfProtocols = values.length + 1;
            for (int i = 1; i <= numberOfProtocols; i++) {
                final String locator = "//html/body/div[6]/div[1]/div[" + i + "]";
                final String value = getText(locator);
                if (protocol.name().equals(value)) {
                    selectItemFromCombo(PROTOCOL_COMBO_ARROW, locator);
                    break;
                }
            }
        }

        if (StringUtils.isNotBlank(terminal)) {
            typeTextInTextField(DESTINATION_TERMINAL_FIELD, terminal);
        }

        if (StringUtils.isNotBlank(privateKey)) {
            typeTextInTextField(DESTINATION_PRIVATE_KEY_FIELD, privateKey);
        }

        expandAndselectItemFromCombo(GROUP_COMBO_ARROW, GROUP_COMBO_LIST_ITEM);

        pause(1000);
        click(SAVE_DESTINATION_HOST_BTN);

        pause(1000);
        click(DESTINATION_HOST_SAVE_CONFIRMATION_BTN);

    }

}
