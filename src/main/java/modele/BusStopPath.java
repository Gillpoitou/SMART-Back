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

    private BusStop busStop;
    private double duration;
    private double distance;
    private Path path;

    public BusStopPath() {
    }

    public BusStopPath(BusStop busStop, double duration, double distance, Path path) {
        this.busStop = busStop;
        this.duration = duration;
        this.distance = distance;
        this.path = path;
    }

    public BusStop getBusStop() {
        return busStop;
    }

    public void setBusStop(BusStop busStop) {
        this.busStop = busStop;
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
