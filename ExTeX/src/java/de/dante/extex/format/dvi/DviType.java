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
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.dante.extex.font.FontFactory;
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
import de.dante.extex.format.dvi.exception.DviException;
import de.dante.extex.format.dvi.exception.DviFontNotFoundException;
import de.dante.extex.format.dvi.exception.DviGlyphNotFoundException;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.util.UnicodeChar;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.file.random.RandomAccessR;

/**
 * DviType.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class DviType implements DviInterpreter, DviExecuteCommand {

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
     * the writer
     */
    private PrintWriter w;

    /**
     * the magnification
     */
    private int mag;

    /**
     * dpi (default)
     */
    private static final double DEFAULTDPI = 300.0d;

    /**
     * resolution in dpi
     */
    private double resolution = DEFAULTDPI;

    /**
     * Create a new object.
     *
     * @param element   the root element
     * @param ff        the fontfactroy
     */
    public DviType(final PrintWriter wout, final FontFactory ff) {

        w = wout;
        fontfactory = ff;
        fontmap = new HashMap();
        val = new DviValues();
        stack = new DviStack();
    }

    /**
     * Load the font.
     *
     * @throws DviException if a font not found.
     * @throws FontException if an font-error occured.
     * @throws ConfigurationException from the config-system.
     */
    private void loadFont() throws DviException, FontException,
            ConfigurationException {

        for (int i = 0; i < fntdefs.size(); i++) {
            DviFntDef command = (DviFntDef) fntdefs.get(i);

            Integer key = new Integer(command.getFont());
            Dimen designsize = command.getScaleAsDimen();
            Count scale = command.getScaledAsCount(mag);
            String name = command.getFName();

            Font f = fontfactory.getInstance(name, designsize, scale, new Glue(
                    0), true, true);
            if (f == null) {
                throw new DviFontNotFoundException(name);
            }
            fontmap.put(key, f);
        }
    }

    /**
     * the post command
     */
    private DviPost post;

    /**
     * the pre command
     */
    private DviPre pre;

    /**
     * the fnt_def commands
     */
    private List fntdefs;

    /**
     * format to 8 digit
     */
    private static final int FORM8 = 8;

    /**
     * NUM 
     */
    private static final double NUM = 254000.0d;

    /**
     * The conversion factor conv is figured as follows:
     * There are exactly n/d decimicrons per DVI unit,
     * and 254000 decimicrons per inch, and resolution pixels per inch.
     * Then we have to adjust this by the stated amount of magnification.
     */
    private double conv;

    /**
     * @see de.dante.extex.format.dvi.DVIInterpreter#interpret(
     *      de.dante.util.file.random.RandomAccessR)
     */
    public void interpret(final RandomAccessR rar) throws IOException,
            DviException, FontException, ConfigurationException {

        // read post
        post = DviCommand.getPost(rar);

        // calculate conv
        conv = (post.getNum() / NUM) * (resolution / post.getDen());

        // read the fnt_def commands
        fntdefs = DviCommand.getFntDefs(rar);

        // read pre
        pre = DviCommand.getPre(rar);

        printHeader();
        printFont();
        loadFont();
        w.println();

        // reset pointer
        rar.seek(0);

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
            System.out.println("mist, einen vergessen opcode:"
                    + command.getOpcode());

        }
    }

    /**
     * round the pixel
     * @param v thje value
     * @return Returns the rounded value
     */
    private int pixelround(final int v) {

        return (int) Math.round(v * conv);
    }

    /**
     * mag (fefault)
     */
    private static final int MAGDEFAULT = 1000;

    /**
     * mag div
     */
    private static final int MAGDIV = 10;

    /**
     * print the font
     */
    private void printFont() {

        for (int i = 0; i < fntdefs.size(); i++) {
            DviFntDef f = (DviFntDef) fntdefs.get(i);

            w.print("Font ");
            w.print(f.getFont());
            w.print(": ");
            w.print(f.getFName());
            if (f.getScaled(post.getMag()) != MAGDEFAULT) {
                w.print(" scaled ");
                w.print(f.getScaled(post.getMag()));
            }
            w.print("---loaded at size ");
            w.print(f.getScale());
            w.println(" DVI units");
            if (f.getScaled(post.getMag()) != MAGDEFAULT) {
                w.print(" (this font is magnified ");
                w.print(f.getScaled(post.getMag()) / MAGDIV);
                w.println("%)");
            }
        }
    }

    /**
     * print the header
     */
    private void printHeader() {

        NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
        nf.setMinimumFractionDigits(FORM8);
        w.println("This is DVItype, Version ExTeX");
        w.print("Resolution = ");
        w.print(nf.format(resolution));
        w.println(" pixels per inch");
        w.print("numerator/denominator=");
        w.print(post.getNum());
        w.print("/");
        w.print(post.getDen());
        w.println();
        w.print("magnification=");
        w.print(post.getMag());
        w.print(";       ");
        w
                .print(nf.format((post.getNum() / NUM)
                        * (resolution / post.getDen())));
        w.println(" pixels per DVI unit");
        w.print("'");
        w.print(pre.getComment());
        w.println("'");
        w.print("Postamble starts at byte ");
        w.print(post.getStartPointer());
        w.println(".");
        w.print("maxv=");
        w.print(post.getHeigthdepth());
        w.print(", maxh=");
        w.print(post.getWidth());
        w.print(", maxstackdepth=");
        w.print(post.getStackdepth());
        w.print(", totalpages=");
        w.println(post.getTotalpage());
    }

    /**
     * Print the values.
     */
    private void printValues() {

        w.print("level ");
        w.print(stack.size() - 1);
        w.print(":(h=");
        w.print(val.getH());
        w.print(",v=");
        w.print(val.getV());
        w.print(",w=");
        w.print(val.getW());
        w.print(",x=");
        w.print(val.getX());
        w.print(",y=");
        w.print(val.getY());
        w.print(",z=");
        w.print(val.getZ());
        w.print(",hh=");
        w.print(pixelround(val.getH()));
        w.print(",vv=");
        w.print(pixelround(val.getV()));
        w.println(")");
    }

    //------------------------------------------------
    //------------------------------------------------
    //------------------------------------------------
    //------------------------------------------------
    //------------------------------------------------

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviBOP)
     */
    public void execute(final DviBOP command) throws DviException,
            FontException, ConfigurationException {

        page++;
        val.clear();
        stack.clear();
        w.print(command.getStartPointer());
        w.print(": beginning of page ");
        w.println(page);
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

        int oldh = val.getH();
        if (!command.isPut()) {
            val.addH((int) width.getValue());
        }
        // TODO incomplete put...
        w.print(command.getStartPointer());
        w.print(": ");
        w.print(command.getName());
        w.print(" h:=");
        w.print(oldh);
        w.print(width.getValue() >= 0 ? "+" : "");
        w.print(width.getValue());
        w.print("=");
        w.print(val.getH());
        w.print(", hh:=");
        w.println(pixelround(val.getH()));
    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviDown)
     */
    public void execute(final DviDown command) throws DviException,
            FontException, ConfigurationException {

        int oldv = val.getV();
        val.addV(command.getValue());
        w.print(command.getStartPointer());
        w.print(": ");
        w.print(command.getName());
        w.print(" ");
        w.print(command.getValue());
        w.print(" v:=");
        w.print(oldv);
        w.print(command.getValue() >= 0 ? "+" : "");
        w.print(command.getValue());
        w.print("=");
        w.print(val.getV());
        w.print(", vv:=");
        w.println(pixelround(val.getV()));
    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviEOP)
     */
    public void execute(final DviEOP command) throws DviException,
            FontException, ConfigurationException {

        w.print(command.getStartPointer());
        w.print(": ");
        w.println(command.getName());
        w.println();
    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviFntDef)
     */
    public void execute(final DviFntDef command) throws DviException,
            FontException, ConfigurationException {

        if (!executepost) {
            w.print(command.getStartPointer());
            w.print(": ");
            w.print(command.getName());
            w.print(" ");
            w.print(command.getFont());
            w.print(": ");
            w.println(command.getFName());
        }
    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviFntNum)
     */
    public void execute(final DviFntNum command) throws DviException,
            FontException, ConfigurationException {

        w.print(command.getStartPointer());
        w.print(": ");
        w.print(command.getName());
        w.print(" current font is ");
        val.setF(command.getFont());
        Integer key = new Integer(val.getF());
        Font f = (Font) fontmap.get(key);
        if (f == null) {
            throw new DviFontNotFoundException(command.getName());
        }
        w.println(f.getFontName());
    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviPOP)
     */
    public void execute(final DviPOP command) throws DviException,
            FontException, ConfigurationException {

        w.print(command.getStartPointer());
        w.print(": ");
        w.println(command.getName());
        printValues();
        DviValues newval = stack.pop();
        val.setValues(newval);
    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviNOP)
     */
    public void execute(final DviNOP command) throws DviException,
            FontException, ConfigurationException {

        w.print(command.getStartPointer());
        w.print(": ");
        w.println(command.getName());

    }

    /**
     * execute psot
     */
    private boolean executepost = false;

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviPost)
     */
    public void execute(final DviPost command) throws DviException,
            FontException, ConfigurationException {

        executepost = true;
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
     *      de.dante.extex.format.dvi.command.DviPUSH)
     */
    public void execute(final DviPush command) throws DviException,
            FontException, ConfigurationException {

        stack.push(val);
        w.print(command.getStartPointer());
        w.print(": ");
        w.println(command.getName());
        printValues();
    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviRight)
     */
    public void execute(final DviRight command) throws DviException,
            FontException, ConfigurationException {

        int oldh = val.getH();
        val.addH(command.getValue());
        w.print(command.getStartPointer());
        w.print(": ");
        w.print(command.getName());
        w.print(" ");
        w.print(command.getValue());
        w.print(" h:=");
        w.print(oldh);
        w.print(command.getValue() >= 0 ? "+" : "");
        w.print(command.getValue());
        w.print("=");
        w.print(val.getH());
        w.print(", hh:=");
        w.println(pixelround(val.getH()));
    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviRule)
     */
    public void execute(final DviRule command) throws DviException,
            FontException, ConfigurationException {

        w.print(command.getStartPointer());
        w.print(": ");
        w.print(command.getName());
        w.print(" height ");
        w.print(command.getHeight());
        w.print(", width ");
        w.println(command.getWidth());
        // TODO incomplete
        if (!command.isPut()) {
            val.addH(command.getWidth());
        }
        //        3274: putrule height 26214, width 9305970 (2x590 pixels) 

    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviW)
     */
    public void execute(final DviW command) throws DviException, FontException,
            ConfigurationException {

        int oldh = val.getH();
        if (!command.isW0()) {
            val.setW(command.getValue());
        }
        val.addH(val.getW());
        w.print(command.getStartPointer());
        w.print(": ");
        w.print(command.getName());
        w.print(" ");
        w.print(val.getW());
        w.print(" h:=");
        w.print(oldh);
        w.print(val.getW() >= 0 ? "+" : "-");
        w.print(val.getW());
        w.print("=");
        w.print(val.getH());
        w.print(", hh:=");
        w.println(pixelround(val.getH()));
    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviX)
     */
    public void execute(final DviX command) throws DviException, FontException,
            ConfigurationException {

        int oldh = val.getH();
        if (!command.isX0()) {
            val.setX(command.getValue());
        }
        val.addH(command.getValue());
        w.print(command.getStartPointer());
        w.print(": ");
        w.print(command.getName());
        w.print(" ");
        w.print(val.getX());
        w.print(" h:=");
        w.print(oldh);
        w.print(val.getX() >= 0 ? "+" : "");
        w.print(val.getX());
        w.print("=");
        w.print(val.getH());
        w.print(", hh:=");
        w.println(pixelround(val.getH()));
    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviXXX)
     */
    public void execute(final DviXXX command) throws DviException,
            FontException, ConfigurationException {

        w.print(command.getStartPointer());
        w.print(": ");
        w.print(command.getName());
        w.print(" '");
        w.print(command.getXXXString());
        w.println("'");
    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviY)
     */
    public void execute(final DviY command) throws DviException, FontException,
            ConfigurationException {

        int oldv = val.getV();
        if (!command.isY0()) {
            val.setY(command.getValue());
        }
        val.addV(val.getY());
        w.print(command.getStartPointer());
        w.print(": ");
        w.print(command.getName());
        w.print(" ");
        w.print(val.getY());
        w.print(" v:=");
        w.print(oldv);
        w.print(val.getY() >= 0 ? "+" : "-");
        w.print(val.getY());
        w.print("=");
        w.print(val.getV());
        w.print(", vv:=");
        w.println(pixelround(val.getV()));

    }

    /**
     * @see de.dante.extex.format.dvi.command.DviExecuteCommand#execute(
     *      de.dante.extex.format.dvi.command.DviZ)
     */
    public void execute(final DviZ command) throws DviException, FontException,
            ConfigurationException {

        int oldv = val.getV();
        if (!command.isZ0()) {
            val.setZ(command.getValue());
        }
        val.addV(val.getZ());
        w.print(command.getStartPointer());
        w.print(": ");
        w.print(command.getName());
        w.print(" ");
        w.print(val.getZ());
        w.print(" v:=");
        w.print(oldv);
        w.print(val.getZ() >= 0 ? "+" : "-");
        w.print(val.getZ());
        w.print("=");
        w.print(val.getV());
        w.print(", vv:=");
        w.println(pixelround(val.getV()));

    }
}
