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

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import soot.jimple.SpecialInvokeExpr;
import soot.util.ArraySet;
import soot.util.Chain;

/** Represents the class hierarchy.  It is closely linked to a Scene,
 * and must be recreated if the Scene changes.
 *
 * The general convention is that if a method name contains
 * "Including", then it returns the non-strict result; otherwise,
 * it does a strict query (e.g. strict superclass).  */
public class Hierarchy
{
    // These two maps are not filled in the constructor.
    HashMap<SootClass, List<SootClass>> classToSubclasses;
    HashMap<SootClass, List<SootClass>> interfaceToSubinterfaces;

    HashMap<SootClass, List> classToDirSubclasses;
    HashMap<SootClass, List> interfaceToDirSubinterfaces;

    // This holds the direct implementers.
    HashMap<SootClass, List> interfaceToDirImplementers;

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
            Chain allClasses = sc.getClasses();

            classToSubclasses = new HashMap<SootClass, List<SootClass>>(allClasses.size() * 2 + 1, 0.7f);
            interfaceToSubinterfaces = new HashMap<SootClass, List<SootClass>>(allClasses.size() * 2 + 1, 0.7f);

            classToDirSubclasses = new HashMap<SootClass, List>
                (allClasses.size() * 2 + 1, 0.7f);
            interfaceToDirSubinterfaces = new HashMap<SootClass, List>
                (allClasses.size() * 2 + 1, 0.7f);
            interfaceToDirImplementers = new HashMap<SootClass, List>
                (allClasses.size() * 2 + 1, 0.7f);

            Iterator classesIt = allClasses.iterator();
            while (classesIt.hasNext())
            {
                SootClass c = (SootClass)classesIt.next();
                if( c.resolvingLevel() < SootClass.HIERARCHY ) continue;

                if (c.isInterface())
                {
                    interfaceToDirSubinterfaces.put(c, new ArrayList());
                    interfaceToDirImplementers.put(c, new ArrayList());
                }
                else
                    classToDirSubclasses.put(c, new ArrayList());
            }

