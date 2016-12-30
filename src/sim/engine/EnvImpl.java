/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sim.engine;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import sim.ifc.Environment;
import sim.ifc.Organism;

/**
 *
 * @author Jack
 */
public class EnvImpl implements Environment
{
    private Random m_rand = new Random();
    private Dimension m_dimension;
    private final Set<Organism> m_stuff = new HashSet<>();
    private final Set<Organism>[][] m_map;
    private final Set<Organism> m_killed = new HashSet<>();
    
    public EnvImpl( int width, int height )
    {
        m_dimension = new Dimension(width, height);
        m_map = new Set[width][height];
        for( int i = 0; i<width; i++ )
        {
            for( int j = 0; j<height; j++ )
            {
                m_map[i][j] = new HashSet<Organism>();
            }
        }
    }

    public void add( Organism o )
    {
        m_stuff.add( o );
        Point p = o.location();
        m_map[p.x][p.y].add( o );
    }
    
    private void remove( Organism o )
    {
        m_stuff.remove( o );
        m_map[o.location().x][o.location().y].remove( o );
    }    
    
    public Set<Organism> getStuff()
    {
        return m_stuff;
    }
    
    @Override
    public Random getRNG()
    {
        return m_rand;
    }

    @Override
    public Dimension getDimension()
    {
        return m_dimension;
    }

    @Override
    public Point getRandomLocation()
    {
        return new Point( m_rand.nextInt( m_dimension.width ), m_rand.nextInt( m_dimension.height ) );
    }

    @Override
    public Set<Organism> findStuff(Point location, int range)
    {
        Set<Organism> ret = new HashSet<>();
        int x = location.x;
        int y = location.y;
        for( int i = x-range; i<x+range; i++ )
        {
            for( int j=y-range; j<y+range; j++ )
            {
                if( i >= 0 && j >= 0 && i<m_dimension.width && j<m_dimension.height )
                {
                    ret.addAll( m_map[i][j] );
                }
            }
        }
        return ret;
    }

    @Override
    public boolean kill(Organism killer, Organism target)
    {
        // for now we always allow.
        m_killed.add( target );
        return true;
    }

    private void assertMapLocation( Organism o )
    {
        Point p = o.location();
        if( ! m_map[p.x][p.y].contains( o ) )
        {
            Point pos = null;
            for( int i = 0; i<m_dimension.width; i++ )
            {
                for( int j = 0; j<m_dimension.height; j++ )
                {
                    if( m_map[i][j].contains( o ) )
                    {
                        pos = new Point( i, j );
                    }
                }
            }
            throw new RuntimeException("Location mismatch: organism thinks its at " + p + " but we found it at " + pos );
        }
    }

    
    public void run( String msg )
    {
        List<Organism[]> newStuffArr = new ArrayList<>();
        Set<Organism> justDied = new HashSet<>();
        int startPopulation = m_stuff.size();
        
        for( Organism org : m_stuff )
        {
            assertMapLocation( org );
            Point oldLocation = new Point( org.location());
            Organism[] newStuff = org.action( this );
            Point newLocation = org.location();
            if( oldLocation.x != newLocation.x || oldLocation.y != newLocation.y )
            {
                m_map[oldLocation.x][oldLocation.y].remove( org );
                m_map[newLocation.x][newLocation.y].add( org );
                assertMapLocation( org );
            }
            
            if( newStuff != null )
            {
                newStuffArr.add( newStuff );
            }
            if( ! org.isAlive() )
            {
                justDied.add( org );
            }
        }
        int killed = m_killed.size();
        int aged = justDied.size();
        
        // now clean up the crowded area
        for( int i = 0; i<m_dimension.width; i++ )
        {
            for( int j = 0; j<m_dimension.height; j++ )
            {
                if( m_map[i][j].size() > 5 )
                {
                    justDied.addAll( m_map[i][j] );
                }
            }
        }
        int crowded = justDied.size() - aged;
        justDied.addAll( m_killed );
        m_killed.clear();
        
        for( Organism o : justDied )
        {
            remove( o );
        }
        
        int n = 0;
        for( Organism[] orgs : newStuffArr )
        {
            for( Organism o : orgs )
            {
                if( o != null )
                {
                    add( o );
                    assertMapLocation( o );
                    n++;
                }
            }
        }
        
        if( msg != null )
        {
            System.out.println(msg + ": start " + startPopulation + ", aged: " + aged + ", killed: " + killed + ", crowded: " + crowded + " (Total Died: " + justDied.size() + "), newborn: " + n + ": end population: " + m_stuff.size() );
            Map<Class,Integer> species = new HashMap<>();
            for( Organism org : m_stuff )
            {
                Class c = org.getClass();
                Integer count = species.get( c );
                if( count == null )
                {
                    species.put( c, 1 );
                }
                else
                {
                    species.put( c, count+1 );
                }
            }

            for( Iterator<Map.Entry<Class,Integer>> iter = species.entrySet().iterator(); iter.hasNext(); )
            {
                Map.Entry<Class,Integer> entry = iter.next();
                System.out.println( "    " + entry.getKey().getSimpleName() + " : " + entry.getValue() );
            }
        }        
    
    }
}
