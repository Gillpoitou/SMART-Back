/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.util.ArrayList;
import java.util.Date;

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
    private ArrayList<Person> passengers;
    private Date lastModif;

    public Bus() {
        this.nbPassengers = 0;
        this.position = null;
        this.passengers = new ArrayList<>();
    }

    public Bus(String id, String name, int nbPlaces, BusStop position, int nbPassengers) {
        this.id = id;
        this.name = name;
        this.nbPlaces = nbPlaces;
        this.position = position;
        this.nbPassengers = nbPassengers;
        this.passengers = new ArrayList<>();
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

    public ArrayList<Person> getPassengers() {
        return this.passengers;
    }

    public void setPassengers(ArrayList<Person> persons) {
        this.passengers = persons;
    }

    public void addPassenger(Person person) {
        this.passengers.add(person);
    }

    public void removePassenger(Person person) {
        this.passengers.remove(person);
    }

    public void setLastModif(Date newDate) {
        this.lastModif = newDate;
    }

    public Date getLastModif() {
        return this.lastModif;
    }
}
