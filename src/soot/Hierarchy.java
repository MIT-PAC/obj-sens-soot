/* Soot - a J*va Optimization Framework
 * Copyright (C) 1997-1999 Raja Vallee-Rai
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

/*
 * Modified by the Sable Research Group and others 1997-1999.  
 * See the 'credits' file distributed with Soot for the complete list of
 * contributors.  (Soot is distributed at http://www.sable.mcgill.ca/soot)
 */

package soot;

import soot.jimple.*;
import soot.util.*;

import java.util.*;

/** Represents the class hierarchy.  It is closely linked to a Scene,
 * and must be recreated if the Scene changes. 
 *
 * The general convention is that if a method name contains 
 * "Including", then it returns the non-strict result; otherwise,
 * it does a strict query (e.g. strict superclass).  */
public class Hierarchy
{
    // These two maps are not filled in the constructor.
    Map<SootClass, List<SootClass>> classToSubclasses;
    Map<SootClass, List<SootClass>> interfaceToSubinterfaces;
    Map<SootClass, List<SootClass>> interfaceToSuperinterfaces;

    Map<SootClass, List<SootClass>> classToDirSubclasses;
    Map<SootClass, List<SootClass>> interfaceToDirSubinterfaces;
    Map<SootClass, List<SootClass>> interfaceToDirSuperinterfaces;

    // This holds the direct implementers.
    Map<SootClass, List<SootClass>> interfaceToDirImplementers;

    int state;
    Scene sc;

    /** Constructs a hierarchy from the current scene. */
    public Hierarchy()
    {
        this.sc = Scene.v();
        state = sc.getState();

        // Well, this used to be describable by 'Duh'.
        // Construct the subclasses hierarchy and the subinterfaces hierarchy.
        {
            Chain<SootClass> allClasses = sc.getClasses();

            classToSubclasses = new HashMap<SootClass, List<SootClass>>(allClasses.size() * 2 + 1, 0.7f);
            interfaceToSubinterfaces = new HashMap<SootClass, List<SootClass>>(allClasses.size() * 2 + 1, 0.7f);
            interfaceToSuperinterfaces = new HashMap<SootClass, List<SootClass>>(allClasses.size() * 2 + 1, 0.7f);
            
            classToDirSubclasses = new HashMap<SootClass, List<SootClass>>
                (allClasses.size() * 2 + 1, 0.7f);
            interfaceToDirSubinterfaces = new HashMap<SootClass, List<SootClass>>
                (allClasses.size() * 2 + 1, 0.7f);
            interfaceToDirSuperinterfaces = new HashMap<SootClass, List<SootClass>>
            	(allClasses.size() * 2 + 1, 0.7f);
            interfaceToDirImplementers = new HashMap<SootClass, List<SootClass>>
                (allClasses.size() * 2 + 1, 0.7f);

            Iterator<SootClass> classesIt = allClasses.iterator();
            while (classesIt.hasNext())
            {
                SootClass c = classesIt.next();
                if( c.resolvingLevel() < SootClass.HIERARCHY ) continue;

                if (c.isInterface())
                {
                    interfaceToDirSubinterfaces.put(c, new ArrayList<SootClass>());
                    interfaceToDirSuperinterfaces.put(c, new ArrayList<SootClass>());
                    interfaceToDirImplementers.put(c, new ArrayList<SootClass>());
                }
                else {
                    classToDirSubclasses.put(c, new ArrayList<SootClass>());   
                }
            }

            classesIt = allClasses.iterator();
            while (classesIt.hasNext())
            {
                SootClass c = classesIt.next();
                if( c.resolvingLevel() < SootClass.HIERARCHY ) continue;

                List<SootClass> l2 = interfaceToDirSuperinterfaces.get(c);

                if (c.hasSuperclass())
                {
                    if (c.isInterface())
                    {
                        for (SootClass i : c.getInterfaces()) {
                            if( c.resolvingLevel() < SootClass.HIERARCHY ) continue;
                            List<SootClass> l = interfaceToDirSubinterfaces.get(i);
                            if (l != null) l.add(c);
                            if (l2 != null) l2.add(i);
                        }
                    }
                    else
                    {
                        List<SootClass> l = classToDirSubclasses.get(c.getSuperclass());
                        if (l == null) {
                            G.v().out.println("WARN: Error in soot hierarchy. Interface found where class excepted: " + c); 
                            c.setPhantomClass();
                        } else {
                            l.add(c);
                        }
                    
                        Iterator<SootClass> subIt = c.getInterfaces().iterator();

                        while (subIt.hasNext())
                        {
                            SootClass i = subIt.next();
                            if( c.resolvingLevel() < SootClass.HIERARCHY ) continue;
                            l = interfaceToDirImplementers.get(i);
                            if (l != null) l.add(c);
                        }
                    }
                }
            }

            // Fill the directImplementers lists with subclasses.
            {
                classesIt = allClasses.iterator();
                while (classesIt.hasNext())
                {
                    SootClass c = (SootClass)classesIt.next();
                    if( c.resolvingLevel() < SootClass.HIERARCHY ) continue;
                    if (c.isInterface())
                    {
                        List<SootClass> imp = interfaceToDirImplementers.get(c);
                        Set<SootClass> s = new ArraySet<SootClass>();
                        
                        Iterator<SootClass> impIt = imp.iterator();
                        while (impIt.hasNext())
                        {
                            SootClass c0 = impIt.next();
                            if( c.resolvingLevel() < SootClass.HIERARCHY ) continue;
                            s.addAll(getSubclassesOfIncluding(c0));
                        }

                        imp.clear(); imp.addAll(s);
                    }
                }
            }

            classesIt = allClasses.iterator();
            while (classesIt.hasNext())
            {
                SootClass c = (SootClass)classesIt.next();
                if( c.resolvingLevel() < SootClass.HIERARCHY ) continue;

                if (c.isInterface())
                {
                    interfaceToDirSubinterfaces.put(c, Collections.unmodifiableList
                    		(interfaceToDirSubinterfaces.get(c)));
                    interfaceToDirSuperinterfaces.put(c, Collections.unmodifiableList
                    		(interfaceToDirSuperinterfaces.get(c)));
                    interfaceToDirImplementers.put(c, Collections.unmodifiableList
                    		(interfaceToDirImplementers.get(c)));
                }
                else {
                    classToDirSubclasses.put(c, Collections.unmodifiableList
                                          (classToDirSubclasses.get(c)));
                }
            }
        }
    }

