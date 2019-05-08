/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import modele.*;

/**
 *
 * @author Grgeoire Bailly
 */
public class Algorithm {

    private static double[][] durations;
    private static double pourcentage;
    private static Date[] currentDate;
    private static Bus[] buses;

    public static ArrayList<Line> calculateLines(double[][] journeyDurations, Bus[] aBuses, ArrayList<Person> requests, Date[] theCurrentDate) {
        System.out.println("ALGO");
        durations = journeyDurations;
        currentDate = theCurrentDate;
        buses = aBuses;
        pourcentage = 3;

        //call tabu who is calling greedy to initialise and then optimise the 
        //dispatchment of persons and the route of each line (optRoute)
        ArrayList<ArrayList<Person>> solution = tabuSearch(requests);

        ArrayList<Line> lines = createLines(solution);

        return lines;
        /*
        //Appeler greedy
        //Appeler taboueLine currentLine = new Line();
        //Créer les lignes avec BusStop
        percentage = 3;
        ArrayList<ArrayList<Person>> lines = greedyAlgo(buses, requests);
        
        optRoute(lines.get(0), 0);
        
        if(lines != null){
            ArrayList<Line> result = createLines(lines);
            return result;
        }
        return null;
        //Créer les lignes avec BusStop*/
    }

    public static ArrayList<ArrayList<Person>> greedyAlgo(Bus[] buses, ArrayList<Person> requests) {
        ArrayList<ArrayList<Person>> busLines = new ArrayList<>();
        ArrayList<Person> currentLine = new ArrayList<>();
        int lineNb = 0;
        busLines.add(currentLine);
        for (int k = 0; k < requests.size(); k++) {

            Person request = requests.get(k);
            //System.out.println(k);
            //System.out.println(request.getId());
            boolean feasible = false;
            while (feasible == false) {
                currentLine.add(request);
                currentLine.add(request);
                boolean endIsDeparture = true;
                for (int i = 0; i < currentLine.size() - 2; i++) {
                    int indexDepartureI = currentLine.indexOf(currentLine.get(i));
                    int indexArrivalI = currentLine.lastIndexOf(currentLine.get(i));

                    int durationK = (int) (durations[requests.get(k).getDeparture().getBusStopID()][requests.get(k).getArrival().getBusStopID()] * pourcentage);
                    Date dateArrivalK = new Date(requests.get(k).getTimeDeparture().getTime() + durationK * 1000);
                    int durationI = (int) (durations[currentLine.get(i).getDeparture().getBusStopID()][currentLine.get(i).getArrival().getBusStopID()] * pourcentage);
                    Date dateArrivalI = new Date(currentLine.get(i).getTimeDeparture().getTime() + durationI * 1000);

                    if (i == indexDepartureI) {
                        if (request.getTimeDeparture().compareTo(currentLine.get(i).getTimeDeparture()) <= 0 && endIsDeparture) {
                            currentLine.add(indexDepartureI, request);
                            currentLine.remove(currentLine.size() - 1);
                            endIsDeparture = false;
                        }
                        if (dateArrivalK.compareTo(currentLine.get(i).getTimeDeparture()) <= 0) {
                            currentLine.add(indexDepartureI, request);
                            currentLine.remove(currentLine.size() - 1);
                            break;
                        }
                    } else {
                        if (request.getTimeDeparture().compareTo(dateArrivalI) <= 0 && endIsDeparture) {
                            currentLine.add(indexArrivalI, request);
                            currentLine.remove(currentLine.size() - 1);
                            endIsDeparture = false;
                        }
                        if (dateArrivalK.compareTo(dateArrivalI) <= 0) {
                            currentLine.add(indexArrivalI, request);
                            currentLine.remove(currentLine.size() - 1);
                            break;
                        }
                    }
                }
                
                //System.out.println("nouvel essai");
                
                for(Person person: currentLine){
                    //System.out.println(person.getId());
                }

                if (feasibleLine(currentLine, busLines.indexOf(currentLine))) {
                    feasible = true;
                    currentLine = busLines.get(0);
                } else {
                    currentLine.remove(request);
                    currentLine.remove(request);

                    if (busLines.indexOf(currentLine) == lineNb) {
                        if (busLines.indexOf(currentLine) != buses.length - 1) {
                            ArrayList<Person> newLine = new ArrayList<>();
                            busLines.add(newLine);
                            currentLine = newLine;
                            lineNb++;
                        } else {
                            System.out.println("Pas assez de bus");
                            return null;
                        }
                    } else {
                        currentLine = busLines.get(busLines.indexOf(currentLine) + 1);
                    }
                }
            }
        }
/*
        for (ArrayList<Person> line : busLines) {
            System.out.println("Nouvelle ligne");
            for (Person person : line) {
                System.out.println(person.getId());
            }
        }*/

        return busLines;
    }

