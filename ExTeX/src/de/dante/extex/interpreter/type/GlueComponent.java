/*
 * Copyright (C) 2003  Gerd Neugebauer
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

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.scanner.Token;
import de.dante.util.GeneralException;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class GlueComponent implements Serializable {
    /** This constant contains the internal representation for 1pt
     * @see "TeX -- The Program [101]"
     */
    public static final long ONE = 1 << 16;

    /** the integer representatation of the dimen register */
    protected long value = 0;

    /** ... */
    protected GlueOrder order = GlueOrder.NONE;

    /**
     * Creates a new object.
     */
    public GlueComponent() {
        super();
    }

    /**
     * Creates a new object.
     */
    public GlueComponent(long value) {
        super();
        this.value = value;
    }

    /**
     * ...
     *
     * 123 pt
     * -123 pt
     * 123.456 pt
     * 123.pt
     * .465 pt
     * -.456pt
     * +456pt
     *
     * @throws GeneralException in case of an error
     */
    public GlueComponent(TokenSource source, Context context)
                  throws GeneralException {
        this(source, context, false);
    }

    /**
     * Creates a new object.
     *
     * @param source the source for the tokens to be read
     * @param context the interpreter context
     * @param fixed if <code>true</code> then no glue order is allowed
     *
     * @throws GeneralException ...
     * @throws GeneralHelpingException ...
     */
    public GlueComponent(TokenSource source, Context context,
                         boolean fixed) throws GeneralException {
        super();
        set(source, context, fixed);
    }

    /**
     * Getter for the value in scaled points (sp).
     *
     * @return the value in internal units of scaled points (sp)
     */
    public long getValue() {
        return value;
    }

    /**
     * Setter for the value in terms of the internal representation.
     *
     * @param l the new value
     */
    public void set(long l) {
        value = l;
    }

    /**
     * Setter for the value.
     *
     * @param d the new value
     */
    public void set(Dimen d) {
        value = d.getValue();
        order = GlueOrder.NONE;
    }

    /**
     * ...
     *
     * @param source ...
     * @param context ...
     *
     * @throws GeneralException ...
     */
    public void set(TokenSource source, Context context)
             throws GeneralException {
        set(source, context, true);
    }

    /**
     * ...
     *
     * @param source ...
     * @param context ...
     * @param fixed ...
     *
     * @throws GeneralException ...
     * @throws GeneralHelpingException ...
     */
    public void set(TokenSource source, Context context, boolean fixed)
             throws GeneralException {
        value = source.scanFloat();

        Token t = source.scanNextNonSpace();

        if (t == null) {
            throw new GeneralHelpingException("xxx"); //TODO
        }

        source.push(t);

        long mag = 1000;

        if (source.scanKeyword("true")) { // cf. TTP[453], TTP[457]
            mag = context.getMagnification();
            source.push(source.scanNextNonSpace());
        }

        // cf. TTP[458]
        if (source.scanKeyword("sp")) {
            value = value / ONE;
        } else if (source.scanKeyword("pt")) {
        } else if (source.scanKeyword("mm")) {
            value = value * 7227 / 2540;
        } else if (source.scanKeyword("cm")) {
            value = value * 7227 / 254;
        } else if (source.scanKeyword("in")) {
            value = value * 7227 / 100;
        } else if (source.scanKeyword("pc")) {
            value = value * 12;
        } else if (source.scanKeyword("bp")) {
            value = value * 7227 / 7200;
        } else if (source.scanKeyword("dd")) {
            value = value * 1238 / 1157;
        } else if (source.scanKeyword("cc")) {
            value = value * 14856 / 1157;
        } else if (source.scanKeyword("ex")) {
            //TODO ex unimplemented
        } else if (source.scanKeyword("em")) {
            //TODO em unimplemented
        } else if (fixed && source.scanKeyword("fil")) {
            order = GlueOrder.FIL;
        } else if (fixed && source.scanKeyword("fill")) {
            order = GlueOrder.FILL;
        } else if (fixed && source.scanKeyword("filll")) {
            order = GlueOrder.FILLL;
        } else { // cf. TTP [459]
            throw new GeneralHelpingException("TTP.IllegalUnit");
        }

        if (mag != 1000) {
            value = value * mag / 1000;
        }
    }
}
