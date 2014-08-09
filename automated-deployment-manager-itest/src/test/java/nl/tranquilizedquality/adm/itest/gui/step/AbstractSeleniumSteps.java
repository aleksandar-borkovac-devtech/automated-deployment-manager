/**
 * <pre>
 * Project: automated-deployment-manager-itest Created on: 24 apr. 2013 File: fAbstractSeleniumSteps.java
 * Package: com.dizizid.events.itest.step
 *
 * Copyright (c) 2013 Tranquilized Quality www.tq-quality.nl All rights
 * reserved.
 *
 * This software is the confidential and proprietary information of Tranquilized Quality
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Tranquilized Quality.
 * </pre>
 */
package nl.tranquilizedquality.adm.itest.gui.step;

import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;

import com.thoughtworks.selenium.Selenium;

/**
 * Abstract base class for steps that need browser interaction to perform steps
 * using Selenium RC.
 *
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 24 apr. 2013
 *
 */
public abstract class AbstractSeleniumSteps extends AbstractBasicSeleniumSteps {

    @BeforeScenario
    public void setUpSelenium() {
        final Selenium selenium = getSelenium();
        selenium.start();
    }

    @AfterScenario
    public void shutDownSelenium() {
        final Selenium selenium = getSelenium();
        selenium.stop();
    }

}
