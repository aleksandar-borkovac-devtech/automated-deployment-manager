/*
 * @(#)AdmIntegrationTest.java 25 jan. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.itest;

import nl.tranquilizedquality.adm.itest.gui.AdmAddDestinationHostStory;
import nl.tranquilizedquality.adm.itest.gui.AdmAddDestinationStory;
import nl.tranquilizedquality.adm.itest.gui.AdmAddMavenModuleStory;
import nl.tranquilizedquality.adm.itest.gui.AdmLoginStory;
import nl.tranquilizedquality.itest.AbstractDefaultHibernateDeploymentTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Integration test suite for ADM that runs all available integration tests.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 25 jan. 2013
 */
@RunWith(Suite.class)
@SuiteClasses({AdmLoginStory.class, AdmAddDestinationStory.class, AdmAddMavenModuleStory.class, AdmAddDestinationHostStory.class })
public class AdmIntegrationTest extends AbstractDefaultHibernateDeploymentTest {

}
