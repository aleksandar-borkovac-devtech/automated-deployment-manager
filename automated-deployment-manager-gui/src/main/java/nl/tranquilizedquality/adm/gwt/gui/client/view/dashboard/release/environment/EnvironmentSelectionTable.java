/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 7 okt. 2011 File: EnvironmentSelectionTable.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.view.dashboard.release.environment
 * 
 * Copyright (c) 2011 Tranquilized Quality www.Tranquilized Quality.nl All rights
 * reserved.
 * 
 * This software is the confidential and proprietary information of Tranquilized Quality
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Tranquilized Quality.
 * </pre>
 */
package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.release.environment;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractGridPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientEnvironment;
import nl.tranquilizedquality.adm.gwt.gui.client.service.environment.EnvironmentServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.release.ReleaseDetailsPanel;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.RowNumberer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Grid where you can select an environment where you want to deploy to.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 7 okt. 2011
 */
public class EnvironmentSelectionTable extends AbstractGridPanel {

    /** Details panel where the release is displayed on. */
    private final ReleaseDetailsPanel detailsPanel;

    /** Selection button. */
    private Button selectButton;

    /** The icons of the application. */
    private final AdmIcons icons;

    /** Window where this table is displayed in. */
    private final EnvironmentSelectionWindow window;

    /**
     * Constructor that takes the details panel and the window where this grid
     * is displayed in.
     * 
     * @param detailsPanel
     *        The details panel where the release is displayed in.
     * @param window
     *        The window where this grid is displayed in.
     */
    public EnvironmentSelectionTable(final ReleaseDetailsPanel detailsPanel, final EnvironmentSelectionWindow window) {
        this.detailsPanel = detailsPanel;
        this.icons = Registry.get(AdmModule.ICONS);
        this.window = window;
        setId("environment-selection-table");

        initializeWidgets();
    }

    @Override
    protected void initializeWidgets() {
        /*
         * Add select button.
         */
        selectButton = new Button("Select");
        selectButton.setId("select-deployment-environment-btn");
        selectButton.setIcon(AbstractImagePrototype.create(icons.add()));

        final SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                selectEnvironment();
            }

        };
        selectButton.addSelectionListener(listener);

        this.menuBarButtons.add(selectButton);

        /*
         * Add proxy loader.
         */
        proxy = new RpcProxy<PagingLoadResult<ClientEnvironment>>() {

            @Override
            public void load(final Object loadConfig, final AsyncCallback<PagingLoadResult<ClientEnvironment>> callback) {
                final EnvironmentServiceAsync environmentService = Registry.get(AdmModule.ENVIRONMENT_SERVICE);
                environmentService.findAvailableEnvironments((PagingLoadConfig) loadConfig, callback);
            }
        };

        this.panelStateId = EnvironmentSelectionTable.class.getName();

        super.initializeWidgets();
    }

    @Override
    protected List<ColumnConfig> createColumns() {
        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        configs.add(new RowNumberer());

        ColumnConfig column = new ColumnConfig();
        column.setId("name");
        column.setHeader("Environment");
        column.setWidth(100);
        column.setSortable(true);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("description");
        column.setHeader("Description");
        column.setWidth(100);
        column.setSortable(true);
        configs.add(column);

        return configs;
    }

    @Override
    protected void handleDoubleClick() {
        selectEnvironment();
    }

    /**
     * Selects the currently selected environment and performs the release.
     */
    private void selectEnvironment() {
        final GridSelectionModel<BeanModel> selectionModel = grid.getSelectionModel();
        final BeanModel selectedItem = selectionModel.getSelectedItem();

        if (selectedItem != null) {
            final ClientEnvironment environment = selectedItem.getBean();
            final boolean production = environment.isProduction();

            if (production) {
                final MessageBox messageBox = new MessageBox();
                messageBox.setIcon(MessageBox.WARNING);
                messageBox.setButtons(MessageBox.YESNO);
                messageBox.setMessage("Are you sure you want to deploy to a production environment?");
                messageBox.addCallback(new Listener<MessageBoxEvent>() {

                    @Override
                    public void handleEvent(final MessageBoxEvent be) {
                        final Button buttonClicked = be.getButtonClicked();
                        final String itemId = buttonClicked.getItemId();
                        if (Dialog.YES.equals(itemId)) {
                            final ClientEnvironment environment = selectedItem.getBean();
                            detailsPanel.deployRelease(environment);

                            window.setUnmask(false);
                            window.hide();
                        } else {
                            window.unmask();
                            window.hide();
                        }
                    }
                });
                messageBox.show();
            } else {
                detailsPanel.deployRelease(environment);

                window.setUnmask(false);
                window.hide();
            }

        }

    }
}
