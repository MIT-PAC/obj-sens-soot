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

package soot.jimple.spark.callgraph;
import soot.*;
import soot.jimple.*;
import soot.jimple.spark.*;
import soot.jimple.toolkits.callgraph.*;
import java.util.*;
import soot.util.*;
import soot.util.queue.*;
import soot.jimple.spark.pag.*;

/** Models the call graph.
 * @author Ondrej Lhotak
 */
public final class CallGraphBuilder
{ 
    private NumberedSet reachable = new NumberedSet( Scene.v().getMethodNumberer() );
    private HashMap invokeExprToVCS = new HashMap();
    private LargeNumberedMap localToVCS = new LargeNumberedMap( Scene.v().getLocalNumberer() );
    private QueueReader worklist;
    private PointsToAnalysis pa;
    private ChunkedQueue reachableQueue;
    public QueueReader reachables() { return reachableQueue.reader(); }
    private ChunkedQueue callEdgeQueue = new ChunkedQueue();
    public QueueReader callEdges() { return callEdgeQueue.reader(); }
    private boolean verbose;
    private boolean allClinit;

    public CallGraphBuilder( PointsToAnalysis pa, boolean verbose, boolean allClinit ) {
        this.verbose = verbose;
        this.allClinit = allClinit;
        this.pa = pa;
        reachableQueue = new ChunkedQueue();
        worklist = reachables();
    }
    /** Specifies a CallGraph that will be filled in as this CallGraphBuilder
     * is built. */
    public void setCallGraph( CallGraph cg ) { this.cg = cg; }
    private boolean setReachable( SootMethod cur, SootMethod m ) {
        if( reachable.add( m ) ) {
            reachableQueue.add( m );
            return true;
        }
        graph.put( cur, m );
        return false;
    }
    public boolean isReachable( SootMethod m ) {
        return reachable.contains( m );
    }
    public Iterator reachableMethods() {
        return reachable.iterator();
    }
    public int numReachableMethods() {
        return reachable.size();
    }
    public void build() {
        for( Iterator mIt = ImplicitMethodInvocation.v().getEntryPoints().iterator(); mIt.hasNext(); ) {
            final SootMethod m = (SootMethod) mIt.next();
            setReachable( m, m );
        }
        if( allClinit ) {
            for( Iterator clIt = Scene.v().getClasses().iterator(); clIt.hasNext(); ) {
                final SootClass cl = (SootClass) clIt.next();
                if( cl.declaresMethod( ImplicitMethodInvocation.v().sigClinit ) ) {
                    SootMethod m = cl.getMethod( ImplicitMethodInvocation.v().sigClinit );
                    setReachable( m, m );
                    G.v().out.println( "making reachable: "+m );
                }
            }
        }
        processWorklist();
    }
    private void processWorklist() {
        while(true) {
            SootMethod m = (SootMethod) worklist.next();
            if( m == null ) break;
            processNewMethod( m );
        }
    }

    private void handleClassName( SootMethod m, AssignStmt s, String name ) {
        if( name.length() == 0 ) return;
        if( name.charAt(0) == '[' ) return;
        if( pa instanceof PAG ) {
            PAG pag = (PAG) pa;
            AllocNode an = pag.makeClassConstantNode( name );
            VarNode vn = pag.makeVarNode( s.getLeftOp(), s.getLeftOp().getType(), m );
            pag.addEdge( an, vn );
        }
    }

