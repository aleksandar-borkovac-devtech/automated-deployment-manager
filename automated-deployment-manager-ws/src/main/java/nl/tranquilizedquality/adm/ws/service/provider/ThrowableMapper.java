package nl.tranquilizedquality.adm.ws.service.provider;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * {@link ExceptionMapper} that maps {@link throwable} exceptions to a user
 * friendly response.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 1 okt. 2012
 */
public class ThrowableMapper implements ExceptionMapper<Throwable> {

    /** logger for this class */
    private static final Log LOGGER = LogFactory.getLog(ThrowableMapper.class);

    @Override
    public Response toResponse(final Throwable throwable) {
        final String message = throwable.getMessage();
        if (LOGGER.isErrorEnabled()) {
            LOGGER.error(message);
            throwable.printStackTrace();
        }

        return Response.status(Status.INTERNAL_SERVER_ERROR).entity("<html><body>" + message + "</body></html>").build();
    }

}
