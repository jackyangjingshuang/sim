/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sim.organisms;

import java.awt.Point;
import sim.ifc.DNA;
import sim.ifc.Environment;
import sim.ifc.Organism;

/**
 *
 * @author Jack
 */
public class Algea extends AgedMutatableOrganism
{
    private static class MyDNA extends AgedMutatableOrganism.AMDNA
    {
        public MyDNA()
        {
            super(20, 1, 0.1, 3, 5, 15);
        }
    }
   
    public Algea( Environment env )
    {
        super( new MyDNA(), env.getRandomLocation() );
    }
    
    public Algea( DNA dna, Point l )
    {
        super( dna, l );
    }

    @Override
    public int size()
    {
        if( age() < 5 )
            return 0;
        return 1;
    }
    
    @Override
    protected int driftDistance()
    {
        return 1;
    }

    @Override
    protected Organism reproduce( DNA dna, Point l )
    {
        return new Algea( dna, l );
    }
}
