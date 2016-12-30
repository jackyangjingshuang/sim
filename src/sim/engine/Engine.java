/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sim.engine;

import sim.ifc.Environment;
import sim.ifc.Organism;

/**
 *
 * @author Jack
 */
public class Engine 
{
    private EnvImpl m_env;
    
    public Engine( int width, int height )
    {
        m_env = new EnvImpl( width, height );
    }
    public Environment getEnv()
    {
        return m_env;
    }
    public void add( Organism o )
    {
        m_env.add( o );
    }
    
    public void run( int n, int report )
    {
        for( int i = 0; i<n; i++ )
        {
            String msg = null;
            if( i % report == 0 )
            {
                msg = "Round " + i;
            }
            m_env.run( msg );
        }
    }
}    

