# Object Sensitive Spark Analysis

This document describes the object sensitive add-on implementation for
Spark as of Spring 2014. The objective is to give one a high-level
understanding of the object sensitive implementation.

It is recommended that one read the following papers before reading
this document:

* [SPARK Master's Thesis](http://citeseerx.ist.psu.edu/viewdoc/download;jsessionid=F82B693D7295363102D22C9827D31EC8?doi=10.1.1.143.5075&rep=rep1&type=pdf)

* [Pick Your Contexts
  Well](http://cgi.di.uoa.gr/~smaragd/typesens-popl11.pdf)

* 

## Overview

Spark is context insensitive out of the box.  By default, the Spark
analysis is a straightforward implementation of a points-to analysis
for Java.  The analysis begins with analyzing the entry points of a
java program (as defined by the `main` method or other entry points
denoted in Soot), and builds a pointer assignment graph (PAG) based on
the assignments and side effects of program statements in the entry
methods.  Method call expressions are explored as they are
encountered, and the call graph is built on the fly, as the analysis
progresses.

The Spark analysis is a fixed-point iterative analysis, that will
terminate when their are no changes to the PAG and the call graph.  

Unfortunately, Spark is a disorganized mess because it was modified
haphazardly, and it tries to be very flexible as a research vehicle.

Spark includes some hooks for context, and where appropriate, we have
attempted to take advantage of these hooks for the object sensitive
implementation.  However, the object sensitive implementation is
currently not well integrated in Spark, as you will see if once you
look at the code.

The object sensitive analysis is grafted onto Spark such that every
method in the call graph has a context.  Also the following program
elements have context: Locals, arguments, `this`, and heap location
(defined by allocation sites or nodes).

### Context

In the object sensitive analysis context is defined as a string of
context elements.  A context element can be one of:

* NO-CONTEXT: Denotes the absence of object sensitive context.  Used
  when it is decided that we do not want to have object sensitivity on
  an heap location or method.  

* Allocation Site: InsensitiveAllocNode representing a new expression
  in the program.

* Entry Context: Denotes a entry point to the app or program we are
  analyzing.  

* STATIC-INIT: Denotes a call to a static initializer from our
  abstract runtime.

* Type:  Denotes the class of an allocation site (a less precise
  version of the exactly allocation site location in code).

There are two forms of context in our object sensitive analysis: heap
and method context.  

Heap context is context that is associated with heap locations. Heap
locations are represented in their most significant context element by
an allocation site, and following context elements are any type of
context element.  For example:

[new A() in B.foo() line 20, new B() in C.bar() line 30]

Denotes an object of class A that is created in method B.foo() of the
object of class B created in method C.bar().

Method context is context that is associated with a method in the call
graph.  If a method is an instance method, the context represents the
heap location (allocation site) that is the receiver of the method.
The `this` pointer of the instance method will point to the method
context.  

For a static method, the context represents the last instance method
in the call stack to call the static method, or if the static method
is an entry point (or called from an entry point), the context could
be STATIC-INIT (to denote it is or is called from a static
initializer) or ENTRY-CONTEXT to denote it is or is called from an
entry point.

Methods can have NO-CONTEXT as their context denoting that for some
reason no context has been associated with this method.  In the call
graph there is exactly one node representing each no context version
of a method.  This no context version is a catch all for all calls
that have no context.

In the PAG, nodes are parameterized by context.  For instance a local
of an instance method has one node in the PAG for each context of the
parent method of the local.  Analysis results are tracked separately
for each context version of the local.

### Call Graph

Vertices in the call graph represent a method x context.  Before, in
the call graph vertices are represented by SootMethod.  Now they are
represented by MethodContext which includes a SootMethod and a
Context.  Both MethodContext and SootMethod implement the interface
MethodOrMethodContext, and this interface is the actual type of call
graph nodes, such that we can use the same implementation for both
context and context insensitive runs.

## Options

* k: depth of the object sensitive context for both method and heap.
  This number corresponds to the number of allocation sites (or types)
  in the context string for methods and objects in the heap.  This
  value can be overrode by some of the options below on a class
  basis.  

* important-allocators:  A list of classes for which we want to track
  all allocations within the class with full context (k above).  If
  this list is empty, all classes are treated the same, and all
  allocation sites have full context depth of k.  Otherwise, if this
  list is non-empty, then we keep a context depth of k for
  object allocations only if an allocation site of class C appears in
  a context string, for C in the list.  

  In practice, we use this list to keep context for user code, and a
  depth of k from user code.  So by default this list includes all
  user code, meaning intuitively, that if we get far away from user
  code (in the call graph) in the API, we will lose context and assign
  NO_CONTEXT to allocations that will not include an important
  allocator in the context string.

* limit-heap-context: A list of classes for which we will limit the
  heap and method context to depth one.  For example, if class C is in
  this list, then any objects in the heap will have a allocation
  string of length one (the actual allocation site) to represent the
  object. This is useful for Strings and GUI objects that are
  allocated frequently, and we would like to keep some precision but
  limit precision for scalability.  This overrides the value k above.

* no-context-list: A list of classes for which no context will be
  associated with their allocations.  This means that we will lose
  precision for all allocations of these classes, and also for across
  inherited methods, especially methods of the Object class that are
  inherited.  

* context-for-static-inits:  This boolean value, if true, will add a
  special context element, STATIC_INIT, that represents entry into a
  static initializer.  If true, objects allocated in a static init
  will have this context element in the context string.  If false,
  then static initializers are lumped in the NO_CONTEXT.

* types-for-context-greater-than-one:  This boolean value, if true,
  will instruct the analysis to use types for context elements beyond
  the most significant allocation site.  It is experimental and has
  not provided appropriate precision for our client analyses.  


## Major Changes to Soot

AllocNode has been modified to be an abstract class that intuitively
represents a heap location.  The two classes that are concrete
implementations of AllocNode are InsensitiveAllocNode (the old
notion of an AllocNode, representing an allocation site), and
ObjectSensitiveAllocNode which represents an object sensitive heap
location and includes a context string made of `ContextElement`s (see
below).  

The context of an instance method can be used to add more precision to
the `this` reference.  The `this` reference for a method with a
context that is a heap location, can only reference the heap
location.  In our implementation, we assume we are using the
PropWorkList (`soot.jimple.spark.solver.PropWorkList`) propagator to
create the points to sets.  The work list propagator has been modified
such that when it is consolidating points to sets for the this points,
it prunes the set based on the context of the enclosing method.  

Many classes have been made context aware by changing SootMethod to
MethodOrMethodContext.  

All entry points methods from the Scene are converted in MethodContext
with the original method x EntryContext.

## Implementation Classes:

This section gives a high level overview of the important
implementation classes.

### Entry point to Spark.

`soot.jimple.spark.SparkTransformer`: Entry point to analysis, resets
object sensitive configuration, resets universe of object sensitive
allocation sites. Starts PAG creation, and call graph construction.

### ObjectSensitiveAllocNode

`soot.jimple.spark.pag.ObjectSensitiveAllocNode` (OSAN) represents an
object sensitive heap location in our analysis, and is the object
sensitive version of the old AllocNode from Spark.  It is a Context
(see below).

OSAN contain an array of ContextElements (see below). This array
represents the Context of the heap location, and elements can be
allocation sites or other context elements such as static
initializers, that denote the "ancestors" of the most significant
allocation site.  These context elements are built up during the call
graph construction when methods context is switched on a method call.

Typically an OSAN is created when the analysis encounters a new
expression.  The new OSAN is a concatenation of the new expression
(allocation site) and the context of the method that encloses the
allocation site.  The context string may need to be truncated to be of
length k (depth) after the concatenation.

All OSAN creations are handled by the class itself, the constructor is
private.  During the run of the analysis, a universe of OSANs are
tracked, and when we need an OSAN the universe is searched to see if
the appropriate node has already been created.

A client that uses OSAN asks for a OSAN to represent an
InsensitiveAllocNode in a given method context.  This is performed in
`getObjSensNode()`.  The method returns the OSAN that should be used
to represent the combination. 

The method first creates a new OSAN for the combination, and then
calls the method addHeapContext() of ObjectSensitiveConfig to decide
if the new OSAN should be kept.  If heap context should not be kept
for with the new node, then an OSAN representing the
InsensitiveAllocNode in NO-CONTEXT is returned.

Otherwise, if the new OSAN should be kept, the universe is consulted
to see if it has already been created.  If so, the node from the
universe is returned.  Otherwise, the node is added to the universe
and returned.

The most signification context element of an OSAN is the allocation
site itself, and this information is stored in fields inherited from
AllocNode.  

### ObjectSensitiveConfig

Due to the fact that the implementation had to be completed quickly,
and it is experimental, configuration and helper methods are stored in
the class `soot.jimple.spark.pag.ObjectSensitiveConfig` and accessed
in a static fashion.

There are fields representing the config options listed above.

There are methods to determine whether context should be added for a
give method or a given allocation site.

To determine if context should be added or retained given a method and
a context (see `addMethodContext` in ObjectSensitiveConfig):

1. If the method is contained in an important allocator, add context.

2. If the context should be retained as context in the analysis return
true.  This determined by calling `boolean addHeapContext(...)` in
ObjectSensitiveConfig and is explained next.

After the analysis has constructed a new context at an allocation site
(from the allocation site and the enclosing method's context), we need
to decide how to treat this new context.  We might want to summarize
it somehow, or we might decide that we want to throw away the context
and represent it by NO-CONTEXT because the context string has lost any
important allocators.

The method `boolean addHeapContext(OSAN probe)` will decide if the context
element represented by probe should be used in the analysis as a
context.  The calculation for this method is as follows:

1. If the type of the object allocation (the class) is on the no
context list, then return false.

2. If there are no important allocators (meaning every class is
important) then return true, we always want the new context.

3. Otherwise, if there are important allocators and the context string
is all allocation sites, then search the context string elements of
`probe` that are allocation sites for a code location in an important
allocator.  If we cannot find a location in an important allocator
return false.

4. Otherwise if the context string starts with EntryContext or
StaticInitContext, return true to keep the context propagated.

5. Otherwise, return false.

ObjectSensitiveConfig also includes a helper method to help prune the
`this` reference for points to sets called by PAG.

### Context

Most classes representing context are currently in the
`soot.jimple.spark.pag` package.  There are two interfaces that
distinguish organize context:

* `soot.Context`: Classes that implement this interface are 
  context (heap or method).

* `soot.jimple.spark.pag.ContextElement`: Classes that implement this
  interface are elements of context, the alphabet of a context string.

The following classes implement Context (only classes important to
object sensitivity are listed):

* NoContext: Represents the "no context" context.

* EntryContext: Represents the entry context, entry point to the
  program.

* StaticInitContext: Represents static initializer method, and
  includes the class type of the context.  So there is a separate
  StaticInitContext for each class (to represent each static
  initializer distinctly).

* ObjectSensitiveAllocNode: Represents a heap location that is object
  sensitivity to a depth k.  More information below.

The following classes are context elements, meaning they can appear as
elements in the context string of heap location (represented by
ObjectSensitiveAllocNode):  

* ClassConstantNode: Class constants are essentially heap locations
  (allocations) in the PAG, and references of the appropriate type can
  reference them. 

* StringConstantNode: String constants are heap locations (an
  allocation that corresponds to the constant).

* EntryContext: Discussed above.

* InsensitiveAllocNode:  This class represents an allocation site (the
  old Spark notion of an AllocNode).  It is used as the allocation
  site element in the context string of ObjectSensitiveAllocNode.

* NoContext: Discussed above.

* StaticInitContext: Discussed above.

* TypeContextElement: This class represents the type of the class that
  is an allocator in a context string.  For example, if we have two
  context strings of allocation sites:

  [new A() in B.foo() line 24, new B() in C.bar() line 55]
  [new A() in B.foo() line 24, new B() in C.baz() line 64]

  We notice that the most signification allocation site is the same,
  but the second element of the strings differ.  If we are looking to
  combine these strings for scalability reasons (such that we don't
  have separate method and heap contexts representing each), we can
  reduce the precision on the second element by representing it by its
  type: C.  Then both can be represented by one element:

  [new A() in B.foo() line 24, TypeContext: C]

  Where the second element is a TypeContextElement denoting that B was
  allocated somewhere in C.  We are lumping together all allocations
  of B in C.

## Spark Implementation and Changes Made for Object Sensitivity

The Spark implementation is haphazard and oddly divided.  In this
section, we will try to give some explanation for various Spark
classes and the changes we made to them.

### soot.jimple.spark.pag.PAG

This class represents the pointer assignment graph and methods to
build and query the graph.  

Implementations of queries given context were changed to support
context sensitive queries.

Creation of AllocNodes were modified to support creation of object
sensitive allocation nodes in the PAG.

### soot.jimple.spark.pag.InsensitiveAllocNode

This node is similar to the original AllocNode, but now denotes an
allocation site with no context, that is a context element.  It also
supports context lookups such that given an insensitive alloc node,
one can find the object sensitive alloc node that represents the
insensitive allocnode for a given context.

### soot.jimple.spark.pag.MethodPAG

The method PAG class represents a section of the PAG corresponding to
a method.  Each method has a corresponding MethodPAG that is created
at most once for each run of Spark. The MethodPAG now supports
contexts through parameterization by context.  Each time a method x
context is deemed reachable, a version of the method pag is added to
the PAG for the context.  The context nodes are added to the pag at
mode one time.

All MethodPAGs are cleared after each run of Spark.

Minor changes made to support correct parameterization by AllocNode
(ObjectSensitiveAllocNode).


### soot.jimple.spark.builder.ObjectSensitiveBuilder

This class is created during PAG setup to call the MethodPAG on any
classes that define entry points.  Does not really seem needed, and
has only minor edits for object sensitivity.

### soot.jimple.spark.builder.MethodNodeFactory

This class builds the MethodPAG nodes for a method.  This is done once
per MethodPAG.

StringConstants and ClassConstants are handled strangely as global
nodes, so they cannot appear as referenced by locals directly.  This
affects how we can use them as context, and currently, this affects
precision since constants of the same value are global.

### soot.jimple.spark.solver.OnFlyCallGraph

Interface between PAG and OnFlyCallGraphBuilder.  This class
initializes the callgraph.  We have modified it such that it creates
context versions of all entry point methods with the ENTRY-CONTEXT.

Every time a variable in the PAG is updated, there is a callback into
this class that calls the OnFlyCallGraphBuilder to determine if the
variable is a receiver for a method call, and if the change will
modify the call graph.

StringConstants are handled similarly to try to handle reflection.

### soot.jimple.toolkits.callgraph.OnFlyCallGraphBuilder

This class builds the call graph as Spark progresses.  There is a
bunch of code in here to handle reflection, but I gutted most of it
because it was not very effective.  

Significant modifications were made to this class to make it context
aware, and handle context correctly for each newly discovered method x
context.  

This class also searches new method bodies to find variables that are
used as receivers, such that they can be watched, to find static
method calls in new methods discovered.

If a variable node is update that is used as a receiver, eventually,
this class will be notified, and will check the concrete method
resolved for the possibly new type (class) of allocation node.  

### soot.jimple.toolkits.callgraph.ObjSensContextManager.java

This class controls how context is added to edges of the call graph.
There are two important methods, both called by OnFlyCallGraphBuilder
when adding edges to the call graph:

* `addStaticEdge(MethodOrMethodContext src, SootMethod target, Context
  context)`: Used to add a static edge to the call graph.  Uses the
  `context` for the target of the new edge only if method context should
  be added for the `context`.  See ObjectSensitiveConfig above for a
  description of this calculation.

* `addVirtualEdge(MethodOrMethodContext src, SootMethod target,
  Context context)`: Used to add a virtual edge (call to an instance
  method) to the call graph.  Uses context for the target of the new
  edge only if the method context should be added for `context`.  See
  ObjectSensitiveConfig above for a description of this calculation.
