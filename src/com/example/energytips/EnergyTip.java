package com.example.energytips;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnergyTip {
private int ID;
private String energyTip;
private boolean enabled;
private String role;

public static List<String> roles = Arrays.asList("Employee","Energy Advocate");
public String getEnergyTip(){
	return energyTip;
	
}

public String getRole(){
	return role;
}

public int getID(){
	return ID;
}
public boolean getEnabled(){
	return enabled;
	
}
public boolean enabledProperty(){
	return enabled;
}

public void setEnergyTip(String energyTip ){	
	this.energyTip = energyTip;
}

public void setEnabled(boolean enabled){
	this.enabled = enabled;
}

public void setID(int ID){
	this.ID = ID;
}

public void setRole(String role){
	this.role = role;
}



}
