package com.example.energytrack2_0;

import com.vaadin.data.Validator;
import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class LoginView extends CustomComponent implements View,
		Button.ClickListener {
	public static final String NAME = "";

	private final TextField user;
	private final PasswordField password;
	private final Button loginButton;

	public LoginView() {
		setSizeFull();

		user = new TextField("User:");
		user.setWidth("300px");
		user.setRequired(true);
		user.setInputPrompt("Enter Username");

		password = new PasswordField("Password:");
		password.setWidth("300px");
		password.addValidator(new PasswordValidator());
		password.setRequired(true);
		password.setValue("");
		password.setNullRepresentation("");

		loginButton = new Button("Login", this);

		VerticalLayout fields = new VerticalLayout(user, password, loginButton);
		fields.setCaption("Please login to access this application");
		fields.setSpacing(true);
		fields.setMargin(new MarginInfo(true, true, true, false));
		fields.setSizeUndefined();

		VerticalLayout viewLayout = new VerticalLayout(fields);
		viewLayout.setSizeFull();
		viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
		viewLayout.setStyleName(Reindeer.LAYOUT_BLUE);
		setCompositionRoot(viewLayout);

	}

	@Override
	public void buttonClick(ClickEvent event) {
		// TODO Auto-generated method stub
		if (!user.isValid() || !password.isValid()) {
			return;
		}

		String username = user.getValue();
		String password = this.password.getValue();

		boolean isValid = username.equals("admin")
				&& password.equals("password");

		if (isValid) {
			getSession().setAttribute("user", username);
			getUI().getNavigator().navigateTo(MainView.NAME);
		} else {
			this.password.setValue(null);
			this.password.focus();

		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		user.focus();

	}

	private static final class PasswordValidator extends
			AbstractValidator<String> {

		public PasswordValidator() {
			super("The password provided is not valid");
		}

		@Override
		protected boolean isValidValue(String value) {
			/*
			 * if(value != null && (value.length()< 8 ||
			 * !value.matches(".*\\d.*"))){ return false; } else{ return true; }
			 */
			return true;

		}

		@Override
		public Class<String> getType() {
			return String.class;
		}
	}

}
