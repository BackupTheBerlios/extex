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

import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.noad.util.MathContext;
import de.dante.extex.typesetter.type.noad.util.MathSpacing;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This Noad represents an ordinary character.
 *
 * @see "TTP [682]"
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.13 $
 */
public class OrdinaryNoad extends AbstractNucleusNoad implements SimpleNoad {

    /**
     * Creates a new object.
     *
     * @param nucleus the nucleus
     * @param tc the typesetting context for the color
     */
    public OrdinaryNoad(final Noad nucleus, final TypesettingContext tc) {

        super(nucleus, tc);
        setSpacingClass(MathSpacing.ORD);
    }

    /**
     * @see "TTP [696]"
     * @see de.dante.extex.typesetter.type.noad.AbstractNoad#toStringAdd(
     *      java.lang.StringBuffer,
     *      int)
     */
    protected void toStringAdd(final StringBuffer sb, final int depth) {

        sb.append("mathord");
    }

    /**
     * @see "TTP [752]"
     * @see de.dante.extex.typesetter.type.noad.Noad#typeset(
     *      de.dante.extex.typesetter.type.noad.NoadList,
     *      int,
     *      de.dante.extex.typesetter.type.NodeList,
     *      de.dante.extex.typesetter.type.noad.util.MathContext,
     *      java.util.logging.Logger)
     */
    public void typeset(final NoadList noads, final int index,
            final NodeList list, final MathContext mathContext,
            final Logger logger)
            throws TypesetterException,
                ConfigurationException {

        getNucleus().typeset(noads, index, list, mathContext, logger);

        //TODO gene: typeset() unimplemented
        throw new RuntimeException("unimplemented");
        //return index + 1;
    }

    /**
     * Translate an OrdNoad into a NodeList.
     *
     * @param noads the list of noads currently processed
     * @param index the index of the current node in the list
     * @param list the list to add the nodes to. This list contains the Nodes
     *  previously typeset. Thus it can be used to look back
     * @param mathContext the context to consider
     * @param logger the logger for debugging and tracing information
     *
     * @return the index of the next noad to consider
     */
    public static final int make_ord(final NoadList noads, final int index,
            final NodeList list, final MathContext mathContext,
            final Logger logger) {

        int i = index;
        //    var a: integer;  {address of lig/kern instruction}
        //    p,r: pointer;  {temporary registers for list manipulation}
        // begin restart:
        do {
            Noad noad = noads.get(i);
            if (noad.getSubscript() == null && noad.getSuperscript() == null
                    && noad instanceof CharNoad) {

                // if math_type(subscr(q))=empty then
                //    if math_type(supscr(q))=empty then
                //      if math_type(nucleus(q))=math_char then
                if (++i >= noads.size()) {
                    Noad n = noads.get(i);
                    //        begin p ? link(q);
                    //        if p ? null then
                    if (n instanceof SimpleNoad) {
                        //          if (type(p) ? ord_noad) ? (type(p) ? punct_noad) then
                        Noad nuc = ((AbstractNucleusNoad) n).getNucleus();
                        if (nuc instanceof CharNoad) {
                            //            if math_type(nucleus(p))=math_char then
                            //              if fam(nucleus(p))=fam(nucleus(q)) then
                            //                begin math_type(nucleus(q)) ? math_text_char; fetch(nucleus(q));
                            //                if char_tag(cur_i)=lig_tag then
                            //                  begin a ? lig_kern_start(cur_f)(cur_i); cur_c ? character(nucleus(p)); cur_i ? font_info[a].qqqq;
                            //                  if skip_byte(cur_i)>stop_flag then
                            //                    begin a ? lig_kern_restart(cur_f)(cur_i); cur_i ? font_info[a].qqqq;
                            //                    end ;
                            //                   loop begin ?If instruction cur_i is a kern with cur_c, attach the kern after q; or if it is a ligature with cur_c, combine noads q and p appropriately; then return if the cursor has moved past a noad, or goto restart 753?;
                            //                    if skip_byte(cur_i) ? stop_flag then return ;
                            //                    a ? a+qo(skip_byte(cur_i))+1; cur_i ? font_info[a].qqqq;
                            //                    end ;
                            //                  end ;
                            //                end ;
                            //        end ;
                            // exit: end ;
                        }
                    }
                }
            } else {
                return i;
            }
        } while (true);
    }

}
