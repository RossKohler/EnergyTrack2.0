package com.example.energytrack2_0;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import javafx.stage.StageStyle;

import com.example.database.DatabaseConnection;
import com.example.database.FirebirdGenerator;
import com.example.email.EmailManagement;
import com.example.programpreferences.ProgramPreferences;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;



public class ProjectLayout extends VerticalLayout{
	final static Logger errorLogger = Logger.getLogger(Log4jContextListener.class);
	
	private Panel projectStage;
	private OptionGroup projectStageGroup;
	private VerticalLayout projectStageLayout;
	private List<String> stages = Arrays.asList("Stage 1","Stage 2","Stage 3");
	
	private String floorQueryString = "BUILDING_PROFILE";
	private SQLContainer floorContainer;
	
	
	private HorizontalLayout buttonLayout;
	private HorizontalLayout introLayout;
	private Label notifyLabel;
	
	
	public ProjectLayout(){
		this.setSpacing(true);
		
		projectStage = new Panel("Project Stage");
		projectStageGroup = new OptionGroup("Please select the stage of the project below:");
		
		projectStageLayout = new VerticalLayout();
		projectStageLayout.setSpacing(true);
		projectStageGroup.addItems(stages);
		projectStageGroup.setValue("Stage "+ProgramPreferences.getProjectStage());
		projectStageGroup.addValueChangeListener(new ValueChangeListener(){
			
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				buttonLayout.setEnabled(true);
				notifyLabel.setVisible(false);
				
			}
	
			
		});
		
		
		
		projectStageLayout.addComponent(projectStageGroup);
		
		buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);
		buttonLayout.setEnabled(false);
		Button saveButton = new Button("Save Changes");
		saveButton.addClickListener(new Button.ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				ProgramPreferences.setProjectStage(stages.indexOf(projectStageGroup.getValue().toString())+1);
				buttonLayout.setEnabled(false);
				notifyLabel.setVisible(true);
				
			}
			
			
			
			
			
			
		});
		Button cancelButton = new Button("Cancel");
		cancelButton.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				projectStageGroup.setValue("Stage "+ProgramPreferences.getProjectStage());
				buttonLayout.setEnabled(false);
				notifyLabel.setVisible(true);
				
				
			}
		});
		notifyLabel = new Label("Change the project stage to enable the buttons.");
		buttonLayout.addComponents(saveButton,cancelButton,notifyLabel);
		projectStageLayout.addComponent(buttonLayout);
		projectStage.setContent(projectStageLayout);
		this.addComponent(projectStage);
		
		Panel projectTablePanel = new Panel();
		VerticalLayout projectTableLayout = new VerticalLayout();
		
		introLayout = new HorizontalLayout();
		introLayout.setSpacing(true);
		Button sendButton = new Button("Send");
		sendButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				Thread t1 = new Thread(new Runnable() {
				     public void run()
				     {
				    	 int stage = ProgramPreferences.getProjectStage();
				    	 if(stage !=1){
				    		 errorLogger.info("Sending project introductory emails (stage +"+stage+")..");
				    		 System.out.println("Sending Introductory emails...");
				    	 if(stage ==2){
				    		 EmailManagement.sendStageOneIntroEmail();
				    		 errorLogger.info("*Send introductory email method* finished (stage +"+stage+").");
				    	 }
				    	 else if(stage == 3){
				    		 EmailManagement.sendStageTwoIntroEmailA();
				    		 EmailManagement.sendStageTwoIntroEmailB();
				    		 errorLogger.info("*Send introductory email method* finished (stage +"+stage+").");
				    	 }}
				    	 
				    	
				    	
				     }});  t1.start();
				
			}
		});
		
		
		
		
		Label sendIntroLabel = new Label();
		sendIntroLabel.setValue("Send Stage "+ProgramPreferences.getProjectStage()+" emails to introduce the employees to the project.");
		
		introLayout.addComponents(sendButton,sendIntroLabel);
		projectStageLayout.addComponent(introLayout);
		
		Table floorTable = new Table();
		TableQuery floorQuery = new TableQuery(floorQueryString, DatabaseConnection.retrieveJ2EEConnectionPool(),new FirebirdGenerator());
		try {
			floorContainer = new SQLContainer(floorQuery);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			errorLogger.error("An Error Occured:",e);
			e.printStackTrace();
		}
		
		floorTable.setContainerDataSource(floorContainer);
		floorTable.setVisibleColumns(new Object[]{"FLOOR","TREATMENT","ENERGY_ADVOCATE","NUMBER_EMPLOYEES","RANK"});
		floorTable.setColumnHeaders(new String[]{"Floor #","Treatment Group","Energy Advocate","No. Employees","Floor Rank"});
		floorTable.setSelectable(true);
		floorTable.setMultiSelect(true);
		
	
		
		projectTableLayout.addComponent(floorTable);
		
		Button editGroupButton = new Button("Change Group(s)");
		editGroupButton.addListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(floorTable.getValue()!=null){
					getUI().getCurrent().addWindow(new ModifyGroup(floorContainer,(Set)floorTable.getValue()));}
			}
		});
		
		floorTable.addItemClickListener(new ItemClickEvent.ItemClickListener() {
			
			@Override
			public void itemClick(ItemClickEvent event) {
				editGroupButton.setEnabled(true);
				
			}
		});
		
		projectTableLayout.setSizeFull();
		projectTableLayout.addComponent(editGroupButton);
		projectTableLayout.setSpacing(true);
		projectTablePanel.setContent(projectTableLayout);
		this.addComponent(projectTablePanel);
		
		
		
		
	}
	
	
	
	
	

}
