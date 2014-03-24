package soot.jimple.spark.pag;

import java.util.HashMap;
import java.util.Map;

import soot.Context;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;

public class StaticInitContext implements Context, ContextElement {
    private static Map<SootClass, StaticInitContext> universe = new HashMap<SootClass, StaticInitContext>(); 
    
    private SootClass clz;
    
    private StaticInitContext(SootClass c) {
        clz = c;
    }

    public static void reset() {
        universe = new HashMap<SootClass, StaticInitContext>(); 
    }
    
    public static StaticInitContext v(SootClass clz) {
        if (!universe.containsKey(clz))
            universe.put(clz, new StaticInitContext(clz));
        
        StaticInitContext node = universe.get(clz);
        
        return node;
    }

    public String toString() {
        return "StaticInitNode " + hashCode() + " class: " + clz;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((clz == null) ? 0 : clz.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        StaticInitContext other = (StaticInitContext) obj;
        if (clz == null) {
            if (other.clz != null) return false;
        } else if (!clz.equals(other.clz)) return false;
        return true;
    }

    
}