    private void checkState()
    {
        if (state != sc.getState())
            throw new ConcurrentModificationException("Scene changed for Hierarchy!");
    }

    // This includes c in the list of subclasses.
    /** Returns a list of subclasses of c, including itself. */
    public List<SootClass> getSubclassesOfIncluding(SootClass c)
    {
        c.checkLevel(SootClass.HIERARCHY);
        if (c.isInterface())
            throw new RuntimeException("class needed!");

        List<SootClass> l = new ArrayList<SootClass>();
        l.addAll(getSubclassesOf(c));
        l.add(c);

        return Collections.unmodifiableList(l);
    }

    /** Returns a list of subclasses of c, excluding itself. */
    public List<SootClass> getSubclassesOf(SootClass c)
    {
        c.checkLevel(SootClass.HIERARCHY);
        if (c.isInterface())
            throw new RuntimeException("class needed!");

        checkState();

        // If already cached, return the value.
        if (classToSubclasses.get(c) != null)
            return classToSubclasses.get(c);

        // Otherwise, build up the hashmap.
        List<SootClass> l = new ArrayList<SootClass>();

        ListIterator<SootClass> it = classToDirSubclasses.get(c).listIterator();
        while (it.hasNext())
        {
            SootClass cls = it.next();
            if( cls.resolvingLevel() < SootClass.HIERARCHY ) continue;
            l.addAll(getSubclassesOfIncluding(cls));
        }
        
        l = Collections.unmodifiableList(l);
        classToSubclasses.put(c, l);

        return l;
    }

