package soot.jimple.spark;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import soot.Local;
import soot.MethodOrMethodContext;
import soot.PointsToSet;
import soot.RefLikeType;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.spark.geom.geomPA.Constants;
import soot.jimple.spark.internal.TypeManager;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.GlobalVarNode;
import soot.jimple.spark.pag.LocalVarNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.ObjectSensitiveAllocNode;
import soot.jimple.spark.pag.ObjectSensitiveConfig;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetInternal;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.util.queue.QueueReader;

/**
 * Gather stats on the performance and precision of a PTA run. 
 * 
 * Each new run of Spark will over-write stats
 * 
 * - Wall time (sec)
 * - Memory (max, current) before and after
 * - Reachable methods (context and no-context)
 * - Total reachable casts
 * - Reachable casts that may fail
 * - Call graph edges
 * - Context sensitive call graph edges
 * - Total reachable virtual call sites
 * - Polymorphic virtual call sites (sites with >1 target methods)
 * - Number of pointers (local and global)
 * - Total points to sets size (local and global) context insensitive (convert to alloc site) 
 * 
 * @author mgordon
 *
 */
public class SparkEvaluator {
    /** static singleton */
    private static SparkEvaluator v;

    private Date startTime;

    private StringBuffer report;

    private int GB = 1024*1024*1024;

    /** all method reachable from the harness main */
    private Set<SootMethod> reachableMethods;
    /** add method + contexts that are reachable */
    private Set<MethodOrMethodContext> reachableMethodContexts;
    /** Map of SootMethod to its contexts (including the sootmethod itself if no context) **/
    private HashMap<SootMethod, Set<MethodOrMethodContext>> methodToContexts;

    private PAG ptsProvider;

    private boolean fails;

    private RefType exceptionType;

    private SparkEvaluator() {
        report = new StringBuffer();
        exceptionType = RefType.v( "java.lang.Throwable" );
    }

    public static SparkEvaluator v() {
        if (v == null) 
            v = new SparkEvaluator();

        return v;        
    }
    
    public static void reset() {
        v = null;
    }
    
    /**
     * Return set of all reachable methods (insensitive).
     */
    public Set<SootMethod> getReachableMethods() {
        return reachableMethods;        
    }

    /** 
     * Return set of all method x context (or just method if run without context).
     */
    public Set<MethodOrMethodContext> getReachableMethodContexts() {
        return reachableMethodContexts;
    }

    /**
     * Return set of contexts (method x context) for a soot method
     * @param method
     * @return
     */
    public Set<MethodOrMethodContext> getContexts(SootMethod method) {
        if (!methodToContexts.containsKey(method)) {
            return Collections.<MethodOrMethodContext>emptySet();
        }

        return methodToContexts.get(method);
    }

    /**
     * Note the start of a pta run.
     */
    public void begin() {              
        //Getting the runtime reference from system
        Runtime runtime = Runtime.getRuntime();

        addLine(" ====== Pre-Analysis Stats ======");

        //Print used memory
        addLine("Used Memory Before: "
                + (runtime.totalMemory() - runtime.freeMemory()) / GB + " GB");

        //Print free memory
        addLine("Free Memory Before: "
                + runtime.freeMemory() / GB + " GB");

        //Print total available memory
        addLine("Total Memory Before: " + runtime.totalMemory() / GB + " GB");

        //Print Maximum available memory
        addLine("Max Memory Before: " + runtime.maxMemory() / GB + " GB");

        //get current date time with Date()
        startTime = new Date();
    }

    /**
     * Note the end of a pta run.
     */
    public void end() {
        //done with processing
        Date endTime = new Date();

        ptsProvider = (PAG)Scene.v().getPointsToAnalysis();

        addLine(" ====== Post-Analysis Stats ======");

        long elapsedTime = endTime.getTime() - startTime.getTime();

        addLine("Time (sec): " + (((double)elapsedTime) / 1000.0) );

        //memory stats

        //Getting the runtime reference from system
        Runtime runtime = Runtime.getRuntime();

        //Print used memory
        addLine("Used Memory After: "
                + (runtime.totalMemory() - runtime.freeMemory()) / GB + " GB");

        //Print free memory
        addLine("Free Memory After: "
                + runtime.freeMemory() / GB + " GB");

        //Print total available memory
        addLine("Total Memory After: " + runtime.totalMemory() / GB + " GB");

        //Print Maximum available memory
        addLine("Max Memory After: " + runtime.maxMemory() / GB + " GB");

        callGraphProcessing();

        stmtProcessing();

        nodeProcessing();

        if (ObjectSensitiveConfig.isObjectSensitive())
            addLine("Number of heap objects: " + ObjectSensitiveAllocNode.numberOfObjSensNodes());
        else
            addLine("Number of heap objects: " + ptsProvider.getNumAllocNodes());
    }

    private boolean isExceptionType(Type type) {
        if ( type instanceof RefType ) {
            SootClass sc = ((RefType)type).getSootClass();
            if ( !sc.isInterface() && Scene.v().getActiveHierarchy().isClassSubclassOfIncluding(
                sc, exceptionType.getSootClass()) ) {
                return true;
            }
        }

        return false;
    }

