package nl.tranquilizedquality.adm.itest.business.manager;

import nl.tranquilizedquality.adm.commons.business.domain.ReleaseStatus;
import nl.tranquilizedquality.adm.itest.business.domain.MavenArtifactDto;
import nl.tranquilizedquality.adm.itest.business.domain.ReleaseDto;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.condition.ConditionRunner;

/**
 * Manager that provides services to perform release related actions.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 15 feb. 2013
 */
public interface ReleaseManagementManager {

    /**
     * Cleans up all the release related data.
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
     * Adds a new release based on the passed in {@link ReleaseDto}.
     * 
     * @param release
     *        The release data that will be used to create a release.
     */
    void addRelease(ReleaseDto release);

    /**
     * Adds the specified artifact to the specified release.
     * 
     * @param release
     *        The release that will be edited.
     * @param artifact
     *        The artifact that will be added.
     */
    void addArtifactToRelease(ReleaseDto release, MavenArtifactDto artifact);

    /**
     * Deploys the specified release.
     * 
     * @param release
     *        The release that will be deployed.
     * @param environmentName
     *        The name of the environment to deploy to.
     */
    void deployReleaseToEnvironment(ReleaseDto release, String environmentName);

    /**
     * Searches for the release status of the release.
     * 
     * @param release
     *        The release where the status will be retrieved for.
     * @return Returns a {@link ReleaseStatus}.
     */
    ReleaseStatus findReleaseStatusForRelease(ReleaseDto release);

}
