/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

/**
 *
 * @author elise
 */
public class AlgoParameters {
    
    private String id;
    int maxRequestNb;
    long maxTimeInterval;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMaxRequestNb() {
        return maxRequestNb;
    }

    public void setMaxRequestNb(int maxRequestNb) {
        this.maxRequestNb = maxRequestNb;
    }

    public long getMaxTimeInterval() {
        return maxTimeInterval;
    }

    public void setMaxTimeInterval(long maxTimeInterval) {
        this.maxTimeInterval = maxTimeInterval;
    }
    
    
}
