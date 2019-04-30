/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import modele.Bus;

import com.mongodb.BasicDBObjectBuilder;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author thomasmalvoisin
 */
public class BusConverter {

    public static Document toDocument(Bus bus) {

        Document doc = new Document("name", bus.getName())
                .append("nbPlaces", bus.getNbPlaces());
        if (bus.getId() != null) {
            doc.append("_id", bus.getId());
        }
        return doc;
    }
    
    public static Bus toBus(Document doc) {
		Bus bus = new Bus();
		bus.setName((String) doc.get("name"));
		bus.setNbPlaces((Integer) doc.get("nbPlaces"));
		ObjectId id = (ObjectId) doc.get("_id");
		bus.setId(id);
		return bus;

	}
    
}
