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

package soot.jimple.toolkits.callgraph;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import soot.ArrayType;
import soot.Body;
import soot.Context;
import soot.EntryPoints;
import soot.FastHierarchy;
import soot.G;
import soot.Kind;
import soot.Local;
import soot.MethodContext;
import soot.MethodOrMethodContext;
import soot.PackManager;
import soot.PhaseOptions;
import soot.RefType;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Transform;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.javaToJimple.LocalGenerator;
import soot.jimple.AssignStmt;
import soot.jimple.FieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.spark.SparkTransformer;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.ClassConstantNode;
import soot.jimple.spark.pag.EntryContext;
import soot.jimple.spark.pag.NoContext;
import soot.jimple.spark.pag.ObjectSensitiveAllocNode;
import soot.jimple.spark.pag.ObjectSensitiveConfig;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.StaticInitContext;
import soot.jimple.spark.pag.StringConstantNode;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.toolkits.reflection.ReflectionTraceInfo;
import soot.options.CGOptions;
import soot.options.Options;
import soot.tagkit.Host;
import soot.tagkit.SourceLnPosTag;
import soot.toolkits.scalar.Pair;
import soot.util.LargeNumberedMap;
import soot.util.NumberedString;
import soot.util.SmallNumberedMap;
import soot.util.queue.ChunkedQueue;
import soot.util.queue.QueueReader;

/** Models the call graph.
 * @author Ondrej Lhotak
 */
public final class OnFlyCallGraphBuilder
{ 
    //if true then don't try to add clinit calls for newInstance() calls based on string constants
    private static boolean UNSOUND_REFLECTION_CLINIT_CALLS = true;

    public class DefaultReflectionModel implements ReflectionModel {

        protected CGOptions options = new CGOptions( PhaseOptions.v().getPhaseOptions("cg") );

        protected HashSet<MethodOrMethodContext> warnedAlready = new HashSet<MethodOrMethodContext>();

        public void classForName(MethodOrMethodContext source, Stmt s) {
            /*
            List<Local> stringConstants = (List<Local>) methodToStringConstants.get(source);
            if( stringConstants == null )
                methodToStringConstants.put(source, stringConstants = new ArrayList<Local>());
            InvokeExpr ie = s.getInvokeExpr();
            Value className = ie.getArg(0);
            if( className instanceof StringConstant ) {
                String cls = ((StringConstant) className ).value;
                constantForName( cls, source, s);
            } else {
                Local constant = (Local) className;
                if( options.safe_forname() ) {
                    for (SootMethod tgt : EntryPoints.v().clinits()) {
                        System.out.println("Adding clinit call for safety: " + tgt);
                        addEdge( source, s, tgt, Kind.CLINIT );
                    }
                } else {
                    for (SootClass cls : Scene.v().dynamicClasses()) {
                        for (SootMethod clinit : EntryPoints.v().clinitsOf(cls)) {
                            addEdge( source,  s, clinit, Kind.CLINIT);
                            System.out.println("Adding clinit call for dynamic?: " + clinit);
                        }
                    }
                    VirtualCallSite site = new VirtualCallSite( s, source, null, null, Kind.CLINIT );
                    List<VirtualCallSite> sites = stringConstToSites.get(constant);
                    if (sites == null) {
                        stringConstToSites.put(constant, sites = new ArrayList<VirtualCallSite>());
                        stringConstants.add(constant);
                    }
                    sites.add(site);
                }
            } 
            */       
        }

        public void classNewInstance(MethodOrMethodContext source, Stmt s) {
            /*
            if( options.safe_newinstance() ) {
                for (SootMethod tgt : EntryPoints.v().inits()) {
                    addEdge( source, s, tgt, Kind.NEWINSTANCE );
                }
            } else {
                for (SootClass cls : Scene.v().dynamicClasses()) {
                    if( cls.declaresMethod(sigInit) ) {
                        addEdge( source, s, cls.getMethod(sigInit), Kind.NEWINSTANCE );
                    }
                }

                if( options.verbose() ) {
                    G.v().out.println( "Warning: Method "+source+
                        " is reachable, and calls Class.newInstance;"+
                        " graph will be incomplete!"+
                            " Use safe-newinstance option for a conservative result." );
                }
            } 
            */
        }

