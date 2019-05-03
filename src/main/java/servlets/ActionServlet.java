/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.google.gson.JsonObject;
import com.mongodb.client.MongoClient;
import converter.BusConverter;
import converter.PersonConverter;
import dao.BusDAO;
import dao.BusStopDAO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
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
                    if (Services.postBusRequest(mongoClient, person)) {

                        try (PrintWriter out = response.getWriter()) {
                            out.println("Request Posted");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendError(422, "Unprocessable entity");
                }
                break;
            case "initDataBase":
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
            case "createBus":
//                try {
//                    data = parsePostBody(request.getReader());
//                    //TODO:
////                    Bus bus = BusConverter.toBus(jsonToBus);
//                    Bus bus = new Bus();
//                    BusStop initPos = 
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    response.sendError(422, "Unprocessable entity");
//                }
//
//                Services.createBus(mongoClient);
                break;
            case "test":
                Services.test(mongoClient);
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
