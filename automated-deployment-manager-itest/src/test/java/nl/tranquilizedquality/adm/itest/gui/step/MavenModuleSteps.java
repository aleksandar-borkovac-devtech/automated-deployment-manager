/*
 * @(#)MavenModuleSteps.java 12 feb. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.itest.gui.step;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Set;

import nl.tranquilizedquality.adm.commons.business.domain.ArtifactType;
import nl.tranquilizedquality.adm.itest.business.domain.MavenModuleDto;
import nl.tranquilizedquality.adm.itest.business.manager.MavenModuleManagementManager;

import org.apache.commons.lang.StringUtils;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Required;

/**
 * Steps used for Maven module related stories.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 12 feb. 2013
 */
public class MavenModuleSteps extends LoginSteps {

    /** Manager used to perform actions regarding Maven module management. */
    private MavenModuleManagementManager mavenModuleManagementManager;

    /** Modules that were created during execution of the steps. */
    private final Set<MavenModuleDto> createdModules;

    /**
     * Default constructor initializing the created modules.
     */
    public MavenModuleSteps() {
        createdModules = new HashSet<MavenModuleDto>();
    }

    /**
     * Initializes the managers.
     */
    @Override
    public void init() {
        super.init();
        mavenModuleManagementManager.setUpPages(getSelenium(), getConditionRunner());
        mavenModuleManagementManager.cleanUp();
    }

    @When("the user adds a Maven module called $mavenModuleName with group name $mavenModuleGroup and artifact id $artifactId of type $artifactType")
    public void addMavenModule(@Named("mavenModuleName") final String mavenModuleName,
            @Named("mavenModuleGroup") final String mavenModuleGroup, @Named("artifactId") final String artifactId,
            @Named("artifactType") final String artifactType) {

        dashboardManager.openAddNewMavenModuleTab();

        final MavenModuleDto mavenModule = new MavenModuleDto();
        mavenModule.setArtifactId(artifactId);
        mavenModule.setGroup(mavenModuleGroup);
        mavenModule.setName(mavenModuleName);
        mavenModule.setType(ArtifactType.valueOf(artifactType));
        createdModules.add(mavenModule);

        mavenModuleManagementManager.addMavenModule(mavenModule);
    }

    @Then("there should be $numberOfExpectedModuels module(s) created")
    public void validateNumberOfModulesCreated(@Named("numberOfExpectedModuels") final Integer numberOfExpectedModules) {
        final Integer numberOfModules = mavenModuleManagementManager.countNumberOfModules();
        assertEquals("Invalid number of modules created!", numberOfExpectedModules, numberOfModules);
    }

    @When("the user adds a dependency $dependencyModuleName to the Maven module called $moduleName")
    public void addDependencyToModule(@Named("dependencyModuleName") final String dependencyModuleName,
            @Named("moduleName") final String moduleName) {

        MavenModuleDto dependencyModule = null;
        MavenModuleDto module = null;

        for (final MavenModuleDto createdModule : createdModules) {
            final String name = createdModule.getName();
            if (StringUtils.equals(name, moduleName)) {
                module = createdModule;
            } else if (StringUtils.equals(name, dependencyModuleName)) {
                dependencyModule = createdModule;
            }
        }

        assertNotNull("No module found called " + moduleName, module);
        assertNotNull("No dependency found called " + dependencyModuleName, dependencyModule);

        mavenModuleManagementManager.addDependency(module, dependencyModule);
    }

    @Then("there should be $numberOfDependencies dependency created for the Maven module called $moduleName")
    public void validateNumberOfDependenciesCreated(@Named("numberOfDependencies") final Integer expectedNumberOfDependencies,
            @Named("moduleName") final String moduleName) {

        MavenModuleDto module = null;
        for (final MavenModuleDto createdModule : createdModules) {
            final String name = createdModule.getName();
            if (StringUtils.equals(name, moduleName)) {
                module = createdModule;
            }
        }

        assertNotNull("No module found called " + moduleName, module);

        final Integer nubmerOfDependencies = mavenModuleManagementManager.countNumberOfDependenciesForModule(module);
        assertEquals("Invalid number of dependencies created!", expectedNumberOfDependencies, nubmerOfDependencies);
    }

    @Required
    public void setMavenModuleManagementManager(final MavenModuleManagementManager mavenModuleManagementManager) {
        this.mavenModuleManagementManager = mavenModuleManagementManager;
    }

}