    public static boolean feasibleLines(ArrayList<ArrayList<Person>> lines) {
        int i = 0;
        for (ArrayList<Person> line : lines) {
            if (!feasibleLine(line, i)) {
                return false;
            }
            i++;
        }
        return true;
    }

    public static boolean feasibleLine(ArrayList<Person> line, int index) {
        int personNb = 0;
        int duration;

        Date precedDate = currentDate[index];
        Date arrival = currentDate[index];
        BusStop preced = buses[index].getPosition();

        for (int i = 0; i < line.size(); i++) {
            // Si c'est un départ
            if (i == line.indexOf(line.get(i))) {
                personNb++;
                if (!preced.getId().equals(line.get(i).getId())) {
                    duration = (int) durations[preced.getBusStopID()][line.get(i).getArrival().getBusStopID()];
                    arrival = new Date(precedDate.getTime() + duration * 1000);
                    precedDate = arrival;
                    preced = line.get(i).getDeparture();
                }
                if (arrival.compareTo(line.get(i).getTimeDeparture()) < 0 && precedDate.compareTo(line.get(i).getTimeDeparture()) < 0) {
                    precedDate = line.get(i).getTimeDeparture();
                }
                // Si c'est une arrivée
            } else {
                personNb--;
                if (!preced.getId().equals(line.get(i).getId())) {
                    duration = (int) durations[preced.getBusStopID()][line.get(i).getArrival().getBusStopID()];
                    arrival = new Date(precedDate.getTime() + duration * 1000);
                    preced = line.get(i).getArrival();
                }

                int durationMin = (int) ((durations[line.get(i).getDeparture().getBusStopID()][line.get(i).getArrival().getBusStopID()] * 1000) * pourcentage);
                Date maxDate = new Date(line.get(i).getTimeDeparture().getTime() + durationMin);
                if (arrival.compareTo(maxDate) > 0) {
                    return false;
                }
                if (arrival.compareTo(precedDate) > 0) {
                    precedDate = arrival;
                }
            }

            if (personNb > buses[index].getNbPlaces()) {
                return false;
            }
        }
        return true;
    }

