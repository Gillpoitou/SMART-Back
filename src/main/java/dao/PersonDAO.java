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
import converter.BusConverter;
import converter.PersonConverter;
import java.util.ArrayList;
import modele.Bus;
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
        p.setId(id.toHexString());
        return p;
    }

    public Person getPersonById(String id) {
        Person p = (Person) PersonConverter.toPerson((Document) coll.find(eq("_id", new ObjectId(id))).first());
        return p;
    }

    public Person updatePerson(Person p) {
        Document doc = PersonConverter.toDocument(p);
        this.coll.replaceOne(
                eq("_id", new ObjectId(p.getId())),
                doc);
        return p;
    }

    public void deletePerson(Person p) {
        System.out.println("delete person : " + p.getId());
        this.coll.deleteOne(
                eq("_id", new ObjectId(p.getId())));
    }

    public ArrayList<Person> selectAllPersons() {
        ArrayList<Person> allPersons = new ArrayList<Person>();

        FindIterable<Document> personDocs = coll.find();
        for (Document personDoc : personDocs) {
            Person person = PersonConverter.toPerson(personDoc);
            allPersons.add(person);
        }

        return allPersons;
    }
    
    public boolean deleteAllPersons(){
        this.coll.drop();
        return true;
    }

}
