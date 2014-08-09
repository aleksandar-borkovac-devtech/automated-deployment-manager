package nl.tranquilizedquality.adm.itest.business.manager;

import nl.tranquilizedquality.adm.itest.business.domain.DestinationHostDto;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.condition.ConditionRunner;

/**
 * Manager that can manage hosts within ADM.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 8 mrt. 2013
 */
public interface HostManagementManager {

    /**
     * Cleans up all the destination host related data.
     */
    void cleanUp();

    /**
     * Constructor taking the selenium object so we can perform actions on the page and the
     * condition runner that drives the test case.
     * 
     * @param selenium
     *        The selenium object used to perform action on the login page.
     * @param conditionRunner
     *        The condition runner used to run the test case.
     */
    void setUpPages(Selenium selenium, ConditionRunner conditionRunner);

    /**
     * Adds a host using the values of the passed in {@link DestinationHostDto}.
     * 
     * @param host
     *        The {@link DestinationHostDto} where the values will be used from.
     */
    void addHost(DestinationHostDto host);

    /**
     * Validates if the private key is stored for the specified host.
     * 
     * @param host
     *        The {@link DestinationHostDto} that will be used to validate.
     * @return Returns true if the private key was stored otherwise it will return false.
     */
    boolean isPrivateKeyStoredForHost(DestinationHostDto host);

}
