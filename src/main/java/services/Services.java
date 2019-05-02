/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package services;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoClient;
import dao.BusStopDAO;
import dao.PersonDAO;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;
import static javax.ws.rs.core.HttpHeaders.USER_AGENT;
import modele.BusStop;
import modele.Person;


/**
 *
 * @author etien
 */
public class Services {
    
    public static String getBusMapDisplay(){
        
        return "GET_BUS_MAP_DISPLAY";
    }
    
    public static boolean postBusRequest(MongoClient mongoClient,Person person){
        
        try{
            PersonDAO personDAO = new PersonDAO(mongoClient);
            personDAO.createPerson(person);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    
    public static boolean initDataBase(MongoClient mongoClient){
        System.out.println("stating initialization");
        try{
            BusStopDAO busStopDAO = new BusStopDAO(mongoClient);
            Vector<BusStop> busStops = busStopDAO.selectBusStops(4.863718173086466,45.7708809489496);
            float duration = getOSRMdistanceDuration(4.833944353746921,45.74349532570069,4.852895527422981,45.709186530065985);
            System.out.println(duration);
            
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    // HTTP GET request
    private static float getOSRMdistanceDuration(double latA, double longA, double latB, double longB) throws Exception {

                System.out.println("Sending HTTP request");
		String url = "http://router.project-osrm.org/route/v1/driving/"+latA+","+longA+";" +latB+","+longB+"?overview=false";
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

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

		//print result
		System.out.println(response.toString());
                
                JsonElement jelement = new JsonParser().parse(response.toString());
                JsonObject  racine = jelement.getAsJsonObject();
                JsonArray route = racine.getAsJsonArray("routes");
                
                return route.get(0).getAsJsonObject().get("duration").getAsFloat();
	}
}

