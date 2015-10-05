package com.example.email;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.example.database.DatabaseQuery;
import com.example.floor.BuildingFloor;
import com.example.floor.FloorMap;
import com.example.employee.Employee;
import com.example.energytrack2_0.Log4jContextListener;
import com.vaadin.server.VaadinService;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;


public class TemplateMarker {
	final static Logger errorLogger = Logger.getLogger(TemplateMarker.class);
	private static Configuration cfg;
	private static String url = "/WEB-INF/Resources/Templates";
	
	public static void initConfiguration(){
		
		cfg = new Configuration();
		FileTemplateLoader templateLoader = null;
		String basepath = VaadinService.getCurrent()
                .getBaseDirectory().getAbsolutePath();
		try {
			templateLoader = new FileTemplateLoader(new File(basepath+url));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cfg.setTemplateLoader(templateLoader);
		//cfg.setClassForTemplateLoading(TemplateMarker.class,"/Templates");
		cfg.setIncompatibleImprovements(new Version(2,3,20));
		cfg.setLocale(Locale.US);
	    cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		
		
	}
	
	/*public static String setGroupBIntroMessage(){
		
		Writer out = null;
		try {
			Template template = cfg.getTemplate("Introductory(Group B).ftl");
			out = new StringWriter();
			
			template.process(null, out);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			errorLogger.error("Error in TemplateMarker",e);
			e.printStackTrace();
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			errorLogger.error("Error in TemplateMarker",e);
			e.printStackTrace();
		}
		return out.toString();
	}*/
	
	public static Vector<Employee> setStageOneIntroMessage(){
		initConfiguration();
		
		Writer out = null;
		Vector<Employee> groupEmployees = DatabaseQuery.getGroupEmployees();
		Iterator<Employee> employeeIterator = groupEmployees.iterator();
		while(employeeIterator.hasNext()){
			Employee employee = employeeIterator.next();
			 Map<String, Object> input = new HashMap<String, Object>();
			 input.put("name", employee.getName());
			try {
			Template template = cfg.getTemplate("Introductory(phase1).ftl");
			out = new StringWriter();
			
			template.process(input, out);
			employee.setPersonalisedMessage(out.toString());
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			errorLogger.error("Error in TemplateMarker",e);
			e.printStackTrace();
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			errorLogger.error("Error in TemplateMarker",e);
			e.printStackTrace();
		}
		}
		return groupEmployees;	
	}
	
	public static Vector<Employee> setStageTwoIntroMessageA(){
		initConfiguration();
		Writer out = null;
		Vector<Employee> groupEmployees = DatabaseQuery.getGroupAEmployees();
		Iterator<Employee> employeeIterator = groupEmployees.iterator();
		while(employeeIterator.hasNext()){
			Employee employee = employeeIterator.next();
			 Map<String, Object> input = new HashMap<String, Object>();
			 input.put("name", employee.getName());
			try {
			Template template = cfg.getTemplate("Introductory(phase2.A).ftl");
			out = new StringWriter();
			
			template.process(input, out);
			employee.setPersonalisedMessage(out.toString());
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			errorLogger.error("Error in TemplateMarker",e);
			e.printStackTrace();
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			errorLogger.error("Error in TemplateMarker",e);
			e.printStackTrace();
		}
		}
		return groupEmployees;	
	}
	public static Vector<Employee> setStageTwoIntroMessageB(){
		initConfiguration();
		Writer out = null;
		Vector<Employee> groupEmployees = DatabaseQuery.getGroupBEmployees();
		Iterator<Employee> employeeIterator = groupEmployees.iterator();
		while(employeeIterator.hasNext()){
			Employee employee = employeeIterator.next();
			 Map<String, Object> input = new HashMap<String, Object>();
			 input.put("name", employee.getName());
			try {
			Template template = cfg.getTemplate("Introductory(phase2.B).ftl");
			out = new StringWriter();
			
			template.process(input, out);
			employee.setPersonalisedMessage(out.toString());
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			errorLogger.error("Error in TemplateMarker",e);
			e.printStackTrace();
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			errorLogger.error("Error in TemplateMarker",e);
			e.printStackTrace();
		}
		}
		return groupEmployees;	
	}
	
public static Vector<Employee> setTreatmentOneEmail(){
	initConfiguration();
	Iterator<Employee> employeeIterator = null;
	Vector<Employee> groupEmployees=null;
	groupEmployees = DatabaseQuery.getGroupEmployees();
	employeeIterator = groupEmployees.iterator();
	
	Map<Integer,Float> usageMap = new HashMap<Integer,Float>();
	Map<Integer,Float> changeMap = new HashMap<Integer,Float>();
	Writer out = null;
	while(employeeIterator.hasNext()){
	try{
		 Employee currentEmployee = employeeIterator.next();
		 Map<String, Object> input = new HashMap<String, Object>();
		 input.put("name",currentEmployee.getName());
		 if(usageMap.containsKey(currentEmployee.getFloor())){
			 input.put("usage", usageMap.get(currentEmployee.getFloor()));	 
		 }
		 else{
			 usageMap.put(currentEmployee.getFloor(),DatabaseQuery.getCumulativeUsage(currentEmployee.getFloor()));
			 input.put("usage", usageMap.get(currentEmployee.getFloor()));
		 }
		 if(changeMap.containsKey(currentEmployee.getFloor())){
			 input.put("percentChange", changeMap.get(currentEmployee.getFloor()));}
		 else{
			 changeMap.put(currentEmployee.getFloor(),DatabaseQuery.getWeekChange(currentEmployee.getFloor()));
			 input.put("percentChange", changeMap.get(currentEmployee.getFloor()));
		 }
		 if(changeMap.get(currentEmployee.getFloor())>=0){
		 input.put("moreOrLess", "more");}
		 else{
			 input.put("moreOrLess", "less");
		 }
		 input.put("energyTip",DatabaseQuery.getRandomTip("Employee"));
		 Template template = cfg.getTemplate("Treatment1.ftl");
		 out = new StringWriter();
		 template.process(input,out);
		 currentEmployee.setPersonalisedMessage(out.toString());
		 
	} catch (IOException e) {
		// TODO Auto-generated catch block
		errorLogger.error("Error in TemplateMarker",e);
		e.printStackTrace();
	} catch (TemplateException e) {
		// TODO Auto-generated catch block
		errorLogger.error("Error in TemplateMarker",e);
		e.printStackTrace();
	}}
return groupEmployees;
}

public static Vector<Employee> setCompetitionNoAdvocateEmail(){
	initConfiguration();
	 Vector<Employee> groupEmployees = null;
	 Map<String, Object> input = new HashMap<String, Object>();
	 Vector<BuildingFloor> topFloors = DatabaseQuery.getTopFloors();
	 Iterator<BuildingFloor> floorIterator = topFloors.iterator();
	 Writer out = null;
	 
	 BuildingFloor winner = floorIterator.next();
	 input.put("winnerFloor",FloorMap.getOrdinal(Integer.parseInt(winner.getFloor()))+" Floor");
	 if(winner.getWeekChange()<0){
		 input.put("reducingOrincreasing", "reducing");
		 input.put("winnerMoreOrLess", "reduction");
		 float change =  Math.abs(winner.getWeekChange());
		 input.put("winnerFloorChange",change);
	 }
	 else{
		 input.put("winnerFloorChange", winner.getWeekChange());
		 input.put("reducing/increasing", "increasing");
		 input.put("winnerMoreOrLess", "increase");
	 }
	 
	 BuildingFloor second = floorIterator.next();
	 input.put("secondFloor", FloorMap.getOrdinal(Integer.parseInt(second.getFloor()))+" Floor");
	 if(second.getWeekChange()<0){
		 input.put("secondMoreOrLess", "reduction");
		 input.put("secondFloorChange", Math.abs(second.getWeekChange()));
	 }
	 else{
		 input.put("secondMoreOrLess", "increase");
		 input.put("secondFloorChange", second.getWeekChange());
	 }
	 BuildingFloor third = floorIterator.next();
	 input.put("thirdFloor", FloorMap.getOrdinal(Integer.parseInt(third.getFloor()))+" Floor");
	 
	 if(third.getWeekChange()<0){
		 input.put("thirdMoreOrLess", "reduction");
		 input.put("thirdFloorChange", Math.abs(third.getWeekChange()));
	 }
	 else{
		 input.put("thirdMoreOrLess", "increase");
		 input.put("thirdFloorChange", third.getWeekChange());
	 }
	 
	 input.put("energyTip", DatabaseQuery.getRandomTip("Employee"));
	 input.put("numberOfFloors",BuildingFloor.FLOORS);
	 groupEmployees = DatabaseQuery.getGroupAEmployees();
	 
	 Iterator<Employee> employeeIterator = groupEmployees.iterator();
	 Map<Integer,Integer> rankMap = DatabaseQuery.getFloorRankingsMap();
	 
	 while(employeeIterator.hasNext()){
		 Employee currentEmployee = employeeIterator.next();
		 input.put("name", currentEmployee.getName());
		 input.put("floorPlace", FloorMap.getOrdinal(rankMap.get(currentEmployee.getFloor())));
		 Template template = null;
		try {
			template = cfg.getTemplate("CompetitionNoAdvocateEmail.ftl");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			errorLogger.error("Error in TemplateMarker",e);
			e.printStackTrace();
		}
		 out = new StringWriter();
		 
		 try {
			template.process(input,out);
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			errorLogger.error("Error in TemplateMarker",e);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			errorLogger.error("Error in TemplateMarker",e);
			e.printStackTrace();
		}
		 currentEmployee.setPersonalisedMessage(out.toString());}
	 return groupEmployees;	
		 
		 
		 
	 }

public static Vector<Employee> setCompetitionAdvocateEmail(){
	initConfiguration();
	 Vector<Employee> groupEmployees = null;
	 Map<String, Object> input = new HashMap<String, Object>();
	 Vector<BuildingFloor> topFloors = DatabaseQuery.getTopFloors();
	 Iterator<BuildingFloor> floorIterator = topFloors.iterator();
	 Writer out = null;
	 
	 BuildingFloor winner = floorIterator.next();
	 input.put("winnerFloor",FloorMap.getOrdinal(Integer.parseInt(winner.getFloor()))+" Floor");
	 if(winner.getWeekChange()<0){
		 input.put("reducingOrincreasing", "reducing");
		 input.put("winnerMoreOrLess", "reduction");
		 float change =  Math.abs(winner.getWeekChange());
		 input.put("winnerFloorChange",change);
	 }
	 else{
		 input.put("winnerFloorChange", winner.getWeekChange());
		 input.put("reducingOrincreasing", "increasing");
		 input.put("winnerMoreOrLess", "increase");
	 }
	 
	 BuildingFloor second = floorIterator.next();
	 input.put("secondFloor", FloorMap.getOrdinal(Integer.parseInt(second.getFloor()))+" Floor");
	 if(second.getWeekChange()<0){
		 input.put("secondMoreOrLess", "reduction");
		 input.put("secondFloorChange", Math.abs(second.getWeekChange()));
	 }
	 else{
		 input.put("secondMoreOrLess", "increase");
		 input.put("secondFloorChange", second.getWeekChange());
	 }
	 BuildingFloor third = floorIterator.next();
	 input.put("thirdFloor", FloorMap.getOrdinal(Integer.parseInt(third.getFloor()))+" Floor");
	 
	 if(third.getWeekChange()<0){
		 input.put("thirdMoreOrLess", "reduction");
		 input.put("thirdFloorChange", Math.abs(third.getWeekChange()));
	 }
	 else{
		 input.put("thirdMoreOrLess", "increase");
		 input.put("thirdFloorChange", third.getWeekChange());
	 }
	 
	 input.put("energyTip", DatabaseQuery.getRandomTip("Employee"));
	 input.put("numberOfFloors",BuildingFloor.FLOORS);
	 groupEmployees = DatabaseQuery.getGroupBEmployees();
	 Map<Integer,Employee> energyAdvocates = DatabaseQuery.getEnergyAdvocates();
	 Iterator<Employee> employeeIterator = groupEmployees.iterator();
	 Map<Integer,Integer> rankMap = DatabaseQuery.getFloorRankingsMap();
	 
	 while(employeeIterator.hasNext()){
		 Employee currentEmployee = employeeIterator.next();
		 input.put("name", currentEmployee.getName());
		 if(energyAdvocates.get(currentEmployee.getFloor())!=null){
		 input.put("energyAdvocate", energyAdvocates.get(currentEmployee.getFloor()).getName());}
		 
		 input.put("floorPlace", FloorMap.getOrdinal(rankMap.get(currentEmployee.getFloor())));
		 Template template = null;
		try {
			if(energyAdvocates.get(currentEmployee.getFloor())!= null){
				template = cfg.getTemplate("CompetitionAdvocateEmail.ftl"); 
			 }
			else{
				template = cfg.getTemplate("CompetitionNoAdvocateEmail.ftl");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			errorLogger.error("Error in TemplateMarker",e);
			e.printStackTrace();
		}
		 out = new StringWriter();
		 
		 try {
			template.process(input,out);
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			errorLogger.error("Error in TemplateMarker",e);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			errorLogger.error("Error in TemplateMarker",e);
			e.printStackTrace();
		}
		 currentEmployee.setPersonalisedMessage(out.toString());}
	 return groupEmployees;	
		 
		 
		 
}





/*public static Vector<Employee> setTreatmentThreeEmail(){
	 Vector<Employee> groupEmployees = null;
	 Map<String, Object> input = new HashMap<String, Object>();
	 Vector<Integer> topFloors = DatabaseQuery.getTopFloors();
	 Iterator<Integer> floorIterator = topFloors.iterator();
	 Writer out = null;
	 int floorWinner = floorIterator.next();
	 input.put("floorWinner", FloorMap.getOrdinal(floorWinner)+" Floor");
	 input.put("secondFloor", FloorMap.getOrdinal(floorIterator.next())+" Floor");
	 input.put("thirdFloor",FloorMap.getOrdinal(floorIterator.next())+" Floor");
	 input.put("energyTip", DatabaseQuery.getRandomTip("Employee"));
	 input.put("numberOfFloors",BuildingFloor.FLOORS);
	 
	 groupEmployees = DatabaseQuery.getGroupBEmployees();
	
	 Iterator<Employee> employeeIterator = groupEmployees.iterator();
	 
	 Map<Integer,Float> usageMap = new HashMap<Integer,Float>();
	 Map<Integer,Integer> floorRanking = new HashMap<Integer,Integer>();
	 Map<Integer,Employee> energyAdvocate = DatabaseQuery.getEnergyAdvocates();
	 
	 input.put("winnerAdvocate", energyAdvocate.get(floorWinner));
	 DatabaseQuery.randomEnergyAdvocate();
	 Map<Integer,Employee> newEnergyAdvocates = DatabaseQuery.getEnergyAdvocates();
	 
	 while(employeeIterator.hasNext()){
		 Employee currentEmployee = employeeIterator.next();
		 if(usageMap.containsKey(currentEmployee.getFloor())){
			 input.put("energyUsage", usageMap.get(currentEmployee.getFloor()));
		 }
		 else{
			 usageMap.put(currentEmployee.getFloor(),DatabaseQuery.getCumulativeUsage(currentEmployee.getFloor()));
			 input.put("energyUsage", usageMap.get(currentEmployee.getFloor()));
		 }
		 if(floorRanking.containsKey(currentEmployee.getFloor())){
			 input.put("floorPlace", positionFormat(floorRanking.get(currentEmployee.getFloor())));
		 }
		 else{
			 floorRanking.put(currentEmployee.getFloor(),DatabaseQuery.getFloorRanking(currentEmployee.getFloor()));
			 input.put("floorPlace",positionFormat(floorRanking.get(currentEmployee.getFloor())));
		 }
		 if(energyAdvocate.get(currentEmployee.getFloor()).getName()!=null){
			 input.put("energyAdvocate",energyAdvocate.get(currentEmployee.getFloor()).getName());}
		 input.put("newEnergyAdvocate",newEnergyAdvocates.get(currentEmployee.getFloor()).getName());
	
		 Template template = null;
			try {
				if(input.get("energyAdvocate")==null && input.get("winnerAdvocate")!=null){
					template = cfg.getTemplate("Treatment3B.ftl");
				}
			
				else if(input.get("winnerAdvocate")==null && input.get("energyAdvocate")!=null){
					template =cfg.getTemplate("Treatment3C.ftl");	
				}
				else if(input.get("winnerAdvocate")==null && input.get("energyAdvocate")==null){
					template = cfg.getTemplate("Treatment3D.ftl");
				}
				else{
					template = cfg.getTemplate("Treatment3A.ftl");}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				errorLogger.error("Error in TemplateMarker",e);
				e.printStackTrace();
			}
			 out = new StringWriter();
			 try {
				template.process(input,out);
			} catch (TemplateException e) {
				// TODO Auto-generated catch block
				errorLogger.error("Error in TemplateMarker",e);
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				errorLogger.error("Error in TemplateMarker",e);
				e.printStackTrace();
			}
			 currentEmployee.setPersonalisedMessage(out.toString());}
		 
		 
		 
		 return groupEmployees;	
		 }*/

public static Vector<Employee> setEnergyAdvocateEmail(){
	initConfiguration();
	Writer out = null;
	Vector<Employee> energyAdvocates = new Vector<Employee>();
	
	Map<Integer,Employee> advocatesMap = DatabaseQuery.getEnergyAdvocates();
	Map<String, Object> input = new HashMap<String, Object>();
	
	Vector<String> energyTips = DatabaseQuery.getThreeRandomTips("Energy Adv");
	Iterator<String> tipIterator = energyTips.iterator();
	int count = 1;
	String energyTipString = "";
	while(tipIterator.hasNext()){
		energyTipString += count+". "+tipIterator.next();
		if(tipIterator.hasNext()){
			energyTipString+="<br /><br />";
		}
		count++;
	}
		input.put("energyTips",energyTipString);
		
	for(Employee advocate: advocatesMap.values()){
		input.put("energyAdvocate", advocate.getName());
		 Template template = null;
			try {
				template = cfg.getTemplate("EnergyAdvocateA.ftl");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				errorLogger.error("Error in TemplateMarker",e);
				e.printStackTrace();
			}
			 out = new StringWriter();
			 try {
				template.process(input,out);
			} catch (TemplateException e) {
				// TODO Auto-generated catch block
				errorLogger.error("Error in TemplateMarker",e);
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				errorLogger.error("Error in TemplateMarker",e);
				e.printStackTrace();
			}
			 advocate.setPersonalisedMessage(out.toString());
			 energyAdvocates.add(advocate);}
		 return energyAdvocates;
		
		
	}



public static Vector<Employee> setEmployeeTipEmail(){
	initConfiguration();
	Writer out = null;
	Vector<Employee> groupEmployees = new Vector<Employee>();
	
	groupEmployees = DatabaseQuery.getGroupEmployees();
	Map<String, Object> input = new HashMap<String, Object>();
	
	Vector<String> energyTips = DatabaseQuery.getThreeRandomTips("Employee");
	Iterator<String> tipIterator = energyTips.iterator();
	
	Iterator<Employee> employeeIterator = groupEmployees.iterator();
	int count = 1;
	String energyTipString = "<b>";
	while(tipIterator.hasNext()){
		energyTipString += count+". "+tipIterator.next();
		if(tipIterator.hasNext()){
			energyTipString+="<br />";
		}
		count++;
	}
		energyTipString += "</b>";
		input.put("energyTips",energyTipString);
		
	while(employeeIterator.hasNext()){
		Employee employee = employeeIterator.next();
		input.put("name", employee.getName());
		 Template template = null;
			try {
				template = cfg.getTemplate("EnergyTips(phase1).ftl");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				errorLogger.error("Error in TemplateMarker",e);
				e.printStackTrace();
			}
			 out = new StringWriter();
			 try {
				template.process(input,out);
			} catch (TemplateException e) {
				// TODO Auto-generated catch block
				errorLogger.error("Error in TemplateMarker",e);
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				errorLogger.error("Error in TemplateMarker",e);
				e.printStackTrace();
			}
			 employee.setPersonalisedMessage(out.toString());}
		 return groupEmployees;
		
		
	}


public static Vector<Employee> setAfternoonReminder(){
	initConfiguration();
	Writer out = null;
	Vector<Employee> groupEmployees = DatabaseQuery.getGroupEmployees();
	Iterator<Employee> employeeIterator = groupEmployees.iterator();
	while(employeeIterator.hasNext()){
		Employee employee = employeeIterator.next();
		 Map<String, Object> input = new HashMap<String, Object>();
		 input.put("name", employee.getName());
		 input.put("floor",FloorMap.getOrdinal(employee.getFloor()));
		try {
		Template template = cfg.getTemplate("AfternoonReminder.ftl");
		out = new StringWriter();
		
		template.process(input, out);
		employee.setPersonalisedMessage(out.toString());
	
	} catch (IOException e) {
		// TODO Auto-generated catch block
		errorLogger.error("Error in TemplateMarker",e);
		e.printStackTrace();
	} catch (TemplateException e) {
		// TODO Auto-generated catch block
		errorLogger.error("Error in TemplateMarker",e);
		e.printStackTrace();
	}
	}
	return groupEmployees;
	
}

public static Vector<Employee> setKitchenTipEmail(){
	initConfiguration();
	Writer out = null;
	Vector<Employee> groupEmployees = DatabaseQuery.getGroupEmployees();
	Iterator<Employee> employeeIterator = groupEmployees.iterator();
	while(employeeIterator.hasNext()){
		Employee employee = employeeIterator.next();
		 Map<String, Object> input = new HashMap<String, Object>();
		 input.put("name", employee.getName());
		try {
		Template template = cfg.getTemplate("KitchenTips.ftl");
		out = new StringWriter();
		
		template.process(input, out);
		employee.setPersonalisedMessage(out.toString());
	
	} catch (IOException e) {
		// TODO Auto-generated catch block
		errorLogger.error("Error in TemplateMarker",e);
		e.printStackTrace();
	} catch (TemplateException e) {
		// TODO Auto-generated catch block
		errorLogger.error("Error in TemplateMarker",e);
		e.printStackTrace();
	}
	}
	return groupEmployees;
	
}

public static Vector<Employee> setAdvocateNotiEmail(){
	initConfiguration();
	Writer out = null;
	Map<String, Object> input = new HashMap<String, Object>();
	Vector<Employee> emailEmployees = new Vector<Employee>();
	Map<Integer,Employee> advocatesMap = DatabaseQuery.getEnergyAdvocates();
	
	Vector<Employee> groupEmployees = DatabaseQuery.getGroupBEmployees();
	
	for(Employee employee : groupEmployees){
		if(advocatesMap.get(employee.getFloor()).getName().equals(employee.getName())){
			continue;
		}
		
		else{
			input.put("name",employee.getName());
			input.put("energyAdvocate",advocatesMap.get(employee.getFloor()).getName());
			Template template = null;
			try {
				template = cfg.getTemplate("EnergyAdvocateB.ftl");
				out = new StringWriter();
				template.process(input,out);
				employee.setPersonalisedMessage(out.toString());
				
				emailEmployees.add(employee);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				errorLogger.error("Error in TemplateMarker",e);
				e.printStackTrace();
			}
			 catch (TemplateException e) {
				// TODO Auto-generated catch block
				errorLogger.error("Error in TemplateMarker",e);
				e.printStackTrace();
			} 
		
		}
		
		
		
	}
		return emailEmployees;
	
}

public static String positionFormat(int place){
	initConfiguration();
	String stringPlace = Integer.toString(place);
	if(stringPlace.charAt(stringPlace.length()-1)=='1'){
		stringPlace += "st";
	}
	else if(stringPlace.charAt(stringPlace.length()-1)=='2'){
		stringPlace += "nd";
	}
	else if(stringPlace.charAt(stringPlace.length()-1)=='3'){
		stringPlace += "rd";
	}
	else{
		stringPlace += "th";
	}
	return stringPlace;
	}
	
	
	
}
	
	 
