/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.typesetter.paragraphBuilder;

import junit.framework.TestCase;
import de.dante.extex.interpreter.type.dimen.Dimen;

/**
 * Test cases for the class HangingParagraphShape.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class HangingParagraphShapeTest extends TestCase {

    /**
     * The constant <tt>INDENT</tt> contains the amount of indentation to use.
     */
    private static final int INDENT = 1024;

    /**
     * The constant <tt>RIGHT</tt> contains the right margin.
     */
    private static final int RIGHT = 4096;

    /**
     * Command line interface.
     *
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(HangingParagraphShapeTest.class);
    }

    /**
     * Test case checking that 0 length parshape repeats 0 as left and the right
     * margin.
     */
    public void test0() {

        HangingParagraphShape shape = new HangingParagraphShape(0, new Dimen(
                INDENT), new Dimen(RIGHT));

        assertEquals(0, shape.getLeft(0).getValue());
        assertEquals(0, shape.getLeft(1).getValue());
        assertEquals(0, shape.getLeft(2).getValue());
        assertEquals(0, shape.getLeft(3).getValue());
        assertEquals(RIGHT, shape.getRight(0).getValue());
        assertEquals(RIGHT, shape.getRight(1).getValue());
        assertEquals(RIGHT, shape.getRight(2).getValue());
        assertEquals(RIGHT, shape.getRight(3).getValue());
    }

    /**
     * Test case checking that 0 length parshape repeats the left and the right
     * margin.
     */
    public void test1() {

        HangingParagraphShape shape = new HangingParagraphShape(1, new Dimen(
                INDENT), new Dimen(RIGHT));

        assertEquals(0, shape.getLeft(0).getValue());
        assertEquals(INDENT, shape.getLeft(1).getValue());
        assertEquals(INDENT, shape.getLeft(2).getValue());
        assertEquals(INDENT, shape.getLeft(3).getValue());
        assertEquals(RIGHT, shape.getRight(0).getValue());
        assertEquals(RIGHT, shape.getRight(1).getValue());
        assertEquals(RIGHT, shape.getRight(2).getValue());
        assertEquals(RIGHT, shape.getRight(3).getValue());
    }

    /**
     * ...
     */
    public void test2() {

        HangingParagraphShape shape = new HangingParagraphShape(2, new Dimen(
                INDENT), new Dimen(RIGHT));

        assertEquals(0, shape.getLeft(0).getValue());
        assertEquals(0, shape.getLeft(1).getValue());
        assertEquals(INDENT, shape.getLeft(2).getValue());
        assertEquals(INDENT, shape.getLeft(3).getValue());
        assertEquals(RIGHT, shape.getRight(0).getValue());
        assertEquals(RIGHT, shape.getRight(1).getValue());
        assertEquals(RIGHT, shape.getRight(2).getValue());
        assertEquals(RIGHT, shape.getRight(3).getValue());
    }

    /**
     * ...
     */
    public void test3() {

        HangingParagraphShape shape = new HangingParagraphShape(3, new Dimen(
                INDENT), new Dimen(RIGHT));

        assertEquals(0, shape.getLeft(0).getValue());
        assertEquals(0, shape.getLeft(1).getValue());
        assertEquals(0, shape.getLeft(2).getValue());
        assertEquals(INDENT, shape.getLeft(3).getValue());
        assertEquals(RIGHT, shape.getRight(0).getValue());
        assertEquals(RIGHT, shape.getRight(1).getValue());
        assertEquals(RIGHT, shape.getRight(2).getValue());
        assertEquals(RIGHT, shape.getRight(3).getValue());
    }

    /**
     * ...
     */
    public void testM1() {

        HangingParagraphShape shape = new HangingParagraphShape(-1, new Dimen(
                INDENT), new Dimen(RIGHT));

        assertEquals(INDENT, shape.getLeft(0).getValue());
        assertEquals(0, shape.getLeft(1).getValue());
        assertEquals(0, shape.getLeft(2).getValue());
        assertEquals(0, shape.getLeft(3).getValue());
        assertEquals(RIGHT, shape.getRight(0).getValue());
        assertEquals(RIGHT, shape.getRight(1).getValue());
        assertEquals(RIGHT, shape.getRight(2).getValue());
        assertEquals(RIGHT, shape.getRight(3).getValue());
    }

    /**
     * ...
     */
    public void testM2() {

        HangingParagraphShape shape = new HangingParagraphShape(-2, new Dimen(
                INDENT), new Dimen(RIGHT));

        assertEquals(INDENT, shape.getLeft(0).getValue());
        assertEquals(INDENT, shape.getLeft(1).getValue());
        assertEquals(0, shape.getLeft(2).getValue());
        assertEquals(0, shape.getLeft(3).getValue());
        assertEquals(RIGHT, shape.getRight(0).getValue());
        assertEquals(RIGHT, shape.getRight(1).getValue());
        assertEquals(RIGHT, shape.getRight(2).getValue());
        assertEquals(RIGHT, shape.getRight(3).getValue());
    }

    /**
     * ...
     */
    public void testM3() {

        HangingParagraphShape shape = new HangingParagraphShape(-3, new Dimen(
                INDENT), new Dimen(RIGHT));

        assertEquals(INDENT, shape.getLeft(0).getValue());
        assertEquals(INDENT, shape.getLeft(1).getValue());
        assertEquals(INDENT, shape.getLeft(2).getValue());
        assertEquals(0, shape.getLeft(3).getValue());
        assertEquals(RIGHT, shape.getRight(0).getValue());
        assertEquals(RIGHT, shape.getRight(1).getValue());
        assertEquals(RIGHT, shape.getRight(2).getValue());
        assertEquals(RIGHT, shape.getRight(3).getValue());
    }

}