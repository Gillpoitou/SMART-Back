/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

/**
 *
 * @author thomasmalvoisin
 */
public class BusStopPath {

    private BusStop busStopDestination;
    private double duration;
    private double distance;
    private Path path;

    public BusStopPath() {
    }

    public BusStopPath(BusStop busStopDestination, double duration, double distance, Path path) {
        this.busStopDestination = busStopDestination;
        this.duration = duration;
        this.distance = distance;
        this.path = path;
    }

    public BusStop getBusStop() {
        return busStopDestination;
    }

    public void setBusStop(BusStop busStop) {
        this.busStopDestination = busStop;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}
