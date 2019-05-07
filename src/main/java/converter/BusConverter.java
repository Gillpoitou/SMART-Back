/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import com.google.gson.JsonObject;
import modele.Bus;

import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author thomasmalvoisin
 */
public class BusConverter {

    public static Document toDocument(Bus bus) {

        Document doc = new Document("name", bus.getName())
                .append("nbPlaces", bus.getNbPlaces())
                .append("position", BusStopConverter.toConstantDocument(bus.getPosition()))
                .append("nbPassengers", bus.getNbPassengers());
        if (bus.getId() != null) {
            doc.append("_id", new ObjectId(bus.getId()));
        }
        return doc;
    }

    public static Document toConstantDocument(Bus bus) {
        Document doc = new Document("name", bus.getName())
                .append("nbPlaces", bus.getNbPlaces());

        if (bus.getId() != null) {
            doc.append("_id", new ObjectId(bus.getId()));
        }
        return doc;
    }

    public static Bus toBus(Document doc) {
        Bus bus = new Bus();
        bus.setName((String) doc.get("name"));
        bus.setNbPlaces((Integer) doc.get("nbPlaces"));

        if (doc.get("nbPassengers") != null) {
            bus.setNbPassengers((Integer) doc.get("nbPassengers"));
        }

        if (doc.get("position") != null) {
            bus.setPosition(BusStopConverter.toBusStop((Document) doc.get("position")));
        }

        ObjectId id = (ObjectId) doc.get("_id");
        bus.setId(id.toHexString());

        return bus;
    }

    public static JsonObject BusToJson(Bus bus) {

        JsonObject result = new JsonObject();
        result.addProperty("id", bus.getId());
        result.addProperty("name", bus.getName());
        result.addProperty("nbPlaces", bus.getNbPlaces());
        
        if (bus.getPosition() != null) {
            JsonObject position = BusStopConverter.BusStopToJson(bus.getPosition());
            result.add("position", position);
        }

        result.addProperty("nbPassengers", bus.getNbPassengers());

        return result;
    }
}