        public void contructorNewInstance(MethodOrMethodContext source, Stmt s) {
            /*
            if( options.safe_newinstance() ) {
                for (SootMethod tgt : EntryPoints.v().allInits()) {
                    addEdge( source, s, tgt, Kind.NEWINSTANCE );
                }
            } else {
                for (SootClass cls : Scene.v().dynamicClasses()) {
                    for(SootMethod m: cls.getMethods()) {
                        if(m.getName().equals("<init>")) {
                            addEdge( source, s, m, Kind.NEWINSTANCE );
                        }
                    }
                }
                if( options.verbose() ) {
                    G.v().out.println( "Warning: Method "+source+
                        " is reachable, and calls Constructor.newInstance;"+
                        " graph will be incomplete!"+
                            " Use safe-newinstance option for a conservative result." );
                }
            } 
            */
        }

        public void methodInvoke(MethodOrMethodContext container, Stmt invokeStmt) {
            /*
            if( !warnedAlready(container) ) {
                if( options.verbose() ) {
                    G.v().out.println( "Warning: call to "+
                            "java.lang.reflect.Method: invoke() from "+container+
                            "; graph will be incomplete!" );
                }
                markWarned(container);
            }
            */
        }

        private void markWarned(MethodOrMethodContext m) {
            warnedAlready.add(m);
        }

        private boolean warnedAlready(MethodOrMethodContext m) {
            return warnedAlready.contains(m);
        }

    }


    public class TraceBasedReflectionModel implements ReflectionModel {

        class Guard {
            public Guard(SootMethod container, Stmt stmt, String message) {
                this.container = container;
                this.stmt = stmt;
                this.message = message;
            }
            final SootMethod container;
            final Stmt stmt;
            final String message;
        }

        protected Set<Guard> guards;

        protected ReflectionTraceInfo reflectionInfo;

        private boolean registeredTransformation = false;

        private TraceBasedReflectionModel() {
            guards = new HashSet<Guard>();

            String logFile = options.reflection_log();
            if(logFile==null) {
                throw new InternalError("Trace based refection model enabled but no trace file given!?");
            } else {
                reflectionInfo = new ReflectionTraceInfo(logFile);
            }
        }

        /**
         * Adds an edge to all class initializers of all possible [s
         * of Class.forName() calls within source.
         */
        public void classForName(MethodOrMethodContext container, Stmt forNameInvokeStmt) {
            Set<String> classNames = reflectionInfo.classForNameClassNames(container);
            if(classNames==null || classNames.isEmpty()) {
                registerGuard(container, forNameInvokeStmt, "Class.forName() call site; Soot did not expect this site to be reached");
            } else {
                for (String clsName : classNames) {
                    constantForName( clsName, container, forNameInvokeStmt);
                }
            }
        }

        /**
         * Adds an edge to the constructor of the target class from this call to
         * {@link Class#newInstance()}.
         */
        public void classNewInstance(MethodOrMethodContext container, Stmt newInstanceInvokeStmt) {
            Set<String> classNames = reflectionInfo.classNewInstanceClassNames(container);
            if(classNames==null || classNames.isEmpty()) {
                registerGuard(container, newInstanceInvokeStmt, "Class.newInstance() call site; Soot did not expect this site to be reached");
            } else {
                for (String clsName : classNames) {
                    SootClass cls = Scene.v().getSootClass(clsName);
                    if( cls.declaresMethod(sigInit) ) {
                        SootMethod constructor = cls.getMethod(sigInit);
                        addEdge( container, newInstanceInvokeStmt, constructor, Kind.REFL_CLASS_NEWINSTANCE );
                    }
                }
            }
        }

