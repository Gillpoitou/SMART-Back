/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import com.mongodb.DBCursor;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Aggregates;
import java.util.Arrays;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Aggregates.addFields;
import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.limit;
import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Aggregates.sort;
import static com.mongodb.client.model.Accumulators.first;
import static com.mongodb.client.model.Projections.exclude;
import static com.mongodb.client.model.Sorts.ascending;
import com.mongodb.client.model.Field;
import com.mongodb.client.model.Field;
import static com.mongodb.client.model.Filters.eq;
import converter.BusStopConverter;
import java.util.Vector;
import modele.BusStop;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author etien
 */
public class BusStopDAO {

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

    public BusStop getBusStopById(String id) {
        BusStop busStop = (BusStop) BusStopConverter.toBusStop((Document) coll.find(eq("_id", new ObjectId(id))).first());
        return busStop;
    }

    public Vector<BusStop> selectBusStops() {
        Vector<BusStop> result = new Vector<BusStop>(50);
        FindIterable<Document> busStopDocs = coll.find();
        for (Document busStopDoc : busStopDocs) {
            BusStop busStop = BusStopConverter.toBusStop(busStopDoc);
            result.add(busStop);
            System.out.println(busStop.getName());
        }
        return result;
    }

    public Vector<BusStop> selectBusStopsGeoJson(double latitude, double longitude) {

        AggregateIterable<Document> aggregate = this.coll_geoJson.aggregate(
                Arrays.asList(
                        eq("$geoNear", and(
                                eq("near", and(
                                        eq("type", "Point"),
                                        eq("coordinates", Arrays.asList(latitude, longitude)))
                                ),
                                eq("num", "100000000"),
                                eq("maxDistance", "1000000000"),
                                eq("distanceField", "distance"))
                        ),
                        addFields(new Field("name", "$properties.nom")
                        ), project(
                                exclude("properties", "type")
                        ), group("$name", first("busStop", "$$CURRENT")),
                        sort(ascending("busStop.distance")),
                        limit(50)));

        Vector<BusStop> result = new Vector<BusStop>(50);
        int currentID = 0;
        MongoCursor<Document> iterator = aggregate.iterator();
        while (iterator.hasNext()) {
            Document next = iterator.next();
            BusStop currentBusStop = BusStopConverter.geoJsonToBusStop((Document) next.get("busStop"));
            currentBusStop.setBusStopID(currentID);
            System.out.println(next.toString());
            result.add(currentBusStop);
            currentID++;
        }

        return result;
    }
    
    public BusStop updateBusStop(BusStop busStop){
        this.coll.replaceOne(eq("_id", new ObjectId(busStop.getId())),
                BusStopConverter.toDocument(busStop)
                );
        
        return busStop;
    }
}
