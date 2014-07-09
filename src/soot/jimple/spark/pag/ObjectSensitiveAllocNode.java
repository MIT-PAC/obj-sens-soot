package soot.jimple.spark.pag;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import soot.ArrayType;
import soot.Context;
import soot.Hierarchy;
import soot.PhaseOptions;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.jimple.spark.SparkTransformer;
import soot.jimple.toolkits.callgraph.ObjSensContextManager;
import soot.options.CGOptions;

/**
 * This class represents context in an object sensitive PTA.
 * 
 * It is a list of new expressions
 * 
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

    static class ContextElements {
        ContextElement[] array;

        ContextElements(InsensitiveAllocNode base, Context context) {
            int contextLength = ObjectSensitiveConfig.v().k();

            if (ObjectSensitiveConfig.v().limitHeapContext(base)) {
                contextLength = 1; 
            } else {
                // do something more for arrays

                
                /*
                 //add more context for classes that have arrays
                if (ObjectSensitiveConfig.v().hasArrayField(base.getType())) {
                    contextLength = ObjectSensitiveConfig.v().k() + 1;
                }
                
                */
                
                //add more context for arrays, change to  + 2 if using the above
                if (ObjectSensitiveConfig.v().extraArrayContext() && !(base.getType() instanceof RefType)) {
                    contextLength = ObjectSensitiveConfig.v().k() + 1;
                }
            }

            array = new ContextElement[contextLength];            
            array[0] = base;

            if (contextLength > 1) {                
                if (context instanceof ObjectSensitiveAllocNode) {
                    ObjectSensitiveAllocNode osan = (ObjectSensitiveAllocNode)context;

                    for (int i = 1; i < contextLength; i++) {
                        if ((i-1) >= osan.contextAllocs.length)
                            break;
                        
                        if (ObjectSensitiveConfig.v().typesForContextGTOne() && 
                                osan.contextAllocs[i-1] instanceof InsensitiveAllocNode) {
                            array[i] = TypeContextElement.v(((InsensitiveAllocNode)osan.contextAllocs[i-1]).getType());
                        } else 
                            array[i] = osan.contextAllocs[i - 1];
                    }
                } else if (context instanceof ContextElement) {
                    if (ObjectSensitiveConfig.v().typesForContextGTOne() && 
                            context instanceof InsensitiveAllocNode)
                        array[1] = TypeContextElement.v(((InsensitiveAllocNode)context).getType());
                    else 
                        array[1] = (ContextElement)context;

                } else {
                    throw new RuntimeException("Unsupported context on alloc node: " + context);
                }
                
                //for loop to fill context elements
                for (int i = 0; i < array.length; i++) {
                    if (array[i] == null)
                        array[i] = NoContext.v();
                }
            }

            
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(array);
        }

        /** 
         * Return the number of non-null or non-No-Context elements, assuming that in the array
         * once we see a no-context element, we don't see a context element.
         * 
         * @return
         */
        public int numContextElements() {
            for (int i = 0; i < array.length; i++) {
                if (array[i] == null || array[i].equals(NoContext.v()))
                    return i;
            }

            return array.length;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (getClass() != obj.getClass()) return false;
            ContextElements other = (ContextElements) obj;
            
            //if (!Arrays.equals(array, other.array)) return false;
            
            //custom array equals for context
            //allows for checking of different sized arrays, but represent same context-sensitive heap object
            if (this.array == null || other.array == null)
                return false;
           
            if (this.numContextElements() != other.numContextElements())
                return false;
            
            for (int i = 0; i < numContextElements(); i++) {
                Object o1 = this.array[i];
                Object o2 = other.array[i];
                if (!(o1==null ? o2==null : o1.equals(o2)))
                    return false;                
            }
            
            return true;
        }

    }

}
