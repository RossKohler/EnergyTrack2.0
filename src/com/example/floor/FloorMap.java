package com.example.floor;
import java.util.Vector;

public class FloorMap{
	
	public static Vector<String> ordinals(){
		String[] sufixes = new String[] {"th","st","nd","rd","th","th","th","th","th","th"};
		Vector<String> ordinals = new Vector<String>();
		String ordinal = "";
		for(int i=1;i<=22;i++){
		
			switch(i%100){
				case 11:
				case 12:
				case 13:
					ordinal = i + "th";
					break;
				default:
					ordinal = i + sufixes[i%10];
		
			}
		ordinals.add(ordinal);
		
		
	}
	
	return ordinals;
	
	
	
}
	

	public static String getOrdinal(int i) {
	    String[] sufixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
	    switch (i % 100) {
	    case 11:
	    case 12:
	    case 13:
	        return i + "th";
	    default:
	        return i + sufixes[i % 10];

	    }
	}

	public static int extractDigits(String ordinal){
		StringBuilder builder = new StringBuilder();
	    for (int i = 0; i < ordinal.length(); i++) {
	        char c = ordinal.charAt(i);
	        if (Character.isDigit(c)) {
	            builder.append(c);
	        }
	    }
	    return Integer.parseInt(builder.toString());
		
		
	}


}