    /** Returns a list of superclasses of c, including itself. */
    public List<SootClass> getSuperclassesOfIncluding(SootClass c)
    {
        c.checkLevel(SootClass.HIERARCHY);
        List<SootClass> l = getSuperclassesOf(c);
        ArrayList<SootClass> al = new ArrayList<SootClass>(); al.add(c); al.addAll(l);
        return Collections.unmodifiableList(al);
    }

    /** Returns a list of strict superclasses of c, starting with c's parent. */
    public List<SootClass> getSuperclassesOf(SootClass c)
    {
        c.checkLevel(SootClass.HIERARCHY);
        if (c.isInterface())
            throw new RuntimeException("class needed!");

        checkState();

        ArrayList<SootClass> l = new ArrayList<SootClass>();
        SootClass cl = c;

        while (cl.hasSuperclass())
        {
            l.add(cl.getSuperclass());
            cl = cl.getSuperclass();
        }

        return Collections.unmodifiableList(l);
    }

    /** Returns a list of subinterfaces of c, including itself. */
    public List<SootClass> getSubinterfacesOfIncluding(SootClass c)
    {
        c.checkLevel(SootClass.HIERARCHY);
        if (!c.isInterface())
            throw new RuntimeException("interface needed!");

        List<SootClass> l = new ArrayList<SootClass>();
        l.addAll(getSubinterfacesOf(c));
        l.add(c);

        return Collections.unmodifiableList(l);
    }

    /** Returns a list of subinterfaces of c, excluding itself. */
    public List<SootClass> getSubinterfacesOf(SootClass c)
    {
        c.checkLevel(SootClass.HIERARCHY);
        if (!c.isInterface())
            throw new RuntimeException("interface needed!");

        checkState();

        // If already cached, return the value.
        if (interfaceToSubinterfaces.get(c) != null)
            return interfaceToSubinterfaces.get(c);

        // Otherwise, build up the hashmap.
        List<SootClass> l = new ArrayList<SootClass>();

        ListIterator<SootClass> it = interfaceToDirSubinterfaces.get(c).listIterator();
        while (it.hasNext())
        {
            l.addAll(getSubinterfacesOfIncluding((SootClass)it.next()));
        }
        
        interfaceToSubinterfaces.put(c, Collections.unmodifiableList(l));

        return Collections.unmodifiableList(l);
    }

    /** Returns a list of superinterfaces of c, including itself. */
    public List<SootClass> getSuperinterfacesOfIncluding(SootClass c)
    {
        c.checkLevel(SootClass.HIERARCHY);
        if (!c.isInterface())
            throw new RuntimeException("interface needed!");

        List<SootClass> l = new ArrayList<SootClass>();
        l.addAll(getSuperinterfacesOf(c));
        l.add(c);

        return Collections.unmodifiableList(l);
    }

    /** Returns a list of superinterfaces of c, excluding itself. */
    public List<SootClass> getSuperinterfacesOf(SootClass c)
    {
        c.checkLevel(SootClass.HIERARCHY);
        if (!c.isInterface())
            throw new RuntimeException("interface needed!");

        checkState();

        // If already cached, return the value.
        if (interfaceToSuperinterfaces.get(c) != null)
            return interfaceToSuperinterfaces.get(c);

        // Otherwise, build up the hashmap.
        List<SootClass> l = new ArrayList<SootClass>();

        ListIterator<SootClass> it = interfaceToDirSuperinterfaces.get(c).listIterator();
        while (it.hasNext())
        {
            l.addAll(getSuperinterfacesOfIncluding((SootClass)it.next()));
        }
        
        interfaceToSuperinterfaces.put(c, Collections.unmodifiableList(l));

        return Collections.unmodifiableList(l);
    }

    /** Returns a list of direct superclasses of c, excluding c. */
    public List<SootClass> getDirectSuperclassesOf(SootClass c)
    {
        throw new RuntimeException("Not implemented yet!");
    }

    /** Returns a list of direct subclasses of c, excluding c. */
    public List<SootClass> getDirectSubclassesOf(SootClass c)
    {
        c.checkLevel(SootClass.HIERARCHY);
        if (c.isInterface())
            throw new RuntimeException("class needed!");

        checkState();

        return Collections.unmodifiableList(classToDirSubclasses.get(c));
    }

