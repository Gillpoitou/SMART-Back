/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import modele.Bus;
import modele.BusStop;
import java.util.Date;
import modele.Line;
import modele.Person;

/**
 *
 * @author 
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ParseException {
        double[][] durations = {{0, 5*60, 6*60}, {3*60, 0, 2*60}, {60*9, 4*60, 0}};
        BusStop busStop1 = new BusStop("BusStop 1", "Stop 1", 0, 0, 0, 2, 1, null);
        BusStop busStop2 = new BusStop("BusStop 2", "Stop 2", 0, 0, 1, 1, 2, null);
        BusStop busStop3 = new BusStop("BusStop 3", "Stop 3", 0, 0, 2, 2, 2, null);
        
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date currentDate = simpleDateFormat.parse("2019-05-03 09:50:00");
        
        Bus[] buses = {new Bus("Bus 1", "Bus 1", 5, busStop1, 0), new Bus("Bus 2", "Bus 2", 5, busStop1, 2), new Bus("Bus 3", "Bus 3", 5, busStop2, 2)}; 
        
        Date date1 = simpleDateFormat.parse("2019-05-03 09:51:40");
        Date date2 = simpleDateFormat.parse("2019-05-03 10:25:56");
        Date date3 = simpleDateFormat.parse("2019-05-03 09:58:05");
        Date date4 = simpleDateFormat.parse("2019-05-03 10:05:10");
        Date date5 = simpleDateFormat.parse("2019-05-03 10:06:50");
        Date date6 = simpleDateFormat.parse("2019-05-03 09:56:50");
        Date date7 = simpleDateFormat.parse("2019-05-03 09:54:50");
        Person person1 = new Person("Person 1", busStop1, busStop2, date1, null);
        Person person2 = new Person("Person 2", busStop1, busStop3, date2, null);
        Person person3 = new Person("Person 3", busStop2, busStop3, date3, null);
        Person person4 = new Person("Person 4", busStop3, busStop1, date4, null);
        Person person5 = new Person("Person 5", busStop3, busStop2, date5, null);
        Person person6 = new Person("Person 6", busStop1, busStop2, date6, null);
        Person person7 = new Person("Person 7", busStop2, busStop1, date7, null);
        ArrayList <Person> requests = new ArrayList<>();
        requests.add(person1);
        requests.add(person2);
        requests.add(person3);
        requests.add(person4);
        requests.add(person5);
        requests.add(person6);
        requests.add(person7);
        
        
        
        
        ArrayList<Line> result = Algorithm.calculateLines(durations, buses, requests, currentDate);
        
        /*for(Line line: result){
            System.out.println(line.toString());
        }*/
    }
    
}
