package soot.jimple.toolkits.pta;

import soot.Type;
import soot.util.Numberable;

// (LWG) new interface
public interface IAllocNode extends Numberable {

    public Type getType();

    public Object getNewExpr();
    
}
