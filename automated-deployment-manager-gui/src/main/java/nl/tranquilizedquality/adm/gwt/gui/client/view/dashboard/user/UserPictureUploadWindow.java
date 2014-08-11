/**
 * <pre>
 * Project: dam-gwt-gui Created on: 28 mei 2011 File: UserPictureUploadWindow.java
 * Package: nl.Tranquilized Quality.dam.gwt.gui.client.view.dashboard.user
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
package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.user;

import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUser;
import nl.tranquilizedquality.adm.gwt.gui.client.view.AdmViewPort;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;

/**
 * Window where a user picture can be uploaded.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 mei 2011
 */
class UserPictureUploadWindow extends Window {

    /** The user where the picture will be uploaded for. */
    private ClientUser user;

    /** The form panel. */
    private FormPanel formPanel;

    /** Panel where the user is being displayed on. */
    private UserDetailPanel userDetailPanel;

    /**
     * Default constructor.
     */
    public UserPictureUploadWindow() {
        setHeading("User Picture Upload");
        setMaximizable(false);
        setResizable(false);
        setWidth(350);
        setHeight(115);
        setLayout(new FitLayout());

        initializeWidgets();
    }

    private void initializeWidgets() {
        formPanel = new FormPanel();
        formPanel.setMethod(Method.POST);
        formPanel.setEncoding(Encoding.MULTIPART);
        formPanel.setHeaderVisible(false);
        formPanel.setFrame(true);
        formPanel.setButtonAlign(HorizontalAlignment.CENTER);

        final FileUploadField fileUpload = new FileUploadField();
        fileUpload.setAllowBlank(false);
        fileUpload.setName("userpicture");
        fileUpload.setFieldLabel("File");
        fileUpload.setEmptyText("Select JPG file...");

        formPanel.add(fileUpload);

        final Button submit = new Button("Upload");
        final SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                if (formPanel.isValid()) {
                    formPanel.submit();

                    formPanel.mask("Image is being uploaded...");

                    final Timer timer = new Timer() {

                        @Override
                        public void run() {
                            formPanel.unmask();
                            UserPictureUploadWindow.this.hide();
                        }
                    };
                    timer.schedule(2000);
                }
                else {
                    final MessageBox messageBox = new MessageBox();
                    messageBox.setButtons(MessageBox.OK);
                    messageBox.setIcon(MessageBox.WARNING);
                    messageBox.setMessage("You need to specify a file!");

                    messageBox.show();
                }
            }
        };
        submit.addSelectionListener(listener);

        formPanel.addButton(submit);

        add(formPanel);
    }

    @Override
    protected void onHide() {
        super.onHide();

        final AdmViewPort viewPort = Registry.get(AdmModule.VIEW_PORT);
        viewPort.unmask();

        userDetailPanel.setModel(user);
    }

    /**
     * @param user
     *            the user to set
     */
    public void setUser(final ClientUser user) {
        this.user = user;

        formPanel.setAction(GWT.getHostPageBaseURL() + "services/images/user/" + this.user.getId());
    }

    /**
     * @param userDetailPanel
     *            the userDetailPanel to set
     */
    public void setUserDetailPanel(final UserDetailPanel userDetailPanel) {
        this.userDetailPanel = userDetailPanel;
    }

}
