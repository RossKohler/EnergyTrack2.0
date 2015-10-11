package com.example.energytrack2_0;

import java.sql.SQLException;
import java.util.Set;

import org.apache.log4j.Logger;

import com.example.database.DatabaseConnection;
import com.example.database.FirebirdGenerator;
import com.vaadin.data.Property;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class EmployeeLayout extends VerticalLayout {
	
	private Table employeeTable;
	private VerticalLayout mainLayout;
	private String employeeQuery = "EMPLOYEE_PROFILES";
	private SQLContainer container;
	private HorizontalLayout buttonLayout;
	
	private Button addButton;
	private Button editButton;
	private Button removeButton;

	
	final static Logger errorLogger = Logger.getLogger(Log4jContextListener.class);
	
	public EmployeeLayout(){
		this.setMargin(true);
		Panel testPanel = new Panel();
		mainLayout = new VerticalLayout();
		employeeTable = new Table();
		TableQuery tableQuery = new TableQuery(employeeQuery, DatabaseConnection.retrieveJ2EEConnectionPool(), new FirebirdGenerator());
		try {
			container = new SQLContainer(tableQuery);
			container.setAutoCommit(false);
		} catch (SQLException e) {
			errorLogger.error("An Error Occured:",e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		employeeTable.setContainerDataSource(container);
		employeeTable.addGeneratedColumn("TREATMENT", new BooleanColumnGenerator("TREATMENT", true, null));
		employeeTable.setSelectable(true);
		employeeTable.setMultiSelect(true);
		mainLayout.addComponent(employeeTable);
		mainLayout.setExpandRatio(employeeTable, 1.0f);
		mainLayout.setSpacing(true);
		employeeTable.setSizeFull();
		buttonLayout = new HorizontalLayout();

		addButton = new Button("New");
		editButton = new Button("Edit");
		removeButton = new Button("Remove");
		
		buttonLayout.addComponent(addButton);
		buttonLayout.addComponent(editButton);
		buttonLayout.addComponent(removeButton);
		buttonLayout.setSpacing(true);
		mainLayout.addComponent(buttonLayout);
		//buttonLayout.setSizeFull();
		addButton.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				getUI().getCurrent().addWindow(new ModifyEmployee(container));
				
			}
		});
		
	editButton.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				for(Object itemId: (Set)employeeTable.getValue()){
					getUI().getCurrent().addWindow(new ModifyEmployee(container,itemId));
					break;
				}
				
				
			}
		});

		
		
		
		removeButton.addClickListener(new Button.ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				
				for(Object itemId: (Set)employeeTable.getValue()){
					container.removeItem(itemId);

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
				
			}
		});
		
		this.addComponent(mainLayout);
		mainLayout.setSizeFull();
		this.setSizeFull();
		//this.setExpandRatio(mainLayout, 1.0f);
		
		
		
		
		
		
		
		
		
		
		
		
	}
	
	
	  class BooleanColumnGenerator implements Table.ColumnGenerator {
	        /**
	         * Generates the cell containing an open image when boolean is true
	         */
		    private String originId = "";
		    private boolean output = true;
		    private String capt = null;
		    
		    public BooleanColumnGenerator(String originPropId, boolean readOnly, String caption){
		        originId = originPropId;
		        output = readOnly;
		        capt = caption;
		    }
		  
		  
	        public Component generateCell(Table source, Object itemId, Object columnId) {
	        	  Property prop = source.getItem(itemId).getItemProperty(originId);
	        	  boolean val;
	        	  if(prop.getValue().toString().equals("1")){
	        		  val = true;
	        	  }
	        	  else{
	        		  val = false;
	        	  }
	            
	              
	              CheckBox ret = new CheckBox(capt,val);
	              ret.setReadOnly(true);
	              
	              return ret;
	    }
	
	

}}