        /** 
         * Adds a special edge of kind {@link Kind#REFL_CONSTR_NEWINSTANCE} to all possible target constructors
         * of this call to {@link Constructor#newInstance(Object...)}.
         * Those kinds of edges are treated specially in terms of how parameters are assigned,
         * as parameters to the reflective call are passed into the argument array of
         * {@link Constructor#newInstance(Object...)}.
         * @see PAG#addCallTarget(Edge) 
         */
        public void contructorNewInstance(MethodOrMethodContext container, Stmt newInstanceInvokeStmt) {
            Set<String> constructorSignatures = reflectionInfo.constructorNewInstanceSignatures(container);
            if(constructorSignatures==null || constructorSignatures.isEmpty()) {
                registerGuard(container, newInstanceInvokeStmt, "Constructor.newInstance(..) call site; Soot did not expect this site to be reached");
            } else {
                for (String constructorSignature : constructorSignatures) {
                    SootMethod constructor = Scene.v().getMethod(constructorSignature);
                    addEdge( container, newInstanceInvokeStmt, constructor, Kind.REFL_CONSTR_NEWINSTANCE );
                }
            }
        }

        /** 
         * Adds a special edge of kind {@link Kind#REFL_INVOKE} to all possible target methods
         * of this call to {@link Method#invoke(Object, Object...)}.
         * Those kinds of edges are treated specially in terms of how parameters are assigned,
         * as parameters to the reflective call are passed into the argument array of
         * {@link Method#invoke(Object, Object...)}.
         * @see PAG#addCallTarget(Edge) 
         */
        public void methodInvoke(MethodOrMethodContext container, Stmt invokeStmt) {
            Set<String> methodSignatures = reflectionInfo.methodInvokeSignatures(container);
            if (methodSignatures == null || methodSignatures.isEmpty()) {
                registerGuard(container, invokeStmt, "Method.invoke(..) call site; Soot did not expect this site to be reached");
            } else {
                for (String methodSignature : methodSignatures) {
                    SootMethod method = Scene.v().getMethod(methodSignature);
                    addEdge( container, invokeStmt, method, Kind.REFL_INVOKE );
                }
            }
        }

        private void registerGuard(MethodOrMethodContext container, Stmt stmt, String string) {
            guards.add(new Guard(container.method(),stmt,string));

            if(options.verbose()) {
                G.v().out.println("Incomplete trace file: Class.forName() is called in method '" +
                        container+"' but trace contains no information about the receiver class of this call.");
                if(options.guards().equals("ignore")) {
                    G.v().out.println("Guarding strategy is set to 'ignore'. Will ignore this problem.");
                } else if(options.guards().equals("print")) {
                    G.v().out.println("Guarding strategy is set to 'print'. " +
                            "Program will print a stack trace if this location is reached during execution.");
                } else if(options.guards().equals("throw")) {
                    G.v().out.println("Guarding strategy is set to 'throw'. Program will throw an " +
                            "Error if this location is reached during execution.");
                } else {
                    throw new RuntimeException("Invalid value for phase option (guarding): "+options.guards());
                }
            }

            if(!registeredTransformation) {
                registeredTransformation=true;
                PackManager.v().getPack("wjap").add(new Transform("wjap.guards",new SceneTransformer() {

                    @Override
                    protected void internalTransform(String phaseName, Map options) {
                        for (Guard g : guards) {
                            insertGuard(g);
                        }
                    }
                }));
                PhaseOptions.v().setPhaseOption("wjap.guards", "enabled");
            }
        }

