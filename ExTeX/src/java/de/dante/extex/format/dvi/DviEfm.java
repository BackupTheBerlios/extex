/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.format.dvi;

import java.io.IOException;
import java.util.Map;

import org.jdom.Element;

import de.dante.extex.font.FontFactory;
import de.dante.extex.font.Glyph;
import de.dante.extex.font.exception.FontException;
import de.dante.extex.font.type.tfm.TFMFixWord;
import de.dante.extex.format.dvi.command.DviBOP;
import de.dante.extex.format.dvi.command.DviChar;
import de.dante.extex.format.dvi.command.DviCommand;
import de.dante.extex.format.dvi.command.DviDown;
import de.dante.extex.format.dvi.command.DviEOP;
import de.dante.extex.format.dvi.command.DviExecuteCommand;
import de.dante.extex.format.dvi.command.DviFntDef;
import de.dante.extex.format.dvi.command.DviFntNum;
import de.dante.extex.format.dvi.command.DviNOP;
import de.dante.extex.format.dvi.command.DviPOP;
import de.dante.extex.format.dvi.command.DviPost;
import de.dante.extex.format.dvi.command.DviPostPost;
import de.dante.extex.format.dvi.command.DviPre;
import de.dante.extex.format.dvi.command.DviPush;
import de.dante.extex.format.dvi.command.DviRight;
import de.dante.extex.format.dvi.command.DviRule;
import de.dante.extex.format.dvi.command.DviW;
import de.dante.extex.format.dvi.command.DviX;
import de.dante.extex.format.dvi.command.DviXXX;
import de.dante.extex.format.dvi.command.DviY;
import de.dante.extex.format.dvi.command.DviZ;
import de.dante.extex.format.dvi.exception.DviException;
import de.dante.extex.format.dvi.exception.DviGlyphNotFoundException;
import de.dante.extex.format.dvi.exception.DviMissingFontException;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.util.UnicodeChar;
import de.dante.util.Unit;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.file.random.RandomAccessR;

