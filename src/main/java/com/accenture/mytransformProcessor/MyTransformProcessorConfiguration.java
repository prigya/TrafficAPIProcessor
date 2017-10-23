package com.accenture.mytransformProcessor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.integration.annotation.Transformer;



@EnableBinding(Processor.class)
@EnableConfigurationProperties(MyTransformProcessorProperties.class)
public class MyTransformProcessorConfiguration {
	
	@Autowired
	private MyTransformProcessorProperties properties;
	
	@Transformer(inputChannel = Processor.INPUT, outputChannel = Processor.OUTPUT)
	
	
	public String processPayload(String payload) throws Exception {
		String url = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins="+this.properties.origins+"&destinations="+this.properties.destinations+"&departure_time=now&traffic_model=best_guess&mode=driving&key=AIzaSyDxM4ZhGncwqAgNiKnSUO_CcxytBBpO1sE";
				
				//https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=18.595416,73.770809&destinations=18.578769, 73.737213&departure_time=1552142340000&traffic_model=pessimistic&key=AIzaSyDxM4ZhGncwqAgNiKnSUO_CcxytBBpO1sE
				URL obj = new URL(url);
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();

				// optional default is GET
				con.setRequestMethod("GET");
				

				//add request header
				//con.setRequestProperty("User-Agent", "java call");
				

				int responseCode = con.getResponseCode();
				System.out.println("\nSending 'GET' request to URL : " + url);
				System.out.println("Response Code : " + responseCode);

				BufferedReader in = new BufferedReader(
				        new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				
				//trying for json stuff here 
				JSONObject msgObj = new JSONObject(response.toString());
				
				//getting date in IST 
				Date dt = new Date();
				DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata")); // Or whatever IST is supposed to be
				String dateinIST = formatter.format(dt);
						
				String message = "{" +
		                "   \"origin\":"+ msgObj.getString("origin_addresses").toString().replace("[","").replace("]","") + "," +
		                "   \"destination\":" + msgObj.getString("destination_addresses").toString().replace("[","").replace("]","") +"," +
		                "   \"duration\":\""+ msgObj.getJSONArray("rows")
		                .getJSONObject(0)
		                .getJSONArray ("elements")
		                .getJSONObject(0)
		                .getJSONObject("duration").getString("value") +"\","+
		                "   \"durationInTraffic\":\""+ msgObj.getJSONArray("rows")
		                .getJSONObject(0)
		                .getJSONArray ("elements")
		                .getJSONObject(0)
		                .getJSONObject("duration_in_traffic").getString("value") +"\","+
		                "   \"distance\":\"" + msgObj.getJSONArray("rows")
		                .getJSONObject(0)
		                .getJSONArray ("elements")
		                .getJSONObject(0)
		                .getJSONObject("distance").getString("text")+"\","+
		                "   \"username\":\""+this.properties.userName.toLowerCase()+"\","+
			            "   \"travellingto\":\""+this.properties.travellingTo.toLowerCase()+"\","+
			            "   \"systemtime\":\""+dateinIST+
		                "\"}";
				
				/*System.out.println("origin: " + msgObj.getString("origin_addresses") +
		             ", destination: " + msgObj.getString("destination_addresses"));*/

				//print result
				System.out.println("\n ********"+message+"********\n");
				//return () -> MessageBuilder.withPayload(response.toString()).build();
				
				return message;
	}
}





