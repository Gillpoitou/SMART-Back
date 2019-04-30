/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package services;

import dao.PersonDAO;
import modele.BusStop;
import modele.Person;


/**
 *
 * @author etien
 */
public class Services {
    
    public static String getBusMapDisplay(){
        
        return "";
    }
    
    public static void postBusRequest(Person person){
        PersonDAO psdao = new PersonDAO(null);
        psdao.insertPerson(person);
    }
}
