/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sim.organisms;

import java.awt.Point;
import java.util.Set;
import sim.ifc.DNA;
import sim.ifc.Environment;
import sim.ifc.Organism;

/**
 *
 * @author Jack
 */
public class Fish extends AgedMutatableOrganism
{
    private static final String EAT_RATE = "EatRate";
    
    private static class MyDNA extends AgedMutatableOrganism.AMDNA
    {
        public MyDNA()
        {
            super(50, 2, 0.1, 2, 5, 35);
            set( EAT_RATE, 0.2 );
        }
        
        @Override
        public boolean isValid()
        {
            double er = getValue(EAT_RATE);
            return super.isValid() && er>0 && er<1;
        }
    }
   
    public Fish( Environment env )
    {
        super( new MyDNA(), env.getRandomLocation() );
    }
    
    public Fish( DNA dna, Point l )
    {
        super( dna, l );
    }

    @Override
    protected int driftDistance()
    {
        return 3;
    }

    @Override
    protected Organism reproduce( DNA dna, Point l )
    {
        return new Fish( dna, l );
    }

    @Override
    public Organism[] action( Environment env )
    {
        // first we deal with our stomach
        if( env.getRNG().nextDouble() < getDNA().getValue(EAT_RATE) )
        {
            Set<Organism> stuff = env.findStuff( location(), size() + 3 );
            boolean satisfied = false;
            for( Organism o : stuff )
            {
                if( o instanceof Algea )
                {
                    if( o.size() > 0 )
                    {
                        //great, found food
                        if( env.kill( this, o ) )
                        {
                            satisfied = true;
                            break;
                        }
                    }
                }
            }
            if( ! satisfied )
            {
                env.kill( this, this );  // then we starve to death
                return null;
            }
        }
        return super.action( env );
    }
    
}
