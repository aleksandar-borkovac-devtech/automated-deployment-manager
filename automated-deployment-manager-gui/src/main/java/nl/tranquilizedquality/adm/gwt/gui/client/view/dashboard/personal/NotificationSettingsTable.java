/*
 * @(#)NotificationSettingsTable.java 23 okt. 2012
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.personal;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.EnvironmentNotificationSetting;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractRelationListTable;
import nl.tranquilizedquality.adm.gwt.gui.client.model.settings.ClientEnvironmentNotificationSetting;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CheckColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.RowEditor;
import com.extjs.gxt.ui.client.widget.grid.RowNumberer;

/**
 * Table where you can manage your settings.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 23 okt. 2012
 */
public class NotificationSettingsTable extends
        AbstractRelationListTable<EnvironmentNotificationSetting, ClientEnvironmentNotificationSetting> {

    /**
     * Default constructor.
     */
    public NotificationSettingsTable() {
        setHeading("Settings");

        /*
         * Initialize the widgets.
         */
        initializeWidgets();

        grid.addPlugin(new RowEditor<ModelData>());
        grid.getView().setForceFit(false);
    }

    @Override
    protected String getPanelStateId() {
        return NotificationSettingsTable.class.getName();
    }

    @Override
    protected Class<ClientEnvironmentNotificationSetting> getBeanModelClass() {
        return ClientEnvironmentNotificationSetting.class;
    }

    @Override
    protected List<ColumnConfig> createColumns() {
        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        configs.add(new RowNumberer());

        final ColumnConfig column = new ColumnConfig();
        column.setId("environmentName");
        column.setHeader("Environment");
        column.setWidth(100);
        column.setSortable(true);
        configs.add(column);

        final CheckColumnConfig checkColumn = new CheckColumnConfig("emailNotification", "Email", 55);
        final CellEditor checkBoxEditor = new CellEditor(new CheckBox());
        checkColumn.setEditor(checkBoxEditor);
        configs.add(checkColumn);

        return configs;
    }

    public List<ClientEnvironmentNotificationSetting> getSettings() {
        final ListStore<BeanModel> store = grid.getStore();
        store.commitChanges();
        final List<BeanModel> models = store.getModels();
        final List<ClientEnvironmentNotificationSetting> settings = new ArrayList<ClientEnvironmentNotificationSetting>();
        for (final BeanModel beanModel : models) {
            final ClientEnvironmentNotificationSetting setting = beanModel.getBean();
            settings.add(setting);
        }

        return settings;
    }

}
