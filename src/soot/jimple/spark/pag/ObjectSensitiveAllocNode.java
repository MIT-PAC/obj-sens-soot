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
import soot.jimple.spark.SparkTransformer;
import soot.jimple.toolkits.callgraph.ObjSensContextManager;
import soot.options.CGOptions;

/**
 * This class represents context in an object sensitive PTA.
 * 
 * It is a list of new expression 
 * @author mgordon
 *
 */
public class ObjectSensitiveAllocNode extends AllocNode implements Context {
    public static Map<ObjectSensitiveAllocNode,ObjectSensitiveAllocNode> universe;

    /** Array for context of allocation (new exprs) */
    private ContextElement[] contextAllocs;


    /**
     * Reset the universe of object sensitive allocation nodes.  Set the depth to the first argument.
     * Do not track context for classes (and their subclasses) defined in the noContextList 
     * which is a comma separated list of fully-qualified class names.
     */
    public static void reset() {
        universe = new HashMap<ObjectSensitiveAllocNode,ObjectSensitiveAllocNode>(10000);
    }

    public ObjectSensitiveAllocNode getRepFromUniverse() {
        return universe.get(this);
    }

    public static ObjectSensitiveAllocNode getObjSensNode(PAG pag, InsensitiveAllocNode base, Context context) {
        ObjectSensitiveAllocNode probe = new ObjectSensitiveAllocNode(pag, base, context);

        if (!ObjectSensitiveConfig.v().addHeapContext(probe))
            probe =  new ObjectSensitiveAllocNode(pag, base, NoContext.v());

        if (!universe.containsKey(probe)) {
            //System.out.println("Creating obj sens node: " + probe + "\n");
            universe.put(probe, probe);
            pag.getAllocNodeNumberer().add( probe );
            pag.newAllocNodes.add(probe);

            /* print some stats
            if (base.getMethod() != null) {
                SootClass allocatorClass = base.getMethod().getDeclaringClass();

                if (!numNodes.containsKey(allocatorClass))
                    numNodes.put(allocatorClass, 0);

                numNodes.put(allocatorClass, numNodes.get(allocatorClass) + 1);
                if (universe.size() % printQuantum == 0) {
                    System.out.println(" ======= ");
                    for (Map.Entry<SootClass, Integer> entry : numNodes.entrySet()) {
                        System.out.println(entry.getKey() + " => " + entry.getValue());
                    }
                }

            }
            */
        } 

        return universe.get(probe);        
    }


    public static int numberOfObjSensNodes() {


        if (universe != null)
            return universe.size();
        else 
            return 0;

    }


    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("ObjSensAllocNode ");

        for (int i = 0; i < contextAllocs.length; i++) {
            if (contextAllocs[i] != null)
                buf.append(String.format("[%s (%s)]", contextAllocs[i], 
                    (contextAllocs[i] == null ? 0 : contextAllocs[i].hashCode())));
        }

        return buf.toString();
    }


    /* End of public methods. */

    private ObjectSensitiveAllocNode( PAG pag, InsensitiveAllocNode base, Context context ) {
        super( pag, base.newExpr, base.type, base.getMethod());

        contextAllocs = new ContextElement[ObjectSensitiveConfig.v().k()];
        contextAllocs[0] = base;

        //check if we should limit heap context for this type
        if (base.getType() instanceof RefType && 
                ObjectSensitiveConfig.v().limitHeapContext(base))
            return;

        if (context instanceof ObjectSensitiveAllocNode) {
            ObjectSensitiveAllocNode osan = (ObjectSensitiveAllocNode)context;

            for (int i = 1; i < osan.contextAllocs.length; i++) {
                if (ObjectSensitiveConfig.v().typesForContextGTOne() && 
                        osan.contextAllocs[i-1] instanceof InsensitiveAllocNode) {
                    contextAllocs[i] = TypeContextElement.v(((InsensitiveAllocNode)osan.contextAllocs[i-1]).getType());
                } else 
                    contextAllocs[i] = osan.contextAllocs[i - 1];
            }
        } else if (context instanceof ContextElement) {
            if (contextAllocs.length > 1) {
                if (ObjectSensitiveConfig.v().typesForContextGTOne() && 
                        context instanceof InsensitiveAllocNode)
                    contextAllocs[1] = TypeContextElement.v(((InsensitiveAllocNode)context).getType());
                else 
                    contextAllocs[1] = (ContextElement)context;
            }
        } else {
            throw new RuntimeException("Unsupported context on alloc node: " + context);
        }
    }

    /**
     * Return the oldest context for this next if it has a depth of k for object sensitive context
     */
    public ContextElement getStartingContextElement() {
        for (int i = contextAllocs.length - 1; i >= 0; i--) {
            if (contextAllocs[i] != null && !contextAllocs[i].equals(NoContext.v()))
                return contextAllocs[i];
        }

        return NoContext.v();
    }

    public boolean noContext() {
        return numContextElements() == 1;
    }
    
    public int numContextElements() {
        for (int i = 0; i < contextAllocs.length; i++) {
            if (contextAllocs[i] == null || contextAllocs[i].equals(NoContext.v()))
                return i;
        }

        return contextAllocs.length;
    }

    public boolean containsNewExprContext(Object contextAlloc) {
        for (int i = 0; i < contextAllocs.length; i++) {
            if (contextAlloc.equals(contextAllocs[i]))
                return true;
        }
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
        // LWG: removed for efficiency
        //if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        ObjectSensitiveAllocNode other = (ObjectSensitiveAllocNode) obj;
        if (!Arrays.equals(contextAllocs, other.contextAllocs)) return false;
        return true;
    }



}