        private void insertGuard(Guard guard) {
            if(options.guards().equals("ignore")) return;

            SootMethod container = guard.container;
            Stmt insertionPoint = guard.stmt;
            if(!container.hasActiveBody()) {
                G.v().out.println("WARNING: Tried to insert guard into "+container+" but couldn't because method has no body.");
            } else {
                Body body = container.getActiveBody();

                //exc = new Error
                RefType runtimeExceptionType = RefType.v("java.lang.Error");
                NewExpr newExpr = Jimple.v().newNewExpr(runtimeExceptionType);
                LocalGenerator lg = new LocalGenerator(body);
                Local exceptionLocal = lg.generateLocal(runtimeExceptionType);
                AssignStmt assignStmt = Jimple.v().newAssignStmt(exceptionLocal, newExpr);
                body.getUnits().insertBefore(assignStmt, insertionPoint);

                //exc.<init>(message)
                SootMethodRef cref = runtimeExceptionType.getSootClass().getMethod("<init>", Collections.<Type>singletonList(RefType.v("java.lang.String"))).makeRef();
                SpecialInvokeExpr constructorInvokeExpr = Jimple.v().newSpecialInvokeExpr(exceptionLocal, cref, StringConstant.v(guard.message));
                InvokeStmt initStmt = Jimple.v().newInvokeStmt(constructorInvokeExpr);
                body.getUnits().insertAfter(initStmt, assignStmt);

                if(options.guards().equals("print")) {
                    //exc.printStackTrace();
                    VirtualInvokeExpr printStackTraceExpr = Jimple.v().newVirtualInvokeExpr(exceptionLocal, Scene.v().getSootClass("java.lang.Throwable").getMethod("printStackTrace", Collections.<Type>emptyList()).makeRef());
                    InvokeStmt printStackTraceStmt = Jimple.v().newInvokeStmt(printStackTraceExpr);
                    body.getUnits().insertAfter(printStackTraceStmt, initStmt);
                } else if(options.guards().equals("throw")) {
                    body.getUnits().insertAfter(Jimple.v().newThrowStmt(exceptionLocal), initStmt);
                } else {
                    throw new RuntimeException("Invalid value for phase option (guarding): "+options.guards());
                }
            }
        }

    }

    /** context-insensitive stuff */
    private final CallGraph cicg = new CallGraph();
    private final HashSet<MethodOrMethodContext> analyzedMethods = new HashSet<MethodOrMethodContext>();

    private final HashMap<VarNode, List<VirtualCallSite>> receiverToSites; 


    private final Map<MethodOrMethodContext, List<VarNode>> methodToReceivers =
            new HashMap<MethodOrMethodContext, List<VarNode>>( Scene.v().getMethodNumberer().size() );
    public Map<MethodOrMethodContext, List<VarNode>> methodToReceivers() { return methodToReceivers; }

    private final SmallNumberedMap<List<VirtualCallSite>> stringConstToSites = new SmallNumberedMap<List<VirtualCallSite>>( Scene.v().getLocalNumberer() ); // Local -> List(VirtualCallSite)
    private final Map<MethodOrMethodContext, List<Local>> methodToStringConstants = 
            new HashMap<MethodOrMethodContext, List<Local>>( Scene.v().getMethodNumberer().size() ); // SootMethod -> List(Local)
    public Map<MethodOrMethodContext, List<Local>> methodToStringConstants() { return methodToStringConstants; }

    private CGOptions options;

    private boolean appOnly;

    /** context-sensitive stuff */
    private ReachableMethods rm;
    private QueueReader worklist;

    private ContextManager cm;

    private PAG pag;

    private final ChunkedQueue targetsQueue = new ChunkedQueue();
    private final QueueReader targets = targetsQueue.reader();


    public OnFlyCallGraphBuilder( PAG pag, ContextManager cm, ReachableMethods rm ) {
        if (UNSOUND_REFLECTION_CLINIT_CALLS)
            System.out.println("Info: Not accounting for newInstance(String) calls.");
        this.cm = cm;
        this.rm = rm;
        this.pag = pag;
        worklist = rm.listener();
        //initialize the receiver to sites map with the number of locals * an estimate for the number of contexts per methods
        receiverToSites = new HashMap<VarNode, List<VirtualCallSite>>(Scene.v().getLocalNumberer().size()); 
        options = new CGOptions( PhaseOptions.v().getPhaseOptions("cg") );
        if( !options.verbose() ) {
            G.v().out.println( "[Call Graph] For information on where the call graph may be incomplete, use the verbose option to the cg phase." );
        }

        if(options.reflection_log()==null || options.reflection_log().length()==0) {
            reflectionModel = new DefaultReflectionModel();
        } else {
            reflectionModel = new TraceBasedReflectionModel();
        }
    }

