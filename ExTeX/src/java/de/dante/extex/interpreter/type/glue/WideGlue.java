/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.type.glue;

import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.dimen.ImmutableDimen;

/**
 * TODO gene: missing JavaDoc.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class WideGlue {

    /**
     * The field <tt>SIZE</tt> contains the ...
     */
    private static final int SIZE = 5;

    /**
     * The field <tt>length</tt> contains the ...
     */
    private Dimen length = new Dimen();

    /**
     * The field <tt>shrink</tt> contains the ...
     */
    private long[] shrink = new long[SIZE];

    /**
     * The field <tt>stretch</tt> contains the ...
     */
    private long[] stretch = new long[SIZE];

    /**
     * Creates a new object.
     *
     */
    public WideGlue() {

        super();
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @param glue the glue to add
     */
    public void add(final FixedDimen glue) {

        length.add(glue.getValue());
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @param glue the glue to add
     */
    public void add(final FixedGlue glue) {

        length.add(glue.getLength());
        FixedGlueComponent s = glue.getStretch();
        stretch[s.getOrder()] += s.getValue();

        s = glue.getShrink();
        shrink[s.getOrder()] += s.getValue();
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @param a
     * @return
     */
    private GlueComponent getGC(final long[] a) {

        for (int i = SIZE - 1; i >= 0; i--) {
            if (a[i] != 0) {
                return new GlueComponent(a[i], i);
            }
        }

        return GlueComponent.ZERO;
    }

    /**
     * Getter for the natural length.
     *
     * @return the natural length
     */
    public Dimen getLength() {

        return length;
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @return
     */
    public GlueComponent getShrink() {

        for (int i = SIZE - 1; i >= 0; i--) {
            if (shrink[i] != 0) {
                return new GlueComponent(shrink[i], i);
            }
        }

        return GlueComponent.ZERO;
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @return
     */
    public GlueComponent getStretch() {

        for (int i = SIZE - 1; i >= 0; i--) {
            if (stretch[i] != 0) {
                return new GlueComponent(stretch[i], i);
            }
        }

        return GlueComponent.ZERO;
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @param length 
     */
    public void set(ImmutableDimen length) {

        this.length.set(length);

        for (int i = SIZE - 1; i >= 0; i--) {
            stretch[i] = 0;
            shrink[i] = 0;
        }
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @return the Glue representation of this instance
     */
    public Glue toGlue() {

        GlueComponent st = getGC(stretch);
        GlueComponent sh = getGC(shrink);

        return new Glue(length, st, sh);
    }
}
