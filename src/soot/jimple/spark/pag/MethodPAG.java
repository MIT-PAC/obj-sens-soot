/* Soot - a J*va Optimization Framework
 * Copyright (C) 2003 Ondrej Lhotak
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import soot.ArrayType;
import soot.Body;
import soot.Context;
import soot.EntryPoints;
import soot.G;
import soot.RefLikeType;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.VoidType;
import soot.jimple.Stmt;
import soot.jimple.spark.builder.MethodNodeFactory;
import soot.util.NumberedString;
import soot.util.SingletonList;
import soot.util.queue.ChunkedQueue;
import soot.util.queue.QueueReader;


/** Part of a pointer assignment graph for a single method.
 * @author Ondrej Lhotak
 */
public final class MethodPAG {
    private static HashMap<SootMethod, MethodPAG> MethodPAG_methodToPag = 
            new HashMap<SootMethod, MethodPAG>();
    private PAG pag;
    public PAG pag() { return pag; }

    public static void reset() {
        MethodPAG_methodToPag = 
                new HashMap<SootMethod, MethodPAG>();
    }

    protected MethodPAG( PAG pag, SootMethod m ) {
        this.pag = pag;
        this.method = m;
        this.nodeFactory = new MethodNodeFactory( pag, this);
    }

    private Set<Context> addedContexts;

    /** Adds this method to the main PAG, with all VarNodes parameterized by
     * varNodeParameter. */
    public void addToPAG( Context varNodeParameter ) {
        if( !hasBeenBuilt ) throw new RuntimeException();
        if( varNodeParameter == null ) {
            if( hasBeenAdded ) return;
            hasBeenAdded = true;
        } else {
            if( addedContexts == null ) addedContexts = new HashSet<Context>();
            if( !addedContexts.add( varNodeParameter ) ) return;
        }
        QueueReader reader = (QueueReader) internalReader.clone();
        while(reader.hasNext()) {
            Node src = (Node) reader.next();
            src = parameterize( src, varNodeParameter );
            Node dst = (Node) reader.next();
            dst = parameterize( dst, varNodeParameter );
            pag.addEdge( src, dst );
        }
        reader = (QueueReader) inReader.clone();
        while(reader.hasNext()) {
            Node src = (Node) reader.next();
            Node dst = (Node) reader.next();
            dst = parameterize( dst, varNodeParameter );
            pag.addEdge( src, dst );
        }
        reader = (QueueReader) outReader.clone();
        while(reader.hasNext()) {
            Node src = (Node) reader.next();
            src = parameterize( src, varNodeParameter );
            Node dst = (Node) reader.next();
            pag.addEdge( src, dst );
        }
    }
    public void addInternalEdge( Node src, Node dst ) {
        if( src == null ) return;
        internalEdges.add( src );
        internalEdges.add( dst );
        if (hasBeenAdded) {
            pag.addEdge(src, dst);
        }        
    }
    public void addInEdge( Node src, Node dst ) {
        if( src == null ) return;
        inEdges.add( src );
        inEdges.add( dst );
        if (hasBeenAdded) {
            pag.addEdge(src, dst);
        }        
    }
    public void addOutEdge( Node src, Node dst ) {
        if( src == null ) return;
        outEdges.add( src );
        outEdges.add( dst );
        if (hasBeenAdded) {
            pag.addEdge(src, dst);
        }        
    }
    private final ChunkedQueue internalEdges = new ChunkedQueue();
    private final ChunkedQueue inEdges = new ChunkedQueue();
    private final ChunkedQueue outEdges = new ChunkedQueue();
    private final QueueReader internalReader = internalEdges.reader();
    private final QueueReader inReader = inEdges.reader();
    private final QueueReader outReader = outEdges.reader();

    SootMethod method;
    public SootMethod getMethod() { return method; }
    protected MethodNodeFactory nodeFactory;
    public MethodNodeFactory nodeFactory() { return nodeFactory; }

    public static MethodPAG v( PAG pag, SootMethod m) {
        MethodPAG ret = MethodPAG_methodToPag.get( m );
        if( ret == null ) { 
            ret = new MethodPAG( pag, m);
            MethodPAG_methodToPag.put( m, ret );
        }
        return ret;
    }

    public void build() {
        if( hasBeenBuilt ) return;
        hasBeenBuilt = true;
        if( method.isNative() ) {
            if( pag().getOpts().simulate_natives() ) {
                buildNative();
            }
        } else {
            if( method.isConcrete() && !method.isPhantom() ) {
                buildNormal();
            }
        }
        addMiscEdges();
    }

    protected Node parameterize( LocalVarNode vn, Context context ) {
        SootMethod m = vn.getMethod();
        if( m != method && m != null ) throw new RuntimeException( "VarNode "+vn+" with method "+m+" parameterized in method "+method );
        //System.out.println( "parameterizing "+vn+" with "+varNodeParameter );
        /*
        if (ObjectSensitiveConfig.isObjectSensitive() && vn.isThisPtr()) {
            //do something special for this pointer...
            if (context instanceof AllocNode ) {
                return (AllocNode)context;
            }
            else if (context instanceof NoContext) 
                return null;
            else 
                throw new RuntimeException("Strange context for this reference when parameterizing: " + context.getClass());
        } else 
            
            */
        return pag().makeContextVarNode( vn, context ); 
    }

    protected FieldRefNode parameterize( FieldRefNode frn, Context varNodeParameter ) {
        return pag().makeFieldRefNode(
            (VarNode) parameterize( frn.getBase(), varNodeParameter ),
            frn.getField() );
    }

    protected AllocNode parameterize(InsensitiveAllocNode node, Context context) {
        return pag().makeObjSensAllocNode(node, context);
    }

