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
    private String id;
    private BusStop departure;
    private BusStop arrival;
    private Date timeDeparture;
    private Line line;

    public Person() {
    }

    public Person(String id, BusStop departure, BusStop arrival, Date timeDeparture, Line line) {
        this.id = id;
        this.departure = departure;
        this.arrival = arrival;
        this.timeDeparture = timeDeparture;
        this.line = line;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }
    
    public boolean equals (Object o){
        if(!(o instanceof Person))
        return false;
        Person other = (Person)o;
        return this.getId().equals(other.getId());
    }
    
    @Override
    public String toString(){
        return id;
    }
}