    public static ArrayList<Line> createLines(ArrayList<ArrayList<Person>> lines) {
        System.out.println("Calcul Lines");
        ArrayList<Line> result = new ArrayList<>();
        int duration;
        Date theCurrentDate;

        for (int i = 0; i < lines.size(); i++) {
            ArrayList<Person> currentCalculatedLine = lines.get(i);
            theCurrentDate = currentDate[i];
            ArrayList<BusStopLine> currentLine = new ArrayList<>();
            
            
            //if empty line:
            if (currentCalculatedLine.isEmpty()){
                Line newLine = new Line("Line " + i, buses[i].getPosition(), null, currentLine, buses[i]);
                result.add(newLine);
                continue;
            }
            duration = (int) durations[buses[i].getPosition().getBusStopID()][currentCalculatedLine.get(0).getDeparture().getBusStopID()];
            BusStopLine currentBusStop = new BusStopLine(currentCalculatedLine.get(0).getDeparture(), 0, 0, new Date(theCurrentDate.getTime() + duration * 1000));
            currentBusStop.setNbGetOn(currentBusStop.getNbGetOn() + 1);
            currentLine.add(currentBusStop);

            Date arrivalDate = new Date(theCurrentDate.getTime() + duration * 1000);
            if (arrivalDate.compareTo(currentCalculatedLine.get(0).getTimeDeparture()) < 0) {
                theCurrentDate = currentCalculatedLine.get(0).getTimeDeparture();
            } else {
                theCurrentDate = arrivalDate;
            }

            for (int j = 1; j < currentCalculatedLine.size(); j++) {
                // Si c'est un départ
                if (j == currentCalculatedLine.indexOf(currentCalculatedLine.get(j))) {
                    if (!currentBusStop.getBusStop().getName().equals(currentCalculatedLine.get(j).getDeparture().getName())) {
                        System.out.println("currentDate depart "+ theCurrentDate);
                        duration = (int) durations[currentBusStop.getBusStop().getBusStopID()][currentCalculatedLine.get(j).getDeparture().getBusStopID()];
                        currentBusStop = new BusStopLine(currentCalculatedLine.get(j).getDeparture(), 0, 0, new Date(theCurrentDate.getTime() + duration * 1000));
                        currentLine.add(currentBusStop);
                        arrivalDate = new Date(theCurrentDate.getTime() + duration * 1000);
                    }

                    if (arrivalDate.compareTo(currentCalculatedLine.get(j).getTimeDeparture()) <= 0) {
                        if(theCurrentDate.compareTo(currentCalculatedLine.get(j).getTimeDeparture()) < 0){
                            theCurrentDate = currentCalculatedLine.get(j).getTimeDeparture();
                        }
                    } else if(arrivalDate.compareTo(theCurrentDate) > 0){
                        theCurrentDate = arrivalDate;
                    }
                    currentBusStop.setNbGetOn(currentBusStop.getNbGetOn() + 1);
                    currentBusStop.addGetOnPerson(currentCalculatedLine.get(j));
                    // Si c'est une arrivée
                } else {
                    if (!currentBusStop.getBusStop().getName().equals(currentCalculatedLine.get(j).getArrival().getName())) {
                        System.out.println("currentDate arrivee "+ theCurrentDate);
                        duration = (int) durations[currentBusStop.getBusStop().getBusStopID()][currentCalculatedLine.get(j).getArrival().getBusStopID()];
                        System.out.println("from "+currentBusStop.getBusStop().getBusStopID());
                        System.out.println("to " +currentCalculatedLine.get(j).getArrival().getBusStopID());
                        System.out.println(duration);
                        
                        currentBusStop = new BusStopLine(currentCalculatedLine.get(j).getArrival(), 0, 0, new Date(theCurrentDate.getTime() + duration * 1000));
                        currentLine.add(currentBusStop);
                        arrivalDate = new Date(theCurrentDate.getTime() + duration * 1000);
                    }
                    if (arrivalDate.compareTo(theCurrentDate) > 0) {
                        theCurrentDate = new Date(theCurrentDate.getTime() + duration * 1000);
                    }
                    currentBusStop.setNbGetOff(currentBusStop.getNbGetOff() + 1);
                }
            }
            Line newLine = new Line("Line " + i, buses[i].getPosition(), null, currentLine, buses[i]);
            result.add(newLine);
        }

        return result;
    }

