/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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
package de.dante.extex.interpreter.primitives;

import de.dante.extex.interpreter.type.AbstractCode;

/**
 * This class provides an implementation for the primitive <code>\relax</code>.
 *
 * <doc name="relax">
 * <h3>The Primitive <tt>\relax</tt></h3>
 * <p>
 *  This primitive simply does nothing. It acts as a no-op for the
 *  <logo>TeX</logo> macro language. <tt>\relax</tt> is not even expandable.
 *  in certain circumstances it might be treated as if it where expandable and
 *  the expansion is empty.
 * </p>
 * <p>
 *  <tt>\relax</tt> sometimes acts as terminating token. For instance when a
 *  number is parsed <tt>\relax</tt> terminates the parsing even if the
 *  following token is a digit.
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;relax&rang;
 *      &rarr; <tt>\relax</tt>  </pre>
 * </p>
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \relax  </pre>
 *  <pre class="TeXSample">
 *    \the\count123\relax456  </pre>
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.14 $
 */
public class Relax extends AbstractCode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new object.
     *
     * @param name the name for tracing and debugging
     */
    public Relax(final String name) {

        super(name);
    }

}
