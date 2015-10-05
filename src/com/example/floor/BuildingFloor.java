package com.example.floor;
import java.text.DecimalFormat;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

	
public class BuildingFloor{
	
   public static final int FLOORS = 24;
   
   private SimpleStringProperty floor = new SimpleStringProperty();
   private SimpleFloatProperty  currentUsage = new SimpleFloatProperty();
   private SimpleStringProperty treatment = new SimpleStringProperty();

   private SimpleFloatProperty  dayChange = new SimpleFloatProperty();
   private SimpleFloatProperty  dayNormalUsage = new SimpleFloatProperty(); 
   private SimpleFloatProperty  dayHighUsage = new SimpleFloatProperty();
   private SimpleFloatProperty  dayLowUsage = new SimpleFloatProperty();
   
   private SimpleFloatProperty weekChange = new SimpleFloatProperty();
   private SimpleFloatProperty weekNormalUsage = new SimpleFloatProperty();
   private SimpleFloatProperty weekHighUsage = new SimpleFloatProperty();
   private SimpleFloatProperty weekLowUsage = new SimpleFloatProperty();
   
   private SimpleFloatProperty monthChange = new SimpleFloatProperty();
   private SimpleFloatProperty monthNormalUsage = new SimpleFloatProperty();
   private SimpleFloatProperty monthHighUsage = new SimpleFloatProperty();
   private SimpleFloatProperty monthLowUsage = new SimpleFloatProperty();
   
   private SimpleStringProperty energyAdvocate = new SimpleStringProperty();
   private SimpleIntegerProperty employeeCount = new SimpleIntegerProperty();
   
   private int rank;
   private float weekCumulativeUsage;
   
   DecimalFormat twoDForm = new DecimalFormat("#.##");
   
   
   
   
   /*public SimpleFloatProperty  highUsage = new SimpleFloatProperty();
   public SimpleFloatProperty  lowUsage = new SimpleFloatProperty();
   public SimpleFloatProperty  currentUsage = new SimpleFloatProperty();
   public SimpleStringProperty  treatment = new SimpleStringProperty();*/
   
   

 
 public String getFloor(){
	 return floor.get();
 }
 
