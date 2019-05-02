/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import converter.BusStopConverter;
import modele.BusStop;
import org.bson.Document;
import org.bson.types.ObjectId;


/**
 *
 * @author etien
 */
public class BusStopDAO {
    
    public BusStop getBusStopById(String id){
       return null; 
    }
    
      private MongoCollection<Document> coll;

    public BusStopDAO(MongoClient mongo) {
        this.coll = mongo.getDatabase("optibus").getCollection("BusStops");
    }

    public BusStop createBusStop(BusStop busStop) {
        Document doc = BusStopConverter.toDocument(busStop);
        this.coll.insertOne(doc);
        ObjectId id = (ObjectId) doc.get("_id");
        busStop.setId(id.toHexString());
        return busStop;
    }
}