    public static ArrayList<ArrayList<Person>> tabuSearch(ArrayList<Person> requests) {

        //Initialise sol with greedy and optRoutes
        ArrayList<ArrayList<Person>> sol = greedyAlgo(buses, requests);
        optRoutes(sol);
        ArrayList<Line> solLines = createLines(sol);

        //Careful: never change the person's attributes
        ArrayList<ArrayList<Person>> bestSol = createCopy(sol);
        ArrayList<ArrayList<Person>> neighbour;
        ArrayList<ArrayList<Person>> bestNeighbour = createCopy(sol);

        double currentCost = getCost(sol, solLines);
        double neighbourCost;
        double bestNeighbourCost;
        double bestSolCost = currentCost;

        //Generate tabu list as the last time a person was used in a move
        HashMap<String, Integer> tabuList = new HashMap<>();
        for (int i = 0; i < requests.size(); i++) {
            tabuList.put(requests.get(i).getId(), -1000);     //to free every move
        }
        int TABU_LENGTH = 4;

        int iter = 0;
        int bestLastUpdate = 0;
        int convergence = 40;
        int routesNb = sol.size();

        while (iter - bestLastUpdate < convergence) {
            iter++;
            //searching for the best OR a good neighbour
            //best among 10
            bestNeighbourCost = Double.MAX_VALUE;
            String bestNeighbourMoveId = "";
            for (int i = 0; i < 10; i++) {
                int randomPerson;
                int fromRoute;
                int toRoute;
                do {
                    do{
                        fromRoute = (int) (Math.random() * routesNb);
                    }while(sol.get(fromRoute).isEmpty());
                    
                    toRoute = (int) (Math.random() * (routesNb - 1));
                    if (toRoute >= fromRoute) {
                        toRoute++;
                    }

                    //get a random person from 'from', check it's not Tabu
                    randomPerson = (int) (Math.random() * sol.get(fromRoute).size());
                    //System.out.println("sol"+sol.get(fromRoute).size()+"    n"+neighbour.get(fromRoute).size());
                } while (iter - tabuList.get(sol.get(fromRoute).get(randomPerson).getId()) <= TABU_LENGTH); //if Tabu reset randoms
                //else put person in 'to'
                neighbour = createCopy(sol);
                Person p = neighbour.get(fromRoute).get(randomPerson);
                
                neighbour.get(fromRoute).remove(p);
                neighbour.get(fromRoute).remove(p);
                neighbour.get(toRoute).add(p);
                neighbour.get(toRoute).add(p);
                
                //opt the routes we changed
                neighbour.set(fromRoute, optRoute(neighbour.get(fromRoute), fromRoute));
                neighbour.set(toRoute, optRoute(neighbour.get(toRoute), toRoute));
                
                //checking the cost
                ArrayList<Line> neighbourLines = createLines(neighbour);
                neighbourCost = getCost(neighbour, neighbourLines);
                if (neighbourCost < bestNeighbourCost) {
                    bestNeighbour = createCopy(neighbour);
                    bestNeighbourCost = neighbourCost;
                    bestNeighbourMoveId = sol.get(fromRoute).get(randomPerson).getId();
                }
            }
            //move to bestNeighbour AND update tabuList of the request that has been moved
            tabuList.put(bestNeighbourMoveId, iter);
            sol = createCopy(bestNeighbour);
            currentCost = bestNeighbourCost;

            //update bestSol if needed
            if (currentCost < bestSolCost && feasibleLines(sol)) {
                bestSol = createCopy(sol);
                bestSolCost = currentCost;

                //to reset the 'get out' countdown
                bestLastUpdate = iter;
            }
        }
        return bestSol;
    }

    //TABU UTIL METHODS
    public static double getCost(ArrayList<ArrayList<Person>> journeys, ArrayList<Line> lines) {
        //System.out.println("ini "+journeys);
        //System.out.println(lines);
        double cost = 0;
        for (int i = 0; i < journeys.size(); i++) {
            ArrayList<Person> journey = journeys.get(i);
            ArrayList<BusStopLine> stops = lines.get(i).getBusStops();
            for (int j = 0; j < journey.size(); j++) {
                double realDuration;
                Date depDate = journey.get(j).getTimeDeparture();
                String DepId = journey.get(j).getDeparture().getId();
                String ArrId = journey.get(j).getArrival().getId();

                //search for the first stop of the line after the departure time
                //to avoid being confused by possibles loop of the line
                int k = 0;
                Date firstStopDate = stops.get(k).getTime();
                
                System.out.println(journey.get(j));
                System.out.println("dep "+ DepId+"  arr "+ArrId+"  date"+ depDate);
                System.out.println(journey);
                System.out.println(stops);
                
                System.out.println();
                while (depDate.after(firstStopDate)) {
                    k++;
                    firstStopDate = stops.get(k).getTime();
                }

                //need to check the previous one because firstStopDate is the date the bus arrive at a stop
                //not the date it leaves this stop: it might wait for someone
                //so we check that the bus hasn't left before the client departure
                if (k > 0) {
                    double travelTime = durations[stops.get(k - 1).getBusStop().getBusStopID()][stops.get(k).getBusStop().getBusStopID()];
                    double bus_leavingTime = stops.get(k).getTime().getTime() / 1000.0 - travelTime;
                    if (depDate.getTime() / 1000.0 <= bus_leavingTime) {
                        k--;
                    }
                }

                //search for the waiting time at the bus stop
                String CurStopId = stops.get(k).getBusStop().getId();
                while (!DepId.equals(CurStopId)) {
                    k++;
                    CurStopId = stops.get(k).getBusStop().getId();
                }
                //System.out.println("after dep k: "+k);

                //search for the time the bus reach the arrivalStop
                //System.out.println(stops);
                //System.out.println(DepId+"  "+ArrId + "  "+depDate);
                //System.out.println("feasible "+feasibleLines(journeys));
                while (!ArrId.equals(CurStopId)) {
                    k++;
                    CurStopId = stops.get(k).getBusStop().getId();
                }

                realDuration = (stops.get(k).getTime().getTime() - depDate.getTime()) / 1000;
                int departureId = journey.get(j).getDeparture().getBusStopID();
                int arrivalId = journey.get(j).getArrival().getBusStopID();

                double bestDuration = durations[departureId][arrivalId];

                double value = realDuration / bestDuration;

                //DEBUG
                /*
                System.out.println("realD: " + realDuration + "   bestD: " + bestDuration + "   Val: " + value);
                System.out.println(journey.get(j));
                System.out.println("dep "+ departureId+"  arr "+arrivalId+"  date"+ depDate);
                System.out.println(journey);
                System.out.println(stops);
                
                System.out.println();
                if (value < 1) System.exit(0);*/

                cost += value;
            }
        }
        return cost / 2;      //because we calculate twice for each person
    }

