/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.ws.rs.core.HttpHeaders.USER_AGENT;
import modele.Simulation;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author etien
 */
public class CalculSimulationThread extends Thread {

    Simulation simulation;
    double[] frequencies;

    public CalculSimulationThread(Simulation simulation) {
        this.simulation = simulation;
        frequencies = new double[this.simulation.getBusStopsRatios().size()];
        for (int i = 0; i < frequencies.length; i++) {
            frequencies[i] = simulation.getBusStopsRatios().get(i).getFrquency();
        }
    }

    @Override
    public void run() {

        System.out.println("thread id : " + this.getId());
        //number of call in a blocks of 2 secondes
        int calls = Math.round(simulation.getNumberTravelers() / 1800);
        if (calls <= 0) {
            calls = 1;
        }

        Date startingDate = new Date();
        Date currentDate = new Date();
        while (currentDate.getTime() < startingDate.getTime() + 600000) {

            for (int i = 0; i < calls; i++) {
                String idDpt = "";
                String idArr = "";

                //while the random departure is equal to random arrival we chose others bus stops
                while (idDpt.equals(idArr)) {
                    System.out.println(idDpt);
                    System.out.println(idArr);
                    //pick a random departure
                    double randomDpt = Math.random();
                    double currentCumulatedFrequency = 0;
                    for (int j = 0; j < frequencies.length; j++) {
                        currentCumulatedFrequency += frequencies[j];
                        if (randomDpt < currentCumulatedFrequency) {
                            idDpt = simulation.getBusStopsRatios().get(j).getId();
                            break;
                        }
                    }

                    //pick a random arrival
                    double randomArr = Math.random();
                    currentCumulatedFrequency = 0;
                    for (int j = 0; j < frequencies.length; j++) {
                        currentCumulatedFrequency += frequencies[j];
                        if (randomArr < currentCumulatedFrequency) {
                            idArr = simulation.getBusStopsRatios().get(j).getId();
                            break;
                        }
                    }

                }

                JsonObject body = new JsonObject();
                JsonObject person = new JsonObject();

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                Date date = new Date();
                person.addProperty("departure_date", sdf.format(date));
                person.addProperty("departure", idDpt);
                person.addProperty("arrival", idArr);
                body.add("person", person);

                post("http://localhost:8080/OptiBus_Back/ActionServlet?action=postBusRequest", body.toString());
                System.out.println("bus request posted");
            }
            currentDate = new Date();
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException ex) {
                Logger.getLogger(CalculSimulationThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Simulation finished");
        this.interrupt();
    }

    private void post(String completeUrl, String body) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(completeUrl);
        httpPost.setHeader("Content-type", "application/json");
        try {
            StringEntity stringEntity = new StringEntity(body);
            httpPost.getRequestLine();
            httpPost.setEntity(stringEntity);

            httpClient.execute(httpPost);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
