/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.util.ArrayList;

/**
 *
 * @author etien
 */
public class Simulation {
    int numberTravelers;
    ArrayList<SimulationRatio> busStopsRatios = new ArrayList();

    public void setNumberTravelers(int numberTravelers) {
        this.numberTravelers = numberTravelers;
    }

    public void setBusStopsRatios(ArrayList<SimulationRatio> busStopsRatios) {
        this.busStopsRatios = busStopsRatios;
    }

    public int getNumberTravelers() {
        return numberTravelers;
    }

    public ArrayList<SimulationRatio> getBusStopsRatios() {
        return busStopsRatios;
    }
}


