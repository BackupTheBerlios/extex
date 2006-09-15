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

package de.dante.dviware.dvitype;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.dante.dviware.Dvi;
import de.dante.dviware.DviProcessor;
import de.dante.extex.main.TeX;
import de.dante.extex.main.logging.LogFormatter;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.ConfigurationFactory;
import de.dante.util.resource.ResourceFinder;
import de.dante.util.resource.ResourceFinderFactory;

/**
 * This class provides a command line tool to disassemble a DVI file.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class DviDisassemble implements DviProcessor {

    /**
     * The field <tt>condensed</tt> contains the indicator that sequences of
     * put_char instructions should be condensed.
     */
    private static boolean condensed = true;

    /**
     * The field <tt>hexLabel</tt> contains the indicator that the label should
     * be presented as hex number.
     */
    private static boolean hexLabel = true;

    /**
     * The constant <tt>PROP_CONFIG</tt> contains the name of the property for
     * the configuration resource to use.
     */
    protected static final String PROP_CONFIG = "extex.config";

    /**
     * The field <tt>showLabel</tt> contains the indicator that the label
     * should be shown as labels.
     */
    private static boolean showLabel = true;

    /**
     * The command line interface to dvitype.
     *
     * @param args the command line arguments
     */
    public static void main(final String[] args) {

        Logger logger = Logger.getLogger(TeX.class.getName());
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);

        Handler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new LogFormatter());
        consoleHandler.setLevel(Level.WARNING);
        logger.addHandler(consoleHandler);

        for (int i = 0; i < args.length; i++) {
            String a = args[i];
            if (a.startsWith("-decimal")) {
                hexLabel = false;
            } else if (a.startsWith("-nolabel")) {
                showLabel = false;
            } else if (a.startsWith("-uncondensed")) {
                condensed = false;
            } else {
                process(a, logger);
            }
        }
    }

    /**
     * Process an input file.
     *
     * @param arg the resource name to process
     * @param logger the logger
     */
    protected static void process(final String arg, final Logger logger) {

        Properties properties = System.getProperties();
        properties.setProperty(PROP_CONFIG, "config/extex");

        Configuration config;
        ResourceFinder finder;
        try {
            config = new ConfigurationFactory().newInstance(properties
                    .getProperty(PROP_CONFIG));
            finder = new ResourceFinderFactory().createResourceFinder(config
                    .getConfiguration("Resource"), logger, properties);
            InputStream dvi = finder.findResource(arg, "dvi");

            if (dvi == null) {
                logger.severe("Resource `" + arg + "' not found");
                return;
            }
            new Dvi(dvi).parse(new DviDisassemble(System.out));

            dvi.close();
        } catch (Exception e) {
            logger.throwing("DviDisassemble", "process", e);
        }
    }

    /**
     * The field <tt>inString</tt> contains the indicator that a sequence of
     * characters has already been begun.
     */
    private boolean inString = false;

    /**
     * The field <tt>out</tt> contains the output stream.
     */
    private PrintStream out;

    /**
     * Creates a new object.
     *
     * @param out the output stream
     */
    public DviDisassemble(final PrintStream out) {

        super();
        this.out = out;
    }

    /**
     * @see de.dante.dviware.DviProcessor#bop(int, int[], int)
     */
    public void bop(final int off, final int[] c, final int p) {

        printLabel(off);
        out.print("bop ");
        out.print(c[0]);
        out.print(' ');
        out.print(c[1]);
        out.print(' ');
        out.print(c[2]);
        out.print(' ');
        out.print(c[3]);
        out.print(' ');
        out.print(c[4]);
        out.print(' ');
        out.print(c[5]);
        out.print(' ');
        out.print(c[6]);
        out.print(' ');
        out.print(c[7]);
        out.print(' ');
        out.print(c[8]);
        out.print(' ');
        out.print(c[9]);
        out.print(" 0x");
        out.println(Integer.toHexString(p));
    }

    /**
     * @see de.dante.dviware.DviProcessor#down(int, int)
     */
    public void down(final int off, final int b) {

        printLabel(off);
        out.print("down ");
        out.println(b);
    }

    /**
     * @see de.dante.dviware.DviProcessor#eop(int)
     */
    public void eop(final int off) {

        printLabel(off);
        out.println("eop");
    }

    /**
     * @see de.dante.dviware.DviProcessor#fnt(int, int)
     */
    public void fnt(final int off, final int k) {

        printLabel(off);
        out.print("fnt ");
        out.println(k);
    }

    /**
     * @see de.dante.dviware.DviProcessor#fntDef(int, int, int, int, int, java.lang.String)
     */
    public void fntDef(final int off, final int k, final int c, final int s,
            final int d, final String name) {

        printLabel(off);
        out.print("fnt_def ");
        out.print(k);
        out.print(' ');
        out.print(c);
        out.print(' ');
        out.print(s);
        out.print(' ');
        out.print(d);
        out.print(' ');
        out.print('"');
        out.print(name);
        out.println('"');
    }

    /**
     * @see de.dante.dviware.DviProcessor#nop(int)
     */
    public void nop(final int off) {

        printLabel(off);
        out.println("nop");
    }

    /**
     * @see de.dante.dviware.DviProcessor#pop(int)
     */
    public void pop(final int off) {

        printLabel(off);
        out.println("pop");
    }

    /**
     * @see de.dante.dviware.DviProcessor#post(int, int, int, int, int, int, int, int, int)
     */
    public void post(final int off, final int p, final int num, final int den,
            final int mag, final int l, final int u, final int sp, final int tp) {

        printLabel(off);
        out.print("post 0x");
        out.print(Integer.toHexString(p));
        out.print(' ');
        out.print(num);
        out.print(' ');
        out.print(den);
        out.print(' ');
        out.print(mag);
        out.print(' ');
        out.print(l);
        out.print(' ');
        out.print(u);
        out.print(' ');
        out.print(sp);
        out.print(' ');
        out.println(tp);
    }

    /**
     * @see de.dante.dviware.DviProcessor#postPost(int, int, int)
     */
    public void postPost(final int off, final int bop, final int id) {

        printLabel(off);
        out.print("post_post 0x");
        out.print(Integer.toHexString(bop));
        out.print(' ');
        out.println(id);
    }

    /**
     * @see de.dante.dviware.DviProcessor#pre(int, int, int, int, int, java.lang.String)
     */
    public void pre(final int off, final int id, final int num, final int den,
            final int mag, final String comment) {

        printLabel(off);
        out.print("pre ");
        out.print(id);
        out.print(' ');
        out.print(num);
        out.print(' ');
        out.print(den);
        out.print(' ');
        out.print(mag);
        out.print(' ');
        out.print('"');
        out.print(comment);
        out.println('"');
    }

    /**
     * Print the prefix before the opcode.
     *
     * @param off the label, i.e. the address of the opcode
     */
    private void printLabel(final int off) {

        if (inString) {
            inString = false;
            out.print("\"\n");
        }

        if (!showLabel) {
            // ignore
        } else if (hexLabel) {
            String s = Integer.toHexString(off);
            switch (s.length()) {
                case 0:
                    out.print('0');
                case 1:
                    out.print('0');
                case 2:
                    out.print('0');
                case 3:
                    out.print('0');
                default:
            // continue
            }
            out.print(s);
        } else {
            out.print(Integer.toString(off));
        }
        out.print('\t');
    }

    /**
     * @see de.dante.dviware.DviProcessor#push(int)
     */
    public void push(final int off) {

        printLabel(off);
        out.println("push");
    }

    /**
     * @see de.dante.dviware.DviProcessor#putChar(int, int)
     */
    public void putChar(final int off, final int c) {

        printLabel(off);
        out.print("put_char ");
        out.print(Integer.toString(c));
        out.print("\t\t\t; ");
        out.println(Character.toString((char) c));
    }

    /**
     * @see de.dante.dviware.DviProcessor#putRule(int, int, int)
     */
    public void putRule(final int off, final int a, final int b) {

        printLabel(off);
        out.print("put_rule ");
        out.print(Integer.toString(a));
        out.print(' ');
        out.println(Integer.toString(b));
    }

    /**
     * @see de.dante.dviware.DviProcessor#right(int, int)
     */
    public void right(final int off, final int b) {

        printLabel(off);
        out.print("right ");
        out.println(b);
    }

    /**
     * @see de.dante.dviware.DviProcessor#setChar(int, int)
     */
    public void setChar(final int off, final int c) {

        if (condensed) {
            if (!inString) {
                printLabel(off);
                out.print("\"");
                inString = true;
            }
            out.print(Character.toString((char) c));
        } else {
            printLabel(off);
            out.print("set_char_");
            out.print(Integer.toString(c));
            out.print("\t\t\t; ");
            out.println(Character.toString((char) c));
        }
    }

    /**
     * @see de.dante.dviware.DviProcessor#setRule(int, int, int)
     */
    public void setRule(final int off, final int a, final int b) {

        printLabel(off);
        out.print("set_rule ");
        out.print(Integer.toString(a));
        out.print(' ');
        out.println(Integer.toString(b));
    }

    /**
     * @see de.dante.dviware.DviProcessor#undef(int, int, java.io.InputStream)
     */
    public void undef(final int off, final int c, final InputStream stream) {

        printLabel(off);
        out.println("0x");
        out.println(Integer.toHexString(c));
    }

    /**
     * @see de.dante.dviware.DviProcessor#w(int, int)
     */
    public void w(final int off, final int b) {

        printLabel(off);
        out.print("w ");
        out.println(b);
    }

    /**
     * @see de.dante.dviware.DviProcessor#w0(int)
     */
    public void w0(final int off) {

        printLabel(off);
        out.println("w0");
    }

    /**
     * @see de.dante.dviware.DviProcessor#x(int, int)
     */
    public void x(final int off, final int b) {

        printLabel(off);
        out.print("x ");
        out.println(b);
    }

    /**
     * @see de.dante.dviware.DviProcessor#x0(int)
     */
    public void x0(final int off) {

        printLabel(off);
        out.println("x0");
    }

    /**
     * @see de.dante.dviware.DviProcessor#xxx(int, byte[])
     */
    public void xxx(final int off, final byte[] x) {

        printLabel(off);
        out.print("xxx ");
        out.print('"');
        out.print(x);
        out.println('"');
    }

    /**
     * @see de.dante.dviware.DviProcessor#y(int, int)
     */
    public void y(final int off, final int b) {

        printLabel(off);
        out.print("y ");
        out.println(b);
    }

    /**
     * @see de.dante.dviware.DviProcessor#y0(int)
     */
    public void y0(final int off) {

        printLabel(off);
        out.println("y0");
    }

    /**
     * @see de.dante.dviware.DviProcessor#z(int, int)
     */
    public void z(final int off, final int b) {

        printLabel(off);
        out.print("z ");
        out.println(b);
    }

    /**
     * @see de.dante.dviware.DviProcessor#z0(int)
     */
    public void z0(final int off) {

        printLabel(off);
        out.println("z0");
    }

}