 public int getRank(){
	 return rank;
 }
 

public float getCurrentUsage(){
	return Float.valueOf(twoDForm.format(currentUsage.get()));
} 
public float getDayChange() {
	return Float.valueOf(twoDForm.format(dayChange.get()));
}
public float getDayNormalUsage(){
	
	return Float.valueOf(twoDForm.format(dayNormalUsage.get()));
}

public float getDayHighUsage(){
	return Float.valueOf(twoDForm.format(dayHighUsage.get()));
}

public float getDayLowUsage(){
	return Float.valueOf(twoDForm.format(dayLowUsage.get()));
}
public String getTreatment(){
	return treatment.get();
}

public float getWeekLowUsage(){
	return Float.valueOf(twoDForm.format(weekLowUsage.get()));
}
public float getWeekHighUsage(){
	return Float.valueOf(twoDForm.format(weekHighUsage.get()));
}
public float getWeekNormalUsage(){
	return Float.valueOf(twoDForm.format(weekNormalUsage.get()));
}
public float getWeekChange(){
	return Float.valueOf(twoDForm.format(weekChange.get()));
}

public float getMonthChange() {
	return Float.valueOf(twoDForm.format(monthChange.get()));
}
public float getMonthNormalUsage(){
	
	return Float.valueOf(twoDForm.format(monthNormalUsage.get()));
}

public float getMonthHighUsage(){
	return Float.valueOf(twoDForm.format(monthHighUsage.get()));
}

public float getMonthLowUsage(){
	return Float.valueOf(twoDForm.format(monthLowUsage.get()));
}

public String getEnergyAdvocate(){
	return energyAdvocate.get();
	}

public int getEmployeeCount(){
	return employeeCount.get();
}

public float getWeekCumulativeUsage(){
	return Float.valueOf(twoDForm.format(weekCumulativeUsage));
}

public void setFloor(String floor){
	this.floor.set(floor);
}


public void setCurrentUsage(float currentUsage){
	this.currentUsage.set(currentUsage);
}


public void setDayChange(float energyChange){
	this.dayChange.set(energyChange);
}

public void setDayNormalUsage(float normalUsage){
	this.dayNormalUsage.set(normalUsage);
}

public void setDayLowUsage(float lowUsage){
	this.dayLowUsage.set(lowUsage);
}

public void setDayHighUsage(float highUsage){
	this.dayHighUsage.set(highUsage);
}

public void setTreatment(String treatment){
	this.treatment.set(treatment);
}

public void setWeekHighUsage(float weekHighUsage){
	this.weekHighUsage.set(weekHighUsage);
}
public void setWeekLowUsage(float weekLowUsage){
	this.weekLowUsage.set(weekLowUsage);
}
public void setWeekChange(float weekChange){
	this.weekChange.set(weekChange);
}
public void setWeekNormalUsage(float weekNormalUsage){
	this.weekNormalUsage.set(weekNormalUsage);
}

public void setMonthHighUsage(float monthHighUsage){
	this.monthHighUsage.set(monthHighUsage);
}
public void setMonthLowUsage(float monthLowUsage){
	this.monthLowUsage.set(monthLowUsage);
}
public void setMonthChange(float monthChange){
	this.monthChange.set(monthChange);
}
public void setMonthNormalUsage(float monthNormalUsage){
	this.monthNormalUsage.set(monthNormalUsage);
}

public void setEnergyAdvocate(String energyAdvocate){
	this.energyAdvocate.set(energyAdvocate);
}

public void setEmployeeCount(int employeeCount){
	this.employeeCount.set(employeeCount);
}

public void setWeekCumulativeUsage(float weekCumulativeUsage){
	this.weekCumulativeUsage= weekCumulativeUsage;
}
public void setRank(int rank){
	this.rank = rank;
	
}


@Override
public String toString() {
	return "BuildingFloor [floor=" + floor + ", currentUsage=" + currentUsage
			+ ", treatment=" + treatment + ", dayChange=" + dayChange
			+ ", dayNormalUsage=" + dayNormalUsage + ", dayHighUsage="
			+ dayHighUsage + ", dayLowUsage=" + dayLowUsage + ", weekChange="
			+ weekChange + ", weekNormalUsage=" + weekNormalUsage
			+ ", weekHighUsage=" + weekHighUsage + ", weekLowUsage="
			+ weekLowUsage + ", monthChange=" + monthChange
			+ ", monthNormalUsage=" + monthNormalUsage + ", monthHighUsage="
			+ monthHighUsage + ", monthLowUsage=" + monthLowUsage
			+ ", twoDForm=" + twoDForm + ", getFloor()=" + getFloor()
			+ ", getCurrentUsage()=" + getCurrentUsage() + ", getDayChange()="
			+ getDayChange() + ", getDayNormalUsage()=" + getDayNormalUsage()
			+ ", getDayHighUsage()=" + getDayHighUsage()
			+ ", getDayLowUsage()=" + getDayLowUsage() + ", getTreatment()="
			+ getTreatment() + ", getWeekLowUsage()=" + getWeekLowUsage()
			+ ", getWeekHighUsage()=" + getWeekHighUsage()
			+ ", getWeekNormalUsage()=" + getWeekNormalUsage()
			+ ", getWeekChange()=" + getWeekChange() + ", getMonthChange()="
			+ getMonthChange() + ", getMonthNormalUsage()="
			+ getMonthNormalUsage() + ", getMonthHighUsage()="
			+ getMonthHighUsage() + ", getMonthLowUsage()="
			+ getMonthLowUsage() + "]";
}






}
 