/**
 * <pre>
 * Project: events-itest Created on: 24 apr. 2013 File: fAbstractBasicSeleniumSteps.java
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

import org.jbehave.core.steps.Steps;
import org.springframework.beans.factory.annotation.Required;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.condition.ConditionRunner;

/**
 * Basic selenium based JBehave step.
 *
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 24 apr. 2013
 *
 */
public abstract class AbstractBasicSeleniumSteps extends Steps {

    /** The Selenium object used to use Selenium RC. */
    private Selenium selenium;

    /** The condition runner used within Selenium. */
    private ConditionRunner conditionRunner;

    /**
     * @param selenium
     */
    @Required
    public void setSelenium(final Selenium selenium) {
        this.selenium = selenium;
    }

    /**
     * Retrieves the Selenium instance.
     *
     * @return Returns a {@link Selenium} object.
     */
    public Selenium getSelenium() {
        return selenium;
    }

    /**
     * Retrieves the condition runner.
     *
     * @return Returns a {@link ConditionRunner}.
     */
    public ConditionRunner getConditionRunner() {
        return conditionRunner;
    }

    /**
     * @param conditionRunner
     */
    @Required
    public void setConditionRunner(final ConditionRunner conditionRunner) {
        this.conditionRunner = conditionRunner;
    }

}
