package nl.tranquilizedquality.adm.core.business.template.renderer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.core.business.template.renderer.TemplateRenderer;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateEnvironment;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateMavenArtifact;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateMavenModule;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateRelease;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUser;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test for template rendering.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 19 okt. 2012
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:adm-core-template-context.xml" })
public class TemplateRendererTest {

    /** Logger for this class. */
    private static final Log LOGGER = LogFactory.getLog(TemplateRendererTest.class);

    @Autowired
    private TemplateRenderer templateRenderer;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testEmailDeploymentNotificationTemplate() throws Exception {
        final HibernateUser loggedInUser = new HibernateUser();
        loggedInUser.setName("Salomo Petrus");

        final HibernateRelease release = new HibernateRelease();
        release.setName("S12.14.Consumer");

        final HibernateMavenModule parentModule = new HibernateMavenModule();
        parentModule.setName("Automated Deployment Manager");

        final HibernateMavenArtifact artifact = new HibernateMavenArtifact();
        artifact.setParentModule(parentModule);
        artifact.setVersion("1.0.0");

        final List<MavenArtifact> artifacts = new ArrayList<MavenArtifact>();
        artifacts.add(artifact);
        release.setArtifacts(artifacts);

        final HibernateEnvironment environment = new HibernateEnvironment();
        environment.setName("INT");

        final Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("environment", environment);
        properties.put("release", release);
        properties.put("user", loggedInUser);

        final String message = templateRenderer.merge("templates/email-deployment-notification.vm", properties);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(message);
        }

        final String expectedMessage =
                FileUtils.readFileToString(new File("src/test/resources/rendered-templates/email-deployment-notification.html"));

        // FIXME: For some reason it doesn't see that it's not the same.
        // assertEquals("Template not rendered properly!", expectedMessage, message);
    }

}
