/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import java.util.Arrays;
import modele.Warehouse;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author elise
 */
public class WarehouseConverter {
    public static Document toDocument(Warehouse w) {

        Document doc = new Document("name", w.getName())
                .append("latitude", w.getLatitude())
                .append("longitude", w.getLongitude())
                .append("nbBus", w.getNbBus())
                .append("busStopId", w.getBusStopID());
        
        //Coordinates
        Document coord = new Document("type", "Point")
                .append("coordinates", Arrays.asList(w.getLongitude(), w.getLatitude()));
        doc.append("location", coord);
        
        if (w.getId() != null) {
            doc.append("_id", w.getId());
        }
        return doc;
    }
    
    public static Warehouse toWarehouse(Document doc) {
		Warehouse w = new Warehouse();
		w.setLatitude((Double) doc.get("latitude"));
		w.setLongitude((Double) doc.get("longitude"));
                w.setName((String) doc.get("name"));
                w.setNbBus((int) doc.get("nbBus"));

		ObjectId id = (ObjectId) doc.get("_id");
		w.setId(id.toHexString());
		return w;

	}
}
