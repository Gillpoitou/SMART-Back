/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listener;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;

/**
 *
 * @author thomasmalvoisin
 */
@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            ServletContext ctx = sce.getServletContext();
            MongoClient mongoClient = MongoClients.create(ctx.getInitParameter("MONGODB_STRING_CONNEXION"));
            System.out.println("MongoClient initialized successfully");
            sce.getServletContext().setAttribute("MONGO_CLIENT", mongoClient);
        } catch (Exception e){
            throw new RuntimeException("MongoClient init failed");
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        MongoClient mongo = (MongoClient) sce.getServletContext()
                .getAttribute("MONGO_CLIENT");
        mongo.close();
        System.out.println("MongoClient closed successfully");
    }

}