    private void handleClassConstant( SootMethod m, AssignStmt s, String name ) {
        InstanceInvokeExpr ie = (InstanceInvokeExpr) s.getInvokeExpr();
        if( !Scene.v().containsClass( name ) ) {
            if( verbose ) {
                G.v().out.println( "WARNING: Class "+name+" is"+
                    " a dynamic class, and you did not specify"+
                    " it as such; graph will be incomplete!" );
            }
        } else {
            SootClass sootcls = Scene.v().getSootClass( name );
            if( pa instanceof PAG ) {
                PAG pag = (PAG) pa;
                AllocNode an = pag.makeAllocNode( s, sootcls.getType(), null );
                VarNode vn = pag.makeVarNode( s.getLeftOp(), s.getLeftOp().getType(), m );
                pag.addEdge( an, vn );
            }
            if( sootcls.declaresMethod( ImplicitMethodInvocation.v().sigClinit ) ) {
                SootMethod clinit = 
                    sootcls.getMethod( ImplicitMethodInvocation.v().sigClinit );
                setReachable( m, clinit );
                cg.addEdge( new Edge( m, s, clinit, Edge.CLINIT ) );
            }
        }
    }
    private void handleNewInstance( SootMethod m, AssignStmt s, InstanceInvokeExpr ie ) {
        Value nameVal = ie.getBase();
        wantedClassConstants.put( nameVal, s );
        stmtToMethod.put( s, m );
        Set classes = pa.reachingObjects( (Local) nameVal ).possibleClassConstants();
        if( classes == null ) {
            if( verbose ) {
                G.v().out.println( "WARNING: Method "+m+
                    " is reachable, and calls newInstance on an unknown"+
                    " java.lang.Class; graph will be incomplete!" );
            }
        } else {
            for( Iterator nameIt = classes.iterator(); nameIt.hasNext(); ) {
                final String name = (String) nameIt.next();
                handleClassConstant( m, s, name );
            }
        }
    }

    private void handleForName( SootMethod m, AssignStmt s, StaticInvokeExpr ie ) {
        Value nameVal = ie.getArg(0);
        wantedStringConstants.put( nameVal, s );
        stmtToMethod.put( s, m );
        if( nameVal instanceof StringConstant ) {
            String name = ((StringConstant) nameVal ).value;
            handleClassName( m, s, name );
        } else if( nameVal instanceof Local ) {
            Set names = pa.reachingObjects( (Local) nameVal ).possibleStringConstants();
            if( names == null ) {
                if( verbose ) {
                    G.v().out.println( "WARNING: Method "+m+
                        " is reachable, and calls Class.forName on a"+
                        " non-constant String; graph will be incomplete!" );
                }
            } else {
                for( Iterator nameIt = names.iterator(); nameIt.hasNext(); ) {
                    final String name = (String) nameIt.next();
                    handleClassName( m, s, name );
                }
            }
        } else throw new RuntimeException( "oops "+nameVal );
    }

    private void processNewMethod( SootMethod m ) {
        if( m.isNative() ) {
            return;
        }
        Body b = m.retrieveActiveBody();
        HashSet receivers = new HashSet();
        for( Iterator eIt = ImplicitMethodInvocation.v().getImplicitTargets( m, verbose ).iterator(); eIt.hasNext(); ) {
            final Edge e = (Edge) eIt.next();
            cg.addEdge( e );
            setReachable( e.src(), e.tgt() );
        }
        for( Iterator sIt = b.getUnits().iterator(); sIt.hasNext(); ) {
            final Stmt s = (Stmt) sIt.next();
            if( s.containsInvokeExpr() ) {
                InvokeExpr ie = (InvokeExpr) s.getInvokeExpr();

                if( ie instanceof InstanceInvokeExpr ) {
                    VirtualCallSite vcs = new VirtualCallSite( s, m );
                    invokeExprToVCS.put( ie, vcs );
                    Local receiver = (Local) ((InstanceInvokeExpr)ie).getBase();
                    HashSet vcss = (HashSet) localToVCS.get( receiver );
                    if( vcss == null ) {
                        localToVCS.put( receiver, vcss = new HashSet() );
                    }
                    vcss.add( vcs );
                    receivers.add( receiver );

                    /*
                    SootMethod tgt = ie.getMethod();
                    if( tgt.getName().equals( "newInstance" )
                    && tgt.getDeclaringClass().getName().equals( "java.lang.Class" )
                    && s instanceof AssignStmt ) {
                        handleNewInstance( m, (AssignStmt) s, (InstanceInvokeExpr) ie );
                    }
                    */
                } else {
                    SootMethod tgt = ((StaticInvokeExpr)ie).getMethod();
                    callEdgeQueue.add( s );
                    callEdgeQueue.add( tgt );
                    setReachable( m, tgt );
                    if( cg != null ) cg.addEdge( new Edge( m, s, tgt ) );
                    //tgt.addTag( new soot.tagkit.StringTag( "SCS: "+s.getInvokeExpr()+" in "+m ) );

                    /*
                    if( tgt.getName().equals( "forName" ) 
                    && tgt.getDeclaringClass().getName().equals( "java.lang.Class" )
                    && s instanceof AssignStmt ) {
                        handleForName( m, (AssignStmt) s, (StaticInvokeExpr) ie );
                    }
                    */
                }
            }
        }
        for( Iterator receiverIt = receivers.iterator(); receiverIt.hasNext(); ) {
            final Local receiver = (Local) receiverIt.next();
            Set types = pa.reachingObjects( receiver ).possibleTypes();
            HashSet vcss = (HashSet) localToVCS.get( receiver );
            for( Iterator vcsIt = vcss.iterator(); vcsIt.hasNext(); ) {
                final VirtualCallSite vcs = (VirtualCallSite) vcsIt.next();
                callEdgeQueue.add( vcs );
                QueueReader targets = callEdges();
                for( Iterator tIt = types.iterator(); tIt.hasNext(); ) {
                    final Type t = (Type) tIt.next();
                    vcs.addType( t, callEdgeQueue );
                }
                while(true) {
                    SootMethod target = (SootMethod) targets.next();
                    if( target == null ) break;
                    setReachable( m, target );
                    if( cg != null ) cg.addEdge( new Edge(
                            vcs.getContainer(), vcs.getStmt(), target ) );
                }
                if( !(pa instanceof PAG) ) {
                    vcs.noMoreTypes();
                }
            }
        }
    }

