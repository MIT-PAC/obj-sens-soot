package soot.jimple.toolkits.callgraph;

import soot.Context;
import soot.MethodOrMethodContext;
import soot.SootMethod;
import soot.jimple.Stmt;

public interface ReflectionModel {

	void methodInvoke(MethodOrMethodContext container, Stmt invokeStmt);

	void classNewInstance(MethodOrMethodContext source, Stmt s);

	void contructorNewInstance(MethodOrMethodContext source, Stmt s);

	void classForName(MethodOrMethodContext source, Stmt s);

}
