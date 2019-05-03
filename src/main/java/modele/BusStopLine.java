/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

/**
 *
 * @author elise
 */
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
