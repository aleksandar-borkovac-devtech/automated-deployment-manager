package nl.tranquilizedquality.adm.commons.gwt.ext.server.service;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.domain.PagingSearchCommand;

import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;

/**
 * Abstract base class for services.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 */
public abstract class AbstractService {

    /**
     * Creates a {@link PagingSearchCommand} based on a {@link PagingLoadConfig}
     * .
     * 
     * @param config
     *            The {@link PagingLoadConfig} that will be used to construct a
     *            {@link PagingSearchCommand}. This may not be NULL.
     * @return Returns a {@link PagingSearchCommand}.
     */
    protected PagingSearchCommand createPagingSearchCommand(final PagingLoadConfig config,
            final String defaultSortField) {
        final PagingSearchCommand sc = new PagingSearchCommand();
        final SortDir sortDir = config.getSortDir();

        /*
         * Setup sorting direction.
         */
        switch (sortDir) {
            case ASC:
            case NONE:
                sc.setAsc(true);
                break;
            case DESC:
                sc.setAsc(false);
                break;

            default:
                sc.setAsc(true);
                break;
        }

        /*
         * Setup the limit.
         */
        final int limit = config.getLimit();
        sc.setMaxResults(limit);

        /*
         * Setup the starting record.
         */
        final int offset = config.getOffset();
        sc.setStart(offset);

        /*
         * Setup sort field.
         */
        final String sortField = config.getSortField();
        if (sortField == null || "".equals(sortField)) {
            sc.setOrderBy(defaultSortField);
        }
        else {
            sc.setOrderBy(sortField);
        }

        return sc;
    }

    /**
     * Extracts all default error messages from the passed in {@link Errors}
     * object and puts them in a {@link List}.
     * 
     * @param errors
     *            The {@link Errors} object where the error messages will be
     *            retrieved from.
     * @return Returns a {@link List} containing all the error messages or an
     *         empty one if there are none.
     */
    protected List<String> createErrorList(final Errors errors) {
        final List<String> errorMessages = new ArrayList<String>();

        if (errors.hasErrors()) {
            final List<ObjectError> allErrors = errors.getAllErrors();

            for (final ObjectError objectError : allErrors) {
                final String defaultMessage = objectError.getDefaultMessage();

                errorMessages.add(defaultMessage);
            }
        }

        return errorMessages;
    }

}
