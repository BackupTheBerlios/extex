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

package de.dante.extex.interpreter.type.font;

import de.dante.extex.font.Glyph;
import de.dante.extex.font.type.Fount;
import de.dante.extex.font.type.ModifiableFount;
import de.dante.extex.font.type.VirtualFount;
import de.dante.extex.font.type.efm.commands.EfmHVW;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.typesetter.type.node.AbstractNode;
import de.dante.extex.typesetter.type.node.VirtualCharNode;
import de.dante.util.UnicodeChar;

/**
 * Implemetation for a virtual font.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.7 $
 */
public class VirtualFontImpl extends FontImpl implements VirtualFount {

    /**
     * Create a new Object
     * @param afount  the fount
     */
    public VirtualFontImpl(final ModifiableFount afount) {

        super(afount);
    }

    /**
     * @see de.dante.extex.font.type.VirtualFount#getVirtualCharNode(
     *      de.dante.extex.interpreter.context.TypesettingContext,
     *      de.dante.util.UnicodeChar)
     */
    public VirtualCharNode getVirtualCharNode(final TypesettingContext context,
            final UnicodeChar uc) {

        VirtualCharNode cnode = new VirtualCharNode(context, uc);

        Fount fount = getFount();
        Glyph vglyph = fount.getGlyph(uc);

        // h and v position
        Dimen h = new Dimen(0);
        Dimen v = new Dimen(0);

        // get all commands
        //        if (fount instanceof EFMFount) {
        //            EFMFount efm = (EFMFount) fount;
        //            ArrayList commands = efm.getCommands(uc);
        //            if (commands != null) {
        //                for (int i = 0; i < commands.size(); i++) {
        //                    Object o = commands.get(i);
        //                    if (o instanceof EfmChar) {
        //                        EfmChar c = (EfmChar) o;
        //                        String fontname = c.getFont();
        //                        Dimen fontsize = c.getFontsize();
        //                        Font nf = null;
        //                        try {
        //                            nf = efm.getFontfactory().getInstance(fontname,
        //                                    fontsize);
        //                        } catch (Exception e) {
        //                            // eigentlich unmöglich, da Font schon
        //                            // geladen worden ist
        //                            e.printStackTrace();
        //                            // TODO: handle exception
        //                        }
        //                        if (nf != null) {
        //                            ModifiableTypesettingContext newcontext = new TypesettingContextImpl(
        //                                    context);
        //                            newcontext.setFont(nf);
        //                            CharNode cn = new CharNode(newcontext, uc);
        //
        //                            calculateMoveShift(v, h, c, cn, cnode);
        //                        }
        //                    } else if (o instanceof EfmRule) {
        //                        EfmRule r = (EfmRule) o;
        //
        //                        RuleNode rn = new RuleNode(r.getWidth(), r.getHeight(),
        //                                Dimen.ZERO_PT, context);
        //
        //                        calculateMoveShift(v, h, r, rn, cnode);
        //
        //                    } else if (o instanceof EfmSpecial) {
        //                        EfmSpecial sp = (EfmSpecial) o;
        //                        SpecialNode sn = new SpecialNode(sp.getText());
        //                        cnode.add(sn);
        //                    }
        //                }
        //            }
        //}

        // set the dimension from the glyph to the nodelist
        cnode.setHeight(vglyph.getHeight());
        cnode.setDepth(vglyph.getDepth());
        cnode.setWidth(vglyph.getWidth());

        return cnode;
    }

    /**
     * Calculate move / shift
     * @param v     the v position
     * @param h     the h position
     * @param hvw   the EfmHVW  (EfmChar, EfmRule )
     * @param node  the Node (CharNode, RuleNode )
     * @param cnode the VirtualCharNode
     */
    private void calculateMoveShift(final Dimen v, final Dimen h,
            final EfmHVW hvw, final AbstractNode node,
            final VirtualCharNode cnode) {

        // calculate move and shift
        // v goes down -> shift goes up
        Dimen nodeh = hvw.getH();
        Dimen nodev = hvw.getV();
        nodev.negate();

        // move = nodeh-h
        Dimen move = new Dimen(nodeh);
        move.subtract(h);

        // shift = nodev-v
        Dimen shift = new Dimen(nodev);
        shift.subtract(v);

        // set move / shift
        //TODO MGN: node.setMove(move);
        //TODO MGN: node.setShift(shift);
        cnode.add(node);

        // calculate new refpoint
        // h = move + width
        h.set(move);
        h.add(node.getWidth());
        // v = 0
        v.set(0);
    }
}