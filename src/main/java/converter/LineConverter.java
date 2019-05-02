/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import java.util.ArrayList;
import modele.Bus;
import modele.BusStop;
import modele.Line;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author elise
 */
public class LineConverter {
    public static Document toDocument(Line l) {

        Document doc = new Document("name", l.getName())
                .append("departure", l.getDeparture())
                .append("arrival", l.getArrival())
                .append("bus", l.getBus())
                .append("busStops", l.getBusStops());
        if (l.getId() != null) {
            doc.append("_id", l.getId());
        }
        return doc;
    }
    
    public static Line toLine(Document doc) {
		Line l = new Line();
		l.setName((String) doc.get("name"));
		l.setDeparture((BusStop) BusStopConverter.toBusStop((Document)doc.get("departure")));
                l.setArrival((BusStop) BusStopConverter.toBusStop((Document)doc.get("arrival")));
                l.setBus((Bus) BusConverter.toBus((Document)doc.get("Bus")));
                l.setBusStops((ArrayList<Line.BusStopLine>) doc.get("busStops"));

		ObjectId id = (ObjectId) doc.get("_id");
		l.setId(id.toHexString());
		return l;

	}
}
