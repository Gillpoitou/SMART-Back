/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import converter.WarehouseConverter;
import modele.Warehouse;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author elise
 */
public class WarehouseDAO {
    private MongoCollection<Document> coll;

    public WarehouseDAO(MongoClient mongo) {
        this.coll = mongo.getDatabase("optibus").getCollection("Warehouses");
    }

    public Warehouse createWarehouse(Warehouse w) {
        Document doc = WarehouseConverter.toDocument(w);
        this.coll.insertOne(doc);
        ObjectId id = (ObjectId) doc.get("_id");
        w.setId(id.toHexString());
        return w;
    } 
}
