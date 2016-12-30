/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sim;

import sim.engine.Engine;
import sim.organisms.Algea;
import sim.organisms.Fish;

/**
 *
 * @author Jack
 */
public class Sim
{
    public static void main( String[] args )
    {
        Engine e = new Engine( 100, 100 );

        for( int i = 0; i<1000; i++ )
        {
            e.add( new Algea(e.getEnv()));
        }
        for( int i = 0; i<30; i++ )
        {
            e.add( new Fish(e.getEnv()));
        }

        e.run(500, 1);
    }
}
