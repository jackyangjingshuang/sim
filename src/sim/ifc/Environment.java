/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sim.ifc;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Jack
 */
public interface Environment
{
    public Random getRNG();
    public Dimension getDimension();
    public Point getRandomLocation();
    public Set<Organism> findStuff( Point location, int range );
    public boolean kill( Organism killer, Organism target );
}
