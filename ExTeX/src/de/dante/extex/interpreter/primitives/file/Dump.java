/*
 * Copyright (C) 2003  Gerd Neugebauer
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
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\dump</code>.
 *
 * Example
 * <pre>
 * \dump
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class Dump extends AbstractCode {
    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Dump(String name) {
        super(name);
    }

    /**
     *
     * @see "TeX -- The Program [1303,1304]"
     * @see de.dante.extex.interpreter.Code#expand(de.dante.extex.interpreter.Flags, de.dante.extex.interpreter.context.Context, de.dante.extex.interpreter.TokenSource, de.dante.extex.typesetter.Typesetter)
     */
    public void expand(Flags prefix, Context context, TokenSource source, Typesetter typesetter) throws GeneralException {
        if (!context.isGlobalGroup()) {
            throw new GeneralHelpingException("TTP.DumpInGroup");
        }

        Calendar calendar = Calendar.getInstance();


        //TODO @see "TeX -- The Program [1328]"

        try {
            String format         = "xxx"; //TODO: replace by jobname
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(format +
                                                                                "fmt"));
            os.writeChars("#!extex");
            os.writeObject("1.0");
            os.writeObject(format + " " + //
                           calendar.get(Calendar.YEAR) + "." +
                           calendar.get(Calendar.MONTH) + "." +
                           calendar.get(Calendar.DAY_OF_MONTH));
            os.writeObject(context);
            //@see "TeX -- The Program [1329]"
            os.close();
        } catch (FileNotFoundException e) {
            throw new GeneralException(e);
        } catch (IOException e) {
            throw new GeneralException(e);
        }

        prefix.clear();
    }
}
