package soot.jimple.spark.pag;

import java.util.HashMap;
import java.util.Map;

import soot.Context;
import soot.SootMethod;
import soot.Type;

public class InsensitiveAllocNode extends AllocNode implements ContextElement {

    public InsensitiveAllocNode(PAG pag, Object newExpr, Type t, SootMethod m) {
        super(pag, newExpr, t, m);
        cvns = new HashMap<Context, ObjectSensitiveAllocNode>();
    }

    /**
     * Return the context sensitive allocation node associated with this alloc node.
     * Create the context sensitive allocation node if we have not seen it before.
     */
    public ObjectSensitiveAllocNode context( Context context ) 
    {   
        if (context == null)
            throw new RuntimeException("Context should not be null when getting context for insensitive node.");
        
        //if we have seen this context before, just return the context object for this alloc node
        if (!cvns.containsKey(context)) {
            ObjectSensitiveAllocNode objsensnode = ObjectSensitiveAllocNode.getObjSensNode(pag, this, (Context)context);
            cvns.put(context, objsensnode );
        }
        
        return cvns.get(context);
    }

    public Map<Context, ObjectSensitiveAllocNode> getContextNodeMap() {
        return cvns;
    }

    /* End of package methods. */

    protected Map<Context, ObjectSensitiveAllocNode> cvns;
    
    public String toString() {
        return "InsensitiveAllocNode "+hashCode()+" "+newExpr+" in "+getMethod();
    }
}
