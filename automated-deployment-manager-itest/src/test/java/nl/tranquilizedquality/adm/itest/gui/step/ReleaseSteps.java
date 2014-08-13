/*
 * @(#)ReleaseSteps.java 12 feb. 2013
 *
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.itest.gui.step;

import static junit.framework.Assert.assertEquals;

import java.util.Date;

import nl.tranquilizedquality.adm.commons.business.domain.ReleaseStatus;
import nl.tranquilizedquality.adm.itest.business.domain.MavenArtifactDto;
import nl.tranquilizedquality.adm.itest.business.domain.ReleaseDto;
import nl.tranquilizedquality.adm.itest.business.manager.ReleaseManagementManager;

import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Required;

/**
 * Steps for managing releases.
 *
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 12 feb. 2013
 */
public class ReleaseSteps extends LoginSteps {

    /** Manager used to perform release related actions. */
    private ReleaseManagementManager releaseManagementManager;

    /**
     * Initializes the managers.
     */
    @Override
    public void init() {
        super.init();
        releaseManagementManager.setUpPages(getSelenium(), getConditionRunner());
        releaseManagementManager.cleanUp();
    }

    @When("the user creates a release called $releaseName")
    public void createRelease(@Named("releaseName") final String releaseName) {
        final ReleaseDto release = new ReleaseDto();
        release.setName(releaseName);
        release.setReleaseDate(new Date());

        releaseManagementManager.addRelease(release);
    }

    @When("adds an artifact called $artifactName with version $version to the release $releaseName")
    public void addArtifactToRelease(@Named("artifactName") final String artifactName, @Named("version") final String version,
            @Named("releaseName") final String releaseName) {
        final ReleaseDto release = new ReleaseDto();
        release.setName(releaseName);

        final MavenArtifactDto mavenArtifact = new MavenArtifactDto();
        mavenArtifact.setName(artifactName);
        mavenArtifact.setVersion(version);

        releaseManagementManager.addArtifactToRelease(release, mavenArtifact);
    }

    @When("the user deploys the release $releaseName to $environment")
    public void deployRelease(@Named("releaseName") final String releaseName, @Named("environment") final String environmentName) {
        final ReleaseDto release = new ReleaseDto();
        release.setName(releaseName);

        releaseManagementManager.deployReleaseToEnvironment(release, environmentName);
    }

    @Then("the deployment status of the release $releaseName should be $releaseStatus")
    public void validateReleaseStatus(@Named("releaseName") final String releaseName,
            @Named("releaseStatus") final String expectedReleaseStatus) {

        final ReleaseDto release = new ReleaseDto();
        release.setName(releaseName);

        final ReleaseStatus releaseStatus = releaseManagementManager.findReleaseStatusForRelease(release);
        assertEquals("Invalid release status!", ReleaseStatus.valueOf(expectedReleaseStatus), releaseStatus);
    }

    @Required
    public void setReleaseManagementManager(final ReleaseManagementManager releaseManagementManager) {
        this.releaseManagementManager = releaseManagementManager;
    }

}
