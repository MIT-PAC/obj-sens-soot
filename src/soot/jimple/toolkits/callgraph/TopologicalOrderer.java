/* Soot - a J*va Optimization Framework
 * Copyright (C) 2003 Ondrej Lhotak
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */

/*
 * Modified by the Sable Research Group and others 1997-1999.  
 * See the 'credits' file distributed with Soot for the complete list of
 * contributors.  (Soot is distributed at http://www.sable.mcgill.ca/soot)
 */


package soot.jimple.toolkits.callgraph;

import soot.*;
import soot.util.*;
import java.util.*;


public class TopologicalOrderer
{
    CallGraph cg;
    List<MethodOrMethodContext> order = new ArrayList<MethodOrMethodContext>();
    HashSet<MethodOrMethodContext> visited;
    
    public TopologicalOrderer( CallGraph cg ) {
        this.cg = cg;
    }

    public void go() {
        visited = new HashSet<MethodOrMethodContext>(cg.numSources());
        Iterator<MethodOrMethodContext> methods = cg.sourceMethods();
        while( methods.hasNext() ) {
            MethodOrMethodContext m = (MethodOrMethodContext) methods.next();
            dfsVisit( m );
        }
    }

    private void dfsVisit( MethodOrMethodContext m ) {
        if( visited.contains( m ) ) return;
        visited.add( m );
        Iterator<MethodOrMethodContext> targets = new Targets( cg.edgesOutOf(m) );
        while( targets.hasNext() ) {
            MethodOrMethodContext target = (MethodOrMethodContext) targets.next();
            dfsVisit( target );
        }
        order.add( m );
    }

    public List<MethodOrMethodContext> order() { return order; }
}