    private void nodeProcessing() {
        int totalGlobalPointers = 0;
        int appGlobalPointers = 0;
        int totalGlobalPointsTo = 0;
        int appGlobalPointsTo = 0;

        int totalLocalPointers = 0;
        int totalLocalPointsTo = 0;
        int appLocalPointers = 0;
        int appLocalPointsTo = 0;  

        //locals       
        for (Local local : ptsProvider.getLocalPointers()) {
            try {
                //don't count exceptions
                if (isExceptionType(local.getType()))
                    continue;

                LocalVarNode varNode = ptsProvider.findLocalVarNode(local);

                SootMethod sm = varNode.getMethod();
                boolean app = !(sm.isJavaLibraryMethod() || sm.isAndroidLibraryMethod());

                totalLocalPointers++;
                if (app) appLocalPointers++;

                final Set<Object> allocSites = new HashSet<Object>();

                PointsToSetInternal pts = (PointsToSetInternal)ptsProvider.reachingObjects(local);
                //if context sensitive, we could get context sensitive heap object in the pt set
                //so we extract the alloc site, and count those to get a number of context insens
                //heap sites
                pts.forall(new P2SetVisitor() {
                    @Override
                    public void visit(Node n) {
                        allocSites.add(((AllocNode)n).getNewExpr());                        
                    }});

                totalLocalPointsTo += allocSites.size();
                if (app) appLocalPointsTo += allocSites.size();

            } catch (Exception e) {
                //just continue on exception
            }
        }
        
        //globals
        for (Object global : ptsProvider.getGlobalPointers()) {
            try { 
                if (!(global instanceof SootField))
                    continue;

                GlobalVarNode varNode = ptsProvider.findGlobalVarNode(global);

                SootClass sc = varNode.getDeclaringClass();
                boolean app = !(sc.isJavaLibraryClass() || sc.isAndroidLibraryClass());


                totalGlobalPointers++;
                if (app) appGlobalPointers++;

                final Set<Object> allocSites = new HashSet<Object>();

                PointsToSetInternal pts = (PointsToSetInternal)ptsProvider.reachingObjects((SootField)global);
                //if context sensitive, we could get context sensitive heap object in the pt set
                //so we extract the alloc site, and count those to get a number of context insens
                //heap sites
                pts.forall(new P2SetVisitor() {
                    @Override
                    public void visit(Node n) {
                        allocSites.add(((AllocNode)n).getNewExpr());                        
                    }});

                totalGlobalPointsTo += allocSites.size();
                if (app) appGlobalPointsTo += allocSites.size();

            } catch (Exception e) {
                //just continue on exception
            }

        }

        addLine("Total Global Pointers: " + totalGlobalPointers);
        addLine("Total Global Points To: "+ totalGlobalPointsTo);
        addLine("App Global Pointers: " + appGlobalPointers);
        addLine("App Global Points To: " + appGlobalPointsTo);
        addLine("Total Global Avg Points-To: " + ((double)totalGlobalPointsTo)/((double)totalGlobalPointers));
        addLine("App Global Avg Points-To: " + ((double)appGlobalPointsTo)/((double)appGlobalPointers));
        addLine("Total Local Pointers: " + totalLocalPointers);
        addLine("Total Local Points To: " + totalLocalPointsTo);
        addLine("App Local Pointers: " + appLocalPointers);
        addLine("App Local Points To: " + appLocalPointsTo);
        addLine("Total Local Avg Points-To: " + ((double)totalLocalPointsTo)/((double)totalLocalPointers));
        addLine("App Local Avg Points-To: " + ((double)appLocalPointsTo)/((double)appLocalPointers));
    }


