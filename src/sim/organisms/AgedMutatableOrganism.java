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
public abstract class AgedMutatableOrganism extends BaseOrganism
{
    private static final String BR = "BirthRate";
    private static final String BC = "BirthCount";
    private static final String BAS = "BirthAgeStart";
    private static final String BAE = "BirthAgeEnd";
    
    public static class AMDNA extends DNA
    {
        public AMDNA( int LE, int size, double br, double bc, double bas, double bae )
        {
            super(LE, size);
            set( BR, br );
            set( BC, bc );
            set( BAS, bas );
            set( BAE, bae );
        }
        
        @Override
        public boolean isValid()
        {
            double br = getValue(BR);
            double bc = getValue(BC);
            double bas = getValue(BAS);
            double bae = getValue(BAE);
            int LE = lifeExpectancy();
            return super.isValid() && (br>0&&br<1&&bc>0&&bas>0&&bas<LE);
        }
    }
    
    public AgedMutatableOrganism( DNA dna, Point l )
    {
        super( dna, l );
    }

    @Override
    protected int driftDistance()
    {
        return 1;
    }

    @Override
    protected int shouldReproduce(Environment env, int age)
    {
        if( age >= getDNAValue(BAS) && age < getDNAValue(BAE) )
        {
            return reproducutionHelper(env, getDNAValue( BR ), (int) getDNAValue( BC ));
        }
        return 0;
    }

    @Override
    protected Organism reproduce( Environment env )
    {
        DNA dna = getDNA().mutate( env );
        if( dna.isValid() )
        {
            return reproduce( dna, new Point( location() ) );
        }
        return null;
    }
    
    protected abstract Organism reproduce( DNA dna, Point l );
    
}
