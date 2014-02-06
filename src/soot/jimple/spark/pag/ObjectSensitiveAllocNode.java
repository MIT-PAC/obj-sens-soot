package soot.jimple.spark.pag;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import soot.Context;
import soot.Hierarchy;
import soot.PhaseOptions;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.options.CGOptions;

/**
 * This class represents context in an object sensitive PTA.
 * 
 * It is a list of new expression 
 * @author mgordon
 *
 */
public class ObjectSensitiveAllocNode extends AllocNode {
    public static Map<ObjectSensitiveAllocNode,ObjectSensitiveAllocNode> universe;
    
    private static final String NO_CONTEXT_INCLUDING_SUBCLASSES[] = 
            new String[]{
                         "java.lang.String", 
                         "java.lang.StringBuffer",
                         "java.lang.StringBuilder",
                         "java.lang.Throwable",
                         "java.math.BigInt",
                         "java.math.BigInteger"
        };
    
    private static Set<SootClass> ignoreList;
    

    /** Array for context of allocation (new exprs) */
    private Object[] contextAllocs;
    /** depth of the object sensitivity on heap and method */
    public static int k = 0;

    public static void reset(int depth) {
        universe = new HashMap<ObjectSensitiveAllocNode,ObjectSensitiveAllocNode>(10000);
        k = depth;
        installIgnoreList();
    }
    
    public static ObjectSensitiveAllocNode getObjSensNode(PAG pag, AllocNode base, Context context) {
        ObjectSensitiveAllocNode probe = new ObjectSensitiveAllocNode(pag, base, context);
        if (!universe.containsKey(probe)) {
            //System.out.println("Adding " + probe);
            universe.put(probe, probe);
        }
            
        return universe.get(probe);        
    }
    
    public static void installIgnoreList() {
        ignoreList = new HashSet<SootClass>();
        Hierarchy h = Scene.v().getActiveHierarchy();
        
        for (String str : NO_CONTEXT_INCLUDING_SUBCLASSES) {
            SootClass clz = Scene.v().getSootClass(str);
            ignoreList.addAll(h.getSubclassesOfIncluding(clz));
        }
    }
    
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(String.format("ObjSensAllocNode %d %s (%d) in %s", 
            hashCode(),
            newExpr, 
            (newExpr == null ? 0 : newExpr.hashCode()), 
            getMethod()));

        for (int i = 0; i < k; i++) {
            if (contextAllocs[i] != null)
                buf.append(String.format("[%s (%s)]", contextAllocs[i], 
                    (contextAllocs[i] == null ? 0 : contextAllocs[i].hashCode())));
        }

        return buf.toString();
    }

    /* End of public methods. */

    private ObjectSensitiveAllocNode( PAG pag, AllocNode base, Context context ) {
        super( pag, base.newExpr, base.type, base.getMethod());
        
        contextAllocs = new Object[k];
        
        //short-circuit context for some types
        if (noContext(base))
            return;
        
        if (context instanceof ObjectSensitiveAllocNode) {
            ObjectSensitiveAllocNode osan = (ObjectSensitiveAllocNode)context;
            
            //add context from the context node, plus k  - 1 of object
            if (k > 1 && context != null) {
               
                /*
                //don't add a node twice
                if (this.newExpr.equals(osan.newExpr))
                    return;
                */
                
                contextAllocs[0] = osan.newExpr;
                
                for (int i = 1; i < osan.contextAllocs.length; i++) {
                    //don't add a node twice, break context at recursive alloc chain
                    /*
                    if (osan.contextAllocs[i - 1] == null || 
                            containsNewExprContext(osan.contextAllocs[i - 1]))
                        return;
                    */
                    contextAllocs[i] = osan.contextAllocs[i - 1];
                }
            }
        } else if (context instanceof AllocNode) {
            contextAllocs = new Object[k];
            contextAllocs[0] = ((AllocNode)context).newExpr;
        } else {
            throw new RuntimeException("Unsupported context on alloc node: " + context);
        }
    }
    
    public boolean containsNewExprContext(Object contextAlloc) {
        for (int i = 0; i < contextAllocs.length; i++) {
            if (contextAlloc.equals(contextAllocs[i]))
                return true;
        }
        return false;
    }

    private boolean noContext(AllocNode base) {

        if (base.getType() instanceof RefType) {
            RefType type = null;
            type = (RefType)base.getType();
            SootClass clz = type.getSootClass();
            return ignoreList.contains(clz);
        }
        //context for everything else?
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Arrays.hashCode(contextAllocs);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        ObjectSensitiveAllocNode other = (ObjectSensitiveAllocNode) obj;
        if (!Arrays.equals(contextAllocs, other.contextAllocs)) return false;
        return true;
    }



}
