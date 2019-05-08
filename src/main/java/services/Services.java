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
import converter.BusConverter;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import static javax.ws.rs.core.HttpHeaders.USER_AGENT;
import modele.Bus;
import modele.BusStop;
import modele.BusStopLine;
import modele.BusStopPath;
import modele.Line;
import modele.Person;
import modele.Simulation;
import modele.SimulationRatio;

/**
 *
 * @author etien
 */
public class Services {

    public static boolean postBusRequest(MongoClient mongoClient, Person person, int personCounter, Date lastRequestDate, int maxRequestNb, long maxTimeInterval) {

        try {
            PersonDAO personDAO = new PersonDAO(mongoClient);
            BusStopDAO busStopDAO = new BusStopDAO(mongoClient);

            BusStop departure = person.getDeparture();
            BusStop arrival = person.getArrival();

            departure.setNbPersonsWaiting(departure.getNbPersonsWaiting() + 1);
            arrival.setNbPersonsComing(arrival.getNbPersonsComing() + 1);

            busStopDAO.updateBusStop(departure);
            busStopDAO.updateBusStop(arrival);

            Person pers = personDAO.createPerson(person);
            Date currentDate = new Date();
            if (personCounter + 1 >= maxRequestNb || currentDate.getTime() >= lastRequestDate.getTime() + maxTimeInterval) {
                if (!callAlgoCalculation(mongoClient)) {
                    return false;
                }
            }

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
                        double result;
                        result = busStops.get(i).getDurationToTarget(j);
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
            Date maxCurrentDate = new Date();

            LineDAO lineDAO = new LineDAO(mongoClient);
            Bus[] busesArray = new Bus[buses.size()];
            for (int k = 0; k < buses.size(); k++) {
                Bus currentBus = buses.get(k);
                Line currentLine = lineDAO.retrieveLineByBusId(currentBus.getId());

                if (currentLine != null) {
                    BusStop position;
                    for (BusStopLine bsl : currentLine.getBusStops()) {
                        if (bsl.getTime().after(currentDate)) {
                            position = bsl.getBusStop();

                            currentBus.setPosition(position);

                            if (bsl.getTime().after(maxCurrentDate)) {
                                maxCurrentDate = bsl.getTime();
                            }
                            break;
                        }
                    }
                }
                busesArray[k] = currentBus;
            }

            //call algo
            ArrayList<Line> lines = Algorithm.calculateLines(durations, busesArray, persons, maxCurrentDate);

            lineDAO.deleteAll();
            for (Line l : lines) {
                System.out.println(l.toString());
                lineDAO.createLine(l);
            }

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

    public static boolean postBusProgress(MongoClient mongoClient, JsonObject result) {
        try {

            BusDAO busDAO = new BusDAO(mongoClient);
            PersonDAO personDAO = new PersonDAO(mongoClient);
            LineDAO lineDAO = new LineDAO(mongoClient);

            LinkedList<Line> lineList = lineDAO.retrieveAll();

            Bus firstBus = busDAO.getBusById(lineList.get(0).getBus().getId());
            Date precedTime = firstBus.getLastModif();
            Date now = new Date();

            for (Line line : lineList) {
                Bus bus = busDAO.getBusById(line.getBus().getId());
                for (BusStopLine busStopLine : line.getBusStops()) {

                    if (busStopLine.getTime().getTime() >= now.getTime()) {
                        // Update bus position
                        bus.setPosition(busStopLine.getBusStop());

                        // Update passengers position
                        for (Person person : bus.getPassengers()) {
                            Person completePerson = personDAO.getPersonById(person.getId());
                            completePerson.setDeparture(busStopLine.getBusStop());
                            completePerson.setTimeDeparture(now);
                            personDAO.updatePerson(completePerson);
                        }
                        break;
                    } else if (busStopLine.getTime().getTime() >= precedTime.getTime()) {
                        // New passengers get on the bus
                        for (Person person : busStopLine.getGetOnPersons()) {
                            bus.addPassenger(person);
                        }
                        // Passenger get off if it is their stop
                        for (Person person : bus.getPassengers()) {
                            bus.removePassenger(person);
                            personDAO.deletePerson(person);
                        }
                        line.removeBusStopLine(busStopLine);
                        lineDAO.updateLine(line);
                    }
                }
                bus.setLastModif(now);
                busDAO.updateBus(bus);
            }

            getBusLines(mongoClient, result);
            getBusStops(mongoClient, result);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void test(MongoClient mongoClient) {

//        callAlgoCalculation(mongoClient);
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

    public static boolean createBus(MongoClient mongoClient, String data) {
        BusDAO busDAO = new BusDAO(mongoClient);
        BusStopDAO busStopDA0 = new BusStopDAO(mongoClient);

        Bus bus = BusConverter.jsonToBus(data);

        BusStop position = busStopDA0.getBusStopByName("Charpennes");
        bus.setPosition(position);

        busDAO.createBus(bus);

        return true;
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

    public static boolean getBusLines(MongoClient mongoClient, JsonObject result) {
        try {

            JsonArray linesArray = new JsonArray();
            LineDAO lineDAO = new LineDAO(mongoClient);
            BusStopDAO busStopDAO = new BusStopDAO(mongoClient);
            BusDAO busDAO = new BusDAO(mongoClient);

            LinkedList<Line> lines = lineDAO.retrieveAll();

            BusStop currentBusStop;
            BusStop nextBusStop;
            BusStop currentBusStopComplete;
            Bus currentBus;

            for (Line line : lines) {
//                System.out.println(line.getBusStops().size());
                currentBus = busDAO.getBusById(line.getBus().getId());
                currentBusStop = currentBus.getPosition();

                for (int i = 0; i < line.getBusStops().size(); i++) {
                    nextBusStop = line.getBusStops().get(i).getBusStop();

//                    System.out.println(i + "  current : " + currentBusStop.getBusStopID() + "   " + currentBusStop.getName());
//                    System.out.println("next : " + nextBusStop.getBusStopID() + "   " + nextBusStop.getName());
                    currentBusStopComplete = busStopDAO.getBusStopById(currentBusStop.getId());

                    int index = nextBusStop.getBusStopID();

                    if (currentBusStop.getBusStopID() < nextBusStop.getBusStopID()) {
                        index--;
                    }

                    currentBusStop.addBusStopPath(currentBusStopComplete.getPaths().get(index));

                    if (i == 0) {
                        line.setDeparture(currentBusStop);
                    } else {
                        line.getBusStops().get(i - 1).setBusStop(currentBusStop);
                    }

                    currentBusStop = nextBusStop;
                }
                line.setBus(currentBus);
                linesArray.add(LineConverter.LineToJson(line));
            }
            result.add("lines", linesArray);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean initDBValues(MongoClient mongoClient) {
        //Remet à 0 les passengers des bus, leur position, et la last modif à heure courrante
        //Remet à 0 les personsWaiting et coming à chaque arrêt de bus
        //Supprime toutes les persons

        try {
            BusDAO busDAO = new BusDAO(mongoClient);
            BusStopDAO busStopDAO = new BusStopDAO(mongoClient);
            PersonDAO personDAO = new PersonDAO(mongoClient);

            ArrayList<Bus> buses = busDAO.selectAllBus();

            Date currentDate = new Date();

            for (Bus bus : buses) {
                bus.setLastModif(currentDate);
                bus.setNbPassengers(0);
                bus.setPassengers(new ArrayList<Person>());
                bus.setPosition(busStopDAO.getBusStopByName("Charpennes"));
                bus = busDAO.updateBus(bus);
            }

            Vector<BusStop> busStops = busStopDAO.selectBusStops();
            for (BusStop busStop : busStops) {
                busStopDAO.resetPersons(busStop);
            }

            personDAO.deleteAllPersons();
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean startSimulation(JsonObject request, ServletContext context) {
        JsonArray busStopRatio = request.getAsJsonArray("busStopRatio");
        int numberTravelers = request.get("number").getAsInt();
        ArrayList<SimulationRatio> ratioArray = new ArrayList();

        for (JsonElement jel : busStopRatio) {
            JsonObject jobj = jel.getAsJsonObject();
            SimulationRatio currentRatio = new SimulationRatio(jobj.get("busStop").getAsString(), jobj.get("frequency").getAsDouble());
            ratioArray.add(currentRatio);
        }

        Simulation simulation = new Simulation();
        simulation.setBusStopsRatios(ratioArray);
        simulation.setNumberTravelers(numberTravelers);

        CalculSimulationThread thread = new CalculSimulationThread(simulation);
        context.setAttribute("SIMULATION_THREAD", thread);
        thread.start();
        return true;
    }
}
