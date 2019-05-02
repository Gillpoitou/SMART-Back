/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.util.ArrayList;

/**
 *
 * @author elise
 */
public class Line {

    private String id;
    private String name;
    private BusStop departure;
    private BusStop arrival;
    ArrayList<BusStopLine> busStops;
    private Bus bus;

    public Line() {
    }

    public Line(String name, Warehouse departure, Warehouse arrival, ArrayList<BusStopLine> busStops, Bus bus) {
        this.name = name;
        this.departure = departure;
        this.arrival = arrival;
        this.busStops = busStops;
        this.bus = bus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BusStop getDeparture() {
        return departure;
    }

    public void setDeparture(BusStop departure) {
        this.departure = departure;
    }

    public BusStop getArrival() {
        return arrival;
    }

    public void setArrival(BusStop arrival) {
        this.arrival = arrival;
    }

    public ArrayList<BusStopLine> getBusStops() {
        return busStops;
    }

    public void setBusStops(ArrayList<BusStopLine> busStops) {
        this.busStops = busStops;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public class BusStopLine {

        private BusStop busStop;
        private int nbGetOn;
        private int nbGetOff;
        private float time;

        public BusStopLine() {
        }

        public BusStopLine(BusStop busStop, int nbGetOn, int nbGetOff, float time) {
            this.busStop = busStop;
            this.nbGetOn = nbGetOn;
            this.nbGetOff = nbGetOff;
            this.time = time;
        }

        public float getTime() {
            return time;
        }

        public void setTime(float time) {
            this.time = time;
        }

        public BusStop getBusStop() {
            return busStop;
        }

        public void setBusStop(BusStop busStop) {
            this.busStop = busStop;
        }

        public int getNbGetOn() {
            return nbGetOn;
        }

        public void setNbGetOn(int nbGetOn) {
            this.nbGetOn = nbGetOn;
        }

        public int getNbGetOff() {
            return nbGetOff;
        }

        public void setNbGetOff(int nbGetOff) {
            this.nbGetOff = nbGetOff;
        }

    }
}