    public static ArrayList<ArrayList<Person>> createCopy(ArrayList<ArrayList<Person>> origin) {
        ArrayList<ArrayList<Person>> copy = new ArrayList<>();
        for (int i = 0; i < origin.size(); i++) {
            ArrayList<Person> part = new ArrayList<>();
            for (int j = 0; j < origin.get(i).size(); j++) {
                part.add(origin.get(i).get(j));
            }
            copy.add(part);
        }
        return copy;
    }

    public static void optRoutes(ArrayList<ArrayList<Person>> routes) {
        for (int i = 0; i < routes.size(); i++) {
            routes.set(i, optRoute(routes.get(i), i));
        }
    }

    public static ArrayList<Person> optRoute(ArrayList<Person> aRoute, int busNb) {
        LinkedList<Person> route = new LinkedList<>(aRoute);
        LinkedList<Person> neighbour = new LinkedList<>(route);

        int stop = 50;

        double previousCost = getRouteCost(route, busNb);
        double neighbourCost;

        for (int i = 0; i < stop; i++) {
            //create neighbour
            createRouteNeighbour(route, neighbour);

            //test if neighbour is better
            neighbourCost = getRouteCost(neighbour, busNb);
            //System.out.println("NCost : "+neighbourCost);
            if (neighbourCost < previousCost && feasibleLine(new ArrayList<>(neighbour), busNb)) {
                //if needed update route and reset i
                System.out.println("Updating cost Opt: " + previousCost +" to "+ neighbourCost);
                previousCost = neighbourCost;
                copyRoute(neighbour, route);
                i = 0;
            }
        }
        return new ArrayList<>(route);
    }

    public static void createRouteNeighbour(LinkedList<Person> route, LinkedList<Person> neighbour) {
        if (route.size() <= 2) return;
        int r1 = (int) ((route.size() - 1) * Math.random()); //0 .. n-2
        int r2 = (int) ((route.size() - r1 - 1) * Math.random() + r1 + 1); //r1+1 .. n-1  i think
        //System.out.println("R1: "+r1+"   R2: "+r2);

        //copy route in neighbour and then modify it
        copyRoute(route, neighbour);

        LinkedList<Person> buffer = new LinkedList();

        int j = 0;
        //filling buffer (reverse way)
        for (Person current : route) {
            if (j >= r1 && j <= r2) {
                buffer.addFirst(current);
            } else if (j > r2) {
                break;
            }
            j++;
        }

        //copy buffer in neighbour
        Iterator<Person> it = buffer.iterator();
        for (int i = r1; i <= r2; i++) {
            neighbour.set(i, it.next());
        }
    }

    public static void copyRoute(LinkedList<Person> from, LinkedList<Person> to) {
        int i = 0;
        for (Person current : from) {
            to.set(i, current);
            i++;
        }
    }

    public static double getRouteCost(LinkedList<Person> route, int busNb) {
        double cost = 0;
        int previousStop = buses[busNb].getPosition().getBusStopID();
        HashSet<String> alreadySeen = new HashSet<>();

        for (Person current : route) {
            String currentPersonId = current.getId();
            int currentStop;
            if (alreadySeen.contains(currentPersonId)) {
                alreadySeen.remove(currentPersonId);
                currentStop = current.getArrival().getBusStopID();
            } else {
                alreadySeen.add(currentPersonId);
                currentStop = current.getDeparture().getBusStopID();
            }
            cost += durations[previousStop][currentStop];
            previousStop = currentStop;
        }
        return cost;
    }
}
