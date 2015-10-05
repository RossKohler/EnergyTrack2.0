package com.example.employee;
import java.text.DecimalFormat;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

	
public class Employee{
   
   private SimpleIntegerProperty ID = new SimpleIntegerProperty();
   private SimpleStringProperty name = new SimpleStringProperty(); 
   private SimpleIntegerProperty floor = new SimpleIntegerProperty();
   private SimpleBooleanProperty treatment = new SimpleBooleanProperty();
   private SimpleStringProperty email = new SimpleStringProperty();
   
   public String personalisedMessage;
   
   DecimalFormat twoDForm = new DecimalFormat("#.##");

   public Integer getID() {
      return ID.get();
   }

   public String getName() {
      return name.get();
   }
   
   public int getFloor() {
      return floor.get();
   }

    public boolean getTreatment() {
       return treatment.get();
    }
    
    public BooleanProperty treatmentProperty(){
    	return treatment;
    }

    public String getEmail() {
        return email.get();
    }
    public String getPersonalisedMessage(){
    	return personalisedMessage;
    }
    public void setID(int Id){
    	ID.set(Id);
    }
    public void setName(String name){
    	this.name.set(name);
    }
    public void setEmail(String emailAddress){
    	email.set(emailAddress);
    }
   
    public void setFloor(int floorNumber){
    	floor.set(floorNumber);
    }
    public void setTreatment(boolean treatment){
    	this.treatment.set(treatment);
    }
    public void setPersonalisedMessage(String personalisedMessage){
    	this.personalisedMessage = personalisedMessage;
    }

	@Override
	public String toString() {
		return "Employee [name= " + name.get() + ", floor= " + floor.get() + ", treatment= "
				+ treatment.get() + ", email= " + email.get() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((floor == null) ? 0 : floor.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((treatment == null) ? 0 : treatment.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (floor == null) {
			if (other.floor != null)
				return false;
		} else if (!floor.equals(other.floor))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (treatment == null) {
			if (other.treatment != null)
				return false;
		} else if (!treatment.equals(other.treatment))
			return false;
		return true;
	}
	
    
    }