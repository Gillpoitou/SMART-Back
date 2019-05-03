/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.util.Date;

/**
 *
 * @author elise
 */
public class BusStopLine {

    private BusStop busStop;
    private int nbGetOn;
    private int nbGetOff;
    private Date time;

    public BusStopLine() {
    }

    public BusStopLine(BusStop busStop, int nbGetOn, int nbGetOff, Date time) {
        this.busStop = busStop;
        this.nbGetOn = nbGetOn;
        this.nbGetOff = nbGetOff;
        this.time = time;
    }
 
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
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
