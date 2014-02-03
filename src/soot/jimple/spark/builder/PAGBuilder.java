package soot.jimple.spark.builder;

import java.util.ArrayList;
import java.util.Iterator;

import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.spark.pag.PAG;
import soot.options.SparkOptions;

public abstract class PAGBuilder {

    public PAGBuilder() {
    }
    
    public abstract PAG setup( SparkOptions opts );
    
    public abstract void build();
    
    public void preJimplify() {
        boolean change = true;
        while( change ) {
            change = false;
            for( Iterator<SootClass> cIt = new ArrayList<SootClass>(Scene.v().getClasses()).iterator(); cIt.hasNext(); ) {
                final SootClass c = cIt.next();
                for( Iterator mIt = c.methodIterator(); mIt.hasNext(); ) {
                    final SootMethod m = (SootMethod) mIt.next();
                    if( !m.isConcrete() ) continue;
                    if( m.isNative() ) continue;
                    if( m.isPhantom() ) continue;
                    if( !m.hasActiveBody() ) {
                        change = true;
                        m.retrieveActiveBody();
                    }
                }
            }
        }
    }

}
