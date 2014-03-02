package soot.jimple.spark.pag;

import java.util.HashSet;
import java.util.Set;

import soot.ArrayType;
import soot.Hierarchy;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;

public class ObjectSensitiveConfig {
    
    private boolean contextForAllStaticMethods = false;
    
    private boolean preciseStrings = false;
    
    /** list of classes for which we do not add context */
    private Set<SootClass> ignoreList;
    
    private Set<SootClass> stringClasses;
    
    private Set<SootClass> importantAllocators;
    
    private static ObjectSensitiveConfig v;
    
    /** depth of the object sensitivity on heap and method */
    private int k = 0;
    
    private ObjectSensitiveConfig(int k, String csl, String importantAs, boolean preciseStrings,
                                  boolean contextForStaticMethods) {
        this.k = k;
        
        this.preciseStrings = preciseStrings;
        
        this.contextForAllStaticMethods = contextForStaticMethods;
        
        stringClasses = new HashSet<SootClass>();
        stringClasses.add(Scene.v().getSootClass("java.lang.String"));
        stringClasses.add(Scene.v().getSootClass("java.lang.StringBuilder"));
        stringClasses.add(Scene.v().getSootClass("java.lang.StringBuffer"));
        
        installNoContextList(csl);
        installImportantAllocators(importantAs);
        
    }
    
    public static boolean isObjectSensitive() {
        return (v != null && v.k > 0); 
    }
    
    public static void noObjectSens() {
        v = null;
    }
    
    private void installImportantAllocators(String isa) {
        importantAllocators = new HashSet<SootClass>();
        
        if (isa == null || isa.isEmpty())
            return;
     
        String[] classes = isa.split(",");
        
        for (String str : classes) {
            str = str.trim();
            SootClass clz = tryToGetClass(str);
            if (clz != null)
                importantAllocators.add(clz);
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
                ignoreList.addAll(h.getSubclassesOfIncluding(clz));
        }
    }
    
    public int k() {
        return k;
    }
    
    public static void initialize(int k, String noContextList, String importantAllocators,  
                                 boolean preciseStrings, boolean contextForAllStaticMethods) {
        v = null;
        v = new ObjectSensitiveConfig(k, noContextList, importantAllocators, preciseStrings, contextForAllStaticMethods);
    }
    
    public boolean contextForAllStaticMethods() {
        return contextForAllStaticMethods;
    }
    
    public static ObjectSensitiveConfig v() {
        return v;
    }
    
    public boolean limitContextDepth(Type type, SootMethod allocatorMethod) {
        
        SootClass allocated = null;
        
        if (type instanceof RefType) {
            allocated = ((RefType)type).getSootClass();
        } else if (type instanceof ArrayType && ((ArrayType)type).getArrayElementType() instanceof RefType) {
            allocated = ((RefType)((ArrayType)type).getArrayElementType()).getSootClass();
        }
        
        //not something we understand, so return false to denote we should have context for it
        if (allocated == null)
            return false;
        
                
        //first check if on the no context list, that trumps all
        if (ignoreList.contains(allocated)) 
            return true;
        
        //now move on to string checking
        
        //if not a string, then keep context
        if (!stringClasses.contains(allocated))
            return false;
        
        //now we know that alloc is a string
        //if precise strings, then context for everything!
        if (preciseStrings)
            return false;
        
        //no precise strings, check important allocation list
        
        //if we have no allocator method, then should have no context for strings
        if (allocatorMethod == null)
            return true;
        
        SootClass allocatingClass = allocatorMethod.getDeclaringClass();
        if (allocatingClass != null && importantAllocators.contains(allocatingClass)) {
            //string in an important class
            return false;
        } else {
            //string but not in an important class
            return true;
        }
        
    }
    
    public boolean limitContextDepth(AllocNode node) {
        return limitContextDepth(node.getType(), node.getMethod());
    }

}
