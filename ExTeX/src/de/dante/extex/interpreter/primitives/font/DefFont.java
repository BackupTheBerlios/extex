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

package de.dante.extex.interpreter.primitives.font;

import de.dante.extex.font.FontFactory;
import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.AbstractCode;
import de.dante.extex.interpreter.Code;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.interpreter.type.Font;
import de.dante.extex.scanner.SpaceToken;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;
import de.dante.util.configuration.ConfigurationException;

/**
 * This class provides an implementation for the primitive <code>\font</code>.
 * <p>
 * Example:
 * <pre>
 * \font\myfont=cmr12 at 15pt
 * \font\magnifiedfiverm=cmr5 scaled 2000
 * \font\second=cmr10 at 12truept
 * </pre>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.6 $
 */
public class DefFont extends AbstractCode {

    /**
     * Default-Font-size
     */
    private static final int DEFAULTSIZE = 10;

    /**
     * Default-Scale-factor
     */
    private static final int DEFAULTSCALEFACTOR = 1000;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public DefFont(final String name) {

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

        Token tok = source.getNonSpace();
        source.scanOptionalEquals();
        String filename = scanFileName(source);
        int size = getFontSize(filename);
        if (size < 0) {
            size = DEFAULTSIZE;
        }
        Dimen fontsize = new Dimen(Dimen.ONE * size);

        // optional parameters
        try {
            if (source.scanKeyword("at", true)) {
                // \font\myfont=cmr12 at 15pt
                // \font\second=cmr10 at 12truept
                source.skipSpace();
                fontsize = new Dimen(context, source);
            } else if (source.scanKeyword("scaled", true)) {
                // \font\magnifiedfiverm=cmr5 scaled 2000
                source.skipSpace();
                long scale = source.scanInteger();
                fontsize = new Dimen(Dimen.ONE * size * scale
                        / DEFAULTSCALEFACTOR);
            }
        } catch (Exception e) {
            // do nothing, use default
            fontsize = new Dimen(Dimen.ONE * size);
        }

        FontFactory ff = context.getFontFactory();
        Font font;
        try {
            font = ff.getInstance(filename, fontsize);
        } catch (ConfigurationException e) {
            throw new GeneralException(e);
        }

        // create new primitive
        Code code = context.getMacro(tok.getValue());
        if (code != null) {
            throw new GeneralHelpingException("FONT.fontprimitiveexists", code
                    .getName());
        }
        code = new FontCode(tok.getValue(), font);
        context.setMacro(tok.getValue(), code);
        prefix.clear();
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
     * @return the file name as string
     * @throws GeneralException in case of an error
     */
    private String scanFileName(final TokenSource source)
            throws GeneralException {

        Token t = source.scanNonSpace();

        if (t == null) {
            throw new GeneralHelpingException("TTP.EOFinDef", "font");
        }

        StringBuffer sb = new StringBuffer(t.getValue());

        for (t = source.scanToken(); t != null && !(t instanceof SpaceToken); t = source
                .scanToken()) {
            sb.append(t.getValue());
        }

        return sb.toString();
    }
}
