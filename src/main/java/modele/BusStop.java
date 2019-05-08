/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.util.Vector;

/**
 *
 * @author etien
 */
public class BusStop {

    private String id;
    private int busStopID;
    private String name;
    private double latitude;
    private double longitude;
    private int nbPersonsWaiting;
    private int nbPersonsComing;
    private Vector<BusStopPath> paths;

    public BusStop() {
        this.nbPersonsWaiting = 0;
        this.nbPersonsComing = 0;
        this.paths = new Vector<BusStopPath>();
    }

    public BusStop(String id, String name, double latitude, double longitude, int busStopID, int nbPersonsWaiting, int nbPersonsComing, Vector<BusStopPath> paths) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.busStopID = busStopID;
        this.nbPersonsWaiting = nbPersonsWaiting;
        this.nbPersonsComing = nbPersonsComing;
        this.paths = paths;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setBusStopID(int busStopID) {
        this.busStopID = busStopID;
    }

    public int getBusStopID() {
        return busStopID;
    }

    public int getNbPersonsWaiting() {
        return nbPersonsWaiting;
    }

    public void setNbPersonsWaiting(int nbPersonsWaiting) {
        this.nbPersonsWaiting = nbPersonsWaiting;
    }

    public int getNbPersonsComing() {
        return nbPersonsComing;
    }

    public void setNbPersonsComing(int nbPersonsComing) {
        this.nbPersonsComing = nbPersonsComing;
    }

    public Vector<BusStopPath> getPaths() {
        return paths;
    }

    public void setPaths(Vector<BusStopPath> paths) {
        this.paths = paths;
    }

    public void addBusStopPath(BusStopPath bsPath) {
        this.paths.add(bsPath);
    }

    public double getDurationToTarget(int targetBusStopId) {
        if (targetBusStopId > 0 && paths.get(targetBusStopId - 1).getBusStop().getBusStopID() == targetBusStopId) {
            return paths.get(targetBusStopId - 1).getDuration();
        } else if (paths.get(targetBusStopId).getBusStop().getBusStopID() == targetBusStopId) {
            return paths.get(targetBusStopId).getDuration();
        }else {
            //target not found
            return -1;
        }
    }
}
