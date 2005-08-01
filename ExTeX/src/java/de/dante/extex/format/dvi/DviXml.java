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
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.jdom.Element;

import de.dante.extex.font.FontFactory;
import de.dante.extex.font.FountKey;
import de.dante.extex.font.Glyph;
import de.dante.extex.font.exception.FontException;
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
import de.dante.extex.format.dvi.exception.DviBopEopException;
import de.dante.extex.format.dvi.exception.DviException;
import de.dante.extex.format.dvi.exception.DviFontNotFoundException;
import de.dante.extex.format.dvi.exception.DviGlyphNotFoundException;
import de.dante.extex.format.dvi.exception.DviMissingFontException;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.util.UnicodeChar;
import de.dante.util.Unit;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.file.random.RandomAccessR;

/**
 * DVI to XML converter.
 *
 * <p>
 * Commands are taken from DVItype 3.4.
 * </p>
 *
 * @see <a href="package-summary.html#DVIformat">DVI-Format</a>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.10 $
 */

public class DviXml implements DviInterpreter, DviExecuteCommand {

    // TODO incompelte: change from element to xmlwriter
    /**
     * the parent element (change from some commands to nested other elements)
     */
    private Element parent;

    /**
     * Stack for the parents.
     * <p>
     * Each <code>bop</code> push the current parent element to the stack,
     * create a new Element and add it to the last parent.
     * Each <code>eop</code> pop the top element to parent.
     * </p>
     * <p>
     * The fntdef-Element are also stored in the post element, so post calls push
     * ans postpost calls pop before execute.
     * </p>
     */
    private Stack parentstack;

    /**
     * page counter
     */
    private int page = 0;

    /**
     * the fontfactory
     */
    private FontFactory fontfactory;

    /**
     * the map for all sub fonts.
     */
    private Map fontmap;

    /**
     * the dvi stack
     */
    private DviStack stack;

    /**
     * the dvi values;
     */
    private DviValues val;

    /**
     * Create a new object.
     * <p>
     * It is only usefull for small dvi-files
     * (or you have a lot of memory)
     * </p>
     *
     * @param element   the root element
     * @param ff        the fontfactroy
     */
    public DviXml(final Element element, final FontFactory ff) {

        parent = element;
        fontfactory = ff;
        parentstack = new Stack();
        fontmap = new HashMap();
        val = new DviValues();
        stack = new DviStack();
    }

    /**
     * show pt-values in elements
     */
    private boolean showPT;

    /**
     * the mag
     */
    private int mag;

    /**
     * Load the font.
     *
     * @param command   the command
     * @throws DviException if a font not found.
     * @throws FontException if an font-error occured.
     * @throws ConfigurationException from the config-system.
     */
    private void loadFont(final DviFntDef command) throws DviException,
            FontException, ConfigurationException {

        Integer key = new Integer(command.getFont());
        Dimen designsize = command.getScaleAsDimen();
        Count scale = command.getScaledAsCount(mag);
        String name = command.getFName();

        //        Font f = fontfactory.getInstance(name, designsize, scale, new Glue(0),
        //                true, true);
        Font f = fontfactory.getInstance(new FountKey(name, designsize, scale,
                new Glue(0), true, true));
        if (f == null) {
            throw new DviFontNotFoundException(name);
        }
        fontmap.put(key, f);
    }

