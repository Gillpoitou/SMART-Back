/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import converter.BusConverter;
import converter.PersonConverter;
import modele.Person;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author elise
 */
public class PersonDAO {
   private MongoCollection<Document> coll;

    public PersonDAO(MongoClient mongo) {
        this.coll = mongo.getDatabase("optibus").getCollection("Persons");
    }

    public Person createPerson(Person p) {
        Document doc = PersonConverter.toDocument(p);
        this.coll.insertOne(doc);
        ObjectId id = (ObjectId) doc.get("_id");
        p.setId(id);
        return p;
    } 
  
  public void insertPerson(Person person){
        
    }

}
