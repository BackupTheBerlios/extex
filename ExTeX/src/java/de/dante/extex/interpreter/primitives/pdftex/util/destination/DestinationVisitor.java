/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.pdftex.util.destination;

/**
 * This interface descries a visitor for destination types.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public interface DestinationVisitor {

    /**
     * This visit method is invoked on a fit.
     *
     * @param fit the type encountered
     *
     * @return some value
     */
    Object visitFit(DestType fit);

    /**
     * This visit method is invoked on a fitb.
     *
     * @param fit the type encountered
     *
     * @return some value
     */
    Object visitFitb(DestType fit);

    /**
     * This visit method is invoked on a fitbh.
     *
     * @param fit the type encountered
     *
     * @return some value
     */
    Object visitFitbh(DestType fit);

    /**
     * This visit method is invoked on a fitbv.
     *
     * @param fit the type encountered
     *
     * @return some value
     */
    Object visitFitbv(DestType fit);

    /**
     * This visit method is invoked on a fith.
     *
     * @param fit the type encountered
     *
     * @return some value
     */
    Object visitFith(DestType fit);

    /**
     * This visit method is invoked on a fitr.
     *
     * @param fit the type encountered
     *
     * @return some value
     */
    Object visitFitr(FitrDestType fit);

    /**
     * This visit method is invoked on a fitv.
     *
     * @param fit the type encountered
     *
     * @return some value
     */
    Object visitFitv(DestType fit);

    /**
     * This visit method is invoked on a xyz.
     *
     * @param fit the type encountered
     *
     * @return some value
     */
    Object visitXyz(DestType fit);

    /**
     * This visit method is invoked on a zoom.
     *
     * @param zoom the type encountered
     *
     * @return some value
     */
    Object visitZoom(ZoomDestType zoom);

}