    /**
     * @param command   the command
     * @param element   the element
     */
    private void addFontAttributes(final DviFntDef command,
            final Element element) {

        element.setAttribute("font", String.valueOf(command.getFont()));
        element.setAttribute("checksum", String.valueOf(command.getChecksum()));
        element.setAttribute("scalefactor", String.valueOf(command.getScale()));
        element.setAttribute("designsize", String.valueOf(command
                .getDesignsize()));
        if (showPT) {
            element.setAttribute("scalefactor_pt", Unit
                    .getDimenAsPTString(new Dimen(command.getScale())));
            element.setAttribute("designsize_pt", Unit
                    .getDimenAsPTString(new Dimen(command.getDesignsize())));
        }
        element
                .setAttribute("scaled", command.getScaledAsCount(mag)
                        .toString());
        element.setAttribute("area", command.getArea());
        element.setAttribute("name", command.getFName());
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
            //System.out.println("mist, einen vergessen opcode:"
            //        + command.getOpcode());

        }
    }

    /**
     * Read the next command as element.
     * All commands of a 'bop' are collected in one element.
     * <p>
     * This method ist for large dvi-files, to read each page in
     * bop-element separretly.
     * </p>
     *
     * @param rar           the input
     * @return Returns the Element for the command
     * @throws IOException   if an IO-error occurs.
     * @throws DviException  if a DVI-error occurs.
     * @throws FontException if a font-error occurs.
     * @throws ConfigurationException from the config systen.
     */
    public Element readNextElement(final RandomAccessR rar) throws IOException,
            DviException, FontException, ConfigurationException {

        // store old parent
        Element old = parent;
        parent = null;

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
                break;
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
                break;
            } else if (command instanceof DviPostPost) {
                DviPostPost cc = (DviPostPost) command;
                execute(cc);
                break;
            } else if (command instanceof DviPre) {
                DviPre cc = (DviPre) command;
                execute(cc);
                break;
            }
        }
        Element rt = parent;
        // restore parent
        parent = old;
        return rt;
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
     * Set the values in the xml-element.
     * @param element   the element
     */
    private void setValues(final Element element) {

        element.setAttribute("h", String.valueOf(val.getH()));
        element.setAttribute("v", String.valueOf(val.getV()));
        element.setAttribute("w", String.valueOf(val.getW()));
        element.setAttribute("x", String.valueOf(val.getX()));
        element.setAttribute("y", String.valueOf(val.getY()));
        element.setAttribute("z", String.valueOf(val.getZ()));
        if (showPT) {
            element.setAttribute("h_pt", Unit.getDimenAsPTString(new Dimen(val
                    .getH())));
            element.setAttribute("v_pt", Unit.getDimenAsPTString(new Dimen(val
                    .getV())));
            element.setAttribute("w_pt", Unit.getDimenAsPTString(new Dimen(val
                    .getW())));
            element.setAttribute("x_pt", Unit.getDimenAsPTString(new Dimen(val
                    .getX())));
            element.setAttribute("y_pt", Unit.getDimenAsPTString(new Dimen(val
                    .getY())));
            element.setAttribute("z_pt", Unit.getDimenAsPTString(new Dimen(val
                    .getZ())));
        }
    }

    /**
     * Set the Font and Glyph-Info.
     * @param opcode    the opcode (char-id)
     * @param element   the element
     * @throws DviMissingFontException  if the font is missinbg.
     */
    private void setFontGlyphInfo(final int opcode, final Element element)
            throws DviMissingFontException {

        Font font = getFont();
        if (font != null) {
            element.setAttribute("font", font.getFontName());
            Glyph glyph = font.getGlyph(new UnicodeChar(opcode));
            Dimen h = glyph.getHeight();
            Dimen d = glyph.getDepth();
            Dimen w = glyph.getWidth();
            element.setAttribute("height", String.valueOf(h.getValue()));
            element.setAttribute("depth", String.valueOf(d.getValue()));
            element.setAttribute("width", String.valueOf(w.getValue()));
            if (showPT) {
                element.setAttribute("height_pt", Unit.getDimenAsPTString(h));
                element.setAttribute("depth_pt", Unit.getDimenAsPTString(d));
                element.setAttribute("width_pt", Unit.getDimenAsPTString(w));
            }
            val.addH((int) w.getValue());
        }
    }

    /**
     * Set the value for showPT.
     * @param show The showPT to set.
     */
    public void setShowPT(final boolean show) {

        showPT = show;
    }

    /**
     * Set the fontmap.
     * @param fontm The fontmap to set.
     */
    public void setFontmap(Map fontm) {

        fontmap = fontm;
    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviBOP)
     */
    public void execute(final DviBOP command) throws DviException,
            FontException, ConfigurationException {

        page++;
        val.clear();
        stack.clear();

        Element element = createElement(command);
        int[] c = command.getC();
        for (int i = 0; i < c.length; i++) {
            element.setAttribute("c" + String.valueOf(i), String.valueOf(c[i]));
        }
        element.setAttribute("p", String.valueOf(command.getP()));
        element.setAttribute("page", String.valueOf(page));
        if (parent == null) {
            parent = element;
        } else {
            parent.addContent(element);
        }
        parentstack.push(parent);
        parent = element;
        val.clear();

    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviChar)
     */
    public void execute(final DviChar command) throws DviException,
            FontException, ConfigurationException {

        Integer key = new Integer(val.getF());
        Font f = (Font) fontmap.get(key);
        if (f == null) {
            throw new DviFontNotFoundException(command.getName());
        }
        Glyph g = f.getGlyph(new UnicodeChar(command.getCh()));
        if (g == null) {
            throw new DviGlyphNotFoundException(String.valueOf(command.getCh()));
        }
        Dimen width = g.getWidth();

        Element element = createElement(command);
        element.setAttribute("char", String.valueOf(command.getCh()));
        setValues(element);
        setFontGlyphInfo(command.getOpcode(), element);
        if (parent == null) {
            parent = element;
        } else {
            parent.addContent(element);
        }

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

        Element element = createElement(command);
        element.setAttribute("value", String.valueOf(command.getValue()));
        setValues(element);
        val.addV(command.getValue());
        if (parent == null) {
            parent = element;
        } else {
            parent.addContent(element);
        }

    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviEOP)
     */
    public void execute(final DviEOP command) throws DviException,
            FontException, ConfigurationException {

        Element element = createElement(command);
        if (parent == null) {
            parent = element;
        } else {
            parent.addContent(element);
        }
        if (parentstack.empty()) {
            throw new DviBopEopException();
        }
        parent = (Element) parentstack.pop();

    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviFntDef)
     */
    public void execute(final DviFntDef command) throws DviException,
            FontException, ConfigurationException {

        Element element = createElement(command);
        addFontAttributes(command, element);
        loadFont(command);
        if (parent == null) {
            parent = element;
        } else {
            parent.addContent(element);
        }

    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviFntNum)
     */
    public void execute(final DviFntNum command) throws DviException,
            FontException, ConfigurationException {

        Element element = createElement(command);
        element.setAttribute("id", String.valueOf(command.getFont()));
        if (parent == null) {
            parent = element;
        } else {
            parent.addContent(element);
        }
        val.setF(command.getFont());

    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviPOP)
     */
    public void execute(final DviPOP command) throws DviException,
            FontException, ConfigurationException {

        Element element = createElement(command);
        DviValues v = stack.pop();
        val.setValues(v);
        setValues(element);
        if (parent == null) {
            parent = element;
        } else {
            parent.addContent(element);
        }

    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviNOP)
     */
    public void execute(final DviNOP command) throws DviException,
            FontException, ConfigurationException {

        Element element = createElement(command);
        if (parent == null) {
            parent = element;
        } else {
            parent.addContent(element);
        }

    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviPost)
     */
    public void execute(final DviPost command) throws DviException,
            FontException, ConfigurationException {

        Element element = createElement(command);
        element.setAttribute("p", String.valueOf(command.getPointer()));
        element.setAttribute("num", String.valueOf(command.getNum()));
        element.setAttribute("den", String.valueOf(command.getDen()));
        element.setAttribute("mag", String.valueOf(command.getMag()));
        element.setAttribute("heightplusdepth", String.valueOf(command
                .getHeigthdepth()));
        element.setAttribute("widthwidest", String.valueOf(command.getWidth()));
        element.setAttribute("stackdepth", String.valueOf(command
                .getStackdepth()));
        element.setAttribute("totalpage", String
                .valueOf(command.getTotalpage()));
        if (parent == null) {
            parent = element;
        } else {
            parent.addContent(element);
        }
        parentstack.push(parent);
        parent = element;

    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviPostPost)
     */
    public void execute(final DviPostPost command) throws DviException,
            FontException, ConfigurationException {

        if (parentstack.empty()) {
            throw new DviBopEopException();
        }
        parent = (Element) parentstack.pop();

        Element element = createElement(command);
        element.setAttribute("q", String.valueOf(command.getPointer()));
        element.setAttribute("identifies", String.valueOf(command
                .getIdentification()));
        if (parent == null) {
            parent = element;
        } else {
            parent.addContent(element);
        }

    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviPre)
     */
    public void execute(final DviPre command) throws DviException,
            FontException, ConfigurationException {

        mag = command.getMag();
        Element element = createElement(command);
        element.setAttribute("identifies", String.valueOf(command
                .getIdentifier()));
        element.setAttribute("num", String.valueOf(command.getNum()));
        element.setAttribute("den", String.valueOf(command.getDen()));
        element.setAttribute("mag", String.valueOf(command.getMag()));
        if (command.getComment().length() > 0) {
            Element comment = new Element("comment");
            comment.setText(command.getComment());
            element.addContent(comment);
        }
        if (parent == null) {
            parent = element;
        } else {
            parent.addContent(element);
        }

    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviPush)
     */
    public void execute(final DviPush command) throws DviException,
            FontException, ConfigurationException {

        Element element = createElement(command);
        setValues(element);
        if (parent == null) {
            parent = element;
        } else {
            parent.addContent(element);
        }
        stack.push(val);

    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviRight)
     */
    public void execute(final DviRight command) throws DviException,
            FontException, ConfigurationException {

        Element element = createElement(command);
        element.setAttribute("value", String.valueOf(command.getValue()));
        setValues(element);
        val.addH(command.getValue());
        if (parent == null) {
            parent = element;
        } else {
            parent.addContent(element);
        }

    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviRule)
     */
    public void execute(final DviRule command) throws DviException,
            FontException, ConfigurationException {

        Element element = createElement(command);
        element.setAttribute("height", String.valueOf(command.getHeight()));
        element.setAttribute("width", String.valueOf(command.getWidth()));
        setValues(element);
        if (!command.isPut()) {
            val.addH(command.getWidth());
        }
        if (parent == null) {
            parent = element;
        } else {
            parent.addContent(element);
        }

    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviW)
     */
    public void execute(final DviW command) throws DviException, FontException,
            ConfigurationException {

        Element element = createElement(command);
        setValues(element);
        if (!command.isW0()) {
            val.setW(command.getValue());
            element.setAttribute("value", String.valueOf(command.getValue()));
        }
        val.addH(val.getW());
        if (parent == null) {
            parent = element;
        } else {
            parent.addContent(element);
        }

    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviX)
     */
    public void execute(final DviX command) throws DviException, FontException,
            ConfigurationException {

        Element element = createElement(command);
        setValues(element);
        if (!command.isX0()) {
            val.setX(command.getValue());
            element.setAttribute("value", String.valueOf(command.getValue()));
        }
        val.addH(val.getX());
        if (parent == null) {
            parent = element;
        } else {
            parent.addContent(element);
        }

    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviXXX)
     */
    public void execute(final DviXXX command) throws DviException,
            FontException, ConfigurationException {

        Element element = createElement(command);
        element.setText(command.getXXXString());
        if (parent == null) {
            parent = element;
        } else {
            parent.addContent(element);
        }

    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviY)
     */
    public void execute(final DviY command) throws DviException, FontException,
            ConfigurationException {

        Element element = createElement(command);
        setValues(element);
        if (!command.isY0()) {
            val.setY(command.getValue());
            element.setAttribute("value", String.valueOf(command.getValue()));
        }
        val.addV(val.getY());
        if (parent == null) {
            parent = element;
        } else {
            parent.addContent(element);
        }

    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviZ)
     */
    public void execute(final DviZ command) throws DviException, FontException,
            ConfigurationException {

        Element element = createElement(command);
        setValues(element);
        if (!command.isZ0()) {
            val.setZ(command.getValue());
            element.setAttribute("value", String.valueOf(command.getValue()));
        }
        val.addV(val.getZ());
        if (parent == null) {
            parent = element;
        } else {
            parent.addContent(element);
        }

    }

    /**
     * Create the element from a dvicommand.
     * @param command   the command
     * @return Returns the element.
     */
    private Element createElement(final DviCommand command) {

        Element e = new Element(command.getName());
        e.setAttribute("opcode", String.valueOf(command.getOpcode()));
        return e;
    }
}
