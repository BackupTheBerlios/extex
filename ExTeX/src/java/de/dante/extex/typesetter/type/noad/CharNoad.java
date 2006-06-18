/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.typesetter.type.noad;

import java.util.logging.Logger;

import de.dante.extex.font.type.other.NullFont;
import de.dante.extex.interpreter.context.Color;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.context.TypesettingContextFactory;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.primitives.register.font.NumberedFont;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.font.FontUtil;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.noad.util.MathContext;
import de.dante.extex.typesetter.type.noad.util.MathSpacing;
import de.dante.extex.typesetter.type.node.CharNode;
import de.dante.extex.typesetter.type.node.ImplicitKernNode;
import de.dante.util.UnicodeChar;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This class provides a container for a mathematical character.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.24 $
 */
public class CharNoad extends AbstractNoad {

    /**
     * The field <tt>color</tt> contains the color.
     */
    private Color color;

    /**
     * The field <tt>glyph</tt> contains the character representation.
     */
    private MathGlyph glyph;

    /**
     * Creates a new object.
     *
     * @param character the character representation
     * @param tc the typesetting context for the color
     */
    protected CharNoad(final MathGlyph character, final TypesettingContext tc) {

        super();
        this.glyph = character;
        this.color = tc.getColor();
    }

    /**
     * Getter for the character.
     *
     * @return the character.
     */
    public MathGlyph getChar() {

        return this.glyph;
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#toString(
     *      java.lang.StringBuffer)
     */
    public void toString(final StringBuffer sb) {

        glyph.toString(sb);
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#typeset(
     *      de.dante.extex.typesetter.type.noad.Noad,
     *      de.dante.extex.typesetter.type.noad.NoadList,
     *      int,
     *      de.dante.extex.typesetter.type.NodeList,
     *      de.dante.extex.typesetter.type.noad.util.MathContext,
     *      java.util.logging.Logger)
     */
    public void typeset(final Noad previousNoad, final NoadList noads,
            final int index, final NodeList nodes,
            final MathContext mathContext, final Logger logger)
            throws TypesetterException,
                ConfigurationException {

        TypesetterOptions context = mathContext.getOptions();
        StyleNoad style = mathContext.getStyle();
        UnicodeChar c = glyph.getCharacter();
        Font font = context.getFont(NumberedFont.key(context, //
                style.getFontName(), Integer.toString(glyph.getFamily())));
        if (font instanceof NullFont) {
            throw new TypesetterException(new HelpingException(getLocalizer(),
                    "TTP.UndefinedFamily", style.getStyleName(), Integer
                            .toString(glyph.getFamily()), c.toString()));
        }

        if (font.getGlyph(c) == null) {
            FontUtil.charWarning(logger, context, font, c);
            return;
        }

        int size = nodes.size();
        if (size > 0) {
            Node n = nodes.get(size - 1);
            if (n instanceof CharNode) {
                CharNode cn = ((CharNode) n);
                if (cn.getTypesettingContext().getFont().equals(font)) {
                    Dimen kerning = font.getGlyph(cn.getCharacter())
                            .getKerning(c);
                    if (!kerning.isZero()) {
                        nodes.add(new ImplicitKernNode(kerning, true));
                    }
                }
            }
        }

        TypesettingContextFactory tcFactory = context
                .getTypesettingContextFactory();
        TypesettingContext tc = tcFactory.newInstance();
        tc = tcFactory.newInstance(tc, font);
        tc = tcFactory.newInstance(tc, color);
        CharNode charNode = new CharNode(tc, c);
        font.getGlyph(c).getItalicCorrection();
        Dimen delta = font.getGlyph(c).getItalicCorrection();
        Node scripts = makeScripts(charNode, mathContext, delta, logger);

        if (scripts != null) {
            nodes.add(scripts);
        } else {
            nodes.add(charNode);
        }

        //see "TTP [755]"
        //TODO gene: insert kern for italic correction if required
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#typeset(
     *      de.dante.extex.typesetter.type.NodeList,
     *      de.dante.extex.typesetter.type.noad.util.MathContext,
     *      de.dante.extex.typesetter.TypesetterOptions)
     */
    public void typeset(final NodeList nodes, final MathContext mathContext,
            final TypesetterOptions context, final Logger logger)
            throws ConfigurationException,
                TypesetterException {

        StyleNoad style = mathContext.getStyle();
        UnicodeChar c = glyph.getCharacter();
        Font font = context.getFont(NumberedFont.key(context, //
                style.getFontName(), Integer.toString(glyph.getFamily())));
        if (font instanceof NullFont) {
            throw new TypesetterException(new HelpingException(getLocalizer(),
                    "TTP.UndefinedFamily", style.getStyleName(), Integer
                            .toString(glyph.getFamily()), c.toString()));
        }

        if (font.getGlyph(c) == null) {
            //see "TTP [581]"
            if (context.getCountOption("tracinglostchars").gt(Count.ZERO)) {
                logger.info(getLocalizer().format("TTP.MissingChar",
                        c.toString(), font.getFontName()));
            }
            setSpacingClass(MathSpacing.UNDEF);
            return;
        }

        int size = nodes.size();
        if (size > 0) {
            Node n = nodes.get(size - 1);
            if (n instanceof CharNode) {
                CharNode cn = ((CharNode) n);
                if (cn.getTypesettingContext().getFont().equals(font)) {
                    Dimen kerning = font.getGlyph(cn.getCharacter())
                            .getKerning(c);
                    if (kerning.ne(Dimen.ZERO_PT)) {
                        nodes.add(new ImplicitKernNode(kerning, true));
                    }
                }
            }
        }

        TypesettingContext tc = context.getTypesettingContextFactory()
                .newInstance(context.getTypesettingContext(), font);
        nodes.add(new CharNode(tc, c));
    }

}