/**
 * DVI to EFM converter.
 *
 * <p>
 * Commands are taken from DVItype 3.4.
 * </p>
 *
 * @see <a href="package-summary.html#DVIformat">DVI-Format</a>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class DviEfm implements DviInterpreter, DviExecuteCommand {

    /**
     * show all elements (move, fnt, )
     */
    private static final boolean SHOWALL = true;

    /**
     * the dvi stack
     */
    private DviStack stack;

    /**
     * the dvi values;
     */
    private DviValues val;

    /**
     * dvi element
     */
    private Element dvi;

    //    /**
    //     * the fontfactory
    //     */
    //    private FontFactory fontfactory;

    /**
     * the map for all sub fonts.
     */
    private Map fontmap;

    /**
     * Create a new object.
     *
     * @param element   the dvi element
     * @param ff        the fontfactroy
     * @param fm        the font map
     */
    public DviEfm(final Element element, final FontFactory ff, final Map fm) {

        dvi = element;
        // fontfactory = ff;
        val = new DviValues();
        stack = new DviStack();
        fontmap = fm;
    }

    /**
     * Returns the font from the fontmap.
     * @return Returns the font from the fontmap.
     * @throws DviMissingFontException if the font is not found.
     */
    private Font getFont() throws DviMissingFontException {

        if (val.getF() == -1) {
            return null;
        }
        Font font = (Font) fontmap.get(new Integer(val.getF()));
        if (font == null) {
            throw new DviMissingFontException(String.valueOf(val.getF()));
        }
        return font;
    }

    /**
     * Add h,v values to the element
     * @param element   the element
     */
    private void addHV(final Element element) {

        TFMFixWord h = new TFMFixWord();
        TFMFixWord v = new TFMFixWord();
        h.setValue(val.getH());
        v.setValue(val.getV());
        element.setAttribute("h", h.toStringComma());
        element.setAttribute("v", v.toStringComma());
        element.setAttribute("h-fw", String.valueOf(h.getValue()));
        element.setAttribute("v-fw", String.valueOf(v.getValue()));
    }

    /**
     * @see de.dante.extex.format.dvi.DVIInterpreter#interpret(
     *      de.dante.util.file.random.RandomAccessR)
     */
    public void interpret(final RandomAccessR rar) throws IOException,
            DviException, FontException, ConfigurationException {

        while (!rar.isEOF()) {
            DviCommand command = DviCommand.getNextCommand(rar);

            if (command instanceof DviChar) {
                DviChar cc = (DviChar) command;
                execute(cc);
                continue;
            } else if (command instanceof DviRight) {
                DviRight cc = (DviRight) command;
                execute(cc);
                continue;
            } else if (command instanceof DviDown) {
                DviDown cc = (DviDown) command;
                execute(cc);
                continue;
            } else if (command instanceof DviW) {
                DviW cc = (DviW) command;
                execute(cc);
                continue;
            } else if (command instanceof DviX) {
                DviX cc = (DviX) command;
                execute(cc);
                continue;
            } else if (command instanceof DviY) {
                DviY cc = (DviY) command;
                execute(cc);
                continue;
            } else if (command instanceof DviZ) {
                DviZ cc = (DviZ) command;
                execute(cc);
                continue;
            } else if (command instanceof DviBOP) {
                DviBOP cc = (DviBOP) command;
                execute(cc);
                continue;
            } else if (command instanceof DviEOP) {
                DviEOP cc = (DviEOP) command;
                execute(cc);
                continue;
            } else if (command instanceof DviPOP) {
                DviPOP cc = (DviPOP) command;
                execute(cc);
                continue;
            } else if (command instanceof DviPush) {
                DviPush cc = (DviPush) command;
                execute(cc);
                continue;
            } else if (command instanceof DviRule) {
                DviRule cc = (DviRule) command;
                execute(cc);
                continue;
            } else if (command instanceof DviXXX) {
                DviXXX cc = (DviXXX) command;
                execute(cc);
                continue;
            } else if (command instanceof DviFntDef) {
                DviFntDef cc = (DviFntDef) command;
                execute(cc);
                continue;
            } else if (command instanceof DviFntNum) {
                DviFntNum cc = (DviFntNum) command;
                execute(cc);
                continue;
            } else if (command instanceof DviPost) {
                DviPost cc = (DviPost) command;
                execute(cc);
                continue;
            } else if (command instanceof DviPostPost) {
                DviPostPost cc = (DviPostPost) command;
                execute(cc);
                continue;
            } else if (command instanceof DviPre) {
                DviPre cc = (DviPre) command;
                execute(cc);
                continue;
            }
        }
    }

    /**
     * Create a new move element and add the value
     * @param attr  the attribute name
     * @param fw    the fixword value
     * @return Returns the new move element.
     */
    private Element addMove(final String attr, final TFMFixWord fw) {

        Element element = new Element("move");

        element.setAttribute(attr, fw.toStringComma());
        element.setAttribute(attr + "-fw", String.valueOf(fw.getValue()));
        return element;
    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviBOP)
     */
    public void execute(final DviBOP command) throws DviException,
            FontException, ConfigurationException {

        // do nothing
    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviChar)
     */
    public void execute(final DviChar command) throws DviException,
            FontException, ConfigurationException {

        Element element = new Element("char");
        element.setAttribute("id", String.valueOf(command.getCh()));
        Font font = getFont();
        if (font != null) {
            element.setAttribute("font", font.getFontName());
            element.setAttribute("fontsize", Unit.getDimenAsPTString(font
                    .getDesignSize()));
        }
        Glyph g = font.getGlyph(new UnicodeChar(command.getCh()));
        if (g == null) {
            throw new DviGlyphNotFoundException(String.valueOf(command.getCh()));
        }
        // TODO UWE stimmt dies mit der Breite???
        Dimen width = g.getWidth();
        element.setAttribute("width", Unit.getDimenAsPTString(width,
                TFMFixWord.FRACTIONDIGITS));
        addHV(element);
        dvi.addContent(element);
        if (!command.isPut()) {
            val.addH((int) width.getValue());
        }
    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviDown)
     */
    public void execute(final DviDown command) throws DviException,
            FontException, ConfigurationException {

        if (SHOWALL) {
            TFMFixWord fw = new TFMFixWord();
            fw.setValue(command.getValue());
            dvi.addContent(addMove("down", fw));
        }

        val.addV(command.getValue());
    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviEOP)
     */
    public void execute(final DviEOP command) throws DviException,
            FontException, ConfigurationException {

        // do nothing
    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviFntDef)
     */
    public void execute(final DviFntDef command) throws DviException,
            FontException, ConfigurationException {

        // do nothing
    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviFntNum)
     */
    public void execute(final DviFntNum command) throws DviException,
            FontException, ConfigurationException {

        val.setF(command.getFont());
        if (SHOWALL) {
            Element element = new Element("font");
            element.setAttribute("id", String.valueOf(command.getFont()));
            Font font = getFont();
            if (font != null) {
                element.setAttribute("font", font.getFontName());
            }
            dvi.addContent(element);
        }
    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviPOP)
     */
    public void execute(final DviPOP command) throws DviException,
            FontException, ConfigurationException {

        DviValues v = stack.pop();
        val.setValues(v);
        if (SHOWALL) {
            Element element = new Element("pop");
            dvi.addContent(element);
        }
    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviNOP)
     */
    public void execute(final DviNOP command) throws DviException,
            FontException, ConfigurationException {

        // do nothing
    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviPost)
     */
    public void execute(final DviPost command) throws DviException,
            FontException, ConfigurationException {

        // do nothing
    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviPostPost)
     */
    public void execute(final DviPostPost command) throws DviException,
            FontException, ConfigurationException {

        // do nothing
    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviPre)
     */
    public void execute(final DviPre command) throws DviException,
            FontException, ConfigurationException {

        // do nothing
    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviPush)
     */
    public void execute(final DviPush command) throws DviException,
            FontException, ConfigurationException {

        stack.push(val);
        if (SHOWALL) {
            Element element = new Element("push");
            dvi.addContent(element);
        }
    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviRight)
     */
    public void execute(final DviRight command) throws DviException,
            FontException, ConfigurationException {

        if (SHOWALL) {
            TFMFixWord fw = new TFMFixWord();
            fw.setValue(command.getValue());
            dvi.addContent(addMove("right", fw));
        }
        val.addH(command.getValue());
    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviRule)
     */
    public void execute(final DviRule command) throws DviException,
            FontException, ConfigurationException {

        TFMFixWord w = new TFMFixWord();
        TFMFixWord h = new TFMFixWord();
        w.setValue(command.getWidth());
        h.setValue(command.getHeight());
        Element element = new Element("rule");
        element.setAttribute("width", w.toStringComma());
        element.setAttribute("hight", h.toStringComma());
        element.setAttribute("width-fw", String.valueOf(w.getValue()));
        element.setAttribute("hight-fw", String.valueOf(h.getValue()));
        addHV(element);
        dvi.addContent(element);
        if (!command.isPut()) {
            val.addH(command.getWidth());
        }
    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviW)
     */
    public void execute(final DviW command) throws DviException, FontException,
            ConfigurationException {

        if (SHOWALL) {
            TFMFixWord fw = new TFMFixWord();
            if (command.isW0()) {
                fw.setValue(val.getW());
            } else {
                fw.setValue(command.getValue());
            }
            dvi.addContent(addMove("right", fw));
        }

        // calculate
        if (!command.isW0()) {
            val.setW(command.getValue());
        }
        val.addH(val.getW());
    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviX)
     */
    public void execute(final DviX command) throws DviException, FontException,
            ConfigurationException {

        if (SHOWALL) {
            TFMFixWord fw = new TFMFixWord();
            if (command.isX0()) {
                fw.setValue(val.getX());
            } else {
                fw.setValue(command.getValue());
            }
            dvi.addContent(addMove("right", fw));
        }

        if (!command.isX0()) {
            val.setX(command.getValue());
        }
        val.addH(val.getX());
    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviXXX)
     */
    public void execute(final DviXXX command) throws DviException,
            FontException, ConfigurationException {

        Element element = new Element("special");
        element.setAttribute("xxx", command.getXXXString());
        dvi.addContent(element);
    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviY)
     */
    public void execute(final DviY command) throws DviException, FontException,
            ConfigurationException {

        if (SHOWALL) {
            TFMFixWord fw = new TFMFixWord();
            if (command.isY0()) {
                fw.setValue(val.getY());
            } else {
                fw.setValue(command.getValue());
            }
            dvi.addContent(addMove("down", fw));
        }

        if (!command.isY0()) {
            val.setY(command.getValue());
        }
        val.addV(val.getY());
    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviZ)
     */
    public void execute(final DviZ command) throws DviException, FontException,
            ConfigurationException {

        if (SHOWALL) {
            TFMFixWord fw = new TFMFixWord();
            if (command.isZ0()) {
                fw.setValue(val.getZ());
            } else {
                fw.setValue(command.getValue());
            }
            dvi.addContent(addMove("down", fw));
        }

        if (!command.isZ0()) {
            val.setZ(command.getValue());
        }
        val.addV(val.getZ());
    }
}
