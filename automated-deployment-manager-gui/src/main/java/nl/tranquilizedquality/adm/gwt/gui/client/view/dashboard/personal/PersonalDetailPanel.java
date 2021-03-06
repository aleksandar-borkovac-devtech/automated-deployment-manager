/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: Sep 1, 2012 File: fPersonalDetailPanel.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.view.dashboard.user
 * 
 * Copyright (c) 2012 Tranquilized Quality www.Tranquilized Quality.nl All rights
 * reserved.
 * 
 * This software is the confidential and proprietary information of Tranquilized Quality
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Tranquilized Quality.
 * </pre>
 */
package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.personal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.EnvironmentNotificationSetting;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.AbstractServiceException;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.view.AbstractDetailPanel;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.controller.navigation.AdmNavigationController;
import nl.tranquilizedquality.adm.gwt.gui.client.icons.AdmIcons;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUser;
import nl.tranquilizedquality.adm.gwt.gui.client.model.settings.ClientEnvironmentNotificationSetting;
import nl.tranquilizedquality.adm.gwt.gui.client.service.user.UserServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.service.user.UserServiceException;
import nl.tranquilizedquality.adm.gwt.gui.client.util.StringUtils;
import nl.tranquilizedquality.adm.gwt.gui.client.view.AdmViewPort;
import nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.AdmTabs;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.HtmlEditor;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Image;

/**
 * Panel where the details of the logged in user are displayed on an can be
 * managed on.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Sep 1, 2012
 */
public class PersonalDetailPanel extends AbstractDetailPanel<ClientUser> {

    /** The icons of the application. */
    private final AdmIcons icons;

    /** Determines if the user is blocked or not. */
    private CheckBox blocked;

    /** The first name of the user. */
    private TextField<String> name;

    /** The last name of the user. */
    private TextField<String> userName;

    /** The date when the user logged in for the last time. */
    private DateField lastLoginDate;

    /** Determines if the user is active. */
    private CheckBox active;

    /** The date that the user started to be active. */
    private DateField activeFrom;

    /** The date that the user stopped being active. */
    private DateField activeUntil;

    /** The comments. */
    private HtmlEditor comments;

    /** The user that will be displayed. */
    private ClientUser user;

    /** The save button. */
    private Button saveButton;

    /** Button which displays the user history information. */
    private Button userHistoryButton;

    /** Field that displays the email address of the user. */
    private TextField<String> email;

    /** Field that displays the mobile phone number. */
    private TextField<String> mobilePhoneNumber;

    /** Field that displays the user picture. */
    private Image userPicture;

    /** Window that will be used to upload a member image. */
    private UserPictureUploadWindow window;

    /**
     * The current password of the user that is logged in. This is used to
     * authorize a password change.
     */
    private TextField<String> currentPassword;

    /** The new password the user wants to use. */
    private TextField<String> newPassword;

    /** The verification password which should be the same as the new password. */
    private TextField<String> verificationPassword;

    private NotificationSettingsTable settingsTable;

    /**
     * Default constructor.
     */
    public PersonalDetailPanel() {
        setId(this.getClass().getName());
        this.icons = Registry.get(AdmModule.ICONS);

        initializeWidgets();

        performPrivilegeCheck();
    }

    @Override
    protected void performPrivilegeCheck() {

    }

    /**
     * Set the model for this screen It will retrieve the user and its relations
     * to be put in the model.
     * 
     * @param user
     *            the user to display the details of.
     */
    @Override
    public void setModel(final ClientUser user) {
        this.user = user;

        final AsyncCallback<ClientUser> callback = new AsyncCallback<ClientUser>() {

            @Override
            public void onFailure(final Throwable throwable) {
                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.ERROR);
                box.setTitle("Retrieve user.");
                box.setMessage(throwable.getMessage());
                box.setButtons(MessageBox.OK);
                box.show();
            }

            @Override
            public void onSuccess(final ClientUser user) {
                PersonalDetailPanel.this.user = user;
                bindModel(PersonalDetailPanel.this.user);

                userPicture.setUrl(GWT.getHostPageBaseURL() + "services/images/user/" + user.getId() + "?" + new Date().getTime());

                final UserServiceAsync userService = Registry.get(AdmModule.USER_SERVICE);
                userService.findSettingsForLoggedInUser(new AsyncCallback<List<ClientEnvironmentNotificationSetting>>() {

                    @Override
                    public void onFailure(final Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(final List<ClientEnvironmentNotificationSetting> user) {
                        settingsTable.setModel(new ArrayList<EnvironmentNotificationSetting>(user));
                    }
                });
            }

        };

        final UserServiceAsync userService = Registry.get(AdmModule.USER_SERVICE);
        userService.findLoggedInUser(callback);
    }

