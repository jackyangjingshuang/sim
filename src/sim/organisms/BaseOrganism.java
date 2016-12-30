/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sim.organisms;

import sim.ifc.DNA;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Random;
import sim.ifc.Environment;
import sim.ifc.Organism;

/**
 *
 * @author Jack
 */
public abstract class BaseOrganism implements Organism
{
    private int m_age;
    private DNA m_dna;
    private Point m_location;
    
    protected BaseOrganism( DNA dna, Point location )
    {
        m_dna = dna;
        m_location = location;
        m_age = 0;
    }
    
    @Override
    public DNA getDNA()
    {
        return m_dna;
    }
    
    protected double getDNAValue( String key )
    {
        return m_dna.getValue( key );
    }
    public int age()
    {
        return m_age;
    }

    public int lifeExpectancy()
    {
        return m_dna.lifeExpectancy();
    }
    @Override
    public boolean isAlive()
    {
        return m_age<lifeExpectancy();
    }

    @Override
    public int size()
    {
        return m_dna.size();
    }

    @Override
    public Point location()
    {
        return m_location;
    }
    
    public void drift(Environment env, int howMuch)
    {
        Point p = m_location;
        Random rand = env.getRNG();
        Dimension d = env.getDimension();
        
        p.x += (rand.nextInt(howMuch*2+1)-howMuch);
        if( p.x < 0 )
        {
            p.x = 0;
        }
        if( p.x > d.width-1 )
        {
            p.x = d.width-1;
        }
        
        p.y += (rand.nextInt(howMuch*2+1)-howMuch);
        if( p.y < 0 )
        {
            p.y = 0;
        }
        if( p.y > d.height-1 )
        {
            p.y = d.height-1;
        }
    }
    
    /**
     * returns number of offsprings you should reproduce according to 
     * the inputs
     * @param chanceOfBirth
     * @param howMany
     * @return 
     */
    public int reproducutionHelper( Environment env, double chanceOfBirth, int maxKids )
    {
        Random rand = env.getRNG();
        if( rand.nextDouble() < chanceOfBirth && maxKids > 0 )
        {
            return rand.nextInt( maxKids )+1;
        }
        else
        {
            return 0;
        }
    }

    @Override
    public Organism[] action( Environment env )
    {
        m_age ++;
        if( isAlive() )
        {
            drift( env, driftDistance() );
            int age = age();
            int n = shouldReproduce( env, age );
            if( n > 0 )
            {
                Organism[] ret = new Organism[n];
                for( int i=0; i<n; i++ )
                {
                    // reproduction can fail, which should return a null
                    ret[i] = reproduce( env );
                }
                return ret;
            }
        }
        return null;
    }
    
    protected abstract int driftDistance();
    protected abstract int shouldReproduce( Environment env, int age );
    protected abstract Organism reproduce( Environment env );
}
