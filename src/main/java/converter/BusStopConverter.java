/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import java.util.List;
import modele.BusStop;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author elise
 */
public class BusStopConverter {

    public static Document toDocument(BusStop busStop) {
        Document doc = new Document("name", busStop.getName())
                .append("latitude", busStop.getLatitude())
                .append("longitude", busStop.getLongitude());
        if (busStop.getId() != null) {
            doc.append("_id", new ObjectId(busStop.getId()));
        }
        return doc;
    }

    public static BusStop toBusStop(Document doc) {
        BusStop busStop = new BusStop();
        busStop.setLatitude((Double) doc.get("latitude"));
        busStop.setLongitude((Double) doc.get("longitude"));
        busStop.setName((String) doc.get("name"));

        ObjectId id = (ObjectId) doc.get("_id");
        busStop.setId(id.toHexString());
        return busStop;

    }

    public static BusStop geoJsonToBusStop(Document doc) {
        BusStop busStop = new BusStop();
        
        Document geometry = (Document) doc.get("geometry");
        List<Double> coordinates = null;
        coordinates = (List<Double>) geometry.get("coordinates");

        busStop.setName((String) doc.get("name"));
        busStop.setLatitude(coordinates.get(0));
        busStop.setLongitude(coordinates.get(1));
        
        return busStop;
    }

    public static BusStop jsonToBusStop(String json) {
        return null;
    }

}
