package soot.jimple.spark.pag;

import java.util.HashSet;
import java.util.Set;

import soot.ArrayType;
import soot.Context;
import soot.Hierarchy;
import soot.MethodOrMethodContext;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.jimple.spark.sets.PointsToSetInternal;

public class ObjectSensitiveConfig {

    private boolean contextForStaticInits = false;

    /** list of classes for which we do not add context */
    private Set<SootClass> ignoreList;

    private Set<SootClass> importantAllocators;

    private Set<SootClass> limitHeapContext;

    private static ObjectSensitiveConfig v;

    private boolean typesForContextGTOne;

    private Set<SootClass> stringClasses;

    private static Set<Object> newExprsForNoContext;

    /** depth of the object sensitivity on heap and method */
    private int k = 0;

    private int minK = 0;

    private boolean naiveDecay = false;

    private ObjectSensitiveConfig(int k, int mink, String csl, String importantAs, String limitHeapContext,
                                  boolean contextForStaticInits, boolean typesForContextGTOne,
                                  boolean naiveDecay) {
        if (minK > k)
            throw new RuntimeException("mink cannot be greater than k for object sensitive pta");

        this.k = k;
        this.minK = mink;


        this.naiveDecay = naiveDecay;
        this.contextForStaticInits = contextForStaticInits;
        this.typesForContextGTOne = typesForContextGTOne;

        installNoContextList(csl);
        installImportantAllocators(importantAs);
        installLimitHeapContext(limitHeapContext);

        stringClasses = new HashSet<SootClass>();
        stringClasses.add(Scene.v().getSootClass("java.lang.String"));
        stringClasses.add(Scene.v().getSootClass("java.lang.StringBuilder"));
        stringClasses.add(Scene.v().getSootClass("java.lang.StringBuffer"));
    }

    public static boolean isObjectSensitive() {
        return (v != null && v.k > 0); 
    }

    public static void noObjectSens() {
        v = null;
    }

    public static void setNewExprsNoContext(Set<Object> set) {
        newExprsForNoContext = set;
    }

    public static boolean isNewExprNoContext(Object newExpr) {
        return newExprsForNoContext != null && newExprsForNoContext.contains(newExpr);
    }

    public int minK() {
        return Math.min(minK, k);
    }



    private void installImportantAllocators(String isa) {

        importantAllocators = new HashSet<SootClass>();

        if (isa == null || isa.isEmpty())
            return;

        String[] classes = isa.split(",");

        for (String str : classes) {
            str = str.trim();
            SootClass clz = tryToGetClass(str);
            if (clz != null && !clz.isInterface())
                importantAllocators.add(clz);
        }
    }


    private void installLimitHeapContext(String lhc) {

        limitHeapContext = new HashSet<SootClass>();

        if (lhc == null || lhc.isEmpty())
            return;

        String[] classes = lhc.split(",");

        for (String str : classes) {
            str = str.trim();
            SootClass clz = tryToGetClass(str);
            if (clz != null && !clz.isInterface()) {
                limitHeapContext.add(clz);
            }
        }
    }

