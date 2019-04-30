/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.util.Date;
import org.bson.types.ObjectId;

/**
 *
 * @author elise
 */
public class Person {
     private ObjectId id;
     private BusStop departure;
     private BusStop arrival;
     private Date timeDeparture;
     private Bus bus;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
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

    public Date getTimeDeparture() {
        return timeDeparture;
    }

    public void setTimeDeparture(Date time_departure) {
        this.timeDeparture = time_departure;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }
     
     
}
