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

package soot.jimple.spark.pag;
import soot.RefType;
import soot.jimple.StringConstant;
import soot.jimple.toolkits.pta.IStringConstantNode;

/** Represents an allocation site node the represents a constant string.
 * @author Ondrej Lhotak
 */
public class StringConstantNode extends AllocNode implements IStringConstantNode { // (LWG) implements IStringConstantNode
    public String toString() {
	return "StringConstantNode "+getNumber()+" "+getString();
    }

    public String getString() {
        return ((StringConstant) newExpr).value;
    }

    /* End of public methods. */

    public StringConstantNode( PAG pag, StringConstant sc ) {
        super( pag, sc, RefType.v( "java.lang.String" ), null );
    }
}

