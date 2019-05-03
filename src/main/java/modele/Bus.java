/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import org.bson.types.ObjectId;

/**
 *
 * @author thomasmalvoisin
 */
public class Bus {

    private String id;
    private String name;
    private int nbPlaces;
    private BusStop position;
    private int nbPassengers;

    public Bus() {
    }

    public Bus(String id, String name, int nbPlaces, BusStop position, int nbPassengers) {
        this.id = id;
        this.name = name;
        this.nbPlaces = nbPlaces;
        this.position = position;
        this.nbPassengers = nbPassengers;
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

    public int getNbPlaces() {
        return nbPlaces;
    }

    public void setNbPlaces(int nbPlaces) {
        this.nbPlaces = nbPlaces;
    }

    public BusStop getPosition() {
        return position;
    }

    public void setPosition(BusStop position) {
        this.position = position;
    }

    public int getNbPassengers() {
        return nbPassengers;
    }

    public void setNbPassengers(int nbPassengers) {
        this.nbPassengers = nbPassengers;
    }
}