    public OnFlyCallGraphBuilder( ContextManager cm, ReachableMethods rm, boolean appOnly ) {
        this( null, cm, rm );
        this.appOnly = appOnly;
    }


    public void processReachables() {

        while(true) {
            if( !worklist.hasNext() ) {
                rm.update();
                if( !worklist.hasNext() ) break;
            }
            MethodOrMethodContext momc = (MethodOrMethodContext) worklist.next();

            if( appOnly && !momc.method().getDeclaringClass().isApplicationClass() ) continue;

            if( analyzedMethods.add( momc ) ) processNewMethod( momc );
            processNewMethodContext( momc );
        }
    }

    //used to determine if an update to the pag should notify the call graph
    //called by pag
    public boolean wantTypes( VarNode vn) {
        //sSparkTransformer.println("OFCGG: wantTypes " + vn + " " + vn.hashCode() + " " + (receiverToSites.get(vn) != null));

        if (vn == null)
            throw new RuntimeException("Null varnode in OnFlyCallGraphBuilder");

        return receiverToSites.get(vn) != null;
    }

    //used to update the call graph with additional points to information for the local 
    //and context, the last context is the context for the target (ObjectSensitiveAllocNode)
    public void addType( VarNode receiver, Type type, AllocNode receiverNode, boolean debug ) {
        FastHierarchy fh = Scene.v().getOrMakeFastHierarchy();

        //calculate if we want to add context for this alloc node
        Context tgtContext = NoContext.v();

        if (receiverNode instanceof Context)
            tgtContext = (Context)receiverNode;


        //SparkTransformer.println("OFCB: addType " + receiver + " " + tgtContext);

        for( Iterator siteIt = ((Collection) receiverToSites.get( receiver )).iterator(); siteIt.hasNext(); ) {
            final VirtualCallSite site = (VirtualCallSite) siteIt.next();
            InstanceInvokeExpr iie = site.iie();
  
            if( site.kind() == Kind.THREAD 
                    && !fh.canStoreType( type, clRunnable ) )
                continue;

            if( site.iie() instanceof SpecialInvokeExpr && site.kind != Kind.THREAD ) {
                SootMethod target = VirtualCalls.v().resolveSpecial( 
                    (SpecialInvokeExpr) site.iie(),
                    site.subSig(),
                    site.container().method() );
                //if the call target resides in a phantom class then "target" will be null;
                //simply do not add the target in that case
                if(target!=null) {
                    targetsQueue.add( target );            		
                } 
            } else {
                /*if (debug) 
                    SparkTransformer.printf("\tResolve: %s %s %s %s\n", type, 
                        receiver.getType(), site.subSig(), site.container().method());*/

                VirtualCalls.v().resolve( type,
                    receiver.getType(),
                    site.subSig(),
                    site.container().method(), 
                    targetsQueue );
            }

            while(targets.hasNext()) {
                
                SootMethod target = (SootMethod) targets.next();
                               
                cm.addVirtualEdge(
                    site.container(),
                    site.stmt(),
                    target,
                    site.kind(),
                    tgtContext );
            }
        }
    }
    public boolean wantStringConstants( Local stringConst ) {

        return stringConstToSites.get(stringConst) != null;
    }
    public void addStringConstant( Local l, String constant ) {
        if (UNSOUND_REFLECTION_CLINIT_CALLS)
            return ;

        for( Iterator siteIt = (stringConstToSites.get( l )).iterator(); siteIt.hasNext(); ) {
            final VirtualCallSite site = (VirtualCallSite) siteIt.next();
            if( constant == null ) {
                if( options.verbose() ) {
                    G.v().out.println( "Warning: Method "+site.container()+
                        " is reachable, and calls Class.forName on a"+
                        " non-constant String; graph will be incomplete!"+
                            " Use safe-forname option for a conservative result." );
                }
            } else {
                if( constant.length() > 0 && constant.charAt(0) == '[' ) {
                    if( constant.length() > 1 && constant.charAt(1) == 'L' 
                            && constant.charAt(constant.length()-1) == ';' ) {
                        constant = constant.substring(2,constant.length()-1);
                    } else continue;
                }
                
                if( !Scene.v().containsClass( constant ) ) {
                    if( options.verbose() ) {
                        G.v().out.println( "Warning: Class "+constant+" is"+
                                " a dynamic class, and you did not specify"+
                                " it as such; graph will be incomplete!" );
                    }
                } else {
                    SootClass sootcls = Scene.v().getSootClass( constant );
                    if( !sootcls.isApplicationClass() ) {
                        sootcls.setLibraryClass();
                    }
                    for (SootMethod clinit : EntryPoints.v().clinitsOf(sootcls)) {
                        System.out.println("Adding static edge for string constant used in reflection: " + site.container() + " -> " +
                                clinit);

                        Context context = null;

                        if (ObjectSensitiveConfig.isObjectSensitive())
                            context = NoContext.v();

                        cm.addStaticEdge(
                            site.container(),
                            site.stmt(),
                            clinit,
                            Kind.CLINIT, context );
                    }
                }
            }
        }
    }

