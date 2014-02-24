/* Soot - a J*va Optimization Framework
 * Copyright (C) 2003 Ondrej Lhotak
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

package soot;
import java.util.*;

/** Represents a pair of a method and a context.
 * @author Ondrej Lhotak
 */
public final class MethodContext implements MethodOrMethodContext
{ 
    private static final Map<MethodContext, MethodContext> MethodContext_map = 
            new HashMap<MethodContext, MethodContext>();
    
    private SootMethod method;
    public SootMethod method() { return method; }
    private Context context;
    public Context context() { return context; }
    private MethodContext( SootMethod method, Context context ) {
        this.method = method;
        this.context = context;
    }
    
    public static void clearUniverse() {
        MethodContext_map.clear();
    }
    
    public static int universeSize() {
        return MethodContext_map.size();
    }
  
    public static MethodOrMethodContext v( SootMethod method, Context context ) {
        if( context == null ) return method;
        MethodContext probe = new MethodContext( method, context );
        Map<MethodContext, MethodContext> map = MethodContext_map;
        MethodContext ret = map.get( probe );
        if( ret == null ) {
            map.put( probe, probe );
            return probe;
        }
        return ret;
    }
    public String toString() {
        return "Method "+method+" in context "+context;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((context == null) ? 0 : context.hashCode());
        result = prime * result + ((method == null) ? 0 : method.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        MethodContext other = (MethodContext) obj;
        if (context == null) {
            if (other.context != null) return false;
        } else if (!context.equals(other.context)) return false;
        if (method == null) {
            if (other.method != null) return false;
        } else if (!method.equals(other.method)) return false;
        return true;
    }
    
    
}