    private void stmtProcessing() {
        // Get type manager from Soot
        final TypeManager typeManager = ptsProvider.getTypeManager();
        CallGraph callGraph = Scene.v().getCallGraph();

        int totalCasts = 0;
        int appCasts = 0;
        int totalCastsNeverFail = 0;
        int appCastsNeverFail = 0;
        int totalVirtualCalls = 0;
        int appVirtualCalls = 0;
        int totalPolyCalls = 0;
        int appPolyCalls = 0;

        //loop over all reachable method's statement
        //find casts, local references, virtual call sites
        for (SootMethod sm : reachableMethods) {
            if (!sm.isConcrete())
                continue;
            if (!sm.hasActiveBody()) {
                sm.retrieveActiveBody();
            }

            boolean app = !(sm.isJavaLibraryMethod() || sm.isAndroidLibraryMethod());

            // All the statements in the method
            for (Iterator stmts = sm.getActiveBody().getUnits().iterator(); stmts.hasNext();) {
                Stmt st = (Stmt) stmts.next();
                try {

                    //casts
                    if (st instanceof AssignStmt) {
                        Value rhs = ((AssignStmt) st).getRightOp();
                        Value lhs = ((AssignStmt) st).getLeftOp();
                        if (rhs instanceof CastExpr
                                && lhs.getType() instanceof RefLikeType) {

                            final Type targetType = (RefLikeType) ((CastExpr) rhs).getCastType();

                            Value v = ((CastExpr) rhs).getOp();

                            if (!(v instanceof Local))
                                continue;

                            totalCasts++;
                            if (app) appCasts++;

                            PointsToSetInternal opPts = (PointsToSetInternal)ptsProvider.reachingObjects((Local)v);

                            fails = false;

                            opPts.forall(new P2SetVisitor() {
                                @Override
                                public void visit(Node n) {
                                    if (fails) return;

                                    AllocNode an = (AllocNode) n;
                                    fails = !typeManager.castNeverFails(n.getType(), targetType);
                                }                                
                            });

                            if (!fails) {
                                totalCastsNeverFail++;
                                if (app) appCastsNeverFail++;
                            }
                        }
                    }

                    //virtual calls
                    if (st.containsInvokeExpr()) {
                        InvokeExpr ie = st.getInvokeExpr();
                        if (ie instanceof VirtualInvokeExpr) {
                            totalVirtualCalls++;
                            if (app) appVirtualCalls++;
                            Local l = (Local) ((VirtualInvokeExpr)ie).getBase();

                            //have to check target soot method, cannot just count edges
                            Set<SootMethod> targets = new HashSet<SootMethod>();
                            
                            for ( Iterator<Edge> it = callGraph.edgesOutOf(st); it.hasNext(); ) {
                                Edge edge = it.next();
                                targets.add(edge.tgt());
                            }

                            if (targets.size() > 1) {
                                totalPolyCalls++;
                                if (app) appPolyCalls++;
                            }
                        }
                    }

                } catch (Exception e) {
                    //just ignore all exceptions in stats gathering
                }

            }
        }
        addLine("Total Casts: " + totalCasts);
        addLine("App Casts: " + appCasts);
        addLine("Total Casts Never Fail: " + totalCastsNeverFail);
        addLine("App Casts Never Fail: " + appCastsNeverFail);
        addLine("Total Virtual Calls: " + totalVirtualCalls);
        addLine("App Virtual Calls: " + appVirtualCalls);
        addLine("Total Polymorphic Calls: " + totalPolyCalls);
        addLine("App Polymorphic Calls: " + appPolyCalls);
    }

    private void callGraphProcessing() {
        CallGraph callGraph = Scene.v().getCallGraph();

        //fill reachable methods map
        reachableMethods = new LinkedHashSet<SootMethod>();
        reachableMethodContexts = new LinkedHashSet<MethodOrMethodContext>();
        methodToContexts = new LinkedHashMap<SootMethod, Set<MethodOrMethodContext>>();
        Set<InsensEdge> insEdges = new HashSet<InsensEdge>();

        QueueReader<MethodOrMethodContext> qr = Scene.v().getReachableMethods().listener();

        while (qr.hasNext()) {
            MethodOrMethodContext momc = qr.next();

            reachableMethodContexts.add(momc);
            reachableMethods.add(momc.method());

            if (!methodToContexts.containsKey(momc.method()))
                methodToContexts.put(momc.method(), new LinkedHashSet<MethodOrMethodContext>());

            methodToContexts.get(momc.method()).add(momc);

            Iterator<Edge> iterator = callGraph.edgesInto(momc);
            while (iterator.hasNext()) {
                Edge e = iterator.next();
                InsensEdge insEdge = new InsensEdge(e);
                insEdges.add(insEdge);                
            }
        }

        addLine("Reachable Methods: " + reachableMethods.size() );
        addLine("Reachable Method Contexts: " + reachableMethodContexts.size() );        
        addLine("Callgraph Edges (Insens): " + insEdges.size());
        addLine("Callgraph Edges (CS): " + callGraph.size());

    }

    private void addLine(String str) {
        report.append(str + System.getProperty("line.separator"));
    }

    public String toString() {
        return report.toString();
    }

    class InsensEdge {
        SootMethod src;
        SootMethod dst;
        Unit srcUnit;

        public InsensEdge(Edge edge) {
            this.src = edge.src();
            this.dst = edge.tgt();
            srcUnit = edge.srcUnit();            
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((dst == null) ? 0 : dst.hashCode());
            result = prime * result + ((src == null) ? 0 : src.hashCode());
            result = prime * result + ((srcUnit == null) ? 0 : srcUnit.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            InsensEdge other = (InsensEdge) obj;
            if (!getOuterType().equals(other.getOuterType())) return false;
            if (dst == null) {
                if (other.dst != null) return false;
            } else if (!dst.equals(other.dst)) return false;
            if (src == null) {
                if (other.src != null) return false;
            } else if (!src.equals(other.src)) return false;
            if (srcUnit == null) {
                if (other.srcUnit != null) return false;
            } else if (!srcUnit.equals(other.srcUnit)) return false;
            return true;
        }

        private SparkEvaluator getOuterType() {
            return SparkEvaluator.this;
        }

        

    }
}