            classesIt = allClasses.iterator();
            while (classesIt.hasNext())
            {
                SootClass c = (SootClass)classesIt.next();
                if( c.resolvingLevel() < SootClass.HIERARCHY ) continue;

                if (c.hasSuperclass())
                {
                    if (c.isInterface())
                    {
                        Iterator subIt = c.getInterfaces().iterator();

                        while (subIt.hasNext())
                        {
                            SootClass i = (SootClass)subIt.next();
                            if( c.resolvingLevel() < SootClass.HIERARCHY ) continue;
                            List<SootClass> l = interfaceToDirSubinterfaces.get(i);
                            if (l != null) l.add(c);
                        }
                    }
                    else
                    {
                        List<SootClass> l = classToDirSubclasses.get(c.getSuperclass());
                        l.add(c);


                        Iterator subIt = c.getInterfaces().iterator();

                        while (subIt.hasNext())
                        {
                            SootClass i = (SootClass)subIt.next();
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
                        Set<SootClass> s = new ArraySet();

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
                    interfaceToDirImplementers.put(c, Collections.unmodifiableList
                                                (interfaceToDirImplementers.get(c)));
                }
                else
                    classToDirSubclasses.put(c, Collections.unmodifiableList
                                          (classToDirSubclasses.get(c)));
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
            throw new RuntimeException("class needed! (got '"+ c.getName() +"' which is interface and thus cannot have subclasses)");

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
            throw new RuntimeException("class needed! (got '"+ c.getName() +"' which is an interface and thus cannot have subclasses)");

        checkState();

        // If already cached, return the value.
        if (classToSubclasses.get(c) != null)
            return classToSubclasses.get(c);

        // Otherwise, build up the hashmap.
        List<SootClass> l = new ArrayList<SootClass>();

        ListIterator it = classToDirSubclasses.get(c).listIterator();
        while (it.hasNext())
        {
            SootClass cls = (SootClass) it.next();
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
            throw new RuntimeException("class needed! (got '"+ c.getName() +"' which is an interface and thus cannot have a superclass)");

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
            throw new RuntimeException("interface needed! (got '"+ c.getName() +"' which is not an interface and thus cannot have subinterfaces)");

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
            throw new RuntimeException("interface needed! (got '"+ c.getName() +"' which is not an interface and thus cannot have subinterfaces)");

        checkState();

        // If already cached, return the value.
        if (interfaceToSubinterfaces.get(c) != null)
            return interfaceToSubinterfaces.get(c);

        // Otherwise, build up the hashmap.
        List<SootClass> l = new ArrayList<SootClass>();

        ListIterator it = interfaceToDirSubinterfaces.get(c).listIterator();
        while (it.hasNext())
        {
            l.addAll(getSubinterfacesOfIncluding((SootClass)it.next()));
        }

        interfaceToSubinterfaces.put(c, Collections.unmodifiableList(l));

        return Collections.unmodifiableList(l);
    }

    /** Returns a list of superinterfaces of c, excluding itself. */
    public List getSuperinterfacesOf(SootClass c)
    {
        throw new RuntimeException("Not implemented yet!");
    }

    /** Returns a list of direct superclasses of c, excluding c. */
    public List getDirectSuperclassesOf(SootClass c)
    {
        throw new RuntimeException("Not implemented yet!");
    }

    /** Returns a list of direct subclasses of c, excluding c. */
    public List getDirectSubclassesOf(SootClass c)
    {
        c.checkLevel(SootClass.HIERARCHY);
        if (c.isInterface())
            throw new RuntimeException("class needed! (got '"+ c.getName() +"' which is an interface and thus cannot not have subclasses)");

        checkState();

        return Collections.unmodifiableList(classToDirSubclasses.get(c));
    }

    // This includes c in the list of subclasses.
    /** Returns a list of direct subclasses of c, including c. */
    public List<SootClass> getDirectSubclassesOfIncluding(SootClass c)
    {
        c.checkLevel(SootClass.HIERARCHY);
        if (c.isInterface())
            throw new RuntimeException("class needed! (got '"+ c.getName() +"' which is an interface and thus cannot have subclasses)");

        checkState();

        List<SootClass> l = new ArrayList<SootClass>();
        l.addAll(classToDirSubclasses.get(c));
        l.add(c);

        return Collections.unmodifiableList(l);
    }

    /** Returns a list of direct superinterfaces of c. */
    public List getDirectSuperinterfacesOf(SootClass c)
    {
        throw new RuntimeException("Not implemented yet!");
    }

    /** Returns a list of direct subinterfaces of c. */
    public List getDirectSubinterfacesOf(SootClass c)
    {
        c.checkLevel(SootClass.HIERARCHY);
        if (!c.isInterface())
            throw new RuntimeException("interface needed! (got '"+ c.getName() +"' which is not an interface and thus cannot have subinterfaces)");

        checkState();

        return interfaceToDirSubinterfaces.get(c);
    }

    /** Returns a list of direct subinterfaces of c, including itself. */
    public List<SootClass> getDirectSubinterfacesOfIncluding(SootClass c)
    {
        c.checkLevel(SootClass.HIERARCHY);
        if (!c.isInterface())
            throw new RuntimeException("interface needed! (got '"+ c.getName() +"' which is not an interface and thus cannot have subinterfaces)");

        checkState();

        List<SootClass> l = new ArrayList<SootClass>();
        l.addAll(interfaceToDirSubinterfaces.get(c));
        l.add(c);

        return Collections.unmodifiableList(l);
    }

    /** Returns a list of direct implementers of c, excluding itself. */
    public List getDirectImplementersOf(SootClass i)
    {
        i.checkLevel(SootClass.HIERARCHY);
        if (!i.isInterface())
            throw new RuntimeException("interface needed! (got '"+ i.getName() +"' which is not an interface and thus cannot be implemented)");

        checkState();

        return Collections.unmodifiableList(interfaceToDirImplementers.get(i));
    }

    /** Returns a list of implementers of c, excluding itself. */
    public List<SootClass> getImplementersOf(SootClass i)
    {
        i.checkLevel(SootClass.HIERARCHY);
        if (!i.isInterface())
            throw new RuntimeException("interface needed! (got '"+ i.getName() +"' which is not an interface and thus cannot be implemented)");

        checkState();

        Iterator<SootClass> it = getSubinterfacesOfIncluding(i).iterator();
        ArraySet set = new ArraySet();

        while (it.hasNext())
        {
            SootClass c = it.next();

            set.addAll(getDirectImplementersOf(c));
        }

        ArrayList l = new ArrayList();
        l.addAll(set);

        return Collections.unmodifiableList(l);
    }

    /** Returns true if child is a subclass of possibleParent. */
    public boolean isClassSubclassOf(SootClass child, SootClass possibleParent)
    {
        child.checkLevel(SootClass.HIERARCHY);
        possibleParent.checkLevel(SootClass.HIERARCHY);
        return getSuperclassesOf(child).contains(possibleParent);
    }

    /** Returns true if child is, or is a subclass of, possibleParent. */
    public boolean isClassSubclassOfIncluding(SootClass child, SootClass possibleParent)
    {
        child.checkLevel(SootClass.HIERARCHY);
        possibleParent.checkLevel(SootClass.HIERARCHY);
        return getSuperclassesOfIncluding(child).contains(possibleParent);
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

    /** Given an object of actual type C (o = new C()), returns the method which will be called
        on an o.f() invocation. */
    public SootMethod resolveConcreteDispatch(SootClass concreteType, SootMethod m)
    {
        concreteType.checkLevel(SootClass.HIERARCHY);
        m.getDeclaringClass().checkLevel(SootClass.HIERARCHY);
        checkState();

        if (concreteType.isInterface())
            throw new RuntimeException("class needed! (got class '"+ concreteType.getName() +"' which is an interface)");

        Iterator<SootClass> it = getSuperclassesOfIncluding(concreteType).iterator();
        String methodSig = m.getSubSignature();

        while (it.hasNext())
        {
            SootClass c = it.next();
            if (c.declaresMethod(methodSig)
            && isVisible( c, m )
            ) {
                return c.getMethod(methodSig);
            }
        }
        throw new RuntimeException("could not resolve concrete dispatch!\nType: "+concreteType+"\nMethod: "+m);
    }

    /** Given a set of definite receiver types, returns a list of possible targets. */
    public List resolveConcreteDispatch(List classes, SootMethod m)
    {
        m.getDeclaringClass().checkLevel(SootClass.HIERARCHY);
        checkState();

        ArraySet s = new ArraySet();
        Iterator classesIt = classes.iterator();

        while (classesIt.hasNext()) {
            Object cls = classesIt.next();
            if (cls instanceof RefType)
                s.add(resolveConcreteDispatch(((RefType)cls).getSootClass(), m));
            else if (cls instanceof ArrayType) {
                s.add(resolveConcreteDispatch((RefType.v("java.lang.Object")).getSootClass(), m));
            }
            else throw new RuntimeException("Unable to resolve concrete dispatch of type "+ cls);
        }

        List l = new ArrayList(); l.addAll(s);
        return Collections.unmodifiableList(l);
    }

    // what can get called for c & all its subclasses
    /** Given an abstract dispatch to an object of type c and a method m, gives
     * a list of possible receiver methods. */
    public List resolveAbstractDispatch(SootClass c, SootMethod m)
    {
        c.checkLevel(SootClass.HIERARCHY);
        m.getDeclaringClass().checkLevel(SootClass.HIERARCHY);
        checkState();

        Iterator<SootClass> classesIt = null;

        if (c.isInterface()) {
            classesIt = getImplementersOf(c).iterator();
            HashSet<SootClass> classes = new HashSet<SootClass>();
            while (classesIt.hasNext())
                classes.addAll(getSubclassesOfIncluding(classesIt.next()));
            classesIt = classes.iterator();
        }

        else
            classesIt = getSubclassesOfIncluding(c).iterator();

        ArraySet s = new ArraySet();

        while (classesIt.hasNext()) {
            SootClass cl = classesIt.next();
            if( Modifier.isAbstract( cl.getModifiers() ) ) continue;
            s.add(resolveConcreteDispatch(cl, m));
        }

        List l = new ArrayList(); l.addAll(s);
        return Collections.unmodifiableList(l);
    }

    // what can get called if you have a set of possible receiver types
    /** Returns a list of possible targets for the given method and set of receiver types. */
    public List resolveAbstractDispatch(List classes, SootMethod m)
    {
        m.getDeclaringClass().checkLevel(SootClass.HIERARCHY);
        ArraySet s = new ArraySet();
        Iterator classesIt = classes.iterator();

        while (classesIt.hasNext())
            s.addAll(resolveAbstractDispatch((SootClass)classesIt.next(), m));

        List l = new ArrayList(); l.addAll(s);
        return Collections.unmodifiableList(l);
    }

    /** Returns the target for the given SpecialInvokeExpr. */
    public SootMethod resolveSpecialDispatch(SpecialInvokeExpr ie, SootMethod container)
    {
        container.getDeclaringClass().checkLevel(SootClass.HIERARCHY);
        SootMethod target = ie.getMethod();
        target.getDeclaringClass().checkLevel(SootClass.HIERARCHY);

        /* This is a bizarre condition!  Hopefully the implementation is correct.
           See VM Spec, 2nd Edition, Chapter 6, in the definition of invokespecial. */
        if (target.getName().equals("<init>") || target.isPrivate())
            return target;
        else if (isClassSubclassOf(target.getDeclaringClass(), container.getDeclaringClass()))
            return resolveConcreteDispatch(container.getDeclaringClass(), target);
        else
            return target;
    }
}
