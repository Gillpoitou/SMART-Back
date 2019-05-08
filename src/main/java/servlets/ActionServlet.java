/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoClient;
import converter.BusConverter;
import converter.PersonConverter;
import dao.BusDAO;
import dao.BusStopDAO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modele.Bus;
import modele.BusStop;
import modele.Person;
import services.Services;

/**
 *
 * @author etien
 */
public class ActionServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println(request.getParameter("action"));
        String data;

        MongoClient mongoClient = (MongoClient) request.getServletContext()
                .getAttribute("MONGO_CLIENT");
        if (request.getParameter("action") == null) {
            response.sendError(400, "Bad Request, the current request has no action parameter");
            return;
        }
        switch (request.getParameter("action")) {
            case "getBusMapDisplay":
                response.setContentType("application/json");
                try (PrintWriter out = response.getWriter()) {
                    out.println(Services.getBusMapDisplay());
                }
                break;
            case "postBusRequest":
                response.setContentType("text");
                StringBuilder buffer = new StringBuilder();
                BufferedReader reader = request.getReader();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                data = buffer.toString();
                try {
                    Person person = PersonConverter.jsonToPerson(mongoClient, data);
                    int personCounter = (int)request.getServletContext().getAttribute("PERSON_COUNTER");
                    Date lastRequestDate = (Date)request.getServletContext().getAttribute("LAST_REQUEST_DATE");
                    int maxRequestNb = (int)request.getServletContext().getAttribute("MAX_REQUEST_NB");
                    long maxTimeInterval = (long)request.getServletContext().getAttribute("REQUEST_TIME_INTERVAL");
                    
                    if (Services.postBusRequest(mongoClient, person, personCounter, lastRequestDate, maxRequestNb,maxTimeInterval)) {

                        try (PrintWriter out = response.getWriter()) {
                            out.println("Request Posted");
                        }
                        request.getServletContext().setAttribute("PERSON_COUNTER", personCounter+1);
                        request.getServletContext().setAttribute("LAST_REQUEST_DATE", new Date());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendError(422, "Unprocessable entity");
                }
                break;
            case "initDataBase":
                if(request.getParameter("key")== null || !request.getParameter("key").equals("iamanadministrator")){
                    try (PrintWriter out = response.getWriter()) {
                        out.println("Access denied !");
                        response.setStatus(401);
                    }
                    return;
                }
                if (Services.initDataBase(mongoClient)) {
                    try (PrintWriter out = response.getWriter()) {
                        out.println("DB initilized");
                    }
                } else {
                    try (PrintWriter out = response.getWriter()) {
                        out.println("Initialization Error");
                    }
                }
                break;
            case "initDBTravel":
                if(request.getParameter("key")== null || !request.getParameter("key").equals("iamanadministrator")){
                    try (PrintWriter out = response.getWriter()) {
                        out.println("Access denied !");
                        response.setStatus(401);
                    }
                    return;
                }
                if (Services.initDBTravel(mongoClient)) {
                    try (PrintWriter out = response.getWriter()) {
                        out.println("DB initilized");
                    }
                } else {
                    try (PrintWriter out = response.getWriter()) {
                        out.println("Initialization Error");
                    }
                }
                break;
            case "getBusStops":
                response.setContentType("application/json");
                JsonObject result = new JsonObject();
                if (Services.getBusStops(mongoClient, result)) {
                    try (PrintWriter out = response.getWriter()) {
                        out.println(result);
                    }
                } else {
                    try (PrintWriter out = response.getWriter()) {
                        out.println("Error");
                    }
                }
            break;
            case "getBusLines":
               response.setContentType("application/json");
                JsonObject resultLines = new JsonObject();
                if(Services.getBusLines(mongoClient,resultLines)){
                   try (PrintWriter out = response.getWriter()){
                            out.println(resultLines);
                        } 
                }else{
                    try (PrintWriter out = response.getWriter()){
                            out.println("Error");
                        } 
                } 
            break;
            case "test":
                Services.test(mongoClient);
                break;
            case "createBus":
                data = this.parsePostBody(request.getReader());
                
                if (Services.createBus(mongoClient, data)) {
                    try (PrintWriter out = response.getWriter()) {
                        out.println("Bus created");
                    }
                } else {
                    try (PrintWriter out = response.getWriter()) {
                        out.println("Error");
                    }
                }
                break;
            case "startSimulation":
                 data = this.parsePostBody(request.getReader());
                 JsonElement jelement = new JsonParser().parse(data);
                 JsonObject jobject = jelement.getAsJsonObject();
                 JsonObject simulation = jobject.get("simulation").getAsJsonObject();
                 System.out.println(simulation);
                if(Services.startSimulation(simulation,request.getServletContext())){
                    try (PrintWriter out = response.getWriter()) {
                        out.println("Simulation started");
                    }
                }
                break;
            default:
                response.sendError(422, "Unprocessable entity, please specify a valid action type ");
                break;
        }
    }

    private String parsePostBody(BufferedReader reader) throws IOException {
        StringBuilder buffer = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String data = buffer.toString();

        return data;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
