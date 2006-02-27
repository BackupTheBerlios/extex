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

package de.dante.extex.typesetter.paragraphBuilder.impl;

import junit.framework.TestCase;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.context.TypesettingContextFactory;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.count.FixedCount;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.glue.FixedGlue;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.scanner.type.token.TokenFactory;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.paragraphBuilder.ParagraphBuilder;
import de.dante.extex.typesetter.paragraphBuilder.ParagraphShape;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.util.UnicodeChar;
import de.dante.util.exception.GeneralException;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.10 $
 */
public class ParagraphBuilderImplTest extends TestCase {

    /**
     * Inner class for the typesetter options.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.10 $
     */
    private class MockOptions implements TypesetterOptions {

        /**
         * @see de.dante.extex.typesetter.TypesetterOptions#getCountOption(
         *      java.lang.String)
         */
        public FixedCount getCountOption(final String name) {

            if (name.equals("tracingparagraphs")) {
                return new Count(0);
            } else if (name.equals("pretolerance")) {
                return new Count(300);
            } else if (name.equals("tolerance")) {
                return new Count(200);
            } else if (name.equals("hyphenpenalty")) {
                return new Count(20);
            } else if (name.equals("exhyphenpenalty")) {
                return new Count(30);
            }
            return new Count(0);
        }

        /**
         * @see de.dante.extex.typesetter.TypesetterOptions#getDimenOption(java.lang.String)
         */
        public FixedDimen getDimenOption(final String name) {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.TypesetterOptions#getFont(
         *      java.lang.String)
         */
        public Font getFont(final String name) {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.TypesetterOptions#getGlueOption(
         *      java.lang.String)
         */
        public FixedGlue getGlueOption(final String name) {

            if (name.equals("parfillskip")) {
                return new Glue(1000);
            }
            return new Glue(0);
        }

        /**
         * @see de.dante.extex.typesetter.TypesetterOptions#getLccode(
         *      de.dante.util.UnicodeChar)
         */
        public UnicodeChar getLccode(final UnicodeChar uc) {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.TypesetterOptions#getNamespace()
         */
        public String getNamespace() {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.TypesetterOptions#getParshape()
         */
        public ParagraphShape getParshape() {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.TypesetterOptions#getTokenFactory()
         */
        public TokenFactory getTokenFactory() {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.TypesetterOptions#getTypesettingContext()
         */
        public TypesettingContext getTypesettingContext() {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.TypesetterOptions#getTypesettingContextFactory()
         */
        public TypesettingContextFactory getTypesettingContextFactory() {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.TypesetterOptions#setParshape(
         *      de.dante.extex.typesetter.paragraphBuilder.ParagraphShape)
         */
        public void setParshape(final ParagraphShape shape) {

        }

        /**
         * @see de.dante.extex.typesetter.TypesetterOptions#setCountOption(
         *      java.lang.String,
         *      de.dante.extex.interpreter.type.count.FixedCount)
         */
        public void setCountOption(final String name, final long value)
                throws GeneralException {

        }
    }

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(ParagraphBuilderImplTest.class);
    }

    /**
     * ...
     */
    public void test1() throws Exception {

        ParagraphBuilder builder = new ParagraphBuilderImpl();
        builder.setOptions(new MockOptions());
        HorizontalListNode nodes = new HorizontalListNode();
        assertEquals(0, builder.build(nodes).size());
    }
}