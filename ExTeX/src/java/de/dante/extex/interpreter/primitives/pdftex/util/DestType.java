/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.pdftex.util;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.type.node.RuleNode;

/**
 * TODO gene: missing JavaDoc.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class DestType {

    public static final DestType XYZ = new DestType();

    public static final DestType FIT = new DestType();

    public static final DestType FITH = new DestType();

    public static final DestType FITV = new DestType();

    public static final DestType FITB = new DestType();

    public static final DestType FITBV = new DestType();

    public static final DestType FITBH = new DestType();

    /**
     * TODO gene: missing JavaDoc
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     * @param name
     *
     * @return
     *
     * @throws InterpreterException in case of an error
     */
    public static DestType parseDestType(final Context context,
            final TokenSource source, final Typesetter typesetter,
            final String name) throws InterpreterException {

        if (source.getKeyword(context, "xyz")) {
            return XYZ;
        } else if (source.getKeyword(context, "fit")) {
            return FIT;
        } else if (source.getKeyword(context, "fith")) {
            return FITH;
        } else if (source.getKeyword(context, "fitv")) {
            return FITV;
        } else if (source.getKeyword(context, "fitb")) {
            return FITB;
        } else if (source.getKeyword(context, "fitbv")) {
            return FITBV;
        } else if (source.getKeyword(context, "fitbh")) {
            return FITBH;
        } else if (source.getKeyword(context, "fitr")) {

            Dimen width = null;
            Dimen height = null;
            Dimen depth = null;

            for (;;) {
                if (source.getKeyword(context, "width")) {
                    width = new Dimen(context, source, typesetter);
                } else if (source.getKeyword(context, "height")) {
                    height = new Dimen(context, source, typesetter);
                } else if (source.getKeyword(context, "depth")) {
                    depth = new Dimen(context, source, typesetter);
                } else {
                    break;
                }
            }

            return new FitrDestType(new RuleNode(width, height, depth, null));

        } else if (source.getKeyword(context, "zoom")) {
            long zoom = source.scanInteger(context, typesetter);
            return new ZoomDestType(zoom);
        }

        //TODO gene: error unimplemented
        throw new RuntimeException("unimplemented");
    }

    /**
     * Creates a new object.
     */
    protected DestType() {

        super();
    }

}
