/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This factory provides means to get a localizer.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public final class LocalizerFactory {

    /**
     * This inner class is the one and only implementation of a Localizer
     * delived by this factory.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.5 $
     */
    private static class BasicLocalizer implements Localizer {

        /**
         * The field <tt>bundle</tt> contains the resource bundle for this
         * instance or <code>null</code> if none has been loaded yet.
         */
        private ResourceBundle bundle = null;

        /**
         * The field <tt>bundleName</tt> contains the name of the resource
         * bundle to use.
         */
        private String bundleName;

        /**
         * Creates a new object.
         *
         * @param name name of the resource bundle
         */
        public BasicLocalizer(final String name) {

            super();
            bundleName = name;
        }

        /**
         * Getter for the value of a format string associated to a given key.
         *
         * @param key the key in the resource bundle to search for
         * @return the resource string or the String
         * <tt>???</tt><i>key</i><tt>???</tt> if none is found
         */
        public String format(final String key) {

            if (bundle == null) {
                bundle = ResourceBundle.getBundle(bundleName);

            }
            try {
                return bundle.getString(key);
            } catch (MissingResourceException e) {
                return "???" + key + "???";
            }
        }

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
        public String format(final String fmt, final Object a) {

            return MessageFormat.format(format(fmt), new Object[]{a});
        }

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
        public String format(final String fmt, final Object a, final Object b) {

            return MessageFormat.format(format(fmt), new Object[]{a, b});
        }

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
        public String format(final String fmt, final Object a, final Object b,
                final Object c) {

            return MessageFormat.format(format(fmt), new Object[]{a, b, c});
        }

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
        public String format(final String fmt, final Object a, final Object b,
                final Object c, final Object d) {

            return MessageFormat.format(format(fmt), new Object[]{a, b, c, d});
        }

        /**
         * Getter for the value of a format string associated to a given key.
         *
         * @param key the key in the resource bundle to search for
         *
         * @return the resource string or <code>null</code>
         */
        public String getFormat(final String key) {

            try {
                return bundle.getString(key);
            } catch (MissingResourceException e) {
                return null;
            }
        }

        /**
         * Get the value of a format string associated to a given key in the
         * resource bundle and print it to the given writer.
         *
         * @param writer the target output writer
         * @param fmt the key in the resource bundle to search for
         */
        public void message(final PrintStream writer, final String fmt) {

            writer.println(format(fmt));
        }

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
        public void message(final PrintStream writer, final String fmt,
                final Object a) {

            writer.println(MessageFormat.format(format(fmt), new Object[]{a}));
        }

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
        public void message(final PrintStream writer, final String fmt,
                final Object a, final Object b) {

            writer.println(MessageFormat
                    .format(format(fmt), new Object[]{a, b}));
        }

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
        public void message(final PrintStream writer, final String fmt,
                final Object a, final Object b, final Object c) {

            writer.println(MessageFormat.format(format(fmt), new Object[]{a, b,
                    c}));
        }

    }

    /**
     * The field <tt>cache</tt> contains the map of localizers already
     * constructed. The localizers are cache to minimize the overhead of
     * acquiring the same localizer several times.
     */
    private static final Map cache = new HashMap();

    /**
     * Return the localizer associated to a given name.
     *
     * @param name the name of the localizer
     *
     * @return the localizer for the given name
     */
    public static Localizer getLocalizer(final String name) {

        Localizer loc = (Localizer) cache.get(name);
        if (loc == null) {
            loc = new BasicLocalizer(name);
            cache.put(name, loc);
        }
        return loc;
    }

    /**
     * Creates a new object.
     * The constructor is private to avoid abuse.
     */
    private LocalizerFactory() {

    }

}