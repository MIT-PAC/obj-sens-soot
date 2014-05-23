package soot.jimple.spark.builder;

import java.util.Iterator;

import soot.G;
import soot.MethodContext;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.spark.geom.geomPA.GeomPointsTo;
import soot.jimple.spark.internal.SparkNativeHelper;
import soot.jimple.spark.pag.EntryContext;
import soot.jimple.spark.pag.MethodPAG;
import soot.jimple.spark.pag.NoContext;
import soot.jimple.spark.pag.ObjectSensitiveConfig;
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
            throw new RuntimeException("Improperly configured!");
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

        while(callEdges.hasNext()) {
            Edge e = (Edge) callEdges.next();
            if(!e.getTgt().method().getDeclaringClass().isPhantom()) {
                MethodPAG.v( pag, e.tgt() ).addToPAG(e.tgtCtxt());
                pag.addCallTarget( e );
            }
        }
    }


    //Called after entry points have been processed, plus any static calls each entry point makes
    //so reachables has these methods in it.
    protected void handleClass( SootClass c ) {
        //System.out.println("Called handleClass(), should be called once.");
        Iterator methodsIt = c.methodIterator();
        while( methodsIt.hasNext() ) 
        {
            SootMethod m = (SootMethod) methodsIt.next();
            if( !m.isConcrete() && !m.isNative() ) continue;
            totalMethods++;
            if(reachables.contains(MethodContext.v(m, EntryContext.v()))) {
                //System.out.println("handleClass EntryContext: " + m);
                MethodPAG mpag = MethodPAG.v( pag, m);
                if (!mpag.hasBeenBuilt()) {
                    System.out.println("mpag has not been built: " + m + " " + EntryContext.v());
                    mpag.build();
                } 
                
                if (!mpag.hasContextAdded(EntryContext.v())) {
                    mpag.addToPAG(EntryContext.v());
                    System.out.println("context not added to mpag: " + m + " " + EntryContext.v());
                }

            }  else if (reachables.contains( MethodContext.v(m, NoContext.v()))) {
                MethodPAG mpag = MethodPAG.v( pag, m);
                if (!mpag.hasBeenBuilt()) {
                    System.out.println("mpag has not been built: " + m + " " + NoContext.v());
                    mpag.build();
                } 
                
                if (!mpag.hasContextAdded(NoContext.v())) {
                    mpag.addToPAG(NoContext.v());
                    System.out.println("context not added to mpag: " + m + " " + NoContext.v());
                }
            }
        }
    }

    private PAG pag;
    private CallGraphBuilder cgb;
    private OnFlyCallGraph ofcg;
    private ReachableMethods reachables;
    int totalMethods = 0;
    int stmts = 0;

}
