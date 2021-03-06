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
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.jimple.spark.sets.PointsToSetInternal;

public class ObjectSensitiveConfig {

    private boolean contextForStaticInits = false;

    /** list of classes for which we do not add context */
    private Set<SootClass> ignoreList;
    
    /** list of app classes */
    private Set<SootClass> appClasses;
  
    private Set<Integer> limitHeapContext;

    private static ObjectSensitiveConfig v;

    private boolean typesForContextGTOne;

    private Set<SootClass> stringClasses;

    private static Set<Object> newExprsForNoContext;

    /** depth of the object sensitivity on heap and method */
    private int k = 0;
    /** depth to traverse into API for call graph building, -1 is follow all edges */
    private int apiCallDepth = -1;

    private HashSet<SootClass> hasArrayField;
    
    private boolean extraArrayContext = false;

    private ObjectSensitiveConfig(int k, int apiCallDepth, String appClassesStr, String noContextStr, String limitHeapContextStr,
                                  boolean contextForStaticInits, boolean typesForContextGTOne,
                                  boolean extraArrayContext) {
        this.k = k;
        this.apiCallDepth = apiCallDepth; 
        System.out.println("Api Call Depth: " + this.apiCallDepth);
      
        this.contextForStaticInits = contextForStaticInits;
        this.typesForContextGTOne = typesForContextGTOne;
        this.extraArrayContext = extraArrayContext;

        this.ignoreList = new HashSet<SootClass>();
        this.limitHeapContext = new HashSet<Integer>();
        this.appClasses = new HashSet<SootClass>();
        
        installClassListWithAncestors(this.ignoreList, noContextStr);                  
        installClassList(this.appClasses, appClassesStr);
        installLimitHeapContext(limitHeapContextStr);
        
        buildHasArraySet();

        stringClasses = new HashSet<SootClass>();
        stringClasses.add(Scene.v().getSootClass("java.lang.String"));
        stringClasses.add(Scene.v().getSootClass("java.lang.StringBuilder"));
        stringClasses.add(Scene.v().getSootClass("java.lang.StringBuffer"));
    }
    
    public boolean hasArrayField(Type type) {
        if (type instanceof RefType) {
            return hasArrayField.contains(((RefType)type).getSootClass());
        }
        
        return false;
    }
    
    public int apiCallDepth() {
        return apiCallDepth;
    }
    
    public boolean isAppClass(SootClass clz) {
        return appClasses.contains(clz);
    }
    

    private void buildHasArraySet() {
        hasArrayField = new HashSet<SootClass>();
        
        for (SootClass clz : Scene.v().getClasses()) {
            if (clz.isInterface())
                continue;
            boolean foundArray = false;
            
            for (SootClass ancestor : Scene.v().getActiveHierarchy().getSuperclassesOfIncluding(clz)) {
                for (SootField field : ancestor.getFields()) {
                    if (field.getType() instanceof ArrayType) {
                        hasArrayField.add(clz);
                        foundArray = true;
                        break;
                    }
                }
                if (foundArray)
                    break;
            }
        }
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


    private void installLimitHeapContext(String lhc) {
    	if (lhc == null || lhc.isEmpty())
    		return;

    	String[] hashcodes = lhc.split(",");

    	for (String str : hashcodes) {
    		str = str.trim();
    		try {
    			int hashCode = Integer.parseInt(str);
    			limitHeapContext.add(hashCode);
    		} catch (NumberFormatException e) {
    			System.out.println("Invalid hashCode integer in limit heap context string: " + str);
    		}    		    		
    	}
    }
    
    private void installClassList(Set<SootClass> set, String lhc) {

        if (lhc == null || lhc.isEmpty())
            return;

        String[] classes = lhc.split(",");

        for (String str : classes) {
            str = str.trim();
            SootClass clz = tryToGetClass(str);
            if (clz != null && !clz.isInterface()) {
                set.add(clz);
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
    private void installClassListWithAncestors(Set<SootClass> set, String csl) {

        if (csl == null || csl.isEmpty())
            return;

        Hierarchy h = Scene.v().getActiveHierarchy();

        String[] classes = csl.split(",");

        for (String str : classes) {
            str = str.trim();
            //System.out.println("Adding class plus subclasses to ignore list: " + str);
            SootClass clz = tryToGetClass(str);
            if (clz != null)
                set.add(clz);
        }
    }

    public boolean extraArrayContext() {
        return extraArrayContext;
    }    

    public int k() {
        return k;
    }

    public static void initialize(int k, int apiCallDepth, String appClasses, String noContextList, 
                                  String limitHeapContext, boolean contextForStaticInits,
                                  boolean typesForContextGTOne, boolean extraArrayContext) {
        v = null;
        v = new ObjectSensitiveConfig(k, apiCallDepth, appClasses, noContextList, limitHeapContext, 
            contextForStaticInits, typesForContextGTOne, extraArrayContext);
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
     * Limit heap context to 1 for these AllocNodes.
     * 
     * @param clz
     * @return
     */
    public boolean limitHeapContext(AllocNode base) {
        return limitHeapContext.contains(base.newExpr.hashCode());
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

    /*
    public int contextDepth(AllocNode probe, Context context) {
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
                        SootClass currentAllocated = getSootClass(((InsensitiveAllocNode)ce).getType());
                        if (importantAllocators.contains(currentAllocated))
                            return k;
                        
                        
                       
                        }
                        
                    }   
                }
            } 
            return minK;
        }
    }
    */

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
