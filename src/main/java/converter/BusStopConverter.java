/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;


import java.util.Date;
import modele.Bus;
import modele.BusStop;
import modele.Person;
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
            doc.append("_id", busStop.getId());
        }
        return doc;
    }
    
    public static BusStop toBusStop(Document doc) {
		BusStop busStop = new BusStop();
		busStop.setLatitude((Double) doc.get("latitude"));
		busStop.setLongitude((Double) doc.get("longitude"));
                busStop.setName((String) doc.get("name"));

		ObjectId id = (ObjectId) doc.get("_id");
		busStop.setId(id);
		return busStop;

	}
  
  public static BusStop jsonToBusStop(String json){
       return null;
  }
    
}