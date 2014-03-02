/* Soot - a J*va Optimization Framework
 * Copyright (C) 2002 Ondrej Lhotak
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
import soot.jimple.toolkits.pta.IAllocNode;
import soot.options.CGOptions;

/** Represents an allocation site node (Blue) in the pointer assignment graph.
 * @author Ondrej Lhotak
 */
public class AllocNode extends Node implements Context, IAllocNode { // (LWG) implements IAllocNode
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
        return "AllocNode "+hashCode()+" "+newExpr+" in method "+method + " type: " + getType() + " " + getType().hashCode();
    }

        
    public AllocNode( PAG pag, Object newExpr, Type t, SootMethod m) {
        super( pag, t );
        cvns = new HashMap<Context, AllocNode>();
        this.method = m;
        if( t instanceof RefType ) {
            RefType rt = (RefType) t;
            if( rt.getSootClass().isAbstract()) {
                boolean usesReflectionLog = new CGOptions(PhaseOptions.v().getPhaseOptions("cg")).reflection_log()!=null;
                if (!usesReflectionLog) {
                    throw new RuntimeException( "Attempt to create allocnode with abstract type "+t );
                }
            }
        }
        this.newExpr = newExpr;
        if( newExpr instanceof ContextVarNode ) throw new RuntimeException();
    
    }
    /** Registers a AllocDotField as having this node as its base. */
    void addField( AllocDotField adf, SparkField field ) {
        if( fields == null ) fields = new HashMap();
        fields.put( field, adf );
    }

    public Set<AllocDotField> getFields() {
        if( fields == null ) return Collections.EMPTY_SET;
        return new HashSet<AllocDotField>( fields.values() );
    }

    /* End of package methods. */

    protected Object newExpr;
    protected Map<SparkField, AllocDotField> fields;

    private SootMethod method;

    public SootMethod getMethod() { return method; }

    @Override
    public int hashCode() {
        int result = ((newExpr == null) ? 0 : newExpr.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (getClass() != obj.getClass()) return false;
        AllocNode other = (AllocNode) obj;
        if (newExpr == null) {
            if (other.newExpr != null) return false;
        } else if (!newExpr.equals(other.newExpr)) return false;
        return true;
    }

    /**
     * Force the context map to include a map from context -> cNode, denoting
     * that cNode the context-sensitive version of this allocnode for <context>
     */
    public void addContext(Context context, AllocNode cNode) {
        cvns.put(context, cNode);
    }
    
    /**
     * Return the context sensitive allocation node associated with this alloc node.
     * Create the context sensitive allocation node if we have not seen it before.
     */
    public AllocNode context( Context context ) 
    { 
                
        //if we have seen this context before, just return the context object for this alloc node
        if (cvns.containsKey(context)) {
            return cvns.get(context);
        }
        
        //short cut for k == 1
        if (ObjectSensitiveConfig.isObjectSensitive() && ObjectSensitiveConfig.v().k() == 1) {
            cvns.put(context, this);
            return this;
        }
            

        //have not seen this context before, so add it and its obj sens node
        if (context instanceof AllocNode) {
            ObjectSensitiveAllocNode objsensnode = ObjectSensitiveAllocNode.getObjSensNode(pag, this, (Context)context);

            cvns.put(context, objsensnode );

        } else {
            throw new RuntimeException("Invalid context object: " + context);
        }

        return cvns.get(context);
    }

    public Map<Context, AllocNode> getContextNodeMap() {
        return cvns;
    }
    
    /* End of package methods. */

    protected Map<Context, AllocNode> cvns;

}

