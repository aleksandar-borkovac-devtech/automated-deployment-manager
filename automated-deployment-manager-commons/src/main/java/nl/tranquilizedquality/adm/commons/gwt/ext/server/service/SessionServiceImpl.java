package nl.tranquilizedquality.adm.commons.gwt.ext.server.service;

import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.SessionService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Service to retrieve session details.
 *
 * @author e-pragt (erik.pragt@Tranquilized Quality.com)
 * @since 10/25/12
 */
public class SessionServiceImpl implements SessionService {

    /** logger for this class */
    private static final Log LOGGER = LogFactory.getLog(SessionServiceImpl.class);

    @Override
    public Integer getUserSessionTimeout() {
        final HttpSession session = getSession();
        final int inactiveInterval = session.getMaxInactiveInterval() * 1000;

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Current inactiveInterval: " + inactiveInterval);
        }

        return inactiveInterval;
    }

    private HttpSession getSession() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        final HttpServletRequest request = attributes.getRequest();
        return request.getSession();
    }
}