    private SootClass tryToGetClass(String cls) {
        try {
            SootClass clz = Scene.v().getSootClass(cls);
            return clz;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Install no context list for classes given plus all subclasses.
     */
    private void installNoContextList(String csl) {
        ignoreList = new HashSet<SootClass>();

        if (csl == null || csl.isEmpty())
            return;

        Hierarchy h = Scene.v().getActiveHierarchy();

        String[] classes = csl.split(",");

        for (String str : classes) {
            str = str.trim();
            //System.out.println("Adding class plus subclasses to ignore list: " + str);
            SootClass clz = tryToGetClass(str);
            if (clz != null)
                ignoreList.add(clz);
        }
    }

    public boolean isImportantAlloc(SootMethod m) {
        return isImportantAlloc(m.getDeclaringClass());
    }

    public boolean isImportantAlloc(SootClass c) {
        return importantAllocators.isEmpty() || importantAllocators.contains(c);
    }

    public int k() {
        return k;
    }

    public static void initialize(int k, int mink, String noContextList, String importantAllocators,  
                                  String limitHeapContext, boolean contextForStaticInits,
                                  boolean typesForContextGTOne, boolean naiveDecay) {
        v = null;
        v = new ObjectSensitiveConfig(k, mink, noContextList, importantAllocators, limitHeapContext, 
            contextForStaticInits, typesForContextGTOne, naiveDecay);
    }

    public boolean contextForStaticInits() {
        return contextForStaticInits;
    }

    public boolean typesForContextGTOne() {
        return typesForContextGTOne;
    }

    public static ObjectSensitiveConfig v() {
        return v;
    }

    /**
     * Limit heap context to 1 for these classes.
     * 
     * @param clz
     * @return
     */
    public boolean limitHeapContext(AllocNode base) {
        //limit class constant context
        if (base instanceof ClassConstantNode)
            return true;

        if (base.getType() instanceof RefType) {
            SootClass clz = ((RefType)base.getType()).getSootClass();
            if (limitHeapContext.contains(clz))
                return true;
        }

        return false;
    }

    public boolean addHeapContext(AllocNode probe) {
        if (isNewExprNoContext(probe.getNewExpr()))
            return false;

        //shortcircuit below computation
        if (ignoreList.isEmpty())
            return true;

        //check if the type that is allocated should never has context because
        //it is on the ignore List
        SootClass allocated = getSootClass(probe.getType());

        if (allocated != null) {
            //first check if on the no context list, that trumps all
            if (ignoreList.contains(allocated)) 
                return false;
        }

        return true;

    }

    public int contextDepth(ObjectSensitiveAllocNode probe, Context context) {
        if (!addHeapContext(probe))
            return 0;

        //now check if we have defined important allocators, we check to see if the starting context is an important
        //allocator
        if (importantAllocators.isEmpty())
            return k;

        SootClass allocated = getSootClass(probe.getType());

        if (allocated != null && importantAllocators.contains(allocated)) {
            return k;
        }   

        //if we have made it here, then the allocated type is not important

        if (naiveDecay) {
            return minK;
        } else {
            //smarter decision hopefully...
            if (context instanceof ObjectSensitiveAllocNode) {
                ObjectSensitiveAllocNode contextNode = (ObjectSensitiveAllocNode)context;

                for (int i = 0; i < contextNode.numContextElements(); i++) {
                    ContextElement ce = contextNode.getContextElement(i);
                    if (ce instanceof InsensitiveAllocNode) {
                        SootMethod allocatorMethod = ((InsensitiveAllocNode)ce).getMethod();
                        if (allocatorMethod != null) {
                            SootClass allocatingClass = allocatorMethod.getDeclaringClass();
                            if (allocatingClass != null && importantAllocators.contains(allocatingClass)) {
                                // in an important class
                                return k;
                            }
                        }
                    }   
                }
            }
            return minK;
        }
    }

    private SootClass getSootClass(Type type) {
        SootClass allocated = null;

        if (type instanceof RefType) {
            allocated = ((RefType)type).getSootClass();
        } else if (type instanceof ArrayType && ((ArrayType)type).getArrayElementType() instanceof RefType) {
            allocated = ((RefType)((ArrayType)type).getArrayElementType()).getSootClass();
        }

        return allocated;
    }

    // LWG
    public boolean thisPtrShouldAdd(AllocNode allocNode, Context thisRefContext) {

        if (allocNode instanceof ObjectSensitiveAllocNode) {
            ObjectSensitiveAllocNode osan = (ObjectSensitiveAllocNode)allocNode;        
            //if the this pointer has no context, then only add obj sens alloc nodes with no context beyond new expr
            if (thisRefContext instanceof NoContext) {
                return osan.noContext();
            } else if (thisRefContext instanceof ObjectSensitiveAllocNode) {
                return thisRefContext.equals((Context)allocNode);
            } else {
                throw new RuntimeException("This filter has strange type of context on this ref: " + thisRefContext.getClass()); 
            }
        } else {
            //not an obj sens node, so if there is context in the this ref, then alloc node should not be added
            return thisRefContext instanceof NoContext;
        }


    }
}