    /**
     * Create a vertical layout to show the user details and its relations.
     */
    @Override
    protected void initializeWidgets() {
        final BorderLayout layout = new BorderLayout();
        setLayout(layout);

        /*
         * Create the detail panel.
         */
        this.formPanel = createDetailPanel();

        /*
         * Setup layout.
         */
        final BorderLayoutData northData = new BorderLayoutData(LayoutRegion.NORTH);
        northData.setCollapsible(false);
        northData.setFloatable(false);
        northData.setSplit(true);
        northData.setMinSize(520);
        northData.setMaxSize(520);
        northData.setSize(520);
        northData.setMargins(new Margins(0, 0, 0, 0));

        add(this.formPanel, northData);
    }

    /**
     * Creates the panel where the details are shown.
     * 
     * @return Returns the {@link FormPanel} containing the filter cirtieria.
     */
    @Override
    protected FormPanel createDetailPanel() {
        final FormPanel formPanel = new FormPanel();
        formPanel.setHeading("Details");
        formPanel.setFrame(true);
        formPanel.setLabelWidth(110);
        formPanel.setIcon(AbstractImagePrototype.create(icons.user()));

        final FormData formData = new FormData("90%");

        /*
         * Create user picture section.
         */
        final FieldSet userPersonalInformationFieldSet = new FieldSet();
        userPersonalInformationFieldSet.setId("personal-detail-user-picture-fs");
        userPersonalInformationFieldSet.setCollapsible(true);
        userPersonalInformationFieldSet.setAutoHeight(true);
        userPersonalInformationFieldSet.setHeading("User Information");
        final FormLayout userPersonalInformationFormLayout = new FormLayout();
        userPersonalInformationFormLayout.setLabelAlign(LabelAlign.LEFT);
        userPersonalInformationFieldSet.setLayout(userPersonalInformationFormLayout);

        final LayoutContainer userPicturePanel = new LayoutContainer();
        userPicturePanel.setStyleAttribute("paddingRight", "10px");
        final FormLayout userPicturePanelFormLayout = new FormLayout();
        userPicturePanelFormLayout.setLabelWidth(110);
        userPicturePanel.setLayout(userPicturePanelFormLayout);

        userPicture = new Image();
        userPicture.setHeight("180px");
        userPicture.setWidth("136px");
        userPicturePanel.add(userPicture);

        final ClickHandler handler = new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                if (window == null) {
                    window = new UserPictureUploadWindow();
                    window.setPersonalDetailPanel(PersonalDetailPanel.this);
                }

                window.show();
                window.setUser(user);

                final AdmViewPort viewPort = Registry.get(AdmModule.VIEW_PORT);
                viewPort.mask();
            }

        };
        userPicture.addClickHandler(handler);

        final LayoutContainer userPersonalDetailsPanel = new LayoutContainer();
        userPersonalDetailsPanel.setStyleAttribute("paddingRight", "10px");
        final FormLayout userPersonalDetailsPanelFormLayout = new FormLayout();
        userPersonalDetailsPanelFormLayout.setLabelWidth(130);
        userPersonalDetailsPanel.setLayout(userPersonalDetailsPanelFormLayout);

        name = new TextField<String>();
        name.setId("personal-detail-name");
        name.setName("name");
        name.setFieldLabel("Name");
        name.setEnabled(true);
        name.setAllowBlank(false);
        name.setReadOnly(false);
        userPersonalDetailsPanel.add(name, formData);

        userName = new TextField<String>();
        userName.setId("personal-detail-user-name");
        userName.setName("userName");
        userName.setFieldLabel("User Name");
        userName.setEnabled(true);
        userName.setAllowBlank(false);
        userName.setReadOnly(true);
        userPersonalDetailsPanel.add(userName, formData);

        mobilePhoneNumber = new TextField<String>();
        mobilePhoneNumber.setId("personal-detail-mobile-phone-number");
        mobilePhoneNumber.setName("mobilePhoneNumber");
        mobilePhoneNumber.setFieldLabel("Mobile number");
        mobilePhoneNumber.setEnabled(true);
        mobilePhoneNumber.setAllowBlank(false);
        userPersonalDetailsPanel.add(mobilePhoneNumber, formData);

        email = new TextField<String>();
        email.setId("personal-detail-email");
        email.setName("email");
        email.setFieldLabel("Email");
        email.setEnabled(true);
        email.setAllowBlank(false);
        userPersonalDetailsPanel.add(email, formData);

        currentPassword = new TextField<String>();
        currentPassword.setPassword(true);
        currentPassword.setId("personal-detail-current-password");
        currentPassword.setName("password");
        currentPassword.setFieldLabel("Current Password");
        currentPassword.setWidth(50);
        currentPassword.setAllowBlank(false);
        currentPassword.setEnabled(true);
        userPersonalDetailsPanel.add(currentPassword, formData);

        newPassword = new TextField<String>();
        newPassword.setPassword(true);
        newPassword.setId("personal-detail-current-new-password");
        newPassword.setName("newPassword");
        newPassword.setFieldLabel("New Password");
        newPassword.setWidth(50);
        newPassword.setAllowBlank(true);
        newPassword.setEnabled(true);
        newPassword.setValidator(new Validator() {
            @Override
            public String validate(final Field<?> field, final String value) {
                final String verificationPasswordValue = verificationPassword.getValue();
                if (value != null && value.equals(verificationPasswordValue)) {
                    return null;
                } else if (StringUtils.isEmpty(value) && StringUtils.isEmpty(verificationPasswordValue)) {
                    return null;
                }
                return "The new password you specified is not the same as the verification password";
            }
        });
        userPersonalDetailsPanel.add(newPassword, formData);

        verificationPassword = new TextField<String>();
        verificationPassword.setPassword(true);
        verificationPassword.setId("personal-detail-verification-password");
        verificationPassword.setName("verificationPassword");
        verificationPassword.setFieldLabel("Verification Password");
        verificationPassword.setWidth(50);
        verificationPassword.setAllowBlank(true);
        verificationPassword.setEnabled(true);
        verificationPassword.setValidator(new Validator() {

            @Override
            public String validate(final Field<?> field, final String value) {
                final String newPasswordValue = newPassword.getValue();
                if (value != null && value.equals(newPasswordValue)) {
                    return null;
                } else if (StringUtils.isEmpty(value) && StringUtils.isEmpty(newPasswordValue)) {
                    return null;
                }
                return "The new password you specified is not the same as the verification password";
            }
        });
        userPersonalDetailsPanel.add(verificationPassword, formData);

        /*
         * Create main user details panel.
         */
        final LayoutContainer mainUserDetailsPanel = new LayoutContainer();
        mainUserDetailsPanel.setLayout(new ColumnLayout());
        mainUserDetailsPanel.add(userPicturePanel, new ColumnData(200));
        mainUserDetailsPanel.add(userPersonalDetailsPanel, new ColumnData(350));

        userPersonalInformationFieldSet.add(mainUserDetailsPanel);

        /*
         * Create user information section.
         */
        final LayoutContainer left = new LayoutContainer();
        left.setStyleAttribute("paddingRight", "10px");
        FormLayout formLayout = new FormLayout();
        formLayout.setLabelWidth(110);
        left.setLayout(formLayout);

        active = new CheckBox();
        active.setId("personal-detail-active");
        active.setName("active");
        active.setFieldLabel("Active");
        active.setEnabled(true);
        active.setReadOnly(true);
        left.add(active, new FormData(15, 20));

        blocked = new CheckBox();
        blocked.setId("personal-detail-blocked");
        blocked.setName("blocked");
        blocked.setFieldLabel("Blocked");
        blocked.setEnabled(true);
        blocked.setReadOnly(true);
        left.add(blocked, new FormData(15, 20));

        lastLoginDate = new DateField();
        lastLoginDate.setId("personal-detail-last-login");
        lastLoginDate.setName("lastLoginDate");
        lastLoginDate.setFieldLabel("Last login date");
        lastLoginDate.setWidth(50);
        lastLoginDate.setAllowBlank(true);
        lastLoginDate.setEnabled(true);
        lastLoginDate.setReadOnly(true);
        left.add(lastLoginDate, formData);

        final LayoutContainer right = new LayoutContainer();
        right.setStyleAttribute("paddingLeft", "10px");
        formLayout = new FormLayout();
        right.setLayout(formLayout);

        activeFrom = new DateField();
        activeFrom.setId("personal-detail-active-from");
        activeFrom.setName("activeFrom");
        activeFrom.setFieldLabel("Active from");
        activeFrom.setWidth(50);
        activeFrom.setAllowBlank(true);
        activeFrom.setEnabled(true);
        activeFrom.setReadOnly(true);
        right.add(activeFrom, formData);

        activeUntil = new DateField();
        activeUntil.setId("personal-detail-active-until");
        activeUntil.setName("activeUntil");
        activeUntil.setFieldLabel("Active until");
        activeUntil.setWidth(50);
        activeUntil.setAllowBlank(true);
        activeUntil.setEnabled(true);
        activeUntil.setReadOnly(true);
        right.add(activeUntil, formData);

        final LayoutContainer accountInformationPanel = new LayoutContainer();
        accountInformationPanel.setLayout(new ColumnLayout());
        accountInformationPanel.add(left, new ColumnData(350));
        accountInformationPanel.add(right, new ColumnData(350));
        accountInformationPanel.setStyleAttribute("paddingLeft", "10px");
        accountInformationPanel.setStyleAttribute("paddingTop", "10px");

        comments = new HtmlEditor();
        comments.setId("personal-detail-comments");
        comments.setName("comments");
        comments.setFieldLabel("Comments");
        comments.setEnabled(true);
        comments.setHeight(150);
        comments.setHideLabel(true);
        comments.setReadOnly(true);

        /*
         * Create comments panel.
         */
        final LayoutContainer commentsPanel = new LayoutContainer();
        commentsPanel.setLayout(new FitLayout());

        commentsPanel.add(comments, new FormData("100%"));

        /*
         * Setup tabs for user details.
         */
        final TabPanel userDetailsTabPanel = new TabPanel();
        userDetailsTabPanel.setHeight(200);
        final TabItem accountInformationTabItem = new TabItem("Account information");
        final TabItem commentsTabItem = new TabItem("Comments");

        accountInformationTabItem.setLayout(new FitLayout());
        commentsTabItem.setLayout(new FitLayout());

        accountInformationTabItem.add(accountInformationPanel);
        commentsTabItem.add(commentsPanel);

        final TabItem notificationSettingsTabItem = createNotificationSettingsPanel();

        userDetailsTabPanel.add(commentsTabItem);
        userDetailsTabPanel.add(accountInformationTabItem);
        userDetailsTabPanel.add(notificationSettingsTabItem);

        /*
         * Configure form panel.
         */
        formPanel.add(userPersonalInformationFieldSet);
        formPanel.add(userDetailsTabPanel);

        userHistoryButton = new Button("Show user history");

        final SelectionListener<ButtonEvent> userHistoryListener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                final AdmNavigationController controller = Registry.get(AdmModule.NAVIGATION_CONTROLLER);

                controller.selectTab(AdmTabs.USER_HISTORY_TAB, PersonalDetailPanel.this.user);
            }
        };
        userHistoryButton.addSelectionListener(userHistoryListener);

        formPanel.addButton(userHistoryButton);

        saveButton = new Button("Save");
        saveButton.setId("personal-detail-save-btn");
        final SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                for (final FieldBinding fieldBinding : fieldBindings) {
                    fieldBinding.updateModel();
                }

                saveUser();

                saveUserSettings();
            }

        };
        saveButton.addSelectionListener(listener);

        formPanel.addButton(saveButton);

        final FormButtonBinding binding = new FormButtonBinding(formPanel);
        binding.addButton(saveButton);

        return formPanel;
    }

    private void saveUser() {
        user.setComments(comments.getValue());

        final UserServiceAsync userService = Registry.get(AdmModule.USER_SERVICE);
        final AsyncCallback<ClientUser> callback = new AsyncCallback<ClientUser>() {

            @Override
            public void onFailure(final Throwable throwable) {
                handleException(throwable, "Save user");
            }

            @Override
            public void onSuccess(final ClientUser user) {
                PersonalDetailPanel.this.user = user;
                setModel(PersonalDetailPanel.this.user);

                final MessageBox box = new MessageBox();
                box.setIcon(MessageBox.INFO);
                box.setTitle("Save user.");
                box.setMessage("Successfully saved " + user.getName() + ".");
                box.setButtons(MessageBox.OK);
                box.show();
            }

        };

        userService.updateUserAccount(user, callback);
    }

    private void saveUserSettings() {
        final UserServiceAsync userService = Registry.get(AdmModule.USER_SERVICE);
        final List<ClientEnvironmentNotificationSetting> settings = settingsTable.getSettings();
        userService.saveSettings(settings, new AsyncCallback<Void>() {

            @Override
            public void onSuccess(final Void value) {

            }

            @Override
            public void onFailure(final Throwable throwable) {
                handleException(throwable, "User settings");
            }
        });
    }

    private TabItem createNotificationSettingsPanel() {
        final TabItem accountInformationTabItem = new TabItem("Notification Settings");
        accountInformationTabItem.setLayout(new FitLayout());

        final LayoutContainer userPersonalDetailsPanel = new LayoutContainer();
        final BorderLayout userPersonalDetailsPanelFormLayout = new BorderLayout();
        userPersonalDetailsPanel.setLayout(userPersonalDetailsPanelFormLayout);
        settingsTable = new NotificationSettingsTable();

        final BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
        centerData.setMargins(new Margins(0, 1, 1, 1));
        centerData.setSplit(true);
        userPersonalDetailsPanel.add(settingsTable, centerData);

        accountInformationTabItem.add(userPersonalDetailsPanel);

        return accountInformationTabItem;
    }

    private void handleException(final Throwable throwable, final String title) {
        final StringBuilder builder = new StringBuilder();

        if (throwable instanceof UserServiceException) {
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
        box.setTitle(title);
        box.setMessage(builder.toString());
        box.setButtons(MessageBox.OK);
        box.show();
    }

}
