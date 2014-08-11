package nl.tranquilizedquality.adm.gwt.gui.client.model.scope;

import nl.tranquilizedquality.adm.commons.business.command.ScopeSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.Scope;

import com.extjs.gxt.ui.client.data.BeanModelTag;

/**
 * Client side representation of the search criteria for searching for
 * {@link Scope} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public class ClientScopeSearchCommand extends ScopeSearchCommand implements BeanModelTag {

    /**
     * Unique identifier used for serialization.
     */
    private static final long serialVersionUID = 5244810951894470432L;

}
