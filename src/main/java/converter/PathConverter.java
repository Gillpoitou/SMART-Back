/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import modele.Coordinates;
import modele.Path;
import org.bson.Document;

/**
 *
 * @author thomasmalvoisin
 */
public class PathConverter {
    
    public static Document toDocument(Path bus) {
        return null;
    }
    
    public static Path toPath(Document doc) {
        return null;
    }
    
    public static JsonObject PathToJson(Path path){
        JsonObject result = new JsonObject();
        JsonArray coordinates = new JsonArray();
        for(Coordinates coord : path.getCoordinates()){
            JsonObject jsonCoord = new JsonObject();
            jsonCoord.addProperty("latitude", coord.latitude);
            jsonCoord.addProperty("longitude", coord.longitude);
            coordinates.add(jsonCoord);
        }
        result.add("coordinates", coordinates);
        return result;
    }
    
}
