/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.color;

import de.dante.extex.color.ColorUtil;
import de.dante.extex.color.ColorVisitor;
import de.dante.extex.color.model.CmykColor;
import de.dante.extex.color.model.GrayscaleColor;
import de.dante.extex.color.model.HsvColor;
import de.dante.extex.color.model.RgbColor;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Color;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.AbstractAssignment;
import de.dante.extex.interpreter.type.Showable;
import de.dante.extex.interpreter.type.Theable;
import de.dante.extex.interpreter.type.color.ColorConvertible;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.exception.GeneralException;

/**
 * This class is a abstract base class for color primitives.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractColor extends AbstractAssignment
        implements
            ColorConvertible,
            Theable,
            Showable {

    /**
     * The field <tt>theVisitor</tt> contains the color visitor for converting
     * to a printable representation.
     */
    private static ColorVisitor theVisitor = new ColorVisitor() {

        /**
         * @see de.dante.extex.color.ColorVisitor#visitCmyk(
         *       de.dante.extex.color.model.CmykColor,
         *       java.lang.Object)
         */
        public Object visitCmyk(final CmykColor color, final Object c)
                throws GeneralException {

            StringBuffer sb = new StringBuffer();
            ColorUtil.formatAlpha(sb, color.getAlpha());
            sb.append("cmyk {");
            ColorUtil.formatComponent(sb, color.getCyan());
            sb.append(" ");
            ColorUtil.formatComponent(sb, color.getMagenta());
            sb.append(" ");
            ColorUtil.formatComponent(sb, color.getYellow());
            sb.append(" ");
            ColorUtil.formatComponent(sb, color.getBlack());
            sb.append("}");
            return sb.toString();
        }

        /**
         * @see de.dante.extex.color.ColorVisitor#visitGray(
         *      de.dante.extex.color.model.GrayscaleColor,
         *      java.lang.Object)
         */
        public Object visitGray(final GrayscaleColor color, final Object c)
                throws GeneralException {

            StringBuffer sb = new StringBuffer();
            ColorUtil.formatAlpha(sb, color.getAlpha());
            sb.append("gray {");
            ColorUtil.formatComponent(sb, color.getGray());
            sb.append("}");
            return sb.toString();
        }

        /**
         * @see de.dante.extex.color.ColorVisitor#visitHsv(
         *      de.dante.extex.color.model.HsvColor,
         *      java.lang.Object)
         */
        public Object visitHsv(final HsvColor color, final Object c)
                throws GeneralException {

            StringBuffer sb = new StringBuffer();
            ColorUtil.formatAlpha(sb, color.getAlpha());
            sb.append("hsv {");
            ColorUtil.formatComponent(sb, color.getHue());
            sb.append(" ");
            ColorUtil.formatComponent(sb, color.getSaturation());
            sb.append(" ");
            ColorUtil.formatComponent(sb, color.getValue());
            sb.append("}");
            return sb.toString();
        }

        /**
         * @see de.dante.extex.color.ColorVisitor#visitRgb(
         *      de.dante.extex.color.model.RgbColor,
         *      java.lang.Object)
         */
        public Object visitRgb(final RgbColor color, final Object c)
                throws GeneralException {

            StringBuffer sb = new StringBuffer();
            ColorUtil.formatAlpha(sb, color.getAlpha());
            sb.append("rgb {");
            ColorUtil.formatComponent(sb, color.getRed());
            sb.append(" ");
            ColorUtil.formatComponent(sb, color.getGreen());
            sb.append(" ");
            ColorUtil.formatComponent(sb, color.getBlue());
            sb.append("}");
            return sb.toString();
        }

    };

    /**
     * Creates a new object.
     *
     * @param name the name for tracing
     */
    public AbstractColor(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.Showable#show(
     *      de.dante.extex.interpreter.context.Context)
     */
    public Tokens show(final Context context) throws InterpreterException {

        Color color = convertColor(context, null, null);
        try {
            return new Tokens(context, (String) color.visit(theVisitor,
                    null));
        } catch (InterpreterException e) {
            throw e;
        } catch (GeneralException e) {
            throw new InterpreterException(e);
        }
    }

    /**
     * @see de.dante.extex.interpreter.type.Theable#the(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public Tokens the(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        Color color = convertColor(context, source, typesetter);
        try {
            return new Tokens(context, (String) color.visit(theVisitor,
                    null));
        } catch (InterpreterException e) {
            throw e;
        } catch (GeneralException e) {
            throw new InterpreterException(e);
        }
    }

}
