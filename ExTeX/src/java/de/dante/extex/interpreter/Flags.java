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

package de.dante.extex.interpreter;

/**
 * This interface describes a set of named flags.
 *
 * <p>
 * The flags correspond largely to the prefix macros like <tt>\global</tt>
 * The following flags are known:
 * </p>
 * <table>
 *  <tr>
 *   <th>Flag</th>
 *   <th>Macro</th>
 *   <th>Description</th>
 *  </tr>
 *  <tr>
 *   <td><b>expanded</b></td>
 *   <td></td>
 *   <td>The expanded flag is used internally to signal that expansion should
 *    be used for a definition. This is applied in <tt>\edef</tt> and
 *    <tt>\xdef</tt>.
 *   </td>
 *  </tr>
 *  <tr>
 *   <td><b>global</b></td>
 *   <td><tt>\global</tt></td>
 *   <td>The global flag controls the scope of assignments like <tt>\def</tt>s.
 *   </td>
 *  </tr>
 *  <tr>
 *   <td><b>immediate</b></td>
 *   <td><tt>\immediate</tt></td>
 *   <td>The immediate flag controls the delay of writing operations. This is
 *    used in <tt>\write</tt> and friends.
 *   </td>
 *  </tr>
 *  <tr>
 *   <td><b>long</b></td>
 *   <td><tt>\long</tt></td>
 *   <td>The long flag controls the matching of macro parameters.
 *   </td>
 *  </tr>
 *  <tr>
 *   <td><b>outer</b></td>
 *   <td><tt>\outer</tt></td>
 *   <td>The outer flag controls that the macro can not be used at places where
 *    tokens are absorbed at high speed.
 *   </td>
 *  </tr>
 *  <tr>
 *   <td><b>protected</b></td>
 *   <td><tt>\protected</tt></td>
 *   <td>The protected flag controls the expansion of the macro when the
 *    arguments for <tt>\edef</tt> and friends i expanded.
 *   </td>
 *  </tr>
 * </table>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.20 $
 */
public interface Flags {

    /**
     * The constant <tt>NONE</tt> contains an instance where no flags are set.
     * Beware of changing this instance!
     */
    Flags NONE = new FlagsImpl();

    /**
     * This method clears all flags.
     */
    void clear();

    /**
     * Setter for the expanded flag.
     */
    void clearExpanded();

    /**
     * Setter for the global flag.
     */
    void clearGlobal();

    /**
     * Setter for the immediate flag.
     */
    void clearImmediate();

    /**
     * Setter for the long flag.
     */
    void clearLong();

    /**
     * Setter for the outer flag.
     */
    void clearOuter();

    /**
     * Setter for the protected flag.
     */
    void clearProtected();

    /**
     * Clone an instance.
     *
     * @return a copy of the instance
     */
    Flags copy();

    /**
     * Test if all flags are cleared.
     *
     * @return <code>true</code> iff not all flags are cleared
     */
    boolean isDirty();

    /**
     * Getter for the expanded flag.
     *
     * @return the current value of the expanded flag
     */
    boolean isExpanded();

    /**
     * Getter for the global flag.
     *
     * @return the current value of the global flag
     */
    boolean isGlobal();

    /**
     * Getter for the immediate flag.
     *
     * @return the current value of the immediate flag
     */
    boolean isImmediate();

    /**
     * Getter for the long flag.
     *
     * @return the current value of the long flag
     */
    boolean isLong();

    /**
     * Getter for the outer flag.
     *
     * @return the current value of the outer flag
     */
    boolean isOuter();

    /**
     * Getter for the protected flag.
     *
     * @return the current value of the protected flag
     */
    boolean isProtected();

    /**
     * Copy the flag settings from a given instance int this instance.
     *
     * @param flags the flags to copy
     */
    void set(Flags flags);

    /**
     * Setter for the expanded flag.
     */
    void setExpanded();

    /**
     * Setter for the global flag.
     */
    void setGlobal();

    /**
     * Setter for the global flag.
     *
     * @param value the new value for the global flag
     */
    void setGlobal(final boolean value);

    /**
     * Setter for the immediate flag.
     */
    void setImmediate();

    /**
     * Setter for the long flag.
     */
    void setLong();

    /**
     * Setter for the outer flag.
     */
    void setOuter();

    /**
     * Setter for the protected flag.
     */
    void setProtected();

}
