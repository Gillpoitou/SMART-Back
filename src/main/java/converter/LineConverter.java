/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import modele.Bus;
import modele.BusStop;
import modele.BusStopLine;
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
                .append("departure", BusStopConverter.toConstantDocument(l.getDeparture()))
                .append("arrival", BusStopConverter.toConstantDocument(l.getArrival()))
                .append("bus", BusConverter.toConstantDocument(l.getBus()));

        //BusStops
        ArrayList<Document> busStops = new ArrayList<>();
        for (BusStopLine bs : l.getBusStops()) {
            busStops.add(BusStopLineConverter.toDocument(bs));
        }
        doc.append("busStops", Arrays.asList(busStops));

        if (l.getId() != null) {
            doc.append("_id", l.getId());
        }

        return doc;
    }

    public static Document toConstantDocument(Line l) {
        Document doc = new Document("name", l.getName())
                .append("bus", BusConverter.toConstantDocument(l.getBus()));;

        if (l.getId() != null) {
            doc.append("_id", l.getId());
        }

        return doc;
    }

    public static Line toLine(Document doc) {
        Line l = new Line();
        l.setName((String) doc.get("name"));
        l.setDeparture((BusStop) BusStopConverter.toBusStop((Document) doc.get("departure")));
        l.setArrival((BusStop) BusStopConverter.toBusStop((Document) doc.get("arrival")));
        l.setBus((Bus) BusConverter.toBus((Document) doc.get("Bus")));

        //BusStops
        List<Document> _busStops = (List<Document>) doc.get("busStops");
        ArrayList<BusStopLine> busStops = new ArrayList<BusStopLine>();
        for (Document d : _busStops) {
            busStops.add(BusStopLineConverter.toBusStopLine(d));
        }
        l.setBusStops(busStops);

        ObjectId id = (ObjectId) doc.get("_id");
        l.setId(id.toHexString());

        return l;
    }
    
    public static JsonObject LineToJson(Line line){
        
        JsonObject result = new JsonObject();
        result.addProperty("id", line.getId());
        result.addProperty("name", line.getName());
        JsonObject busStopDpt = BusStopConverter.BusStopToJson(line.getDeparture());
        JsonObject busStopArr = BusStopConverter.BusStopToJson(line.getArrival());
        result.add("departure", busStopDpt);
        result.add("arrival",busStopArr);
        JsonObject bus = BusConverter.BusToJson(line.getBus());
        result.add("bus", bus);
        JsonArray busStops = new JsonArray();
        for(BusStopLine bsl : line.getBusStops()){
            JsonObject busStopLine = BusStopLineConverter.BusStopLineToJson(bsl);
            busStops.add(busStopLine);
        }
        result.add("busStopLines", busStops);
        return result;
    }
}
