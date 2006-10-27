/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.type.math;

import java.io.Serializable;
import java.util.logging.Logger;

import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.noad.MathGlyph;
import de.dante.extex.typesetter.type.noad.Noad;
import de.dante.extex.typesetter.type.noad.NoadList;
import de.dante.extex.typesetter.type.noad.util.MathContext;
import de.dante.extex.typesetter.type.noad.util.MathSpacing;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This class provides a container for a delimiter consisting of a class, a
 * large, and a small math glyph.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class MathDelimiter implements Noad, Serializable {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 20060420L;

    /**
     * The field <tt>largeChar</tt> contains the code of the large character.
     */
    private MathGlyph largeChar;

    /**
     * The field <tt>mathClass</tt> contains the class of this delimiter.
     */
    private MathClass mathClass;

    /**
     * The field <tt>smallChar</tt> contains the code of the small character.
     */
    private MathGlyph smallChar;

    /**
     * Creates a new object.
     *
     * @param mathClass the class
     * @param smallChar the small character
     * @param largeChar the large character
     */
    public MathDelimiter(final MathClass mathClass, final MathGlyph smallChar,
            final MathGlyph largeChar) {

        super();
        this.mathClass = mathClass;
        this.smallChar = smallChar;
        this.largeChar = largeChar;
    }

    /**
     * Getter for largeChar.
     *
     * @return the largeChar.
     */
    public MathGlyph getLargeChar() {

        return this.largeChar;
    }

    /**
     * Getter for mathClass.
     *
     * @return the mathClass.
     */
    public MathClass getMathClass() {

        return this.mathClass;
    }

    /**
     * Getter for smallChar.
     *
     * @return the smallChar.
     */
    public MathGlyph getSmallChar() {

        return this.smallChar;
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#getSpacingClass()
     */
    public MathSpacing getSpacingClass() {

        return MathSpacing.UNDEF; // gene: correct?
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#getSubscript()
     */
    public Noad getSubscript() {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#getSuperscript()
     */
    public Noad getSuperscript() {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#setSubscript(
     *      de.dante.extex.typesetter.type.noad.Noad)
     */
    public void setSubscript(final Noad subscript) {

        throw new UnsupportedOperationException("setSubscript");
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#setSuperscript(
     *      de.dante.extex.typesetter.type.noad.Noad)
     */
    public void setSuperscript(final Noad superscript) {

        throw new UnsupportedOperationException("setSuperscript");
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        StringBuffer sb = new StringBuffer();
        toString(sb);
        return sb.toString();
    }

    /**
     * Append the printable representation of the current instance to the
     * string buffer.
     *
     * @param sb the target string buffer
     *
     * @see "TTP [691]"
     */
    public void toString(final StringBuffer sb) {

        //sb.append('\"');
        mathClass.toString(sb);
        smallChar.toString(sb);
        largeChar.toString(sb);
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#toString(
     *      java.lang.StringBuffer, int)
     */
    public void toString(final StringBuffer sb, final int depth) {

        toString(sb);
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#typeset(
     *      de.dante.extex.typesetter.type.noad.Noad,
     *      de.dante.extex.typesetter.type.noad.NoadList,
     *      int,
     *      de.dante.extex.typesetter.type.NodeList,
     *      de.dante.extex.typesetter.type.noad.util.MathContext,
     *      java.util.logging.Logger)
     */
    public void typeset(final Noad previousNoad, final NoadList noads,
            final int index, final NodeList list,
            final MathContext mathContext, final Logger logger)
            throws TypesetterException,
                ConfigurationException {

        typeset(list, mathContext, null, null);
    }

    /**
     * Translate a MathDelimter into a NodeList.
     *
     * @param list the list to add the nodes to. This list contains the Nodes
     *  previously typeset. Thus it can be used to look back
     * @param mathContext the context to consider
     * @param height the target height. If <code>null</code> then the natural
     *  height is used
     * @param depth the target depth. If <code>null</code> then the natural
     *  depth is used
     *
     * @throws TypesetterException in case of a problem
     * @throws ConfigurationException in case of a configuration problem
     */
    public void typeset(final NodeList list, final MathContext mathContext,
            final FixedDimen height, final FixedDimen depth)
            throws TypesetterException,
                ConfigurationException {

        if (mathClass == null && smallChar == null && largeChar == null) {
            return;
        }

        //TODO gene: typeset() unimplemented
        throw new RuntimeException("unimplemented MathDelimiter");
    }

}