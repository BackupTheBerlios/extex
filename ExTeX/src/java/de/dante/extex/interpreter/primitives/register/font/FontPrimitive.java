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

package de.dante.extex.interpreter.primitives.register.font;

import de.dante.extex.font.FontFactory;
import de.dante.extex.i18n.HelpingException;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.AbstractAssignment;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.font.FontConvertible;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.scanner.SpaceToken;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;
import de.dante.util.configuration.ConfigurationException;

/**
 * This class provides an implementation for the primitive <code>\font</code>.
 * <p>Example:</p>
 * <pre>
 * \font\myfont=cmr12 at 15pt
 * \font\magnifiedfiverm=cmr5 scaled 2000
 * \font\second=cmr10 at 12truept
 * </pre>
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
 * @version $Revision: 1.9 $
 */
public class FontPrimitive extends AbstractAssignment
        implements
            FontConvertible {

    /**
     * The field <tt>DEFAULT_SCALE_FACTOR</tt> contains the default scale
     * factor.
     */
    private static final int DEFAULT_SCALE_FACTOR = 1000;

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
            throws GeneralException {

        Token t = source.getControlSequence();
        source.getOptionalEquals();
        String fontname = scanFontName(source);
        int size = getFontSize(fontname);
        Dimen fontsize = null;

        // optional parameters 'at' and 'scaled'
        // if 'at' not used, the fontname must have a size (e.g. cmr10)
        if (source.getKeyword("at", true)) {
            // \font\myfont=cmr12 at 15pt
            // \font\second=cmr10 at 12truept
            source.skipSpace();
            fontsize = new Dimen(context, source);
        } else if (source.getKeyword("scaled", true)) {
            // \font\magnifiedfiverm=cmr5 scaled 2000
            source.skipSpace();
            long scale = source.scanInteger();
            fontsize = new Dimen(Dimen.ONE * size * scale
                    / DEFAULT_SCALE_FACTOR);
        } else {
            // use size from the fontname
            fontsize = new Dimen(Dimen.ONE * size);
        }

        // fontsize < 0
        if (fontsize.lt(Dimen.ZERO_PT)) {
            throw new HelpingException(getLocalizer(), "TTP.ImproperAt",
                    fontsize.toString());
        }

        Glue letterspaced = new Glue(0);
        boolean ligatures = true;
        boolean kerning = true;

        for (boolean onceMore = true; onceMore;) {

            if (source.getKeyword("letterspaced", true)) {
                // \font\myfont=cmr12 at 15pt letterspaced 10sp plus 3sp minus 2sp
                source.skipSpace();
                letterspaced = new Glue(source, context);
            } else if (source.getKeyword("noligatures", true)) {
                // \font\myfont=cmr12 at 15pt noligatures
                ligatures = false;
            } else if (source.getKeyword("nokerning", true)) {
                // \font\myfont=cmr12 at 15pt nokerning
                kerning = false;
            } else {
                onceMore = false;
            }
        }

        FontFactory ff = context.getFontFactory();
        Font font;
        try {
            font = ff.getInstance(fontname, fontsize, letterspaced, ligatures,
                    kerning);
        } catch (ConfigurationException e) {
            throw new GeneralException(e);
        }

        Code code = new FontCode(t.getValue(), font);
        context.setCode(t, code, prefix.isGlobal());
    }

    /**
     * @see de.dante.extex.interpreter.type.font.FontConvertible#convertFont(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public Font convertFont(final Context context, final TokenSource source)
            throws GeneralException {

        return context.getTypesettingContext().getFont();
    }

    /**
     * Return the size of a font with a fontname. If no number in the
     * filename exits, -1 is returned.
     *
     * @param filename the filename (e.g. <tt>cmr12</tt>)
     * @return the fontsize or -1, if no digits are found
     */
    private int getFontSize(final String filename) {

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < filename.length(); i++) {
            UnicodeChar uc = new UnicodeChar(filename, i);
            if (uc.isDigit()) {
                sb.append(uc.toString());
            }
        }
        int rt = -1;
        try {
            rt = Integer.parseInt(sb.toString());
        } catch (NumberFormatException e) {
            // do nothing, use default
            rt = -1;
        }
        return rt;
    }

    /**
     * scan the filename until a <code>SpaceToken</code>.
     *
     * @param source the source for new tokens
     *
     * @return the file name as string
     *
     * @throws GeneralException in case of an error
     */
    private String scanFontName(final TokenSource source)
            throws GeneralException {

        Token t = source.scanNonSpace();

        if (t == null) {
            throw new HelpingException(getLocalizer(), "TTP.EOFinDef", "font");
        }

        StringBuffer sb = new StringBuffer(t.getValue());

        for (t = source.scanToken(); t != null && !(t instanceof SpaceToken); t = source
                .scanToken()) {
            sb.append(t.getValue());
        }

        return sb.toString();
    }

}