package soot.jimple.toolkits.callgraph;

import soot.Context;
import soot.SootMethod;
import soot.jimple.Stmt;

public interface ReflectionModel {

	void methodInvoke(SootMethod container, Stmt invokeStmt, Context c);

	void classNewInstance(SootMethod source, Stmt s, Context c);

	void contructorNewInstance(SootMethod source, Stmt s, Context c);

	void classForName(SootMethod source, Stmt s, Context c);

}
