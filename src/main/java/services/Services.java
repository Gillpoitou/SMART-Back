/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package services;

import com.mongodb.client.MongoClient;
import dao.PersonDAO;
import modele.BusStop;
import modele.Person;


/**
 *
 * @author etien
 */
public class Services {
    
    public static String getBusMapDisplay(){
        
        return "GET_BUS_MAP_DISPLAY";
    }
    
    public static boolean postBusRequest(MongoClient mongoClient,Person person){
        
        try{
            PersonDAO personDAO = new PersonDAO(mongoClient);
            personDAO.createPerson(person);
            return true;
        }catch(Exception e){
            return false;
        }
    }
}
