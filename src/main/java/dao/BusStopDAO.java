/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Aggregates;
import java.util.Arrays;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Aggregates.addFields;
import static com.mongodb.client.model.Aggregates.limit;
import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Aggregates.sort;
import static com.mongodb.client.model.Projections.exclude;
import static com.mongodb.client.model.Sorts.ascending;
import com.mongodb.client.model.Field;
import com.mongodb.client.model.Field;
import static com.mongodb.client.model.Filters.eq;
import converter.BusStopConverter;
import java.util.Arrays;
import java.util.Vector;
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
      private MongoCollection<Document> coll_geoJson;

    public BusStopDAO(MongoClient mongo) {
        this.coll = mongo.getDatabase("optibus").getCollection("BusStops");
        this.coll_geoJson = mongo.getDatabase("optibus").getCollection("geoJson");
    }

    public BusStop createBusStop(BusStop busStop) {
        Document doc = BusStopConverter.toDocument(busStop);
        this.coll.insertOne(doc);
        ObjectId id = (ObjectId) doc.get("_id");
        busStop.setId(id.toHexString());
        return busStop;
    }
    
    public Vector<BusStop> selectBusStops(double latitude, double longitude){
      
        AggregateIterable<Document> aggregate = this.coll_geoJson.aggregate(Arrays.asList(eq("$geoNear", and(eq("near", and(eq("type", "Point"), eq("coordinates", Arrays.asList(latitude, longitude)))), eq("distanceField", "distance"))), sort(ascending("distance")), limit(50), addFields(new Field("name", "$properties.nom"), 
            new Field("id", "")), project(exclude("properties", "type")))
        );
        
        Vector<BusStop> result = new Vector<BusStop>();
        int currentID = 0;
        MongoCursor<Document> iterator = aggregate.iterator();
        while (iterator.hasNext()) {
        Document next = iterator.next();
            BusStop currentBusStop = BusStopConverter.geoJsonToBusStop(next);
            
            /*currentBusStop.setBusStopID(currentID);
            currentBusStop.setLatitude(next.getDouble("latitude"));*/
            //map.put(next.getString("_id"), next.getInteger("_count"));
            currentID++;
        }
        
        
        return null;
    }
}