    // This includes c in the list of subclasses.
    /** Returns a list of direct subclasses of c, including c. */
    public List<SootClass> getDirectSubclassesOfIncluding(SootClass c)
    {
        c.checkLevel(SootClass.HIERARCHY);
        if (c.isInterface())
            throw new RuntimeException("class needed!");

        checkState();

        List<SootClass> l = new ArrayList<SootClass>();
        l.addAll(classToDirSubclasses.get(c));
        l.add(c);

        return Collections.unmodifiableList(l);
    }

    /** Returns a list of direct superinterfaces of c. */
    public List<SootClass> getDirectSuperinterfacesOf(SootClass c)
    {
        throw new RuntimeException("Not implemented yet!");
    }

    /** Returns a list of direct subinterfaces of c. */
    public List<SootClass> getDirectSubinterfacesOf(SootClass c)
    {
        c.checkLevel(SootClass.HIERARCHY);
        if (!c.isInterface())
            throw new RuntimeException("interface needed!");

        checkState();

        return interfaceToDirSubinterfaces.get(c);
    }

    /** Returns a list of direct subinterfaces of c, including itself. */
    public List<SootClass> getDirectSubinterfacesOfIncluding(SootClass c)
    {
        c.checkLevel(SootClass.HIERARCHY);
        if (!c.isInterface())
            throw new RuntimeException("interface needed!");

        checkState();

        List<SootClass> l = new ArrayList<SootClass>();
        l.addAll(interfaceToDirSubinterfaces.get(c));
        l.add(c);

        return Collections.unmodifiableList(l);
    }

    /** Returns a list of direct implementers of c, excluding itself. */
    public List<SootClass> getDirectImplementersOf(SootClass i)
    {
        i.checkLevel(SootClass.HIERARCHY);
        if (!i.isInterface())
            throw new RuntimeException("interface needed; got "+i);

        checkState();

        return Collections.unmodifiableList(interfaceToDirImplementers.get(i));
    }

    /** Returns a list of implementers of c, excluding itself. */
    public List<SootClass> getImplementersOf(SootClass i)
    {
        i.checkLevel(SootClass.HIERARCHY);
        if (!i.isInterface())
            throw new RuntimeException("interface needed; got "+i);

        checkState();

        Iterator<SootClass> it = getSubinterfacesOfIncluding(i).iterator();
        ArraySet<SootClass> set = new ArraySet<SootClass>();

        while (it.hasNext())
        {
            SootClass c = it.next();

            set.addAll(getDirectImplementersOf(c));
        }

        ArrayList<SootClass> l = new ArrayList<SootClass>();
        l.addAll(set);

        return Collections.unmodifiableList(l);
    }

    /**
     * Returns true if child is a subclass of possibleParent. If one of the
     * known parent classes is phantom, we conservatively assume that the
     * current class might be a child.
     */
    public boolean isClassSubclassOf(SootClass child, SootClass possibleParent)
    {
        child.checkLevel(SootClass.HIERARCHY);
        possibleParent.checkLevel(SootClass.HIERARCHY);
        List<SootClass> parentClasses = getSuperclassesOf(child);
        if (parentClasses.contains(possibleParent))
        	return true;
        
        for (SootClass sc : parentClasses)
        	if (sc.isPhantom())
        		return true;
        
        return false;
    }

    /**
     * Returns true if child is, or is a subclass of, possibleParent. If one of
     * the known parent classes is phantom, we conservatively assume that the
     * current class might be a child.
     */
    public boolean isClassSubclassOfIncluding(SootClass child, SootClass possibleParent)
    {
        child.checkLevel(SootClass.HIERARCHY);
        possibleParent.checkLevel(SootClass.HIERARCHY);
        List<SootClass> parentClasses = getSuperclassesOfIncluding(child);
        if (parentClasses.contains(possibleParent))
        	return true;
        
        for (SootClass sc : parentClasses)
        	if (sc.isPhantom())
        		return true;
        
        return false;
    }

