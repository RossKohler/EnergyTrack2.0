package com.example.energytrack2_0;

import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class HomeLayout extends Panel {
	
	
	public HomeLayout(){
		VerticalLayout welcomeLayout = new VerticalLayout();
		Label nameLabel = new Label("EnergyTrack");
		Label buildingLabel = new Label("4 Dorp Street");
		Label projectLabel = new Label("2Wise2Waste");
		welcomeLayout.addComponents(nameLabel,buildingLabel,projectLabel);
		welcomeLayout.setSpacing(true);
		welcomeLayout.setSizeFull();
		setContent(welcomeLayout);
		//addComponent(welcomeLayout,"left: 0px; top: 0px;");
		this.setSizeFull();
	}
	
	
	
	
	
	
	
	
	

}
