/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import modele.BusStop;
import modele.BusStopLine;
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
        
        return doc;
    }
    
    public static BusStopLine toBusStopLine(Document doc) {
        BusStopLine b = new BusStopLine();
        b.setBusStop((BusStop) BusStopConverter.toBusStop((Document)doc.get("busStop")));
        b.setNbGetOn((int) doc.get("nbGetOn"));
        b.setNbGetOff((int) doc.get("nbGetOff"));
        b.setTime((float) doc.get("time"));

        return b;
    }
}
