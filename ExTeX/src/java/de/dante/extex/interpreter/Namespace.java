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
package de.dante.extex.interpreter;


/**
 * This class contains the definitions for name spaces. It is not a class which
 * is supposed to be instantiated.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
 */
public final class Namespace {

    /**
     * The constant <tt>DEFAULT_NAMESPACE</tt> contains the name of the default
     * name space.
     */
    public static final String DEFAULT_NAMESPACE = "";

    /**
     * The constant <tt>SUPPORT_NAMESPACE_BOX</tt> contains the flag
     * indicating whether or not the name space should be used for box
     * registers.
     */
    public static final boolean SUPPORT_NAMESPACE_BOX = false;

    /**
     * The constant <tt>SUPPORT_NAMESPACE_COUNT</tt> contains the flag
     * indicating whether or not the name space should be used for count
     * registers.
     */
    public static final boolean SUPPORT_NAMESPACE_COUNT = false;

    /**
     * The constant <tt>SUPPORT_NAMESPACE_DEF</tt> contains the indicator that
     * name spaces should be honored. In this case the look-up for Code is
     * also performed in the default name space if not found in the current one.
     */
    public static final boolean SUPPORT_NAMESPACE_DEF = true;

    /**
     * The constant <tt>SUPPORT_NAMESPACE_DIMEN</tt> contains the flag
     * indicating whether or not the name space should be used for dimen
     * registers.
     */
    public static final boolean SUPPORT_NAMESPACE_DIMEN = false;

    /**
     * The constant <tt>SUPPORT_NAMESPACE_FONT</tt> contains the flag
     * indicating whether or not the name space should be used for toks
     * registers.
     */
    public static final boolean SUPPORT_NAMESPACE_FONT = false;

    /**
     * The constant <tt>SUPPORT_NAMESPACE_MUSKIP</tt> contains the flag
     * indicating whether or not the name space should be used for toks
     * registers.
     */
    public static final boolean SUPPORT_NAMESPACE_MUSKIP = false;

    /**
     * The constant <tt>SUPPORT_NAMESPACE_SKIP</tt> contains the flag
     * indicating whether or not the name space should be used for toks
     * registers.
     */
    public static final boolean SUPPORT_NAMESPACE_SKIP = false;

    /**
     * The constant <tt>SUPPORT_NAMESPACE_TOKS</tt> contains the flag
     * indicating whether or not the name space should be used for toks
     * registers.
     */
    public static final boolean SUPPORT_NAMESPACE_TOKS = false;

}
