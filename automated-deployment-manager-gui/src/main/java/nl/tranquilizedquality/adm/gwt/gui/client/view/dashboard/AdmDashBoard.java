package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard;

import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractDashBoard;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;

import com.extjs.gxt.ui.client.Registry;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * AdmDashBoard
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 */
public class AdmDashBoard extends AbstractDashBoard {

    /** The icons of the application. */
    private final AdmIcons icons;

    /**
     * Default constructor.
     */
    public AdmDashBoard() {
        /*
         * Set the heading.
         */
        setHeading("ADM Dashboard");

        /*
         * Set the dashboard icon.
         */
        this.icons = Registry.get(AdmModule.ICONS);
        setIcon(AbstractImagePrototype.create(icons.dashboard()));
    }

}
