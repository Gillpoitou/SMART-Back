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
import converter.BusStopConverter;
import converter.LineConverter;
import dao.BusDAO;
import dao.BusStopDAO;
import dao.LineDAO;
import dao.PersonDAO;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletResponse;
import static javax.ws.rs.core.HttpHeaders.USER_AGENT;
import modele.Bus;
import modele.BusStop;
import modele.BusStopPath;
import modele.Line;
import modele.Person;

/**
 *
 * @author etien
 */
public class Services {

    public static String getBusMapDisplay() {

        return "GET_BUS_MAP_DISPLAY";
    }

    public static boolean postBusRequest(MongoClient mongoClient, Person person, int personCounter, Date lastRequestDate, int maxRequestNb, long maxTimeInterval) {
        
        try {
            PersonDAO personDAO = new PersonDAO(mongoClient);
            BusStopDAO busStopDAO = new BusStopDAO(mongoClient);
            
            BusStop departure = person.getDeparture();
            BusStop arrival = person.getArrival();
            
            departure.setNbPersonsWaiting(departure.getNbPersonsWaiting()+1);
            arrival.setNbPersonsComing(arrival.getNbPersonsComing()+1);
            
            busStopDAO.updateBusStop(departure);
            busStopDAO.updateBusStop(arrival);
           
           Person pers = personDAO.createPerson(person);
           
            System.out.println(pers);
            System.out.println(pers.getId());
            Date currentDate = new Date();
           /* if (personCounter + 1 >= maxRequestNb || currentDate.getTime() >= lastRequestDate.getTime() + maxTimeInterval) {
                if (!callAlgoCalculation(mongoClient)) {
                    return false;
                }
            }*/

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean callAlgoCalculation(MongoClient mongoClient) {

        try {
            BusStopDAO bsDAO = new BusStopDAO(mongoClient);
            Vector<BusStop> busStops = bsDAO.selectBusStops();
            double[][] durations = new double[busStops.size()][busStops.size()];
            for (int i = 0; i < busStops.size(); i++) {
                for (int j = 0; j < busStops.size(); j++) {
                    if (i == j) {
                        durations[i][j] = 0;
                    } else {

                        double result = busStops.get(i).getDurationToTarget(j);
                        if (result == -1) {
                            //DB error, target bus not found
                            return false;
                        }
                        durations[i][j] = result;
                    }
                }
            }
            BusDAO busDAO = new BusDAO(mongoClient);
            ArrayList<Bus> buses = busDAO.selectAllBus();
            PersonDAO personDAO = new PersonDAO(mongoClient);
            ArrayList<Person> persons = personDAO.selectAllPersons();
            Date currentDate = new Date(); //create current date time
            
            //call algo
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean initDataBase(MongoClient mongoClient) {
        System.out.println("stating initialization");
        try {
            BusStopDAO busStopDAO = new BusStopDAO(mongoClient);
            Vector<BusStop> busStops = busStopDAO.selectBusStopsGeoJson(4.863718173086466, 45.7708809489496);
            for (int i = 0; i < busStops.size(); i++) {
                busStopDAO.createBusStop(busStops.get(i));

            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean initDBTravel(MongoClient mongoClient) {
        try {
            BusStopDAO busStopDAO = new BusStopDAO(mongoClient);
            Vector<BusStop> busStops = busStopDAO.selectBusStops();
            for (int i = 0; i < busStops.size(); i++) {
                TimeUnit.SECONDS.sleep(68);
                for (int j = 0; j < busStops.size(); j++) {
                    //Don't make travels between same point i== J TODOOOO
                    if (j == i) {
                        continue;
                    }
                    JsonObject APIresult = getAPITravel(busStops.get(i).getLongitude(), busStops.get(i).getLatitude(), busStops.get(j).getLongitude(), busStops.get(j).getLatitude());
                    BusStop updatedBusStop = BusStopConverter.UpdateBusStopFromJson(busStops.get(i), busStops.get(j), APIresult);
                    //JsonObject test = BusStopConverter.BusStopToJson(updatedBusStop);
                    //System.out.println(test.toString());
                    busStopDAO.updateBusStop(updatedBusStop);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // HTTP GET request
    private static JsonObject getAPITravel(double latA, double longA, double latB, double longB) throws Exception {

        System.out.println("Sending HTTP request");
        String url = "https://api.openrouteservice.org/v2/directions/driving-car?api_key=5b3ce3597851110001cf6248f04cc86b213d468795dd64628a835cab"
                + "&start=" + latA + "," + longA + "&end=" + latB + "," + longB;

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
        JsonObject racine = jelement.getAsJsonObject();

        return racine;
    }

    public static boolean getBusLines(MongoClient mongoClient, JsonObject result) {

        try {
            LineDAO lineDAO = new LineDAO(mongoClient);
            //TODO match with BD DAO
            List<Line> lines = new ArrayList();
            JsonArray linesJson = new JsonArray();
            for (Line l : lines) {
                JsonObject line = LineConverter.LineToJson(l);
                linesJson.add(line);
            }
            result.add("lines", linesJson);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void test(MongoClient mongoClient) {
        
        
        
//        BusDAO busDAO = new BusDAO(mongoClient);
//        BusStopDAO busStopDAO = new BusStopDAO(mongoClient);
//
//        Bus bus = busDAO.getBusById("5ccbf7bcd67d4439b2320b45");
//        System.out.println(bus);
//        BusStop busStop = busStopDAO.getBusStopById("5ccb9085b5238e28b882a129");
//        busStop.setBusStopID(999);
//        busStop.setLatitude(12);
//        busStop.setLongitude(12);
//        busStop.setName("arrêt");
//        busStop.setNbPersonsComing(2);
//        busStop.setNbPersonsWaiting(5);
//        busStop.setPaths(new Vector<BusStopPath>());
//        Bus bus = new Bus();
//        bus.setName("test");
//        bus.setNbPassengers(10);
//        bus.setNbPlaces(20);
//        bus.setPosition(busStop);
//        
//        busDAO.createBus(bus);
    }

    public static boolean getBusStops(MongoClient mongoClient, JsonObject result) {
        try {
            JsonArray busStopsArray = new JsonArray();
            BusStopDAO busStopDAO = new BusStopDAO(mongoClient);
            Vector<BusStop> busStops = busStopDAO.selectBusStops();
            for (int i = 0; i < busStops.size(); i++) {
                busStopsArray.add(BusStopConverter.BusStopToJson(busStops.get(i)));
            }
            result.add("bus_stops", busStopsArray);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
