/*
 * Copyright (C) 2003-2004 Gerd Neugebauer, Michael Niedermair
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
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 */
package de.dante.extex.interpreter.type;

import java.io.Serializable;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.util.GeneralException;

/**
 * Class for a <tt>glue</tt>-value.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.9 $
 */
public class Glue implements Serializable {

	/**
	 * the length 
	 */
    private GlueComponent length = new GlueComponent(0);

    /** 
     * the shrink
     */
    private GlueComponent shrink = new GlueComponent(0);

    /**
     * the stretch 
     */
    private GlueComponent stretch = new GlueComponent(0);

    /**
     * Creates a new object.
     * 
     * @param length ...
     */
    public Glue(final long length) {
        super();
        this.length = new Dimen(length);
    }

    /**
     * Creates a new object.
     * 
     * @param length ...
     * @param stretch ...
     * @param shrink ...
     */
    public Glue(final GlueComponent length, final GlueComponent stretch,
            final GlueComponent shrink) {
        super();
        this.length = length;
        this.stretch = stretch;
        this.shrink = shrink;
    }

    /**
     * Creates a new object.
     * 
     * @param length ...
     */
    public Glue(final Dimen length) {
        super();
        this.length = length;
    }
    
    /**
     * Creates a new object.
     * 
     * @param source ...
     * @param context ...
     * @throws GeneralException in case of an error
     */
    public Glue(final TokenSource source, final Context context)
            throws GeneralException {
        super();
        this.length = new Dimen(context, source);
        if (source.scanKeyword("plus")) {
            this.stretch = new GlueComponent(source, context, true);
        }
        if (source.scanKeyword("minus")) {
            this.shrink = new GlueComponent(source, context, true);
        }
    }

    /**
     * ...
     * 
     * @return ...
     */
    public Dimen getLength() {
        return new Dimen(length.getValue());
    }

    /**
     * ...
     * 
     * @return ...
     */
    public Glue copy() {
        return new Glue(length.copy(),stretch.copy(),shrink.copy());
    }
    
    /**
     * ...
     * 
     * @param g the glue to add
     *
     * @return this to allow the combination in an expression
     */
    public Glue add(final Glue g) {
        //TODO incomplete
        return this;
    }

    /**
     * ...
     * 
     * @param nom
     * @param denom
     * @return
     */
    public Glue multiply(final long nom, final long denom) {
        //TODO incomplete
        return this;
    }

    /**
     * ...
     * 
     * @param nom
     * @param denom
     * @return
     */
    public Glue multiplyStretch(final long nom, final long denom) {
        //TODO incomplete
        return this;
    }

    /**
     * ...
     * 
     * @param nom
     * @param denom
     * @return
     */
    public Glue multiplyShrink(final long nom, final long denom) {
        //TODO incomplete
        return this;
    }
    
    /**
     * ...
     *
     * @return the string representation of this glue
     * @see "TeX -- The Program [178,177]"
     */
    public String toString() {
    	return length.toString() + " minus " + shrink.toString() + " plus " + stretch.toString();
    }
    
    /**
     * Return a String with the Glue-value in pt 
     * @return a String with the Glue-value in pt
     */
    public String toPT() {
    	return length.toPT() + " minus " + shrink.toPT() + " plus " + stretch.toPT(); 
    }
}
