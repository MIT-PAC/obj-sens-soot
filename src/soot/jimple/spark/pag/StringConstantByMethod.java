package soot.jimple.spark.pag;

import soot.SootMethod;
import soot.jimple.StringConstant;

/**
 * Wrapper class around Jimple's StringConstant that adds the method where
 * the string constant is encountered. This allows the pointer analysis to have 
 * separate stringconstantnodes (allocnodes) for the same string constant, and 
 * parametrize the local variables referencing the string constant nodes to increase
 * precision.
 * 
 * @author mgordon
 *
 */
public class StringConstantByMethod {
    private StringConstant stringConstant;
    private SootMethod method;
    
    public StringConstantByMethod(StringConstant sc, SootMethod m) {
        this.stringConstant = sc;
        this.method = m;           
    }

    public StringConstant getStringConstant() {
        return stringConstant;
    }

    public void setStringConstant(StringConstant stringConstant) {
        this.stringConstant = stringConstant;
    }

    public SootMethod getMethod() {
        return method;
    }

    public void setMethod(SootMethod method) {
        this.method = method;
    }

    public String toString() {
        return "SC: " + stringConstant.value + " (in method " + method + ")";
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((method == null) ? 0 : method.hashCode());
        result = prime * result + ((stringConstant == null) ? 0 : stringConstant.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        StringConstantByMethod other = (StringConstantByMethod) obj;
        if (method == null) {
            if (other.method != null) return false;
        } else if (!method.equals(other.method)) return false;
        if (stringConstant == null) {
            if (other.stringConstant != null) return false;
        } else if (!stringConstant.equals(other.stringConstant)) return false;
        return true;
    }

    
}
