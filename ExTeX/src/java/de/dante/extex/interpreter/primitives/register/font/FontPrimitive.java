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

package de.dante.extex.interpreter.primitives.register.font;

import de.dante.extex.font.FontFactory;
import de.dante.extex.font.FountKey;
import de.dante.extex.font.exception.FontException;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.type.AbstractAssignment;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.font.FontConvertible;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.scanner.type.CodeToken;
import de.dante.extex.scanner.type.ControlSequenceToken;
import de.dante.extex.scanner.type.SpaceToken;
import de.dante.extex.scanner.type.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationIOException;

/**
 * This class provides an implementation for the primitive <code>\font</code>.
 *
 * <doc name="font">
 * <h3>The Primitive <tt>\font</tt></h3>
 * <p>
 *  The primitive <tt>\font</tt>
 *  TODO missing documentation
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;font&rang;
 *      &rarr; <tt>\font</tt> ... </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \font\myfont=cmr12 at 15pt
 *  <pre class="TeXSample">
 *    \font\magnifiedfiverm=cmr5 scaled 2000
 *  <pre class="TeXSample">
 *    \font\second=cmr10 at 12truept  </pre>
 * </p>
 * </doc>
 *
 *
 * <h3>Possible Extension</h3>
 * <p>Example</p>
 * <pre>
 * \font\myfont=cmr12 at 15pt letterspaced 10sp plus 3sp minus 2sp
 * \font\myfont=cmr12 at 15pt letterspaced 10sp plus 3sp minus 2sp noligatures
 * \font\myfont=cmr12 at 15pt noligatures
 * \font\myfont=cmr12 at 15pt noligatures nokerning
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.24 $
 */
public class FontPrimitive extends AbstractAssignment
        implements
            FontConvertible {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public FontPrimitive(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void assign(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        CodeToken fontId = source.getControlSequence(context);
        source.getOptionalEquals(context);
        String fontname = scanFontName(context, source);
        Dimen fontSize = null;
        Count scale = null;

        if (source.getKeyword(context, "at")) {
            fontSize = new Dimen(context, source);
            if (fontSize.lt(Dimen.ZERO_PT)) {
                throw new HelpingException(getLocalizer(), "TTP.ImproperAt",
                        fontSize.toString());
            }

        } else if (source.getKeyword(context, "scaled")) {
            long s = source.scanInteger(context);
            if (s <= 0) {
                throw new HelpingException(getLocalizer(), "TTP.IllegalMag",
                        Long.toString(s), "32768"); //TODO gene: max ok?
            }
            scale = new Count(s);
        }

        Glue letterspaced = new Glue(0);
        boolean ligatures = true;
        boolean kerning = true;

        for (;;) {

            if (source.getKeyword(context, "letterspaced")) {
                letterspaced = new Glue(source, context);
            } else if (source.getKeyword(context, "noligatures")) {
                ligatures = false;
            } else if (source.getKeyword(context, "nokerning")) {
                kerning = false;
            } else {
                break;
            }
        }

        FontFactory factory = context.getFontFactory();
        Font font;
        try {
            font = factory.getInstance(new FountKey(fontname, fontSize, scale,
                    letterspaced, ligatures, kerning));
        } catch (FontException e) {
            throw new HelpingException(getLocalizer(), "TTP.TFMnotFound", //
                    context.esc(fontId), fontname);
        } catch (ConfigurationIOException e) {
            throw new HelpingException(getLocalizer(), "TTP.TFMnotFound", //
                    context.esc(fontId), fontname);
        } catch (ConfigurationException e) {
            throw new InterpreterException(e);
        }

        Code code = new FontCode(fontId.getName(), font);
        context.setCode(fontId, code, prefix.isGlobal());
    }

    /**
     * @see de.dante.extex.interpreter.type.font.FontConvertible#convertFont(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public Font convertFont(final Context context, final TokenSource source)
            throws InterpreterException {

        return context.getTypesettingContext().getFont();
    }

    /**
     * scan the filename until a <code>SpaceToken</code>.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     *
     * @return the file name as string
     *
     * @throws InterpreterException in case of an error
     */
    private String scanFontName(final Context context, final TokenSource source)
            throws InterpreterException {

        Token t = source.scanNonSpace(context);

        if (t == null) {
            throw new HelpingException(getLocalizer(), "TTP.EOFinDef",
                    printableControlSequence(context));
        }

        StringBuffer sb = new StringBuffer((char) t.getChar().getCodePoint());

        while (t != null && !(t instanceof SpaceToken)) {
            if (t instanceof ControlSequenceToken) {
                source.push(t);
                break;
            }
            sb.append((char) t.getChar().getCodePoint());
            t = source.getToken(context);
        }

        return sb.toString();
    }

}