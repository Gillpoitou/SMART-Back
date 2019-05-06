/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.eq;
import converter.LineConverter;
import java.util.LinkedList;
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

    public LinkedList<Line> retrieveAll() {
        LinkedList<Line> result = new LinkedList<Line>();

        FindIterable<Document> lines = coll.find();
        for (Document d : lines) {
            result.add(LineConverter.toLine(d));
        }

        return result;
    }

    public Line createLine(Line l) {
        Document doc = LineConverter.toDocument(l);
        this.coll.insertOne(doc);
        ObjectId id = (ObjectId) doc.get("_id");
        l.setId(id.toHexString());
        return l;
    }

    public Line updateLine(Line l) {
        Document doc = LineConverter.toDocument(l);
        this.coll.updateOne(
                eq("_id", new ObjectId(l.getId())),
                doc);
        return l;
    }

    public void deleteLine(Line l) {
        this.coll.deleteOne(
                eq("_id", new ObjectId(l.getId())));
    }
}
