/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

/**
 *
 * @author etien
 */
public class SimulationRatio{
    private String id; //ID of coresponding BusStop
    private double frquency;
    
    public SimulationRatio(String id, double frquency) {
        this.id = id;
        this.frquency = frquency;
    }

    public String getId() {
        return id;
    }

    public double getFrquency() {
        return frquency;
    }
    
}
