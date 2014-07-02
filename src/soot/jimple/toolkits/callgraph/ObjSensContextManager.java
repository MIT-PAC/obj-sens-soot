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

package soot.jimple.toolkits.callgraph;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import soot.*;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.NoContext;
import soot.jimple.spark.pag.ObjectSensitiveAllocNode;
import soot.jimple.spark.pag.ObjectSensitiveConfig;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.StaticInitContext;

/** A context manager which creates an object-sensitive call graph.
 * @author Ondrej Lhotak
 */
public class ObjSensContextManager implements ContextManager 
{ 
    private CallGraph cg;
    private PAG pag;
    
    public ObjSensContextManager( CallGraph cg, PAG pag ) {
        this.cg = cg;
        this.pag = pag;
    }
    

    public void addStaticEdge( MethodOrMethodContext src, Unit srcUnit, SootMethod target, Kind kind, Context typeContext ) {
        if (ObjectSensitiveConfig.isObjectSensitive() && typeContext == null)
            throw new RuntimeException("With object sensitive context should never be null! " + src + " " + target);
        
        if (ObjectSensitiveConfig.isObjectSensitive() && kind.isClinit()) {
            SootClass cl = target.getDeclaringClass();
            
            if (cl == null)
                throw new RuntimeException("No declaring class for clinit when adding context!");
            
            if (ObjectSensitiveConfig.v().contextForStaticInits())    
                typeContext = StaticInitContext.v(target.getDeclaringClass());
            else
                typeContext = NoContext.v();
            
        } 
        
        cg.addEdge( new Edge( src, srcUnit, MethodContext.v(target, typeContext), kind ) );
      
    }

    public void addVirtualEdge( MethodOrMethodContext src, Unit srcUnit, SootMethod target, Kind kind, Context typeContext ) {
        if (ObjectSensitiveConfig.isObjectSensitive() && typeContext == null)
            throw new RuntimeException("With object sensitive context should never be null!");      
        
        cg.addEdge( new Edge( src, srcUnit, MethodContext.v( target, typeContext ), kind ) );
      
    }

    public CallGraph callGraph() { return cg; }
}

