/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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
package de.dante.extex.i18n;

import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This class provides means for using externalized strings and formats for
 * messages.
 * <p>
 * The Strings used are read from the resource <tt>extex.messages</tt> or one
 * of its localized variants.
 * </p>
 *
 * <p>
 * For ExTeX this properties file has many similarities to <tt>TEX.POOL</tt>
 * in TeX.
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.11 $
 */
public final class Messages {

    /**
     * The constant <tt>BUNDLE_NAME</tt> contains the name of the resource
     * bundle to use.
     */
    private static final String BUNDLE_NAME = "config.extexMessage";

    /**
     * The constant <tt>bundle</tt> contains the resource bundle for further
     * use.
     */
    private static ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME);

    /**
     * Creates a new object. This constructor is private to avoid that an
     * instance is created by accident. This class contains static methods
     * only. Thus it is never meant to be instanciated.
     */
    private Messages() {
    }

    /**
     * Getter for the value of a format string associated to a given key.
     *
     * @param key the key in the resource bundle to search for
     *
     * @return the resource string or <code>null</code>
     */
    public static String getFormat(final String key) {

        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return null;
        }
    }

    /**
     * Getter for the value of a format string associated to a given key.
     *
     * @param key the key in the resource bundle to search for
     * @return the resource string or the String
     * <tt>???</tt><i>key</i><tt>???</tt> if none is found
     */
    public static String format(final String key) {

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
    public static String format(final String fmt, final Object a) {

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
    public static String format(final String fmt, final Object a,
            final Object b) {

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
    public static String format(final String fmt, final Object a,
            final Object b, final Object c) {

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
    public static String format(final String fmt, final Object a,
            final Object b, final Object c, final Object d) {

        return MessageFormat.format(format(fmt), new Object[]{a, b, c, d});
    }

    /**
     * Initialize the messages with a given locale. This method should be
     * called early in the application and not called again. Otherwise the
     * change in the language might confuse the user.
     *
     * @param locale the preferred locale to use
     */
    public static void configure(final Locale locale) {

        bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
    }

    /**
     * Initialize the messages with a given bundle name and locale.
     * This method should be
     * called early in the application and not called again. Otherwise the
     * change in the language might confuse the user.
     *
     * @param bundleName the name of the resource bundle to use
     * @param locale the preferred locale to use
     */
    public static void configure(final String bundleName,
            final Locale locale) {

        bundle = ResourceBundle.getBundle(bundleName, locale);
    }

    /**
     * Initialize the messages with a given bundle name and the default
     * language.
     * This method should be
     * called early in the application and not called again. Otherwise the
     * change in the language might confuse the user.
     *
     * @param bundleName the name of the resource bundle to use
     */
    public static void configure(final String bundleName) {

        bundle = ResourceBundle.getBundle(bundleName);
    }

    /**
     * Get the value of a format string associated to a given key in the
     * resource bundle and print it to the given writer.
     *
     * @param writer the target output writer
     * @param fmt the key in the resource bundle to search for
     */
    public static void message(final PrintStream writer, final String fmt) {

        writer.println(Messages.format(fmt));
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
    public static void message(final PrintStream writer, final String fmt,
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
    public static void message(final PrintStream writer, final String fmt,
            final Object a, final Object b) {

        writer.println(MessageFormat.format(format(fmt), new Object[]{a, b}));
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
    public static void message(final PrintStream writer, final String fmt,
        final Object a, final Object b, final Object c) {

        writer.println(MessageFormat.format(format(fmt),
            new Object[]{a, b, c}));
    }

}
