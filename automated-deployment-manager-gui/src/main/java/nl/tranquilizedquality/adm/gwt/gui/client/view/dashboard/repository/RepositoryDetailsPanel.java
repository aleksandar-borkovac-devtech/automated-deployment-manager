/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 4 sep. 2011 File: RepositoryDetailsPanel.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.view.dashboard.repository
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
package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.repository;

import java.util.List;

import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.AbstractServiceException;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.security.AuthorizationServiceAsync;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractDetailPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.repository.ClientRepository;
import nl.tranquilizedquality.adm.gwt.gui.client.service.repository.RepositoryServiceAsync;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Details panel where the details of a repository are displayed.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 4 sep. 2011
 */
public class RepositoryDetailsPanel extends AbstractDetailPanel<ClientRepository> {

    /** The icons of the application. */
    private final AdmIcons icons;

    /** The name of the repository. */
    private TextField<String> name;

    /** The repository URL. */
    private TextField<String> repositoryUrl;

    /** The id of the repository from Nexus. */
    private TextField<String> repositoryId;

    /** Determines if the repository is enabled or not. */
    private CheckBox enabled;

    /** Save button. */
    private Button saveButton;

    /**
     * Default constructor.
     */
    public RepositoryDetailsPanel() {
        this.icons = Registry.get(AdmModule.ICONS);

        initializeWidgets();
    }

    @Override
    protected void initializeWidgets() {
        final RowLayout layout = new RowLayout(Orientation.VERTICAL);
        setLayout(layout);

        formPanel = createDetailPanel();

        add(this.formPanel, new RowData(1, 230, new Margins(0)));
    }

    @Override
    public void setModel(final ClientRepository model) {
        this.model = model;

        if (this.model.isPersistent()) {
            final AsyncCallback<ClientRepository> callback = new AsyncCallback<ClientRepository>() {

                @Override
                public void onFailure(final Throwable throwable) {
                    final MessageBox box = new MessageBox();
                    box.setIcon(MessageBox.ERROR);
                    box.setTitle("Retrieve repository.");
                    box.setMessage(throwable.getMessage());
                    box.setButtons(MessageBox.OK);
                    box.show();
                }

                @Override
                public void onSuccess(final ClientRepository office) {
                    RepositoryDetailsPanel.this.model = office;
                    bindModel(RepositoryDetailsPanel.this.model);
                }

            };

            final RepositoryServiceAsync repositoryService = Registry.get(AdmModule.REPOSITORY_SERVICE);
            repositoryService.findRepositoryById(this.model.getId(), callback);
        } else {
            bindModel(this.model);
        }
    }

    @Override
    protected void performPrivilegeCheck() {
        final AuthorizationServiceAsync authorizationService = Registry.get(AdmModule.AUTHORIZATION_SERVICE);
        authorizationService.isLoggedInUserAuthorized("ADD_REPOSITORY", new AsyncCallback<Boolean>() {

            @Override
            public void onFailure(final Throwable throwable) {
                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.ERROR);
                box.setTitle("Create repository check.");
                box.setMessage(throwable.getMessage());
                box.setButtons(MessageBox.OK);
                box.show();
            }

            @Override
            public void onSuccess(final Boolean authorized) {
                if (authorized) {
                    name.setReadOnly(false);
                    repositoryUrl.setReadOnly(false);
                    enabled.setReadOnly(false);
                    saveButton.show();
                } else {
                    name.setReadOnly(true);
                    repositoryUrl.setReadOnly(true);
                    enabled.setReadOnly(true);
                    saveButton.show();
                }
            }

        });
    }

    @Override
    protected FormPanel createDetailPanel() {
        final FormPanel formPanel = new FormPanel();
        formPanel.setHeading("Details");
        formPanel.setFrame(true);
        formPanel.setLabelWidth(110);
        formPanel.setFieldWidth(400);
        formPanel.setIcon(AbstractImagePrototype.create(icons.addRepository()));
        formPanel.setButtonAlign(HorizontalAlignment.RIGHT);

        /*
         * Create field set for repository related information.
         */
        final FieldSet repositoryInfoFieldSet = new FieldSet();
        repositoryInfoFieldSet.setLayout(new FormLayout());
        repositoryInfoFieldSet.setCollapsible(true);
        repositoryInfoFieldSet.setAutoHeight(true);
        repositoryInfoFieldSet.setHeading("Repository Information");

        name = new TextField<String>();
        name.setId("repository-details-pnl-name");
        name.setAllowBlank(false);
        name.setName("name");
        name.setFieldLabel("Name");
        repositoryInfoFieldSet.add(name);

        repositoryUrl = new TextField<String>();
        repositoryUrl.setId("repository-details-pnl-url");
        repositoryUrl.setAllowBlank(false);
        repositoryUrl.setName("repositoryUrl");
        repositoryUrl.setFieldLabel("URL");
        repositoryInfoFieldSet.add(repositoryUrl);

        repositoryId = new TextField<String>();
        repositoryId.setId("repository-details-pnl-id");
        repositoryId.setAllowBlank(false);
        repositoryId.setName("repositoryId");
        repositoryId.setFieldLabel("Repository Id");
        repositoryInfoFieldSet.add(repositoryId);

        enabled = new CheckBox();
        enabled.setId("repository-details-pnl-enabled");
        enabled.setName("enabled");
        enabled.setFieldLabel("Enabled");
        repositoryInfoFieldSet.add(enabled, new FormData(15, 20));

        formPanel.add(repositoryInfoFieldSet);

        saveButton = new Button();
        saveButton.setId("save-repository-btn");
        saveButton.setText("Save");

        final SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                for (final FieldBinding fieldBinding : fieldBindings) {
                    fieldBinding.updateModel();
                }

                final RepositoryServiceAsync repositoryService = Registry.get(AdmModule.REPOSITORY_SERVICE);
                final AsyncCallback<ClientRepository> callback = new AsyncCallback<ClientRepository>() {

                    @Override
                    public void onFailure(final Throwable throwable) {
                        final StringBuilder builder = new StringBuilder();

                        if (throwable instanceof AbstractServiceException) {
                            final AbstractServiceException ex = (AbstractServiceException) throwable;
                            final List<String> errors = ex.getErrors();

                            for (final String string : errors) {
                                builder.append(string);
                                builder.append("<br>");
                            }
                        } else {
                            builder.append(throwable.getMessage());
                        }

                        final MessageBox box = new MessageBox();
                        box.setIcon(MessageBox.ERROR);
                        box.setTitle("Failed to save repository.");
                        box.setMessage(builder.toString());
                        box.setButtons(MessageBox.OK);
                        box.show();
                    }

                    @Override
                    public void onSuccess(final ClientRepository repository) {
                        final MessageBox box = new MessageBox();
                        box.setIcon(MessageBox.INFO);
                        box.setTitle("Save repository.");
                        box.setMessage("Successfully saved " + repository.getName() + ".");
                        box.setButtons(MessageBox.OK);
                        box.show();

                        setModel(repository);
                    }

                };

                repositoryService.saveRepository(model, callback);
            }

        };
        saveButton.addSelectionListener(listener);

        formPanel.addButton(saveButton);

        return formPanel;
    }

}
