/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.util.ArrayList;
import java.util.Date;
import modele.*;

/**
 *
 * @author
 */
public class Algorithm {
    private static float[][] durations;
    
    public static double getCost(ArrayList<ArrayList<Person>> journeys, ArrayList<Line> lines){
        double cost = 0;
        for (int i = 0 ; i < journeys.size() ; i++){
            ArrayList<Person> journey = journeys.get(i);
            ArrayList<BusStopLine> stops = lines.get(i).getBusStops();
            for (int j = 0 ; j < journey.size() ; j++){
                double realDuration;
                Date depDate = journey.get(j).getTimeDeparture();
                String DepId = journey.get(j).getDeparture().getId();
                String ArrId = journey.get(j).getArrival().getId();
                
                //search for the first stop of the line after the departure time
                //to avoid being confused by possibles loop of the line
                int k = 0;
                Date firstStopDate = stops.get(k).getTime();
                while (depDate.after(firstStopDate)){
                    k++;
                    firstStopDate = stops.get(k).getTime();
                }
                
                //search for the waiting time at the bus stop
                String CurStopId = stops.get(k).getBusStop().getId();
                while(!DepId.equals(CurStopId)){
                    k++;
                    CurStopId = stops.get(k).getBusStop().getId();
                }
                
                
                //search for the time the bus reach the arrivalStop
                while(!ArrId.equals(CurStopId)){
                    k++;
                    CurStopId = stops.get(k).getBusStop().getId();
                }
                
                realDuration = (stops.get(k).getTime().getTime() - depDate.getTime())/1000;
                int departureId = journey.get(j).getDeparture().getBusStopID();
                int arrivalId = journey.get(j).getArrival().getBusStopID();
                
                double bestDuration = durations[departureId][arrivalId];
                
                double value = realDuration / bestDuration;
                
                //DEBUG
                System.out.println("realD: "+realDuration+"   bestD: "+bestDuration+"   Val: "+value);
                
                cost += value;
            }
        }
        return cost/2;      //because we calculate twice for each person
    }
    
    public static ArrayList<Line> calculateLines (float[][]journeyDurations, Bus[]buses, Person[]requests, Date currentDate){
        durations = journeyDurations;
        //Appeler greedy
        //Appeler taboueLine currentLine = new Line();
        //Créer les lignes avec BusStop
        return null;
    }
    
    public static ArrayList<ArrayList<Person>> greedyAlgo(Bus[]buses, ArrayList<Person> requests, Date currentDate){
        ArrayList <ArrayList<Person>> busLines = new ArrayList<>();
        ArrayList<Person> currentLine = new ArrayList<>();
        int lineNb = 0;
        busLines.add(currentLine);
        
        for (Person request : requests) {
            currentLine.add(request);
            currentLine.add(request);
            
            for(int i=0; i< requests.size(); i++){
                for(int j=0; j< requests.size(); j++){
                    int indexDepartureI = currentLine.indexOf(requests.get(i));
                    int indexArrivalI = currentLine.lastIndexOf(requests.get(i));
                    int indexDepartureJ = currentLine.indexOf(requests.get(j));
                    int indexArrivalJ = currentLine.lastIndexOf(requests.get(j));
                    int durationI = (int) durations[requests.get(i).getDeparture().getBusStopID()][requests.get(i).getArrival().getBusStopID()];
                    int durationJ = (int) durations[requests.get(j).getDeparture().getBusStopID()][requests.get(j).getArrival().getBusStopID()];
                    Date dateArrivalI = new Date(requests.get(i).getTimeDeparture().getTime() + durationI * 60000);
                    Date dateArrivalJ = new Date(requests.get(j).getTimeDeparture().getTime() + durationJ * 60000);
                    
                    if(requests.get(i).getTimeDeparture().compareTo(requests.get(j).getTimeDeparture()) < 0 && indexDepartureI > indexDepartureJ){
                        Person temp = currentLine.get(indexDepartureI);
                        currentLine.set(indexDepartureI, currentLine.get(indexDepartureJ));
                        currentLine.set(indexDepartureJ, temp);
                    }else if(requests.get(i).getTimeDeparture().compareTo(dateArrivalJ) < 0 && indexDepartureI > indexArrivalJ){
                        Person temp = currentLine.get(indexDepartureI);
                        currentLine.set(indexDepartureI, currentLine.get(indexArrivalJ));
                        currentLine.set(indexArrivalJ, temp);
                    }else if(dateArrivalI.compareTo(requests.get(j).getTimeDeparture()) < 0 && indexArrivalI > indexDepartureJ){
                        Person temp = currentLine.get(indexArrivalI);
                        currentLine.set(indexArrivalI, currentLine.get(indexDepartureJ));
                        currentLine.set(indexDepartureJ, temp);
                    }else if(dateArrivalI.compareTo(dateArrivalJ) < 0 && indexArrivalI > indexArrivalJ){
                        Person temp = currentLine.get(indexArrivalI);
                        currentLine.set(indexArrivalI, currentLine.get(indexArrivalJ));
                        currentLine.set(indexArrivalJ, temp);
                    }
                }
            }
            
            if(feasibleLine(currentLine, buses[lineNb], currentDate)){
                requests.remove(request);
                currentLine = busLines.get(0);
                lineNb=0;
            }else{
                currentLine.remove(request);
                currentLine.remove(request);
                ArrayList<Person> newLine = new ArrayList<>();
                busLines.add(newLine);
                currentLine=newLine;
                lineNb++;
            } 
        }
        return busLines;
    }
    
