/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import converter.LineConverter;
import modele.Line;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author elise
 */
public class LineDAO {
    private MongoCollection<Document> coll;

    public LineDAO(MongoClient mongo) {
        this.coll = mongo.getDatabase("optibus").getCollection("Lines");
    }

    public Line createLine(Line l) {
        Document doc = LineConverter.toDocument(l);
        this.coll.insertOne(doc);
        ObjectId id = (ObjectId) doc.get("_id");
        l.setId(id.toHexString());
        return l;
    } 
}