    /* End of public methods. */

    private void addVirtualCallSite( Stmt s, MethodOrMethodContext m, VarNode receiver,
                                     InstanceInvokeExpr iie, NumberedString subSig, Kind kind ) {
        if (receiver == null)
            throw new RuntimeException("Null receiver node in OnFlyCallGraphBuilder");

        List<VirtualCallSite> sites = (List<VirtualCallSite>) receiverToSites.get(receiver);

        if (sites == null) {
            receiverToSites.put(receiver, sites = new ArrayList<VirtualCallSite>());
            List<VarNode> receivers = (List<VarNode>) methodToReceivers.get(m);
            if( receivers == null )
                methodToReceivers.put(m, receivers = new ArrayList<VarNode>());
            receivers.add(receiver);
        }

        //SparkTransformer.println("OFCGB: addVirtualCallSite: " + receiver + " " + receiver.hashCode() + " " + iie);

        sites.add(new VirtualCallSite(s, m, iie, subSig, kind));
    }

    private void processNewMethod( MethodOrMethodContext momc) {
        SootMethod method = momc.method();
        if( method.isNative() || method.isPhantom() ) {
            return;
        }
        Body b = method.retrieveActiveBody();
        getImplicitTargets( momc );
        findReceivers(momc, b);
    }

    private void findReceivers(MethodOrMethodContext m, Body b) {
        for( Iterator sIt = b.getUnits().iterator(); sIt.hasNext(); ) {
            final Stmt s = (Stmt) sIt.next();
            if (s.containsInvokeExpr()) {
                InvokeExpr ie = s.getInvokeExpr();

                if (ie instanceof InstanceInvokeExpr) {
                    InstanceInvokeExpr iie = (InstanceInvokeExpr) ie;
                    Local receiver = (Local) iie.getBase();
                    VarNode recNode = null;

                    //find the context or non-context node for the receiver local
                    if (ObjectSensitiveConfig.isObjectSensitive() && m.context() == null)
                        throw new RuntimeException("With object sensitive context should never be null!");

                    if (m.context() != null)
                        recNode = pag.makeContextVarNode(receiver, receiver.getType(),m.context(), m.method());
                    else 
                        recNode = pag.makeLocalVarNode(receiver, receiver.getType(), m.method());

                    if (recNode == null)
                        throw new RuntimeException("Null recnode in OnFlyCallGraphBuilder");

                    NumberedString subSig = 
                            iie.getMethodRef().getSubSignature();
                    addVirtualCallSite( s, m, recNode,  iie, subSig,
                        Edge.ieToKind(iie) );
                    if( subSig == sigStart ) {
                        addVirtualCallSite( s, m, recNode, iie, sigRun,
                            Kind.THREAD );
                    }
                } else {
                    SootMethod tgt = ie.getMethod();
                    if(tgt!=null) {
                        //static invoke or dynamic invoke
                        //SparkTransformer.println("OFCGB: findReceivers: " + m + " " + s + " " + tgt);
                        addEdge(m, s, tgt);

                        /* not needed for android
                        if( tgt.getSignature().equals( "<java.security.AccessController: java.lang.Object doPrivileged(java.security.PrivilegedAction)>" )
                                ||  tgt.getSignature().equals( "<java.security.AccessController: java.lang.Object doPrivileged(java.security.PrivilegedExceptionAction)>" )
                                ||  tgt.getSignature().equals( "<java.security.AccessController: java.lang.Object doPrivileged(java.security.PrivilegedAction,java.security.AccessControlContext)>" )
                                ||  tgt.getSignature().equals( "<java.security.AccessController: java.lang.Object doPrivileged(java.security.PrivilegedExceptionAction,java.security.AccessControlContext)>" ) ) 
                        {

                            Local receiver = (Local) ie.getArg(0);
                            addVirtualCallSite( s, m, receiver, context, null, sigObjRun,
                                Kind.PRIVILEGED );
                        }
                         */
                    } else {
                        if(!Options.v().ignore_resolution_errors()) {
                            throw new InternalError("Unresolved target "+ie.getMethod()+". Resolution error should have occured earlier.");
                        }
                    }
                }
            }
        }
    }