    public Node parameterize( Node n, Context varNodeParameter ) {
        
        if( varNodeParameter == null) {
            //if obj sens, we should never have the null context!
            if (ObjectSensitiveConfig.isObjectSensitive())
                varNodeParameter = NoContext.v();
            else  //if not obj sens, then just return if null
                return n;
        }
               
        if( n instanceof LocalVarNode ) 
            return parameterize( (LocalVarNode) n, varNodeParameter);
        if( n instanceof FieldRefNode )
            return parameterize( (FieldRefNode) n, varNodeParameter);
        if (n instanceof InsensitiveAllocNode) 
            return parameterize((InsensitiveAllocNode)n, varNodeParameter);

        if (n instanceof ObjectSensitiveAllocNode)
            throw new RuntimeException("Strange type in parameterize: " + n.getClass());

        return n;
    }
    protected boolean hasBeenAdded = false;
    protected boolean hasBeenBuilt = false;

    protected void buildNormal() {
        Body b = method.retrieveActiveBody();
        Iterator unitsIt = b.getUnits().iterator();
        while( unitsIt.hasNext() )
        {
            Stmt s = (Stmt) unitsIt.next();
            nodeFactory.handleStmt( s );
        }
    }
    protected void buildNative() {
        ValNode thisNode = null;
        ValNode retNode = null; 
        if( !method.isStatic() ) { 
            thisNode = (ValNode) nodeFactory.caseThis();
        }
        if( method.getReturnType() instanceof RefLikeType ) {
            retNode = (ValNode) nodeFactory.caseRet();
        }
        ValNode[] args = new ValNode[ method.getParameterCount() ];
        for( int i = 0; i < method.getParameterCount(); i++ ) {
            if( !( method.getParameterType(i) instanceof RefLikeType ) ) continue;
            args[i] = (ValNode) nodeFactory.caseParm(i);
        }
        pag.nativeMethodDriver.process( method, thisNode, retNode, args );
    }

    protected void addMiscEdges() {
        // Add node for parameter (String[]) in main method
        if( method.getSubSignature().equals( SootMethod.getSubSignature( "main", new SingletonList( ArrayType.v(RefType.v("java.lang.String"), 1) ), VoidType.v() ) ) ) {
            addInEdge( pag().nodeFactory().caseArgv(), nodeFactory.caseParm(0) );
        } else

            if( method.getSignature().equals(
                    "<java.lang.Thread: void <init>(java.lang.ThreadGroup,java.lang.String)>" ) ) {
                addInEdge( pag().nodeFactory().caseMainThread(), nodeFactory.caseThis() );
                addInEdge( pag().nodeFactory().caseMainThreadGroup(), nodeFactory.caseParm( 0 ) );
            } else

                if (method.getSignature().equals(
                        "<java.lang.ref.Finalizer: void <init>(java.lang.Object)>")) {
                    addInEdge( nodeFactory.caseThis(), pag().nodeFactory().caseFinalizeQueue());
                } else

                    if (method.getSignature().equals(
                            "<java.lang.ref.Finalizer: void runFinalizer()>")) {
                        addInEdge(pag.nodeFactory().caseFinalizeQueue(), nodeFactory.caseThis());
                    } else

                        if (method.getSignature().equals(
                                "<java.lang.ref.Finalizer: void access$100(java.lang.Object)>")) {
                            addInEdge(pag.nodeFactory().caseFinalizeQueue(), nodeFactory.caseParm(0));
                        } else

                            if (method.getSignature().equals(
                                    "<java.lang.ClassLoader: void <init>()>")) {
                                addInEdge(pag.nodeFactory().caseDefaultClassLoader(), nodeFactory.caseThis());
                            } else

                                if (method.getSignature().equals("<java.lang.Thread: void exit()>")) {
                                    addInEdge(pag.nodeFactory().caseMainThread(), nodeFactory.caseThis());
                                } else

                                    if (method
                                            .getSignature()
                                            .equals(
                                                    "<java.security.PrivilegedActionException: void <init>(java.lang.Exception)>")) {
                                        addInEdge(pag.nodeFactory().caseThrow(), nodeFactory.caseParm(0));
                                        addInEdge(pag.nodeFactory().casePrivilegedActionException(), nodeFactory.caseThis());
                                    }

        if (method.getNumberedSubSignature().equals(sigCanonicalize)) {
            SootClass cl = method.getDeclaringClass();
            while (true) {
                if (cl.equals(Scene.v().getSootClass("java.io.FileSystem"))) {
                    addInEdge(pag.nodeFactory().caseCanonicalPath(), nodeFactory.caseRet());
                }
                if (!cl.hasSuperclass())
                    break;
                cl = cl.getSuperclass();
            }
        }

        boolean isImplicit = false;
        for (SootMethod implicitMethod : EntryPoints.v().implicit()) {
            if (implicitMethod.getNumberedSubSignature().equals(
                method.getNumberedSubSignature())) {
                isImplicit = true;
                break;
            }
        }
        if (isImplicit) {
            SootClass c = method.getDeclaringClass();
            outer: do {
                while (!c.getName().equals("java.lang.ClassLoader")) {
                    if (!c.hasSuperclass()) {
                        break outer;
                    }
                    c = c.getSuperclass();
                }
                if (method.getName().equals("<init>"))
                    continue;
                addInEdge(pag().nodeFactory().caseDefaultClassLoader(),
                    nodeFactory.caseThis());
                addInEdge(pag().nodeFactory().caseMainClassNameString(),
                    nodeFactory.caseParm(0));
            } while (false);
        }
    }


    protected final NumberedString sigCanonicalize = Scene.v().getSubSigNumberer().
            findOrAdd("java.lang.String canonicalize(java.lang.String)");
}

