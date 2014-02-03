package soot.jimple.spark.builder;

import java.util.Iterator;

import soot.G;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.spark.geom.geomPA.GeomPointsTo;
import soot.jimple.spark.internal.SparkNativeHelper;
import soot.jimple.spark.pag.MethodPAG;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.solver.OnFlyCallGraph;
import soot.jimple.toolkits.callgraph.CallGraphBuilder;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.ReachableMethods;
import soot.jimple.toolkits.pointer.DumbPointerAnalysis;
import soot.jimple.toolkits.pointer.util.NativeMethodDriver;
import soot.options.SparkOptions;
import soot.util.queue.QueueReader;

public class ObjectSensitiveBuilder extends PAGBuilder {

    public ObjectSensitiveBuilder() {
        
    }
    
    /** Creates an empty pointer assignment graph. */
    public PAG setup( SparkOptions opts ) {
        pag = opts.geom_pta() ? new GeomPointsTo( opts ) : new PAG( opts );
        if( opts.simulate_natives() ) {
            pag.nativeMethodDriver = new NativeMethodDriver(new SparkNativeHelper(pag));
        }
        if( opts.on_fly_cg() && !opts.vta() ) {
            ofcg = new OnFlyCallGraph( pag );
            pag.setOnFlyCallGraph( ofcg );
        } else {
            cgb = new CallGraphBuilder( DumbPointerAnalysis.v() );
        }
        return pag;
    }
    
    /** Fills in the pointer assignment graph returned by setup. */
    public void build() {
        QueueReader callEdges = null;
        if( ofcg != null ) {
            callEdges = ofcg.callGraph().listener();
            ofcg.build();
            reachables = ofcg.reachableMethods();
            reachables.update();
        } else {
            throw new RuntimeException("Not supported, must use ofcg");
        }
        
        for( Iterator cIt = Scene.v().getClasses().iterator(); cIt.hasNext(); ) {
            final SootClass c = (SootClass) cIt.next();
            handleClass( c );
        }
        
        //TODO: redundant???
        while(callEdges.hasNext()) {
            Edge e = (Edge) callEdges.next();
            if(!e.getTgt().method().getDeclaringClass().isPhantom()) {
                MethodPAG.v( pag, e.tgt(), e.tgtCtxt() ).addToPAG(e.tgtCtxt());
                pag.addCallTarget( e );
            }
        }
    }

    protected void handleClass( SootClass c ) {
        System.out.println("Called handleClass(), should be called once.");
        boolean incedClasses = false;
        Iterator methodsIt = c.methodIterator();
        while( methodsIt.hasNext() ) 
        {
            SootMethod m = (SootMethod) methodsIt.next();
            if( !m.isConcrete() && !m.isNative() ) continue;
            totalMethods++;
            if( reachables.contains( m ) ) {
                MethodPAG mpag = MethodPAG.v( pag, m, PAG.EMPTY_CONTEXT);
                mpag.build();
                mpag.addToPAG(null);
                analyzedMethods++;
                if( !incedClasses ) {
                    incedClasses = true;
                    classes++;
                }
            }
        }
    }
  
    private PAG pag;
    private CallGraphBuilder cgb;
    private OnFlyCallGraph ofcg;
    private ReachableMethods reachables;
    int classes = 0;
    int totalMethods = 0;
    int analyzedMethods = 0;
    int stmts = 0;

}