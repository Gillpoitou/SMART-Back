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
import java.util.LinkedList;
import java.util.List;
import modele.Coordinates;
import modele.Path;
import org.bson.BsonArray;
import org.bson.Document;

/**
 *
 * @author thomasmalvoisin
 */
public class PathConverter {

    public static Document toDocument(Path path) {
        Document doc = new Document();
        ArrayList<Document> coords = new ArrayList<Document>();
        for (Coordinates coord : path.getCoordinates()) {
            Document current = new Document("longitude", coord.longitude)
                    .append("latitude", coord.latitude);
            coords.add(current);
        }

        doc.append("coords", coords);
        return doc;
    }

    public static Path toPath(Document doc) {
        Path path = new Path();
        List<Coordinates> coords = new LinkedList();

        List<Document> _coords = (List<Document>) doc.get("coords");
        for (Document d : _coords) {
            System.out.println(d);
            coords.add(new Coordinates(((Double) d.get("latitude")).doubleValue(), ((Double) d.get("longitude")).doubleValue())
        
        );
        }
        
        path.setCoordinates(coords);

        return path;
    }

    public static JsonObject PathToJson(Path path) {
        JsonObject result = new JsonObject();
        JsonArray coordinates = new JsonArray();
        for (Coordinates coord : path.getCoordinates()) {
            JsonObject jsonCoord = new JsonObject();
            jsonCoord.addProperty("latitude", coord.latitude);
            jsonCoord.addProperty("longitude", coord.longitude);
            coordinates.add(jsonCoord);
        }
        result.add("coordinates", coordinates);
        return result;
    }

}
