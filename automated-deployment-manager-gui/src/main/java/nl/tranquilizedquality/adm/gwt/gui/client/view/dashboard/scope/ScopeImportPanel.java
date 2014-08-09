package nl.tranquilizedquality.adm.gwt.gui.client.view.dashboard.scope;

import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.security.AuthorizationServiceAsync;
import nl.tranquilizedquality.adm.gwt.gui.client.AdmModule;
import nl.tranquilizedquality.adm.gwt.gui.client.service.scope.ScopeServiceAsync;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The scope import panel.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 26, 2012
 * 
 */
public class ScopeImportPanel extends LayoutContainer {

	/** The form submition button. */
	private Button submitButton;

	/** The {@link Window} where this panel is displayed in if applicable. */
	private Window window;

	/**
	 * Default constructor.
	 */
	public ScopeImportPanel() {
		initializeWidgets();
	}

	/**
	 * Initializes the widgets on the panel.
	 */
	private void initializeWidgets() {
		setLayout(new FitLayout());

		final FormPanel detailPanel = createDetailPanel();

		add(detailPanel);
	}

	/**
	 * Checks if the logged in user is authorized to import a scope.
	 */
	protected void performPrivilegeCheck() {
		final AuthorizationServiceAsync authorizationService = Registry.get(AdmModule.AUTHORIZATION_SERVICE);
		final AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(final Throwable throwable) {
				final MessageBox box = new MessageBox();
				box.setIcon(MessageBox.ERROR);
				box.setTitle("Import scope.");
				box.setMessage(throwable.getMessage());
				box.setButtons(MessageBox.OK);
				box.show();
			}

			@Override
			public void onSuccess(final Boolean authorized) {
				if (authorized) {
					submitButton.enable();
				}
				else {
					submitButton.disable();
				}
			}

		};
		authorizationService.isLoggedInUserAuthorized("IMPORT_SCOPE", callback);
	}

	/**
	 * Creates the detail panel.
	 * 
	 * @return Returns a {@link FormPanel} with the upload capability.
	 */
	private FormPanel createDetailPanel() {
		final StringBuilder builder = new StringBuilder();
		builder.append("services/scopes/import");

		final FormPanel formPanel = new FormPanel();
		formPanel.setHeading("Import scope");
		formPanel.setFrame(true);
		formPanel.setAction(builder.toString());
		formPanel.setEncoding(Encoding.MULTIPART);
		formPanel.setMethod(Method.POST);
		formPanel.setButtonAlign(HorizontalAlignment.LEFT);
		formPanel.setWidth(350);

		final FileUploadField file = new FileUploadField();
		file.setAllowBlank(false);
		file.setFieldLabel("File");
		file.setName("scope");
		formPanel.add(file);

		submitButton = new Button("Import");
		submitButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(final ButtonEvent ce) {
				if (!formPanel.isValid()) {
					return;
				}
				formPanel.submit();
				final Listener<FormEvent> listener = new Listener<FormEvent>() {

					@Override
					public void handleEvent(final FormEvent be) {
						checkFeedBack();
					}
				};
				formPanel.addListener(Events.Submit, listener);
			}
		});
		formPanel.addButton(submitButton);

		return formPanel;
	}

	/**
	 * Check the feedback message.
	 */
	private void checkFeedBack() {
		final ScopeServiceAsync scopeService = Registry.get(AdmModule.SCOPE_SERVICE);
		final AsyncCallback<String> callback = new AsyncCallback<String>() {

			@Override
			public void onFailure(final Throwable throwable) {
				final MessageBox box = new MessageBox();
				box.setIcon(MessageBox.ERROR);
				box.setTitle("Import scope feedback check.");
				box.setMessage(throwable.getMessage());
				box.setButtons(MessageBox.OK);
				box.show();
			}

			@Override
			public void onSuccess(final String message) {
				final MessageBox box = new MessageBox();
				box.setIcon(MessageBox.INFO);
				box.setTitle("Scope import");
				box.setMessage(message);
				box.setButtons(MessageBox.OK);
				box.show();

				window.hide();
			}

		};
		scopeService.getScopeImportFeedback(callback);

	}

	/**
	 * @param window
	 *            the window to set
	 */
	public void setWindow(final Window window) {
		this.window = window;
	}
}
