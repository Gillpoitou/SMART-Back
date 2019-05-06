/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import modele.BusStop;
import modele.BusStopPath;
import modele.Coordinates;
import modele.Path;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author elise
 */
public class BusStopConverter {

    public static Document toDocument(BusStop busStop) {
        Document doc = new Document("name", busStop.getName())
                .append("BusStopId", busStop.getBusStopID())
                .append("nbPersonsWaiting", busStop.getNbPersonsWaiting())
                .append("nbPersonsComing", busStop.getNbPersonsComing());

        //Coordinates
        Document coord = new Document("type", "Point")
                .append("coordinates", Arrays.asList(busStop.getLongitude(), busStop.getLatitude()));
        doc.append("location", coord);

        //Paths
        ArrayList<Document> paths = new ArrayList<>();
        for (BusStopPath p : busStop.getPaths()) {
            paths.add(BusStopPathConverter.toDocument(p));
        }
        doc.append("paths", paths);

        if (busStop.getId() != null) {
            doc.append("_id", new ObjectId(busStop.getId()));
        }
        return doc;
    }

    public static Document toConstantDocument(BusStop busStop) {
        Document doc = new Document("name", busStop.getName())
                .append("BusStopId", busStop.getBusStopID());

        //Coordinates
        Document coord = new Document("type", "Point")
                .append("coordinates", Arrays.asList(busStop.getLongitude(), busStop.getLatitude()));
        doc.append("location", coord);

        if (busStop.getId() != null) {
            doc.append("_id", new ObjectId(busStop.getId()));
        }

        return doc;
    }

    public static BusStop toBusStop(Document doc) {
        BusStop busStop = new BusStop();
        busStop.setBusStopID((Integer) doc.get("BusStopId"));
        busStop.setName((String) doc.get("name"));
        if (doc.get("nbPersonsWaiting") != null) {
            busStop.setNbPersonsWaiting((Integer) doc.get("nbPersonsWaiting"));
        }

        if (doc.get("nbPersonsComing") != null) {
            busStop.setNbPersonsComing((Integer) doc.get("nbPersonsComing"));
        }


        //Coordinates
        List<Double> coord = (List<Double>) ((Document) doc.get("location")).get("coordinates");
        busStop.setLatitude(coord.get(1));
        busStop.setLongitude(coord.get(0));

        //Paths
        if (doc.get("paths") != null) {

            Vector<BusStopPath> paths = new Vector<BusStopPath>();
            List<Document> _paths = (List<Document>) doc.get("paths");
//        System.out.println(doc);
//        System.out.println(_paths.size());
            for (Document d : _paths) {
                paths.add(BusStopPathConverter.toBusStopPath(d));
            }
            busStop.setPaths(paths);
        }

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
        busStop.setLatitude(coordinates.get(1));
        busStop.setLongitude(coordinates.get(0));

        return busStop;
    }

    public static BusStop jsonToBusStop(String json) {
        return null;
    }

    public static JsonObject BusStopToJson(BusStop busStop) {
        JsonObject result = new JsonObject();

        result.addProperty("id", busStop.getId());
        result.addProperty("busStopId", busStop.getBusStopID());
        result.addProperty("name", busStop.getName());
        result.addProperty("latitude", busStop.getLatitude());
        result.addProperty("longitude", busStop.getLongitude());
        result.addProperty("nbPersonsWaiting", busStop.getNbPersonsWaiting());
        result.addProperty("nbPersonsComing", busStop.getNbPersonsComing());
        if (busStop.getPaths() != null) {
            JsonArray busStopPaths = new JsonArray();
            for (BusStopPath bsPath : busStop.getPaths()) {
                JsonObject busStopPathJson = BusStopPathConverter.BusStopPathToJson(bsPath);
                busStopPaths.add(busStopPathJson);
            }
            result.add("paths", busStopPaths);
        }
        return result;
    }

    public static BusStop UpdateBusStopFromJson(BusStop currentBusStop, BusStop destinationBusStop, JsonObject json) {
        JsonArray features = json.getAsJsonArray("features");
        JsonElement travel = features.get(0);
        JsonObject properties = travel.getAsJsonObject();
        properties = properties.getAsJsonObject("properties");
        JsonObject summary = properties.getAsJsonObject("summary");

        BusStopPath bsPath = new BusStopPath();
        bsPath.setBusStop(destinationBusStop);
        bsPath.setDistance(summary.get("distance").getAsDouble());
        bsPath.setDuration(summary.get("duration").getAsFloat());

        JsonObject geometry = travel.getAsJsonObject();
        geometry = geometry.getAsJsonObject("geometry");
        JsonArray coordinates = geometry.getAsJsonArray("coordinates");
        Path javaPath = new Path();
        List<Coordinates> coords = new LinkedList();
        for (JsonElement elem : coordinates) {
            JsonArray currentjsonCoord = elem.getAsJsonArray();
            double latitude = currentjsonCoord.get(1).getAsDouble();
            double longitude = currentjsonCoord.get(0).getAsDouble();
            Coordinates currentCoord = new Coordinates(latitude, longitude);
            coords.add(currentCoord);
        }
        javaPath.setCoordinates(coords);

        bsPath.setPath(javaPath);
        currentBusStop.addBusStopPath(bsPath);
        return currentBusStop;
    }

}
