/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

/**
 *
 * @author etien
 */
public class BusStop {

    private String id;
    private String name;
    private double latitude;
    private double longitude;
    private int busStopID;

    

    public BusStop() {
    }

    public BusStop(String id, String name, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
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
    
}
