/**
 * Simulates the native method side effects in class java.io.ObjectOutputStream
 *
 * @author Feng Qian
 * @author <XXX>
 */

package soot.jimple.toolkits.pointer.nativemethods;

import soot.*;
import soot.jimple.toolkits.pointer.representations.*;
import soot.jimple.toolkits.pointer.util.*;

public class JavaIoObjectOutputStreamNative extends NativeMethodClass {

  private static JavaIoObjectOutputStreamNative instance =
    new JavaIoObjectOutputStreamNative();

  private JavaIoObjectOutputStreamNative(){}

  public static JavaIoObjectOutputStreamNative v() { return instance; }

  /**
   * Implements the abstract method simulateMethod.
   * It distributes the request to the corresponding methods 
   * by signatures.
   */
  public void simulateMethod(SootMethod method,
			     ReferenceVariable thisVar,
			     ReferenceVariable returnVar,
			     ReferenceVariable params[]){

    String subSignature = method.getSubSignature();

    if (subSignature.equals("java.lang.Object getObjectFieldValue(java.lang.Object,long)")){
      java_io_ObjectOutputStream_getObjectFieldValue(method,
						     thisVar,
						     returnVar,
						     params);
      return;

    } else {
      defaultMethod(method, thisVar, returnVar, params);
      return;

    }
  }

  /******************* java.io.ObjectOutputStream *******************/
  /**
   * The object in field is retrieved out by field ID.
   *
   * private static native 
   *         java.lang.Object getObjectFieldValue(java.lang.Object, long);
   */
  public static 
    void java_io_ObjectOutputStream_getObjectFieldValue(
				    SootMethod method,
				    ReferenceVariable thisVar,
				    ReferenceVariable returnVar,
				    ReferenceVariable params[]){
    throw new NativeMethodNotSupportedException(method);
  }

  /**
   * Following three native methods have no side effects.
   *
   * private static native void floatsToBytes(float[], int, byte[], int, int);
   * private static native void doublesToBytes(double[], int, 
   *                                           byte[], int, int);
   * private static native void getPrimitiveFieldValues(java.lang.Object, 
   *                                                    long[], 
   *                                                    char[], 
   *                                                    byte[]);
   * @see default(...)
   */
}