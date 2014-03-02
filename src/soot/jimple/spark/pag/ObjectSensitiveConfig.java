package soot.jimple.spark.pag;

import java.util.HashSet;
import java.util.Set;

import soot.Hierarchy;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;

public class ObjectSensitiveConfig {
    
    private boolean contextForAllStaticMethods = false;
    
    /** list of classes for which we do not add context */
    private Set<SootClass> ignoreList;
    
    private Set<SootClass> stringClasses;
    
    private static ObjectSensitiveConfig v;
    
    /** depth of the object sensitivity on heap and method */
    private int k = 0;
    
    private ObjectSensitiveConfig(String csl) {
        stringClasses = new HashSet<SootClass>();
        stringClasses.add(Scene.v().getSootClass("java.lang.String"));
        stringClasses.add(Scene.v().getSootClass("java.lang.StringBuilder"));
        stringClasses.add(Scene.v().getSootClass("java.lang.StringBuffer"));
        
        installNoContextList(csl);
        
    }
    
    public static boolean isObjectSensitive() {
        return (v != null && v.k > 0); 
    }
    
    public static void noObjectSens() {
        v = null;
    }
    
    /**
     * Install no context list for classes given plus all subclasses.
     */
    private void installNoContextList(String csl) {
        ignoreList = new HashSet<SootClass>();
        
        if (csl == null)
            return;
        
        Hierarchy h = Scene.v().getActiveHierarchy();
        
        String[] classes = csl.split(",");
        
        for (String str : classes) {
            str = str.trim();
            //System.out.println("Adding class plus subclasses to ignore list: " + str);
            SootClass clz = Scene.v().getSootClass(str);
            ignoreList.addAll(h.getSubclassesOfIncluding(clz));
        }
    }
    
    public int k() {
        return k;
    }
    
    public static void initialize(int k, String noContextList, boolean contextForAllStaticMethods) {
        v = null;
        v = new ObjectSensitiveConfig(noContextList);
        
        v.k = k;
        
        v.contextForAllStaticMethods = contextForAllStaticMethods; 
  
    }
    
    public boolean contextForAllStaticMethods() {
        return contextForAllStaticMethods;
    }
    
    public static ObjectSensitiveConfig v() {
        return v;
    }
    
    public boolean noContext(Type type, SootMethod method) {
        if (type instanceof RefType) {
            SootClass clz = ((RefType)type).getSootClass();
            return ignoreList.contains(clz);
        }
        //context for everything else?
        return false;
    }
    
    public boolean noContext(AllocNode node) {
        return noContext(node.getType(), node.getMethod());
    }

}
