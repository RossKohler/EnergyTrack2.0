package com.example.employee;
import java.text.DecimalFormat;

	
public class Employee{
   
   private int ID;
   private String name; 
   private int floor;
   private boolean treatment;
   private String email;
   
   public String personalisedMessage;
   
   DecimalFormat twoDForm = new DecimalFormat("#.##");

   public Integer getID() {
      return ID;
   }

   public String getName() {
      return name;
   }
   
   public int getFloor() {
      return floor;
   }

    public boolean getTreatment() {
       return treatment;
    }
    
    public boolean treatmentProperty(){
    	return treatment;
    }

    public String getEmail() {
        return email;
    }
    public String getPersonalisedMessage(){
    	return personalisedMessage;
    }
    public void setID(int Id){
    	this.ID = Id;
    }
    public void setName(String name){
    	this.name = name;
    }
    public void setEmail(String emailAddress){
    	email = emailAddress;
    }
   
    public void setFloor(int floorNumber){
    	floor = floorNumber;
    }
    public void setTreatment(boolean treatment){
    	this.treatment = treatment;
    }
    public void setPersonalisedMessage(String personalisedMessage){
    	this.personalisedMessage = personalisedMessage;
    }

	@Override
	public String toString() {
		return "Employee [name= " + name + ", floor= " + floor + ", treatment= "
				+ treatment + ", email= " + email+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ID;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + floor;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((personalisedMessage == null) ? 0 : personalisedMessage.hashCode());
		result = prime * result + (treatment ? 1231 : 1237);
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
		if (ID != other.ID)
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (floor != other.floor)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (personalisedMessage == null) {
			if (other.personalisedMessage != null)
				return false;
		} else if (!personalisedMessage.equals(other.personalisedMessage))
			return false;
		if (treatment != other.treatment)
			return false;
		return true;
	}
	
	
    
    }