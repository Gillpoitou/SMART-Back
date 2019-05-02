/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.util.ArrayList;
import java.util.Date;
import modele.*;
import modele.Line.BusStopLine;

/**
 *
 * @author
 */
public class Algorithm {
    
    public static ArrayList<Line> calculateLines (float[][]durations, Bus[]buses, Person[]requests, Date currentDate){
        //Appeler greedy
        //Appeler taboueLine currentLine = new Line();
        //Créer les lignes avec BusStop
        return null;
    }
    
    public static ArrayList<ArrayList<Person>> greedyAlgo(float[][]durations, Bus[]buses, ArrayList<Person> requests, Date currentDate){
        ArrayList <ArrayList<Person>> busLines = new ArrayList<>();
        ArrayList<Person> currentLine = new ArrayList<>();
        int lineNb = 0;
        busLines.add(currentLine);
        
        for (Person request : requests) {
            currentLine.add(request);
            currentLine.add(request);
            
            /*for(){
                for(){
                    
                }
            }*/
            
            if(feasibleLine(currentLine, buses[lineNb], durations, currentDate)){
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
    
    public static boolean feasibleLine(ArrayList<Person> line, Bus bus, float[][] durations, Date currentDate){
        boolean feasible = true;
        int personNb = 0;
        int duration = 0;
        BusStop preced = bus.getPosition();
        boolean[] gotOn = new boolean[line.size()];
        
        for(int i=0; i<line.size(); i++){
            if(gotOn[i] == false){
                gotOn[i] = true;
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
    
    public static ArrayList<Line> createLines (ArrayList<ArrayList<Person>> lines, Bus[] buses, float[][]durations, Date currentDate){
        ArrayList <Line> calculatedLines = new ArrayList<>();
        boolean[] gotOn = new boolean[lines.size()];
        int duration = 0;
        
        for(int i=0; i<lines.size(); i++){
            ArrayList<Person> currentCalculatedLine = lines.get(i);
            
            ArrayList<BusStopLine> currentLine = new ArrayList<>();
            Line.BusStopLine currentBusStop = currentCalculatedLine.new BusStopLine(currentCalculatedLine.get(0).getDeparture(), 0, 0, new Date(currentDate.getTime() + duration * 60000));
            currentLine.add(currentBusStop);
            gotOn[0]=true;
            duration += durations[buses[i].getPosition().getBusStopID()][currentBusStop.getBusStop().getBusStopID()];
            
            for(int j=1; j<currentCalculatedLine.size(); j++){
                if(gotOn[j] == false){
                    if(!currentBusStop.getBusStop().getName().equals(currentCalculatedLine.get(j).getDeparture().getName())){
                        duration += durations[currentBusStop.getBusStop().getBusStopID()][currentCalculatedLine.get(j).getDeparture().getBusStopID()];
                        currentBusStop = currentCalculatedLine.new BusStopLine(currentCalculatedLine.get(j).getDeparture(), 0, 0, new Date(currentDate.getTime() + duration * 60000));
                        currentLine.add(currentBusStop);
                    }
                    gotOn[j] = true;
                    currentBusStop.setNbGetOn(currentBusStop.getNbGetOn()+1);
                }else{
                    if(!currentBusStop.getBusStop().getName().equals(currentCalculatedLine.get(j).getArrival().getName())){
                        duration += durations[currentBusStop.getBusStop().getBusStopID()][currentCalculatedLine.get(j).getArrival().getBusStopID()];
                        currentBusStop = currentCalculatedLine.new BusStopLine(currentCalculatedLine.get(j).getArrival(), 0, 0, new Date(currentDate.getTime() + duration * 60000));
                        currentLine.add(currentBusStop);
                    }
                    currentBusStop.setNbGetOff(currentBusStop.getNbGetOff()+1);
                }
            }
            Line newLine = new Line(null, null, currentLine, buses[i]); //A changer le premier attribut!
            calculatedLines.add(newLine);
        }
        
        return calculatedLines;
    }
}
