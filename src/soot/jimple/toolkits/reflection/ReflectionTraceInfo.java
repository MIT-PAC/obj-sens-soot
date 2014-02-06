/* Soot - a J*va Optimization Framework
 * Copyright (C) 2010 Eric Bodden
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */

package soot.jimple.toolkits.reflection;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import soot.Body;
import soot.G;
import soot.Scene;
import soot.SootClass;
import soot.MethodOrMethodContext;
import soot.Unit;
import soot.tagkit.Host;
import soot.tagkit.LineNumberTag;
import soot.tagkit.SourceLnPosTag;

public class ReflectionTraceInfo {
	
	public enum Kind { ClassForName, ClassNewInstance, ConstructorNewInstance, MethodInvoke, FieldSet, FieldGet }
	
	protected Map<MethodOrMethodContext,Set<String>> classForNameReceivers;
	
	protected Map<MethodOrMethodContext,Set<String>> classNewInstanceReceivers;

	protected Map<MethodOrMethodContext,Set<String>> constructorNewInstanceReceivers;

	protected Map<MethodOrMethodContext,Set<String>> methodInvokeReceivers;

	protected Map<MethodOrMethodContext,Set<String>> fieldSetReceivers;

	protected Map<MethodOrMethodContext,Set<String>> fieldGetReceivers;

