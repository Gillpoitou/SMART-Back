/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoClient;
import dao.BusStopDAO;
import java.text.SimpleDateFormat;
import java.util.Date;
import modele.AlgoParameters;
import modele.BusStop;
import modele.Person;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author elise
 */
public class AlgoParametersConverter {

    public static Document toDocument(AlgoParameters aP) {

        Document doc = new Document("maxRequestNb", aP.getMaxRequestNb())
                .append("maxTimeInterval", aP.getMaxTimeInterval());
        if (aP.getId() != null) {
            doc.append("_id", new ObjectId(aP.getId()));
        }
        return doc;
    }

    public static AlgoParameters toAlgoParameters(Document doc) {

        AlgoParameters aP = new AlgoParameters();
        aP.setMaxRequestNb((Integer) doc.get("maxRequestNb"));
        aP.setMaxTimeInterval((Long) doc.get("maxTimeInterval"));
        ObjectId id = (ObjectId) doc.get("_id");
        aP.setId(id.toHexString());
        return aP;
    }

    public static JsonObject AlgoParamatersToJson(AlgoParameters aP) {

        JsonObject result = new JsonObject();
        result.addProperty("id", aP.getId());
        result.addProperty("maxRequestNb", aP.getMaxRequestNb());
        result.addProperty("maxTimeInterval", aP.getMaxTimeInterval());

        return result;
    }

    public static AlgoParameters jsonToAlgoParameters(String json) throws Exception {

        JsonElement jelement = new JsonParser().parse(json);
        JsonObject jsonAP = jelement.getAsJsonObject();
        jsonAP = jsonAP.getAsJsonObject("algoParameters");

        int maxRequestNb = jsonAP.get("maxRequestNb").getAsInt();
        long maxTimeInterval = jsonAP.get("maxTimeInterval").getAsLong();
        AlgoParameters aP = new AlgoParameters();
        aP.setMaxRequestNb(maxRequestNb);;
        aP.setMaxTimeInterval(maxTimeInterval);
        return aP;
    }
}
