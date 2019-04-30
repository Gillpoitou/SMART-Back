/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dao.BusStopDAO;
import java.text.SimpleDateFormat;
import java.util.Date;
import modele.BusStop;
import modele.Person;

/**
 *
 * @author etien
 */
public class PersonConverter {
    public static Person jsonToPerson(String json) throws Exception{
        
    JsonElement jelement = new JsonParser().parse(json);
    JsonObject  jsonPerson = jelement.getAsJsonObject();
    jsonPerson = jsonPerson.getAsJsonObject("person");
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
    Date date = sdf.parse(jsonPerson.get("departure_date").toString());
    
    BusStopDAO bsdao = new BusStopDAO();
    BusStop departure = bsdao.getBusStopById(jsonPerson.get("departure").toString());
    BusStop arrival = bsdao.getBusStopById(jsonPerson.get("arrival").toString());
    
    Person person = new Person();
    
    return person;
    }
}
