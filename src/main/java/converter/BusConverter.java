/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import modele.Bus;
import modele.BusStopLine;
import modele.Person;

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

        if (bus.getLastModif() != null) {
            doc.append("lastModif", bus.getLastModif());
        }

        ArrayList<Document> passengers = new ArrayList<>();
        for (Person person : bus.getPassengers()) {
            passengers.add(PersonConverter.toConstantDocument(person));
        }
        doc.append("passengers", passengers);

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

        if (doc.get("lastModif") != null) {
            bus.setLastModif((Date) doc.get("lastModif"));
        }

        if (doc.get("nbPassengers") != null) {
            bus.setNbPassengers((Integer) doc.get("nbPassengers"));
        }

        if (doc.get("position") != null) {
            bus.setPosition(BusStopConverter.toBusStop((Document) doc.get("position")));
        }
        
        ArrayList<Person> passengers = new ArrayList<Person>();
        if (doc.get("passengers") != null) {
            List<Document> _passengers = (List<Document>) doc.get("passengers");
            for (Document d : _passengers) {
                passengers.add(PersonConverter.toPerson(d));
            }
        }

        bus.setPassengers(passengers);

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

    public static Bus jsonToBus(String json) {
        Bus bus = new Bus();

        JsonElement jelement = new JsonParser().parse(json);
        JsonObject jsonBus = jelement.getAsJsonObject();
        jsonBus = jsonBus.getAsJsonObject("bus");
        
        String name = jsonBus.get("name").getAsString();
        bus.setName(name);
        
        int nbPlaces = jsonBus.get("nbPlaces").getAsInt();
        bus.setNbPlaces(nbPlaces);
        
        return bus;
    }
}
