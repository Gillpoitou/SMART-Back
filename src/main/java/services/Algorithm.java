/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.util.ArrayList;
import modele.*;

/**
 *
 * @author
 */
public class Algorithm {
    private static float[][] journeyDurations;
    
    public static double getCost(ArrayList<ArrayList<Person>> journeys){
        double cost = 0;
        for (int i = 0 ; i < journeys.size() ; i++){
            for (int j = 0 ; j < journeys.get(i).size() ; j++){
                String personId = journeys.get(i).get(j).getId();
                //int departureId = journeys.get(i).get(j).getDeparture().getBusStopId();
                //int arrivalId = journeys.get(i).get(j).getArrival().getBusStopId();
                //float bestTime = journeyDurations[departureId][arrivalId];
                
                //calcul du tps réel (attente + temps de trajet)
                /*
                int index = j + 1;
                int fromId = departureId;
                int toId = 0;
                
                int time = 0;
                while (!journeys.get(i).get(index).getId().equals(personId)){
                    if (journeys.get(i).get(index).
                    toId = journeys.get(i).get(index).get
                    index++;
                    time += 
                }*/
            }
        }
        return cost;
    }
    
    public static Line[] calculateLines (int[][]durations, Bus[]buses, Person[]requests){
        journeyDurations = durations;
        
        return null;
    }
    
    public static void TabuSearch (int[][]durations, Bus[]buses, Person[]requests){
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
