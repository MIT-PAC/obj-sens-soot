package soot.jimple.spark.pag;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import soot.Context;
import soot.PhaseOptions;
import soot.RefType;
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


    private AllocNode[] contextAllocs;
    /** depth of the object sensitivity on heap and method */
    public static int k = 0;

    /** Returns the new expression of this allocation site. */
    public Object getNewExpr() { return newExpr; }
    /** Returns all field ref nodes having this node as their base. */
    public Collection getAllFieldRefs() { 
        if( fields == null ) return Collections.EMPTY_LIST;
        return fields.values();
    }


    /** Returns the field ref node having this node as its base,
     * and field as its field; null if nonexistent. */
    public AllocDotField dot( SparkField field ) 
    { return fields == null ? null : (AllocDotField) fields.get( field ); }
    
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append( "ObjSensAllocNode " + getNumber() + " "+newExpr+" in method "+getMethod() + " ");
        
        for (int i = 0; i < k; i++) {
            if (contextAllocs[i] != null)
                buf.append("["+contextAllocs[i].newExpr + " " + contextAllocs[i].getMethod()+"]");
        }
        
        buf.append("]");
        return buf.toString();
    }

    /* End of public methods. */

    ObjectSensitiveAllocNode( PAG pag, Object newExpr, Type t, SootMethod m, ObjectSensitiveAllocNode context ) {
        super( pag, newExpr, t, m);

        contextAllocs = new AllocNode[k];
        //add context from the context node, plus k  - 1 of object
        if (k > 1) {
            contextAllocs[0] = context;
            if (context != null) {
                for (int i = 1; i < context.contextAllocs.length; i++) {
                    contextAllocs[i] = context.contextAllocs[i - 1];
                }
            }
        }
    }


}
