/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listener;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import java.util.Date;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import services.Services;

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

            MongoClientSettings settings = MongoClientSettings.builder()
                    .addCommandListener(new MongoCommandListener())
                    .applyConnectionString(new ConnectionString(ctx.getInitParameter("MONGODB_STRING_CONNEXION")))
                    .build();
            
            MongoClient mongoClient = MongoClients.create(settings);
            System.out.println("MongoClient initialized successfully");
            sce.getServletContext().setAttribute("MONGO_CLIENT", mongoClient);

            int personCounter = 0;
            sce.getServletContext().setAttribute("PERSON_COUNTER", personCounter);
            Date lastBusRequestDate = new Date();
            sce.getServletContext().setAttribute("LAST_REQUEST_DATE", lastBusRequestDate);
            int maxRequestNb = 5;
            sce.getServletContext().setAttribute("MAX_REQUEST_NB", maxRequestNb);
            long requestTimeInterval = 1800000;
            sce.getServletContext().setAttribute("REQUEST_TIME_INTERVAL", requestTimeInterval);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("MongoClient init failed or initialization failed");
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        MongoClient mongo = (MongoClient) sce.getServletContext()
                .getAttribute("MONGO_CLIENT");
        Thread t = (Thread)sce.getServletContext().getAttribute("SIMULATION_THREAD");
        t.interrupt();
        mongo.close();
        
        System.out.println("MongoClient closed successfully");
    }

}
