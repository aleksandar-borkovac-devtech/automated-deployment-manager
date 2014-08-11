package nl.tranquilizedquality.adm.gwt.gui.server.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import nl.tranquilizedquality.adm.commons.business.domain.Scope;
import nl.tranquilizedquality.adm.gwt.gui.server.rest.ImportService;
import nl.tranquilizedquality.adm.gwt.gui.server.service.scope.ImportedScope;
import nl.tranquilizedquality.adm.security.business.manager.ScopeManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

/**
 * Implementation of an {@link ImportService} exposed as RESTfull service.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 26, 2012
 * 
 */
public class ImportServiceImpl implements ImportService {

    /** logger for this class */
    private static final Log LOGGER = LogFactory.getLog(ImportServiceImpl.class);

    /** The manager used to store the imported scope. */
    private ScopeManager scopeManager;

    /** The session object that keeps state of the imported scope data. */
    private ImportedScope importedScope;

    @Override
    public Response importScope(final Scope scope) {
        /*
         * Check the input.
         */
        if (scope == null) {
            final String message = "Invalid scope provided: " + scope;
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(message);
            }

            return Response.status(Status.INTERNAL_SERVER_ERROR).entity("<html><body>" + message + "</body></html>").build();
        }

        /*
         * Set the session scope.
         */
        this.importedScope.setScope(scope);

        /*
         * Store the scope.
         */
        final Errors errors = new BindException(scope, scope.getClass().getName());

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("About to import -> " + scope);
        }

        final Scope importedScope = scopeManager.importScope(scope, errors);

        /*
         * Reset the session scope.
         */
        this.importedScope.setScope(null);
        this.importedScope.setErrorMessage("Imported scope successfully");

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Imported -> " + importedScope);
        }

        return Response.ok().entity("<html><body>Imported scope successfully!</body></html>").build();
    }

    /**
     * @param scopeManager
     *            the scopeManager to set
     */
    @Required
    public void setScopeManager(final ScopeManager scopeManager) {
        this.scopeManager = scopeManager;
    }

    /**
     * @param importedScope
     *            the importedScope to set
     */
    @Required
    public void setImportedScope(final ImportedScope importedScope) {
        this.importedScope = importedScope;
    }

}
