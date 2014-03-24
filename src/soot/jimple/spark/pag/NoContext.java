package soot.jimple.spark.pag;

import soot.Context;

public class NoContext implements Context,ContextElement {
    private static NoContext singleton = new NoContext();
    
    public static NoContext v() {
        return singleton;
    }
    
    public String toString() {
        return "<NO-CONTEXT>";
    }

}
