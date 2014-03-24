package soot.jimple.spark.pag;

import soot.Context;

/**
 * Context that denotes an entry point in the application (like main())
 * 
 * This context is added for all entry points in the reachable methods before
 * a pta run.
 * 
 * @author mgordon
 *
 */
public class EntryContext implements Context, ContextElement {

    private EntryContext() {
     
    }
    
    private static EntryContext singleton = new EntryContext();
    
    public static EntryContext v() {
        return singleton;
    }
    
    public String toString() {
        return "<ENTRY-CONTEXT>";
    }


}
