package com.example.energytrack2_0;

import java.sql.SQLException;
import java.util.Set;

import org.apache.log4j.Logger;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ModifyGroup extends Window {
	final static Logger errorLogger = Logger.getLogger(Log4jContextListener.class);
	
	private Set<Object> itemIds;
	private ComboBox groupBox;
	private VerticalLayout mainLayout;
	private SQLContainer container;
	private HorizontalLayout buttonLayout;
	
	
	public ModifyGroup(SQLContainer container, Set<Object> itemIds){
		this.itemIds = itemIds;
		this.setModal(true);
		//this.setWidth("10%");
		//this.setHeight("10%");
		this.center();
		mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		
		buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);
		Button doneButton = new Button("Done");
		Button cancelButton = new Button("Cancel");
		
		cancelButton.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				close();
				
			}
		});
		
		doneButton.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				for(Object itemId: itemIds){
					container.getItem(itemId).getItemProperty("TREATMENT").setValue(groupBox.getValue());
					
					
				}
				try {
					container.commit();
				} catch (UnsupportedOperationException e) {
					// TODO Auto-generated catch block
					errorLogger.error("An Error Occured:",e);
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					errorLogger.error("An Error Occured:",e);
					e.printStackTrace();
				}
				close();
				
			}
			
			
			
			
			
		});
		
		buttonLayout.addComponents(doneButton,cancelButton);
		
		groupBox = new ComboBox("Select Group:");
		groupBox.addItem("Control Group");
		groupBox.addItem("Group A");
		groupBox.addItem("Group B");
		groupBox.setTextInputAllowed(false);
		groupBox.setValue(groupBox.getItemIds().iterator().next());
		
		mainLayout.addComponent(groupBox);
		
		
		mainLayout.addComponent(buttonLayout);
		
		
		
		this.setContent(mainLayout);
		
	
	
	
		
		
	
	}}
		
		
		
		