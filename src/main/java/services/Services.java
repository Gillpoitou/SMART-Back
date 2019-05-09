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
import converter.PersonConverter;
import dao.AlgoParametersDAO;
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
import modele.AlgoParameters;
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

    public static String getBusMapDisplay() {

        return "GET_BUS_MAP_DISPLAY";
    }

    public static boolean postAlgoParameters(MongoClient mongoClient, AlgoParameters aP) {
        try {
            AlgoParametersDAO aPDAO = new AlgoParametersDAO(mongoClient);
            aPDAO.createAlgoParameters(aP);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public static boolean postBusRequest(MongoClient mongoClient, Person person, int personCounter, Date lastRequestDate) {

        try {
            PersonDAO personDAO = new PersonDAO(mongoClient);
            BusStopDAO busStopDAO = new BusStopDAO(mongoClient);
            AlgoParametersDAO algoParametersDAO = new AlgoParametersDAO(mongoClient);

            BusStop departure = person.getDeparture();
            BusStop arrival = person.getArrival();

            departure.setNbPersonsWaiting(departure.getNbPersonsWaiting() + 1);
            arrival.setNbPersonsComing(arrival.getNbPersonsComing() + 1);

            busStopDAO.updateBusStop(departure);
            busStopDAO.updateBusStop(arrival);

            Person pers = personDAO.createPerson(person);

            Date currentDate = new Date();

            AlgoParameters algoParameters = algoParametersDAO.getParameters();
            int maxRequestNb = algoParameters.getMaxRequestNb();
            long maxTimeInterval = algoParameters.getMaxTimeInterval();
            //System.out.println("Current requestNumber : " + personCounter + 1);
//            System.out.println("MAxRequest Number : " + maxRequestNb);
//            System.out.println("Max time interval : " + maxTimeInterval);

            if ((personCounter + 1) % maxRequestNb == 0 || currentDate.getTime() >= lastRequestDate.getTime() + maxTimeInterval) {
                //System.out.println("Algo calcul");
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
//            Date maxCurrentDate = new Date();
            Date[] busDates = new Date[buses.size()];

            LineDAO lineDAO = new LineDAO(mongoClient);
            Bus[] busesArray = new Bus[buses.size()];
            for (int k = 0; k < buses.size(); k++) {
                Bus currentBus = buses.get(k);
                Line currentLine = lineDAO.retrieveLineByBusId(currentBus.getId());
                busDates[k] = currentDate;

                if (currentLine != null) {
                    BusStop position;
                    for (BusStopLine bsl : currentLine.getBusStops()) {
                        if (bsl.getTime().after(currentDate)) {
                            position = bsl.getBusStop();
                            currentBus.setPosition(position);

                            busDates[k] = bsl.getTime();
//                            if (bsl.getTime().after(maxCurrentDate)) {
//                                maxCurrentDate = bsl.getTime();
//                            }
                            break;
                        }
                    }
                }
                busesArray[k] = currentBus;
            }

            /*for (int i = 0; i < busesArray.length; i++) {
                System.out.println(BusConverter.toDocument(busesArray[i]));
            }

            for (Person person : persons) {
                System.out.println(PersonConverter.toDocument(person));
            }

            System.out.println(maxCurrentDate.toString());
            System.out.println("Before algo");*/
            //call algo
            ArrayList<Line> lines = Algorithm.calculateLines(durations, busesArray, persons, busDates);

            //System.out.println("After algo");

            if (lines != null && lineDAO.retrieveAll().size() > 0 && lines.size() > 0) {
                //System.out.println("lines deleted");
                lineDAO.deleteAll();
            }
            for (Line l : lines) {
                //System.out.println(l.toString());
                lineDAO.createLine(l);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean initDataBase(MongoClient mongoClient) {
        //System.out.println("stating initialization");
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

        //System.out.println("Sending HTTP request");
        String url = "https://api.openrouteservice.org/v2/directions/driving-car?api_key=5b3ce3597851110001cf6248f04cc86b213d468795dd64628a835cab"
                + "&start=" + latA + "," + longA + "&end=" + latB + "," + longB;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        //System.out.println("\nSending 'GET' request to URL : " + url);
        //System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        //System.out.println(response.toString());

        JsonElement jelement = new JsonParser().parse(response.toString());
        JsonObject racine = jelement.getAsJsonObject();

        return racine;
    }

    public static boolean postBusProgress(MongoClient mongoClient, JsonObject result) {
        try {

            BusDAO busDAO = new BusDAO(mongoClient);
            PersonDAO personDAO = new PersonDAO(mongoClient);
            LineDAO lineDAO = new LineDAO(mongoClient);
            BusStopDAO busStopDAO = new BusStopDAO(mongoClient);

            LinkedList<Line> lineList = lineDAO.retrieveAll();
            if (lineList.size() > 0) {
                Bus firstBus = busDAO.getBusById(lineList.get(0).getBus().getId());
                Date precedTime = firstBus.getLastModif();
                Date now = new Date();

                for (int z = 0; z<lineList.size(); z++) {
                    //System.out.println(lineList.get(z).getName());
                    ArrayList<Integer> toRemoveIndexes = new ArrayList<Integer>();

                    Bus bus = busDAO.getBusById(lineList.get(z).getBus().getId());
                    for (int k = 0; k < lineList.get(z).getBusStops().size(); k++) {
                        //System.out.println("BusStopId : " + lineList.get(z).getBusStops().get(k).getBusStop().getBusStopID());

                        if (lineList.get(z).getBusStops().get(k).getTime().getTime() >= now.getTime()) {
                            //System.out.println("---------- 1");

                            break;
                        } else if (lineList.get(z).getBusStops().get(k).getTime().getTime() >= precedTime.getTime()) {
                            //System.out.println("---------- 2");
                            //System.out.println("busStopLine time : " + lineList.get(z).getBusStops().get(k).getTime().toString());
                            //System.out.println("Preced time : " + precedTime.toString());

                            //System.out.println("BusStopLine : " + lineList.get(z).getBusStops().get(k).toString());
//                            System.out.println(busStopLine.getGetOnPersons().size());

                            // Update bus position
                            bus.setPosition(lineList.get(z).getBusStops().get(k).getBusStop());
                            //System.out.println("position updated");

                            // Update passengers position
                            for (Person person : bus.getPassengers()) {
                                //System.out.println("passenger");
                                Person completePerson = personDAO.getPersonById(person.getId());
                                //System.out.println("passenger 2");
                                completePerson.setDeparture(lineList.get(z).getBusStops().get(k).getBusStop());
                                //System.out.println("passenger 3");
                                completePerson.setTimeDeparture(lineList.get(z).getBusStops().get(k).getTime());
                                //System.out.println("passenger 4");
                                personDAO.updatePerson(completePerson);
                                //System.out.println("passenger updated");

                            }

                            // New passengers get on the bus
                            for (int i = 0; i < lineList.get(z).getBusStops().get(k).getGetOnPersons().size(); i++) {
                                //System.out.println("i : " + i);
                                //System.out.println(lineList.get(z).getBusStops().get(k).getGetOnPersons().get(i));
                                //System.out.println("******");
                                bus.addPassenger(lineList.get(z).getBusStops().get(k).getGetOnPersons().get(i));
                                //System.out.println("======");
                                bus.setNbPassengers(bus.getNbPassengers() + 1);
                                busStopDAO.decrementPersonsWaiting(lineList.get(z).getBusStops().get(k).getBusStop().getId());
                                //System.out.println("add 1 passenger");
                            }

                            // Passenger get off if it is their stop
                            ArrayList<Integer> indexes = new ArrayList<Integer>();
                            //System.out.println("passengers size : " + bus.getPassengers().size());
                            for (int j = 0; j < bus.getPassengers().size(); j++) {
                                //System.out.println("busPassengers : " + j);
                                if (bus.getPassengers().get(j).getArrival().getBusStopID() == bus.getPosition().getBusStopID()) {
                                    bus.setNbPassengers(bus.getNbPassengers() - 1);
                                    indexes.add(new Integer(j));
                                }
                            }

                            for (Integer i : indexes) {
                                //System.out.println("Removing passenger 1 : " + i.intValue());
                                personDAO.deletePerson(bus.getPassengers().get(i.intValue()));
                                //System.out.println("Removing passenger 2");
                                bus.removePassenger(bus.getPassengers().get(i.intValue()));
                               // System.out.println("passenger removed");
                            }

                            toRemoveIndexes.add(k);
                            //System.out.println("------ busLineindex to remove added :" + k);
                        }
                    }

                    boolean removeLine = false;

                    for (Integer i : toRemoveIndexes) {
                        //System.out.println("Removing busStopLine 1");
                        if (i.intValue() == 0 && lineList.get(z).getBusStops().size() > 1) {
                            lineList.get(z).setDeparture(lineList.get(z).getBusStops().get(i.intValue()).getBusStop());
                        } else if (i.intValue() == 0 && lineList.get(z).getBusStops().size() == 1) {
                            removeLine = true;
                        }
                        lineList.get(z).removeBusStopLine(lineList.get(z).getBusStops().get(i.intValue()));
                        //System.out.println("BusStopLine removed : " + i.intValue());
                    }

                    if (!removeLine) {
                        lineDAO.updateLine(lineList.get(z));
                        //System.out.println("Line updated");
                    } else {
                        lineDAO.deleteLine(lineList.get(z));
                        //System.out.println("Line deleted");
                    }

                    bus.setLastModif(now);
                    //System.out.println("lastModif updated");
                    busDAO.updateBus(bus);
                    //System.out.println("Bus updated");
                }
            }
            getBusLines(mongoClient, result);
            getBusStops(mongoClient, result);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void test(MongoClient mongoClient) {

        callAlgoCalculation(mongoClient);
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
            LineDAO lineDAO = new LineDAO(mongoClient);

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
            lineDAO.deleteAll();

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

    public static boolean stopSimulation(Thread t) {
        t.interrupt();
        t.stop();
        return true;
    }
}
