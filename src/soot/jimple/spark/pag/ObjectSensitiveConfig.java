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
            if (clz != null && !clz.isInterface())
                limitHeapContext.add(clz);
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
     * Limit heap context to 1 for these classes.
     * 
     * @param clz
     * @return
     */
    public boolean limitHeapContext(ObjectSensitiveAllocNode node, AllocNode base) {
        return !addHeapContext(node);
        /*
        //limit class constant context
        if (base instanceof ClassConstantNode)
            return true;
        
        if (base.getType() instanceof RefType) {
            SootClass clz = ((RefType)base.getType()).getSootClass();
            if (limitHeapContext.contains(clz))
                return true;
        }
                
        return false;
        */
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

    public boolean addMethodContext(MethodOrMethodContext src, ObjectSensitiveAllocNode probe) {
        return true;
        /*
        //check if the calling method is important, if so, make sure we add context
        if (src.method().getDeclaringClass() != null) {
            SootClass srcClass = src.method().getDeclaringClass();

            if (importantAllocators.contains(srcClass))
                return true;
        }

        //otherwise use the heap decision
        return addHeapContext(probe);
        */
    }

    private boolean addHeapContext(ObjectSensitiveAllocNode probe) {
        /*
        if (isNewExprNoContext(probe.getNewExpr()))
            return false;

        //first check if the type that is allocated should never has context because
        //it is on the ignore List
        Type allocatedType = probe.getType();

        SootClass allocated = null;

        if (allocatedType instanceof RefType) {
            allocated = ((RefType)allocatedType).getSootClass();
        } else if (allocatedType instanceof ArrayType && ((ArrayType)allocatedType).getArrayElementType() instanceof RefType) {
            allocated = ((RefType)((ArrayType)allocatedType).getArrayElementType()).getSootClass();
        }

        if (allocated != null) {
            //first check if on the no context list, that trumps all
            if (ignoreList.contains(allocated)) 
                return false;
            
            //if a string then we always want to add context, too imprecise without it
           
            //if (stringClasses.contains(allocated))
            //    return true;
             
        }
         */

        //now check if we have defined important allocators, we check to see if the starting context is an important
        //allocator
        if (importantAllocators.isEmpty())
            return true;
        
        //naive decision based on if important or not...
        if (naiveDecay) {
            Type allocatedType = probe.getType();

            SootClass allocated = null;

            if (allocatedType instanceof RefType) {
                allocated = ((RefType)allocatedType).getSootClass();
            } else if (allocatedType instanceof ArrayType && ((ArrayType)allocatedType).getArrayElementType() instanceof RefType) {
                allocated = ((RefType)((ArrayType)allocatedType).getArrayElementType()).getSootClass();
            }

            if (allocated != null) {
                if (importantAllocators.contains(allocated))
                    return true;
                else
                    return false;
            }
        }

        //smarter decision hopefully...
        for (int i = 0; i < probe.numContextElements(); i++) {
            ContextElement ce = probe.getContextElement(i);
            if (ce instanceof InsensitiveAllocNode) {
                SootMethod allocatorMethod = ((InsensitiveAllocNode)ce).getMethod();
                if (allocatorMethod != null) {
                    SootClass allocatingClass = allocatorMethod.getDeclaringClass();
                    if (allocatingClass != null && importantAllocators.contains(allocatingClass)) {
                        // in an important class
                        return true;
                    }
                }
            }   
        }

    
        return false;
        
            /*
        
        ContextElement startingC = probe.getStartingContextElement();

        if (startingC instanceof InsensitiveAllocNode) {
            SootMethod allocatorMethod = ((InsensitiveAllocNode)startingC).getMethod();
            if (allocatorMethod != null) {
                SootClass allocatingClass = allocatorMethod.getDeclaringClass();
                if (allocatingClass != null && importantAllocators.contains(allocatingClass)) {
                    // in an important class
                    return true;
                }
            }

            //not an important allocator
            return false;
        } else if (startingC instanceof StaticInitContext || startingC instanceof EntryContext) {
            return true;
        } else {
            return false;
        }
        */

    }
    
    public boolean thisPtrShouldAdd(AllocNode allocNode, Node assignTo) {
        
        if (!(assignTo instanceof ContextVarNode)) 
            return true;

        ContextVarNode cvn = (ContextVarNode)assignTo;

        //not this pointer
        if (!cvn.isThisPtr())
            return true;
        
        Context thisRefContext = cvn.context();
        
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
