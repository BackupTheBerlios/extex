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

package de.dante.util.framework.i18n;

import java.io.PrintStream;
import java.io.Serializable;

/**
 * The localizer is a convenience class which joins the features of a resource
 * bundle with the features of a message formatter..
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public interface Localizer extends Serializable {

    /**
     * Getter for the value of a format string associated to a given key.
     *
     * @param key the key in the resource bundle to search for
     * @return the resource string or the String
     * <tt>???</tt><i>key</i><tt>???</tt> if none is found
     */
    String format(String key);

    /**
     * Apply the given argument to the format string stored in the resource
     * bundle under the given key. The argument object's value of toString()
     * replaces the substring <tt>'{0}'</tt> in the format.
     *
     * @param fmt the key in the resource bundle to search for
     * @param a the Object used for the substring <tt>{0}</tt>
     *
     * @return the expanded format string
     */
    String format(String fmt, Object a);

    /**
     * Apply the given argument to the format string stored in the resource
     * bundle under the given key. The argument object's value of toString()
     * replaces the substring <tt>'{0}'</tt> and <tt>'{1}'</tt> in the
     * format.
     *
     * @param fmt the key in the resource bundle to search for
     * @param a the Object used for the substring <tt>{0}</tt>
     * @param b the Object used for the substring <tt>{1}</tt>
     *
     * @return the expanded format string
     */
    String format(String fmt, Object a, Object b);

    /**
     * Apply the given argument to the format string stored in the resource
     * bundle under the given key. The argument object's value of toString()
     * replaces the substring <tt>'{0}'</tt>,<tt>'{1}'</tt>, and <tt>'{2}'</tt>
     * in the format.
     *
     * @param fmt the key in the resource bundle to search for
     * @param a the Object used for the substring <tt>{0}</tt>
     * @param b the Object used for the substring <tt>{1}</tt>
     * @param c the Object used for the substring <tt>{2}</tt>
     *
     * @return the expanded format string
     */
    String format(String fmt, Object a, Object b, Object c);

    /**
     * Apply the given argument to the format string stored in the resource
     * bundle under the given key. The argument object's value of toString()
     * replaces the substring <tt>'{0}'</tt>,<tt>'{1}'</tt>,<tt>'{2}'</tt>,
     * and <tt>'{3}'</tt> in the format.
     *
     * @param fmt the key in the resource bundle to search for
     * @param a the Object used for the substring <tt>{0}</tt>
     * @param b the Object used for the substring <tt>{1}</tt>
     * @param c the Object used for the substring <tt>{2}</tt>
     * @param d the Object used for the substring <tt>{3}</tt>
     *
     * @return the expanded format string
     */
    String format(String fmt, Object a, Object b, Object c, Object d);

    /**
     * Getter for the value of a format string associated to a given key.
     *
     * @param key the key in the resource bundle to search for
     *
     * @return the resource string or <code>null</code>
     */
    String getFormat(String key);

    /**
     * Get the value of a format string associated to a given key in the
     * resource bundle and print it to the given writer.
     *
     * @param writer the target output writer
     * @param fmt the key in the resource bundle to search for
     */
    void message(PrintStream writer, String fmt);

    /**
     * Apply the given argument to the format string stored in the resource
     * bundle under the given key and print the result to a writer. The
     * argument object's value of toString() replaces the substring
     * <tt>'{0}'</tt> in the format.
     *
     * @param writer the target output writer
     * @param fmt the key in the resource bundle to search for
     * @param a the Object used for the substring <tt>{0}</tt>
     */
    void message(PrintStream writer, String fmt, Object a);

    /**
     * Apply the given argument to the format string stored in the resource
     * bundle under the given key and print the result to a writer. The
     * argument object's value of toString() replaces the substring
     * <tt>'{0}'</tt> and <tt>'{1}'</tt> in the format.
     *
     * @param writer the target output writer
     * @param fmt the key in the resource bundle to search for
     * @param a the Object used for the substring <tt>{0}</tt>
     * @param b the Object used for the substring <tt>{1}</tt>
     */
    void message(PrintStream writer, String fmt, Object a, Object b);

    /**
     * Apply the given argument to the format string stored in the resource
     * bundle under the given key and print the result to a writer. The
     * argument object's value of toString() replaces the substring
     * <tt>'{0}'</tt>, <tt>'{1}'</tt>, and <tt>'{2}'</tt> in the format.
     *
     * @param writer the target output writer
     * @param fmt the key in the resource bundle to search for
     * @param a the Object used for the substring <tt>{0}</tt>
     * @param b the Object used for the substring <tt>{1}</tt>
     * @param c the Object used for the substring <tt>{2}</tt>
     */
    void message(PrintStream writer, String fmt, Object a, Object b, Object c);

}