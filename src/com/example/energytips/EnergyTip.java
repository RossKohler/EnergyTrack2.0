package com.example.energytips;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class EnergyTip {
private SimpleIntegerProperty ID = new SimpleIntegerProperty();
private SimpleStringProperty energyTip = new SimpleStringProperty();
private SimpleBooleanProperty enabled = new SimpleBooleanProperty();
private SimpleStringProperty role = new SimpleStringProperty();

public static List<String> roles = Arrays.asList("Employee","Energy Advocate");
public String getEnergyTip(){
	return energyTip.get();
	
}

public String getRole(){
	return role.get();
}

public int getID(){
	return ID.get();
}
public boolean getEnabled(){
	return enabled.get();
	
}
public BooleanProperty enabledProperty(){
	return enabled;
}

public void setEnergyTip(String energyTip ){	
	this.energyTip.set(energyTip);
}

public void setEnabled(boolean enabled){
	this.enabled.set(enabled);
}

public void setID(int ID){
	this.ID.set(ID);
}

public void setRole(String role){
	this.role.set(role);
}



}