    /** Returns true if child is a direct subclass of possibleParent. */
    public boolean isClassDirectSubclassOf(SootClass c, SootClass c2)
    {
        throw new RuntimeException("Not implemented yet!");
    }

    /** Returns true if child is a superclass of possibleParent. */
    public boolean isClassSuperclassOf(SootClass parent, SootClass possibleChild)
    {
        parent.checkLevel(SootClass.HIERARCHY);
        possibleChild.checkLevel(SootClass.HIERARCHY);
        return getSubclassesOf(parent).contains(possibleChild);
    }

    /** Returns true if parent is, or is a superclass of, possibleChild. */
    public boolean isClassSuperclassOfIncluding(SootClass parent, SootClass possibleChild)
    {
        parent.checkLevel(SootClass.HIERARCHY);
        possibleChild.checkLevel(SootClass.HIERARCHY);
        return getSubclassesOfIncluding(parent).contains(possibleChild);
    }

    /** Returns true if child is a subinterface of possibleParent. */
    public boolean isInterfaceSubinterfaceOf(SootClass child, SootClass possibleParent)
    {
        child.checkLevel(SootClass.HIERARCHY);
        possibleParent.checkLevel(SootClass.HIERARCHY);
        return getSubinterfacesOf(possibleParent).contains(child);
    }

    /** Returns true if child is a direct subinterface of possibleParent. */
    public boolean isInterfaceDirectSubinterfaceOf(SootClass child,
                                                   SootClass possibleParent)
    {
        child.checkLevel(SootClass.HIERARCHY);
        possibleParent.checkLevel(SootClass.HIERARCHY);
        return getDirectSubinterfacesOf(possibleParent).contains(child);
    }

    /** Returns true if parent is a superinterface of possibleChild. */
    public boolean isInterfaceSuperinterfaceOf(SootClass parent, SootClass possibleChild)
    {
        parent.checkLevel(SootClass.HIERARCHY);
        possibleChild.checkLevel(SootClass.HIERARCHY);
        return getSuperinterfacesOf(possibleChild).contains(parent);
    }

    /** Returns true if parent is a direct superinterface of possibleChild. */
    public boolean isInterfaceDirectSuperinterfaceOf(SootClass parent,
                                                   SootClass possibleChild)
    {
        parent.checkLevel(SootClass.HIERARCHY);
        possibleChild.checkLevel(SootClass.HIERARCHY);
        return getDirectSuperinterfacesOf(possibleChild).contains(parent);
    }

    /** Returns the most specific type which is an ancestor of both c1 and c2. */
    public SootClass getLeastCommonSuperclassOf(SootClass c1, 
                                                SootClass c2)
    {
        c1.checkLevel(SootClass.HIERARCHY);
        c2.checkLevel(SootClass.HIERARCHY);
        throw new RuntimeException("Not implemented yet!");
    }

    // Questions about method invocation.

    /** Returns true if the method m is visible from code in the class from. */
    public boolean isVisible( SootClass from, SootMethod m ) {
        from.checkLevel(SootClass.HIERARCHY);
        m.getDeclaringClass().checkLevel(SootClass.HIERARCHY);
        if( m.isPublic() ) return true;
        if( m.isPrivate() ) {
            return from.equals( m.getDeclaringClass() );
        }
        if( m.isProtected() ) {
            return isClassSubclassOfIncluding( from, m.getDeclaringClass() );
        }
        // m is package
        return from.getJavaPackageName().equals(
                m.getDeclaringClass().getJavaPackageName() );
            //|| isClassSubclassOfIncluding( from, m.getDeclaringClass() );
    }

