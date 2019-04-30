/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

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
}
