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

package de.dante.extex.typesetter.type.noad;

import de.dante.extex.typesetter.type.MathGlyph;

/**
 * This interface describes a visitor for Noads.
 * With the help of this interface the visitor pattern can be implemented.
 * <p>
 * The user of the visitor pattern has to provide an implementation of
 * this interface. Then the <tt>visit</tt> method is invoked and the caller
 * is forwarded to the appropriate <tt>visit</tt> method in the visitor.
 * </p>
 * <p>
 * Consider we have a CharNoad <tt>noad</tt> at hand and a method invokes
 * <pre>
 *   noad.visit(visitor, a);
 * </pre>
 * then the following method in the object <tt>visitor</tt> is invoked:
 * <pre>
 *   visitCharNoad(visitor, a)
 * </pre>
 * </p>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public interface NoadVisitor {

    /**
     * Visitor method for an accent noad.
     *
     * @param noad the visited noad
     */
    void visitAccent(AccentNoad noad);

    /**
     * Visitor method for a binary operator noad.
     *
     * @param noad the visited noad
     */
    void visitBin(BinNoad noad);

    /**
     * Visitor method for a character noad.
     *
     * @param noad the visited noad
     */
    void visitChar(CharNoad noad);

    /**
     * Visitor method for a mathchoice noad.
     *
     * @param noad the visited noad
     */
    void visitChoice(ChoiceNoad noad);

    /**
     * Visitor method for a close noad.
     *
     * @param noad the visited noad
     */
    void visitClose(CloseNoad noad);

    /**
     * Visitor method for a fraction noad.
     *
     * @param noad the visited noad
     */
    void visitFraction(FractionNoad noad);

    /**
     * Visitor method for a vertical center noad.
     *
     * @param noad the visited noad
     */
    void visitGlyph(MathGlyph noad);

    /**
     * Visitor method for an inner noad.
     *
     * @param noad the visited noad
     */
    void visitInner(InnerNoad noad);

    /**
     * Visitor method for a math list noad.
     *
     * @param noad the visited noad
     */
    void visitMathList(MathList noad);

    /**
     * Visitor method for an open noad.
     *
     * @param noad the visited noad
     */
    void visitOpen(OpenNoad noad);

    /**
     * Visitor method for an operation noad.
     *
     * @param noad the visited noad
     */
    void visitOperator(OperatorNoad noad);

    /**
     * Visitor method for an ordinary noad.
     *
     * @param noad the visited noad
     */
    void visitOrd(OrdNoad noad);

    /**
     * Visitor method for an overlined noad.
     *
     * @param noad the visited noad
     */
    void visitOverlined(OverlinedNoad noad);

    /**
     * Visitor method for a punctation noad.
     *
     * @param noad the visited noad
     */
    void visitPunctation(PunctationNoad noad);

    /**
     * Visitor method for a radical noad.
     *
     * @param noad the visited noad
     */
    void visitRadical(RadicalNoad noad);

    /**
     * Visitor method for a relation noad.
     *
     * @param noad the visited noad
     */
    void visitRel(RelNoad noad);

    /**
     * Visitor method for a style noad.
     *
     * @param noad the visited noad
     */
    void visitStyle(StyleNoad noad);

    /**
     * Visitor method for an underline noad.
     *
     * @param noad the visited noad
     */
    void visitUnderlined(UnderlinedNoad noad);

    /**
     * Visitor method for a vertical center noad.
     *
     * @param noad the visited noad
     */
    void visitVCenter(VCenterNoad noad);

}