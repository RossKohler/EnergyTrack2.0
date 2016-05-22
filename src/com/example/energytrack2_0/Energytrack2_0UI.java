package com.example.energytrack2_0;

import javax.servlet.annotation.WebServlet;

import org.apache.log4j.Logger;

import com.example.database.DatabaseConnection;
import com.example.database.DatabaseQuery;
import com.example.email.EmailManagement;
import com.example.email.TemplateMarker;
import com.example.programpreferences.ProgramPreferences;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;


@SuppressWarnings("serial")
@Theme("energytrack2_0")
public class Energytrack2_0UI extends UI {
	
	
	static final Logger LOGGER = Logger.getLogger(Log4jContextListener.class);

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = Energytrack2_0UI.class, widgetset = "com.example.energytrack2_0.widgetset.Energytrack2_0Widgetset")
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		LOGGER.info("New session created!");
		Navigator navigator = new Navigator(this,this);
		this.setNavigator(navigator);
		navigator.addView(LoginView.NAME,new LoginView());
		
	    navigator.addView(MainView.NAME, new MainView());
	    
	    getNavigator().navigateTo(LoginView.NAME);
	    
	    navigator.addViewChangeListener(new ViewChangeListener(){

			@Override
			public boolean beforeViewChange(ViewChangeEvent event) {
				boolean isLoggedIn = getSession().getAttribute("user") != null;
				boolean isLoginView = event.getNewView() instanceof LoginView;
				
				if(!isLoggedIn && !isLoginView){
					;
					//getNavigator().navigateTo(LoginView.NAME);
					return false;
				}
				else if(isLoggedIn && isLoginView){
					return false;
				}
				return true;
			}

			@Override
			public void afterViewChange(ViewChangeEvent event) {
				// TODO Auto-generated method stub
				
			}
	    
	    	
	    	
	    	
	    });
		
		
		
	}

}