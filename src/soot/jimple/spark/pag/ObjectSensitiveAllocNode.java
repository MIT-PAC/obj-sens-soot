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
    public static Map<ContextElements, ObjectSensitiveAllocNode> universeMap;

    /** Array for context of allocation (new exprs) */
    private ContextElement[] contextAllocs;


    /**
     * Reset the universe of object sensitive allocation nodes.  Set the depth to the first argument.
     * Do not track context for classes (and their subclasses) defined in the noContextList 
     * which is a comma separated list of fully-qualified class names.
     */
    public static void reset() {
        universeMap = new HashMap<ContextElements,ObjectSensitiveAllocNode>(10000);
    }

    public ObjectSensitiveAllocNode getRepFromUniverse() {
        return universeMap.get(this);
    }

    public static ObjectSensitiveAllocNode getObjSensNode(PAG pag, InsensitiveAllocNode base, Context context) {
        ContextElements contextElements = new ContextElements(base, context);

        /*
        if (!ObjectSensitiveConfig.v().addHeapContext(probe))
            probe =  new ObjectSensitiveAllocNode(pag, base, NoContext.v());
        */
        
        ObjectSensitiveAllocNode objSenAllocNode = universeMap.get(contextElements);

        if (objSenAllocNode == null) {
            //System.out.println("Creating obj sens node: " + probe + "\n");
            objSenAllocNode = new ObjectSensitiveAllocNode(pag, base, contextElements.array);
            universeMap.put(contextElements, objSenAllocNode);
            pag.getAllocNodeNumberer().add( objSenAllocNode );
            pag.newAllocNodes.add(objSenAllocNode);

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

        return objSenAllocNode;        
    }

    public ContextElement getContextElement(int i) {
        return contextAllocs[i];
    }

    public static int numberOfObjSensNodes() {


        if (universeMap != null)
            return universeMap.size();
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

    private ObjectSensitiveAllocNode( PAG pag, InsensitiveAllocNode base, ContextElement[] contextAllocs ) {
        super( pag, base.newExpr, base.type, base.getMethod());
        this.contextAllocs = contextAllocs;
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

    // LWG: not needed since obj sen alloc node creation ensures that for all equal context elements there is an 
    // unique obj sen alloc node.  See universeMap and getObjSensNode() in this class.
    /*
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
    */

    static class ContextElements {
        ContextElement[] array;
        
        ContextElements(InsensitiveAllocNode base, Context context) {
            array = new ContextElement[ObjectSensitiveConfig.v().k()];
            array[0] = base;
            
            //should we limit heap context to 1 for this node
            if (!ObjectSensitiveConfig.v().limitHeapContext(base)) {
                
                int contextLength = ObjectSensitiveConfig.v().contextDepth(base, context);

            /*    
             //fix for some arrays
                if (!(base.getType() instanceof RefType))
                    contextLength = ObjectSensitiveConfig.v().k();
             */

                if (contextLength == 0)
                    throw new RuntimeException("Problem: Context depth should never be zero for obj sens node with base " + base);

                if (context instanceof ObjectSensitiveAllocNode) {
                    ObjectSensitiveAllocNode osan = (ObjectSensitiveAllocNode)context;

                    for (int i = 1; i < contextLength; i++) {
                        if (ObjectSensitiveConfig.v().typesForContextGTOne() && 
                                osan.contextAllocs[i-1] instanceof InsensitiveAllocNode) {
                            array[i] = TypeContextElement.v(((InsensitiveAllocNode)osan.contextAllocs[i-1]).getType());
                        } else 
                            array[i] = osan.contextAllocs[i - 1];
                    }
                } else if (context instanceof ContextElement) {
                    if (array.length > 1) {
                        if (ObjectSensitiveConfig.v().typesForContextGTOne() && 
                                context instanceof InsensitiveAllocNode)
                            array[1] = TypeContextElement.v(((InsensitiveAllocNode)context).getType());
                        else 
                            array[1] = (ContextElement)context;
                    }
                } else {
                    throw new RuntimeException("Unsupported context on alloc node: " + context);
                }
            }
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(array);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (getClass() != obj.getClass()) return false;
            ContextElements other = (ContextElements) obj;
            if (!Arrays.equals(array, other.array)) return false;
            return true;
        }

    }

}
