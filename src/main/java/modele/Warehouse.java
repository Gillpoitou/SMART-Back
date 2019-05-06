/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.util.Vector;
import org.bson.types.ObjectId;

/**
 *
 * @author elise
 */
public class Warehouse extends BusStop{
    private int nbBus;

    public Warehouse() {
    }

    public Warehouse(int nbBus, String id, String name, double latitude, double longitude, int busStopID, int nbPersonsWaiting, int nbPersonsComing, Vector<BusStopPath> paths) {
        super(id, name, latitude, longitude, busStopID, nbPersonsWaiting, nbPersonsComing, paths);
        this.nbBus = nbBus;
    }

    public int getNbBus() {
        return nbBus;
    }

    public void setNbBus(int nbBus) {
        this.nbBus = nbBus;
    }
}
