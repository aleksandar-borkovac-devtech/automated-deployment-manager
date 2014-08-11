package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.scope;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.Privilege;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractRelationListTable;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.controller.navigation.AdmNavigationController;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.privilege.ClientPrivilege;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.AdmTabs;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.widget.grid.CheckColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Panel for managing relationship with {@link Privilege} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 26, 2012
 * 
 */
public class ScopePrivilegesTable extends AbstractRelationListTable<Privilege, ClientPrivilege> {

    /** The icons of the application. */
    private final AdmIcons icons;

    /**
     * Default constructor.
     */
    public ScopePrivilegesTable() {
        setHeading("Privileges");

        icons = Registry.get(AdmModule.ICONS);

        setIcon(AbstractImagePrototype.create(icons.privilege()));

        initializeWidgets();
    }

    @Override
    protected Class<ClientPrivilege> getBeanModelClass() {
        return ClientPrivilege.class;
    }

    @Override
    protected String getPanelStateId() {
        return ScopePrivilegesTable.class.getName();
    }

    @Override
    protected List<ColumnConfig> createColumns() {
        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        ColumnConfig column = new ColumnConfig();
        column.setId("name");
        column.setHeader("Name");
        column.setWidth(200);
        column.setSortable(true);
        configs.add(column);

        column = new CheckColumnConfig();
        column.setId("valid");
        column.setHeader("Valid");
        column.setWidth(35);
        column.setSortable(true);
        configs.add(column);

        return configs;
    }

    @Override
    protected void handleDoubleClick(final GridEvent<BeanModel> gridEvent) {
        final Grid<BeanModel> grid = gridEvent.getGrid();
        final GridSelectionModel<BeanModel> selectionModel = grid.getSelectionModel();
        final BeanModel selectedItem = selectionModel.getSelectedItem();

        if (selectedItem != null) {
            final ClientPrivilege privilege = (ClientPrivilege) selectedItem.getBean();

            final AdmNavigationController controller = Registry.get(AdmModule.NAVIGATION_CONTROLLER);

            controller.selectTab(AdmTabs.PRIVILEGE_DETAIL_TAB, privilege);
        }
    }

}