    ReflectionModel reflectionModel;

    private void getImplicitTargets( MethodOrMethodContext source) {
        SootMethod sourceMethod = source.method();

        final SootClass scl = sourceMethod.getDeclaringClass();
        if( sourceMethod.method().isNative() || sourceMethod.isPhantom() ) return;
        if( sourceMethod.getSubSignature().indexOf( "<init>" ) >= 0 && source.context() instanceof AllocNode) {
            //handle finalize
            RefType type = (RefType)((AllocNode)source.context()).getType();

            SootMethod target = VirtualCalls.v().resolveNonSpecial(type, sigFinalize);

            if (target != null)
                cm.addVirtualEdge(source, null, target, Kind.FINALIZE, source.context());
        }
        Body b = sourceMethod.retrieveActiveBody();
        for( Iterator sIt = b.getUnits().iterator(); sIt.hasNext(); ) {
            final Stmt s = (Stmt) sIt.next();
            if( s.containsInvokeExpr() ) {
                InvokeExpr ie = s.getInvokeExpr();
                if( ie.getMethodRef().getSignature().equals( "<java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>" ) ) {
                    reflectionModel.methodInvoke(source,s);
                }
                if( ie.getMethodRef().getSignature().equals( "<java.lang.Class: java.lang.Object newInstance()>" ) ) {
                    reflectionModel.classNewInstance(source,s);
                }
                if( ie.getMethodRef().getSignature().equals( "<java.lang.reflect.Constructor: java.lang.Object newInstance(java.lang.Object[])>" ) ) {
                    reflectionModel.contructorNewInstance(source, s);
                }
                if( ie.getMethodRef().getSubSignature() == sigForName ) {
                    reflectionModel.classForName(source,s);
                }
                if( ie instanceof StaticInvokeExpr ) {
                    SootClass cl = ie.getMethodRef().declaringClass();
                    for (SootMethod clinit : EntryPoints.v().clinitsOf(cl)) {
                        addEdge( source, s, clinit, Kind.CLINIT);
                    }
                }
            }
            if( s.containsFieldRef() ) {
                FieldRef fr = s.getFieldRef();
                if( fr instanceof StaticFieldRef ) {
                    SootClass cl = fr.getFieldRef().declaringClass();
                    for (SootMethod clinit : EntryPoints.v().clinitsOf(cl)) {
                        addEdge( source, s, clinit, Kind.CLINIT );
                    }
                }
            }
            if( s instanceof AssignStmt ) {
                Value rhs = ((AssignStmt)s).getRightOp();
                if( rhs instanceof NewExpr ) {
                    NewExpr r = (NewExpr) rhs;
                    SootClass cl = r.getBaseType().getSootClass();
                    for (SootMethod clinit : EntryPoints.v().clinitsOf(cl)) {
                        addEdge( source, s, clinit, Kind.CLINIT );
                    }
                } else if( rhs instanceof NewArrayExpr || rhs instanceof NewMultiArrayExpr ) {
                    Type t = rhs.getType();
                    if( t instanceof ArrayType ) t = ((ArrayType)t).baseType;
                    if( t instanceof RefType ) {
                        SootClass cl = ((RefType) t).getSootClass();
                        for (SootMethod clinit : EntryPoints.v().clinitsOf(cl)) {
                            addEdge( source, s, clinit, Kind.CLINIT );
                        }
                    }
                }
            }
        }
    }

