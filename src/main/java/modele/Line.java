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
        busStops = new ArrayList<BusStopLine>();
    }

    public Line(String name, BusStop departure, BusStop arrival, ArrayList<BusStopLine> busStops, Bus bus) {
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
}
