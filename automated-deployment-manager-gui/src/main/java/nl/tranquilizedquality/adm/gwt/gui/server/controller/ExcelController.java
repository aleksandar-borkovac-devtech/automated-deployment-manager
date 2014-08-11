package nl.tranquilizedquality.adm.gwt.gui.server.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.gwt.gui.server.service.user.UserDataBean;
import nl.tranquilizedquality.adm.security.business.command.UserSearchCommand;
import nl.tranquilizedquality.adm.security.business.manager.UserManager;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.mvc.Controller;

/**
 * Implementation of a {@link Controller} that handles the export of a list of
 * users to an Excel file.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 26, 2012
 * 
 */
public class ExcelController extends AbstractController {

    /** The manager that will be used to retrieve users. */
    private UserManager userManager;

    /** Data bean to retrieve user specific data. */
    private UserDataBean userDataBean;

    @Override
    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {

        final UserSearchCommand sc = userDataBean.getData("sc");

        final List<User> users = userManager.findUsers(sc);

        final ModelAndView mav = new ModelAndView("xl", "userlist", users);

        return mav;
    }

    /**
     * @param userManager
     *            the userManager to set
     */
    @Required
    public void setUserManager(final UserManager userManager) {
        this.userManager = userManager;
    }

    /**
     * @param userDataBean
     *            the userDataBean to set
     */
    @Required
    public void setUserDataBean(final UserDataBean userDataBean) {
        this.userDataBean = userDataBean;
    }

}
