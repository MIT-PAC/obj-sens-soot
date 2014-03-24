package soot.jimple.spark.pag;

import java.util.HashMap;
import java.util.Map;

import soot.RefLikeType;
import soot.Type;

/** 
 * Type based context element in the points to analysis.
 * 
 * @author mgordon
 *
 */
public class TypeContextElement implements ContextElement {

    private Type type;
    
    private static Map<Type, TypeContextElement> universe = 
            new HashMap<Type, TypeContextElement>();
    
    public static TypeContextElement v(Type type) {
        if (!universe.containsKey(type))
            universe.put(type, new TypeContextElement(type));
        
        return universe.get(type);
    }
    
    private TypeContextElement(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        TypeContextElement other = (TypeContextElement) obj;
        if (type == null) {
            if (other.type != null) return false;
        } else if (!type.equals(other.type)) return false;
        return true;
    }

   public String toString() {
       return "TypeContext: " + type;
   }
}
