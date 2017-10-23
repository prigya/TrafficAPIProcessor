package com.accenture.mytransformProcessor;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("mytransformProcessor")
public class MyTransformProcessorProperties {
	
	 public String origins;

	    public String getOrigins() {
	        return origins;
	    }

	    public void setOrigins(String origins) {
	        this.origins = origins;
	    }
	    
	    public String destinations;

	    public String getDestinations() {
	        return destinations;
	    }

	    public void setDestinations(String destinations) {
	        this.destinations = destinations;
	    }
	    
	    public String userName;
		   public String travellingTo; // only two allowed entries "home", "office"

		       public String getTravellingTo() {
		    	   return travellingTo;
		       }

		       public void setTravellingTo(String travellingTo) {
		    	   this.travellingTo = travellingTo;
		       }

			public String getuserName() {
		           return userName;
		       }

		       public void setuserName(String userName) {
		           this.userName = userName;
		       }

}