	public ReflectionTraceInfo(String logFile) {
		classForNameReceivers = new LinkedHashMap<MethodOrMethodContext, Set<String>>();
		classNewInstanceReceivers = new LinkedHashMap<MethodOrMethodContext, Set<String>>();
		constructorNewInstanceReceivers = new LinkedHashMap<MethodOrMethodContext, Set<String>>();
		methodInvokeReceivers = new LinkedHashMap<MethodOrMethodContext, Set<String>>();
		fieldSetReceivers = new LinkedHashMap<MethodOrMethodContext, Set<String>>();
		fieldGetReceivers = new LinkedHashMap<MethodOrMethodContext, Set<String>>();

		if(logFile==null) {
			throw new InternalError("Trace based refection model enabled but no trace file given!?");
		} else {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(logFile)));
				String line;
				int lines = 0;
				Set<String> ignoredKinds = new HashSet<String>();
				while((line=reader.readLine())!=null) {
					if(line.length()==0) continue;
					String[] portions = line.split(";",-1);
					String kind = portions[0];
					String target = portions[1];
					String source = portions[2];
					int lineNumber = portions[3].length()==0 ? -1 : Integer.parseInt(portions[3]);

					Set<MethodOrMethodContext> possibleSourceMethods = inferSource(source, lineNumber);
					for (MethodOrMethodContext sourceMethod : possibleSourceMethods) {
						if(kind.equals("Class.forName")) {
							Set<String> receiverNames;
							if((receiverNames=classForNameReceivers.get(sourceMethod))==null) {
								classForNameReceivers.put(sourceMethod, receiverNames = new LinkedHashSet<String>());
							}
							receiverNames.add(target);
						} else if(kind.equals("Class.newInstance")) {
							Set<String> receiverNames;
							if((receiverNames=classNewInstanceReceivers.get(sourceMethod))==null) {
								classNewInstanceReceivers.put(sourceMethod, receiverNames = new LinkedHashSet<String>());
							}
							receiverNames.add(target);
						} else if(kind.equals("Method.invoke")) {
							if(!Scene.v().containsMethod(target)) {
								throw new RuntimeException("Unknown method for signature: "+target);
							}
							
							Set<String> receiverNames;
							if((receiverNames=methodInvokeReceivers.get(sourceMethod))==null) {
								methodInvokeReceivers.put(sourceMethod, receiverNames = new LinkedHashSet<String>());
							}
							receiverNames.add(target);								
						} else if (kind.equals("Constructor.newInstance")) {
							if(!Scene.v().containsMethod(target)) {
								throw new RuntimeException("Unknown method for signature: "+target);
							}
							
							Set<String> receiverNames;
							if((receiverNames=constructorNewInstanceReceivers.get(sourceMethod))==null) {
								constructorNewInstanceReceivers.put(sourceMethod, receiverNames = new LinkedHashSet<String>());
							}
							receiverNames.add(target);								
						} else if (kind.equals("Field.set*")) {
							if(!Scene.v().containsField(target)) {
								throw new RuntimeException("Unknown method for signature: "+target);
							}
							
							Set<String> receiverNames;
							if((receiverNames=fieldSetReceivers.get(sourceMethod))==null) {
								fieldSetReceivers.put(sourceMethod, receiverNames = new LinkedHashSet<String>());
							}
							receiverNames.add(target);								
						} else if (kind.equals("Field.get*")) {
							if(!Scene.v().containsField(target)) {
								throw new RuntimeException("Unknown method for signature: "+target);
							}
							
							Set<String> receiverNames;
							if((receiverNames=fieldGetReceivers.get(sourceMethod))==null) {
								fieldGetReceivers.put(sourceMethod, receiverNames = new LinkedHashSet<String>());
							}
							receiverNames.add(target);								
						} else {
							ignoredKinds.add(kind);
						}							
					}
					lines++;
				}
				if(!ignoredKinds.isEmpty()) {
					G.v().out.println("Encountered reflective calls entries of the following kinds that\n" +
							"cannot currently be handled:");
					for (String kind : ignoredKinds) {
						G.v().out.println(kind);
					}
				}
			} catch (FileNotFoundException e) {
				throw new RuntimeException("Trace file not found.",e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	private Set<MethodOrMethodContext> inferSource(String source, int lineNumber) {
		String className = source.substring(0,source.lastIndexOf("."));
		String methodName = source.substring(source.lastIndexOf(".")+1);
		if(!Scene.v().containsClass(className)) {
			Scene.v().addBasicClass(className, SootClass.BODIES);
			Scene.v().loadBasicClasses();
			if(!Scene.v().containsClass(className)) {
				throw new RuntimeException("Trace file refers to unknown class: "+className);
			}
		}

		SootClass sootClass = Scene.v().getSootClass(className);
		Set<MethodOrMethodContext> methodsWithRightName = new LinkedHashSet<MethodOrMethodContext>();
		for (MethodOrMethodContext m: sootClass.getMethods()) {
			if(m.method().isConcrete() && m.method().getName().equals(methodName)) {
				methodsWithRightName.add(m);
			}
		} 

		if(methodsWithRightName.isEmpty()) {
			throw new RuntimeException("Trace file refers to unknown method with name "+methodName+" in Class "+className);
		} else if(methodsWithRightName.size()==1) {
			return Collections.singleton(methodsWithRightName.iterator().next());
		} else {
			//more than one method with that name
			for (MethodOrMethodContext momc : methodsWithRightName) {
				if(coversLineNumber(lineNumber, momc.method())) {
					return Collections.singleton(momc);
				}
				if(momc.method().isConcrete()) {
					if(!momc.method().hasActiveBody()) momc.method().retrieveActiveBody();
					Body body = momc.method().getActiveBody();
					if(coversLineNumber(lineNumber, body)) {
						return Collections.singleton(momc);
					}
					for (Unit u : body.getUnits()) {
						if(coversLineNumber(lineNumber, u)) {
							return Collections.singleton(momc);
						}
					}
				}
			}
			
			//if we get here then we found no method with the right line number information;
			//be conservative and return all method that we found
			return methodsWithRightName;				
		}
	}

	private boolean coversLineNumber(int lineNumber, Host host) {
		{
			SourceLnPosTag tag = (SourceLnPosTag) host.getTag("SourceLnPosTag");
			if(tag!=null) {
				if(tag.startLn()<=lineNumber && tag.endLn()>=lineNumber) {
					return true;
				}
			}
		}
		{
			LineNumberTag tag = (LineNumberTag) host.getTag("LineNumberTag");
			if(tag!=null) {
				if(tag.getLineNumber()==lineNumber) {
					return true;
				}
			}
		}
		return false;
	}
	
	public Set<String> classForNameClassNames(MethodOrMethodContext container) {
		if(!classForNameReceivers.containsKey(container)) return Collections.emptySet();
		return classForNameReceivers.get(container);
	}

	public Set<SootClass> classForNameClasses(MethodOrMethodContext container) {
		Set<SootClass> result = new LinkedHashSet<SootClass>();
		for(String className: classForNameClassNames(container)) {
			result.add(Scene.v().getSootClass(className));
		}
		return result;
	}
	
	public Set<String> classNewInstanceClassNames(MethodOrMethodContext container) {
		if(!classNewInstanceReceivers.containsKey(container)) return Collections.emptySet();
		return classNewInstanceReceivers.get(container);
	}
	
	public Set<SootClass> classNewInstanceClasses(MethodOrMethodContext container) {
		Set<SootClass> result = new LinkedHashSet<SootClass>();
		for(String className: classNewInstanceClassNames(container)) {
			result.add(Scene.v().getSootClass(className));
		}
		return result;
	}
	
	public Set<String> constructorNewInstanceSignatures(MethodOrMethodContext container) {
		if(!constructorNewInstanceReceivers.containsKey(container)) return Collections.emptySet();
		return constructorNewInstanceReceivers.get(container);
	}
	
	public Set<MethodOrMethodContext> constructorNewInstanceConstructors(MethodOrMethodContext container) {
		Set<MethodOrMethodContext> result = new LinkedHashSet<MethodOrMethodContext>();
		for(String signature: constructorNewInstanceSignatures(container)) {
			result.add(Scene.v().getMethod(signature));
		}
		return result;
	}
	
	public Set<String> methodInvokeSignatures(MethodOrMethodContext container) {
		if(!methodInvokeReceivers.containsKey(container)) return Collections.emptySet();
		return methodInvokeReceivers.get(container);
	}

	public Set<MethodOrMethodContext> methodInvokeMethods(MethodOrMethodContext container) {
		Set<MethodOrMethodContext> result = new LinkedHashSet<MethodOrMethodContext>();
		for(String signature: methodInvokeSignatures(container)) {
			result.add(Scene.v().getMethod(signature));
		}
		return result;
	}
	
	public Set<MethodOrMethodContext> methodsContainingReflectiveCalls() {
		Set<MethodOrMethodContext> res = new LinkedHashSet<MethodOrMethodContext>();
		res.addAll(classForNameReceivers.keySet());
		res.addAll(classNewInstanceReceivers.keySet());
		res.addAll(constructorNewInstanceReceivers.keySet());
		res.addAll(methodInvokeReceivers.keySet());
		return res;
	}

	public Set<String> fieldSetSignatures(MethodOrMethodContext container) {
		if(!fieldSetReceivers.containsKey(container)) return Collections.emptySet();
		return fieldSetReceivers.get(container);
	}

	public Set<String> fieldGetSignatures(MethodOrMethodContext container) {
		if(!fieldGetReceivers.containsKey(container)) return Collections.emptySet();
		return fieldGetReceivers.get(container);
	}
}