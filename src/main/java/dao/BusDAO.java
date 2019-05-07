/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import com.mongodb.client.FindIterable;
import modele.Bus;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.Projections;
import static com.mongodb.client.model.Projections.exclude;
import converter.BusConverter;
import converter.BusStopConverter;
import java.util.ArrayList;
import java.util.Vector;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author thomasmalvoisin
 */
public class BusDAO {

    private MongoCollection<Document> coll;

    public BusDAO(MongoClient mongo) {
        this.coll = mongo.getDatabase("optibus").getCollection("Bus");
    }

    public Bus createBus(Bus bus) {
        Document doc = BusConverter.toDocument(bus);
        this.coll.insertOne(doc);
        ObjectId id = (ObjectId) doc.get("_id");
        bus.setId(id.toHexString());
        return bus;
    }

    public Bus getBusById(String id) {
        Bus b = (Bus) BusConverter.toBus((Document) coll.find(eq("_id", new ObjectId(id))).first());
        return b;
    }

    public ArrayList<Bus> selectAllBus() {
        ArrayList<Bus> allBus = new ArrayList<Bus>();

        FindIterable<Document> busDocs = coll.find();
        for (Document busDoc : busDocs) {
            System.out.println(busDoc);
            Bus bus = BusConverter.toBus(busDoc);
            allBus.add(bus);
        }

        return allBus;
    }
    
    public Bus updateBus(Bus bus){
        this.coll.replaceOne(eq("_id", new ObjectId(bus.getId())),
                BusConverter.toDocument(bus)
                );
        
        return bus;
    }

}
