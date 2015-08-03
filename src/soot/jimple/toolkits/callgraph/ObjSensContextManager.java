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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import soot.*;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.NoContext;
import soot.jimple.spark.pag.ObjectSensitiveAllocNode;
import soot.jimple.spark.pag.ObjectSensitiveConfig;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.StaticInitContext;
import soot.util.queue.QueueReader;

/** A context manager which creates an object-sensitive call graph.
 * @author Ondrej Lhotak
 */
public class ObjSensContextManager implements ContextManager 
{ 
    private CallGraph cg;
    private PAG pag;
    private Map<MethodOrMethodContext, Integer> apiCallDepthMap;
    
    public ObjSensContextManager( CallGraph cg, PAG pag ) {
        this.cg = cg;
        this.pag = pag;
        apiCallDepthMap = new HashMap<MethodOrMethodContext, Integer>();
        
        //initialize apiCallDepth with reachable entry point methods
        QueueReader<MethodOrMethodContext> reachables = Scene.v().getReachableMethods().listener();
        while (reachables.hasNext()) {
            apiCallDepthMap.put(reachables.next(), 0);
        }
       
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
        
        if (ObjectSensitiveConfig.v().apiCallDepth() < 0 || checkAPICallDepth(src, target, typeContext)) {
            Edge edge = new Edge( src, srcUnit, MethodContext.v(target, typeContext), kind );
            //System.out.println("Adding static edge: " + edge);
            cg.addEdge( edge );
        }
    }

    public void addVirtualEdge( MethodOrMethodContext src, Unit srcUnit, SootMethod target, Kind kind, Context typeContext ) {
        if (ObjectSensitiveConfig.isObjectSensitive() && typeContext == null)
            throw new RuntimeException("With object sensitive context should never be null!");      
        
        if (ObjectSensitiveConfig.v().apiCallDepth() < 0 || checkAPICallDepth(src, target, typeContext)) {
            Edge edge = new Edge( src, srcUnit, MethodContext.v( target, typeContext ), kind );
            // System.out.println("Adding virtual edge: " + edge);
            cg.addEdge( edge );
        }
      
    }

    public CallGraph callGraph() { return cg; }
    
    private boolean isAppMethod(SootMethod method, Context context) {
        if (context instanceof ObjectSensitiveAllocNode) {
            //virtual call
            
            ObjectSensitiveAllocNode rec = (ObjectSensitiveAllocNode)context;
            if (rec.getType() instanceof RefType) {
                SootClass containerClass = ((RefType)rec.getType()).getSootClass();
                //no matter the depth if the target is an app class, then return true
                if (ObjectSensitiveConfig.v().isAppClass(containerClass))
                    return true;
            }                     
            
        } else {
            //not a virtual call, check class of target
            if (ObjectSensitiveConfig.v().isAppClass(method.getDeclaringClass())) 
                return true;                     
        }
        
        return false;
    }
   
    
    private boolean checkAPICallDepth(MethodOrMethodContext src, SootMethod tgt, Context tgtContext) {
        if (ObjectSensitiveConfig.v().apiCallDepth() < 0)
            return true;
        
        MethodOrMethodContext tgtMC = MethodContext.v(tgt, tgtContext);
        
        //found app method
        if (isAppMethod(tgt, tgtContext)) {
           apiCallDepthMap.put(tgtMC, 0);
           return true;
        }
        
        if (ObjectSensitiveConfig.v().apiCallDepth() > 0) {
            return checkDepth(src, tgtMC);
        } else 
            return false;        
    }
    
    private boolean checkDepth(MethodOrMethodContext src, MethodOrMethodContext tgt) {
        int prevTgtDepth = apiCallDepthMap.containsKey(tgt) ? apiCallDepthMap.get(tgt) : Integer.MAX_VALUE;
        int fromSrcTgtDepth = apiCallDepthMap.get(src) + 1;
        
        if (fromSrcTgtDepth < prevTgtDepth) 
            apiCallDepthMap.put(tgt, fromSrcTgtDepth);
        
        return apiCallDepthMap.get(tgt).intValue() <= ObjectSensitiveConfig.v().apiCallDepth();      
    }
}

