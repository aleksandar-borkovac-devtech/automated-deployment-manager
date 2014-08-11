package nl.tranquilizedquality.adm.gwt.gui.server.service.scope;

import nl.tranquilizedquality.adm.commons.business.domain.Scope;

import com.extjs.gxt.ui.client.widget.form.FormPanel;

/**
 * Workaround {@link Scope} class to keep state of the imported scope for a
 * specific HTTP session. This was introduced since there is a bug in the
 * javascript code for IE on the {@link FormPanel}. The panel for some reason
 * can't add the response HTML into a specific IFrame which results in a black
 * result HTML in every call. This way we can't detect through the normal way if
 * the submit was done correctly.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public class ImportedScope {

    /** The scope that was imported. */
    private Scope scope;

    /** Error message that occurred when trying to import a blacklist file. */
    private String errorMessage;

    /**
     * @return the scope
     */
    public Scope getScope() {
        return scope;
    }

    /**
     * @param scope
     *            the scope to set
     */
    public void setScope(final Scope scope) {
        this.scope = scope;
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage
     *            the errorMessage to set
     */
    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