    private void processNewMethodContext( MethodOrMethodContext momc ) { 
        Object ctxt = momc.context();
        Iterator it = cicg.edgesOutOf(momc);
        while( it.hasNext() ) {
            Edge e = (Edge) it.next();                                              
            cm.addStaticEdge( momc, e.srcUnit(), e.tgt(), e.kind(), momc.context() );            
            //SparkTransformer.println("OFCGB: processNewMethodContext: " + e);
        }
    }

    private void constantForName( String cls, MethodOrMethodContext src, Stmt srcUnit ) {
        if( cls.length() > 0 && cls.charAt(0) == '[' ) {
            if( cls.length() > 1 && cls.charAt(1) == 'L' && cls.charAt(cls.length()-1) == ';' ) {
                cls = cls.substring(2,cls.length()-1);
                constantForName( cls, src, srcUnit );
            }
        } else {
            if( !Scene.v().containsClass( cls ) ) {
                if( options.verbose() ) {
                    G.v().out.println( "Warning: Class "+cls+" is"+
                            " a dynamic class, and you did not specify"+
                            " it as such; graph will be incomplete!" );
                }
            } else {

                SootClass sootcls = Scene.v().getSootClass( cls );
                if (!sootcls.isPhantomClass()) {
                    if( !sootcls.isApplicationClass() ) {
                        sootcls.setLibraryClass();
                    }
                    for (SootMethod clinit : EntryPoints.v().clinitsOf(sootcls)) {
                        addEdge( src, srcUnit,  clinit, Kind.CLINIT );
                        System.out.println("Adding clinit for reflective for/getName(): " + sootcls);
                    }
                }

            }
        }
    }

    //instance invoke for reflection and other stuff that has no context, don't add to 
    //global call graph, just add to local temp callgraph
    //is added to program call graph by processNewMethodContext()
    private void addEdge( MethodOrMethodContext src, Stmt stmt, SootMethod tgt, 
                          Kind kind ) {
        cicg.addEdge( new Edge( src, stmt, tgt, kind ) );
    }


    //used for static invoke probably
    private void addEdge( MethodOrMethodContext src, Stmt stmt, SootMethod tgt ) {
        InvokeExpr ie = stmt.getInvokeExpr();
        addEdge( src, stmt, tgt, Edge.ieToKind(ie) );
    }

    protected final NumberedString sigFinalize = Scene.v().getSubSigNumberer().
            findOrAdd( "void finalize()" );
    protected final NumberedString sigInit = Scene.v().getSubSigNumberer().
            findOrAdd( "void <init>()" );
    protected final NumberedString sigStart = Scene.v().getSubSigNumberer().
            findOrAdd( "void start()" );
    protected final NumberedString sigRun = Scene.v().getSubSigNumberer().
            findOrAdd( "void run()" );
    protected final NumberedString sigObjRun = Scene.v().getSubSigNumberer().
            findOrAdd( "java.lang.Object run()" );
    protected final NumberedString sigForName = Scene.v().getSubSigNumberer().
            findOrAdd( "java.lang.Class forName(java.lang.String)" );
    protected final RefType clRunnable = RefType.v("java.lang.Runnable");

}