    HashSet currentvcss = null;
    public boolean wantTypes( Local l ) {
        currentvcss = (HashSet) localToVCS.get( l );
        return currentvcss != null && !currentvcss.isEmpty();
    }
    public void addType( Type t ) {
        for( Iterator vcsIt = currentvcss.iterator(); vcsIt.hasNext(); ) {
            final VirtualCallSite vcs = (VirtualCallSite) vcsIt.next();
            callEdgeQueue.add( vcs );
            QueueReader targets = callEdges();
            vcs.addType( t, callEdgeQueue );
            while(true) {
                SootMethod target = (SootMethod) targets.next();
                if( target == null ) break;
                setReachable( vcs.getContainer(), target );
                cg.addEdge(
                    new Edge( vcs.getContainer(), vcs.getStmt(), target ) );
            }
        }
    }
    public void doneTypes() {
        currentvcss = null;
        processWorklist();
    }

    public boolean wantStringConstants( Local l ) {
        return wantedStringConstants.keySet().contains( l );
    }

    public boolean wantClassConstants( Local l ) {
        return false;
//        return wantedClassConstants.keySet().contains( l );
    }

    public void newStringConstant( Local l, String name ) {
        for( Iterator sIt = wantedStringConstants.get( l ).iterator(); sIt.hasNext(); ) {
            final AssignStmt s = (AssignStmt) sIt.next();
            SootMethod m = (SootMethod) stmtToMethod.get(s);
            if( name == null ) {
                G.v().out.println( "WARNING: Method "+m+
                    " is reachable, and calls Class.forName on a"+
                    " non-constant String; graph will be incomplete!" );
                wantedStringConstants.remove( l );
            } else {
                handleClassName( m, s, name );
            }
        }
    }

    public void newClassConstant( Local l, String name ) {
        for( Iterator sIt = wantedStringConstants.get( l ).iterator(); sIt.hasNext(); ) {
            final AssignStmt s = (AssignStmt) sIt.next();
            SootMethod m = (SootMethod) stmtToMethod.get(s);
            if( name == null ) {
                G.v().out.println( "WARNING: Method "+stmtToMethod.get(s)+
                    " is reachable, and calls newInstance on an unknown"+
                    " java.lang.Class; graph will be incomplete!" );
                wantedClassConstants.remove( l );
            } else {
                handleClassConstant( m, s, name );
            }
        }
    }

    public CallGraph getCallGraph() {
        HashSet r = new HashSet();
        for( Iterator mIt = reachable.iterator(); mIt.hasNext(); ) {
            final SootMethod m = (SootMethod) mIt.next();
            r.add(m);
        }
        return cg;
    }

    private HashMultiMap wantedStringConstants = new HashMultiMap();
    private HashMap stmtToMethod = new HashMap();
    private HashMultiMap wantedClassConstants = new HashMultiMap();
    private CallGraph cg;

    public HashMultiMap graph = new HashMultiMap();
}

