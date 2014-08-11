package nl.tranquilizedquality.adm.commons.gwt.ext.client.controller.navigation;

import nl.tranquilizedquality.adm.commons.gwt.ext.client.AbstractModule;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.icons.ApplicationIcons;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.model.navigation.TreeMenuItem;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractDashBoard;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractNavigationPanel;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.NavigationalItem;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.TabEnum;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.google.gwt.i18n.client.Constants;

/**
 * Controller that is responsible for the application navigation. It uses
 * generics to support passing the right {@link TabEnum} type to the abstract
 * and default methods. The {@link TabEnum} interface is used to support the
 * {@link TreeMenuItem} and its use in the {@link AbstractNavigationPanel}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 27 jan. 2011
 * @param <Consts>
 *            The constants used in the controller.
 * @param <Icons>
 *            The icons used within the controller.
 * @param <Tab>
 *            The tabs used in the controller.
 */
public abstract class AbstractNavigationController<Consts extends Constants, Icons extends ApplicationIcons, Tab extends TabEnum> {

    /** The navigation constants of the application. */
    protected final Consts constants;

    /** The icons of the application. */
    protected final Icons icons;

    /** The {@link TabPanel} that will be managed by this controller. */
    protected final TabPanel tabPanel;

    /**
     * Default constructor retrieving the {@link TabPanel} from the
     * {@link Registry}.
     */
    @SuppressWarnings("unchecked")
    public AbstractNavigationController() {
        tabPanel = Registry.get(AbstractDashBoard.TAB_PANEL);
        constants = (Consts) Registry.get(AbstractModule.NAVIGATION_CONSTANTS);
        icons = (Icons) Registry.get(AbstractModule.ICONS);
    }

    /**
     * Selects a tab to be presented to the user and populates the containing
     * panel with the provided model.
     * 
     * @param <T>
     *            the model type.
     * @param tab
     *            the tab to select.
     * @param model
     *            the model to propagate into the tabs' panel.
     */
    @SuppressWarnings("unchecked")
    public <T> void selectTab(final TabEnum tab, final T model) {
        /*
         * Select the tab.
         */
        final TabItem tabItem = selectTab(tab);

        /*
         * Set the model data.
         */
        final NavigationalItem<T> navItem = (NavigationalItem<T>) tabItem.getWidget(0);
        navItem.setModel(model);
    }

    /**
     * Selects a tab to be presented to the user. It will create the tab item
     * and its containing panel if not already present.
     * 
     * @param tab
     *            the tab to select.
     * @return the selected {@link TabItem}.
     */
    @SuppressWarnings("unchecked")
    public TabItem selectTab(final TabEnum tab) {
        /*
         * Retrieve the tab item.
         */
        TabItem tabItem = tabPanel.getItemByItemId(tab.toString());

        if (tabItem == null) {
            /*
             * Create a new tab because it doesn't exist yet.
             */
            tabItem = createNewTab((Tab) tab);

            tabPanel.add(tabItem);
            tabPanel.fireEvent(Events.Resize);
        }
        else {
            /*
             * Create a new model on the tab.
             */
            createNewModelOnTab((Tab) tab, tabItem);

            tabPanel.fireEvent(Events.Resize);
        }

        /*
         * Select the tab item.
         */
        tabPanel.setSelection(tabItem);

        return tabItem;
    }

    /**
     * Closes the passed in tab.
     * 
     * @param tab
     *            The tab that will be closed.
     */
    public void closeTab(final Tab tab) {
        /*
         * Retrieve the tab item.
         */
        final TabItem tabItem = tabPanel.getItemByItemId(tab.toString());

        if (tabItem != null) {
            tabPanel.remove(tabItem);
        }

    }

    /**
     * Creates a new {@link TabItem} based on the specified TabEnum.
     * 
     * @param tab
     *            The kind of tab that needs to be created.
     * @return the new tab item.
     */
    protected abstract TabItem createNewTab(Tab tab);

    /**
     * Is called when the {@link TabItem} already exists. You can choose to
     * override this so you can set a new model on the tab if needed.
     * 
     * @param tab
     *            The type of tab that needs a new model.
     * @param tabItem
     *            The tab item that needs a new model.
     */
    protected void createNewModelOnTab(final Tab tab, final TabItem tabItem) {
        /*
         * By default do nothing so the tab will just be selected.
         */
    }
}