	/**
	 * Given an object of actual type C (o = new C()), returns the method which
	 * will be called on an o.f() invocation.
	 */
	public SootMethod resolveConcreteDispatch(SootClass concreteType,
			SootMethod m) {
		concreteType.checkLevel(SootClass.HIERARCHY);
		m.getDeclaringClass().checkLevel(SootClass.HIERARCHY);
		checkState();

		if (concreteType.isInterface())
			throw new RuntimeException("class needed!");

		String methodSig = m.getSubSignature();

		for (SootClass c : getSuperclassesOfIncluding(concreteType)) {
			SootMethod sm = c.getMethodUnsafe(methodSig); 
			if (sm != null && isVisible(c, m)) {
				return sm;
			}
		}
		throw new RuntimeException(
				"could not resolve concrete dispatch!\nType: " + concreteType
						+ "\nMethod: " + m);
	}

    /** Given a set of definite receiver types, returns a list of possible targets. */
    public List<SootMethod> resolveConcreteDispatch(List<Type> classes, SootMethod m)
    {
        m.getDeclaringClass().checkLevel(SootClass.HIERARCHY);
        checkState();

        Set<SootMethod> s = new ArraySet<SootMethod>();
        for (Type cls : classes) {
            if (cls instanceof RefType)
                s.add(resolveConcreteDispatch(((RefType)cls).getSootClass(), m));
            else if (cls instanceof ArrayType) {
                s.add(resolveConcreteDispatch((RefType.v("java.lang.Object")).getSootClass(), m));
            }
            else throw new RuntimeException("Unable to resolve concrete dispatch of type "+ cls);
        }

        return Collections.unmodifiableList(new ArrayList<SootMethod>(s));
    }

    // what can get called for c & all its subclasses
    /** Given an abstract dispatch to an object of type c and a method m, gives
     * a list of possible receiver methods. */
    public List<SootMethod> resolveAbstractDispatch(SootClass c, SootMethod m) 
    {
        c.checkLevel(SootClass.HIERARCHY);
        m.getDeclaringClass().checkLevel(SootClass.HIERARCHY);
        checkState();

        Set<SootMethod> s = new ArraySet<SootMethod>();
        
        Iterator<SootClass> classesIt = null;

        if (c.isInterface()) {
            Set<SootClass> classes = new HashSet<SootClass>();
            for (SootClass sootClass : getImplementersOf(c)) {
            	classes.addAll(getSubclassesOfIncluding(sootClass));
            }
            classesIt = classes.iterator();
        }    
            
        else
            classesIt = getSubclassesOfIncluding(c).iterator();

        
        while (classesIt.hasNext()) {
            SootClass cl = classesIt.next();
            if( !Modifier.isAbstract( cl.getModifiers() ) ) {
            	s.add(resolveConcreteDispatch(cl, m));
            }
        }

        return Collections.unmodifiableList(new ArrayList<SootMethod>(s));
    }

    // what can get called if you have a set of possible receiver types
    /** Returns a list of possible targets for the given method and set of receiver types. */
    public List<SootMethod> resolveAbstractDispatch(List<SootClass> classes, SootMethod m)
    {
        m.getDeclaringClass().checkLevel(SootClass.HIERARCHY);
        Set<SootMethod> s = new ArraySet<SootMethod>();
        for (SootClass sootClass : classes ) {
            s.addAll(resolveAbstractDispatch(sootClass, m));
        }
        
        return Collections.unmodifiableList(new ArrayList<SootMethod>(s));
    }

    /** Returns the target for the given SpecialInvokeExpr. */
    public SootMethod resolveSpecialDispatch(SpecialInvokeExpr ie, SootMethod container)
    {
        container.getDeclaringClass().checkLevel(SootClass.HIERARCHY);
        SootMethod target = ie.getMethod();
        target.getDeclaringClass().checkLevel(SootClass.HIERARCHY);

        /* This is a bizarre condition!  Hopefully the implementation is correct.
           See VM Spec, 2nd Edition, Chapter 6, in the definition of invokespecial. */
        if ("<init>".equals(target.getName()) || target.isPrivate())
            return target;
        else if (isClassSubclassOf(target.getDeclaringClass(), container.getDeclaringClass())) {
            //return resolveConcreteDispatch(container.getDeclaringClass(), target);
            //SPECIAL-INVOKE-CHANGE
            return resolveConcreteDispatch(target.getDeclaringClass(), target);
        }
        else
            return target;
    }
}
