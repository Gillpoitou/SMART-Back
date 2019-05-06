/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author thomasmalvoisin
 */
public class Path {

    private List<Coordinates> coordinates;

    public Path() {
        coordinates = new LinkedList();
    }
    
    public List<Coordinates> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Coordinates> coordinates) {
        this.coordinates = coordinates;
    }
    
    public void insertCoordinates(Coordinates coord){
        this.coordinates.add(coord);
    }
    
    public void insertCoordinates(double latitude,double longitude){
        Coordinates coord = new Coordinates(latitude,longitude);
        this.coordinates.add(coord);
    }
}
