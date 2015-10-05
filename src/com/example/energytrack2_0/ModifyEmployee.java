package com.example.energytrack2_0;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.example.database.DatabaseConnection;
import com.example.database.FirebirdGenerator;
import com.google.gwt.dev.js.rhino.ObjToIntMap.Iterator;
import com.vaadin.data.Item;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ModifyEmployee extends Window {
	
	private SQLContainer container;
	
	private TextField firstName;
	private TextField lastName;
	private TextField email;
	private ComboBox floors;
	private CheckBox treatment;
	private VerticalLayout mainLayout;
	private HorizontalLayout buttonLayout;
	private Object selectedRowId;
	private boolean isedit;
	
	private SQLContainer comboBoxContainer;
	
	final static Logger errorLogger = Logger.getLogger(Log4jContextListener.class);
	public ModifyEmployee(SQLContainer container, Object rowId){
		
		this.selectedRowId = rowId;
		this.container = container;
		isedit = true;
		initWindow();
	}
	
	
	
	
	public ModifyEmployee(SQLContainer container){
		this.container = container;
		initWindow();
	}
	
	
	private void initWindow(){
		this.setModal(true);
		this.setWidth("20%");
		this.setHeight("50%");
		this.center();
		
		
		
		
		mainLayout = new VerticalLayout();
		mainLayout.setMargin(true);
		mainLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		firstName = new TextField("First Name:");
		firstName.setRequired(true);
		lastName = new TextField("Last Name:");
		lastName.setRequired(true);
		email = new TextField("Email:");
		email.setRequired(true);
		treatment = new CheckBox("Treatment");
		treatment.setRequired(true);
		floors = new ComboBox("Floor:");
		//floors.setItemCaptionMode(ItemCaptionMode.);
		floors.setRequired(true);
		floors.setImmediate(true);
		floors.setItemCaptionPropertyId("FLOOR");
		floors.setTextInputAllowed(false);
		floors.setNullSelectionAllowed(false);
		//floors.setItemCaptionMode(ItemCaptionMode.ITEM);
		
		TableQuery comboBoxQuery = new TableQuery("BUILDING_PROFILE",DatabaseConnection.retrieveJ2EEConnectionPool(),new FirebirdGenerator());
		
		try {
			comboBoxContainer = new SQLContainer(comboBoxQuery);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			errorLogger.error("An Error Occured:",e);
			e.printStackTrace();
		}
		
		floors.setContainerDataSource(comboBoxContainer);
		
		if(isedit){
			String name = container.getItem(selectedRowId).getItemProperty("NAME").getValue().toString();
			
			firstName.setValue(name.substring(0,name.indexOf(' ')));
			lastName.setValue(name.substring(name.indexOf(' ')+1));
			
			Object emailValue = container.getItem(selectedRowId).getItemProperty("EMAIL").getValue();
			if(emailValue!= null){
				email.setValue(emailValue.toString());}
			
			String treatmentString = container.getItem(selectedRowId).getItemProperty("TREATMENT").getValue().toString();
			if(treatmentString.equals("1")){
				treatment.setValue(true);
			}
			else{
				treatment.setValue(false);
			}
	
			java.util.Iterator<?> floorIterator = floors.getItemIds().iterator();
			String selectedFloor = container.getItem(selectedRowId).getItemProperty("FLOOR").getValue().toString();
			while(floorIterator.hasNext()){
				Object floor = floorIterator.next();
				if(selectedFloor.equals(floor.toString())){
					floors.setValue(floor);
					break;
				}
			}
		}
		
		else{
			floors.setValue(floors.getItemIds().iterator().next());
			
		}
		
		
		
		
		mainLayout.setSpacing(true);
		
		mainLayout.addComponent(firstName);
		mainLayout.addComponent(lastName);
		mainLayout.addComponent(email);
		mainLayout.addComponent(treatment);
		mainLayout.addComponent(floors);
		
		buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);
		
		Button insertButton = new Button();
		if(isedit){
			insertButton.setCaption("Done");
		}
		else{
			insertButton.setCaption("Insert");
		}
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
				if(isedit){
					rowId = selectedRowId;
				}
				else{
					rowId = container.addItem();
				}
				
				container.getItem(rowId).getItemProperty("NAME").setValue(firstName.getValue()+" "+lastName.getValue());
				container.getItem(rowId).getItemProperty("EMAIL").setValue(email.getValue());
				Item item = floors.getItem(floors.getValue());
				container.getItem(rowId).getItemProperty("FLOOR").setValue((int)item.getItemProperty("FLOOR").getValue());
				if(treatment.getValue()){
					container.getItem(rowId).getItemProperty("TREATMENT").setValue("1");
				}
				else{
					container.getItem(rowId).getItemProperty("TREATMENT").setValue("0");
				}
				try {
					container.commit();
				} catch (UnsupportedOperationException e) {
					errorLogger.error("An Error Occured:",e);
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					errorLogger.error("An Error Occured:",e);
					// TODO Auto-generated catch block
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
