package nl.tranquilizedquality.adm.commons.gwt.ext.controller;

import static junit.framework.Assert.assertEquals;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.tranquilizedquality.adm.commons.gwt.ext.controller.LogoutController;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 * Test for {@link LogoutController}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 */
public class LogoutControllerTest {

    /** Controller that will be tested. */
    private LogoutController logoutController;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        logoutController = new LogoutController();
        logoutController.setViewName("logout");
    }

    @Test
    public void testHandleRequest() throws Exception {
        final HttpServletRequest request = new MockHttpServletRequest("GET", "");
        final HttpServletResponse response = new MockHttpServletResponse();

        final ModelAndView modelAndView = logoutController.handleRequest(request, response);

        final String viewName = modelAndView.getViewName();

        assertEquals("logout", viewName);
    }

}