    public static boolean feasibleLine(ArrayList<Person> line, Bus bus, Date currentDate){
        boolean feasible = true;
        int personNb = 0;
        int duration = 0;
        BusStop preced = bus.getPosition();
        
        for(int i=0; i<line.size(); i++){
            if(i == line.indexOf(line.get(i))){
                personNb++;
                duration += durations[preced.getBusStopID()][line.get(i).getDeparture().getBusStopID()];
            }else{
                personNb--;
                duration += durations[preced.getBusStopID()][line.get(i).getArrival().getBusStopID()];
                
                Date arrivalDate = new Date(currentDate.getTime() + duration * 60000);
                int durationMin = (int) (durations[line.get(i).getDeparture().getBusStopID()][line.get(i).getArrival().getBusStopID()] * 60000);
                Date maxDate = new Date(line.get(i).getTimeDeparture().getTime() + durationMin); // A changer avec pourcentage
                if(arrivalDate.compareTo(maxDate) > 0){
                    feasible = false;
                    break;
                }
            }
            
            if(personNb>bus.getNbPlaces()){
                feasible = false;
                break;
            }
        }
        return feasible;
    }
    
    public static ArrayList<Line> createLines (ArrayList<ArrayList<Person>> lines, Bus[] buses, Date currentDate){
        ArrayList <Line> calculatedLines = new ArrayList<>();
        int duration = 0;
        
        for(int i=0; i<lines.size(); i++){
            ArrayList <Person> currentCalculatedLine = lines.get(i);
            
            ArrayList<BusStopLine> currentLine = new ArrayList<>();
            BusStopLine currentBusStop = new BusStopLine(currentCalculatedLine.get(0).getDeparture(), 0, 0, new Date(currentDate.getTime() + duration * 60000));
            currentLine.add(currentBusStop);
            duration += durations[buses[i].getPosition().getBusStopID()][currentBusStop.getBusStop().getBusStopID()];
            
            for(int j=1; j<currentCalculatedLine.size(); j++){
                if(j == lines.indexOf(lines.get(j))){
                    if(!currentBusStop.getBusStop().getName().equals(currentCalculatedLine.get(j).getDeparture().getName())){
                        duration += durations[currentBusStop.getBusStop().getBusStopID()][currentCalculatedLine.get(j).getDeparture().getBusStopID()];
                        currentBusStop = new BusStopLine(currentCalculatedLine.get(j).getDeparture(), 0, 0, new Date(currentDate.getTime() + duration * 60000));
                        currentLine.add(currentBusStop);
                    }
                    currentBusStop.setNbGetOn(currentBusStop.getNbGetOn()+1);
                }else{
                    if(!currentBusStop.getBusStop().getName().equals(currentCalculatedLine.get(j).getArrival().getName())){
                        duration += durations[currentBusStop.getBusStop().getBusStopID()][currentCalculatedLine.get(j).getArrival().getBusStopID()];
                        currentBusStop = new BusStopLine(currentCalculatedLine.get(j).getArrival(), 0, 0, new Date(currentDate.getTime() + duration * 60000));
                        currentLine.add(currentBusStop);
                    }
                    currentBusStop.setNbGetOff(currentBusStop.getNbGetOff()+1);
                }
            }
            Line newLine = new Line("Line "+i, buses[i].getPosition(), null, currentLine, buses[i]);
            calculatedLines.add(newLine);
        }
        
        return calculatedLines;
    }
    
    public static void TabuSearch (Bus[]buses, Person[]requests){
        int timeSinceLastBestUpdate = 0;
        /*
        sol;
        bestSol;
        neighbour;
        bestNeighbour;*/
        
        //Generate tabu list as the last time a person was used in a move
        int[] tabuList = new int[requests.length];
        for (int i = 0 ; i < tabuList.length ; i++){
            tabuList[i] = -100;     //to free every move
        }
        int TABU_LENGTH = 20;
        
        int iter = 0;
        int bestLastUpdate = 0;
        int convergence = 40;
        
        while (iter - bestLastUpdate < convergence){
            iter++;
            
            //searching for the best OR a good neighbour
            //using only non tabu requests
            
            
            //move to bestNeighbour AND update tabuList of the request that has been moved
            
            
            //update bestSol if needed
        }
    }
}
