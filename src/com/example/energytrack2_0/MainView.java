package com.example.energytrack2_0;

import com.example.programpreferences.ProgramPreferences;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

public class MainView extends CustomComponent implements View {
	public static final String NAME = "MAIN";

	Label text = new Label();

	Button logout = new Button("Logout", new Button.ClickListener() {

		@Override
		public void buttonClick(ClickEvent event) {
			getSession().setAttribute("user", null);

			getUI().getNavigator().navigateTo(LoginView.NAME);

		}

	});

	public MainView() {
		MenuBar menuBar = new MenuBar();
		HorizontalLayout menuLayout = new HorizontalLayout();
		menuLayout.setWidth("100%");
		
		VerticalLayout mainLayout = new VerticalLayout();
		//mainLayout.setMargin(true);
		//menuBar.addItem("File", null);
		//menuBar.addItem("Edit", null);
		//menuBar.addItem("View", null);
		menuLayout.addComponent(menuBar);
		//menuLayout.addComponent(text);
		menuLayout.addComponent(logout);
		//menuLayout.setComponentAlignment(text, Alignment.MIDDLE_RIGHT);
		menuLayout.setComponentAlignment(logout,Alignment.MIDDLE_RIGHT);
		TabSheet tabSheet = new TabSheet();
		
		//tabSheet.addTab(new HomeLayout(),"Home");
		tabSheet.addTab(new EmployeeLayout(),"Employee Profiles");
		//tabSheet.addTab(new UsageLayout(), "Energy Usage");
		tabSheet.addTab(new EnergyTipLayout(),"Energy Tips");
		tabSheet.addTab(new ProjectLayout(),"Project Management");
		tabSheet.setSizeFull();
		mainLayout.addComponent(menuLayout);
		mainLayout.addComponent(tabSheet);
		mainLayout.setSizeFull();
		mainLayout.setExpandRatio(tabSheet,1);
		this.setSizeFull();
		setCompositionRoot(mainLayout);

	}

	@Override
	public void enter(ViewChangeEvent event) {
		//String username = String.valueOf(getSession().getAttribute("user"));
		//text.setValue("Hello " + username);

	}
}
