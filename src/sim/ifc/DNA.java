/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sim.ifc;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * DNA represents the characteristics of a species.
 * Some characteristics are common for all species, and here we explicitly list
 * them.   Others are specific to certain species and here we use a generic
 * keys/value interface to represent them.
 * 
 * @author Jack
 */
public abstract class DNA
{
    protected final int m_lifeExpectancy, m_size;
    private final Map<String, Double> m_values = new HashMap<>();

    public DNA( int le, int size )
    {
        m_lifeExpectancy = le;
        m_size = size;
    }
    
    public int lifeExpectancy()
    {
        return m_lifeExpectancy;
    }
    public int size()
    {
        return m_size;
    }
    
    public Set<String> getKeys()
    {
        return m_values.keySet();
    }
    public double getValue( String key )
    {
        return m_values.get( key );
    }
    
    protected void set( String key, double value )
    {
        if( m_values.containsKey( key ) )
        {
            throw new RuntimeException("Cannot change DNA");
        }
        m_values.put( key, value);
    }
    
    public DNA mutate( Environment env )
    {
        Random r = env.getRNG();
        for( String key : getKeys() )
        {
            if( r.nextDouble() < 0.001 )  // 0.1% mutate chance
            {
                double v = getValue( key );
                v = v * ( 1 + (r.nextDouble() - 0.5 )/10 );   // drift by max of 10%
                m_values.put( key, v );
            }
        }
        return this;
    }
    
    public boolean isValid()
    {
        // only a mutated DNA could be invalid
        return true;
    }
}
