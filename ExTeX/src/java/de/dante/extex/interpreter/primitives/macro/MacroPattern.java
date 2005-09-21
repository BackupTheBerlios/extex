/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */
package de.dante.extex.interpreter.primitives.macro;

import de.dante.extex.interpreter.type.tokens.Tokens;


/**
 * This class provides a container for the pattern of a macro.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
 */
public class MacroPattern extends Tokens {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The constant <tt>EMPTY</tt> contains the empty macro pattern. This can
     * be used in a poor man's factory.
     */
    public static final MacroPattern EMPTY = new MacroPattern(Tokens.EMPTY, 0);

    /**
     * The field <tt>arity</tt> contains the artiy, i.e. the number or
     * parameters.
     */
    private int arity;

    /**
     * Creates a new object.
     *
     * @param tokens the tokens contained
     * @param theArity the number of parameters
     */
    public MacroPattern(final Tokens tokens, final int theArity) {

        super();
        add(tokens);
        this.arity = theArity;
    }

    /**
     * Creates a new object.
     */
    public MacroPattern() {

        super();
        this.arity = 0;
    }

    /**
     * Getter for the arity.
     * The arity is the number of parameters.
     *
     * @return the arity
     */
    public int getArity() {

        return arity;
    }

    /**
     * Setter for the arity.
     *
     * @param theArity the new arity
     */
    public void setArity(final int theArity) {

        this.arity = theArity;
    }

}
