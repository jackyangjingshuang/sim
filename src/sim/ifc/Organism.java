/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sim.ifc;

import java.awt.Point;

/**
 *
 * @author Jack
 */
public interface Organism
{
    /**
     * are you still alive?
     * @return 
     */
    public boolean isAlive();
    
    public int size();
    public Point location();
    
    /**
     * This method is called by the engine to allow this organism to grow
     * by one day.
     * 
     * @return new organisms that it produces
     */
    public Organism[] action( Environment env );   
    
    public DNA getDNA();
}
