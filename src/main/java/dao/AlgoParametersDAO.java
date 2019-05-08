/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.eq;
import converter.AlgoParametersConverter;
import modele.AlgoParameters;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author elise
 */
public class AlgoParametersDAO {
    private MongoCollection<Document> coll;

    public AlgoParametersDAO(MongoClient mongo) {
        this.coll = mongo.getDatabase("optibus").getCollection("AlgoParameters");
    }

    public AlgoParameters createAlgoParameters(AlgoParameters aP) {
        Document doc = AlgoParametersConverter.toDocument(aP);
        this.coll.insertOne(doc);
        ObjectId id = (ObjectId) doc.get("_id");
        aP.setId(id.toHexString());
        return aP;
    }

    public AlgoParameters updateAlgoParameters(AlgoParameters aP) {
        Document doc = AlgoParametersConverter.toDocument(aP);
        this.coll.updateOne(
                eq("_id", new ObjectId(aP.getId())),
                doc);
        return aP;
    }
    
    public AlgoParameters getParameters(){
        Document doc = this.coll.find().first();
        
        return AlgoParametersConverter.toAlgoParameters(doc);
    }
}
