/* Soot - a J*va Optimization Framework
 * Copyright (C) 2002,2003 Ondrej Lhotak
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

package soot.jimple.spark.solver;
import java.util.LinkedList;
import java.util.List;

import soot.jimple.spark.SparkTransformer;
import soot.jimple.spark.sets.*;
import soot.jimple.spark.pag.*;
import soot.jimple.toolkits.callgraph.*;
import soot.*;
import soot.util.queue.*;


/** The interface between the pointer analysis engine and the on-the-fly
 * call graph builder.
 * @author Ondrej Lhotak
 */

public class OnFlyCallGraph {
    private final OnFlyCallGraphBuilder ofcgb;
    private final ReachableMethods reachableMethods;
    private final QueueReader reachablesReader;
    private final QueueReader callEdges;
    private final CallGraph callGraph;

    public ReachableMethods reachableMethods() { return reachableMethods; }
    public CallGraph callGraph() { return callGraph; }

    /**
     * When object sensitive, create a list of reachable methods with NoContext,
     * and then install them in the scene!
     * 
     * @param cg
     */    
    private void resetReachableMethodsWithContext(CallGraph cg) {

        List<MethodOrMethodContext> withNoContext = new LinkedList<MethodOrMethodContext>();
        for (SootMethod method : Scene.v().getEntryPoints()) {
            withNoContext.add(MethodContext.v(method, EntryContext.v()));
            //System.out.println("Adding context to entry points: " + method);
        }

        ReachableMethods reachableMethods = new ReachableMethods(
            cg, withNoContext );


        Scene.v().setReachableMethods(reachableMethods);
    }

    public OnFlyCallGraph( PAG pag ) {
        this.pag = pag;
        callGraph = new CallGraph();
        Scene.v().setCallGraph( callGraph );
        //change reachable methods list of entry points to have method contexts with NoContext
        if (ObjectSensitiveConfig.isObjectSensitive())
            resetReachableMethodsWithContext(callGraph);

        reachableMethods = Scene.v().getReachableMethods();
        ContextManager cm = CallGraphBuilder.makeContextManager(callGraph, pag);
        ofcgb = new OnFlyCallGraphBuilder( pag, cm, reachableMethods );
        reachablesReader = reachableMethods.listener();
        callEdges = cm.callGraph().listener();
    }
    public void build() {
        ofcgb.processReachables();
        processReachables();
        processCallEdges();
    }

    //TODO: redundant??
    private void processReachables() {
        reachableMethods.update();
        while(reachablesReader.hasNext()) {
            MethodOrMethodContext m = (MethodOrMethodContext) reachablesReader.next();
            //SparkTransformer.println("OnFlyCallGraph.processReachables: " + m);
            MethodPAG mpag = MethodPAG.v( pag, m.method());
            mpag.build();
            Context context = m.context();
            if (ObjectSensitiveConfig.isObjectSensitive() && context == null) {
                throw new RuntimeException("context should not be null");
            }

            mpag.addToPAG(context);
        }
    }
    private void processCallEdges() {
        while(callEdges.hasNext()) {
            Edge e = (Edge) callEdges.next();
            //SparkTransformer.println("OnFlyCallGraph.processCallEdge: " + e);
            MethodPAG amp = MethodPAG.v( pag, e.tgt());
            amp.build();
            amp.addToPAG( e.tgtCtxt() );
            pag.addCallTarget( e );
        }
    }

    public OnFlyCallGraphBuilder ofcgb() { return ofcgb; }

    public void updatedNode( final VarNode vn ) {
        final Object r = vn.getVariable();

        /* LWG: removed the sanity check for efficiency (the violation should not happen when the p2sets are
         * correctly constructed
        //is the vn a this ref, if so, test its pt set!
        if (ObjectSensitiveConfig.isObjectSensitive() && (vn.isThisPtr() && vn instanceof ContextVarNode)) {
            PointsToSetInternal p2set = vn.getP2Set().getNewSet();
            final Context context = vn.context();
            p2set.forall( new P2SetVisitor() {
                public final void visit( Node n ) {
                    if (context instanceof NoContext) {
                        if (n instanceof ObjectSensitiveAllocNode && !((ObjectSensitiveAllocNode)n).noContext())
                            throw new RuntimeException("Failed this ref test: " + vn + " " + r + " " 
                                    + context + " " + n);            
                    } else {
                        if (!context.equals(n))
                            throw new RuntimeException("Failed this ref test: " + vn + " " + r + " " 
                                    + context + " " + n);                        
                    }
                }} );

        }
        */
        
        
        if( !(r instanceof Local) ) return;
        final Local receiver = (Local) r;
       
        PointsToSetInternal p2set = vn.getP2Set().getNewSet();
         
        if( ofcgb.wantTypes( vn ) ) {
            p2set.forall( new P2SetVisitor() {
                public final void visit( Node n ) {

                    ofcgb.addType( vn, n.getType(), (AllocNode) n , false);
                }} );
        }
        if( ofcgb.wantStringConstants( receiver ) ) {
            p2set.forall( new P2SetVisitor() {
                public final void visit( Node n ) {
                    if( n instanceof StringConstantNode ) {
                        String constant = ((StringConstantNode)n).getString();
                        ofcgb.addStringConstant( receiver, constant );
                    } else {
                        ofcgb.addStringConstant( receiver, null );
                    }
                }} );
        }
    }

    /** Node uses this to notify PAG that n2 has been merged into n1. */
    public void mergedWith( Node n1, Node n2 ) {
    }

    /* End of public methods. */
    /* End of package methods. */

    private PAG pag;
}



