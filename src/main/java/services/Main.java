/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.util.ArrayList;
import modele.Bus;
import modele.BusStop;
import java.util.Date;
import modele.Person;

/**
 *
 * @author 
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        float[][] durations = {{0, 5, 6}, {3, 0, 2}, {9, 4, 0}};
        BusStop busStop1 = new BusStop("", "Montaigne Montesquieu", 0, 0, 1, 2, 1, null);
        BusStop busStop2 = new BusStop("", "Doyen Brus", 0, 0, 2, 1, 2, null);
        BusStop busStop3 = new BusStop("", "François Bordes", 0, 0, 3, 2, 1, null);
        Date currentDate = new Date();
        Bus[] buses = {new Bus("", "Bus 1", 5, busStop1, 0), new Bus("", "Bus 2", 5, busStop1, 2)}; 
        
        Person person1 = new Person("", busStop1, busStop2, null, null);
        Person person2 = new Person();
        Person person3 = new Person();
        Person person4 = new Person();
        Person person5 = new Person();
        ArrayList <Person> requests = new ArrayList<>();
        
        Algorithm.greedyAlgo(durations, buses, requests, currentDate);
    }
    
}
