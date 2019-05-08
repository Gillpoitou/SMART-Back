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
            System.out.println(d);

            result.add(LineConverter.toLine(d));

        }

        return result;
    }

    public Line retrieveLineByBusId(String busId) {
        Document doc = coll.find(eq("bus._id", new ObjectId(busId))).first();
//        System.out.println(doc);
        Line line = null;
        if (doc != null) {
            line = LineConverter.toLine(doc);
        }
        return line;
    }

    public Line createLine(Line l) {
        Document doc = LineConverter.toDocument(l);
        this.coll.insertOne(doc);
        ObjectId id = (ObjectId) doc.get("_id");
        l.setId(id.toHexString());
        return l;
    }

    public Line updateLine(Line line) {
        Document doc = LineConverter.toDocument(line);
        this.coll.replaceOne(
                eq("_id", new ObjectId(line.getId())),
                doc);
        
        return line;
    }

    public void deleteLine(Line l) {
        this.coll.deleteOne(
                eq("_id", new ObjectId(l.getId())));
    }

    public void deleteAll() {
        this.coll.drop();
    }
}
