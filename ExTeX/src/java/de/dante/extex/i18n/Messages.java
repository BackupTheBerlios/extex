/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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
package de.dante.extex.i18n;

import java.text.MessageFormat;
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
 * <p>
 * Currently this singleton is implemented as a set of static variables.
 * This has to change!!
 * </p>
 *
 * @deprecated use Localizer instead
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.19 $
 */
public final class Messages {
    //TODO gene: This class is a mess. Integrate the localized messages into the components.

    /**
     * The constant <tt>BUNDLE_NAME</tt> contains the name of the resource
     * bundle to use.
     */
    private static final String BUNDLE_NAME = Messages.class.getName();

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
     * Apply the given argument to the format string stored in the resource
     * bundle under the given key. The argument object's value of toString()
     * replaces the substring <tt>'{0}'</tt> in the format.
     *
     * @param fmt the key in the resource bundle to search for
     * @param a the Object used for the substring <tt>{0}</tt>
     *
     * @return the expanded format string
     *
     * @deprecated use the localizer instead
     */
    public static String format(final String fmt, final Object a) {

        String format;
        try {
            format = bundle.getString(fmt);
        } catch (MissingResourceException e) {
            format = "???" + fmt + "???";
        }
        return MessageFormat.format(format, new Object[]{a});
    }

}
