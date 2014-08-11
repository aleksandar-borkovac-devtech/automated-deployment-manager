package nl.tranquilizedquality.adm.commons.gwt.ext.client.model.navigation;

import java.io.Serializable;

import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.TabEnum;

import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;

/**
 * Basic menu item that can be used in a {@link TreePanel}. It has been designed
 * without generics to be able to construct and initialize the child arrays in
 * the menu tree in one go.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 */
public class TreeMenuItem extends BaseTreeModel implements Serializable {

    /** Unique identifier. */
    private static final long serialVersionUID = 298874892555493380L;

    /** Menu item identifier. */
    private static int ID = 0;

    /** The tab to which this menu item will refer to. */
    private TabEnum tab;

    /** Determines if this is the logout menu item. */
    private boolean logoutItem;

    /**
     * Default constructor incrementing the ID.
     */
    public TreeMenuItem() {
        set("id", ID++);
    }

    /**
     * Constructor taking a name of the item.
     * 
     * @param name
     *            The name of the item.
     */
    public TreeMenuItem(final String name) {
        set("id", ID++);
        set("name", name);
    }

    /**
     * Constructor taking a name of the item.
     * 
     * @param name
     *            The name of the item.
     * @param tab
     *            The promTabs to which the menu items will refer to.
     */
    public TreeMenuItem(final String name, final TabEnum tab) {
        set("id", ID++);
        set("name", name);
        this.tab = tab;
        logoutItem = false;
    }

    /**
     * Constructor taking a name of the item.
     * 
     * @param name
     *            The name of the item.
     * @param logoutItem
     *            Determines if this item is a logout item.
     */
    public TreeMenuItem(final String name, final boolean logoutItem) {
        set("id", ID++);
        set("name", name);
        this.logoutItem = logoutItem;
    }

    /**
     * Constructor taking a name and an array of children that will be added to
     * the current item.
     * 
     * @param name
     *            The name of the item.
     * @param children
     *            the children that will be added to the item.
     */
    public TreeMenuItem(final String name, final TabEnum tab, final BaseTreeModel[] children) {
        this(name, tab);
        for (final BaseTreeModel element : children) {
            if (element != null) {
                add(element);
            }
        }
        logoutItem = false;
    }

    /**
     * Retrieves the unique identifier.
     * 
     * @return Returns an {@link Integer} value of the id.
     */
    public Integer getId() {
        return (Integer) get("id");
    }

    /**
     * Retrieves the name of the item.
     * 
     * @return Returns a {@link String} representation of the name of the item.
     */
    public String getName() {
        return (String) get("name");
    }

    /**
     * @return the tab
     */
    public TabEnum getTab() {
        return tab;
    }

    public void setTab(final TabEnum tab) {
        this.tab = tab;
    }

    /**
     * @return the logoutItem
     */
    public boolean isLogoutItem() {
        return logoutItem;
    }

    public void setLogoutItem(final boolean logoutItem) {
        this.logoutItem = logoutItem;
    }

    @Override
    public String toString() {
        return getName();
    }
}
