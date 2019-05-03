/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import com.google.gson.JsonObject;
import modele.BusStop;
import modele.BusStopPath;
import modele.Path;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author thomasmalvoisin
 */
public class BusStopPathConverter {

    public static Document toDocument(BusStopPath busStopPath) {
        Document doc = new Document("duration", busStopPath.getDuration())
                .append("distance", busStopPath.getDistance())
                .append("busStop", BusStopConverter.toDocument(busStopPath.getBusStop()))
                .append("path", PathConverter.toDocument(busStopPath.getPath()));
        return doc;
    }

    public static BusStopPath toBusStopPath(Document doc) {
        BusStopPath busStopPath = new BusStopPath();
        busStopPath.setDuration((Double) doc.get("duration"));
        busStopPath.setDistance((Double) doc.get("distance"));
        busStopPath.setBusStop((BusStop) BusStopConverter.toBusStop((Document) doc.get("busStop")));
        busStopPath.setPath((Path) PathConverter.toPath((Document) doc.get("path")));

        return busStopPath;
    }
    
    public static JsonObject BusStopPathToJson(BusStopPath busStopPath){
        JsonObject result = new JsonObject();
        JsonObject busStopDestination = BusStopConverter.BusStopToJson(busStopPath.getBusStop());
        result.add("busStopDestination",busStopDestination);
        result.addProperty("duration",busStopPath.getDuration());
        result.addProperty("distance",busStopPath.getDistance());
        JsonObject path = PathConverter.PathToJson(busStopPath.getPath());
        result.add("path", path);
        return result;
    }
}
