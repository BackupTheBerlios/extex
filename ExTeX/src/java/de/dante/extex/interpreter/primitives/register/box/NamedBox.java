/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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
package de.dante.extex.interpreter.primitives.register.box;

import java.io.Serializable;

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.AbstractCode;
import de.dante.extex.interpreter.Boxable;
import de.dante.extex.interpreter.Code;
import de.dante.extex.interpreter.ExpandableCode;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Box;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de"> Gerd Neugebauer </a>
 *
 * @version $Revision: 1.1 $
 */
public class NamedBox extends AbstractCode implements Code, Boxable,
        ExpandableCode, Serializable {

    /**
     * Creates a new object.
     *
     * @param name the name of the box
     */
    public NamedBox(final String name) {
        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.Code#execute(de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        String key = getKey(source);
        source.scanOptionalEquals();

        Box value = scanBox(context, source, typesetter);
        context.setBox(key, value, prefix.isGlobal());
        prefix.clear();
        //TODO doAfterAssignment(context, source);
    }

    /**
     * @see de.dante.extex.interpreter.Boxable#getBox(de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public Box getBox(final Context context, final TokenSource source,
            final Typesetter typesetter) throws GeneralException {

        String key = getKey(source);
        return context.getBox(key);
    }

    /**
     * @see de.dante.extex.interpreter.ExpandableCode#expand(de.dante.extex.interpreter.Flags, de.dante.extex.interpreter.context.Context, de.dante.extex.interpreter.TokenSource, de.dante.extex.typesetter.Typesetter)
     */
    public void expand(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        String key = getKey(source);

        // TODO Auto-generated method stub

    }

    /**
     * Return the key (the name of the primitive) for the register.
     *
     * @param source ...
     *
     * @return ...
     */
    protected String getKey(final TokenSource source) throws GeneralException {

        return getName();
    }

    /**
     * ...
     *
     * @param context the processor context
     * @param source the source for new tokens
     *
     * @return ...
     *
     * @throws GeneralException in case of an error
     */
    private Box scanBox(final Context context, final TokenSource source,
            final Typesetter typesetter) throws GeneralException {

        Token t = source.getToken();
        if (t == null) {
            throw new GeneralHelpingException("UnexpectedEOF",
                    printableControlSequence(context));
        } else if (t instanceof Boxable) {
            return ((Boxable) t).getBox(context, source, typesetter);
        } else {
            throw new GeneralException("expected box missing"); // TODO i18n
        }
    }

}
