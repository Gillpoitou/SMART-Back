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
import modele.Bus;
import modele.Person;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author elise
 */
public class PersonConverter {
     public static Document toDocument(Person person) {

        Document doc = new Document("arrival", person.getArrival())
                .append("departure", person.getDeparture())
                .append("bus", person.getBus())
                .append("timeDeparture", person.getTimeDeparture());
        if (person.getId() != null) {
            doc.append("_id", person.getId());
        }
        return doc;
    }
    
    public static Person toPerson(Document doc) {
		Person p = new Person();
		p.setArrival((BusStop) BusStopConverter.toBusStop((Document)doc.get("arrival")));
		p.setDeparture((BusStop) BusStopConverter.toBusStop((Document)doc.get("departure")));
                p.setBus((Bus) BusConverter.toBus((Document)doc.get("bus")));
                p.setTimeDeparture((Date) doc.get("timeDeparture"));
		ObjectId id = (ObjectId) doc.get("_id");
		p.setId(id);
		return p;

	}
  public static Person jsonToPerson(String json) throws Exception{
        
    JsonElement jelement = new JsonParser().parse(json);
    JsonObject  jsonPerson = jelement.getAsJsonObject();
    jsonPerson = jsonPerson.getAsJsonObject("person");
    SimpleDateFormat sdf = new SimpleDateFormat("\"dd-MM-yyyy HH:mm:ss\"");
    Date date = sdf.parse(jsonPerson.get("departure_date").toString());
    
    BusStopDAO bsdao = new BusStopDAO();
    BusStop departure = bsdao.getBusStopById(jsonPerson.get("departure").toString());
    BusStop arrival = bsdao.getBusStopById(jsonPerson.get("arrival").toString());
    
    Person person = new Person();
    person.setArrival(arrival);
    person.setDeparture(departure);
    person.setTimeDeparture(date);
    return person;
    }
}
