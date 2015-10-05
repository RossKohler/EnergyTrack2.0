package com.example.energytrack2_0;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.example.energytips.EnergyTip;
import com.vaadin.data.Item;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

public class ModifyEnergyTip extends Window {
	final static Logger errorLogger = Logger.getLogger(Log4jContextListener.class);
	private SQLContainer container;
	private VerticalLayout mainLayout;
	private Object selectedRowId;
	private boolean isEdit;
	private HorizontalLayout buttonLayout;

	public ModifyEnergyTip(SQLContainer container) {
		// TODO Auto-generated constructor stub
		this.container = container;
		initWindow();
		
		
		
	}

	public ModifyEnergyTip(SQLContainer container, Object itemId) {
		// TODO Auto-generated constructor stub
		this.selectedRowId = itemId;
		this.container = container;
		isEdit = true;
		initWindow();
	}
	
	
	
	
	public void initWindow(){
		this.setModal(true);
		this.setWidth("20%");
		this.setHeight("50%");
		this.center();
		
		
		
		
		mainLayout = new VerticalLayout();
		mainLayout.setMargin(true);
		mainLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		TextArea energyTip = new TextArea("Energy Tip:");
		ComboBox roleBox = new ComboBox("For who?:");
		roleBox.setImmediate(true);
		roleBox.setNullSelectionAllowed(false);
		CheckBox enabled = new CheckBox("Enabled:");
		Button insertButton = new Button();
		
		for(String role : EnergyTip.roles){
			roleBox.addItem(role);
		}
		
		if(isEdit){
			energyTip.setValue(container.getItem(selectedRowId).getItemProperty("SAVINGS_TIP").getValue().toString());
			String enabledString = container.getItem(selectedRowId).getItemProperty("ENABLED").getValue().toString();
			if(enabledString.equals("1")){
				enabled.setValue(true);
			}
			else{
				enabled.setValue(false);
			}
			insertButton.setCaption("Done");
			
			java.util.Iterator<?> roleIterator = roleBox.getItemIds().iterator();
			String selectedRole = container.getItem(selectedRowId).getItemProperty("ROLE").getValue().toString();
			while(roleIterator.hasNext()){
				Object role = roleIterator.next();
				if(selectedRole.equals(role.toString())){
					roleBox.setValue(role);
					break;
				}
			}
			
		}
		else{
			insertButton.setCaption("Insert");
			roleBox.setValue(roleBox.getItemIds().iterator().next());
			
		}
		
		
		
		
	
		
		mainLayout.addComponent(energyTip);
		mainLayout.addComponent(roleBox);
		mainLayout.addComponent(enabled);
		
		buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);
		
		Button cancelButton = new Button("Cancel");
		
		cancelButton.addClickListener(new Button.ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				close();	
			}	
		});
		
		insertButton.addClickListener(new Button.ClickListener(){
			

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Object rowId;
				if(isEdit){
					rowId = selectedRowId;
				}
				else{
					rowId = container.addItem();
				}
				container.getItem(rowId).getItemProperty("SAVINGS_TIP").setValue(energyTip.getValue());
				container.getItem(rowId).getItemProperty("ROLE").setValue(roleBox.getValue());
				if(enabled.getValue()){
					container.getItem(rowId).getItemProperty("ENABLED").setValue("1");
				}
				else{
					container.getItem(rowId).getItemProperty("ENABLED").setValue("0");
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
		
		
		buttonLayout.addComponent(insertButton);
		buttonLayout.addComponent(cancelButton);
		
		mainLayout.addComponent(buttonLayout);
		
		this.setContent(mainLayout);
		
		
		
		
		
		
	}

}
