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

package de.dante.extex.interpreter.primitives.file;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Calendar;

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.AbstractCode;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\dump</code>.
 *
 * <doc name="dump">
 * <h3>The Primitive <tt>\dump</tt></h3>
 * <p>
 *  ...
 * </p>
 * </doc>
 *
 * Example
 * <pre>
 * \dump
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.11 $
 */
public class Dump extends AbstractCode {

    /**
     * The constant <tt>FORMAT_VERSION</tt> contains the version number of the
     * format file.
     */
    private static final String FORMAT_VERSION = "1.0";

    /**
     * The constant <tt>FORMAT_MAGIC_NUMBER</tt> contains the magic first line
     * written to the file.
     */
    private static final String FORMAT_MAGIC_NUMBER = "#!extex";

    /**
     * The constant <tt>FORMAT_EXTENSION</tt> contains the extension for the
     * format file.
     */
    private static final String FORMAT_EXTENSION = ".fmt";

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Dump(final String name) {

        super(name);
    }

    /**
     * @see "TeX -- The Program [1303,1304]"
     * @see de.dante.extex.interpreter.Code#execute(de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public boolean execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        if (!context.isGlobalGroup()) {
            throw new GeneralHelpingException("TTP.DumpInGroup");
        }

        Calendar calendar = Calendar.getInstance();

        //TODO @see "TeX -- The Program [1328]"

        Tokens tJobname = context.getToks("jobname");
        if (tJobname == null) {
            throw new GeneralHelpingException("invalid jobname"); //TODO i18n
        }
        String format = tJobname.toText() + FORMAT_EXTENSION;

        try {
            ObjectOutputStream os = new ObjectOutputStream(
                    new FileOutputStream(format));
            os.writeChars(FORMAT_MAGIC_NUMBER);
            os.writeObject(FORMAT_VERSION);
            os.writeObject(format + " " + //
                           calendar.get(Calendar.YEAR) + "."
                           + calendar.get(Calendar.MONTH) + "."
                           + calendar.get(Calendar.DAY_OF_MONTH));
            os.writeObject(context);
            //@see "TeX -- The Program [1329]"
            os.close();
        } catch (FileNotFoundException e) {
            throw new GeneralException(e);
        } catch (IOException e) {
            throw new GeneralException(e);
        }

        return true;
    }

}
