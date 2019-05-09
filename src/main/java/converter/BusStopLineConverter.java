/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import java.util.ArrayList;
import com.google.gson.JsonObject;
import java.util.Date;
import java.util.List;
import modele.BusStop;
import modele.BusStopLine;
import modele.Person;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author elise
 */
public class BusStopLineConverter {

    public static Document toDocument(BusStopLine busStopLine) {

        Document doc = new Document("busStop", BusStopConverter.toConstantDocument(busStopLine.getBusStop()))
                .append("nbGetOn", busStopLine.getNbGetOn())
                .append("nbGetOff", busStopLine.getNbGetOff())
                .append("time", busStopLine.getTime());

        ArrayList<Document> persons = new ArrayList<Document>();

        for (Person p : busStopLine.getGetOnPersons()) {
            //System.out.println("******* " + p.getId());
            persons.add(PersonConverter.toConstantDocument(p));
        }

        doc.append("getOnPersons", persons);

        return doc;
    }

    public static BusStopLine toBusStopLine(Document doc) {
        BusStopLine b = new BusStopLine();
        b.setBusStop((BusStop) BusStopConverter.toBusStop((Document) doc.get("busStop")));
        b.setNbGetOn((int) doc.get("nbGetOn"));
        b.setNbGetOff((int) doc.get("nbGetOff"));
        b.setTime((Date) doc.get("time"));

        List<Document> _persons = (List<Document>) doc.get("getOnPersons");
        ArrayList<Person> persons = new ArrayList<Person>();
        for (Document d : _persons) {
            persons.add(PersonConverter.toPerson(d));
        }
        b.setGetOnPersons(persons);

        return b;
    }

    public static JsonObject BusStopLineToJson(BusStopLine busStopLine) {
        JsonObject result = new JsonObject();
        JsonObject busStop = BusStopConverter.BusStopToJson(busStopLine.getBusStop());
        result.add("busStop", busStop);
        result.addProperty("nbGetOn", busStopLine.getNbGetOn());
        result.addProperty("nbGetOff", busStopLine.getNbGetOff());
        //TODO ADD DATE TO RESULT
        return result;
    }

}
